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
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.model.impl.admin.AdminContent;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.northernwind.rca.ui.contenteditor.impl.DefaultDocumentProxyFactory.*;
import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.DefaultContentEditorPresentationControl.*;
import static it.tidalwave.util.test.FileComparisonUtils.assertSameContents;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultDocumentProxyFactoryTest
  {
    private DefaultDocumentProxyFactory fixture;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        fixture = new DefaultDocumentProxyFactory();
        fixture.initialize();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_initialize()
      throws IOException
      {
        final File prolog = new File("target/test-results/" + EDITOR_PROLOG);
        final File epilog = new File("target/test-results/" + EDITOR_EPILOG);
        writeToFile(prolog, fixture.editorProlog);
        writeToFile(epilog, fixture.editorEpilog);

        final File expectedProlog = new File("src/main/resources/" + EDITOR_PROLOG);
        final File expectedEpilog = new File("src/main/resources/" + EDITOR_EPILOG);
        assertSameContents(expectedProlog , prolog);
        assertSameContents(expectedEpilog, epilog);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_create_a_proper_proxy_document()
      throws IOException
      {
        final Content content = mock(AdminContent.class); // FIXME: use Content
        final ResourceProperties properties = mock(ResourceProperties.class);
        when(content.getProperties()).thenReturn(properties);

        final String html = "<html>\n<head>\n</head>\n<body>\nthe body\n</body>\n</html>";
        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenReturn(html);

        final Document document = fixture.createDocumentProxy(content, PROPERTY_FULL_TEXT);

        assertThat(document.getMimeType(), is("text/html"));

        final File file = new File("target/test-results/DocumentProxy.txt");
        final File expectedFile = new File("src/test/resources/ExpectedDocumentProxy.txt");
        writeToFile(file, document.getContent());
        assertSameContents(expectedFile, file);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_create_a_proxy_document_that_properly_updates_properties()
      throws IOException
      {
        final AdminContent content = mock(AdminContent.class); // FIXME: use Content
        final ResourceProperties properties = mock(ResourceProperties.class);
        when(content.getProperties()).thenReturn(properties);

        final ExternalPropertyWriter externalPropertyWriter = mock(ExternalPropertyWriter.class);
        when(content.as(eq(ExternalPropertyWriter.class))).thenReturn(externalPropertyWriter);

        final String html = "<html>\n<head>\n</head>\n<body>\nthe body\n</body>\n</html>";
        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenReturn(html);

        final Document document = fixture.createDocumentProxy(content, PROPERTY_FULL_TEXT);
        document.update("the updated body\n");

        // TODO: partially implemented
        final String expectedHtml = "<!doctype html>\n<html>\n<head>\n</head>\n<body>\nthe updated body\n</body>\n</html>";
        verify(externalPropertyWriter).writeProperty(eq(PROPERTY_FULL_TEXT), eq(expectedHtml));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private void writeToFile (final @Nonnull File file, final @Nonnull String editorHeader)
      throws FileNotFoundException
      {
        file.getParentFile().mkdirs();
        final PrintWriter pw = new PrintWriter(file);
        pw.print(editorHeader);
        pw.flush();
        pw.close();
      }
  }