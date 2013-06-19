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
package it.tidalwave.northernwind.rca.ui.structureeditor.spi;

import javax.annotation.Nonnull;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEvent;
import it.tidalwave.northernwind.rca.ui.impl.SpringMessageBusListenerSupport;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentationControl;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.PresentationModelProvider.PresentationModelProvider;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class DefaultStructureEditorPresentationControl extends SpringMessageBusListenerSupport implements StructureEditorPresentationControl
  {
    @Nonnull
    private StructureEditorPresentation presentation;

    public void onSiteNodeSelected (final @ListensTo @Nonnull SiteNodeSelectedEvent event)
      {
        log.debug("onSiteNodeSelected({})", event);
        final SiteNode siteNode = event.getSiteNode();
        final ResourceProperties properties = siteNode.getProperties();
        log.debug(">>>> properties: {}", properties);
        presentation.populate("Viewer not implemented for " + siteNode.getFile());
        presentation.populateProperties(properties.as(PresentationModelProvider).createPresentationModel());
        presentation.showUp();
      }

    @Override
    public void initialize (final @Nonnull StructureEditorPresentation presentation)
      {
        this.presentation = presentation;
      }
  }
