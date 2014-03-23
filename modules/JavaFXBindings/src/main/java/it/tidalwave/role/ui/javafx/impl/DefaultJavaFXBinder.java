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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Executor;
import javafx.beans.property.Property;
import javafx.stage.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import it.tidalwave.role.ui.javafx.impl.tree.TreeViewBindings;
import it.tidalwave.role.ui.javafx.impl.dialog.DialogBindings;
import it.tidalwave.role.ui.javafx.impl.filechooser.FileChooserBindings;
import it.tidalwave.role.ui.javafx.impl.tableview.TableViewBindings;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultJavaFXBinder implements JavaFXBinder
  {
    private final Executor executor;

    private String invalidTextFieldStyle = "-fx-background-color: pink";

    interface Exclusions
      {
        public void setMainWindow (Window window);
      }

    @Delegate(excludes = Exclusions.class)
    private final TreeViewBindings treeItemBindings;

    @Delegate(excludes = Exclusions.class)
    private final TableViewBindings tableViewBindings;

    @Delegate(excludes = Exclusions.class)
    private final DialogBindings dialogBindings;

    @Delegate(excludes = Exclusions.class)
    private final FileChooserBindings fileChooserBindings;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public DefaultJavaFXBinder (final @Nonnull Executor executor)
      {
        this.executor = executor;
        treeItemBindings = new TreeViewBindings(executor);
        tableViewBindings = new TableViewBindings(executor);
        dialogBindings = new DialogBindings(executor);
        fileChooserBindings = new FileChooserBindings(executor);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void setMainWindow (final @Nonnull Window mainWindow)
      {
        treeItemBindings.setMainWindow(mainWindow);
        tableViewBindings.setMainWindow(mainWindow);
        dialogBindings.setMainWindow(mainWindow);
        fileChooserBindings.setMainWindow(mainWindow);
      }

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
    public <T> void bindBidirectionally (final @Nonnull Property<T> property1,
                                         final @Nonnull BoundProperty<T> property2)
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
