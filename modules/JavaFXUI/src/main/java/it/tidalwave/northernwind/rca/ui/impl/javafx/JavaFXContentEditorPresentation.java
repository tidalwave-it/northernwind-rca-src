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
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.JavaFXBindings;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import it.tidalwave.role.ui.javafx.Widget;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j @ToString
public class JavaFXContentEditorPresentation implements ContentEditorPresentation
  {
    @Inject @Nonnull
    private JavaFXBindings bindings;

    @Widget("contentEditorContainer")
    private Pane myContainer;

    @Widget("structureEditorContainer")
    private Pane otherContainer;

    @Widget("contentWebView")
    private WebView webView;

    @Widget("contentTitle")
    private TextField contentTitle;

    @Widget("contentEditorProperties")
    private TableView<PresentationModel> tableView;

//    @PostConstruct FIXME: when Spring calls, it's too early; this is called by JavaFXSafeComponentBuilder
    public void initialize()
      {
        bindings.bindColumn(tableView, 0, "name");
        bindings.bindColumn(tableView, 1, "value");
      }

    @Override
    public void showUp()
      {
        log.info("setting visible: {} setting not visible: {}", myContainer, otherContainer);
        otherContainer.setVisible(false);
        myContainer.setVisible(true);
      }

    @Override
    public void populateText (final @Nonnull String text)
      {
        webView.getEngine().loadContent(text);
      }

    @Override
    public void populateTitle (final @Nonnull String title)
      {
        contentTitle.setText(title);
      }

    @Override
    public void populateProperties (final @Nonnull PresentationModel pm)
      {
        bindings.bind(tableView, pm);
      }
  }
