/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.ui.structureexplorer.impl;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.dci.annotation.DciContext;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.Site;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentationControl;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.Presentable.*;
import static it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEvent.emptySelectionEvent;

/***********************************************************************************************************************
 *
 * The default implementation for {@link StructureExplorerPresentationControl}.
 *
 * @stereotype Control
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciContext(autoThreadBinding = true) @SimpleMessageSubscriber @Slf4j
public class DefaultStructureExplorerPresentationControl implements StructureExplorerPresentationControl
  {
    /* package */ static final String ROOT_SITE_NODE_PATH = "/structure";

    @Inject
    private ModelFactory modelFactory;

    @Inject
    private Site site;

    @Inject
    private MessageBus messageBus;

    @Inject
    private StructureExplorerPresentation presentation;

    /* visible for testing */ void onOpenSite (final @ListensTo @Nonnull OpenSiteEvent event)
      {
        try
          {
            log.debug("onOpenSite({})", event);
            final ResourceFile root = event.getFileSystem().findFileByPath(ROOT_SITE_NODE_PATH);
            final SiteNode rootSiteNode = modelFactory.createSiteNode(site, root);
            presentation.populate(rootSiteNode.as(Presentable).createPresentationModel());
            presentation.expandFirstLevel();
            messageBus.publish(emptySelectionEvent());
          }
        catch (IOException | NotFoundException e)
          {
            log.warn("", e);
          }
      }
  }
