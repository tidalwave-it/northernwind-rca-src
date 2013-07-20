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
import it.tidalwave.role.ui.BoundProperty;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class NonEmptyFunction extends UnaryBoundFunctionSupport<String, Boolean>
  {
    public NonEmptyFunction (final @Nonnull BoundProperty<String> sourceProperty)
      {
        super(sourceProperty);
      }

    @Override
    protected void onSourceChange (final String oldValue, final String newValue)
      {
        final boolean oldNonNull = function(oldValue);
        value = function(newValue);
        pcs.firePropertyChange("value", oldNonNull, (boolean)value);
      }

    @Override
    protected Boolean function (final String value)
      {
        return (value != null) && !"".equals(value.trim());
      }
  }
