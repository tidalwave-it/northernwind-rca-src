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

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.CharStreams;
import org.springframework.core.io.ClassPathResource;
import it.tidalwave.util.Key;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document.UpdateListener;
import it.tidalwave.northernwind.rca.ui.contenteditor.spi.DocumentProxyFactory;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultDocumentProxyFactory implements DocumentProxyFactory
  {
    @VisibleForTesting final static String EDITOR_PROLOG =
            "it/tidalwave/northernwind/rca/ui/contenteditor/spi/EditorProlog.txt";

    @VisibleForTesting final static String EDITOR_EPILOG =
            "it/tidalwave/northernwind/rca/ui/contenteditor/spi/EditorEpilog.txt";

    @VisibleForTesting String editorProlog = "";

    @VisibleForTesting String editorEpilog = "";

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
    public Document createDocumentProxy (final @Nonnull Content content,
                                         final @Nonnull Key<String> propertyName)
      {
        try
          {
            final ResourceProperties properties = content.getProperties();
            final String text = properties.getProperty(propertyName, "");
            final HtmlDocument originalDocument = HtmlDocument.createFromText(text);
            final HtmlDocument editableDocument = originalDocument.withProlog(editorProlog)
                                                                  .withEpilog(editorEpilog);
            // FIXME: mime type - XHTML?
            return new Document().withMimeType("text/html")
                                 .withContent(editableDocument.asString())
                                 .withUpdateListener(new UpdateListener()
              {
                @Override
                public void onUpdate (final @Nonnull String text)
                  {
                    final HtmlDocument editedDocument = originalDocument.withBody(text);
//                    final ResourceProperties newProperties = properties.withProperty(propertyName, editedDocument.asString());
                    content.as(ExternalPropertyWriter.class).writeProperty(propertyName, editedDocument.asString());
                  }
              });
          }
        catch (IOException e)
          {
            throw new RuntimeException(e);
          }
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
            final @Cleanup Reader r = new InputStreamReader(new ClassPathResource(path).getInputStream(), "UTF-8");
            return CharStreams.toString(r);
          }
        catch (IOException e)
          {
            throw new RuntimeException(e);
          }
      }
  }
