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
package it.tidalwave.northernwind.rca.ui.siteopener.spi;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import javax.annotation.Nonnull;
import org.mockito.ArgumentMatcher;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import static lombok.AccessLevel.PRIVATE;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(staticName = "openSiteEvent") @AllArgsConstructor(access = PRIVATE)
public class OpenSiteEventMatcher implements ArgumentMatcher<OpenSiteEvent>
  {
    @Wither
    private Path rootPath;

    @Override
    public boolean matches (final @Nullable OpenSiteEvent event)
      {
        return (event != null) && Paths.get(event.getFileSystemProvider().getRootPath()).equals(rootPath);
      }

    @Override @Nonnull
    public String toString()
      {
        return String.format("OpenSiteEvent(%s)", rootPath);
      }
  }
