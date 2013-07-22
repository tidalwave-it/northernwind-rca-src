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
    private HtmlDocument fixture;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        fixture = new HtmlDocument("<html>\n<head>prolog</head>\n<body>\n",
                                   "body\n",
                                   "</body>\n</html>");
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_create_from_text()
      {
        final String text = "<html>\n<head>\n</head>\n<body>\nthe body\n</body>\n</html>";

        final HtmlDocument fixture = HtmlDocument.createFromText(text);

        assertThat(fixture.getProlog(), is("<html>\n<head>\n</head>\n<body>\n"));
        assertThat(fixture.getBody(), is("the body\n"));
        assertThat(fixture.getEpilog(), is("</body>\n</html>\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_prolog()
      {
        final HtmlDocument result = fixture.withProlog("<html>\n<head>replaced prolog</head>\n<body>\n");

        assertThat(result.asString(), is("<!doctype html>\n<html>\n<head>replaced prolog</head>\n<body>\n" +
                                         "body\n" +
                                         "</body>\n</html>"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_body()
      {
        final HtmlDocument result = fixture.withBody("replaced body\n");

        assertThat(result.asString(), is("<!doctype html>\n<html>\n<head>prolog</head>\n<body>\n" +
                                         "replaced body\n" +
                                         "</body>\n</html>"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_epilog()
      {
        final HtmlDocument result = fixture.withEpilog("</body>\n<!-- replaced -->\n</html>");

        assertThat(result.asString(), is("<!doctype html>\n<html>\n<head>prolog</head>\n<body>\n" +
                                         "body\n" +
                                         "</body>\n<!-- replaced -->\n</html>"));
      }
  }