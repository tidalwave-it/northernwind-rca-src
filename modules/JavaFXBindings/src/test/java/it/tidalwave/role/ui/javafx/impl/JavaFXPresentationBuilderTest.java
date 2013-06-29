/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.role.ui.javafx.impl;

import javafx.application.Platform;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
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

    @Test(enabled = false)
    public void must_properly_create_a_presentation_instance()
      {
        final MockProvider fixture = new MockProvider();

        final MockInterface i = fixture.create(null);
      }
}