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
package it.tidalwave.northernwind.rca.ui.contenteditor;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * The Presentation of the {@link Content} Editor.
 *
 * @stereotype Presentation
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface ContentEditorPresentation
  {
    @RequiredArgsConstructor
    public static class Bindings
      {
        @Nonnull
        public final UserAction openExternalEditor;

        public final BoundProperty<String> title = new BoundProperty<>("");
      }

    /*******************************************************************************************************************
     *
     * Makes sure this presentation is visible on the UI.
     *
     ******************************************************************************************************************/
    public void showUp();

    /*******************************************************************************************************************
     *
     * Clears the presentation.
     *
     ******************************************************************************************************************/
    public void clear();

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull Bindings bindings);

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public void populateDocument (@Nonnull String url);

    /*******************************************************************************************************************
     *
     * Populates the {@link ResourceProperties} of the {@link Content}.
     *
     * @param  pmProperties  the {@link PresentationModel} of the properties
     *
     ******************************************************************************************************************/
    public void populateProperties (@Nonnull PresentationModel pmProperties);
  }
