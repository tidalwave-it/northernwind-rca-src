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
package it.tidalwave.northernwind.rca.ui.contenteditor;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;

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
    public static class Fields
      {
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
     * Populates the fields of the presentation.
     *
     * @param  text  the text
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull Fields bean);

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
