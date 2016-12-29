/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
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
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SiteNodeSelectedEventMatcher implements ArgumentMatcher<SiteNodeSelectedEvent>
  {
    @Nonnull
    private final Optional<SiteNode> node;

    @Nonnull
    public static SiteNodeSelectedEvent emptySelectionEvent()
      {
        return ArgumentMatchers.argThat(new SiteNodeSelectedEventMatcher(Optional.empty()));
      }

    @Nonnull
    public static SiteNodeSelectedEvent eventWith (final @Nonnull SiteNode node)
      {
        return ArgumentMatchers.argThat(new SiteNodeSelectedEventMatcher(Optional.of(node)));
      }

    @Override
    public boolean matches (final SiteNodeSelectedEvent event)
      {
        return (event != null) && this.node.equals(event.getSiteNode());
      }

    @Override @Nonnull
    public String toString()
      {
        return String.format("SiteNodeSelectedEvent(%s)", node);
      }
  }
