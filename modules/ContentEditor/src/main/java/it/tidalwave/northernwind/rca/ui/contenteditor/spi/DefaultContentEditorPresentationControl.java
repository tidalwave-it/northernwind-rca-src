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
package it.tidalwave.northernwind.rca.ui.contenteditor.spi;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import com.google.common.annotations.VisibleForTesting;
import it.tidalwave.util.Key;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentationControl;
import it.tidalwave.northernwind.rca.ui.impl.SpringMessageBusListenerSupport;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.PresentationModelProvider.*;

/***********************************************************************************************************************
 *
 * A default implementation of the {@link ContentEditorPresentationControl}.
 *
 * @stereotype Control
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @Slf4j
public class DefaultContentEditorPresentationControl extends SpringMessageBusListenerSupport
                                                     implements ContentEditorPresentationControl
  {
    @Inject @Nonnull
    @VisibleForTesting EmbeddedServer documentServer;

    @Inject @Nonnull
    @VisibleForTesting DocumentProxyFactory documentProxyFactory;

    @Inject @Nonnull
    private ContentEditorPresentation presentation;

    @VisibleForTesting final ContentEditorPresentation.Bindings bindings = new ContentEditorPresentation.Bindings();

    public static final Key<String> PROPERTY_FULL_TEXT = new Key<>("fullText"); // FIXME copied
    public static final Key<String> PROPERTY_TITLE = new Key<>("title"); // FIXME copied

    private final PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
      {
        @Override
        public void propertyChange (final @Nonnull PropertyChangeEvent event)
          {
            log.info("TODO: changed title {}", event.getNewValue());
          }
      };

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
//    @PostConstruct
    @Override // cannot be postconstruct, because the presentation is not ready initially
    public void initialize()
      {
        bindings.title.addPropertyChangeListener(propertyChangeListener);
        presentation.bind(bindings);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @VisibleForTesting void onContentSelected (final @ListensTo @Nonnull ContentSelectedEvent selectionEvent)
      {
        log.debug("onContentSelected({})", selectionEvent);

        if (selectionEvent.isEmptySelection())
          {
            presentation.clear();
          }
        else
          {
            try
              {
                final Content content = selectionEvent.getContent();
                final ResourceProperties properties = content.getProperties();
                final Document document = documentProxyFactory.createDocumentProxy(content, PROPERTY_FULL_TEXT);

                bindings.title.set(properties.getProperty(PROPERTY_TITLE, ""));
                presentation.populateDocument(documentServer.putDocument("/", document));
                presentation.populateProperties(properties.as(PresentationModelProvider).createPresentationModel());
                presentation.showUp();
              }
            catch (IOException e)
              {
                presentation.clear(); // FIXME: should notify error
                log.warn("", e);
              }
          }
      }
  }
