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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.As;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentationControl;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentationControl;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable
@Slf4j
public class ApplicationHandler
  {
    @FXML
    private TreeView<As> tvStructure;

    @FXML
    private TreeView<As> tvContent;

    @FXML
    private WebView wpHtmlEditor;

    @Inject @Nonnull
    private StructureExplorerPresentationControl structureExplorerPresentationControl;

    @Inject @Nonnull
    private ContentExplorerPresentationControl contentExplorerPresentationControl;

    @Inject @Nonnull
    private ContentEditorPresentationControl contentEditorPresentationControl;

    @FXML
    private void onOpen (final @Nonnull ActionEvent event)
      {
        log.info("open: {}", event);
      }

    public void initialize()
      {
        contentExplorerPresentationControl.initialize(new JavaFXContentExplorerPresentation(tvContent));
        structureExplorerPresentationControl.initialize(new JavaFXStructureExplorerPresentation(tvStructure));
        contentEditorPresentationControl.initialize(new JavaFXPageEditorPresentation(wpHtmlEditor));
      }
  }
