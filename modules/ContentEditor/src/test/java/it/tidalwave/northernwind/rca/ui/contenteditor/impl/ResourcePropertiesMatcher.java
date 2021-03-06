/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import java.util.Map.Entry;
import it.tidalwave.util.Key;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import java.util.NoSuchElementException;
import it.tidalwave.util.TypeSafeMap;
import org.mockito.ArgumentMatcher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
// FIXME: move to NW
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourcePropertiesMatcher implements ArgumentMatcher<ResourceProperties>
  {
    @Nonnull
    private final TypeSafeMap expectedValuesMap;

    @Nonnull
    public static ResourcePropertiesMatcher resourcePropertiesWith (@Nonnull final TypeSafeMap map)
      {
        return new ResourcePropertiesMatcher(map);
      }

    @Override
    public boolean matches (final ResourceProperties properties)
      {
        try
          {
            for (final Entry<Key<?>, ?> e : expectedValuesMap.entrySet())
              {
                if (!e.getValue().equals(properties.getProperty(e.getKey()).get()))
                  {
                    return false;
                  }
              }

            return true;
          }
        catch (NoSuchElementException e)
          {
            return false;
          }
      }

    @Override @Nonnull
    public String toString()
      {
        return String.format("ResourceProperties(%s)", expectedValuesMap);
      }
  }