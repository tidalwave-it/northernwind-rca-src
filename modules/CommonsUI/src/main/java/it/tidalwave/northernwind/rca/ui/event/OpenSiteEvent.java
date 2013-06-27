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
package it.tidalwave.northernwind.rca.ui.event;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import it.tidalwave.northernwind.core.model.ResourceFileSystem;
import it.tidalwave.northernwind.frontend.filesystem.basic.LocalFileSystemProvider;
import lombok.Getter;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * An event that represents the opening of a {@link Site}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Getter @ToString
public class OpenSiteEvent
  {
    @Getter
    private final ResourceFileSystem fileSystem;

    public OpenSiteEvent (final @Nonnull Path folder)
      throws IOException
      {
        final LocalFileSystemProvider fileSystemProvider = new LocalFileSystemProvider();
        fileSystemProvider.setRootPath(folder.toFile().getAbsolutePath());
        fileSystem = fileSystemProvider.getFileSystem();
      }
  }
