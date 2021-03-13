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
package it.tidalwave.northernwind.rca.ui.impl.javafx.structureexplorer;

import javax.annotation.Nonnull;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentation;
import lombok.experimental.Delegate;
import lombok.Getter;
import static it.tidalwave.ui.javafx.JavaFXSafeProxyCreator.*;

/***********************************************************************************************************************
 *
 * The JavaFX implementation for {@link StructureExplorerPresentation}.
 *
 * @stereotype Presentation
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class JavaFXStructureExplorerPresentation implements StructureExplorerPresentation
  {
    @Getter @Nonnull
    private final NodeAndDelegate nad = createNodeAndDelegate(getClass());

    @Delegate
    private final StructureExplorerPresentation delegate = nad.getDelegate();
  }
