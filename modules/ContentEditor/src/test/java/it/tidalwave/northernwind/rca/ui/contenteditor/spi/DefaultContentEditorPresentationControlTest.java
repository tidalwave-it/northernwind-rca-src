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
package it.tidalwave.northernwind.rca.ui.contenteditor.spi;

import java.io.IOException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.Presentable;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import static it.tidalwave.role.ui.Presentable.*;
import static it.tidalwave.northernwind.model.admin.Properties.*;
import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.PropertyBinder.*;
import static it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent.emptySelectionEvent;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultContentEditorPresentationControlTest
  {
    private ClassPathXmlApplicationContext context;

    private DefaultContentEditorPresentationControl underTest;

    private ContentEditorPresentation presentation;

    private Content content;

    private ResourceProperties properties;

    private Presentable presentable;

    private PresentationModel pm;

    private PropertyBinder propertyBinder;

    private EmbeddedServer embeddedServer;

    private Document document;

    private String registeredUrl = "http://localhost:12345/";

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        ContextManager.Locator.set(null);
        context = new ClassPathXmlApplicationContext("DefaultContentEditorPresentationControlTestBeans.xml");
        underTest = context.getBean(DefaultContentEditorPresentationControl.class);
        embeddedServer = context.getBean(EmbeddedServer.class);
        presentation = context.getBean(ContentEditorPresentation.class);

        content = mock(Content.class);
        properties = mock(ResourceProperties.class);
        presentable = mock(Presentable.class);
        pm = mock(PresentationModel.class);
        propertyBinder = mock(PropertyBinder.class);

        when(embeddedServer.putDocument(anyString(), any(Document.class))).thenReturn(registeredUrl);

        when(content.getProperties()).thenReturn(properties);
        when(presentable.createPresentationModel(anyVararg())).thenReturn(pm);
        when(properties.as(eq(Presentable))).thenReturn(presentable);
        when(properties.as(eq(PropertyBinder))).thenReturn(propertyBinder);

        document = new Document().withContent("proxy for: full text")
                                 .withMimeType("text/html");

        when(propertyBinder.createBoundDocument(any(Key.class), any(PropertyBinder.UpdateCallback.class)))
                           .thenAnswer(invocation -> document);

        underTest.initialize();
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
    public void must_bind()
      {
        verify(presentation).bind(same(underTest.bindings));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_clear_the_presentation_on_reception_of_an_empty_selection()
      {
        reset(presentation);

        underTest.onContentSelected(emptySelectionEvent());

        verify(presentation).clear();
//        verify(presentation).bind(same(underTest.bindings));
        verifyNoMoreInteractions(presentation);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_populate_the_presentation_and_bind_properties_on_reception_of_selected_content()
      throws IOException, NotFoundException
      {
        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenReturn("full text");
        when(properties.getProperty(eq(PROPERTY_TITLE), anyString())).thenReturn("title");
        reset(presentation);

        underTest.onContentSelected(ContentSelectedEvent.of(content));

        verify(propertyBinder).bind(eq(PROPERTY_TITLE), same(underTest.bindings.title), same(underTest.propertyUpdateCallback));
        verify(propertyBinder).createBoundDocument(eq(PROPERTY_FULL_TEXT), same(underTest.propertyUpdateCallback));
        verifyNoMoreInteractions(propertyBinder);

        verify(presentation).populateDocument(eq(registeredUrl));
        verify(presentation).populateProperties(same(pm));
        verify(presentation).showUp();
        verify(presentation).bind(same(underTest.bindings));
        verifyNoMoreInteractions(presentation);

        verify(embeddedServer).putDocument(eq("/"), eq(document));
        verifyNoMoreInteractions(embeddedServer);

//        assertThat(underTest.bindings.title.get(), is("title")); no need for this, binding is separatedly tested
      }

    // TODO: test propertyUpdateCallback

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(enabled = false)
    public void must_clear_the_presentation_on_error()
      throws IOException, NotFoundException
      {
        when(properties.getProperty(eq(PROPERTY_TITLE))).thenThrow(new IOException("test"));
        when(properties.getProperty(eq(PROPERTY_TITLE), anyString())).thenThrow(new IOException("test"));

        reset(presentation);

        underTest.onContentSelected(ContentSelectedEvent.of(content));

        verify(presentation).clear();
        verifyNoMoreInteractions(presentation);
      }
  }