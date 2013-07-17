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
import it.tidalwave.dci.annotation.DciContext;
import it.tidalwave.util.Task;
import it.tidalwave.role.ContextManager;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.Presentable.*;

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
@DciContext @SimpleMessageSubscriber @Slf4j
public class DefaultContentExplorerPresentationControl implements ContentExplorerPresentationControl
  {
    @Inject @Named("applicationMessageBus") @Nonnull
    protected MessageBus messageBus;

    @Inject @Nonnull
    private ModelFactory modelFactory;

    @Inject @Nonnull
    private ContentExplorerPresentation presentation;

    @Inject @Nonnull
    private ContextManager contextManager;

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void initialize()
      {
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @VisibleForTesting void onOpenSite (final @ListensTo @Nonnull OpenSiteEvent event)
      {
        // FIXME: use @DciContext(autoThreadBinding = true)
        contextManager.runWithContext(this, new Task<Void, RuntimeException>()
          {
            @Override
            public Void run()
              {
                log.debug("onOpenSite({})", event);
                final ResourceFile root = event.getFileSystem().findFileByPath("/content/document");
                final Content content = modelFactory.createContent().withFolder(root).build();
                presentation.populate(content.as(Presentable).createPresentationModel());
                presentation.expandFirstLevel();
                messageBus.publish(new ContentSelectedEvent());
                return null;
              }
          });
      }
  }
