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

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class TableViewBindings extends DelegateSupport
  {
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
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    public void bindColumn (final @Nonnull TableView<PresentationModel> tableView,
                            final @Nonnegative int columnIndex,
                            final @Nonnull String id)
      {
        assertIsFxApplicationThread();

        final ObservableList rawColumns = tableView.getColumns(); // FIXME
        final ObservableList<TableColumn<PresentationModel, String>> columns =
                (ObservableList<TableColumn<PresentationModel, String>>)rawColumns;
        columns.get(columnIndex).setId(id); // FIXME: is it correct to use Id?
        columns.get(columnIndex).setCellValueFactory(new RowAdapter<String>());
      }
  }
