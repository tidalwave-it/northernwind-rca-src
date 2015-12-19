/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
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
import javax.inject.Provider;
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
import static it.tidalwave.role.ui.javafx.impl.util.JavaFXSafeComponentBuilder.createInstance;

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
    private Provider<SiteOpenerPresentationControl> siteOpenerPresentationControl;

    @Inject @Nonnull
    private Provider<StackPaneSelector> stackPaneSelector;

    @Inject @Nonnull
    private Provider<ContentEditorPresentationControl> contentEditorPresentationControl;

    @Inject @Nonnull
    private Provider<ContentExplorerPresentationControl> contentExplorerPresentationControl;

    @Inject @Nonnull
    private Provider<StructureEditorPresentationControl> structureEditorPresentationControl;

    @Inject @Nonnull
    private Provider<StructureExplorerPresentationControl> structureExplorerPresentationControl;

    @Inject @Nonnull
    private Provider<JavaFXContentEditorPresentation> contentEditorPresentation;

    @Inject @Nonnull
    private Provider<JavaFXContentExplorerPresentation> contentExplorerPresentation;

    @Inject @Nonnull
    private Provider<JavaFXStructureEditorPresentation> structureEditorPresentation;

    @Inject @Nonnull
    private Provider<JavaFXStructureExplorerPresentation> structurExplorerPresentation;

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
        stackPaneSelector.get().registerArea("editorArea", stackPane);
        stackPaneSelector.get().add("editorArea", structureEditorPresentation.get().getNode());
        stackPaneSelector.get().add("editorArea", contentEditorPresentation.get().getNode());

        pnVerticalSplit.getItems().add(structurExplorerPresentation.get().getNode());
        pnVerticalSplit.getItems().add(contentExplorerPresentation.get().getNode());

        // FIXME: controllers can't initialize in postconstruct
        // Too bad because with PAC+EventBus we'd get rid of the control interfaces
        contentEditorPresentationControl.get().initialize();
        contentExplorerPresentationControl.get().initialize();
        structureEditorPresentationControl.get().initialize();
        structureExplorerPresentationControl.get().initialize();

        // FIXME: this should be delegated to other handlers, as already done for the Editors
        siteOpenerPresentationControl.get().initialize(createInstance(JavaFXSiteOpenerPresentation.class, this));
      }
  }
