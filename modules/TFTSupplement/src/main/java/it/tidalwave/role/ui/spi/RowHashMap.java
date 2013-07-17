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
package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.Map;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.TypeSafeHashMap;
import it.tidalwave.role.ui.Row;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * An implementation of {@link Row} based on a Map.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ToString @Immutable
public class RowHashMap implements Row
  {
    private final TypeSafeHashMap hashMap;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public static RowHashMap create()
      {
        return new RowHashMap();
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private RowHashMap()
      {
        hashMap = new TypeSafeHashMap(Collections.<Key<?>, Object>emptyMap());
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private RowHashMap (final @Nonnull Map<Key<?>, Object> values)
      {
        hashMap = new TypeSafeHashMap(values);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    public <T> T getValue (final @Nonnull Key<T> key)
      throws NotFoundException
      {
        return hashMap.get(key);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public RowHashMap withColumn (final @Nonnull String keyName, final @Nonnull Object value)
      {
        return withColumn(new Key<>(keyName), value);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    public RowHashMap withColumn (final @Nonnull Key<?> key, final @Nonnull Object value)
      {
        final Map<Key<?>, Object> map = hashMap.asMap();
        map.put(key, value);

        return new RowHashMap(map);
      }
  }
