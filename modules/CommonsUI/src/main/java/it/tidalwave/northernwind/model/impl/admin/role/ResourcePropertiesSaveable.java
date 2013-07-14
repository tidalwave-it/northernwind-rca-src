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
package it.tidalwave.northernwind.model.impl.admin.role;

import it.tidalwave.northernwind.model.admin.role.WritableFolder;
import it.tidalwave.northernwind.model.admin.role.Saveable;
import javax.annotation.Nonnull;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.role.Marshallable;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datumType = ResourceProperties.class)
@RequiredArgsConstructor @Slf4j
public class ResourcePropertiesSaveable implements Saveable
  {
    public static final Key<String> PROPERTY_FULL_TEXT = new Key<>("fullText"); // FIXME copied
    public static final Key<String> PROPERTY_LATEST_MODIFICATION_DATE = new Key<>("latestModificationDateTime");// FIXME: those should be Key<DateTime>

    private static final List<Key<?>> EXTERNAL_PROPERTIES = Arrays.<Key<?>>asList(PROPERTY_FULL_TEXT);

    private final DateTimeFormatter ISO_FORMATTER = ISODateTimeFormat.dateTime();

    @Nonnull
    private final ResourceProperties properties;

    @Override
    public void saveFor (final @Nonnull Content content)
      {
        try
          {
            log.info("saveFor({}, {})", content.getFile(), properties);
            final WritableFolder writableFolder = content.getFile().as(WritableFolder.class);

            ResourceProperties p = properties.withProperty(PROPERTY_LATEST_MODIFICATION_DATE,
                                                           ISO_FORMATTER.print(new DateTime()));

            for (final Key<?> property : EXTERNAL_PROPERTIES)
              {
                // FIXME: localization
                // FIXME: conversion to string when different types are used
                writableFolder.write(property.stringValue() + "_en.xhtml", p.getProperty(property).toString());

                // p = p.withoutProperty(key); // FIXME
                final Field mapField = p.getClass().getDeclaredField("propertyMap");
                mapField.setAccessible(true);
                final Map<Key<?>, Object> map = (Map<Key<?>, Object>)mapField.get(p);
                map.remove(property);
                // END FIXME
              }

            // FIXME: guess the localization (some properties go to Properties, some other to Properties_en.xml etc...
            writableFolder.write("Properties.xml", p.as(Marshallable.class));
          }
        catch (NotFoundException | IOException | IllegalAccessException | NoSuchFieldException e)
          {
            log.error("property class: " + properties.getClass(), e);
          }
      }
  }
