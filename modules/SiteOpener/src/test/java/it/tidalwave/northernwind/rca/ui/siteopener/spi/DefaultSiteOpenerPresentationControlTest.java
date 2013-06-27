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
package it.tidalwave.northernwind.rca.ui.siteopener.spi;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.stubbing.Answer;
import static org.hamcrest.CoreMatchers.*;
import static it.tidalwave.util.ui.UserNotificationWithFeedbackTestHelper.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultSiteOpenerPresentationControlTest
  {
    private DefaultSiteOpenerPresentationControl fixture;

    private ClassPathXmlApplicationContext context;

    private SiteOpenerPresentation presentation;

    private MessageBus messageBus;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        context = new ClassPathXmlApplicationContext("DefaultSiteOpenerPresentationControlTestBeans.xml");

        fixture = context.getBean(DefaultSiteOpenerPresentationControl.class);
        presentation = context.getBean(SiteOpenerPresentation.class);
        messageBus = context.getBean(MessageBus.class);

        fixture.initialize(presentation);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void initialize_must_bind_the_presentation_and_set_the_default_path_to_user_home()
      {
        assertThat(fixture.folderToOpen.get().toFile().getAbsolutePath(), is(System.getProperty("user.home")));

        verify(presentation).bind(same(fixture.action), same(fixture.folderToOpen));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "initialize_must_bind_the_presentation_and_set_the_default_path_to_user_home")
    public void must_fire_an_OpenSiteEvent_when_action_performed_and_the_user_selected_a_folder()
      {
        doAnswer(confirm()).when(presentation).notifyFolderSelectionNeeded(any(UserNotificationWithFeedback.class));

        fixture.action.actionPerformed();

        verify(messageBus).publish(any(OpenSiteEvent.class)); // FIXME: also assert contents
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "initialize_must_bind_the_presentation_and_set_the_default_path_to_user_home")
    public void must_do_nothing_when_action_performed_and_the_user_cancelled_the_selection()
      {
        doAnswer(cancel()).when(presentation).notifyFolderSelectionNeeded(any(UserNotificationWithFeedback.class));

        fixture.action.actionPerformed();

        verifyZeroInteractions(messageBus);
      }

    // FIXME: for stylistic consistency, CONFIRM and CANCEL should really be static methods
    private static Answer<Void> confirm()
      {
        return CONFIRM;
      }

    private static Answer<Void> cancel()
      {
        return CANCEL;
      }
  }