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
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentationControl;
import it.tidalwave.role.ui.PresentationModel;
import static it.tidalwave.role.ui.javafx.impl.JavaFXSafeComponentBuilder.createInstance;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable
public class JavaFXStructureEditorPresentationHandler
  {
    @Inject @Nonnull
    private StructureEditorPresentationControl structureEditorPresentationControl;

    @FXML
    private Pane structureEditor;

    @FXML
    private WebView structureWebView;

    @FXML
    private TableView<PresentationModel> structureEditorProperties;

    public void initialize()
      {
        structureEditorPresentationControl.initialize(createInstance(JavaFXStructureEditorPresentation.class, this));
      }
  }