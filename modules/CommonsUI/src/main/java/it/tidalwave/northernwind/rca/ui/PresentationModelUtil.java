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

import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.model.impl.admin.ResourceWithAs;
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
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public class PresentationModelUtil
  {
    @Nonnull
    // FIXME: Content should be parameterized SimpleComposite<T>
//    public <T extends As & SimpleComposite<T>> PresentationModel createPresentationModel (
//    public <T extends As & Resource & SimpleComposite<T>> PresentationModel createPresentationModel (
    public <T extends As & ResourceWithAs & SimpleComposite<Content>> PresentationModel createPresentationModel (
            final @Nonnull T datum,
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

//                        for (final T object : datum.findChildren().results())
                        for (final Object object : datum.findChildren().results())
                          {
//                            results.add(createPresentationModel(object, roleFactory, roles));
                            results.add(createPresentationModel((T)object, roleFactory, roles));
                          }

                        return results;
                      }
                  };
              }
          });
      }
  }
