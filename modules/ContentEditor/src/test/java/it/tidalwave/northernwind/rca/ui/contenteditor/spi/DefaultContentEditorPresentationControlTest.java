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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.PresentationModelProvider;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.model.impl.admin.AdminContent;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static it.tidalwave.util.test.FileComparisonUtils.*;
import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.DefaultContentEditorPresentationControl.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultContentEditorPresentationControlTest
  {
    private DefaultContentEditorPresentationControl fixture;

    private ContentEditorPresentation presentation;

    private AdminContent content; // FIXME: use Content

    private ResourceProperties properties;

    private PresentationModelProvider pmProvider;

    private PresentationModel pm;

    private EmbeddedServer embeddedServer;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        fixture = new DefaultContentEditorPresentationControl();
        presentation = mock(ContentEditorPresentation.class);
        content = mock(AdminContent.class);
        properties = mock(ResourceProperties.class);
        pmProvider = mock(PresentationModelProvider.class);
        pm = mock(PresentationModel.class);
        embeddedServer = mock(EmbeddedServer.class);

        fixture.documentServer = embeddedServer; // FIXME: use Spring

        when(content.getProperties()).thenReturn(properties);
        when(pmProvider.createPresentationModel(anyVararg())).thenReturn(pm);
        when(properties.as(eq(PresentationModelProvider.class))).thenReturn(pmProvider);

        fixture.initialize(presentation);
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
        verify(presentation).bind(same(fixture.fields));
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
    @Test(enabled = false) // FIXME: must test the interaction with the embedded server
    public void must_populate_the_presentation_on_reception_of_selected_content()
      throws IOException
      {
        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenReturn("full text");
        when(properties.getProperty(eq(PROPERTY_TITLE), anyString())).thenReturn("title");

        reset(presentation);

        fixture.onContentSelected(new ContentSelectedEvent(content));

        verify(presentation).populateDocument(eq("http://localhost:12345/"));
        verify(presentation).populateProperties(same(pm));
        verify(presentation).showUp();
        verifyNoMoreInteractions(presentation);

        verify(embeddedServer).putDocument(eq("/"), eq(new Document().withContent("full text").withMimeType("text/html")));
        verifyNoMoreInteractions(embeddedServer);

        assertThat(fixture.fields.title.get(), is("title"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_clear_the_presentation_on_error()
      throws IOException
      {
        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenThrow(new IOException("test"));

        reset(presentation);

        fixture.onContentSelected(new ContentSelectedEvent(content));

        verify(presentation).clear();
        verifyNoMoreInteractions(presentation);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_load_resource() // TODO: parametrize. test EPILOG
      throws IOException
      {
        final String editorHeader = fixture.loadResource(EDITOR_PROLOG);
        final File file = new File("target/" + EDITOR_PROLOG);
        file.getParentFile().mkdirs();
        final PrintWriter pw = new PrintWriter(file);
        pw.print(editorHeader);
        pw.flush();
        pw.close();

        final File expectedFile = new File("src/main/resources/" + EDITOR_PROLOG);

        assertSameContents(expectedFile, file);
      }
  }