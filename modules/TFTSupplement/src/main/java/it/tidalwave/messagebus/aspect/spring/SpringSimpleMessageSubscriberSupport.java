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

import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.messagebus.MessageBusHelper;
import it.tidalwave.messagebus.MessageBus;
import org.springframework.beans.factory.BeanFactory;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable(preConstruction = true)
public class SpringSimpleMessageSubscriberSupport
  {
    @Inject @Nonnull
    private BeanFactory beanFactory;

    private final MessageBusHelper busHelper;

    public SpringSimpleMessageSubscriberSupport (final @Nonnull Object bean)
      {
    // TODO: have SimpleMessageSubscriber carry a name of the MessageBus and pass it to the MessageBusHelper
        final MessageBus messageBus = beanFactory.getBean("applicationMessageBus", MessageBus.class);
        busHelper = new MessageBusHelper(bean, new MessageBusAdapterFactory(messageBus));
      }

    public void subscribeAll()
      {
        busHelper.subscribeAll();
      }

    public void unsubscribeAll()
      {
        busHelper.unsubscribeAll();
      }
  }
