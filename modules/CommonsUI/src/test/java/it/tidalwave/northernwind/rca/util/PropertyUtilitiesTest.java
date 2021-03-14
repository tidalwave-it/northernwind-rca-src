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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import it.tidalwave.util.Key;
import it.tidalwave.role.ui.Displayable;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.model.admin.Properties;
import it.tidalwave.northernwind.model.impl.admin.AdminModelFactory;
import it.tidalwave.role.ui.PresentationModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

/***********************************************************************************************************************
 *
 * A provider of a {@link PresentationModel} for {@link ResourceProperties}.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class PropertyUtilitiesTest
  {
    private ResourceProperties properties;

    private ModelFactory factory;

    @BeforeClass
    public void prepareProperties()
      {
        factory = new AdminModelFactory();
        properties = factory.createProperties().build()
                .withProperty(Properties.PROPERTY_CREATION_TIME, ZonedDateTime.ofInstant(Instant.ofEpochMilli(1342536534636L), ZoneId.of("CET")))
                .withProperty(Properties.PROPERTY_TITLE, "the title");
        PropertyUtilities.setLocale(Locale.UK);
        PropertyUtilities.setZone(ZoneId.of("CET"));
      }

    @Test(dataProvider = "keysAndExpectedValues")
    public void must_create_proper_Displayables (final @Nonnull Key<?> key, final @Nonnull String expectedValue)
      {
        // given the properties
        // when
        final Displayable displayable = PropertyUtilities.displayableForValue(properties, key);
        // then
        assertThat(displayable.getDisplayName(), is(expectedValue));
      }

    @DataProvider
    private static Object[][] keysAndExpectedValues()
      {
        return new Object[][]
          {
            { Properties.PROPERTY_CREATION_TIME, "2012-07-17T16:48:54.636+02:00" },
            { Properties.PROPERTY_TITLE,         "the title" }
          };
      }
}
