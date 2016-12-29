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

import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.util.Optional;
import it.tidalwave.northernwind.core.model.Content;
import lombok.RequiredArgsConstructor;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import static lombok.AccessLevel.PRIVATE;

/***********************************************************************************************************************
 *
 * FIXME: this class is expensive to write
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = PRIVATE)
public class ContentSelectedEventMatcher implements ArgumentMatcher<ContentSelectedEvent>
  {
    @Nonnull
    private final Optional<Content> content;

    @Nonnull
    public static ContentSelectedEvent emptyEvent()
      {
        return ArgumentMatchers.argThat(new ContentSelectedEventMatcher(Optional.empty()));
      }

    @Nonnull
    public static ContentSelectedEvent eventWith (final @Nonnull Content content)
      {
        return ArgumentMatchers.argThat(new ContentSelectedEventMatcher(Optional.of(content)));
      }

    @Override
    public boolean matches (final @Nullable ContentSelectedEvent event)
      {
        return (event != null) && this.content.equals(event.getContent());
      }

    @Override @Nonnull
    public String toString()
      {
        return String.format("ContentSelectedEvent(%s)", content);
      }
  }
