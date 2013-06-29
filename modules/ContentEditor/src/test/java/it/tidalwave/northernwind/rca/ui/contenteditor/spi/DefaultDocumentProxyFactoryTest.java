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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static it.tidalwave.util.test.FileComparisonUtils.assertSameContents;
import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.DefaultDocumentProxyFactory.*;

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
    public void must_properly_split_a_document()
      {
        final String text = "<html>\n<head>\n</head>\n<body>\nthe body\n</body>\n</html>";

        final HtmlDocument result = fixture.createSplitDocument(text);

        assertThat(result.getProlog(), is("<html>\n<head>\n</head>\n<body>\n"));
        assertThat(result.getBody(), is("the body\n"));
        assertThat(result.getEpilog(), is("</body>\n</html>\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_prolog()
      {
        final HtmlDocument fixture = new HtmlDocument("prolog\n", "body\n", "epilog\n");
        final HtmlDocument result = fixture.withProlog("replaced prolog\n");

        assertThat(result.asString(), is("replaced prolog\nbody\nepilog\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_body()
      {
        final HtmlDocument fixture = new HtmlDocument("prolog\n", "body\n", "epilog\n");
        final HtmlDocument result = fixture.withBody("replaced body\n");

        assertThat(result.asString(), is("prolog\nreplaced body\nepilog\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_epilog()
      {
        final HtmlDocument fixture = new HtmlDocument("prolog\n", "body\n", "epilog\n");
        final HtmlDocument result = fixture.withEpilog("replaced epilog\n");

        assertThat(result.asString(), is("prolog\nbody\nreplaced epilog\n"));
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