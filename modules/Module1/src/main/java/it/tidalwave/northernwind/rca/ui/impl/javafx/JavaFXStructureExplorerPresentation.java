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
import javafx.scene.control.TreeView;
import javafx.application.Platform;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.northernwind.rca.ui.StructureExplorerPresentation;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * The JavaFX implementation for {@link StructureExplorerPresentation}.
 *
 * @stereotype Presentation
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class JavaFXStructureExplorerPresentation implements StructureExplorerPresentation
  {
    @Nonnull
    private final TreeView<Object> treeView;

    private final TreeItemFactory treeItemFactory = new DefaultTreeItemFactory();

    @Override
    public void populate (final @Nonnull PresentationModel pm)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                treeView.setRoot(treeItemFactory.createTreeItem(pm));
              }
          });
      }
  }
