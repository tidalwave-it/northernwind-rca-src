/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.ui.impl.javafx.contenteditor;

import javax.annotation.Nonnull;
import javafx.scene.Node;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import lombok.Delegate;
import lombok.Getter;
import static it.tidalwave.ui.javafx.JavaFXSafeProxyCreator.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class JavaFXContentEditorPresentation implements ContentEditorPresentation
  {
    @Getter @Nonnull
    private final Node node;

    @Delegate
    private final ContentEditorPresentation delegate;

    public JavaFXContentEditorPresentation()
      {
        final NodeAndDelegate nad = createNodeAndDelegate(getClass(), "ContentEditorPresentation.fxml");
        node = nad.getNode();
        delegate = nad.getDelegate();
      }
  }
