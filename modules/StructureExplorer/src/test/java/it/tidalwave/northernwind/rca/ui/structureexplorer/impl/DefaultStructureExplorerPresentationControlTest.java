/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.any;
import static it.tidalwave.util.MockAs.*;
import static it.tidalwave.role.ui.PresentationModelMatcher.*;
import static it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEventMatcher.*;
import static it.tidalwave.northernwind.rca.ui.structureexplorer.impl.DefaultStructureExplorerPresentationControl.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultStructureExplorerPresentationControlTest
  {
    private DefaultStructureExplorerPresentationControl underTest;

    private ClassPathXmlApplicationContext context;

    private StructureExplorerPresentation presentation;

    private MessageBus messageBus;

    private ResourceFileSystem fileSystem;

    private ResourceFile root;

    private OpenSiteEvent openSiteEvent;

    private ModelFactory modelFactory;

    private SiteNode node;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      throws Exception
      {
        ContextManager.Locator.set(null);
        context = new ClassPathXmlApplicationContext("DefaultStructureExplorerPresentationControlTestBeans.xml");

        underTest = context.getBean(DefaultStructureExplorerPresentationControl.class);
        presentation = context.getBean(StructureExplorerPresentation.class);
        messageBus = context.getBean(MessageBus.class);
        modelFactory = context.getBean(ModelFactory.class);

        openSiteEvent = mock(OpenSiteEvent.class);
        fileSystem = mock(ResourceFileSystem.class);
        root = mock(ResourceFile.class);
        node = mockWithAsSupport(SiteNode.class, (RoleFactory<SiteNode>)(n -> new SimpleCompositePresentable(n)));

        when(fileSystem.findFileByPath(eq(ROOT_SITE_NODE_PATH))).thenReturn(root);
        when(openSiteEvent.getFileSystem()).thenReturn(fileSystem);
        when(modelFactory.createSiteNode(any(Site.class), eq(root))).thenReturn(node);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @AfterMethod
    public void tearDown()
      {
        context.close();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_be_a_MessageSubscriber()
      {
        assertThat(underTest.getClass().getAnnotation(SimpleMessageSubscriber.class), is(not(nullValue())));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void when_a_Site_has_been_opened_must_properly_populate_the_presentation_and_publish_an_empty_selection()
      throws IOException
      {
        // given
        reset(messageBus);
        // when
        underTest.onOpenSite(openSiteEvent);
        // then
        verify(presentation).populate(argThat(presentationModel().withRole(Selectable.class)));
        verify(presentation).expandFirstLevel();
        verifyNoMoreInteractions(presentation);
        verify(messageBus).publish(emptySelectionEvent());
        verifyNoMoreInteractions(messageBus);
      }
  }