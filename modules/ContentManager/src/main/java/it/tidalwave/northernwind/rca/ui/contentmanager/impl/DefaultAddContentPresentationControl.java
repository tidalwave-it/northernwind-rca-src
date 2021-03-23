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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import it.tidalwave.util.TypeSafeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import it.tidalwave.util.Key;
import it.tidalwave.util.TimeProvider;
import it.tidalwave.role.IdFactory;
import it.tidalwave.messagebus.annotation.ListensTo;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.event.CreateContentRequest;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation.Bindings;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentationControl;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.util.ui.UserNotificationWithFeedback.*;
import static it.tidalwave.northernwind.model.admin.Properties.*;
import static it.tidalwave.northernwind.rca.ui.contentmanager.impl.ContentChildCreator._ContentChildCreator_;

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
    /* visible for testing */ void onCreateContentRequest (@ListensTo @Nonnull final CreateContentRequest event)
      {
        log.info("onCreateContentRequest({})", event);
        bindings.publishingDateTime.set(timeProvider.currentZonedDateTime());
        presentation.bind(bindings);
        presentation.showUp(notificationWithFeedback()
                .withCaption("Create new content")
                .withFeedback(feedback().withOnConfirm(() -> createContent(event.getParentContent()))
                                        .withOnCancel(() -> {}))); // FIXME: withOnCancel(DO_NOTHING)
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private void createContent (@Nonnull final Content parentContent)
      throws IOException
      {
        final String folderName = urlEncoded(bindings.folder.get());
        // TODO: use TypeSafeMap, with a safe put() method
        TypeSafeMap map = TypeSafeMap.newInstance();
        map = putIfNonEmpty(map, PROPERTY_TITLE,           bindings.title.get());
        map = putIfNonEmpty(map, PROPERTY_EXPOSED_URI,     bindings.exposedUri.get());
        map = putIfNonEmpty(map, PROPERTY_CREATION_TIME,   timeProvider.currentZonedDateTime());
        map = putIfNonEmpty(map, PROPERTY_PUBLISHING_TIME, bindings.publishingDateTime.get());
        map = putIfNonEmpty(map, PROPERTY_FULL_TEXT,       xhtmlSkeleton);
        map = putIfNonEmpty(map, PROPERTY_ID,              idFactory.createId());

        final Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
        final List<String> tags = Lists.newArrayList(splitter.split(bindings.tags.get()));

        if (!tags.isEmpty())
          {
            // See NWRCA-69
            map = map.with(PROPERTY_TAGS, String.join(",", tags));
//                map.put(PROPERTY_TAGS, tags);
          }

        parentContent.as(_ContentChildCreator_).createContent(folderName, map);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private static <T> TypeSafeMap putIfNonEmpty (@Nonnull TypeSafeMap values,
                                                  @Nonnull final Key<T> key,
                                                  @CheckForNull final T value)
      {
        if ((value != null) && !"".equals(value))
          {
            values = values.with(key, value);
          }

        return values;
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private static String urlEncoded (@Nonnull final String string)
      {
        return URLEncoder.encode(string, StandardCharsets.UTF_8);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    /* visible for testing */ String loadResource (@Nonnull final String path)
      throws IOException
      {
        final String prefix = getClass().getPackage().getName().replace(".", "/");
        final ClassPathResource resource = new ClassPathResource(prefix + "/" + path);
        @Cleanup final Reader r = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return CharStreams.toString(r);
      }
  }
