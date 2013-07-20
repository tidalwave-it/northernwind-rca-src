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
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import java.util.Collection;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.contentmanager.CreateContentRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class CreateContentRequestActionProviderTest
  {
    private ApplicationContext context;

    private CreateContentRequestActionProvider fixture;

    private Content content;

    private MessageBus messageBus;

    @BeforeMethod
    public void setupFixture()
      {
        context = new ClassPathXmlApplicationContext("...");
        messageBus = context.getBean(MessageBus.class);
        content = mock(Content.class);
        fixture = new CreateContentRequestActionProvider(content);
      }

    @Test
    public void must_return_a_proper_New_Content_action()
      {
        final Collection<? extends UserAction> actions = fixture.getActions();

        assertThat(actions, is(not(nullValue())));
        assertThat(actions.size(), is(1));
        final UserAction action = actions.iterator().next();
        assertThat(action, is(same(fixture.sendCreateContentRequestAction)));
        assertThat(action.as(Displayable.class).getDisplayName(), is("New content"));
      }

    @Test
    public void the_action_must_publish_a_proper_CreateContentRequest()
      {
        fixture.sendCreateContentRequestAction.actionPerformed();

        verify(messageBus).publish(eq(new CreateContentRequest(content)));
        verifyNoMoreInteractions(messageBus);
      }
}