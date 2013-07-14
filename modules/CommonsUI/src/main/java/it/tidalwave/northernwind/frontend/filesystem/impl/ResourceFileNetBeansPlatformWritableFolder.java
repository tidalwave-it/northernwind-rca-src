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
package it.tidalwave.northernwind.frontend.filesystem.impl;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.openide.filesystems.FileObject;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.model.admin.role.WritableFolder;
import it.tidalwave.role.Marshallable;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @stereotype role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datumType = ResourceFileNetBeansPlatform.class) @RequiredArgsConstructor @Slf4j
public class ResourceFileNetBeansPlatformWritableFolder implements WritableFolder
  {
    @Nonnull
    private final ResourceFileNetBeansPlatform file;

    @Override @Nonnull
    public void write (final @Nonnull String fileName, final @Nonnull String text)
      throws IOException
      {
        final @Cleanup Writer w = new OutputStreamWriter(openStream(fileName), "UTF-8");
        w.write(text);
        w.close();
      }

    @Override @Nonnull
    public void write (final @Nonnull String fileName, final @Nonnull Marshallable marshallable)
      throws IOException
      {
        final @Cleanup OutputStream os = openStream(fileName);
        marshallable.marshal(os);
        os.close();
      }

    @Nonnull
    private OutputStream openStream (final @Nonnull String fileName)
      throws IOException
      {
        final FileObject fileObject = file.getDelegate().getFileObject(fileName);
        log.debug("opening stream: {}", fileObject);
        return fileObject.getOutputStream();
      }
  }
