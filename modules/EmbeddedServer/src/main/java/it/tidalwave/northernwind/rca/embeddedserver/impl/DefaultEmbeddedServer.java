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
package it.tidalwave.northernwind.rca.embeddedserver.impl;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.core.io.ClassPathResource;
import org.eclipse.jetty.server.Server;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceFileSystem;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.northernwind.rca.ui.impl.SpringMessageBusListenerSupport;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultEmbeddedServer extends SpringMessageBusListenerSupport implements EmbeddedServer
  {
    @Inject @Nonnull
    private ServletContext mimeResolver; // FIXME: replace with MimeResolver

    @Getter @Setter
    private int port = 12345;

    @CheckForNull
    @VisibleForTesting Server server;

    private final Map<String, Document> documentMapByUrl = new HashMap<>();

    @CheckForNull
    private ResourceFileSystem fileSystem;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private final ServletAdapter servlet = new ServletAdapter()
      {
        @Override
        protected void doGet (final @Nonnull HttpServletRequest request,
                              final @Nonnull HttpServletResponse response)
          throws ServletException, IOException
          {
            log.info("doGet({}, {})", request, response);

            final String uri = request.getRequestURI();
            log.debug(">>>> URI: {}", uri);
            // FIXME: use a pipeline for handling those requests - eventually integrate support already in Site

            if (uri.startsWith("/nwa/")) // FIXME - and use ResourcePath
              {
                try
                  {
                    final byte[] resource = loadResource(uri);
                    response.setCharacterEncoding("");
                    response.setContentType(mimeResolver.getMimeType(uri));
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getOutputStream().write(resource);
                  }
                catch (FileNotFoundException e)
                  {
                    log.warn("2 - Not found: {}", uri);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                  }
              }

            else if (uri.startsWith("/library/")) // FIXME - and use ResourcePath
              {
                final ResourceFile file = fileSystem.findFileByPath("/content" + uri); // FIXME

                response.setContentType(file.getMimeType());
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(file.asText("UTF-8"));
              }

            else
              {
                final Document document = documentMapByUrl.get(uri);

                if (document == null)
                  {
                    log.warn("1 - Not found: {}", uri);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                  }
                else
                  {
                    response.setContentType(document.getMimeType());
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(document.getContent());
                  }
              }
          }
      };

    /*******************************************************************************************************************
     *
     *
     *
     *
     ******************************************************************************************************************/
    @VisibleForTesting void onOpenSite (final @ListensTo @Nonnull OpenSiteEvent event)
      {
        log.debug("onOpenSite({})", event);
        fileSystem = event.getFileSystem();
      }

    /*******************************************************************************************************************
     *
     *
     *
     *
     ******************************************************************************************************************/
    @PostConstruct
    public void start()
      {
        try
          {
            log.info("Starting webserver...");
            server = new Server(port);
            server.setHandler(servlet.asHandler());
            server.start();
            log.info(">>>> started");
          }
        catch (Exception e)
          {
            log.error("", e);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     *
     ******************************************************************************************************************/
    @PreDestroy
    public void stop()
      {
        try
          {
            log.info("Starting webserver...");
            server.stop();
            log.info(">>>> stopped");
          }
        catch (Exception e)
          {
            log.error("", e);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public String putDocument (final @Nonnull String path, final @Nonnull Document document)
      {
        documentMapByUrl.put(path, document);
        return String.format("http://localhost:%d%s", port, path);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    @VisibleForTesting byte[] loadResource (final @Nonnull String path)
      throws IOException
      {
        final ClassPathResource resource = new ClassPathResource(path);
        final @Cleanup DataInputStream is = new DataInputStream(resource.getInputStream());
        final byte[] buffer = new byte[(int)resource.contentLength()];
        is.readFully(buffer);
        return buffer;
      }
  }
