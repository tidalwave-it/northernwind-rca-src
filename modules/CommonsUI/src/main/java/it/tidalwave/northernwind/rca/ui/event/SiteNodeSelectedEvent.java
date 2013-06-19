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
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import it.tidalwave.northernwind.core.model.SiteNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @ToString(doNotUseGetters = true)
public class SiteNodeSelectedEvent
  {
    @Nullable
    private final SiteNode siteNode;

    @Getter
    private final boolean emptySelection;

    public SiteNodeSelectedEvent()
      {
        this.siteNode = null;
        this.emptySelection = true;
      }

    public SiteNodeSelectedEvent (final @Nonnull SiteNode siteNode)
      {
        this.siteNode = siteNode;
        this.emptySelection = false;
      }

    @Nonnull
    public SiteNode getSiteNode()
      {
        return Preconditions.checkNotNull(siteNode);
      }
  }
