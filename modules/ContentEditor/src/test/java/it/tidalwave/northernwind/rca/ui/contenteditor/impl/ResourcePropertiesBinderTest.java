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
import it.tidalwave.util.NotFoundException;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.impl.model.DefaultResourceProperties;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.spi.ModelFactorySupport;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import static it.tidalwave.northernwind.rca.ui.contenteditor.impl.ResourcePropertiesBinder.*;
import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.DefaultContentEditorPresentationControl.*;
import static it.tidalwave.util.test.FileComparisonUtils.assertSameContents;
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
        modelFactory = new ModelFactorySupport()
          {
            @Override @Nonnull
            public ResourceProperties build (final @Nonnull ResourceProperties.Builder builder)
              {
                return new DefaultResourceProperties(builder);
              }
          };

        final String html = "<html>\n<head>\n</head>\n<body>\nthe body\n</body>\n</html>";

        properties = modelFactory.createProperties().build().withProperty(PROPERTY_FULL_TEXT, html);
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
        assertSameContents(expectedProlog , prolog);
        assertSameContents(expectedEpilog, epilog);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "must_properly_initialize")
    public void must_create_a_proper_proxy_document()
      throws IOException
      {
        final Document document = fixture.createBoundDocument(PROPERTY_FULL_TEXT, callback);

        assertThat(document.getMimeType(), is("text/html"));

        final File file = new File("target/test-results/DocumentProxy.txt");
        final File expectedFile = new File("src/test/resources/ExpectedDocumentProxy.txt");
        writeToFile(file, document.getContent());
        assertSameContents(expectedFile, file);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_create_a_proxy_document_that_properly_updates_properties()
      throws IOException
      {
        final Document document = fixture.createBoundDocument(PROPERTY_FULL_TEXT, callback);
        document.update("the updated body\n");

        final String expectedHtml = "<!doctype html>\n<html>\n<head>\n</head>\n<body>\nthe updated body\n</body>\n</html>";
        verify(callback).notify(argThat(new BaseMatcher<ResourceProperties>()
          {
            @Override
            public boolean matches (final Object item)
              {
                if (!(item instanceof ResourceProperties))
                  {
                    return false;
                  }

                final ResourceProperties p = (ResourceProperties)item;
                try
                  {
                    return expectedHtml.equals(p.getProperty(PROPERTY_FULL_TEXT));
                  }
                catch (NotFoundException | IOException e)
                  {
                    return false;
                  }
              }

            @Override
            public void describeTo (final Description description)
              {
                description.appendText("ResourceProperties with PROPERTY_FULL_TEXT=" + expectedHtml);
              }
          }));
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
  }