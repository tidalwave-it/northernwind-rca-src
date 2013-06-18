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
import javafx.scene.control.TreeView;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.As;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.JavaFXBindings;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentation;

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
@Configurable
public class JavaFXStructureExplorerPresentation implements StructureExplorerPresentation
  {
    @Inject @Nonnull
    private JavaFXBindings bindings;

    @Nonnull
    private final TreeView<As> treeView;

    public JavaFXStructureExplorerPresentation (final @Nonnull TreeView<As> treeView)
      {
        this.treeView = treeView;
      }

    @Override
    public void populate (final @Nonnull PresentationModel pm)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                bindings.bind(treeView, pm);
              }
          });
      }
  }
