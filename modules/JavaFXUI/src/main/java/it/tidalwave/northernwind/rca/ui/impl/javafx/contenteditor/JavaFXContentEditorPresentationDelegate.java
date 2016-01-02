/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.ui.impl.javafx.contenteditor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import it.tidalwave.role.ui.javafx.StackPaneSelector;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class JavaFXContentEditorPresentationDelegate implements ContentEditorPresentation
  {
    @Inject
    private Provider<JavaFXBinder> binder;

    @Inject
    private Provider<StackPaneSelector> stackPaneSelector;

    @FXML
    private Pane contentEditor;

    @FXML
    private WebView contentWebView;

    @FXML
    private TableView<PresentationModel> contentEditorProperties;

    @FXML
    private TextField contentTitle;

    @FXML
    private Button btOpenExternalEditor;

    @FXML
    private Button btOpenExternalEditorBrowser;

    private final EventHandler<WebEvent<String>> clickHijacker = new EventHandler<WebEvent<String>>()
      {
        @Override
        public void handle (final @Nonnull WebEvent<String> event)
          {
            log.debug("hijacked click: {}", event);
          }
      };

    public void initialize()
      {
        contentWebView.getEngine().setOnAlert(clickHijacker);
      }

    @Override
    public void bind (final @Nonnull Bindings bindings)
      {
        binder.get().bindBidirectionally(contentTitle.textProperty(), bindings.title);
        binder.get().bind(btOpenExternalEditor, bindings.openExternalEditorAction);
        binder.get().bind(btOpenExternalEditorBrowser, bindings.openExternalEditorBrowserAction);
      }

    @Override
    public void showUp()
      {
        stackPaneSelector.get().setShownNode(contentEditor);
      }

    @Override
    public void clear()
      {
        contentWebView.getEngine().loadContent("");
        // FIXME: binder.unbind(contentEditorProperties)
        contentEditorProperties.setItems(FXCollections.<PresentationModel>emptyObservableList());
      }

    @Override
    public void populateDocument (final @Nonnull String url)
      {
        contentWebView.getEngine().load(url);
      }

    @Override
    public void populateProperties (final @Nonnull PresentationModel pm)
      {
        binder.get().bind(contentEditorProperties, pm);
      }
  }
