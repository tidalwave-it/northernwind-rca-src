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
package it.tidalwave.northernwind.rca.ui.structureexplorer.impl;

import javax.annotation.Nonnull;
import java.io.IOException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.ui.spi.SimpleCompositePresentable;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceFileSystem;
import it.tidalwave.northernwind.core.model.Site;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.role.ui.PresentationModelMatcher.*;
import static it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEventMatcher.*;
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
public class DefaultStructureExplorerPresentationControlTest
  {
    private DefaultStructureExplorerPresentationControl fixture;

    private ClassPathXmlApplicationContext context;

    private StructureExplorerPresentation presentation;

    private MessageBus messageBus;

    private ResourceFileSystem fileSystem;

    private ResourceFile root;

    private OpenSiteEvent event;

    private ModelFactory modelFactory;

    private SiteNode node;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      throws Exception
      {
        ContextManager.Locator.set(null);
        context = new ClassPathXmlApplicationContext("DefaultStructureExplorerPresentationControlTestBeans.xml");

        fixture = context.getBean(DefaultStructureExplorerPresentationControl.class);
        presentation = context.getBean(StructureExplorerPresentation.class);
        messageBus = context.getBean(MessageBus.class);
        modelFactory = context.getBean(ModelFactory.class);

        event = mock(OpenSiteEvent.class);
        fileSystem = mock(ResourceFileSystem.class);
        root = mock(ResourceFile.class);
        node = mockWithAsSupport(SiteNode.class, new RoleFactory<SiteNode>()
          {
            @Override @Nonnull
            public Object createRoleFor (final @Nonnull SiteNode node)
              {
                return new SimpleCompositePresentable(node);
              }
          });

        when(fileSystem.findFileByPath(eq("/structure"))).thenReturn(root);
        when(event.getFileSystem()).thenReturn(fileSystem);
        when(modelFactory.createSiteNode(any(Site.class), eq(root))).thenReturn(node);

        fixture.initialize();
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
        reset(messageBus);

        fixture.onOpenSite(event);

        verify(presentation).populate(argThat(presentationModel().withRole(Selectable.class)));
        verify(presentation).expandFirstLevel();
        verifyNoMoreInteractions(presentation);
        verify(messageBus).publish(emptyEvent());
        verifyNoMoreInteractions(messageBus);
      }
  }