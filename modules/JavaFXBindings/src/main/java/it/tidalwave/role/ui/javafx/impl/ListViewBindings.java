/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
 * %%
 * Copyright (C) 2013 - 2014 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.role.ui.javafx.impl;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.impl.tree.AsObjectListCell;
import lombok.extern.slf4j.Slf4j;
import static javafx.collections.FXCollections.observableArrayList;
import static it.tidalwave.role.SimpleComposite.SimpleComposite;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class ListViewBindings extends DelegateSupport
  {
    private final Callback<ListView<PresentationModel>, ListCell<PresentationModel>> cellFactory = 
            new Callback<ListView<PresentationModel>, ListCell<PresentationModel>>() 
      {
        @Override
        public ListCell<PresentationModel> call (final @Nonnull ListView<PresentationModel> listView) 
          {
            return new AsObjectListCell<>();
          }
      };
    
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public ListViewBindings (final @Nonnull Executor executor)
      {
        super(executor);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void bind (final @Nonnull ListView<PresentationModel> listView, final @Nonnull PresentationModel pm)
      {
        listView.setCellFactory(cellFactory);
        listView.setItems(observableArrayList(pm.as(SimpleComposite).findChildren().results()));
      }
  }
