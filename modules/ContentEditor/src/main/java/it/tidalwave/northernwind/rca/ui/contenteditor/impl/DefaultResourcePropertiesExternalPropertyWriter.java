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
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import it.tidalwave.util.Key;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.model.impl.admin.AdminContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datum = AdminContent.class)
@RequiredArgsConstructor @Slf4j
public class DefaultResourcePropertiesExternalPropertyWriter implements ExternalPropertyWriter
  {
    @Nonnull
    private final AdminContent content;

    @Override
    public void writeProperty (final @Nonnull Key<String> propertyName, final @Nonnull String value)
      {
        try
          {
            final ResourceFile file = content.getFile();
            final TextWriter writer = new ResourceFileTextWriter(file); // FIXME: file.as(TextWriter);
            // FIXME: localization
            writer.write(propertyName.stringValue() + "_en.xhtml", value);
          }
        catch (Exception e)
          {
            log.error("", e);
          }
      }
  }
