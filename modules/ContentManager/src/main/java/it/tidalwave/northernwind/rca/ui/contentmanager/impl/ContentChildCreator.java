/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import javax.annotation.Nonnull;
import java.util.Map;
import java.io.IOException;
import it.tidalwave.util.Key;
import it.tidalwave.northernwind.core.model.Content;

/***********************************************************************************************************************
 *
 * A role for a {@link Content} that can create a child {@code Content} item.
 *
 * @stereotype role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface ContentChildCreator
  {
    public static final Class<ContentChildCreator> ContentChildCreator = ContentChildCreator.class;

    /*******************************************************************************************************************
     *
     * Create a child {@link Content} item inside the given folder and with the given properties.
     *
     * @param  folderName       the name of the folder to create
     * @param  propertyValues   the property values
     *
     ******************************************************************************************************************/
    @Nonnull
    public Content createContent (@Nonnull String folderName,
                                  @Nonnull Map<Key<?>, Object> propertyValues)
      throws IOException;
  }
