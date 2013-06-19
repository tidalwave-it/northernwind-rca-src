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
package it.tidalwave.northernwind.rca.ui.structureexplorer.spi;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.role.ui.PresentationModelProvider;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.model.impl.admin.AdminModelFactory;
import it.tidalwave.northernwind.model.impl.admin.AdminSiteNode;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEvent;
import it.tidalwave.northernwind.rca.ui.impl.SpringMessageBusListenerSupport;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.structureexplorer.StructureExplorerPresentationControl;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * The default implementation for {@link StructureExplorerPresentationControl}.
 *
 * @stereotype Control
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class DefaultStructureExplorerPresentationControl extends SpringMessageBusListenerSupport implements StructureExplorerPresentationControl
  {
    @Inject @Nonnull
    private AdminModelFactory modelFactory;

    @Inject @Named("applicationMessageBus") @Nonnull
    private MessageBus messageBus;

    @Nonnull
    private StructureExplorerPresentation presentation;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    // FIXME: use SiteNode
    private final RoleFactory<AdminSiteNode> publisherRoleFactory = new RoleFactory<AdminSiteNode>()
      {
        @Override
        public Object createRoleFor (final @Nonnull AdminSiteNode siteNode)
          {
            return new Selectable()
              {
                @Override
                public void select()
                  {
                    log.debug("Selected {}", siteNode);
                    messageBus.publish(new SiteNodeSelectedEvent(siteNode));
                  }
              };
          }
      };

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    public void onOpenSite (final @ListensTo @Nonnull OpenSiteEvent event)
      {
        try
          {
            log.debug("onOpenSite({})", event);
            final ResourceFile root = event.getFileSystem().findFileByPath("/structure");
            final AdminSiteNode siteNode = (AdminSiteNode)modelFactory.createSiteNode(null, root);
            presentation.populate(siteNode.as(PresentationModelProvider.class).createPresentationModel(publisherRoleFactory));
          }
        catch (IOException | NotFoundException e)
          {
            log.warn("", e);
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void initialize (final @Nonnull StructureExplorerPresentation presentation)
      {
        this.presentation = presentation;
      }
  }
