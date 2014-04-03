/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
 * %%
 * Copyright (C) 2013 - 2014 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.model.impl.admin.role;

import javax.annotation.Nonnull;
import java.util.WeakHashMap;
import com.google.common.annotations.VisibleForTesting;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.PresentationModelFactory;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.event.ContentCreatedEvent;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * An implementation of {@link PresentationModelFactory} for {@link ContentPresentationModel} that keeps track of
 * created instances and dispatches {@link ContentCreatedEvent}s to them. This to avoid that every single
 * {@code ContentPresentationModel} subscribes to the message bus and have all of them but one to discard the received
 * event if it's not related to their datum.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class ContentPresentationModelFactory implements PresentationModelFactory
  {
    @VisibleForTesting final WeakHashMap<Content, ContentPresentationModel> map = new WeakHashMap<>();

    @Override @Nonnull
    public PresentationModel createPresentationModel (final @Nonnull Object datum,
                                                      final @Nonnull Object... rolesOrFactories)
      {
        final ContentPresentationModel contentPM = new ContentPresentationModel((Content)datum, rolesOrFactories)
          {
            @Override
            public void dispose()
              {
                super.dispose();
                map.remove((Content)datum);
                log.debug(">>>> unregistered: {}", this);
              }
          };

        map.put((Content)datum, contentPM);
        log.debug(">>>> created and registered: {}", contentPM);
        return contentPM;
      }

    @VisibleForTesting void onContentCreated (final @ListensTo @Nonnull ContentCreatedEvent event)
      {
        log.debug("onContentCreated({})", event);
        final ContentPresentationModel contentPm =  map.get(event.getParentContent());

        if (contentPm != null)
          {
            contentPm.onContentCreated();
          }
      }
  }
