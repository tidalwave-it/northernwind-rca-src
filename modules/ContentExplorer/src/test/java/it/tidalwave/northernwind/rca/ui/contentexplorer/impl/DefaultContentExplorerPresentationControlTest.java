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
package it.tidalwave.northernwind.rca.ui.contentexplorer.impl;

import javax.annotation.Nonnull;
import java.io.IOException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.role.ui.spi.SimpleCompositePresentable;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceFileSystem;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.role.ui.PresentationModelMatcher.*;
import static it.tidalwave.northernwind.rca.ui.event.ContentSelectedEventMatcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static it.tidalwave.util.MockAs.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultContentExplorerPresentationControlTest
  {
    private DefaultContentExplorerPresentationControl fixture;

    private ClassPathXmlApplicationContext context;

    private ContentExplorerPresentation presentation;

    private MessageBus messageBus;

    private ResourceFileSystem fileSystem;

    private ResourceFile root;

    private OpenSiteEvent event;

    private ModelFactory modelFactory;

    private Content content;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        ContextManager.Locator.set(null);
        context = new ClassPathXmlApplicationContext("DefaultContentExplorerPresentationControlTestBeans.xml");

        fixture = context.getBean(DefaultContentExplorerPresentationControl.class);
        presentation = context.getBean(ContentExplorerPresentation.class);
        messageBus = context.getBean(MessageBus.class);
        modelFactory = context.getBean(ModelFactory.class);

        event = mock(OpenSiteEvent.class);
        fileSystem = mock(ResourceFileSystem.class);
        root = mock(ResourceFile.class);
        content = mockWithAsSupport(Content.class, new RoleFactory<Content>()
          {
            @Override @Nonnull
            public Object createRoleFor (final @Nonnull Content content)
              {
                return new SimpleCompositePresentable(content);
              }
          });

        when(fileSystem.findFileByPath(eq("/content/document"))).thenReturn(root);
        when(event.getFileSystem()).thenReturn(fileSystem);
        // FIXME: this is cumbersome
//        when(modelFactory.createContent(eq(root))).thenReturn(content); FIXME!!!
        final Content.Builder.CallBack callBack = mock(Content.Builder.CallBack.class);
        when(callBack.build(any(Content.Builder.class))).thenReturn(content);
        when(modelFactory.createContent()).thenReturn(new Content.Builder(modelFactory, callBack));

        fixture.initialize();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_be_a_MessageSubscriber()
      {
        assertThat(fixture.getClass().getAnnotation(SimpleMessageSubscriber.class), is(not(nullValue())));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void when_a_Site_has_been_opened_must_properly_populate_the_presentation_and_publish_an_empty_selection()
      throws IOException
      {
        reset(messageBus);

        fixture.onOpenSite(event);

        verify(presentation).populate(argThat(presentationModel().withRole(Selectable.class)));
        verify(presentation).expandFirstLevel();
        verifyNoMoreInteractions(presentation);
        verify(messageBus).publish(emptyEvent());
        verifyNoMoreInteractions(messageBus);
      }
  }