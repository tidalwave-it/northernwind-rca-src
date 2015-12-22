/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import it.tidalwave.util.Id;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.role.spi.DefaultSimpleComposite;
import it.tidalwave.role.spi.MapAggregate;
import it.tidalwave.role.ui.Presentable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.spi.DefaultPresentationModel;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A provider of a {@link PresentationModel} for {@link ResourceProperties}.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datumType = ResourceProperties.class) @RequiredArgsConstructor @Slf4j
public class ResourcePropertiesPresentable implements Presentable
  {
    @Nonnull
    private final ResourceProperties properties;

    @Override @Nonnull
    public PresentationModel createPresentationModel (final @Nonnull Object ... localRoles)
      {
        final SimpleComposite<PresentationModel> composite =
                new DefaultSimpleComposite<>(new SimpleFinderSupport<PresentationModel>()
          {
            @Override
            protected List<? extends PresentationModel> computeResults()
              {
                final List<PresentationModel> results = new ArrayList<>();
                final Collection<Id> groupIds = properties.getGroupIds();
                groupIds.add(new Id(""));

                for (final Id groupId : groupIds)
                  {
                    final ResourceProperties p2 = groupId.equals(new Id("")) ? properties : properties.getGroup(groupId);

                    for (final Key<?> key : p2.getKeys())
                      {
                        try
                          {
                            final String prefix = groupId.stringValue().equals("") ? "" : groupId.stringValue() + ".";
                            final Map<String, PresentationModel> map = new HashMap<>();
                            map.put("Name", new DefaultPresentationModel(new DefaultDisplayable(prefix + key.stringValue())));
                            map.put("Value", new DefaultPresentationModel(new DefaultDisplayable("" + p2.getProperty(key))));
                            results.add(new DefaultPresentationModel(properties, new MapAggregate<>(map)));
                          }
                        catch (IOException | NotFoundException e)
                          {
                            log.warn("", e);
                          }
                        // should never happen, we're cycling on available properties

                      }
                  }

                return results;
              }
          });

        final List<Object> roles = new ArrayList<>(Arrays.asList(localRoles));
        roles.add(composite);

        return new DefaultPresentationModel(properties, roles.toArray());
      }
  }
