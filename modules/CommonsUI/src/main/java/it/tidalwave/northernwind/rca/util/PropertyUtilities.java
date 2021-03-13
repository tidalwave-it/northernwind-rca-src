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
package it.tidalwave.northernwind.rca.util;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import it.tidalwave.util.Key;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import lombok.Getter;
import lombok.Setter;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 *
 **********************************************************************************************************************/
public class PropertyUtilities
  {
    @Getter @Setter
    private static Locale locale = Locale.getDefault();

    @Getter @Setter
    private static ZoneId zone = ZoneId.systemDefault();

    @FunctionalInterface
    static interface Format
      {
        @Nonnull
        public String format (Object object);

        final static Format DEFAULT_FORMAT = object -> "" + object;
        final static Format DATE_TIME_FORMAT = object ->
                DateTimeFormatter.ISO_DATE_TIME.format((TemporalAccessor)object).replaceAll("\\[.*\\]", "");

        @Nonnull
        public static Format formatFor (final @Nonnull Key<?> key)
          {
            if (TemporalAccessor.class.isAssignableFrom(key.getType()))
              {
                return DATE_TIME_FORMAT;
              }

            return DEFAULT_FORMAT;
          }
      }

    /*******************************************************************************************************************
     *
     * Creates a {@link Displayable} for a property value.
     *
     * @param       properties          the {@link ResourceProperties} containing the value
     * @param       key                 the key associated to the value
     * @return                          the {@code Displayable}
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Displayable displayableForValue (final @Nonnull ResourceProperties properties, final @Nonnull Key<?> key)
      {
        return properties.getProperty(key).map(value -> new DefaultDisplayable(Format.formatFor(key).format(value)))
                .orElse((DefaultDisplayable)Displayable.DEFAULT);
      }
  }
