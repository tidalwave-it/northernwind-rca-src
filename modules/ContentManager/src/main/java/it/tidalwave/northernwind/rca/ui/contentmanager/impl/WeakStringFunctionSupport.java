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
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.Changeable;
import it.tidalwave.role.ui.function.UnaryBoundFunctionSupport;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * Changes the destination only at a certain condition in function of the target.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class WeakStringFunctionSupport extends UnaryBoundFunctionSupport<String, String> implements Changeable<String>
  {
    @Nonnull
    private String targetValue;

    public WeakStringFunctionSupport (final @Nonnull BoundProperty<String> sourceProperty)
      {
        super(sourceProperty);
      }

    @Override
    protected void onSourceChange (final @Nonnull String oldValue, final @Nonnull String newValue)
      {
        final String oldF = function(oldValue);
        value = function(newValue);

        if (shouldChange(oldF))
          {
            pcs.firePropertyChange("value", oldF, value);
          }
      }

    @Override
    public void set (final String value)
      {
        this.targetValue = value;
      }

    protected boolean shouldChange (final String value)
      {
        return value.equals(targetValue) || "".equals(targetValue);
      }
  }
