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
package it.tidalwave.northernwind.rca.ui.structureeditor.spi;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import com.google.common.annotations.VisibleForTesting;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEvent;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentationControl;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.Presentable.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class DefaultStructureEditorPresentationControl implements StructureEditorPresentationControl
  {
    @Inject
    private StructureEditorPresentation presentation;

    @Override
    public void initialize()
      {
      }

    @VisibleForTesting void onSiteNodeSelected (final @ListensTo @Nonnull SiteNodeSelectedEvent selectionEvent)
      {
        log.debug("onSiteNodeSelected({})", selectionEvent);

        if (!selectionEvent.getSiteNode().isPresent())
          {
            presentation.clear();
          }
        else
          {
            final SiteNode siteNode = selectionEvent.getSiteNode().get();
            final ResourceProperties properties = siteNode.getProperties();
            log.debug(">>>> properties: {}", properties);
            presentation.populate("Viewer not implemented for " + siteNode.getFile());
            presentation.populateProperties(properties.as(Presentable).createPresentationModel());
            presentation.showUp();
          }
      }
  }
