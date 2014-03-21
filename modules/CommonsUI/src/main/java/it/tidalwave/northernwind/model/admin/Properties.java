/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
 * %%
 * Copyright (C) 2013 - 2014 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.model.admin;

import it.tidalwave.util.Key;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 *
 * FIXME: these properties are redefined here, but the are also in NorthernWind. Use the original ones.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Properties
  {
    public static final Key<String> PROPERTY_FULL_TEXT = new Key<>("fullText");

    public static final Key<String> PROPERTY_LATEST_MODIFICATION_DATE = new Key<>("latestModificationDateTime");

    public static final Key<String> PROPERTY_TITLE = new Key<>("title");

    public static final Key<String> PROPERTY_EXPOSED_URI = new Key<>("exposedUri");

    public static final Key<String> PROPERTY_CREATION_TIME = new Key<>("creationDateTime");

    public static final Key<String> PROPERTY_PUBLISHING_TIME = new Key<>("publishingDateTime");

    public static final Key<String> PROPERTY_TAGS = new Key<>("tags");
  }
