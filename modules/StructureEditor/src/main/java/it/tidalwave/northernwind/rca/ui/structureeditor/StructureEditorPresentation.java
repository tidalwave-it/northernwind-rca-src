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
package it.tidalwave.northernwind.rca.ui.structureeditor;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.PresentationModel;

/***********************************************************************************************************************
 *
 * The Presentation of the Structure Editor.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface StructureEditorPresentation
  {
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
     * Populates the text of the {@link Content}.
     *
     * @param  text  the text
     *
     ******************************************************************************************************************/
    public void populate (@Nonnull String text);

    /*******************************************************************************************************************
     *
     * Populates the {@link ResourceProperties} of the {@link Content}.
     *
     * @param  pmProperties  the {@link PresentationModel} of the properties
     *
     ******************************************************************************************************************/
    public void populateProperties (@Nonnull PresentationModel pmProperties);
  }
