/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
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
public class SiteNodeSelectedEventTest
  {
    @Test
    public void must_represent_an_empty_selection_when_constructed_with_no_arguments()
      {
        final SiteNodeSelectedEvent underTest = new SiteNodeSelectedEvent();

        assertThat(underTest.isEmptySelection(), is(true));
      }

    @Test(expectedExceptions = NullPointerException.class)
    public void must_throw_NPE_when_getSiteNode_invoked_on_an_empty_selection()
      {
        final SiteNodeSelectedEvent underTest = new SiteNodeSelectedEvent();

        underTest.getSiteNode();
      }

    @Test
    public void must_return_the_original_SiteNode_when_constructed_with_arguments()
      {
        final SiteNode siteNode = mock(SiteNode.class);
        final SiteNodeSelectedEvent underTest = new SiteNodeSelectedEvent(siteNode);

        assertThat(underTest.isEmptySelection(), is(false));
        assertThat(underTest.getSiteNode(), is(sameInstance(siteNode)));
      }
  }