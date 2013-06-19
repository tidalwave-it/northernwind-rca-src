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
 * @author  Fabrizio Giudici
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
