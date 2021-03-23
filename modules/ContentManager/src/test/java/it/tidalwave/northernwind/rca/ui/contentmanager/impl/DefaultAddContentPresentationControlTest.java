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

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.Consumer;
import java.io.IOException;
import it.tidalwave.util.TypeSafeMap;
import it.tidalwave.util.test.MockTimeProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.util.Id;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.role.IdFactory;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation.Bindings;
import it.tidalwave.northernwind.rca.ui.event.CreateContentRequest;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import lombok.RequiredArgsConstructor;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static it.tidalwave.util.ui.UserNotificationWithFeedbackTestHelper.*;
import static it.tidalwave.northernwind.model.admin.Properties.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class DefaultAddContentPresentationControlTest
  {
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    static class InputEmulator
      {
        @Nonnull
        private final Consumer<Bindings> initializer;

        @Nonnull
        public Answer<Void> setInput()
          {
            return invocation ->
              {
                initializer.accept(((Bindings)invocation.getArguments()[0]));
                return null;
              };
          }

        @Override @Nonnull
        public String toString()
          {
            final Bindings bindings = new Bindings();
            initializer.accept(bindings);
            return bindings.toString().replaceAll("^AddContentPresentation\\.Bindings\\(", "")
                                      .replaceAll("\\)$", "")
                                      .replaceAll("BoundProperty\\(value=([^)]*)\\)", "$1");
          }
      }

    private ApplicationContext context;

    private DefaultAddContentPresentationControl underTest;

    private AddContentPresentation presentation;

    private Content content;

    private Instant nowInstant;

    private ZonedDateTime now;

    private ZonedDateTime tomorrow;

    private ContentChildCreator contentChildCreator;

    private IdFactory idFactory;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeClass
    public void freezeTime()
      {
        nowInstant = Instant.now();
        now = nowInstant.atZone(ZoneId.systemDefault());
        tomorrow = now.plus(1, DAYS);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        context = new ClassPathXmlApplicationContext("DefaultAddContentPresentationControlTestBeans.xml");
        underTest = context.getBean(DefaultAddContentPresentationControl.class);
        presentation = context.getBean(AddContentPresentation.class);
        idFactory = context.getBean(IdFactory.class);
        context.getBean(MockTimeProvider.class).setInstant(nowInstant);
        content = mock(Content.class);
        contentChildCreator = mock(ContentChildCreator.class);
        when(content.as(eq(ContentChildCreator.class))).thenReturn(contentChildCreator);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_set_initial_values_and_show_up()
      {
        // when
        underTest.onCreateContentRequest(CreateContentRequest.of(content));
        // then
        final Bindings expectedBindings = new Bindings();
        expectedBindings.publishingDateTime.set(now);
        verify(presentation).bind(eq(expectedBindings));
        verify(presentation).showUp(argThat(notificationWithFeedback("Create new content", "")));
        verifyNoMoreInteractions(presentation);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "inputProvider")
    public void must_create_Content_with_the_right_Properties_when_user_confirms
            (final @Nonnull InputEmulator inputEmulator,
             final @Nonnull String expectedFolderName,
             final @Nonnull TypeSafeMap baseExpectedProperties)
      throws IOException
      {
        // given
        doAnswer(inputEmulator.setInput()).when(presentation).bind(any(Bindings.class));
        doAnswer(confirm()).when(presentation).showUp(any(UserNotificationWithFeedback.class));
        final Id id = new Id(UUID.randomUUID());
        when(idFactory.createId()).thenReturn(id);
        // when
        underTest.onCreateContentRequest(CreateContentRequest.of(content));
        // then
        final TypeSafeMap expectedProperties = baseExpectedProperties.
            with(PROPERTY_CREATION_TIME, now).
            with(PROPERTY_FULL_TEXT, underTest.xhtmlSkeleton).
            with(PROPERTY_ID, id);

        verify(contentChildCreator).createContent(eq(expectedFolderName), eq(expectedProperties));
        verifyNoMoreInteractions(contentChildCreator);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_do_nothing_when_user_cancels()
      throws IOException
      {
        // given
        doAnswer(cancel()).when(presentation).showUp(any(UserNotificationWithFeedback.class));
        // when
        underTest.onCreateContentRequest(CreateContentRequest.of(content));
        // then
        verifyZeroInteractions(contentChildCreator);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @DataProvider
    public Object[][] inputProvider()
      {
        return new Object[][]
          {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
              {
                new InputEmulator(bindings ->
                  {
                    bindings.folder.set("the folder");
                    bindings.title.set("the title");
                    bindings.exposedUri.set("the-exposed-uri");
                  }),
                // expected folder name
                "the+folder",
                // expected properties
                TypeSafeMap.newInstance().
                    with(PROPERTY_PUBLISHING_TIME, now).
                    with(PROPERTY_TITLE, "the title").
                    with(PROPERTY_EXPOSED_URI, "the-exposed-uri")
              },
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
              {
                new InputEmulator(bindings ->
                  {
                    bindings.folder.set("another folder");
                    bindings.title.set("another title");
                    bindings.exposedUri.set("another-exposed-uri");
                    bindings.publishingDateTime.set(tomorrow);
                    bindings.tags.set("tag1, tag2");
                  }),
                // expected folder name
                "another+folder",
                // expected properties
                TypeSafeMap.newInstance().
                    with(PROPERTY_TITLE, "another title").
                    with(PROPERTY_EXPOSED_URI, "another-exposed-uri").
                    with(PROPERTY_PUBLISHING_TIME, tomorrow).
                    with(PROPERTY_TAGS, "tag1,tag2") // See NWRCA-69
//                   PROPERTY_TAGS,            Arrays.asList("tag1", "tag2"))
              }
          };
      }
  }