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
package it.tidalwave.northernwind.rca.ui.impl.javafx.structureeditor;

import javax.annotation.Nonnull;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class JavaFXStructureEditorPresentationDelegate implements StructureEditorPresentation
  {
    @Nonnull
    private final JavaFXBinder binder;

    @FXML
    private Pane structureEditor;

    @FXML
    private WebView structureWebView;

    @FXML
    private TableView<PresentationModel> structureEditorProperties;

    public void initialize()
      {
      }

    @Override
    public void showUp()
      {
        // never used
      }

    @Override
    public void clear()
      {
        structureWebView.getEngine().loadContent("");
        structureEditorProperties.setItems(FXCollections.<PresentationModel>emptyObservableList());
      }

    @Override
    public void populate (final @Nonnull String text)
      {
        structureWebView.getEngine().loadContent(text);
      }

    @Override
    public void populateProperties (final @Nonnull PresentationModel pm)
      {
        binder.bind(structureEditorProperties, pm);
      }
  }
