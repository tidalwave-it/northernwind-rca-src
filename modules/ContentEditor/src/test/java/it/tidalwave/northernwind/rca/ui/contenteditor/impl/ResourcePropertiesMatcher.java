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
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Map.Entry;
import java.io.IOException;
import com.google.common.collect.ImmutableMap;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourcePropertiesMatcher extends BaseMatcher<ResourceProperties>
  {
    @Nonnull
    private final Map<Key<String>, String> expectedValuesMap;

    @Nonnull
    public static ResourcePropertiesMatcher resourcePropertiesWith
            (final @Nonnull ImmutableMap.Builder<Key<String>, String> builder)
      {
        return new ResourcePropertiesMatcher(builder.build());
      }

    @Override
    public boolean matches (final Object item)
      {
        if (!(item instanceof ResourceProperties))
          {
            return false;
          }

        try
          {
            final ResourceProperties properties = (ResourceProperties)item;

            for (final Entry<Key<String>, String> e : expectedValuesMap.entrySet())
              {
                if (!e.getValue().equals(properties.getProperty(e.getKey())))
                  {
                    return false;
                  }
              }

            return true;
          }
        catch (NotFoundException | IOException e)
          {
            return false;
          }
      }

    @Override
    public void describeTo (final Description description)
      {
        description.appendText("ResourceProperties with " + expectedValuesMap);
      }
    }