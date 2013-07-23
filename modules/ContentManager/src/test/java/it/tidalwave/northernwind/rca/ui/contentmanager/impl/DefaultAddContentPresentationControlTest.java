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
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import it.tidalwave.util.Key;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation.Bindings;
import it.tidalwave.northernwind.rca.ui.contentmanager.CreateContentRequest;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static com.google.common.collect.ImmutableMap.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static it.tidalwave.util.ui.UserNotificationWithFeedbackTestHelper.*;
import static it.tidalwave.northernwind.model.admin.Properties.*;
import java.util.Arrays;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultAddContentPresentationControlTest
  {
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    static abstract class InputSetter
      {
        public abstract void setInput (@Nonnull Bindings bindings);

        @Nonnull
        public Answer<Void> setInput()
          {
            return new Answer<Void>()
              {
                @Override
                public Void answer (InvocationOnMock invocation)
                  throws Throwable
                  {
                    setInput((Bindings)invocation.getArguments()[0]);
                    return null;
                  }
              };
          }

        @Override @Nonnull
        public String toString()
          {
            final Bindings bindings = new Bindings();
            setInput(bindings);
            return bindings.toString().replaceAll("^AddContentPresentation\\.Bindings\\(", "")
                                      .replaceAll("\\)$", "")
                                      .replaceAll("BoundProperty\\(value=([^)]*)\\)", "$1");
          }
      }

    private final static DateTimeFormatter ISO_FORMATTER = ISODateTimeFormat.dateTime();

    private ApplicationContext context;

    private DefaultAddContentPresentationControl fixture;

    private AddContentPresentation presentation;

    private Content content;

    private DateTime dateTime;

    private ContentChildCreator contentChildCreator;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeClass
    public void freezeTime()
      {
//        dateTime = new DateTime();
        dateTime = new DateTime(0);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        DateTimeUtils.setCurrentMillisFixed(dateTime.getMillis());
        context = new ClassPathXmlApplicationContext("DefaultAddContentPresentationControlTestBeans.xml");
        fixture = context.getBean(DefaultAddContentPresentationControl.class);
        presentation = context.getBean(AddContentPresentation.class);
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
        fixture.onAddContentEvent(new CreateContentRequest(content));

        final Bindings expectedBindings = new Bindings();
        expectedBindings.publishingDateTime.set(ISO_FORMATTER.print(dateTime));
        verify(presentation).bind(eq(expectedBindings));
        verify(presentation).showUp(argThat(notificationWithFeedback("Create new content", "")));
        verifyNoMoreInteractions(presentation);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dataProvider = "inputProvider")
    public void must_create_Content_with_the_right_Properties_when_user_confirms
            (final @Nonnull InputSetter inputSetter,
             final @Nonnull String expectedFolderName,
             final @Nonnull Map<Key<?>, Object> values)
      throws IOException
      {
        doAnswer(inputSetter.setInput()).when(presentation).bind(any(Bindings.class));
        doAnswer(CONFIRM).when(presentation).showUp(any(UserNotificationWithFeedback.class));

        fixture.onAddContentEvent(new CreateContentRequest(content));

        final Map<Key<?>, Object> expectedPropertyValues = new HashMap<>(values);
        expectedPropertyValues.put(PROPERTY_CREATION_TIME, ISO_FORMATTER.print(dateTime));
        expectedPropertyValues.put(PROPERTY_FULL_TEXT, fixture.xhtmlSkeleton);

        verify(contentChildCreator).createContent(eq(expectedFolderName), eq(expectedPropertyValues));
        verifyNoMoreInteractions(contentChildCreator);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_do_nothing_when_user_cancels() throws IOException
      {
        doAnswer(CANCEL).when(presentation).showUp(any(UserNotificationWithFeedback.class));

        fixture.onAddContentEvent(new CreateContentRequest(content));

        verifyZeroInteractions(contentChildCreator);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @DataProvider(name = "inputProvider")
    public Object[][] inputProvider()
      {
        return new Object[][]
          {
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
              {
                new InputSetter()
                  {
                    @Override
                    public void setInput (Bindings bindings)
                      {
                        bindings.folder.set("the folder");
                        bindings.title.set("the title");
                        bindings.exposedUri.set("the-exposed-uri");
                      }
                  },

                "the+folder",

                of(PROPERTY_PUBLISHING_TIME, ISO_FORMATTER.print(dateTime),
                   PROPERTY_TITLE,           "the title",
                   PROPERTY_EXPOSED_URI,     "the-exposed-uri")
              },
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
              {
                new InputSetter()
                  {
                    @Override
                    public void setInput (Bindings bindings)
                      {
                        bindings.folder.set("the folder");
                        bindings.title.set("the title");
                        bindings.exposedUri.set("the-exposed-uri");
                        bindings.tags.set("tag1, tag2");
                      }
                  },

                "the+folder",

                of(PROPERTY_PUBLISHING_TIME, ISO_FORMATTER.print(dateTime),
                   PROPERTY_TITLE,           "the title",
                   PROPERTY_EXPOSED_URI,     "the-exposed-uri",
                   PROPERTY_TAGS,            Arrays.asList("tag1", "tag2"))
              }
          };
      }
  }