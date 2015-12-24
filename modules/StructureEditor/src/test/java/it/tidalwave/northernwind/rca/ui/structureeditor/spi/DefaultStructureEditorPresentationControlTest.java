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
package it.tidalwave.northernwind.rca.ui.structureeditor.spi;

import java.io.IOException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.role.ui.Presentable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEvent;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
import it.tidalwave.role.ContextManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.role.ui.Presentable.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultStructureEditorPresentationControlTest
  {
    private ClassPathXmlApplicationContext context;

    private DefaultStructureEditorPresentationControl underTest;

    private StructureEditorPresentation presentation;

    private SiteNode siteNode;

    private ResourceProperties properties;

    private Presentable presentable;

    private PresentationModel pm;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        ContextManager.Locator.set(null);
        context = new ClassPathXmlApplicationContext("DefaultStructureEditorPresentationControlTestBeans.xml");
        underTest = context.getBean(DefaultStructureEditorPresentationControl.class);
        presentation = context.getBean(StructureEditorPresentation.class);

        siteNode = mock(SiteNode.class);
        properties = mock(ResourceProperties.class);
        presentable = mock(Presentable.class);
        pm = mock(PresentationModel.class);

        when(siteNode.getProperties()).thenReturn(properties);
        when(presentable.createPresentationModel(anyVararg())).thenReturn(pm);
        when(properties.as(eq(Presentable))).thenReturn(presentable);
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

//    /*******************************************************************************************************************
//     *
//     ******************************************************************************************************************/
//    @Test
//    public void must_bind()
//      {
//        verify(presentation).bind(same(underTest.bindings));
//      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_clear_the_presentation_on_reception_of_an_empty_selection()
      {
        // given
        reset(presentation);
        // when
        underTest.onSiteNodeSelected(SiteNodeSelectedEvent.emptySelectionEvent());
        // then
        verify(presentation).clear();
        verifyNoMoreInteractions(presentation);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_populate_the_presentation_on_reception_of_selected_node()
      throws IOException
      {
        // given
//        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenReturn("full text");
//        when(properties.getProperty(eq(PROPERTY_TITLE), anyString())).thenReturn("title");
        reset(presentation);
        // when
        underTest.onSiteNodeSelected(SiteNodeSelectedEvent.of(siteNode));
        // then
        verify(presentation).populate(matches("Viewer not implemented for .*"));
        verify(presentation).populateProperties(same(pm));
        verify(presentation).showUp();
        verifyNoMoreInteractions(presentation);
//        assertThat(underTest.bindings.document.get(), is("full text"));
//        assertThat(underTest.bindings.title.get(), is("title"));
      }

//    /*******************************************************************************************************************
//     *
//     ******************************************************************************************************************/
//    @Test
//    public void must_clear_the_presentation_on_error()
//      throws IOException
//      {
//        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenThrow(new IOException("test"));
//
//        reset(presentation);
//
//        underTest.onContentSelected(new ContentSelectedEvent(content));
//
//        verify(presentation).clear();
//        verifyNoMoreInteractions(presentation);
//      }
  }