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
package it.tidalwave.northernwind.rca.ui.contenteditor.spi;

import javax.annotation.Nonnull;
import it.tidalwave.util.Key;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface DocumentProxyFactory
  {
    /*******************************************************************************************************************
     *
     * Creates a document that can be used as a proxy to edit a textual property.
     *
     * @param  properties       the properties
     * @param  propertyName     the property name
     * @return                  the document
     *
     ******************************************************************************************************************/
    @Nonnull
    public Document createDocumentProxy (@Nonnull ResourceProperties properties, @Nonnull Key<String> propertyName);
  }
