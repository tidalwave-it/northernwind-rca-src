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
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEvent;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * This role is injected only in the context of DefaultStructureExplorerPresentationControl and publishes a selection
 * message whenever its {@link SiteNode} owner is selected on the UI.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datumType = SiteNode.class, context = DefaultStructureExplorerPresentationControl.class)
@Configurable @RequiredArgsConstructor
public class StructureSelectableFiringSelectedEvent implements Selectable
  {
    @Inject
    private MessageBus messageBus;

    @Nonnull
    private final SiteNode siteNode;

    @Override
    public void select()
      {
        messageBus.publish(SiteNodeSelectedEvent.of(siteNode));
      }
  }
