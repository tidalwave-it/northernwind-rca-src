/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
import javax.inject.Inject;
import java.util.Optional;
import java.io.IOException;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.spi.UserActionSupport8;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation.Bindings;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentationControl;
import it.tidalwave.northernwind.rca.ui.contenteditor.impl.ProcessExecutor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.Presentable.*;
import static it.tidalwave.northernwind.model.admin.Properties.*;
import static it.tidalwave.northernwind.model.admin.role.Saveable.Saveable;
import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.PropertyBinder.*;

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
public class DefaultContentEditorPresentationControl implements ContentEditorPresentationControl
  {
    @Inject
    private EmbeddedServer documentServer;

    @Inject
    private ContentEditorPresentation presentation;

    @Nonnull
    private Optional<Content> content = Optional.empty();

    @Nonnull
    private Optional<ResourceProperties> properties = Optional.empty();

    /* visible for testing */ final Bindings bindings = Bindings.builder()
            .openExternalEditorAction(UserActionSupport8.withCallback(this::openExternalEditor))
            .title(new BoundProperty<>(""))
            .build();

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    /* visible for testing */ final PropertyBinder.UpdateCallback propertyUpdateCallback = (updatedProperties) ->
      {
        updatedProperties.as(Saveable).saveIn(content.get().getFile());
        unbindProperties();
        properties = content.map(Content::getProperties); // reload them
        // FIXME: properties have to be re-bound, since they have been reloaded - but this makes the HTML editor
        // to flicker and the caret in text editor to reset at position 0 - and to loop forever
        properties.ifPresent(this::bindProperties);
      };

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void initialize()
      {
        presentation.bind(bindings);
      }

    // FIXME: unbind at termination

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    /* visible for testing */ void onContentSelected (final @ListensTo @Nonnull ContentSelectedEvent selectionEvent)
      {
        log.debug("onContentSelected({})", selectionEvent);

        unbindProperties();
        content = selectionEvent.getContent();
        properties = content.map(Content::getProperties); // reload them

        if (!content.isPresent())
          {
            presentation.clear();
          }
        else
          {
            presentation.showUp();
          }

        properties.ifPresent(this::bindPropertiesAndLoadDocument);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void bindPropertiesAndLoadDocument (final @Nonnull ResourceProperties properties)
      {
        bindProperties(properties);
        final PropertyBinder propertyBinder = properties.as(PropertyBinder);
        final Document document = propertyBinder.createBoundDocument(PROPERTY_FULL_TEXT, propertyUpdateCallback);
        presentation.populateDocument(documentServer.putDocument("/", document));
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void bindProperties (final @Nonnull ResourceProperties properties)
      {
        try
          {
            assert properties != null;
            presentation.bind(bindings); // FIXME: needed because of unbindAll()
            final PropertyBinder propertyBinder = properties.as(PropertyBinder);
            propertyBinder.bind(PROPERTY_TITLE, bindings.title, propertyUpdateCallback);
            presentation.populateProperties(properties.as(Presentable).createPresentationModel());
          }
        catch (IOException | NotFoundException e)
          {
            presentation.clear(); // FIXME: should notify error
            log.warn("", e);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void unbindProperties()
      {
        bindings.title.unbindAll();
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void openExternalEditor()
      {
        log.info("openExternalEditor");
        // FIXME
        final String url = "http://localhost:12345/";

//            ProcessExecutor.forExecutable("Google Chrome.app")
        ProcessExecutor.forExecutable("Firefox.app")
                       .withArguments2(url)
                       .withPostMortemTask(() -> properties.ifPresent(p -> bindPropertiesAndLoadDocument(p)))
                       .execute();
      }
  }
