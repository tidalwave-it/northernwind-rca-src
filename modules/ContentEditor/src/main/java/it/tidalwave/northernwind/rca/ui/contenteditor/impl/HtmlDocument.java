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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import it.tidalwave.northernwind.rca.ui.contenteditor.spi.XhtmlNormalizer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import static lombok.AccessLevel.PACKAGE;

/***********************************************************************************************************************
 *
 * A container for HTML text that allow substitution of prolog and epilog in order to prepare a HTML document for
 * editing. It keeps an internal representation where the prolog, the body and the epilog are separately stored.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Immutable
@RequiredArgsConstructor(access = PACKAGE)
@EqualsAndHashCode @ToString @Slf4j
public class HtmlDocument
  {
    @Getter @With @Nonnull
    private final String prolog;

    @Getter @With @Nonnull
    private final String body;

    @Getter @With @Nonnull
    private final String epilog;

    @Nonnull
    private final XhtmlNormalizer normalizer;

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
            State process (@Nonnull final String line,
                           @Nonnull final StringBuilder prologBuilder,
                           @Nonnull final StringBuilder bodyBuilder,
                           @Nonnull final StringBuilder epilogBuilder)
              {
                prologBuilder.append(line).append("\n");
                return line.contains("<body") ? BODY : PROLOG;
              }
          },

        BODY
          {
            @Override
            State process (@Nonnull final String line,
                           @Nonnull final StringBuilder prologBuilder,
                           @Nonnull final StringBuilder bodyBuilder,
                           @Nonnull final StringBuilder epilogBuilder)
              {
                final boolean containsEndBody = line.contains("</body");
                (containsEndBody ? epilogBuilder : bodyBuilder).append(line).append("\n");
                return containsEndBody ? EPILOG : BODY;
              }
          },

        EPILOG
          {
            @Override
            State process (@Nonnull final String line,
                           @Nonnull final StringBuilder prologBuilder,
                           @Nonnull final StringBuilder bodyBuilder,
                           @Nonnull final StringBuilder epilogBuilder)
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
    public static HtmlDocument createFromText (@Nonnull final String text)
      {
        final StringBuilder prologBuilder = new StringBuilder();
        final StringBuilder bodyBuilder = new StringBuilder();
        final StringBuilder epilogBuilder = new StringBuilder();

        State state = State.PROLOG;

        for (final String line : text.split("\\n"))
          {
            state = state.process(line, prologBuilder, bodyBuilder, epilogBuilder);
          }

        return new HtmlDocument(prologBuilder.toString(),
                                bodyBuilder.toString(),
                                epilogBuilder.toString(),
                                new JSoupXhtmlNormalizer());
      }

    /*******************************************************************************************************************
     *
     * Returns the contents as a string, after having being normalised.
     *
     * @return      the string
     *
     ******************************************************************************************************************/
    @Nonnull
    public String asString()
      {
        return normalizer.asNormalizedString(prolog + body + epilog);
      }
  }

