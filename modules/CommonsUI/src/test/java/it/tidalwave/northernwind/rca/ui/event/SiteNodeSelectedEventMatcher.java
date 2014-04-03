/*
 * #%L
 * *********************************************************************************************************************
 *
 * These Foolish Things - Miscellaneous utilities
 * http://thesefoolishthings.java.net - hg clone https://bitbucket.org/tidalwave/thesefoolishthings-src
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

import it.tidalwave.northernwind.core.model.SiteNode;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Matchers;

/***********************************************************************************************************************
 *
 * FIXME: this class is expensive to write
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SiteNodeSelectedEventMatcher extends BaseMatcher<SiteNodeSelectedEvent>
  {
    private final boolean emptySelection;

    private final SiteNode node;

    @Nonnull
    public static SiteNodeSelectedEvent emptyEvent()
      {
        return Matchers.argThat(new SiteNodeSelectedEventMatcher(true, null));
      }

    @Nonnull
    public static SiteNodeSelectedEvent eventWith (final @Nonnull SiteNode node)
      {
        return Matchers.argThat(new SiteNodeSelectedEventMatcher(false, node));
      }

    @Override public boolean matches (Object item)
      {
        if (!(item instanceof SiteNodeSelectedEvent))
          {
            return false;
          }

        final SiteNodeSelectedEvent event = (SiteNodeSelectedEvent)item;

        if (emptySelection)
          {
            return event.isEmptySelection();
          }

        return event.getSiteNode() == node;
      }

    @Override public void describeTo (Description description)
      {
      }
  }
