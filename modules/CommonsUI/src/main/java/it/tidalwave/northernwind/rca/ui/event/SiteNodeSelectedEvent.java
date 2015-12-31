/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
import java.util.Optional;
import it.tidalwave.northernwind.core.model.SiteNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import static lombok.AccessLevel.PRIVATE;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = PRIVATE) @ToString
public class SiteNodeSelectedEvent
  {
    @Getter @Nonnull
    private final Optional<SiteNode> siteNode;

    @Nonnull
    public static SiteNodeSelectedEvent emptySelectionEvent()
      {
        return new SiteNodeSelectedEvent(Optional.empty());
      }

    @Nonnull
    public static SiteNodeSelectedEvent of (final @Nonnull SiteNode siteNode)
      {
        return new SiteNodeSelectedEvent(Optional.of(siteNode));
      }
  }
