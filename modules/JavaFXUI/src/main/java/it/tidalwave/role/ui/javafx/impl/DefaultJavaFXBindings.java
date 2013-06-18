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
package it.tidalwave.role.ui.javafx.impl;

import javax.annotation.Nonnull;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.application.Platform;
import it.tidalwave.util.AsException;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.role.ui.javafx.JavaFXBindings;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultJavaFXBindings implements JavaFXBindings
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    public void bind (final @Nonnull TableView<PresentationModel> tableView,
                      final @Nonnull PresentationModel pm)
      {
        final SimpleComposite<PresentationModel> composite = pm.as(SimpleComposite.class);
        final ObservableList<PresentationModel> pms =
                FXCollections.observableArrayList(composite.findChildren().results());
        tableView.setItems(pms);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private final Callback<TreeView<PresentationModel>, TreeCell<PresentationModel>> cellFactory =
            new Callback<TreeView<PresentationModel>, TreeCell<PresentationModel>>()
      {
        @Override @Nonnull
        public TreeCell<PresentationModel> call (final @Nonnull TreeView<PresentationModel> p)
          {
            final TextFieldTreeCell<PresentationModel> cell = new TextFieldTreeCell<>();
            cell.setConverter(new StringConverter<PresentationModel>()
              {
                @Override
                public String toString (final @Nonnull PresentationModel pm)
                  {
                    try
                      {
                        return pm.as(Displayable.class).getDisplayName();
                      }
                    catch (AsException e)
                      {
                        return pm.toString();
                      }
                  }

                @Override
                public PresentationModel fromString (final @Nonnull String string)
                  {
                    throw new UnsupportedOperationException("Not supported yet.");
                  }
              });

            return cell;
          }
      };

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private final ChangeListener<TreeItem<PresentationModel>> changeListener =
            new ChangeListener<TreeItem<PresentationModel>>()
      {
        @Override
        public void changed (final @Nonnull ObservableValue<? extends TreeItem<PresentationModel>> ov,
                             final @Nonnull TreeItem<PresentationModel> oldItem,
                             final @Nonnull TreeItem<PresentationModel> item)
          {
            try
              {
                item.getValue().as(Selectable.class).select();
              }
            catch (AsException e)
              {
                e.printStackTrace();
                // ok, do nothing
              }
          }
      };

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    public void bind (final @Nonnull TreeView<PresentationModel> treeView,
                      final @Nonnull PresentationModel pm)
      {
        assert Platform.isFxApplicationThread() : "Must run in the JavaFX Application Thread";

        treeView.setRoot(createTreeItem(pm));
        treeView.setCellFactory(cellFactory);
        treeView.getSelectionModel().selectedItemProperty().addListener(changeListener);
     }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private TreeItem<PresentationModel> createTreeItem (final @Nonnull PresentationModel pm)
      {
        final TreeItem<PresentationModel> rootItem = new TreeItem<>(pm);

        addChildren(pm, rootItem);

        return rootItem;
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    // FIXME: add on demand, upon node expansion
    private void addChildren (final @Nonnull PresentationModel datum,
                              final @Nonnull TreeItem<PresentationModel> parentItem)
      {
        final SimpleComposite<PresentationModel> composite = datum.as(SimpleComposite.class);
        final List<? extends PresentationModel> objects = composite.findChildren().results();

        for (final PresentationModel object : objects)
          {
            final TreeItem<PresentationModel> item = new TreeItem<>(object);
            addChildren(object, item);
            parentItem.getChildren().add(item);
          }
      }
  }
