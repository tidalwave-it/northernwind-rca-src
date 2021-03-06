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
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import lombok.Getter;

/***********************************************************************************************************************
 *
 * The JavaFX delegate for the main GUI of the application. It assembles together all the pieces of the UI.
 *
 * @author Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class JavaFXApplicationPresentationDelegate
  {
    @FXML @Getter
    private StackPane stackPane;

    @FXML
    private SplitPane pnVerticalSplit;

    // Those below aren't used here, but replicated in JavaFXSiteOpenerPresentation
    @FXML
    private Button btOpen;

    @FXML
    private MenuItem openSiteMenu;

    public void setLeftVerticalSplitContents (@Nonnull final Node ... nodes)
      {
        pnVerticalSplit.getItems().setAll(nodes);
      }
  }
