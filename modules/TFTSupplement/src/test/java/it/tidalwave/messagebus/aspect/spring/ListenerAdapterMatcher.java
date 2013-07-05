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
import it.tidalwave.messagebus.aspect.spring.MessageBusAdapterFactory.MessageBusListenerAdapter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(staticName = "listenerAdapter") @AllArgsConstructor @ToString
public class ListenerAdapterMatcher extends BaseMatcher<MessageBusListenerAdapter>
  {
    @Wither
    public String methodName;

    @Wither
    public Object owner;

    @Wither
    public Class<?> topic;

    @Override
    public boolean matches (final Object item)
      {
        if (! (item instanceof MessageBusListenerAdapter))
          {
            return false;
          }

        final MessageBusListenerAdapter listener = (MessageBusListenerAdapter)item;

        return (methodName.equals(listener.getMethod().getName())
                && (owner == listener.getOwner())
                && topic.equals(listener.getTopic()));
      }

    @Override
    public void describeTo (final @Nonnull Description description)
      {
        description.appendText(toString());
      }
  }
