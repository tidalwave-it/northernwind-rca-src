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
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import it.tidalwave.util.Key;
import it.tidalwave.role.IdFactory;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.code.model.TimeProvider;
import it.tidalwave.northernwind.rca.ui.event.CreateContentRequest;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation.Bindings;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentationControl;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import static java.util.stream.Collectors.joining;
import static it.tidalwave.util.ui.UserNotificationWithFeedback.*;
import static it.tidalwave.northernwind.model.admin.Properties.*;
import static it.tidalwave.northernwind.rca.ui.contentmanager.impl.ContentChildCreator.ContentChildCreator;

/***********************************************************************************************************************
 *
 * The default implementation of {@link AddContentPresentationControl}.
 *
 * @stereotype control
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@SimpleMessageSubscriber @RequiredArgsConstructor @Slf4j
public class DefaultAddContentPresentationControl implements AddContentPresentationControl
  {
    @Nonnull
    private final AddContentPresentation presentation;

    @Nonnull
    private final IdFactory idFactory;

    @Nonnull
    private final TimeProvider timeProvider;

    private final Bindings bindings = new ValidatingBindings();

    /* visible for testing */ String xhtmlSkeleton = "tbd";

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @PostConstruct
    /* visible for testing */ void initialize()
      throws IOException
      {
        xhtmlSkeleton = loadResource("Skeleton.xhtml");
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    /* visible for testing */ void onCreateContentRequest (final @ListensTo @Nonnull CreateContentRequest event)
      {
        log.info("onCreateContentRequest({})", event);
        bindings.publishingDateTime.set(timeProvider.getNow());
        presentation.bind(bindings);
        presentation.showUp(notificationWithFeedback()
                .withCaption("Create new content")
                .withFeedback(feedback().withOnConfirm(() -> createContent(event.getParentContent()))
                                        .withOnCancel(() -> {}))); // FIXME: withOnCancel(DO_NOTHING)
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private void createContent (final @Nonnull Content parentContent)
      throws IOException
      {
        final String folderName = urlEncoded(bindings.folder.get());
        // TODO: use TypeSafeMap, with a safe put() method
        final Map<Key<?>, Object> propertyValues = new HashMap<>();
        putIfNonEmpty(propertyValues, PROPERTY_TITLE,           bindings.title.get());
        putIfNonEmpty(propertyValues, PROPERTY_EXPOSED_URI,     bindings.exposedUri.get());
        putIfNonEmpty(propertyValues, PROPERTY_CREATION_TIME,   timeProvider.getNow());
        putIfNonEmpty(propertyValues, PROPERTY_PUBLISHING_TIME, bindings.publishingDateTime.get());
        putIfNonEmpty(propertyValues, PROPERTY_FULL_TEXT,       xhtmlSkeleton);
        putIfNonEmpty(propertyValues, PROPERTY_ID,              idFactory.createId());

        final Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
        final List<String> tags = Lists.newArrayList(splitter.split(bindings.tags.get()));

        if (!tags.isEmpty())
          {
            // See NWRCA-69
            propertyValues.put(PROPERTY_TAGS, tags.stream().collect(joining(",")));
//                propertyValues.put(PROPERTY_TAGS, tags);
          }

        parentContent.as(ContentChildCreator).createContent(folderName, propertyValues);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private static <T> void putIfNonEmpty (final @Nonnull Map<Key<?>, Object> values,
                                           final @Nonnull Key<T> key,
                                           final @CheckForNull T value)
      {
        if ((value != null) && !"".equals(value))
          {
            values.put(key, value);
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private static String urlEncoded (final @Nonnull String string)
      {
        try
          {
            return URLEncoder.encode(string, "UTF-8");
          }
        catch (UnsupportedEncodingException e)
          {
            throw new RuntimeException(e); // never happens
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    /* visible for testing */ String loadResource (final @Nonnull String path)
      throws IOException
      {
        final String prefix = getClass().getPackage().getName().replace(".", "/");
        final ClassPathResource resource = new ClassPathResource(prefix + "/" + path);
        final @Cleanup Reader r = new InputStreamReader(resource.getInputStream(), "UTF-8");
        return CharStreams.toString(r);
      }
  }
