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
package it.tidalwave.northernwind.rca.ui.contenteditor.spi;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.UserAction;
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
import it.tidalwave.northernwind.rca.ui.contenteditor.impl.JSoupXhtmlNormalizer;
import it.tidalwave.northernwind.rca.ui.contenteditor.impl.ProcessExecutor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.ui.Presentable.*;
import static it.tidalwave.northernwind.model.admin.Properties.*;
import static it.tidalwave.northernwind.model.admin.role.Saveable.Saveable;
import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.PropertyBinder.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

/***********************************************************************************************************************
 *
 * A default implementation of the {@link ContentEditorPresentationControl}.
 *
 * @stereotype Control
 *
 * @author  Fabrizio Giudici
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
            .openExternalEditorAction(UserAction.of(this::openExternalEditor))
            .openExternalEditorBrowserAction(UserAction.of(this::openExternalEditorBrowser))
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
        assert properties != null;
        presentation.bind(bindings); // FIXME: needed because of unbindAll()
        final PropertyBinder propertyBinder = properties.as(PropertyBinder);
        propertyBinder.bind(PROPERTY_TITLE, bindings.title, propertyUpdateCallback);
        presentation.populateProperties(properties.as(Presentable).createPresentationModel());
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
    private void openExternalEditorBrowser()
      {
        log.info("openExternalEditorBrowser");
        // FIXME
        final String url = "http://localhost:12345/";

        // Safari breaks Aloha editor
        ProcessExecutor.forExecutable("Google Chrome.app")
                       .withArguments2(url)
                       .withPostMortemTask(() -> properties.ifPresent(p -> bindPropertiesAndLoadDocument(p)))
                       .execute();
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void openExternalEditor()
      {
        log.info("openExternalEditor");
        final String filePath = content.get().getFile().toFile().getAbsolutePath() + "/fullText_en.xhtml";

        ProcessExecutor.forExecutable("BlueGriffon.app")
                        // FIXME
                        // .withArguments2(content.get().getFile().getPath().asString())
                       .withArguments2(filePath)
                       .withPostMortemTask(() -> properties.ifPresent(p -> fix(p, filePath)))
                       .execute();
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void fix (final @Nonnull ResourceProperties properties, final @Nonnull String filePath)
      {
        try
          {
            final Path path = Paths.get(filePath);
            final String s = Files.lines(path, UTF_8).collect(joining("\n"))
                    .replace("%27", "'")
                    .replace("%28", "(")
                    .replace("%29", ")")
                    .replace("%7B", "{")
                    .replace("%7D", "}")
                    .replace("%20", " ");
            final JSoupXhtmlNormalizer normalizer = new JSoupXhtmlNormalizer();
            Files.write(path, asList(normalizer.asNormalizedString(s)), UTF_8);
            bindPropertiesAndLoadDocument(properties);
          }
        catch (IOException e)
          {
            log.error("", e);
          }
      }
  }
