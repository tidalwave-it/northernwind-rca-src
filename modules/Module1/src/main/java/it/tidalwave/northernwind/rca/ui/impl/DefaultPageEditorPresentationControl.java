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
package it.tidalwave.northernwind.rca.ui.impl;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.MessageBus.Listener;
import it.tidalwave.northernwind.rca.ui.PageEditorPresentation;
import it.tidalwave.northernwind.rca.ui.PageEditorPresentationControl;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class DefaultPageEditorPresentationControl implements PageEditorPresentationControl
  {
    @Inject @Named("applicationMessageBus") @Nonnull
    private MessageBus messageBus;

    @Nonnull
    private PageEditorPresentation presentation;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private final Listener<ContentSelectedEvent> siteNodeSelectionListener =
            new Listener<ContentSelectedEvent>()
      {
        @Override
        public void notify (final @Nonnull ContentSelectedEvent event)
          {
              log.info("notified {}", event);
            presentation.open(event.getFile());
          }
      };

    @PostConstruct
    private void initialize()
      {
        messageBus.subscribe(ContentSelectedEvent.class, siteNodeSelectionListener);
      }

    @PreDestroy
    private void destroy()
      {
        messageBus.unsubscribe(siteNodeSelectionListener);
      }

    @Override
    public void initialize (final @Nonnull PageEditorPresentation presentation)
      {
        this.presentation = presentation;
      }
  }
