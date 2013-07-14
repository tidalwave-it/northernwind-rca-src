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
package it.tidalwave.northernwind.rca.ui.contenteditor.spi;

import java.io.IOException;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.Presentable;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import static it.tidalwave.role.ui.Presentable.*;
import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.DefaultContentEditorPresentationControl.*;
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

    private DefaultContentEditorPresentationControl fixture;

    private ContentEditorPresentation presentation;

    private Content content;

    private ResourceProperties properties;

    private Presentable presentable;

    private PresentationModel pm;

    private EmbeddedServer embeddedServer;

//    private DocumentProxyFactory documentProxyFactory;

    private String registeredUrl = "http://localhost:12345/";

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        context = new ClassPathXmlApplicationContext("DefaultContentEditorPresentationControlTestBeans.xml");
        fixture = context.getBean(DefaultContentEditorPresentationControl.class);
        embeddedServer = context.getBean(EmbeddedServer.class);
        presentation = context.getBean(ContentEditorPresentation.class);
//        documentProxyFactory = context.getBean(DocumentProxyFactory.class);

        content = mock(Content.class);
        properties = mock(ResourceProperties.class);
        presentable = mock(Presentable.class);
        pm = mock(PresentationModel.class);

        when(embeddedServer.putDocument(anyString(), any(Document.class))).thenReturn(registeredUrl);

//        fixture.documentServer = embeddedServer; // FIXME: use Spring
//        fixture.documentProxyFactory = documentProxyFactory;
//        fixture.presentation = presentation;

        when(content.getProperties()).thenReturn(properties);
        when(presentable.createPresentationModel(anyVararg())).thenReturn(pm);
        when(properties.as(eq(Presentable))).thenReturn(presentable);

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
    public void must_bind()
      {
        verify(presentation).bind(same(fixture.bindings));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_clear_the_presentation_on_reception_of_an_empty_selection()
      {
        reset(presentation);

        fixture.onContentSelected(new ContentSelectedEvent());

        verify(presentation).clear();
        verifyNoMoreInteractions(presentation);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_populate_the_presentation_on_reception_of_selected_content()
      throws IOException
      {
        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenReturn("full text");
        when(properties.getProperty(eq(PROPERTY_TITLE), anyString())).thenReturn("title");

        reset(presentation);

        fixture.onContentSelected(new ContentSelectedEvent(content));

//        verify(documentProxyFactory).createDocumentProxy(any(Content.class), eq(PROPERTY_FULL_TEXT));
//        verifyNoMoreInteractions(documentProxyFactory);

        verify(presentation).populateDocument(eq(registeredUrl));
        verify(presentation).populateProperties(same(pm));
        verify(presentation).showUp();
        verifyNoMoreInteractions(presentation);

        verify(embeddedServer).putDocument(eq("/"), eq(new Document().withContent("proxy for: full text")
                                                                      .withMimeType("text/html")));
        verifyNoMoreInteractions(embeddedServer);

        assertThat(fixture.bindings.title.get(), is("title"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
//    @Test FIXME
//    public void must_clear_the_presentation_on_error()
//      throws IOException
//      {
//        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenThrow(new IOException("test"));
//
//        reset(presentation);
//
//        fixture.onContentSelected(new ContentSelectedEvent(content));
//
//        verify(presentation).clear();
//        verifyNoMoreInteractions(presentation);
//      }
  }