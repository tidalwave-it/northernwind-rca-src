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
package it.tidalwave.messagebus.aspect.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import it.tidalwave.messagebus.MessageBus;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static it.tidalwave.messagebus.aspect.spring.ListenerAdapterMatcher.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class SpringSimpleMessageSubscriberAspectTest
  {
    private ApplicationContext context;

    private MockSubscriber subscriber;

    private MessageBus messageBus;

    @BeforeMethod
    public void setupFixture()
      {
        context = new ClassPathXmlApplicationContext("SpringSimpleMessageSubscriberAspectTestBeans.xml");
        subscriber = context.getBean(MockSubscriber.class);
        messageBus = context.getBean(MessageBus.class);
      }

    @Test
    public void must_subscribe()
      throws Exception
      {
        assertThat(subscriber, is(instanceOf(InitializingBean.class)));

        reset(messageBus);

        ((InitializingBean)subscriber).afterPropertiesSet();
        verify(messageBus).subscribe(eq(MockEvent.class), argThat(listenerAdapter().withMethodName("onMockEvent")
                                                                                    .withOwner(subscriber)
                                                                                    .withTopic(MockEvent.class)));
        verifyNoMoreInteractions(messageBus);
      }

    @Test
    public void must_unsubscribe()
      throws Exception
      {
        assertThat(subscriber, is(instanceOf(DisposableBean.class)));

        reset(messageBus);

        ((DisposableBean)subscriber).destroy();
        verify(messageBus).unsubscribe(argThat(listenerAdapter().withMethodName("onMockEvent")
                                                                .withOwner(subscriber)
                                                                .withTopic(MockEvent.class)));
        verifyNoMoreInteractions(messageBus);
      }
  }