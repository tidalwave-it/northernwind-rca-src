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
    private HtmlDocument underTest;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        underTest = new HtmlDocument("<html>\n"
                                   + "<head><meta name=\"prolog\"/></head>\n"
                                   + "<body>\n",
                                     "body\n",
                                     "</body>\n"
                                   + "</html>");
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_create_from_text()
      {
        // given
        final String text = "<html>\n"
                          + "<head>\n"
                          + "</head>\n"
                          + "<body>\n"
                          + "the body\n"
                          + "</body>\n"
                          + "</html>";
        // when
        final HtmlDocument underTest = HtmlDocument.createFromText(text);
        // then
        assertThat(underTest.getProlog(), is("<html>\n"
                                           + "<head>\n"
                                           + "</head>\n"
                                           + "<body>\n"));
        assertThat(underTest.getBody(), is("the body\n"));
        assertThat(underTest.getEpilog(), is("</body>\n"
                                           + "</html>\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_prolog()
      {
        // when
        final HtmlDocument result = underTest.withProlog("<html>\n"
                                                       + "<head><meta name=\"replaced prolog\"/></head>\n"
                                                       + "<body>\n");
        // then
        assertThat(result.asString(), is("<!DOCTYPE html>\n"
                                       + "<html>\n"
                                       + "  <head>\n"
                                       + "    <meta name=\"replaced prolog\" />\n"
                                       + "  </head>\n"
                                       + "  <body>\n"
                                       + "     body\n"
                                       + "  </body>\n"
                                       + "</html>\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_body()
      {
        // when
        final HtmlDocument result = underTest.withBody("replaced body\n");
        // then
        assertThat(result.asString(), is("<!DOCTYPE html>\n"
                                       + "<html>\n"
                                       + "  <head>\n"
                                       + "    <meta name=\"prolog\" />\n"
                                       + "  </head>\n"
                                       + "  <body>\n"
                                       + "     replaced body\n"
                                       + "  </body>\n"
                                       + "</html>\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    // epilog replacement is only meaningful together with prolog replacement
    @Test(dependsOnMethods = "must_properly_replace_prolog")
    public void must_properly_replace_epilog()
      {
        // when
        final HtmlDocument result = underTest.withProlog("<html>\n"
                                                       + "<head><meta name=\"prolog\"/></head>\n"
                                                       + "<body>\n"
                                                       + "<div>\n")
                                             .withEpilog("</div>\n"
                                                       + "</body>\n"
                                                       + "</html>");
        // then
        assertThat(result.asString(), is("<!DOCTYPE html>\n"
                                       + "<html>\n"
                                       + "  <head>\n"
                                       + "    <meta name=\"prolog\" />\n"
                                       + "  </head>\n"
                                       + "  <body>\n"
                                       + "    <div>\n"
                                       + "       body\n"
                                       + "    </div>\n"
                                       + "  </body>\n"
                                       + "</html>\n"));
      }
  }