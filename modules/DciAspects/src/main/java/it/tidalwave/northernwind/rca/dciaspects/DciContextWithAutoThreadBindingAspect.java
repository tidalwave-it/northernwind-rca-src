/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone https://bitbucket.org/tidalwave/northernwind-src.git
 * %%
 * Copyright (C) 2011 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.dciaspects;

import it.tidalwave.dci.annotation.DciContext;
import javax.annotation.Nonnull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.spi.LogUtil;
import it.tidalwave.util.Task;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * An aspect which implements {@link DciContext} with {@code autoThreadBinding=true}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Aspect @Slf4j
public class DciContextWithAutoThreadBindingAspect
  {
//    @Inject
//    private Provider<ContextManager> contextManager;

    @Around("within(@it.tidalwave.dci.annotation.DciContext *) && execution(* *(..))")
    public Object advice (final @Nonnull ProceedingJoinPoint pjp)
      throws Throwable
      {
        final Object context = pjp.getTarget();

        if (!context.getClass().getAnnotation(DciContext.class).autoThreadBinding())
          {
            return pjp.proceed();
          }
        else
          {
            log.trace("executing {}.{}() with auto context thread binding",
                      LogUtil.shortId(context), pjp.getSignature().getName());
            // It looks like the @Inject approach creates bogus multiple instance of ContextManager
//            final ContextManager contextManager = contextManager.get();
            final ContextManager contextManager = ContextManager.Locator.find();
            return contextManager.runWithContext(context, new Task<Object, Throwable>()
              {
                @Override
                public Object run()
                  throws Throwable
                  {
                    return pjp.proceed();
                  }
              });
          }
      }
  }