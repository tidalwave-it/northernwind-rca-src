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
import org.mockito.ArgumentMatcher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
// FIXME: move to NW
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourcePropertiesMatcher implements ArgumentMatcher<ResourceProperties>
  {
    @Nonnull
    private final Map<Key<String>, String> expectedValuesMap;

    @Nonnull
    public static ResourcePropertiesMatcher resourcePropertiesWith
            (final @Nonnull ImmutableMap.Builder<Key<String>, String> builder)
      {
        return new ResourcePropertiesMatcher(builder.build());
      }

    @Nonnull
    public static ResourcePropertiesMatcher resourcePropertiesWith (final @Nonnull Map<Key<String>, String> map)
      {
        return new ResourcePropertiesMatcher(map);
      }

    @Override
    public boolean matches (final ResourceProperties properties)
      {
        try
          {
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

    @Override @Nonnull
    public String toString()
      {
        return String.format("ResourceProperties(%s)", expectedValuesMap);
      }
  }