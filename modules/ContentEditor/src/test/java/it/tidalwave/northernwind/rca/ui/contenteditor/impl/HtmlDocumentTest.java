/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class HtmlDocumentTest
  {
    private static final String ORIGINAL_PROLOG = "<html>\n"
                                                + "<head><meta name=\"prolog\"/></head>\n"
                                                + "<body>\n";
    private static final String ORIGINAL_BODY   = "body\n";
    private static final String ORIGINAL_EPILOG = "</body>\n"
                                                + "</html>\n";

    private HtmlDocument underTest;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        underTest = new HtmlDocument(ORIGINAL_PROLOG, ORIGINAL_BODY, ORIGINAL_EPILOG);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_create_from_text()
      {
        // given
        final String text = ORIGINAL_PROLOG + ORIGINAL_BODY + ORIGINAL_EPILOG;
        // when
        final HtmlDocument result = HtmlDocument.createFromText(text);
        // then
        assertThat(result.getProlog(), is(ORIGINAL_PROLOG));
        assertThat(result.getBody(),   is(ORIGINAL_BODY));
        assertThat(result.getEpilog(), is(ORIGINAL_EPILOG));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_prolog()
      {
        // given
        final String replacedProlog = "<html>\n"
                                    + "<head><meta name=\"replaced prolog\"/></head>\n"
                                    + "<body>\n";
        // when
        final HtmlDocument result = underTest.withProlog(replacedProlog);
        // then
        assertThat(result.getProlog(), is(replacedProlog));
        assertThat(result.getBody(),   is(ORIGINAL_BODY));
        assertThat(result.getEpilog(), is(ORIGINAL_EPILOG));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_body()
      {
        // given
        final String replacedBody = "replaced body\n";
        // when
        final HtmlDocument result = underTest.withBody(replacedBody);
        // then
        assertThat(result.getProlog(), is(ORIGINAL_PROLOG));
        assertThat(result.getBody(),   is(replacedBody));
        assertThat(result.getEpilog(), is(ORIGINAL_EPILOG));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_epilog()
      {
        // given
        final String replacedEpilog = "<script>a script</script>\n"
                                    + "</body>\n"
                                    + "</html>\n";
        // when
        final HtmlDocument result = underTest.withEpilog(replacedEpilog);
        // then
        assertThat(result.getProlog(), is(ORIGINAL_PROLOG));
        assertThat(result.getBody(),   is(ORIGINAL_BODY));
        assertThat(result.getEpilog(), is(replacedEpilog));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_convert_to_string()
      {
        // when
        final String asString = underTest.asString();
        // then
        assertThat(asString, is("<!DOCTYPE html>\n"
                              + "<html>\n"
                              + "  <head>\n"
                              + "    <meta name=\"prolog\" />\n"
                              + "  </head>\n"
                              + "  <body>\n"
                              + "     body\n"
                              + "  </body>\n"
                              + "</html>\n"));
      }
  }