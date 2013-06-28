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
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.common.annotations.VisibleForTesting;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceFileSystem;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.northernwind.rca.ui.impl.SpringMessageBusListenerSupport;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
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
     *
     ******************************************************************************************************************/
    private final AbstractHandler handler = new AbstractHandler()
      {
        @Override
        public void handle (final @Nonnull String target,
                            final @Nonnull Request baseRequest,
                            final @Nonnull HttpServletRequest request,
                            final @Nonnull HttpServletResponse response)
          throws IOException, ServletException
          {
            log.info("handle({}, {}, {}, {})", target, baseRequest, request, response);

            if (target.startsWith("/library/")) // FIXME - and use ResourcePath
              {
                final ResourceFile file = fileSystem.findFileByPath("/content" + target); // FIXME

                response.setContentType(file.getMimeType());
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(file.asText("UTF-8"));
              }

            else
              {
                final Document document = documentMapByUrl.get(target);

                if (document == null)
                  {
                    log.info("1 - Not found: {}", target);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                  }
                else
                  {
                    response.setContentType(document.getMimeType());
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(document.getContent());
                  }
              }

            baseRequest.setHandled(true);
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
            server.setHandler(handler);
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
    @Override
    public void putDocument (final @Nonnull String url, final @Nonnull Document document)
      {
        documentMapByUrl.put(url, document);
      }
  }
