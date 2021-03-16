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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.contentexplorer.impl;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.core.model.Content;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static it.tidalwave.northernwind.rca.ui.event.ContentSelectedEventMatcher.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class ContentSelectableFiringSelectedEventTest
  {
    private ContentSelectableFiringSelectedEvent underTest;

    private Content content;

    private ClassPathXmlApplicationContext context;

    private MessageBus messageBus;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      throws Exception
      {
        context = new ClassPathXmlApplicationContext("ContentSelectableFiringSelectedEventTestBeans.xml");
        messageBus = context.getBean(MessageBus.class);
        content = mock(Content.class);
        underTest = new ContentSelectableFiringSelectedEvent(messageBus, content);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_be_a_role_activated_only_in_the_controller_context()
      {
        final DciRole dciRole = underTest.getClass().getAnnotation(DciRole.class);

        assertThat(dciRole, is(not(nullValue())));
        // FIXME assertThat(... is()) is equivalent to isInstanceOf()
        assertEquals(dciRole.context(), DefaultContentExplorerPresentationControl.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_fire_selection_event_on_the_message_bus_when_selected()
      {
        underTest.select();

        verify(messageBus).publish(eventWith(content));
        verifyNoMoreInteractions(messageBus);
      }
}