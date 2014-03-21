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
package it.tidalwave.northernwind.model.impl.admin.role;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import it.tidalwave.util.spi.AsDelegateProvider;
import it.tidalwave.util.spi.EmptyAsDelegateProvider;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.event.ContentCreatedEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ContentPresentationModelFactoryTest
  {
    private ApplicationContext context;

    private ContentPresentationModelFactory fixture;

    private Content content;

    private Content childContent;

    private Object role;

    @BeforeMethod
    public void setupFixture()
      {
        AsDelegateProvider.Locator.set(new EmptyAsDelegateProvider());
        context = new ClassPathXmlApplicationContext("ContentPresentationModelFactoryTestBeans.xml");
        fixture = new ContentPresentationModelFactory();
        content = mock(Content.class);
        childContent = mock(Content.class);
        role = new Object();
      }

    @Test
    public void must_create_a_ContentPresentationModel_and_register_it()
      {
        // when
        final PresentationModel pm = fixture.createPresentationModel(content, role);
        // then
        assertThat(pm, is(notNullValue()));
        assertThat(pm, is(instanceOf(ContentPresentationModel.class)));
        assertThat(fixture.map.containsKey(content), is(true));
      }

    @Test(dependsOnMethods = "must_create_a_ContentPresentationModel_and_register_it")
    public void must_unregister_a_disposed_PresentationModel()
      {
        // given
        final PresentationModel pm = fixture.createPresentationModel(content, role);
        // when
        pm.dispose();
        // then
        assertThat(fixture.map.containsKey(content), is(false));
      }

    @Test(dependsOnMethods = "must_create_a_ContentPresentationModel_and_register_it")
    public void must_properly_dispatch_messages()
      {
        // given
        final ContentPresentationModel pm = mock(ContentPresentationModel.class);
        fixture.map.put(content, pm);
        // when
        fixture.onContentCreated(new ContentCreatedEvent(content, childContent));
        // then
        verify(pm).onContentCreated();
        verifyNoMoreInteractions(pm);
      }
  }