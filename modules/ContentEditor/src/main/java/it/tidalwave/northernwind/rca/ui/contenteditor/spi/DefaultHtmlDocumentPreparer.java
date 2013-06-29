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
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;
import org.springframework.core.io.ClassPathResource;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document.UpdateListener;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultHtmlDocumentPreparer implements HtmlDocumentPreparer
  {
    @VisibleForTesting final static String EDITOR_PROLOG =
            "it/tidalwave/northernwind/rca/ui/contenteditor/spi/EditorProlog.txt";

    @VisibleForTesting final static String EDITOR_EPILOG =
            "it/tidalwave/northernwind/rca/ui/contenteditor/spi/EditorEpilog.txt";

    private String editorProlog = "";

    private String editorEpilog = "";

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
                if (!line.contains("</body"))
                  {
                    bodyBuilder.append(line).append("\n");
                  }
                else
                  {
                    epilogBuilder.append(line).append("\n");
                  }

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

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @PostConstruct
    public void initialize()
      {
        editorProlog = loadResource(EDITOR_PROLOG);
        editorEpilog = loadResource(EDITOR_EPILOG);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public Document prepareForEditing (final @Nonnull String text)
      {
        final HtmlDocument originalDocument = createSplitDocument(text);
        final HtmlDocument editableDocument = originalDocument.withProlog(editorProlog)
                                                              .withEpilog(editorEpilog);
        // FIXME: mime type - XHTML?
        final Document document = new Document().withMimeType("text/html")
                                                .withContent(editableDocument.asString())
                                                .withUpdateListener(new UpdateListener()
          {
            @Override
            public void update (final @Nonnull String content)
              {
                final HtmlDocument editedDocument = originalDocument.withBody(content);
                // FIXME: needs to be pretty printed
                log.warn("TO DO: STORE: {}", editedDocument.asString());
              }
          });

        return document;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnull
    @VisibleForTesting HtmlDocument createSplitDocument (final @Nonnull String text)
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
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    @VisibleForTesting String loadResource (final @Nonnull String path)
      {
        try
          {
            final ClassPathResource resource = new ClassPathResource(path);
            final @Cleanup Reader r = new InputStreamReader(resource.getInputStream());
            return CharStreams.toString(r);
          }
        catch (IOException e)
          {
            throw new RuntimeException(e);
          }
      }
  }
