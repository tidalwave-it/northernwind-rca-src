/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
 * %%
 * Copyright (C) 2013 - 2014 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.embeddedserver.impl;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultEmbeddedServerTest
  {
    private ClassPathXmlApplicationContext context;

    private DefaultEmbeddedServer fixture;

    private DefaultEmbeddedServer nonSpringFixture;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        context = new ClassPathXmlApplicationContext("DefaultEmbeddedServerTestBeans.xml");
        fixture = context.getBean(DefaultEmbeddedServer.class);
        nonSpringFixture = new DefaultEmbeddedServer();
        nonSpringFixture.setPort(12346); // don't clash with the other fixture
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @AfterMethod
    public void shutDown()
      throws Exception
      {
        context.destroy();

        if (nonSpringFixture.server != null)
          {
            log.info(">>>> shutting down server...");
            nonSpringFixture.server.stop();
          }
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void start_must_properly_boot_the_webserver()
      throws InterruptedException
      {
        nonSpringFixture.start();

        assertThat(nonSpringFixture.server, is(not(nullValue())));
        assertThat(nonSpringFixture.server.isStarted(), is(true));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "start_must_properly_boot_the_webserver")
    public void stop_must_properly_shut_the_webserver_down()
      throws InterruptedException
      {
        nonSpringFixture.start();
        Thread.sleep(2000);
        nonSpringFixture.stop();

        assertThat(nonSpringFixture.server, is(not(nullValue())));
        assertThat(nonSpringFixture.server.isStopped(), is(true));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "start_must_properly_boot_the_webserver")
    public void must_properly_serve_loaded_documents()
      throws MalformedURLException, IOException
      {
        final String url1 = fixture.putDocument("/",     new Document().withMimeType("text/html").withContent("document 1"));
        final String url2 = fixture.putDocument("/doc2", new Document().withMimeType("text/plain").withContent("document 2"));
        final String url3 = fixture.putDocument("/doc3", new Document().withMimeType("text/css").withContent("document 3"));

        assertUrlContents(url1, "text/html; charset=UTF-8",  "document 1");
        assertUrlContents(url2, "text/plain; charset=UTF-8", "document 2");
        assertUrlContents(url3, "text/css; charset=UTF-8",   "document 3");
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test(dependsOnMethods = "start_must_properly_boot_the_webserver")
    public void must_return_not_found_for_non_existing_documents()
      throws MalformedURLException, IOException
      {
        fixture.putDocument("/",     new Document().withMimeType("text/html").withContent("document 1"));
        fixture.putDocument("/doc2", new Document().withMimeType("text/plain").withContent("document 2"));
        fixture.putDocument("/doc3", new Document().withMimeType("text/css").withContent("document 3"));

        assertUrlContents("http://localhost:12345/foo", HttpServletResponse.SC_NOT_FOUND);
        assertUrlContents("http://localhost:12345/bar", HttpServletResponse.SC_NOT_FOUND);
      }

    // TODO: test serving of contents from the library

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private static void assertUrlContents (final @Nonnull String url,
                                           final @Nonnull String expectedMimeType,
                                           final @Nonnull String expectedContent)
      throws MalformedURLException, IOException
      {
        final Client client = Client.create();
        final WebResource webResource = client.resource(url);
        final ClientResponse response = webResource.accept(expectedMimeType).get(ClientResponse.class);

        final String content = response.getEntity(String.class);

        assertThat(response.getStatus(), is(HttpServletResponse.SC_OK));
        assertThat(content, is(expectedContent));
        assertThat(response.getType().toString(), is(expectedMimeType));
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    private void assertUrlContents (final @Nonnull String url,
                                    final int expectedStatus)
      {
        final Client client = Client.create();
        final WebResource webResource = client.resource(url);
        final ClientResponse response = webResource.get(ClientResponse.class);

        assertThat(response.getStatus(), is(expectedStatus));
      }
  }