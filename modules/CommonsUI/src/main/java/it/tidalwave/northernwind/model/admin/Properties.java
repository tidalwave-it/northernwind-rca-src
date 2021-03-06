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
package it.tidalwave.northernwind.model.admin;

import it.tidalwave.util.Key;
import it.tidalwave.util.Id;
import java.time.ZonedDateTime;
import lombok.experimental.UtilityClass;

/***********************************************************************************************************************
 *
 * FIXME: these properties are redefined here, but the are also in NorthernWind. Use the original ones.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@UtilityClass
public final class Properties
  {
    public static final Key<String> PROPERTY_FULL_TEXT = Key.of("fullText", String.class);

    public static final Key<ZonedDateTime> PROPERTY_LATEST_MODIFICATION_DATE = Key.of("latestModificationDateTime", ZonedDateTime.class);

    public static final Key<String> PROPERTY_TITLE = Key.of("title", String.class);

    public static final Key<String> PROPERTY_EXPOSED_URI = Key.of("exposedUri", String.class);

    public static final Key<ZonedDateTime> PROPERTY_CREATION_TIME = Key.of("creationDateTime", ZonedDateTime.class);

    public static final Key<ZonedDateTime> PROPERTY_PUBLISHING_TIME = Key.of("publishingDateTime", ZonedDateTime.class);

    public static final Key<String> PROPERTY_TAGS = Key.of("tags", String.class);

    public static final Key<Id> PROPERTY_ID = Key.of("id", Id.class);
  }
