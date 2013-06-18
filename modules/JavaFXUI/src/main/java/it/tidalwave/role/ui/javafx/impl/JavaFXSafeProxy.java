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
package it.tidalwave.role.ui.javafx.impl;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class JavaFXSafeProxy<T> implements InvocationHandler
  {
    @Nonnull @Getter @Setter
    private T delegate;

    @Override
    public Object invoke (final @Nonnull Object proxy, final @Nonnull Method method, final @Nonnull Object[] args)
      throws Throwable
      {
        final AtomicReference<Object> result = new AtomicReference<>();
        final AtomicReference<Throwable> throwable = new AtomicReference<>();

        JavaFXSafeRunner.runSafely(new Runnable()
          {
            @Override
            public void run()
              {
                try
                  {
                    log.trace(">>>> safely invoking {}", method);
                    result.set(method.invoke(delegate, args));
                  }
                catch (Throwable t)
                  {
                    throwable.set(t);
                  }
              }
          });

        if (throwable.get() != null)
          {
            throw throwable.get();
          }

        return result.get();
      }
  }
