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
import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentationControl;
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
    private StackPaneSelector stackPaneSelector;

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
        stackPaneSelector.initialize(stackPane);
        stackPaneSelector.add(load(JavaFXContentEditorPresentationHandler.class, "ContentEditorPresentation.fxml"));
        stackPaneSelector.add(load(JavaFXStructureEditorPresentationHandler.class, "StructureEditorPresentation.fxml"));

        pnVerticalSplit.getItems().add(load(JavaFXStructureExplorerPresentationHandler.class, "StructureExplorerPresentation.fxml"));
        pnVerticalSplit.getItems().add(load(JavaFXContentExplorerPresentationHandler.class, "ContentExplorerPresentation.fxml"));

        // FIXME: this should be delegated to other handlers, as already done for the Editors
        siteOpenerPresentationControl.initialize(createInstance(JavaFXSiteOpenerPresentation.class, this));
      }

    @Nonnull
    private Node load (final @Nonnull Class<?> clazz, final @Nonnull String resourceName)
      throws IOException
      {
        return FXMLLoader.load(clazz.getResource(resourceName));
      }
  }
