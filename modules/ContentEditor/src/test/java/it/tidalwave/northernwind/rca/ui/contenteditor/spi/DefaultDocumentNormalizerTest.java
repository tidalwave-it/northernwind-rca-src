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

import it.tidalwave.northernwind.rca.ui.contenteditor.spi.DocumentNormalizer.TheDoc;
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
public class DefaultDocumentNormalizerTest
  {
    private DefaultDocumentNormalizer fixture;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        fixture = new DefaultDocumentNormalizer();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_split_a_document()
      {
        final String text = "<html>\n<head>\n</head>\n<body>\nthe body\n</body>\n</html>";

        final TheDoc result = fixture.prepareForEditing(text);

        assertThat(result.getProlog(), is("<html>\n<head>\n</head>\n"));
        assertThat(result.getBody(), is("<body>\nthe body\n</body>\n"));
        assertThat(result.getEpilog(), is("</html>\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_prolog()
      {
        final TheDoc fixture = new TheDoc("prolog\n", "body\n", "epilog\n");
        final TheDoc result = fixture.withProlog("replaced prolog\n");

        assertThat(result.asString(), is("replaced prolog\nbody\nepilog\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_body()
      {
        final TheDoc fixture = new TheDoc("prolog\n", "body\n", "epilog\n");
        final TheDoc result = fixture.withBody("replaced body\n");

        assertThat(result.asString(), is("prolog\nreplaced body\nepilog\n"));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_replace_epilog()
      {
        final TheDoc fixture = new TheDoc("prolog\n", "body\n", "epilog\n");
        final TheDoc result = fixture.withEpilog("replaced epilog\n");

        assertThat(result.asString(), is("prolog\nbody\nreplaced epilog\n"));
      }
  }