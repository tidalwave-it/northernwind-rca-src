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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import it.tidalwave.ui.javafx.JavaFXSafeProxyCreator;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentation;
import lombok.Delegate;

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
public class JavaFXStructureExplorerPresentation implements StructureExplorerPresentation
  {
    @CheckForNull
    private Node node;

    @Delegate
    private StructureExplorerPresentation delegate;

    @Nonnull
    public Node getNode()
//      throws IOException FIXME
      {
        assert Platform.isFxApplicationThread();

        if (node == null)
          {
            try
              {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource("StructureExplorerPresentation.fxml"));
                node = (Node)loader.load();
                delegate = JavaFXSafeProxyCreator.createSafeProxy((StructureExplorerPresentation)loader.getController(),
                                                                  StructureExplorerPresentation.class);
              }
            catch (IOException e)
              {
                throw new RuntimeException(e);
              }
          }

        return node;
      }
  }
