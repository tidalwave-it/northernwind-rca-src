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
package it.tidalwave.northernwind.rca.ui.contentexplorer.impl;

import javax.annotation.Nonnull;
import it.tidalwave.util.annotation.VisibleForTesting;
import it.tidalwave.dci.annotation.DciContext;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.contentexplorer.ContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.role.ui.PresentationModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent.emptySelectionEvent;

/***********************************************************************************************************************
 *
 * The default implementation for {@link ContentExplorerPresentationControl}.
 *
 * @stereotype Control
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@DciContext(autoThreadBinding = true) @RequiredArgsConstructor @SimpleMessageSubscriber @Slf4j
public class DefaultContentExplorerPresentationControl implements ContentExplorerPresentationControl
  {
    /* package */ static final String ROOT_DOCUMENT_PATH = "/content/document";

    @Nonnull
    protected final MessageBus messageBus;

    @Nonnull
    private final ModelFactory modelFactory;

    @Nonnull
    private final ContentExplorerPresentation presentation;

    @VisibleForTesting void onOpenSite (@ListensTo @Nonnull final OpenSiteEvent event)
      {
        log.debug("onOpenSite({})", event);
        final ResourceFile root = event.getFileSystem().findFileByPath(ROOT_DOCUMENT_PATH);
        final Content rootContent = modelFactory.createContent().withFolder(root).build();
        presentation.populate(PresentationModel.ofMaybePresentable(rootContent));
        presentation.expandFirstLevel();
        messageBus.publish(emptySelectionEvent());
      }
  }
