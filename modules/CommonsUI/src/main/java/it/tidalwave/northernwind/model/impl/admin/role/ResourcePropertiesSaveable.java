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
package it.tidalwave.northernwind.model.impl.admin.role;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import it.tidalwave.northernwind.code.model.TimeProvider;
import it.tidalwave.util.Key;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.model.admin.role.WritableFolder;
import it.tidalwave.northernwind.model.admin.role.Saveable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.Marshallable.*;
import static it.tidalwave.northernwind.model.admin.Properties.*;
import static it.tidalwave.northernwind.model.admin.role.WritableFolder.*;

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
    /** The properties that must be stored in a separate file. */
    private static final List<Key<?>> EXTERNAL_PROPERTIES = Arrays.<Key<?>>asList(PROPERTY_FULL_TEXT);

    @Inject @Nonnull
    private TimeProvider timeProvider;

    @Nonnull
    private final ResourceProperties properties;

    @Override
    public void saveIn (final @Nonnull ResourceFile folder)
      {
        try
          {
            log.debug("saveIn({}, {})", folder, properties);
            final WritableFolder writableFolder = folder.as(WritableFolder);
            final ResourceProperties p1 = properties.withProperty(PROPERTY_LATEST_MODIFICATION_DATE,
                                                                  timeProvider.getNow());
            final ResourceProperties p2 = saveExternalProperties(p1, writableFolder);
            // FIXME: guess the localization (some properties go to Properties, some other to Properties_en.xml etc...
            writableFolder.write("Properties.xml", p2.as(Marshallable));
          }
        catch (IOException e)
          {
            log.error("property class: " + properties.getClass(), e);
          }
      }

    @Nonnull
    private ResourceProperties saveExternalProperties (@Nonnull ResourceProperties properties,
                                                       final @Nonnull WritableFolder writableFolder)
      throws IOException
      {
        for (final Key<?> property : EXTERNAL_PROPERTIES)
          {
            // FIXME: localization
            // FIXME: conversion to string when different types are used
            writableFolder.write(property.stringValue() + "_en.xhtml", properties.getProperty(property).toString());
            properties = properties.withoutProperty(property);
          }

        return properties;
      }
  }
