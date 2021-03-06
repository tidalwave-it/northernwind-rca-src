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
package it.tidalwave.northernwind.rca.ui.structureeditor.spi;

import javax.annotation.Nonnull;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEvent;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentationControl;
import it.tidalwave.util.annotation.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.Presentable._Presentable_;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @RequiredArgsConstructor @Slf4j
public class DefaultStructureEditorPresentationControl implements StructureEditorPresentationControl
  {
    @Nonnull
    private final StructureEditorPresentation presentation;

    @VisibleForTesting void onSiteNodeSelected (@ListensTo @Nonnull final SiteNodeSelectedEvent selectionEvent)
      {
        log.debug("onSiteNodeSelected({})", selectionEvent);

        if (selectionEvent.getSiteNode().isEmpty())
          {
            presentation.clear();
          }
        else
          {
            final SiteNode siteNode = selectionEvent.getSiteNode().get();
            final ResourceProperties properties = siteNode.getProperties();
            log.debug(">>>> properties: {}", properties);
            presentation.populate("Viewer not implemented for " + siteNode.getFile());
            presentation.populateProperties(properties.as(_Presentable_).createPresentationModel());
            // presentation.populateProperties(PresentationModel.ofMaybePresentable(properties));
            presentation.showUp();
          }
      }
  }
