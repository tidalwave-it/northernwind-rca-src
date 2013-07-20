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
package it.tidalwave.role.ui.function;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.ChangingSource;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class NonEmptyFunction extends UnaryBoundFunctionSupport<String, Boolean>
  {
    @Nonnull
    public static NonEmptyFunction nonEmpty (final @Nonnull ChangingSource<String> source)
      {
        return new NonEmptyFunction(source);
      }

    public NonEmptyFunction (final @Nonnull ChangingSource<String> source)
      {
        super(source);
      }

    @Override
    protected void onSourceChange (final String oldSourceValue, final String newSourceValue)
      {
        final boolean oldValue = function(oldSourceValue);
        value = function(newSourceValue);
        pcs.firePropertyChange("value", oldValue, (boolean)value);
      }

    @Override
    protected Boolean function (final String value)
      {
        return (value != null) && !"".equals(value.trim());
      }
  }
