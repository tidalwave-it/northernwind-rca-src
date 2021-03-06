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
import java.util.Collection;
import java.util.List;
import it.tidalwave.role.Aggregate;
import it.tidalwave.util.Id;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.Displayable;
import it.tidalwave.role.ui.Presentable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.northernwind.rca.util.PropertyUtilities.*;
import static it.tidalwave.util.Parameters.r;

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
        public PropertyPmFinder (@Nonnull final PropertyPmFinder other, @Nonnull final Object override)
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

            groupIds.forEach(groupId ->
              {
                final ResourceProperties propertyGroup = groupId.equals(globalGroupId) ? properties : properties.getGroup(groupId);

                propertyGroup.getKeys().forEach(key ->
                  {
                    final String prefix = "".equals(groupId.stringValue()) ? "" : groupId.stringValue() + ".";
                    final Aggregate<PresentationModel> aggregate = Aggregate
                            .of("Name",  PresentationModel.of(key, Displayable.of(prefix + key.stringValue())))
                            .with( "Value", PresentationModel.of(key, displayableForValue(propertyGroup, key)));
                    results.add(PresentationModel.of(key, r(properties, aggregate)));
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
    public PresentationModel createPresentationModel (@Nonnull final Collection<Object> localRoles)
      {
        return PresentationModel.of(ownerProperties,
                                    r(SimpleComposite.of(new PropertyPmFinder(ownerProperties)), localRoles));
      }
  }
