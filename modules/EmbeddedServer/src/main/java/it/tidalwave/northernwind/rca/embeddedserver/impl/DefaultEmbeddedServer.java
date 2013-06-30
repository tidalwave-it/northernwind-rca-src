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
import com.google.common.io.ByteStreams;
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
            final String uri = request.getRequestURI();
            log.debug("doGet({})", uri);
            // FIXME: use a pipeline for handling those requests - eventually integrate support already in Site

            if (uri.startsWith("/nwa/")) // FIXME - and use ResourcePath
              {
                serveEditorResources(uri, response);
              }

            else if (uri.startsWith("/library/")) // FIXME - and use ResourcePath
              {
                serveLibraryResources(uri, response);
              }

            else
              {
                serveRegisteredResources(uri, response);
              }
          }

        @Override // TODO: better a PUT but I don't know how to do in jQuery
        protected void doPost (final @Nonnull HttpServletRequest request,
                               final @Nonnull HttpServletResponse response)
          throws ServletException, IOException
          {
            final String uri = request.getRequestURI();
            log.debug("doPost({})", uri);
            updateRegisteredResource(request, response);
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
    private void serveEditorResources (final @Nonnull String uri,
                                       final @Nonnull HttpServletResponse response)
      throws IOException
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

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void serveLibraryResources (final @Nonnull String uri,
                                        final @Nonnull HttpServletResponse response)
      throws IOException
      {
        if (fileSystem == null)
          {
            response.setStatus(HttpServletResponse.SC_OK);
          }
        else
          {
            final ResourceFile file = fileSystem.findFileByPath("/content" + uri); // FIXME

            response.setCharacterEncoding("UTF-8");
            response.setContentType(file.getMimeType());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(file.asText("UTF-8"));
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void serveRegisteredResources (final @Nonnull String uri,
                                           final @Nonnull HttpServletResponse response)
      throws IOException
      {
        final Document document = documentMapByUrl.get(uri);

        if (document == null)
          {
            log.warn("1 - Not found: {}", uri);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          }
        else
          {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(document.getMimeType());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(document.getContent());
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void updateRegisteredResource (final @Nonnull HttpServletRequest request,
                                           final @Nonnull HttpServletResponse response)
      {
        final String uri = request.getRequestURI();
        final String s = request.getParameter("content");

        final Document document = documentMapByUrl.get(uri);

        if (document == null)
          {
            log.warn("3 - Not found: {}", uri);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          }
        else
          {
            document.update(s);
            response.setStatus(HttpServletResponse.SC_OK);
          }
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
        return ByteStreams.toByteArray(is);
      }
  }
