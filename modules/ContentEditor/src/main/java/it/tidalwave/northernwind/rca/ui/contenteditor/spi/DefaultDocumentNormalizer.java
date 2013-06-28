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

import javax.annotation.Nonnull;
import com.google.common.base.Splitter;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultDocumentNormalizer implements DocumentNormalizer
  {
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
                if (!line.contains("<body"))
                  {
                    prologBuilder.append(line).append("\n");
                  }
                else
                  {
                    bodyBuilder.append(line).append("\n");
                  }

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
                bodyBuilder.append(line).append("\n");
                return line.contains("</body") ? EPILOG : BODY;
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

    @Override @Nonnull
    public TheDoc prepareForEditing (final @Nonnull String text)
      {
        final StringBuilder prologBuilder = new StringBuilder();
        final StringBuilder bodyBuilder = new StringBuilder();
        final StringBuilder epilogBuilder = new StringBuilder();

        State state = State.PROLOG;

        for (final String line : Splitter.on("\n").trimResults().split(text))
          {
            state = state.process(line, prologBuilder, bodyBuilder, epilogBuilder);
          }

        return new TheDoc(prologBuilder.toString(), bodyBuilder.toString(), epilogBuilder.toString());
      }
  }
