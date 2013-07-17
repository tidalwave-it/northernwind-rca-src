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
package it.tidalwave.northernwind.rca.ui.structureexplorer.impl;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.core.model.SiteNode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEventMatcher.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class StructureSelectableFiringSelectedEventTest
  {
    private StructureSelectableFiringSelectedEvent fixture;

    private SiteNode node;

    private ClassPathXmlApplicationContext context;

    private MessageBus messageBus;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      throws Exception
      {
        context = new ClassPathXmlApplicationContext("StructureSelectableFiringSelectedEventTestBeans.xml");
        messageBus = context.getBean(MessageBus.class);
        node = mock(SiteNode.class);
        fixture = new StructureSelectableFiringSelectedEvent(node);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_be_a_role_activated_only_in_the_controller_context()
      {
        final DciRole dciRole = fixture.getClass().getAnnotation(DciRole.class);

        assertThat(dciRole, is(not(nullValue())));
        // FIXME assertThat(... is()) is equivalent to isInstanceOf()
        assertEquals(dciRole.context(), DefaultStructureExplorerPresentationControl.class);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_fire_selection_event_on_the_message_bus_when_selected()
      {
        fixture.select();

        verify(messageBus).publish(eventWith(node));
        verifyNoMoreInteractions(messageBus);
      }
}