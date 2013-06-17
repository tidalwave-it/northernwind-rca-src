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
package it.tidalwave.northernwind.rca.ui;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.Finder;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class PresentationModelUtil
  {
    @Nonnull
    public <T extends As> PresentationModel createPresentationModel (final @Nonnull T datum,
                                                                     final @Nonnull RoleFactory<T> roleFactory,
                                                                     final @Nonnull Object ... roles)
      {
        return new DefaultPresentationModel(datum, roleFactory.createRoleFor(datum),
                new SimpleComposite<PresentationModel>()
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
                        final SimpleComposite<T> composite = datum.as(SimpleComposite.class);

                        for (final T object : composite.findChildren().results())
                          {
                            results.add(createPresentationModel(object, roleFactory, roles));
                          }

                        return results;
                      }
                  };
              }
          });
      }
  }
