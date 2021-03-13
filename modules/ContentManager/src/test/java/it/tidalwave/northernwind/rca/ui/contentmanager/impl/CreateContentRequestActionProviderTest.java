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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import java.util.Collection;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.spi.DefaultContextManagerProvider;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.event.CreateContentRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.role.Displayable.Displayable;
import static org.hamcrest.CoreMatchers.*;
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
    private CreateContentRequestActionProvider underTest;

    private Content content;

    private MessageBus messageBus;

    @BeforeMethod
    public void setup()
      {
        ContextManager.Locator.set(new DefaultContextManagerProvider());
        messageBus = mock(MessageBus.class);
        content = mock(Content.class);
        underTest = new CreateContentRequestActionProvider(content);
        underTest.messageBus = messageBus;
      }

    @Test
    public void must_provide_only_a_New_Content_action()
      {
        // when just initialized
        final Collection<? extends UserAction> actions = underTest.getActions();
        // then
        assertThat(actions, is(not(nullValue())));
        assertThat(actions.size(), is(1));
        final UserAction action = actions.iterator().next();
        assertThat(action, is(sameInstance(underTest.sendCreateContentRequestAction)));
        assertThat(action.as(Displayable).getDisplayName(), is("New content"));
      }

    @Test
    public void the_action_must_publish_a_proper_CreateContentRequest()
      {
        // when just initialized
        underTest.sendCreateContentRequestAction.actionPerformed();
        // then
        verify(messageBus).publish(eq(CreateContentRequest.of(content)));
        verifyNoMoreInteractions(messageBus);
      }
  }