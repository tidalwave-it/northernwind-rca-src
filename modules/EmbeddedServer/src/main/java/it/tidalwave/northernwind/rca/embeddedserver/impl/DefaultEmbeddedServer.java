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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.common.io.ByteStreams;
import org.springframework.core.io.ClassPathResource;
import org.eclipse.jetty.server.Server;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.impl.filter.LibraryLinkMacroFilter;
import it.tidalwave.northernwind.core.impl.filter.MediaLinkMacroFilter;
import it.tidalwave.northernwind.core.model.MimeTypeResolver;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceFileSystem;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import static java.util.stream.Collectors.joining;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class DefaultEmbeddedServer implements EmbeddedServer
  {
    @Inject
    private MimeTypeResolver mimeTypeResolver;

    @Getter @Setter
    private int port = 12345;

    @CheckForNull
    /* visible for testing */ Server server;

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
        private static final long serialVersionUID = -2887261966375531858L;

        private final MediaLinkMacroFilter mediaLinkMacroFilter = new MediaLinkMacroFilter();
        private final LibraryLinkMacroFilter libraryLinkMacroFilter = new LibraryLinkMacroFilter();

        @Override
        protected void doGet (final @Nonnull HttpServletRequest request,
                              final @Nonnull HttpServletResponse response)
          throws ServletException, IOException
          {
            String uri = request.getRequestURI();
            log.debug("doGet({})", uri);
            // FIXME: use a pipeline for handling those requests - eventually integrate support already in Site

            uri = mediaLinkMacroFilter.filter(uri, "");
            uri = libraryLinkMacroFilter.filter(uri, "");
            uri = uri.replace("//", "/"); // Aloha puts a leading / before the macro
            log.debug(">>> filtered {}", uri);

            if (uri.startsWith("/nwa/")) // FIXME - and use ResourcePath
              {
                serveEditorResources(uri, response);
              }

            else if (uri.startsWith("/library/") || uri.startsWith("/media/")) // FIXME - and use ResourcePath
              {
                serveContentResources(uri, response);
              }

            else
              {
                serveRegisteredResources(uri, response);
              }
          }

        @Override
        protected void doPut (final @Nonnull HttpServletRequest request,
                              final @Nonnull HttpServletResponse response)
          throws ServletException, IOException
          {
            final String uri = request.getRequestURI();
            log.debug("doPut({})", uri);
            updateRegisteredResource(request, response);
          }
      };

    /*******************************************************************************************************************
     *
     *
     *
     *
     ******************************************************************************************************************/
    /* visible for testing */ void onOpenSite (final @ListensTo @Nonnull OpenSiteEvent event)
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
            log.info("Starting webserver on port {}...", port);
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
            if ((server != null) && !server.isStopping() && !server.isStopped())
              {
                log.info("Stopping webserver...");
                server.stop();
                log.info(">>>> stopped");
              }
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
            response.setContentType(mimeTypeResolver.getMimeType(uri));
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
    private void serveContentResources (final @Nonnull String uri,
                                        final @Nonnull HttpServletResponse response)
      throws IOException
      {
        log.debug("serveLibraryResources({})", uri);

        // don't bother when there's no opened Site
        if (fileSystem == null)
          {
            response.setStatus(HttpServletResponse.SC_OK);
          }
        else
          {
            final ResourceFile file = fileSystem.findFileByPath("/content" + uri); // FIXME

            if (file == null)
              {
                log.warn("5 - Not found: {}", "/content" + uri);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
              }

            final String mimeType = file.getMimeType();
            response.setContentType(mimeType);
            response.setStatus(HttpServletResponse.SC_OK);

            if (mimeType.startsWith("image"))
              {
                response.getOutputStream().write(file.asBytes());
              }
            else
              {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(file.asText("UTF-8"));
              }
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
      throws IOException
      {
        final String uri = request.getRequestURI();
        final String body = request.getReader().lines().collect(joining("\n"));

        final Document document = documentMapByUrl.get(uri);

        if (document == null)
          {
            log.warn("3 - Not found: {}", uri);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          }
        else
          {
            document.update(body);
            response.setStatus(HttpServletResponse.SC_OK);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    /* visible for testing */ byte[] loadResource (final @Nonnull String path)
      throws IOException
      {
        final ClassPathResource resource = new ClassPathResource(path);
        final @Cleanup DataInputStream is = new DataInputStream(resource.getInputStream());
        return ByteStreams.toByteArray(is);
      }
  }
