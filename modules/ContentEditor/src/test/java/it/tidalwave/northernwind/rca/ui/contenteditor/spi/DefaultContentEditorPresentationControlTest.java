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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.contenteditor.spi;

import java.util.Optional;
import java.io.IOException;
import org.springframework.context.ConfigurableApplicationContext;
import it.tidalwave.util.Key;
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
import it.tidalwave.northernwind.util.test.SpringTestHelper;
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
 *
 **********************************************************************************************************************/
public class DefaultContentEditorPresentationControlTest
  {
    private final SpringTestHelper helper = new SpringTestHelper(this);

    private ConfigurableApplicationContext context;

    private DefaultContentEditorPresentationControl underTest;

    private ContentEditorPresentation presentation;

    private Content content;

    private ResourceProperties properties;

    private Presentable propertiesPresentable;

    private PresentationModel propertiesPm;

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
        context = (ConfigurableApplicationContext)helper.createSpringContext();
        underTest = context.getBean(DefaultContentEditorPresentationControl.class);
        embeddedServer = context.getBean(EmbeddedServer.class);
        presentation = context.getBean(ContentEditorPresentation.class);

        content = mock(Content.class);
        properties = mock(ResourceProperties.class);
        propertiesPresentable = mock(Presentable.class);
        propertiesPm = mock(PresentationModel.class);
        propertyBinder = mock(PropertyBinder.class);

        when(embeddedServer.putDocument(anyString(), any(Document.class))).thenReturn(registeredUrl);

        when(content.getProperties()).thenReturn(properties);
        when(propertiesPresentable.createPresentationModel()).thenCallRealMethod();
        when(propertiesPresentable.createPresentationModel(anyCollection())).thenReturn(propertiesPm);
        when(properties.as(eq(_Presentable_))).thenReturn(propertiesPresentable);
        when(properties.as(eq(_PropertyBinder_))).thenReturn(propertyBinder);

        document = new Document().withContent("proxy for: full text")
                                 .withMimeType("text/html");

        when(propertyBinder.createBoundDocument(any(Key.class), any(PropertyBinder.UpdateCallback.class)))
                           .thenAnswer(__ -> document);

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
        // when just initialized
        // then
        verify(presentation).bind(same(underTest.bindings));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_clear_the_presentation_on_reception_of_an_empty_selection()
      {
        // given
        reset(presentation);
        // when
        underTest.onContentSelected(emptySelectionEvent());
        // then
        verify(presentation).clear();
//        verify(presentation).bind(same(underTest.bindings));
        verifyNoMoreInteractions(presentation);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_populate_the_presentation_and_bind_properties_on_reception_of_selected_content()
      {
        // given
        when(properties.getProperty(eq(PROPERTY_FULL_TEXT))).thenReturn(Optional.of("full text"));
        when(properties.getProperty(eq(PROPERTY_TITLE))).thenReturn(Optional.of("title"));
        reset(presentation);
        // when
        underTest.onContentSelected(ContentSelectedEvent.of(content));
        // then
        verify(propertyBinder).bind(eq(PROPERTY_TITLE), same(underTest.bindings.title), same(underTest.propertyUpdateCallback));
        verify(propertyBinder).createBoundDocument(eq(PROPERTY_FULL_TEXT), same(underTest.propertyUpdateCallback));
        verifyNoMoreInteractions(propertyBinder);

        verify(presentation).showUp();
        verify(presentation).populateProperties(same(propertiesPm));
        verify(presentation).populateDocument(eq(registeredUrl));
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
      {
        // given
        when(properties.getProperty(eq(PROPERTY_TITLE))).thenThrow(new IOException("test"));

        reset(presentation);
        // when
        underTest.onContentSelected(ContentSelectedEvent.of(content));
        // then
        verify(presentation).clear();
        verifyNoMoreInteractions(presentation);
      }
  }