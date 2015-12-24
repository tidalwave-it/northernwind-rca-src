/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentationControl;
import it.tidalwave.northernwind.rca.ui.impl.javafx.contenteditor.JavaFXContentEditorPresentation;
import it.tidalwave.northernwind.rca.ui.impl.javafx.contentexplorer.JavaFXContentExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.impl.javafx.siteopener.JavaFXSiteOpenerPresentation;
import it.tidalwave.northernwind.rca.ui.impl.javafx.structureexplorer.JavaFXStructureExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.impl.javafx.structureeditor.JavaFXStructureEditorPresentation;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * The JavaFX delegate for the main GUI of the application. It assembles together all the pieces of the UI.
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class JavaFXApplicationPresentationDelegate
  {
    @Inject
    private Provider<StackPaneSelector> stackPaneSelector;

    @Inject
    private Provider<SiteOpenerPresentationControl> siteOpenerPresentationControl;

    @Inject
    private Provider<ContentEditorPresentationControl> contentEditorPresentationControl;

    @Inject
    private Provider<JavaFXContentEditorPresentation> contentEditorPresentation;

    @Inject
    private Provider<JavaFXContentExplorerPresentation> contentExplorerPresentation;

    @Inject
    private Provider<JavaFXStructureEditorPresentation> structureEditorPresentation;

    @Inject
    private Provider<JavaFXStructureExplorerPresentation> structureExplorerPresentation;

    @Inject
    private Provider<JavaFXSiteOpenerPresentation> siteOpenerPresentation;

    @FXML
    private StackPane stackPane;

    @FXML
    private SplitPane pnVerticalSplit;

    // Those below aren't used here, but replicated in JavaFXSiteOpenerPresentation
    @FXML
    private Button btOpen;

    @FXML
    private MenuItem openSiteMenu;

    public void initialize()
      throws IOException
      {
        stackPaneSelector.get().registerArea("editorArea", stackPane);
        stackPaneSelector.get().add("editorArea", structureEditorPresentation.get().getNode());
        stackPaneSelector.get().add("editorArea", contentEditorPresentation.get().getNode());

        pnVerticalSplit.getItems().add(structureExplorerPresentation.get().getNode());
        pnVerticalSplit.getItems().add(contentExplorerPresentation.get().getNode());

        // FIXME: controllers can't initialize in postconstruct
        // Too bad because with PAC+EventBus we'd get rid of the control interfaces
        contentEditorPresentationControl.get().initialize();

        siteOpenerPresentation.get().createDelegate(this);
        siteOpenerPresentationControl.get().initialize();
      }
  }
