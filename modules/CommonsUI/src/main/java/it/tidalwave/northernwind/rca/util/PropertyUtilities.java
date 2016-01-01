/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.util;

import javax.annotation.Nonnull;
import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.northernwind.core.model.ResourceProperties;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id$
 *
 **********************************************************************************************************************/
public class PropertyUtilities
  {
    @FunctionalInterface
    static interface Format
      {
        @Nonnull
        public String format (Object object);

        final static Format DEFAULT_FORMAT = object -> "" + object;
        final static Format DATE_TIME_FORMAT = object -> DateTimeFormat.forStyle("FF").print((DateTime)object);

        // FIXME: should get the type from key, but it's not yet supported
        @Nonnull
        public static Format formatFor (final @Nonnull Key<?> key)
          {
            if (key.stringValue().endsWith("DateTime"))
              {
                return DATE_TIME_FORMAT;
              }

            return DEFAULT_FORMAT;
          }
      }

    /*******************************************************************************************************************
     *
     * Returns a 'fixed' value for a property. ResourceProperties at the moment manages each property as a string.
     * This method converts them in the proper type.
     *
     * @param       properties          the properties to read
     * @param       key                 the key
     * @return                          the value
     * @throws      NotFoundException   if the key is not found
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Object getFixedPropertyValue (final @Nonnull ResourceProperties properties, final @Nonnull Key<?> key)
      throws NotFoundException, IOException
      {
        Object property = properties.getProperty(key);

        // FIXME: should be done by ResourceProperties and get the type from key, but it's not yet supported
        if (key.stringValue().endsWith("DateTime"))
          {
            property = ISODateTimeFormat.dateTimeParser().parseDateTime(property.toString());
          }

        return property;
      }

    /*******************************************************************************************************************
     *
     * Returns a default {@link Displayable} for a property value.
     *
     * @param       properties          the properties to read
     * @param       key                 the key
     * @return                          the {@code Displayable}
     * @throws      NotFoundException   if the key is not found
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Displayable displayableForValue (final @Nonnull ResourceProperties properties, final @Nonnull Key<?> key)
      throws NotFoundException, IOException
      {
        final Object value = getFixedPropertyValue(properties, key);
        return new DefaultDisplayable(Format.formatFor(key).format(value));
      }
  }
