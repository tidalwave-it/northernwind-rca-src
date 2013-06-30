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
import java.lang.reflect.Field;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.openide.filesystems.FileUtil;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourcePath;
import it.tidalwave.northernwind.frontend.filesystem.impl.ResourceFileSystemNetBeansPlatform;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class ResourceFileTextWriter implements TextWriter
  {
    @Nonnull
    private final ResourceFile file;

    @Override @Nonnull
    public void write (@Nonnull String fileName, @Nonnull String text)
      throws IOException
      {
        try
          {
            final ResourceFileSystemNetBeansPlatform fileSystem = (ResourceFileSystemNetBeansPlatform)file.getFileSystem();
            final Field field = fileSystem.getClass().getDeclaredField("fileSystem");
            field.setAccessible(true);
            org.openide.filesystems.FileSystem delegate = (org.openide.filesystems.FileSystem)field.get(fileSystem);
            final File root = FileUtil.toFile(delegate.getRoot());
            final ResourcePath path = file.getPath().appendedWith(fileName);
            final File f = new File(root, path.asString());
            log.warn("write: {} {}", f.getAbsolutePath(), text);
            final @Cleanup Writer w = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
            w.write(text);
            w.close();
          }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
          {
            log.error("", e);
          }
      }
  }
