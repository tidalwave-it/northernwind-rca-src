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
import javafx.scene.layout.Pane;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.JavaFXBindings;
import it.tidalwave.role.ui.javafx.Widget;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class JavaFXStructureEditorPresentation implements StructureEditorPresentation
  {
    @Inject @Nonnull
    private JavaFXBindings bindings;

    @Widget("structureEditorContainer")
    private Pane myContainer;

    @Widget("contentEditorContainer")
    private Pane otherContainer;

    @Widget("structureWebView")
    private WebView webView;

    @Widget("structureEditorProperties")
    private TableView<PresentationModel> tableView;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
//    @PostConstruct FIXME: when Spring calls, it's too early; this is called by JavaFXSafeComponentBuilder
    public void initialize()
      {
        bindings.bindColumn(tableView, 0, "name");
        bindings.bindColumn(tableView, 1, "value");
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void showUp()
      {
        log.info("setting visible: {} setting not visible: {}", myContainer, otherContainer);
        otherContainer.setVisible(false);
        myContainer.setVisible(true);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void populate (final @Nonnull String text)
      {
        webView.getEngine().loadContent(text);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void populateProperties (final @Nonnull PresentationModel pm)
      {
        bindings.bind(tableView, pm);
      }
  }
