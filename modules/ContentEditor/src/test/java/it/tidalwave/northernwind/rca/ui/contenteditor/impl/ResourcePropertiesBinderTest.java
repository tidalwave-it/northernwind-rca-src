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
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import java.io.IOException;
import it.tidalwave.util.Key;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.spi.DefaultContextManagerProvider;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.impl.model.DefaultResourceProperties;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.spi.ModelFactorySupport;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import it.tidalwave.util.TypeSafeMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import it.tidalwave.northernwind.util.test.SpringTestHelper;
import it.tidalwave.northernwind.util.test.SpringTestHelper.TestResource;
import static it.tidalwave.northernwind.rca.ui.contenteditor.impl.ResourcePropertiesBinder.*;
import static it.tidalwave.northernwind.rca.ui.contenteditor.impl.ResourcePropertiesMatcher.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class ResourcePropertiesBinderTest
  {
    private final SpringTestHelper helper = new SpringTestHelper(this);

    private static final Key<String> PROPERTY_1 = Key.of("property1", String.class);
    private static final Key<String> PROPERTY_2 = Key.of("property2", String.class);

    private static final String ORIGINAL_PROPERTY_1_VALUE = "<html>\n"
                                                          + "<head>\n"
                                                          + "</head>\n"
                                                          + "<body>\n"
                                                          + "the body\n"
                                                          + "</body>\n"
                                                          + "</html>";
    private static final String ORIGINAL_PROPERTY_2_VALUE = "title";

    private ResourcePropertiesBinder underTest;

    private ResourceProperties properties;

    private UpdateCallback callback;

    private ModelFactory modelFactory;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setup()
      {
        ContextManager.Locator.set(new DefaultContextManagerProvider());
        modelFactory = new ModelFactorySupport()
          {
            @Override @Nonnull
            public ResourceProperties build (@Nonnull final ResourceProperties.Builder builder)
              {
                return new DefaultResourceProperties(builder);
              }
          };

        properties = modelFactory.createProperties().build().withProperty(PROPERTY_1, ORIGINAL_PROPERTY_1_VALUE)
                                                            .withProperty(PROPERTY_2, ORIGINAL_PROPERTY_2_VALUE);
        callback = mock(UpdateCallback.class);
        underTest = new ResourcePropertiesBinder(properties);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_initialize_resources()
      throws IOException
      {
        // when just initialized
        // then
        final TestResource trProlog = helper.testResourceFor("EditorProlog.txt");
        final TestResource trEpilog = helper.testResourceFor("EditorEpilog.txt");
        trProlog.writeToActualFile(ResourcePropertiesBinder.EDITOR_PROLOG.replaceAll("\\n$", ""));
        trEpilog.writeToActualFile(ResourcePropertiesBinder.EDITOR_EPILOG.replaceAll("\\n$", ""));
        trProlog.assertActualFileContentSameAsExpected();
        trEpilog.assertActualFileContentSameAsExpected();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize_resources")
    public void must_properly_set_value_to_bound_property()
      {
        // given
        final BoundProperty<String> boundProperty = new BoundProperty<>();
        // when
        underTest.bind(PROPERTY_2, boundProperty, callback);
        // then
        assertThat(boundProperty.get(), is(ORIGINAL_PROPERTY_2_VALUE));
        verifyNoInteractions(callback);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize_resources")
    public void must_be_notified_with_updated_ResourceProperties_when_bound_property_updated()
      {
        // given
        final BoundProperty<String> boundProperty = new BoundProperty<>();
        underTest.bind(PROPERTY_2, boundProperty, callback);
        // when
        boundProperty.set("New title");
        // then
        final TypeSafeMap map = TypeSafeMap.newInstance()
                .with(PROPERTY_1, ORIGINAL_PROPERTY_1_VALUE)
                .with(PROPERTY_2, "New title");
        verify(callback).notify(argThat(resourcePropertiesWith(map)));
        verifyNoMoreInteractions(callback);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize_resources")
    public void must_properly_set_value_to_bound_document()
      throws IOException
      {
        // given
        final Document document = underTest.createBoundDocument(PROPERTY_1, callback);
        // then
        assertThat(document.getMimeType(), is("text/html"));

        final TestResource tr = helper.testResourceFor("DocumentProxy.txt");
        tr.writeToActualFile(document.getContent().replaceAll("\\n$", ""));
        tr.assertActualFileContentSameAsExpected();
        verifyNoInteractions(callback);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize_resources")
    public void must_be_notified_with_updated_ResourceProperties_when_bound_document_updated()
      {
        // given
        final Document document = underTest.createBoundDocument(PROPERTY_1, callback);
        // when
        document.update("the updated body\n");
        // then
        final TypeSafeMap map = TypeSafeMap.newInstance()
                                           .with(PROPERTY_1, "<!DOCTYPE html>\n"
                                                            + "<html>\n"
                                                            + "  <head>\n"
                                                            + "  </head>\n"
                                                            + "  <body>\n"
                                                            + "     the updated body\n"
                                                            + "  </body>\n"
                                                            + "</html>\n")
                                           .with(PROPERTY_2, ORIGINAL_PROPERTY_2_VALUE);
        verify(callback).notify(argThat(resourcePropertiesWith(map)));
        verifyNoMoreInteractions(callback);
      }
  }