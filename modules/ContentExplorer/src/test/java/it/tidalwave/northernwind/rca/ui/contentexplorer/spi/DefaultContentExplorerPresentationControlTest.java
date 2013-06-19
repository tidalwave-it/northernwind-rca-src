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
package it.tidalwave.northernwind.rca.ui.contentexplorer.spi;

import java.io.IOException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.role.ui.PresentationModelProvider;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.role.ui.spi.SimplePresentationModelProvider;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceFileSystem;
import it.tidalwave.northernwind.model.impl.admin.AdminContent;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.role.ui.PresentationModelMatcher.*;
import static it.tidalwave.northernwind.rca.ui.event.ContentSelectedEventMatcher.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

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

    private AdminContent content; // FIXME: use Content

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        context = new ClassPathXmlApplicationContext("DefaultContextExplorerPresentationControlTestBeans.xml");

        fixture = context.getBean(DefaultContentExplorerPresentationControl.class);
        presentation = context.getBean(ContentExplorerPresentation.class);
        messageBus = context.getBean(MessageBus.class);
        modelFactory = context.getBean(ModelFactory.class);

        event = mock(OpenSiteEvent.class);
        fileSystem = mock(ResourceFileSystem.class);
        root = mock(ResourceFile.class);
        content = mock(AdminContent.class);

        when(fileSystem.findFileByPath(eq("/content/document"))).thenReturn(root);
        when(event.getFileSystem()).thenReturn(fileSystem);
        when(modelFactory.createContent(eq(root))).thenReturn(content);
        when(content.as(eq(PresentationModelProvider.class))).thenReturn(new SimplePresentationModelProvider(content));

        fixture.initialize(presentation);
      }

//    private void registerMock (final @Nonnull DefaultListableBeanFactory context,
//                               final @Nonnull String name,
//                               final @Nonnull Class<?> mockClass)
//      {
//        context.registerBeanDefinition(name, BeanDefinitionBuilder.rootBeanDefinition(Mockito.class)
//                .setFactoryMethod("mock").addConstructorArgValue(mockClass.getName()).getBeanDefinition());
//      }

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
        fixture.onOpenSite(event);

        verify(presentation).populate(argThat(presentationModel().withRole(Selectable.class)));
        verify(messageBus).publish(emptyEvent());
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_have_injected_a_Selectable_that_fires_the_proper_selection_message()
      {
        final Object role = fixture.publisherRoleFactory.createRoleFor(content);
        assertThat(role, is(instanceOf(Selectable.class)));

        final Selectable selectable = (Selectable)role;
        selectable.select();

        verify(messageBus).publish(eventWith(content));
      }

  }