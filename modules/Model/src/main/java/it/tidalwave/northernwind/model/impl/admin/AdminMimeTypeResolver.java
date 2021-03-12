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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.model.impl.admin;

import javax.annotation.Nonnull;
import it.tidalwave.northernwind.core.model.MimeTypeResolver;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * FIXME: required by the ResourceFileSystem - only for getMimeType(). Introduce a simple MimeTypeResolver. See NW-184
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class AdminMimeTypeResolver implements MimeTypeResolver
  {
    @Override @Nonnull
    public String getMimeType (final @Nonnull String fileName)
      {
        log.warn("MIME type resolver with temporary implementation");

        // FIXME: use a map
        if (fileName.endsWith(".html"))
          {
            return "text/xhtml";
          }

        if (fileName.endsWith(".css"))
          {
            return "text/css";
          }

        if (fileName.endsWith(".png"))
          {
            return "image/png";
          }

        if (fileName.endsWith(".gif"))
          {
            return "image/gif";
          }

        if (fileName.endsWith(".jpg"))
          {
            return "image/jpg";
          }

        if (fileName.endsWith(".js"))
          {
            return "application/x-javascript";
          }

        return "content/unknown";
      }
  }
