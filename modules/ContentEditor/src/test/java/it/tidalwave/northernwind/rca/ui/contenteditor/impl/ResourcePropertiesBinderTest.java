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
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.common.collect.ImmutableMap;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.impl.model.DefaultResourceProperties;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.spi.ModelFactorySupport;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static it.tidalwave.northernwind.rca.ui.contenteditor.impl.ResourcePropertiesBinder.*;
import static it.tidalwave.util.test.FileComparisonUtils.assertSameContents;
import static it.tidalwave.northernwind.rca.ui.contenteditor.impl.ResourcePropertiesMatcher.*;
import it.tidalwave.role.spi.DefaultContextManagerProvider;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ResourcePropertiesBinderTest
  {
    private static final Key<String> PROPERTY_1 = new Key<>("property1");
    private static final Key<String> PROPERTY_2 = new Key<>("property2");
    private static final String ORIGINAL_PROPERTY_1_VALUE = "<html>\n<head>\n</head>\n<body>\nthe body\n</body>\n</html>";
    private static final String ORIGINAL_PROPERTY_2_VALUE = "title";

    private ResourcePropertiesBinder fixture;

    private ResourceProperties properties;

    private UpdateCallback callback;

    private ModelFactory modelFactory;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      throws IOException
      {
        ContextManager.Locator.set(new DefaultContextManagerProvider());
        modelFactory = new ModelFactorySupport()
          {
            @Override @Nonnull
            public ResourceProperties build (final @Nonnull ResourceProperties.Builder builder)
              {
                return new DefaultResourceProperties(builder);
              }
          };

        properties = modelFactory.createProperties().build().withProperty(PROPERTY_1, ORIGINAL_PROPERTY_1_VALUE)
                                                            .withProperty(PROPERTY_2, ORIGINAL_PROPERTY_2_VALUE);
        callback = mock(UpdateCallback.class);
        fixture = new ResourcePropertiesBinder(properties);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_initialize()
      throws IOException
      {
        final File prolog = new File("target/test-results/" + EDITOR_PROLOG);
        final File epilog = new File("target/test-results/" + EDITOR_EPILOG);
        writeToFile(prolog, fixture.editorProlog);
        writeToFile(epilog, fixture.editorEpilog);

        final File expectedProlog = new File("src/main/resources/" + EDITOR_PROLOG);
        final File expectedEpilog = new File("src/main/resources/" + EDITOR_EPILOG);
        assertSameContents(expectedProlog, prolog);
        assertSameContents(expectedEpilog, epilog);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize")
    public void must_properly_set_value_to_bound_property()
      throws NotFoundException, IOException
      {
        final BoundProperty<String> boundProperty = new BoundProperty<>();

        fixture.bind(PROPERTY_2, boundProperty, callback);

        assertThat(boundProperty.get(), is(ORIGINAL_PROPERTY_2_VALUE));
        verifyZeroInteractions(callback);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize")
    public void must_be_notified_with_updated_ResourceProperties_when_bound_property_updated()
      throws NotFoundException, IOException
      {
        final BoundProperty<String> boundProperty = new BoundProperty<>();
        fixture.bind(PROPERTY_2, boundProperty, callback);

        boundProperty.set("New title");

        verify(callback).notify(argThat(resourcePropertiesWith(map().put(PROPERTY_1, ORIGINAL_PROPERTY_1_VALUE)
                                                                    .put(PROPERTY_2, "New title"))));
        verifyNoMoreInteractions(callback);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize")
    public void must_properly_set_value_to_bound_document()
      throws IOException
      {
        final Document document = fixture.createBoundDocument(PROPERTY_1, callback);

        assertThat(document.getMimeType(), is("text/html"));

        final File file = new File("target/test-results/DocumentProxy.txt");
        final File expectedFile = new File("src/test/resources/ExpectedDocumentProxy.txt");
        writeToFile(file, document.getContent());
        assertSameContents(expectedFile, file);
        verifyZeroInteractions(callback);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize")
    public void must_be_notified_with_updated_ResourceProperties_when_bound_document_updated()
      throws IOException
      {
        final Document document = fixture.createBoundDocument(PROPERTY_1, callback);

        document.update("the updated body\n");

        final String expectedHtml = "<!doctype html>\n<html>\n<head>\n</head>\n<body>\nthe updated body\n</body>\n</html>";
        verify(callback).notify(argThat(resourcePropertiesWith(map().put(PROPERTY_1, expectedHtml)
                                                                    .put(PROPERTY_2, ORIGINAL_PROPERTY_2_VALUE))));
        verifyNoMoreInteractions(callback);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private void writeToFile (final @Nonnull File file, final @Nonnull String editorHeader)
      throws FileNotFoundException
      {
        file.getParentFile().mkdirs();

        try (final PrintWriter pw = new PrintWriter(file))
          {
            pw.print(editorHeader);
            pw.flush();
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Nonnull
    private static ImmutableMap.Builder<Key<String>, String> map()
      {
        return new ImmutableMap.Builder<>();
      }
  }