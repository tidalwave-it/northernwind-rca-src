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
package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.javafx.StackPaneSelector;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentationControl;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentationControl;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentationControl;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.impl.javafx.contenteditor.JavaFXContentEditorPresentation;
import it.tidalwave.northernwind.rca.ui.impl.javafx.contentexplorer.JavaFXContentExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.impl.javafx.siteopener.JavaFXSiteOpenerPresentation;
import it.tidalwave.northernwind.rca.ui.impl.javafx.structureexplorer.JavaFXStructureExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.impl.javafx.structureeditor.JavaFXStructureEditorPresentation;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.javafx.impl.JavaFXSafeComponentBuilder.*;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class JavaFXApplicationPresentationDelegate
  {
    @Inject @Nonnull
    private SiteOpenerPresentationControl siteOpenerPresentationControl;

    @Inject @Nonnull
    private StackPaneSelector stackPaneSelector;

    @Inject @Nonnull
    private ContentEditorPresentationControl contentEditorPresentationControl;

    @Inject @Nonnull
    private ContentExplorerPresentationControl contentExplorerPresentationControl;

    @Inject @Nonnull
    private StructureEditorPresentationControl structureEditorPresentationControl;

    @Inject @Nonnull
    private StructureExplorerPresentationControl structureExplorerPresentationControl;

    @Inject @Nonnull
    private JavaFXContentEditorPresentation contentEditorPresentation;

    @Inject @Nonnull
    private JavaFXContentExplorerPresentation contentExplorerPresentation;

    @Inject @Nonnull
    private JavaFXStructureEditorPresentation structureEditorPresentation;

    @Inject @Nonnull
    private JavaFXStructureExplorerPresentation structurExplorerPresentation;

    @FXML
    private Button btOpen;

    @FXML
    private StackPane stackPane;

    @FXML
    private SplitPane pnVerticalSplit;

    @FXML
    private MenuItem openSiteMenu;

//    @FXML
//    private void onOpen (final @Nonnull ActionEvent event)
//      {
//        log.info("open: {}", event);
//      }

    public void initialize()
      throws IOException
      {
        stackPaneSelector.registerArea("editorArea", stackPane);
        stackPaneSelector.add("editorArea", structureEditorPresentation.getNode());
        stackPaneSelector.add("editorArea", contentEditorPresentation.getNode());

        pnVerticalSplit.getItems().add(structurExplorerPresentation.getNode());
        pnVerticalSplit.getItems().add(contentExplorerPresentation.getNode());

        // FIXME: controllers can't initialize in postconstruct
        // Too bad because with PAC+EventBus we'd get rid of the control interfaces
        contentEditorPresentationControl.initialize();
        contentExplorerPresentationControl.initialize();
        structureEditorPresentationControl.initialize();
        structureExplorerPresentationControl.initialize();

        // FIXME: this should be delegated to other handlers, as already done for the Editors
        siteOpenerPresentationControl.initialize(createInstance(JavaFXSiteOpenerPresentation.class, this));
      }
  }
