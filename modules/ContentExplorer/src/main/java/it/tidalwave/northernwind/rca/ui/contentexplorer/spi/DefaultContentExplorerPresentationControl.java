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
package it.tidalwave.northernwind.rca.ui.contentexplorer.spi;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import com.google.common.annotations.VisibleForTesting;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.role.ui.PresentationModelProvider;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.model.impl.admin.AdminContent;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.northernwind.rca.ui.impl.SpringMessageBusListenerSupport;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * The default implementation for {@link ContentExplorerPresentationControl}.
 *
 * @stereotype Control
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class DefaultContentExplorerPresentationControl extends SpringMessageBusListenerSupport
                                                       implements ContentExplorerPresentationControl
  {
    @Inject @Named("applicationMessageBus") @Nonnull
    protected MessageBus messageBus;

    @Inject @Nonnull
    @VisibleForTesting ModelFactory modelFactory;

    private ContentExplorerPresentation presentation;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    // FIXME: use Content
    @VisibleForTesting final RoleFactory<AdminContent> publisherRoleFactory = new RoleFactory<AdminContent>()
      {
        @Override
        public Object createRoleFor (final @Nonnull AdminContent content)
          {
            return new Selectable()
              {
                @Override public void select()
                  {
                    log.debug("Selected {}", content);
                    messageBus.publish(new ContentSelectedEvent(content));
                  }
              };
          }
      };

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void initialize (final @Nonnull ContentExplorerPresentation presentation)
      {
        this.presentation = presentation;
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @VisibleForTesting void onOpenSite (final @ListensTo @Nonnull OpenSiteEvent event)
      {
        log.debug("onOpenSite({})", event);
        final ResourceFile root = event.getFileSystem().findFileByPath("/content/document");
        final AdminContent content = (AdminContent)modelFactory.createContent(root);
        presentation.populate(content.as(PresentationModelProvider.class).createPresentationModel(publisherRoleFactory));
        messageBus.publish(new ContentSelectedEvent());
      }
  }
