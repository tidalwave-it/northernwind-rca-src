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

import javax.annotation.Nonnull;
import com.google.common.base.Splitter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A container for HTML text that allow substitution of prolog and epilog in order to prepare a HTML document for
 * editing.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = AccessLevel.PACKAGE) @Getter @EqualsAndHashCode @ToString @Slf4j
public class HtmlDocument
  {
    @Wither @Nonnull
    private final String prolog;

    @Wither @Nonnull
    private final String body;

    @Wither @Nonnull
    private final String epilog;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    enum State
      {
        PROLOG
          {
            @Override
            State process (final @Nonnull String line,
                           final @Nonnull StringBuilder prologBuilder,
                           final @Nonnull StringBuilder bodyBuilder,
                           final @Nonnull StringBuilder epilogBuilder)
              {
                prologBuilder.append(line).append("\n");
                return line.contains("<body") ? BODY : PROLOG;
              }
          },

        BODY
          {
            @Override
            State process (final @Nonnull String line,
                           final @Nonnull StringBuilder prologBuilder,
                           final @Nonnull StringBuilder bodyBuilder,
                           final @Nonnull StringBuilder epilogBuilder)
              {
                final boolean containsEndBody = line.contains("</body");
                (containsEndBody ? epilogBuilder : bodyBuilder).append(line).append("\n");
                return containsEndBody ? EPILOG : BODY;
              }
          },

        EPILOG
          {
            @Override
            State process (final @Nonnull String line,
                           final @Nonnull StringBuilder prologBuilder,
                           final @Nonnull StringBuilder bodyBuilder,
                           final @Nonnull StringBuilder epilogBuilder)
              {
                epilogBuilder.append(line).append("\n");
                return EPILOG;
              }
          };

        abstract State process (@Nonnull String line,
                                @Nonnull StringBuilder prologBuilder,
                                @Nonnull StringBuilder bodyBuilder,
                                @Nonnull StringBuilder epilogBuilder);
      }

    /*******************************************************************************************************************
     *
     * Creates a new document from a string.
     *
     * @param       text        the string
     * @return                  the document
     *
     ******************************************************************************************************************/
    @Nonnull
    public static HtmlDocument createFromText (final @Nonnull String text)
      {
        final StringBuilder prologBuilder = new StringBuilder();
        final StringBuilder bodyBuilder = new StringBuilder();
        final StringBuilder epilogBuilder = new StringBuilder();

        State state = State.PROLOG;

        for (final String line : Splitter.on("\n").trimResults().split(text))
          {
            state = state.process(line, prologBuilder, bodyBuilder, epilogBuilder);
          }

        return new HtmlDocument(prologBuilder.toString(), bodyBuilder.toString(), epilogBuilder.toString());
      }

    /*******************************************************************************************************************
     *
     * Returns the contents as a string, after having being normalised
     *
     * @return      the string
     *
     ******************************************************************************************************************/
    @Nonnull
    public String asString()
      {
        return new XhtmlNormalizer().asNormalizedString(prolog + body + epilog);
      }
  }

