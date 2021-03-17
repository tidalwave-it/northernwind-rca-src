/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.impl.javafx.contenteditor;

import javax.annotation.Nonnull;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.scene.layout.Pane;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class JavaFXContentEditorPresentationDelegate implements ContentEditorPresentation
  {
    @Nonnull
    private final JavaFXBinder binder;

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

    private final EventHandler<WebEvent<String>> clickHijacker = event -> log.debug("hijacked click: {}", event);

    public void initialize()
      {
        contentWebView.getEngine().setOnAlert(clickHijacker);
      }

    @Override
    public void bind (final @Nonnull Bindings bindings)
      {
        binder.bindBidirectionally(contentTitle.textProperty(), bindings.title);
        binder.bind(btOpenExternalEditor, bindings.openExternalEditorAction);
        binder.bind(btOpenExternalEditorBrowser, bindings.openExternalEditorBrowserAction);
      }

    @Override
    public void showUp()
      {
        // never used
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
        binder.bind(contentEditorProperties, pm);
      }
  }
