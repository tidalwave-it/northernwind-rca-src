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
package it.tidalwave.northernwind.frontend.filesystem.impl;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import org.openide.filesystems.FileObject;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.role.io.Marshallable;
import it.tidalwave.northernwind.model.admin.role.WritableFolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @stereotype role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@DciRole(datumType = ResourceFileNetBeansPlatform.class) @RequiredArgsConstructor @Slf4j
public class ResourceFileNetBeansPlatformWritableFolder implements WritableFolder
  {
    @Nonnull
    private final ResourceFileNetBeansPlatform file;

    @Override
    public synchronized void write (@Nonnull final String fileName, @Nonnull final String text)
      throws IOException
      {
        try (final Writer w = new OutputStreamWriter(openStream(fileName), StandardCharsets.UTF_8))
          {
            w.write(text);
          }
      }

    @Override
    public void write (@Nonnull final String fileName, @Nonnull final Marshallable marshallable)
      throws IOException
      {
        try (final OutputStream os = openStream(fileName))
          {
            marshallable.marshal(os);
          }
      }

    @Nonnull
    private OutputStream openStream (@Nonnull final String fileName)
      throws IOException
      {
        FileObject fileObject = file.getDelegate().getFileObject(fileName);

        if (fileObject == null)
          {
            fileObject = file.getDelegate().createData(fileName);
          }

        log.debug("opening stream: {}", fileObject);
        return fileObject.getOutputStream();
      }
  }
