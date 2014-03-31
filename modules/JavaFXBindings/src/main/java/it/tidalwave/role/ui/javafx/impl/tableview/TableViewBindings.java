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

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nonnull;
import java.util.concurrent.Executor;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.impl.DelegateSupport;
import static javafx.collections.FXCollections.observableArrayList;
import static it.tidalwave.role.SimpleComposite.SimpleComposite;
import static it.tidalwave.role.ui.Selectable.Selectable;
import it.tidalwave.util.AsException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
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
    @VisibleForTesting final ChangeListener<PresentationModel> changeListener =
            new ChangeListener<PresentationModel>()
      {
        @Override
        public void changed (final @Nonnull ObservableValue<? extends PresentationModel> ov,
                             final @Nonnull PresentationModel oldItem,
                             final @Nonnull PresentationModel item)
          {
            executor.execute(new Runnable()
              {
                @Override
                public void run()
                  {
                    try
                      {
                        item.as(Selectable).select();
                      }
                    catch (AsException e)
                      {
                        log.debug("No Selectable role for {}", item); // ok, do nothing
                      }
                  }
              });
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
        tableView.getSelectionModel().selectedItemProperty().addListener(changeListener);
        
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
