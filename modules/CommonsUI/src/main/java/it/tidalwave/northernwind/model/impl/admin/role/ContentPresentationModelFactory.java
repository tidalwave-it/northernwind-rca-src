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
package it.tidalwave.northernwind.model.impl.admin.role;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.WeakHashMap;
import it.tidalwave.util.Callback;
import it.tidalwave.util.NamedCallback;
import it.tidalwave.util.annotation.VisibleForTesting;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.PresentationModelFactory;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.event.ContentCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.PresentationModel.PROPERTY_CHILDREN;
import static it.tidalwave.util.Parameters.r;

/***********************************************************************************************************************
 *
 * An implementation of {@link PresentationModelFactory} for {@link Content} that keeps track of
 * created instances and dispatches {@link ContentCreatedEvent}s to them. This to avoid that every single
 * {@code ContentPresentationModel} subscribes to the message bus and have all of them but one to discard the received
 * event if it's not related to their datum.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class ContentPresentationModelFactory implements PresentationModelFactory
  {
    @VisibleForTesting final WeakHashMap<Content, PresentationModel> map = new WeakHashMap<>();

    @Override @Nonnull
    public PresentationModel createPresentationModel (@Nonnull final Object datum,
                                                      @Nonnull final Collection<Object> roles)
      {
        final Callback cb = NamedCallback.of(PresentationModel.CALLBACK_DISPOSE, () ->
          {
            map.remove(datum);
            log.debug(">>>> unregistered: {}", this);
          });

        final PresentationModel contentPM = PresentationModel.of(datum, r(cb, roles));
        map.put((Content)datum, contentPM);
        log.debug(">>>> created and registered: {}", contentPM);
        return contentPM;
      }

    @VisibleForTesting void onContentCreated (@ListensTo @Nonnull final ContentCreatedEvent event)
      {
        log.debug("onContentCreated({})", event);
        // FIXME: map.getOptional(event.getParentContent()).ifPresent(cpm -> ...);
        final PresentationModel contentPM =  map.get(event.getParentContent());

        if (contentPM != null)
          {
            log.debug(">>>> dispatching {} to {}", event, contentPM);
            // FIXME: this is an undocumented feature
            contentPM.as(PropertyChangeSupport.class).firePropertyChange(PROPERTY_CHILDREN, null, null);
          }
      }
  }
