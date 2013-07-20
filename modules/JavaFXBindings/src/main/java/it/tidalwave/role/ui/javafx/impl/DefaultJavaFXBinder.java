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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;
import java.nio.file.Path;
import javafx.util.Callback;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.application.Platform;
import com.google.common.annotations.VisibleForTesting;
import it.tidalwave.util.AsException;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import lombok.extern.slf4j.Slf4j;
import static javafx.collections.FXCollections.*;
import static it.tidalwave.role.ui.Selectable.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultJavaFXBinder implements JavaFXBinder
  {
    private static final Class<SimpleComposite> SimpleComposite = SimpleComposite.class; // FIXME: move to TFT

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @VisibleForTesting final Callback<TreeView<PresentationModel>, TreeCell<PresentationModel>> treeCellFactory =
            new Callback<TreeView<PresentationModel>, TreeCell<PresentationModel>>()
      {
        @Override @Nonnull
        public TreeCell<PresentationModel> call (final @Nonnull TreeView<PresentationModel> treeView)
          {
            return new AsObjectTreeCell<>();
          }
      };

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @VisibleForTesting final ChangeListener<TreeItem<PresentationModel>> treeItemChangeListener =
            new ChangeListener<TreeItem<PresentationModel>>()
      {
        @Override
        public void changed (final @Nonnull ObservableValue<? extends TreeItem<PresentationModel>> ov,
                             final @Nonnull TreeItem<PresentationModel> oldItem,
                             final @Nonnull TreeItem<PresentationModel> item)
          {
            try
              {
                item.getValue().as(Selectable).select();
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

        tableView.setItems(observableArrayList(pm.as(SimpleComposite).findChildren().results()));
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
    public void showInModalDialog (final @Nonnull Node node, final @Nonnull UserNotificationWithFeedback notification)
      {
        Platform.runLater(new Runnable() // FIXME: should not be needed
          {
            @Override
            public void run()
              {
                log.info("modalDialog({}, {})", node, notification);

                final Stage dialogStage = new Stage(StageStyle.DECORATED);
                dialogStage.initModality(Modality.APPLICATION_MODAL);

                final VBox vbox = new VBox();
                final FlowPane buttonPane = new FlowPane();
                final Button okButton = new Button("Ok");
                final Button cancelButton = new Button("Cancel");
                buttonPane.getChildren().add(okButton);
                buttonPane.getChildren().add(cancelButton);
                vbox.getChildren().add(node);
                vbox.getChildren().add(buttonPane);

                okButton.setDefaultButton(true);
                cancelButton.setCancelButton(true);
                okButton.setOnAction(new DialogCloserHandler(executorService, dialogStage)
                  {
                    @Override
                    protected void doSomething() throws Exception
                      {
                        notification.getFeedback().onConfirm();
                      }
                  });

                cancelButton.setOnAction(new DialogCloserHandler(executorService, dialogStage)
                  {
                    @Override
                    protected void doSomething() throws Exception
                      {
                        notification.getFeedback().onCancel();
                      }
                  });

                dialogStage.setTitle(notification.getCaption());
                dialogStage.setScene(new Scene(vbox));
                dialogStage.centerOnScreen();
                dialogStage.showAndWait();
              }
          });
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
        log.debug("openFileChooserFor({}, {}, {})", notification, selectedFile, window);
        assertIsFxApplicationThread();

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(notification.getCaption());
        fileChooser.setInitialDirectory(selectedFile.get().toFile());

        // It seems we need to take care of modality: https://javafx-jira.kenai.com/browse/RT-13949
        final File file = runWhileDisabling(window, new Callable<File>()
          {
            @Override
            public File call()
              {
                return fileChooser.showOpenDialog(window);
              }
          });

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
        log.debug("openDirectoryChooserFor({}, {}, {})", notification, selectedFolder, window);
        assertIsFxApplicationThread();

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(notification.getCaption());
        directoryChooser.setInitialDirectory(selectedFolder.get().toFile());

        // It seems we need to take care of modality: https://javafx-jira.kenai.com/browse/RT-13949
        final File file = runWhileDisabling(window, new Callable<File>()
          {
            @Override
            public File call()
              {
                return directoryChooser.showDialog(window);
              }
          });

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
        final SimpleComposite<PresentationModel> composite = pm.as(SimpleComposite);

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
     * Runs the given (@link Callable} while disabling the given {@link Window}.
     *
     * @param  window    the {@code Window} to disable
     * @param  callable  the (@code Callable} to run
     * @return           the (@code Callable} result
     *
     ******************************************************************************************************************/
    private <T> T runWhileDisabling (final @Nonnull Window window, final @Nonnull Callable<T> callable)
      {
        final Parent root = window.getScene().getRoot();
        final Effect effect = root.getEffect();
        final boolean disabled = root.isDisable();

        try
          {
            root.setDisable(true);
            root.setEffect(createDisablingEffect());
            return callable.call();
          }
        catch (Exception e)
          {
            throw new RuntimeException(e);
          }
        finally
          {
            root.setEffect(effect);
            root.setDisable(disabled);
          }
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

    /*******************************************************************************************************************
     *
     * TODO: delegate to a provider
     *
     ******************************************************************************************************************/
    private BoxBlur createDisablingEffect()
      {
        final BoxBlur bb = new BoxBlur();
        bb.setWidth(5);
        bb.setHeight(5);
        bb.setIterations(3);
        return bb;
      }
  }
