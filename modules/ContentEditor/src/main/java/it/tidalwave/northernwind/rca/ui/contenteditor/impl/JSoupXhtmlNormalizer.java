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
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import it.tidalwave.northernwind.rca.ui.contenteditor.spi.XhtmlNormalizer;
import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import com.google.common.base.Splitter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class JSoupXhtmlNormalizer implements XhtmlNormalizer
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    @Override
    public String asNormalizedString (final @Nonnull String text)
      {
        log.trace("asNormalizedString()\n{}", text);
        final Document.OutputSettings os = new Document.OutputSettings()
                .charset(StandardCharsets.UTF_8)
                .escapeMode(Entities.EscapeMode.xhtml)
                .indentAmount(2)
                .prettyPrint(true)
                .syntax(Document.OutputSettings.Syntax.xml);
        final String result = finalCleanup(breakLongLines(Jsoup.parse(text).outputSettings(os).outerHtml()));
        log.trace(">>>> returning:\n{}", result);
        return result;
      }

    /*******************************************************************************************************************
     *
     * Jsoup doesn't do everything properly, so we're patching the results a bit.
     *
     ******************************************************************************************************************/
    @Nonnull
    private String breakLongLines (final @Nonnull String html)
      {
        final Document document = Jsoup.parse(html);
        document.select("br").after("\n       ");

        // Remove img attributes inserted by Aloha
        document.select("img")
                .removeAttr("draggable")
                .removeAttr("contenteditable")
                .forEach(element ->
          {
            final String style = element.attr("style");

            if (!"".equals(style))
              {
                element.attr("style", style.replaceAll(" *cursor: -webkit-grab;", ""));
              }
          });

        final Document.OutputSettings os = new Document.OutputSettings()
                .charset(StandardCharsets.UTF_8)
                .escapeMode(Entities.EscapeMode.xhtml)
                .indentAmount(2)
                .prettyPrint(false)
                .syntax(Document.OutputSettings.Syntax.xml);
        return document.outputSettings(os).outerHtml()
                .replaceFirst("([^\\n])<html ", "$1\n<html ")
                .replaceFirst("([^\\n]) *<head>", "$1\n  <head>")
                .replaceFirst("([^\\n])\\n<\\/body>", "$1<\\/body>")
                .replaceFirst("<\\/body>([^\\n])", "<\\/body>\n$1");
      }

    /*******************************************************************************************************************
     *
     * Jsoup doesn't do everything properly, so we're patching the results a bit.
     *
     ******************************************************************************************************************/
    @Nonnull
    private static String finalCleanup (final @Nonnull String string)
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
            buffer.append(line.replaceAll(" *$", "")).append("\n"); // trailing spaces
          }

        return buffer.toString();
      }
  }
