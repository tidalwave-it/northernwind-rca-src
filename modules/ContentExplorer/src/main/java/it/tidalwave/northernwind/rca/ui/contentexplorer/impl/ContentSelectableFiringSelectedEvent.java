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
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * A {@link Selectable} for {@link Content} in the context of {@link DefaultContentExplorerPresentationControl}
 * that fires a {@link ContentSelectedEvent} message when selected.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@DciRole(datumType = Content.class, context = DefaultContentExplorerPresentationControl.class)
@RequiredArgsConstructor
public class ContentSelectableFiringSelectedEvent implements Selectable
  {
    @Nonnull
    private final MessageBus messageBus;

    @Nonnull
    private final Content content;

    @Override
    public void select()
      {
        messageBus.publish(ContentSelectedEvent.of(content));
      }
  }
