/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.ChangingSource;
import it.tidalwave.role.ui.function.UnaryBoundFunctionSupport;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class UriNormalizedFunction extends UnaryBoundFunctionSupport<String, String>
  {
    @Nonnull
    public static UriNormalizedFunction uriNormalized (final @Nonnull ChangingSource<String> source)
      {
        return new UriNormalizedFunction(source);
      }

    public UriNormalizedFunction (final @Nonnull ChangingSource<String> source)
      {
        super(source);
      }

    // FIXME: copied from DefaultContent
    @Override @Nonnull
    protected String function (@Nonnull String string)
      {
        string = StringUtilities.deAccent(string);
        string = string.replaceAll(" ", "-")
                     .replaceAll(",", "")
                     .replaceAll("\\.", "")
                     .replaceAll(";", "")
                     .replaceAll("/", "")
                     .replaceAll("!", "")
                     .replaceAll("\\?", "")
                     .replaceAll(":", "")
                     .replaceAll("[^\\w-]*", "");
        return string.toLowerCase();
      }
  }
