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

import it.tidalwave.northernwind.core.model.Content;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ContentSelectedEventTest
  {
    @Test
    public void must_represent_an_empty_selection_when_constructed_with_no_arguments()
      {
        final ContentSelectedEvent fixture = new ContentSelectedEvent();

        assertThat(fixture.isEmptySelection(), is(true));
      }

    @Test(expectedExceptions = NullPointerException.class)
    public void must_throw_NPE_when_getSiteNode_invoked_on_an_empty_selection()
      {
        final ContentSelectedEvent fixture = new ContentSelectedEvent();

        fixture.getContent();
      }

    @Test
    public void must_return_the_original_SiteNode_when_constructed_with_arguments()
      {
        final Content content = mock(Content.class);
        final ContentSelectedEvent fixture = new ContentSelectedEvent(content);

        assertThat(fixture.isEmptySelection(), is(false));
        assertThat(fixture.getContent(), is(sameInstance(content)));
      }
  }