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
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import java.io.IOException;
import it.tidalwave.northernwind.util.test.TestHelper;
import it.tidalwave.northernwind.util.test.TestHelper.TestResource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class JSoupXhtmlNormalizerTest
  {
    private final TestHelper helper = new TestHelper(this);

    private JSoupXhtmlNormalizer underTest;

    @BeforeMethod
    public void prepare()
      {
        underTest = new JSoupXhtmlNormalizer();
      }

    @Test(dataProvider = "documentNames")
    public void must_properly_normalise_Xhtml (final @Nonnull String name)
      throws IOException
      {
        // given
        final TestResource tr = helper.testResourceFor(name);
        final String html = tr.readStringFromResource();
        // when
        final String actual = underTest.asNormalizedString(html);
        // then
        tr.writeToActualFile(actual.replaceAll("\\n$", "")); // method adds a newline
        tr.assertActualFileContentSameAsExpected();
      }

    @DataProvider
    private static Object[][] documentNames()
      {
        return new Object[][]
          {
            { "1.xhtml" },
            { "DocumentWithIndentedCode.xhtml" }
          };
      }
  }
