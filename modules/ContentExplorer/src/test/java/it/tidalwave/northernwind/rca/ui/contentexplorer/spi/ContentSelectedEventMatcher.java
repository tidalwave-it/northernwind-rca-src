/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.contentexplorer.spi;

import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Matchers;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentSelectedEventMatcher extends BaseMatcher<ContentSelectedEvent>
  {
    private final boolean emptySelection;

    private final Content content;

    @Nonnull
    public static ContentSelectedEvent emptyEvent()
      {
        return Matchers.argThat(new ContentSelectedEventMatcher(true, null));
      }

    @Nonnull
    public static ContentSelectedEvent eventWith (final @Nonnull Content content)
      {
        return Matchers.argThat(new ContentSelectedEventMatcher(false, content));
      }

    @Override public boolean matches (Object item)
      {
        if (!(item instanceof ContentSelectedEvent))
          {
            return false;
          }

        final ContentSelectedEvent event = (ContentSelectedEvent)item;

        if (emptySelection)
          {
            return event.isEmptySelection();
          }

        return event.getContent() == content;
      }

    @Override public void describeTo (Description description)
      {
      }
  }
