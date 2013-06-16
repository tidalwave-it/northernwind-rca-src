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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.application.Platform;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.northernwind.rca.ui.ContentExplorerPresentation;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.util.As;
import java.util.List;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * The JavaFX implementation for {@link ContentExplorerPresentation}.
 *
 * @stereotype Presentation
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class JavaFXContentExplorerPresentation implements ContentExplorerPresentation
  {
    @Nonnull
    private final TreeView<String> treeView;

    @Override
    public void populate (final @Nonnull PresentationModel pm)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                final TreeItem<String> rootItem = new TreeItem<>("Structure");
                final SimpleComposite<? extends As> composite = pm.as(SimpleComposite.class);
                final List<? extends As> objects = composite.findChildren().results();

                for (final As object : objects)
                  {
                    final TreeItem<String> item = new TreeItem<>(object.as(Displayable.class).getDisplayName());
                    rootItem.getChildren().add(item);
                  }

                treeView.setRoot(rootItem);
              }
          });
      }
  }
