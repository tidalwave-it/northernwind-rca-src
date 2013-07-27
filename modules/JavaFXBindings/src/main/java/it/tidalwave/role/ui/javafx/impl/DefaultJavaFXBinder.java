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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.io.File;
import java.nio.file.Path;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
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
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.application.Platform;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import it.tidalwave.role.ui.javafx.impl.tree.TreeItemBindings;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;
import static javafx.collections.FXCollections.*;
import static it.tidalwave.role.SimpleComposite.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class DefaultJavaFXBinder implements JavaFXBinder
  {
    private final Executor executor;

    private String invalidTextFieldStyle = "-fx-background-color: pink";

    @Setter
    private Window mainWindow;

    @Delegate
    private final TreeItemBindings treeItemBindings = new TreeItemBindings();

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

        property1.bindBidirectional(new PropertyAdapter<>(executor, property2));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <T> void bindBidirectionally (final @Nonnull TextField textField,
                                         final @Nonnull BoundProperty<String> textProperty,
                                         final @Nonnull BoundProperty<Boolean> validProperty)
      {
        assertIsFxApplicationThread();

        textField.textProperty().bindBidirectional(new PropertyAdapter<>(executor, textProperty));

        // FIXME: weak listener
        validProperty.addPropertyChangeListener(new PropertyChangeListener()
          {
            @Override
            public void propertyChange (final @Nonnull PropertyChangeEvent event)
              {
                 textField.setStyle(validProperty.get() ? "" : invalidTextFieldStyle);
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void showInModalDialog (final @Nonnull Node node, final @Nonnull UserNotificationWithFeedback notification)
      {
        showInModalDialog(node, notification, new BoundProperty<>(true));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void showInModalDialog (final @Nonnull Node node,
                                   final @Nonnull UserNotificationWithFeedback notification,
                                   final @Nonnull BoundProperty<Boolean> valid)
      {
        Platform.runLater(new Runnable() // FIXME: should not be needed
          {
            @Override
            public void run()
              {
                log.info("modalDialog({}, {})", node, notification);

                final Stage dialogStage = new Stage(StageStyle.DECORATED);
                dialogStage.setResizable(false);
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.initOwner(mainWindow);
                dialogStage.setTitle(notification.getCaption());

                final VBox vbox = new VBox();
                vbox.setPadding(new Insets(8, 8, 8, 8));
                final FlowPane buttonPane = new FlowPane();
                buttonPane.setAlignment(Pos.CENTER_RIGHT);
                buttonPane.setHgap(8);

                final Button okButton = new Button("Ok");
                final Button cancelButton = new Button("Cancel");

                if (isOSX())
                  {
                    buttonPane.getChildren().add(cancelButton);
                    buttonPane.getChildren().add(okButton);
                  }
                else
                  {
                    buttonPane.getChildren().add(okButton);
                    buttonPane.getChildren().add(cancelButton);
                  }

                vbox.getChildren().add(node);
                vbox.getChildren().add(buttonPane);

                okButton.setDefaultButton(true);
                cancelButton.setCancelButton(true);

                final DialogCloserHandler closeAndConfirm = new DialogCloserHandler(executor, dialogStage)
                  {
                    @Override
                    protected void doSomething() throws Exception
                      {
                        notification.getFeedback().onConfirm();
                      }
                  };

                final DialogCloserHandler closeAndCancel = new DialogCloserHandler(executor, dialogStage)
                  {
                    @Override
                    protected void doSomething() throws Exception
                      {
                        notification.getFeedback().onCancel();
                      }
                  };

//                okButton.disableProperty().bind(new PropertyAdapter<>(valid)); // FIXME: doesn't work

                dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>()
                  {
                    @Override
                    public void handle (final @Nonnull WindowEvent event)
                      {
                        executor.execute(new Runnable()
                          {
                            @Override
                            public void run()
                              {
                                try
                                  {
                                    notification.getFeedback().onCancel();
                                  }
                                catch (Exception e)
                                  {
                                    log.error("", e);
                                  }
                              }
                          });
                      }
                  });

                okButton.setOnAction(closeAndConfirm);
                cancelButton.setOnAction(closeAndCancel);

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
                                    final @Nonnull BoundProperty<Path> selectedFile)
      {
        log.debug("openFileChooserFor({}, {})", notification, selectedFile);
        assertIsFxApplicationThread();

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(notification.getCaption());
        fileChooser.setInitialDirectory(selectedFile.get().toFile());

        // It seems we need to take care of modality: https://javafx-jira.kenai.com/browse/RT-13949
        final File file = runWhileDisabling(mainWindow, new Callable<File>()
          {
            @Override
            public File call()
              {
                return fileChooser.showOpenDialog(mainWindow);
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
                                         final @Nonnull BoundProperty<Path> selectedFolder)
      {
        log.debug("openDirectoryChooserFor({}, {})", notification, selectedFolder);
        assertIsFxApplicationThread();

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(notification.getCaption());
        directoryChooser.setInitialDirectory(selectedFolder.get().toFile());

        // It seems we need to take care of modality: https://javafx-jira.kenai.com/browse/RT-13949
        final File file = runWhileDisabling(mainWindow, new Callable<File>()
          {
            @Override
            public File call()
              {
                return directoryChooser.showDialog(mainWindow);
              }
          });

        notifyFile(file, notification, selectedFolder);
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

    /*******************************************************************************************************************
     *
     * TODO: delegate to a provider
     *
     ******************************************************************************************************************/
    public static boolean isOSX()
      {
        return System.getProperty("os.name").contains("OS X");
      }
  }
