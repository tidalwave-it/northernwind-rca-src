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
package it.tidalwave.role.ui.javafx.impl.tableview;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.Executor;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.impl.DelegateSupport;
import static javafx.collections.FXCollections.observableArrayList;
import static it.tidalwave.role.SimpleComposite.SimpleComposite;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class TableViewBindings extends DelegateSupport
  {
    private final Callback<TableColumn<PresentationModel, PresentationModel>, 
                           TableCell<PresentationModel, PresentationModel>> cellFactory = 
            new Callback<TableColumn<PresentationModel, PresentationModel>, TableCell<PresentationModel, PresentationModel>>() 
      {
        @Override @Nonnull
        public TableCell<PresentationModel, PresentationModel> call (TableColumn<PresentationModel, PresentationModel> param) 
          {
            return new AsObjectTableCell<PresentationModel>();
          }
      };
    
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public TableViewBindings (final @Nonnull Executor executor)
      {
        super(executor);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    public void bind (final @Nonnull TableView<PresentationModel> tableView,
                      final @Nonnull PresentationModel pm)
      {
        assertIsFxApplicationThread();

        tableView.setItems(observableArrayList(pm.as(SimpleComposite).findChildren().results()));
        
        final ObservableList rawColumns = tableView.getColumns(); // FIXME
        final ObservableList<TableColumn<PresentationModel, PresentationModel>> columns =
                (ObservableList<TableColumn<PresentationModel, PresentationModel>>)rawColumns;
        
        for (final TableColumn<PresentationModel, PresentationModel> column : columns)
          {
            column.setCellValueFactory(new AggregateAdapter());
            column.setCellFactory(cellFactory);
          }
      }
  }
