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

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.application.Platform;
import it.tidalwave.util.AsException;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.javafx.JavaFXBindings;
import lombok.extern.slf4j.Slf4j;
import static javafx.collections.FXCollections.*;
import javafx.scene.control.MenuItem;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultJavaFXBindings implements JavaFXBindings
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @VisibleForTesting final Callback<TreeView<PresentationModel>, TreeCell<PresentationModel>> treeCellFactory =
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
    private final ChangeListener<TreeItem<PresentationModel>> treeItemChangeListener =
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
                log.debug("No Selectable role for {}", item); // ok, do nothing
              }
          }
      };

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void bind (final @Nonnull Button button, final @Nonnull UserAction action)
      {
        assertIsFxApplicationThread();

//        button.disableProperty().not().bind(new PropertyAdapter<>(action.enabled())); // FIXME: not
        button.setOnAction(new EventHandler<ActionEvent>()
          {
            @Override
            public void handle (final @Nonnull ActionEvent event)
              {
                action.actionPerformed();
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void bind (final @Nonnull MenuItem menuItem, final @Nonnull UserAction action)
      {
        assertIsFxApplicationThread();

//        button.disableProperty().not().bind(new PropertyAdapter<>(action.enabled())); // FIXME: not
        menuItem.setOnAction(new EventHandler<ActionEvent>()
          {
            @Override
            public void handle (final @Nonnull ActionEvent event)
              {
                action.actionPerformed();
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void bind (final @Nonnull TableView<PresentationModel> tableView,
                      final @Nonnull PresentationModel pm)
      {
        assertIsFxApplicationThread();

        tableView.setItems(observableArrayList(pm.as(SimpleComposite.class).findChildren().results()));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void bind (final @Nonnull TreeView<PresentationModel> treeView,
                      final @Nonnull PresentationModel pm)
      {
        assertIsFxApplicationThread();

        treeView.setRoot(createTreeItem(pm));
        treeView.setCellFactory(treeCellFactory);
        treeView.getSelectionModel().selectedItemProperty().addListener(treeItemChangeListener);
     }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
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

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <T> void bindBidirectionally (final @Nonnull Property<T> property1, final @Nonnull BoundProperty<T> property2)
      {
        assertIsFxApplicationThread();

        property1.bindBidirectional(new PropertyAdapter<>(property2));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void openFileChooserFor (final @Nonnull UserNotificationWithFeedback notification,
                                    final @Nonnull BoundProperty<Path> selectedFile,
                                    final @Nonnull Window window)
      {
        assertIsFxApplicationThread();

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(notification.getCaption());
        fileChooser.setInitialDirectory(selectedFile.get().toFile());
        final File file = fileChooser.showOpenDialog(window);
        notifyFile(file, notification, selectedFile);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void openDirectoryChooserFor (final @Nonnull UserNotificationWithFeedback notification,
                                         final @Nonnull BoundProperty<Path> selectedFolder,
                                         final @Nonnull Window window)
      {
        assertIsFxApplicationThread();

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(notification.getCaption());
        directoryChooser.setInitialDirectory(selectedFolder.get().toFile());
        final File file = directoryChooser.showDialog(window);
        notifyFile(file, notification, selectedFolder);
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

        addChildren(rootItem, pm);

        return rootItem;
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    // FIXME: add on demand, upon node expansion
    private void addChildren (final @Nonnull TreeItem<PresentationModel> parentItem,
                              final @Nonnull PresentationModel pm)
      {
        final SimpleComposite<PresentationModel> composite = pm.as(SimpleComposite.class);

        for (final PresentationModel childPm : composite.findChildren().results())
          {
            final TreeItem<PresentationModel> childItem = new TreeItem<>(childPm);
            addChildren(childItem, childPm);
            parentItem.getChildren().add(childItem);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void notifyFile (final @Nullable File file,
                             final @Nonnull UserNotificationWithFeedback notification,
                             final @Nonnull BoundProperty<Path> selectedFile)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                try
                  {
                    if (file == null)
                      {
                        notification.cancel();
                      }
                    else
                      {
                        selectedFile.set(file.toPath());
                        notification.confirm();
                      }
                  }
                catch (Exception e)
                  {
                    log.warn("", e);
                  }
              }
          });
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void assertIsFxApplicationThread()
      {
        if (!Platform.isFxApplicationThread())
          {
            throw new AssertionError("Must run in the JavaFX Application Thread");
          }
      }
  }
