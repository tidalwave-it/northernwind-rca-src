/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
 * %%
 * Copyright (C) 2011 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * *********************************************************************************************************************
 *
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.model.impl.admin.role;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.Finder;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.PresentationModelProvider;
import it.tidalwave.role.ui.spi.DefaultPresentationModel;
import it.tidalwave.util.AsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class CompositePresentationModelProvider<T extends As> implements PresentationModelProvider
  {
    @Nonnull
    private final T datum;

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public PresentationModel createPresentationModel (final @Nonnull Object ... rolesOrFactories)
      {
        log.trace("createPresentationModel({})", rolesOrFactories);
        return internalCreatePresentationModel(datum, new ArrayList<>(Arrays.asList(rolesOrFactories)));
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private PresentationModel internalCreatePresentationModel (final @Nonnull T datum,
                                                               final @Nonnull List<Object> rolesOrFactories)
      {
        final List<Object> roles = resolveRoles(datum, rolesOrFactories);

        roles.add(new SimpleComposite<PresentationModel>()
          {
            @Override @Nonnull
            public Finder<PresentationModel> findChildren()
              {
                return new SimpleFinderSupport<PresentationModel>()
                  {
                    @Override
                    protected List<? extends PresentationModel> computeResults()
                      {
                        final List<PresentationModel> results = new ArrayList<>();

                        try
                          {
                            final SimpleComposite<T> composite = datum.as(SimpleComposite.class);

                            for (final T child : composite.findChildren().results())
                              {
                                results.add(internalCreatePresentationModel(child, rolesOrFactories));
                              }
                          }
                        catch (AsException e)
                          {
                            // ok, no Composite role
                          }

                        return results;
                      }
                  };
              }
          });

        log.trace(">>>> roles for {}: {}", datum, roles);

        return new DefaultPresentationModel(datum, roles.toArray());
      }

    @Nonnull
    private List<Object> resolveRoles (final @Nonnull T datum,
                                       final @Nonnull List<Object> rolesOrFactories)
      {
        final List<Object> roles = new ArrayList<>();

        for (final Object roleOrFactory : rolesOrFactories)
          {
            if (roleOrFactory instanceof RoleFactory)
              {
                roles.add(((RoleFactory<T>)roleOrFactory).createRoleFor(datum));
              }
            else
              {
                roles.add(roleOrFactory);
              }
          }

        return roles;
      }
  }
