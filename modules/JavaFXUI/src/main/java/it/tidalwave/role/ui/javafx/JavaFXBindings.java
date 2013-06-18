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
package it.tidalwave.role.ui.javafx;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javafx.beans.property.Property;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.BoundProperty;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface JavaFXBindings
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull TableView<PresentationModel> tableView, @Nonnull PresentationModel pm);

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull TreeView<PresentationModel> treeView, @Nonnull PresentationModel pm);

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void bindColumn (@Nonnull TableView<PresentationModel> tableView,
                            @Nonnegative int columnIndex,
                            @Nonnull String id);

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public <T> void bindBidirectionally (@Nonnull Property<T> property1, @Nonnull BoundProperty<T> property2);
  }
