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
import it.tidalwave.role.spi.DefaultContextManagerProvider;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.impl.model.DefaultResourceProperties;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.spi.ModelFactorySupport;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import it.tidalwave.northernwind.util.test.TestHelper;
import it.tidalwave.northernwind.util.test.TestHelper.TestResource;
import static it.tidalwave.util.test.FileComparisonUtils.assertSameContents;
import static it.tidalwave.northernwind.rca.ui.contenteditor.impl.ResourcePropertiesBinder.*;
import static it.tidalwave.northernwind.rca.ui.contenteditor.impl.ResourcePropertiesMatcher.*;
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
    private final TestHelper helper = new TestHelper(this);

    private static final Key<String> PROPERTY_1 = new Key<>("property1");
    private static final Key<String> PROPERTY_2 = new Key<>("property2");

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
        underTest = new ResourcePropertiesBinder(properties);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_initialize()
      throws IOException
      {
        // when just initialized
        // then
        final File prolog = new File("target/test-results/" + EDITOR_PROLOG);
        final File epilog = new File("target/test-results/" + EDITOR_EPILOG);
        final File expectedProlog = new File("src/main/resources/" + EDITOR_PROLOG);
        final File expectedEpilog = new File("src/main/resources/" + EDITOR_EPILOG);
        writeToFile(prolog, underTest.editorProlog);
        writeToFile(epilog, underTest.editorEpilog);
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
        // given
        final BoundProperty<String> boundProperty = new BoundProperty<>();
        // when
        underTest.bind(PROPERTY_2, boundProperty, callback);
        // then
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
        // given
        final BoundProperty<String> boundProperty = new BoundProperty<>();
        underTest.bind(PROPERTY_2, boundProperty, callback);
        // when
        boundProperty.set("New title");
        // then
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
        // given
        final Document document = underTest.createBoundDocument(PROPERTY_1, callback);
        // then
        assertThat(document.getMimeType(), is("text/html"));

        final TestResource tr = helper.testResourceFor("DocumentProxy.txt");
        tr.writeToActualFile(document.getContent().replaceAll("\\n$", ""));
        tr.assertActualFileContentSameAsExpected();
        verifyZeroInteractions(callback);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize")
    public void must_be_notified_with_updated_ResourceProperties_when_bound_document_updated()
      throws IOException
      {
        // given
        final Document document = underTest.createBoundDocument(PROPERTY_1, callback);
        // when
        document.update("the updated body\n");
        // then
        verify(callback).notify(argThat(resourcePropertiesWith(map().put(PROPERTY_1, "<!DOCTYPE html>\n"
                                                                                   + "<html>\n"
                                                                                   + "  <head>\n"
                                                                                   + "  </head>\n"
                                                                                   + "  <body>\n"
                                                                                   + "     the updated body\n"
                                                                                   + "  </body>\n"
                                                                                   + "</html>\n")
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