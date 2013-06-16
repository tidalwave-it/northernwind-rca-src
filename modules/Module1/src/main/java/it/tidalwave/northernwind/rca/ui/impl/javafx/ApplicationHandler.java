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

import it.tidalwave.northernwind.rca.ui.ContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.StructureExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.impl.DefaultContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.impl.DefaultStructureExplorerPresentationControl;
import javax.annotation.Nonnull;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class ApplicationHandler
  {
    @FXML
    private TreeView<Object> tvStructure;

    @FXML
    private TreeView<Object> tvContent;

    private JavaFXStructureExplorerPresentation structureExplorerPresentation;

    private JavaFXContentExplorerPresentation contentExplorerPresentation;

    @FXML
    private void onOpen (final @Nonnull ActionEvent event)
      {
        log.info("open: {}", event);
      }

    public void initialize()
      {
        structureExplorerPresentation = new JavaFXStructureExplorerPresentation(tvStructure);
        contentExplorerPresentation = new JavaFXContentExplorerPresentation(tvContent);

        final ContentExplorerPresentationControl contentExplorerPresentationControl =
                new DefaultContentExplorerPresentationControl(contentExplorerPresentation);
        final StructureExplorerPresentationControl structureExplorerPresentationControl =
                new DefaultStructureExplorerPresentationControl(structureExplorerPresentation);

        contentExplorerPresentationControl.initialize();
        structureExplorerPresentationControl.initialize();
      }
  }
