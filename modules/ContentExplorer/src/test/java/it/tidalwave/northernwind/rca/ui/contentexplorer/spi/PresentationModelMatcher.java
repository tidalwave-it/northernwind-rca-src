/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.contentexplorer.spi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.util.AsException;
import it.tidalwave.role.ui.PresentationModel;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@NotThreadSafe
public class PresentationModelMatcher extends BaseMatcher<PresentationModel>
  {
    private final StringBuilder description = new StringBuilder("PresentationModel ");

    private String separator = "";

    private final List<Class<?>> expectedRoleTypes = new ArrayList<>();

    @Nonnull
    public static PresentationModelMatcher presentationModel()
      {
        return new PresentationModelMatcher();
      }

    @Nonnull
    public PresentationModelMatcher withRole (final @Nonnull Class<?> roleType)
      {
        expectedRoleTypes.add(roleType);
        description.append(separator).append(" with role" ).append(roleType.getName());
        separator = ", ";
        return this;
      }

    @Override
    public boolean matches (final Object item)
      {
        if (!(item instanceof PresentationModel))
          {
            return false;
          }

        final PresentationModel pm = (PresentationModel)item;

        for (final Class<?> roleType : expectedRoleTypes)
          {
            try
              {
                pm.as(roleType);
              }
            catch (AsException e)
              {
                return false;
              }
          }

        return true;
      }

    @Override
    public void describeTo (final @Nonnull Description description)
      {
        description.appendText(description.toString());
      }
  }
