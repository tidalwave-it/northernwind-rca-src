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
import javafx.scene.Node;
import javafx.application.Platform;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
import lombok.Delegate;
import static it.tidalwave.ui.javafx.JavaFXSafeProxyCreator.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class JavaFXStructureEditorPresentation implements StructureEditorPresentation
  {
    @CheckForNull
    private Node node;

    @Delegate
    private StructureEditorPresentation delegate;

    @Nonnull
    public Node getNode()
      throws IOException
      {
        assert Platform.isFxApplicationThread();

        if (node == null)
          {
            final NodeAndDelegate nad = createNodeAndDelegate(getClass(), "StructureEditorPresentation.fxml");
            node = nad.getNode();
            delegate = nad.getDelegate();
          }

        return node;
      }
  }
