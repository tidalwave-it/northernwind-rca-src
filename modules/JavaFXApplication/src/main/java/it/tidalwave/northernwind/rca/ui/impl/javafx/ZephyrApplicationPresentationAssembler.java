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
package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.javafx.ApplicationPresentationAssembler;
import it.tidalwave.role.ui.javafx.StackPaneSelector;
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
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class ZephyrApplicationPresentationAssembler
        implements ApplicationPresentationAssembler<JavaFXApplicationPresentationDelegate>
  {
    private static final String EDITOR_AREA = "editorArea";

    @Inject
    private StackPaneSelector stackPaneSelector;

    @Inject
    private JavaFXContentEditorPresentation contentEditorPresentation;

    @Inject
    private JavaFXContentExplorerPresentation contentExplorerPresentation;

    @Inject
    private JavaFXStructureEditorPresentation structureEditorPresentation;

    @Inject
    private JavaFXStructureExplorerPresentation structureExplorerPresentation;

    @Inject
    private JavaFXSiteOpenerPresentation siteOpenerPresentation;

    @Override
    public void assemble (final @Nonnull JavaFXApplicationPresentationDelegate applicationDelegate)
      {
        stackPaneSelector.registerArea(EDITOR_AREA, applicationDelegate.getStackPane());
        stackPaneSelector.add(EDITOR_AREA, structureEditorPresentation.getNad().getNode());
        stackPaneSelector.add(EDITOR_AREA, contentEditorPresentation.getNad().getNode());

        applicationDelegate.setLeftVerticalSplitContents(structureExplorerPresentation.getNad().getNode(),
                                              contentExplorerPresentation.getNad().getNode());
        siteOpenerPresentation.createDelegate(applicationDelegate);
      }
  }
