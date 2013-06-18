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
package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javafx.scene.web.WebView;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.RowAdapter;
import it.tidalwave.role.ui.javafx.JavaFXBindings;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class JavaFXContentEditorPresentation implements ContentEditorPresentation
  {
    @Inject @Nonnull
    private JavaFXBindings bindings;

    // FIXME: have them injected instead than being passed on the constructor
    @Nonnull
    private final Pane myContainer;

    @Nonnull
    private final Pane otherContainer;

    @Nonnull
    private final WebView webView;

    @Nonnull
    private final TextField contentTitle;

    @Nonnull
    private final TableView<PresentationModel> tableView;

    private final TableColumn<PresentationModel, String> nameColumn = new TableColumn<>("Name");

    private final TableColumn<PresentationModel, String> valueColumn = new TableColumn<>("Value");

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public JavaFXContentEditorPresentation (final @Nonnull Pane myContainer,
                                            final @Nonnull Pane otherContainer,
                                            final @Nonnull WebView webView,
                                            final @Nonnull TextField contentTitle,
                                            final @Nonnull TableView<PresentationModel> tableView)
      {
        this.myContainer = myContainer;
        this.otherContainer = otherContainer;
        this.webView = webView;
        this.contentTitle = contentTitle;
        this.tableView = tableView;

        nameColumn.setId("name");
        valueColumn.setId("value");
        nameColumn.setCellValueFactory(new RowAdapter<String>());
        valueColumn.setCellValueFactory(new RowAdapter<String>());
        tableView.getColumns().setAll(nameColumn, valueColumn);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void showUp()
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                log.info("setting visible: {} setting not visible: {}", myContainer, otherContainer);
                otherContainer.setVisible(false);
                myContainer.setVisible(true);
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void populateText (final @Nonnull String text)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                webView.getEngine().loadContent(text);
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void populateTitle (final @Nonnull String title)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                contentTitle.setText(title);
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void populateProperties (final @Nonnull PresentationModel pm)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                bindings.bind(tableView, pm);
              }
          });
      }
  }
