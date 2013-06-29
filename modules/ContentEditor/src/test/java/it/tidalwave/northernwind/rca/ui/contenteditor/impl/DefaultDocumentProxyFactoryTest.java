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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.util.test.FileComparisonUtils.assertSameContents;
import static it.tidalwave.northernwind.rca.ui.contenteditor.impl.DefaultDocumentProxyFactory.*;
import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.DefaultContentEditorPresentationControl.*;
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

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_create_a_proper_proxy_document()
      throws IOException
      {
        final ResourceProperties properties = mock(ResourceProperties.class);
        final String html = "<html>\n<head>\n</head>\n<body>\nthe body\n</body>\n</html>";
        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenReturn(html);
        fixture.editorProlog = "prolog\n";
        fixture.editorEpilog = "epilog\n";

        final Document document = fixture.createDocumentProxy(properties, PROPERTY_FULL_TEXT);

        assertThat(document.getContent(), is("prolog\nthe body\nepilog\n"));
        assertThat(document.getMimeType(), is("text/html"));

        // TODO: must test that updates are back-propagated to properties
      }
  }