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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentation;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.mockito.stubbing.Answer;
import static it.tidalwave.util.ui.UserNotificationWithFeedbackTestHelper.*;
import static it.tidalwave.northernwind.rca.ui.siteopener.spi.OpenSiteEventMatcher.*;
import static org.hamcrest.CoreMatchers.*;
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
    private File folder;

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

        verify(presentation).bind(same(fixture.openSiteAction), same(fixture.folderToOpen));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "pathsProvider",
          dependsOnMethods = "initialize_must_bind_the_presentation_and_set_the_default_path_to_user_home")
    public void must_fire_an_OpenSiteEvent_when_action_performed_and_the_user_selected_a_folder (final String path)
      {
        doAnswer(confirm()).when(presentation).notifyFolderSelectionNeeded(any(UserNotificationWithFeedback.class));
        fixture.folderToOpen.set(new File(path).toPath());

        fixture.openSiteAction.actionPerformed();

        verify(messageBus).publish(argThat(openSiteEvent().withRootPath(path)));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "initialize_must_bind_the_presentation_and_set_the_default_path_to_user_home")
    public void must_do_nothing_when_action_performed_and_the_user_cancelled_the_selection()
      {
        doAnswer(cancel()).when(presentation).notifyFolderSelectionNeeded(any(UserNotificationWithFeedback.class));

        fixture.openSiteAction.actionPerformed();

        verifyZeroInteractions(messageBus);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @DataProvider(name = "pathsProvider")
    private Object[][] pathsProvider() throws IOException
      {
        final String[][] result = new String[10][1];

        for (int i = 0; i < 10; i++)
          {
            folder = Files.createTempDirectory("SampleFolder-").getFileName().toFile();
            folder.mkdirs();
            result[i][0] = folder.getAbsolutePath();
          }

        return result;
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