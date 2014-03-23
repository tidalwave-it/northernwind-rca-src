/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
 * %%
 * Copyright (C) 2013 - 2014 Tidalwave s.a.s. (http://tidalwave.it)
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
import com.google.common.annotations.VisibleForTesting;
import it.tidalwave.northernwind.core.model.ResourceFileSystem;
import it.tidalwave.northernwind.core.model.Site;
import it.tidalwave.northernwind.frontend.filesystem.basic.LocalFileSystemProvider;
import lombok.Getter;

/***********************************************************************************************************************
 *
 * An event that represents the opening of a {@link Site}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class OpenSiteEvent
  {
    @Getter @Nonnull
    private final ResourceFileSystem fileSystem;

    @VisibleForTesting @Getter @Nonnull
    private final LocalFileSystemProvider fileSystemProvider;

    public OpenSiteEvent (final @Nonnull Path folder)
      throws IOException
      {
        fileSystemProvider = new LocalFileSystemProvider();
        fileSystemProvider.setRootPath(folder.toFile().getAbsolutePath());
        fileSystem = fileSystemProvider.getFileSystem();
      }

    @Override @Nonnull
    public String toString()
      {
        return String.format("OpenSiteEvent(root = %s)", fileSystemProvider.getRootPath());
      }
  }
