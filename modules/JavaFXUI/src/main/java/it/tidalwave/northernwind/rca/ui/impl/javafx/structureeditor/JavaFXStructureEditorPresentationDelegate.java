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
package it.tidalwave.northernwind.rca.ui.impl.javafx.structureeditor;

import it.tidalwave.role.ui.javafx.StackPaneSelector;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable
public class JavaFXStructureEditorPresentationDelegate implements StructureEditorPresentation
  {
    @Inject @Nonnull
    private JavaFXBinder binder;

    @Inject @Nonnull
    private StackPaneSelector stackPaneSelector;

    @FXML
    private Pane structureEditor;

    @FXML
    private WebView structureWebView;

    @FXML
    private TableView<PresentationModel> structureEditorProperties;

    public void initialize()
      {
        binder.bindColumn(structureEditorProperties, 0, "name");
        binder.bindColumn(structureEditorProperties, 1, "value");
      }

    @Override
    public void showUp()
      {
        stackPaneSelector.setShownNode(structureEditor);
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
