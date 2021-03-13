/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
import it.tidalwave.util.Id;
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
import static it.tidalwave.northernwind.rca.util.PropertyUtilities.*;

/***********************************************************************************************************************
 *
 * A provider of a {@link PresentationModel} for {@link ResourceProperties} suitable for being rendered in a table.
 * See {@link PropertyPmFinder#computeResults()} for more details.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@DciRole(datumType = ResourceProperties.class) @RequiredArgsConstructor @Slf4j
public class ResourcePropertiesPresentable implements Presentable
  {
    /*******************************************************************************************************************
     *
     * A {@link it.tidalwave.util.Finder} of {@link PresentationModel}s for each property inside a given
     * {@link ResourceProperties}.
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    static class PropertyPmFinder extends SimpleFinderSupport<PresentationModel>
      {
        private static final long serialVersionUID = 6614643999849054070L;

        @Nonnull
        private final ResourceProperties properties;

        /***************************************************************************************************************
         *
         * The usual {@link it.tidalwave.util.Finder} copy constructor.
         *
         **************************************************************************************************************/
        public PropertyPmFinder (final @Nonnull PropertyPmFinder other, final @Nonnull Object override)
          {
            super(other, override);
            final PropertyPmFinder source = getSource(PropertyPmFinder.class, other, override);
            this.properties = source.properties;
          }

        /***************************************************************************************************************
         *
         * Each item is a {@link PresentationModel} containing an {@link it.tidalwave.role.Aggregate} suitable
         * for being rendered in a table with two columns. It's made of two named objects:
         *
         * <ul>
         *   <li>{@code Name} contains a {@code PresentationModel} with a {@code Displayable} for the property
         *   name;</li>
         *   <li>{@code Value} contains a {@code PresentationModel} with a {@code Displayable} for the property
         *   value;</li>
         * </ul>
         *
         * @return
         *
         **************************************************************************************************************/
        @Override @Nonnull
        protected List<? extends PresentationModel> computeResults()
          {
            final Id globalGroupId = new Id("");
            final List<PresentationModel> results = new ArrayList<>();
            final Collection<Id> groupIds = properties.getGroupIds();
            groupIds.add(globalGroupId);

            groupIds.stream().forEach(groupId ->
              {
                final ResourceProperties propertyGroup = groupId.equals(globalGroupId) ? properties : properties.getGroup(groupId);

                propertyGroup.getKeys().stream().forEach(key ->
                  {
                    final String prefix = groupId.stringValue().equals("") ? "" : groupId.stringValue() + ".";
                    final Map<String, PresentationModel> map = new HashMap<>();
                    map.put("Name",  new DefaultPresentationModel(key,
                                                                  new DefaultDisplayable(prefix + key.stringValue())));
                    map.put("Value", new DefaultPresentationModel(key, displayableForValue(propertyGroup, key)));
                    results.add(new DefaultPresentationModel(key, properties, new MapAggregate<>(map)));
                  });
              });

            return results;
          }
      }

    @Nonnull
    private final ResourceProperties ownerProperties;

    /*******************************************************************************************************************
     *
     * Creates a {@link PresentationModel} containing a {@link it.tidalwave.role.Composite} bound to the {@code
     * PropertyPmFinder}.
     *
     * @param localRoles  optional additional roles
     * @return the {@code PresentationModel}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public PresentationModel createPresentationModel (final @Nonnull Object ... localRoles)
      {
        final SimpleComposite<PresentationModel> composite =
                new DefaultSimpleComposite<>(new PropertyPmFinder(ownerProperties));
        final List<Object> roles = new ArrayList<>(Arrays.asList(localRoles));
        roles.add(composite);

        return new DefaultPresentationModel(ownerProperties, roles.toArray());
      }
  }
