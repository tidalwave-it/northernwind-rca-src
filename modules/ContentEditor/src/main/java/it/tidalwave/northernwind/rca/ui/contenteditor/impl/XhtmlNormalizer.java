/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2014 Tidalwave s.a.s. (http://tidalwave.it)
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
import com.google.common.base.Splitter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class XhtmlNormalizer
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public String asNormalizedString (final @Nonnull String text)
      {
        log.trace("asNormalizedString()\n{}", text);
        final Document document = Jsoup.parse(text);
        document.outputSettings().indentAmount(2).prettyPrint(true);
        final String result = postNormalized(document.outerHtml());
        log.trace(">>>> returning:\n{}", result);
        return result;
      }

    /*******************************************************************************************************************
     *
     * Jsoup doesn't do everything properly, so we're patching the results a bit.
     *
     ******************************************************************************************************************/
    @Nonnull
    private static String postNormalized (final @Nonnull String string)
      {
        final StringBuilder buffer = new StringBuilder();

        boolean first = true;

        for (final String line : Splitter.on("\n").split(string))
          {
            if (first && !line.equals("<!DOCTYPE html>"))
              {
                buffer.append("<!DOCTYPE html>").append("\n");
              }

            first = false;
            buffer.append(line.replaceAll(" *$", "")).append("\n");
          }

        return buffer.toString().replaceAll("\n$", "");
      }
  }
