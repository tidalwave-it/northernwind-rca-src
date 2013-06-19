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
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentationControl;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentationControl;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentationControl;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.javafx.impl.JavaFXSafeComponentBuilder.*;

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
    @Inject @Nonnull
    private SiteOpenerPresentationControl siteOpenerPresentationControl;

    @Inject @Nonnull
    private StructureExplorerPresentationControl structureExplorerPresentationControl;

    @Inject @Nonnull
    private ContentExplorerPresentationControl contentExplorerPresentationControl;

    @Inject @Nonnull
    private ContentEditorPresentationControl contentEditorPresentationControl;

    @Inject @Nonnull
    private StructureEditorPresentationControl structureEditorPresentationControl;

    @FXML
    private Button btOpen;

    @FXML
    private TreeView<PresentationModel> tvStructure;

    @FXML
    private TreeView<PresentationModel> tvContent;

    @FXML
    private TextField contentTitle;

    @FXML
    private WebView contentWebView;

    @FXML
    private WebView structureWebView;

    @FXML
    private Pane contentEditorContainer;

    @FXML
    private Pane structureEditorContainer;

    @FXML
    private TableView<PresentationModel> contentEditorProperties;

    @FXML
    private TableView<PresentationModel> structureEditorProperties;

//    @FXML
//    private void onOpen (final @Nonnull ActionEvent event)
//      {
//        log.info("open: {}", event);
//      }

    public void initialize()
      {
        contentEditorContainer.setVisible(false);
        structureEditorContainer.setVisible(false);

        // FIXME: could this be done by Spring?
        siteOpenerPresentationControl.initialize(createInstance(JavaFXSiteOpenerPresentation.class, this));
        contentExplorerPresentationControl.initialize(createInstance(JavaFXContentExplorerPresentation.class, this));
        structureExplorerPresentationControl.initialize(createInstance(JavaFXStructureExplorerPresentation.class, this));
        contentEditorPresentationControl.initialize(createInstance(JavaFXContentEditorPresentation.class, this));
        structureEditorPresentationControl.initialize(createInstance(JavaFXStructureEditorPresentation.class, this));
      }
  }
