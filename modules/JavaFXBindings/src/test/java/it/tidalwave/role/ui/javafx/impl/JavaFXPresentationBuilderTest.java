/*
 *
 * PROJECT NAME
 * PROJECT COPYRIGHT
 *
 ***********************************************************************************************************************
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
 ***********************************************************************************************************************
 *
 * WWW: PROJECT URL
 * SCM: PROJECT SCM
 *
 */
package it.tidalwave.role.ui.javafx.impl;

import javafx.application.Platform;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author fritz
 */
public class JavaFXPresentationBuilderTest
  {
    public static interface MockInterface
      {
        public void theMethod();
      }

    public static class MockImplementation implements MockInterface
      {
        @Override
        public void theMethod()
          {
            assert Platform.isFxApplicationThread();
          }
      }

    public class MockProvider extends JavaFXPresentationBuilder<MockInterface, MockImplementation>
      {
      }

    @Test
    public void testSomeMethod()
      {
        final MockProvider fixture = new MockProvider();

        final MockInterface i = fixture.create(null);
      }
}