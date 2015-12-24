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

import it.tidalwave.role.ContextManager;
import it.tidalwave.dci.annotation.DciContext;
import it.tidalwave.util.Task;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import static org.mockito.Mockito.*;
import org.testng.annotations.DataProvider;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DciContextWithAutoThreadBindingAspectTest
  {
    static class WithoutAnnotation
      {
        public void publicMethod()
          {
            log.info("publicMethod()");
          }

        protected void protectedMethod()
          {
            log.info("protectedMethod()");
          }

       /* package */ void packageMethod()
          {
            log.info("packageMethod()");
          }

        private void privateMethod()
          {
            log.info("privateMethod()");
          }
      }

    @DciContext
    static class WithAnnotationButNoAutoThreadBinding
      {
        public void publicMethod()
          {
            log.info("publicMethod()");
          }

        protected void protectedMethod()
          {
            log.info("protectedMethod()");
          }

       /* package */ void packageMethod()
          {
            log.info("packageMethod()");
          }

        private void privateMethod()
          {
            log.info("privateMethod()");
          }
      }

    @DciContext(autoThreadBinding = true)
    static class WithAnnotationAndAutoThreadBinding
      {
        public void publicMethod()
          {
            log.info("publicMethod()");
          }

        protected void protectedMethod()
          {
            log.info("protectedMethod()");
          }

       /* package */ void packageMethod()
          {
            log.info("packageMethod()");
          }

        private void privateMethod()
          {
            log.info("privateMethod()");
          }
      }

    private ContextManager contextManager;

    @BeforeClass
    public void setup()
      throws RuntimeException
      {
        contextManager = mock(ContextManager.class);
        // just to see the wrapped methods invoked in the log
        doAnswer(invocation ->
          {
            final Task task = (Task)invocation.getArguments()[1];
            return task.run();
          }).when(contextManager).runWithContext(anyObject(), any(Task.class));

        ContextManager.Locator.set(() -> contextManager);
      }

    @Test(dataProvider = "methodNames")
    public void must_not_bind_context_when_no_annotation (final @Nonnull String methodName)
      throws Exception
      {
        // given
        final Object object = new WithoutAnnotation();
        // when
        invoke(object, methodName);
        // then
        verifyZeroInteractions(contextManager);
      }

    @Test(dataProvider = "methodNames")
    public void must_not_bind_context_when_annotation_present_but_no_autoThreadBinding(final @Nonnull String methodName)
      throws Exception
      {
        // given
        final Object object = new WithAnnotationButNoAutoThreadBinding();
        // when
        invoke(object, methodName);
        // then
        verifyZeroInteractions(contextManager);
      }

    @Test(dataProvider = "methodNames")
    public void must_bind_context_when_annotation_present_and_autoThreadBinding (final @Nonnull String methodName)
      throws Exception
      {
        // given
        final Object object = new WithAnnotationAndAutoThreadBinding();
        // when
        invoke(object, methodName);
        // then
        verify(contextManager, times(1)).runWithContext(same(object), anyObject());
        verifyNoMoreInteractions(contextManager);
      }

    @DataProvider
    private static Object[][] methodNames()
      {
        return new Object[][]
          {
            {"publicMethod"}, {"protectedMethod"}, {"packageMethod"}, {"privateMethod"}
          };
      }

    private void invoke (final @Nonnull Object context, final @Nonnull String methodName)
      throws Exception
      {
        final Method method = context.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(context);
      }
  }
