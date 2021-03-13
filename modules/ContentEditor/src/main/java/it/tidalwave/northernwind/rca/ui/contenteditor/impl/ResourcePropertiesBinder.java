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

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.ClassPathResource;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.ui.contenteditor.spi.PropertyBinder;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datumType = ResourceProperties.class)
@Configurable @RequiredArgsConstructor
public class ResourcePropertiesBinder implements PropertyBinder
  {
    private final static String EDITOR_TEMPLATE =
            "it/tidalwave/northernwind/rca/ui/contenteditor/spi/EditorTemplate.xhtml";

    @Nonnull
    private final ResourceProperties properties;

    /* visible for testing */ final static String EDITOR_PROLOG;

    /* visible for testing */ final static String EDITOR_EPILOG;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    static
      {
        try (final BufferedReader r = new BufferedReader(new InputStreamReader(
                    new ClassPathResource(EDITOR_TEMPLATE).getInputStream(), "UTF-8")))
          {
            final StringBuilder prologBuilder = new StringBuilder();
            final StringBuilder epilogBuilder = new StringBuilder();
            boolean prolog = true;

            for (String line = r.readLine(); line != null; line = r.readLine())
              {
                if (line.trim().equals("<!-- split here -->"))
                  {
                    prolog = false;
                    continue;
                  }

                (prolog ? prologBuilder : epilogBuilder).append(line).append("\n");
              }

            EDITOR_PROLOG = prologBuilder.toString();
            EDITOR_EPILOG = epilogBuilder.toString();
          }
        catch (Exception e)
          {
            throw new ExceptionInInitializerError(e);
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <T> void bind (final @Nonnull Key<T> propertyName,
                          final @Nonnull BoundProperty<T> boundProperty,
                          final @Nonnull UpdateCallback callback)
      throws NotFoundException, IOException
      {
        boundProperty.set(properties.getProperty(propertyName));
        boundProperty.addPropertyChangeListener(
                event -> callback.notify(properties.withProperty(propertyName, boundProperty.get())));
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public EmbeddedServer.Document createBoundDocument (final @Nonnull Key<String> propertyName,
                                                        final @Nonnull UpdateCallback callback)
      {
        try
          {
            final String text = properties.getProperty(propertyName, "");
            final HtmlDocument originalDocument = HtmlDocument.createFromText(text);
            final HtmlDocument editableDocument = originalDocument.withProlog(EDITOR_PROLOG)
                                                                  .withEpilog(EDITOR_EPILOG);
            // FIXME: mime type - XHTML?
            return new EmbeddedServer.Document().withMimeType("text/html")
                                                .withContent(editableDocument.asString())
                                                .withUpdateListener(
                text1 -> callback.notify(properties.withProperty(propertyName,
                                                                 originalDocument.withBody(text1).asString())));
          }
        catch (IOException e)
          {
            throw new RuntimeException(e);
          }
      }
  }
