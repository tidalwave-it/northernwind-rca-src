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
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.core.impl.model.DefaultResourceProperties;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.model.spi.ModelFactorySupport;
import it.tidalwave.northernwind.model.admin.role.Saveable;
import it.tidalwave.northernwind.rca.ui.event.ContentCreatedEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import lombok.Delegate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultContentChildCreatorTest
  {
    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    static class MockSaveable implements Saveable
      {
        @Getter @Nonnull
        private final ResourceProperties properties;

        @Override
        public void saveIn (final @Nonnull ResourceFile folder)
          {
          }
      }

    /*******************************************************************************************************************
     *
     * Injects a MockSaveable into ResourceProperties.
     * FIXME: try to get rid of this, and have MockSaveable normally injected.
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    static class ResourcePropertiesDecorator implements ResourceProperties
      {
        interface Exclusions
          {
            public <T> T as(Class<T> roleType);
          }

        @Delegate(excludes = Exclusions.class) @Nonnull
        private final ResourceProperties delegate;

        @Getter
        private final MockSaveable saveable = spy(new MockSaveable(this));

        @Override
        public <T> T as (final @Nonnull Class<T> roleType)
          {
            return roleType.equals(Saveable.class) ? roleType.cast(saveable) : delegate.as(roleType);
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    static class MockModelFactory extends ModelFactorySupport
      {
        @Getter
        private MockSaveable saveable;

        @Override @Nonnull
        public Content build (final @Nonnull Content.Builder builder)
          {
            final Content content = mock(Content.class);
            when(content.getFile()).thenReturn(builder.getFolder());
            return content;
          }

        @Override @Nonnull
        public ResourceProperties build (final @Nonnull ResourceProperties.Builder builder)
          {
            final ResourcePropertiesDecorator properties = new ResourcePropertiesDecorator(new DefaultResourceProperties(builder));
            saveable = properties.getSaveable();
            return properties;
          }
      }

    private final static Key<String> PROPERTY_1 = new Key<>("Property1");

    private final static Key<String> PROPERTY_2 = new Key<>("Property2");

    private ApplicationContext context;

    private DefaultContentChildCreator underTest;

    private Content parentContent;

    private MessageBus messageBus;

    private MockModelFactory modelFactory;

    private ResourceFile childFolder;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      throws IOException
      {
        context = new ClassPathXmlApplicationContext("DefaultContentChildCreatorTestBeans.xml");
        messageBus = context.getBean(MessageBus.class);
        modelFactory = context.getBean(MockModelFactory.class);
        parentContent = mock(Content.class);

        final ResourceFile parentFolder = mock(ResourceFile.class);
        childFolder = mock(ResourceFile.class);
        when(parentContent.getFile()).thenReturn(parentFolder);
        when(parentFolder.createFolder(anyString())).thenReturn(childFolder);

        underTest = new DefaultContentChildCreator(parentContent);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_create_the_new_Content_and_store_it()
      throws IOException, NotFoundException
      {
        // when
        final Map<Key<?>, Object> values = new HashMap<>();
        values.put(PROPERTY_1, "value 1");
        values.put(PROPERTY_2, "value 2");

        final Content content = underTest.createContent("foldername", values);
        // then
        final MockSaveable saveable = modelFactory.getSaveable();
        final ResourceProperties properties = saveable.getProperties();
        assertThat(properties.getKeys().size(), is(2));
        // FIXME: needs ResourcePropertiesMatcher to be moved to NW
        assertThat(properties.getProperty(PROPERTY_1), is("value 1"));
        assertThat(properties.getProperty(PROPERTY_2), is("value 2"));
        // END FIXME
//        assertThat(argThat(resourcePropertiesWith(values)));

        verify(saveable).saveIn(same(childFolder));
        verify(messageBus).publish(eq(ContentCreatedEvent.of(parentContent, content)));
      }
  }