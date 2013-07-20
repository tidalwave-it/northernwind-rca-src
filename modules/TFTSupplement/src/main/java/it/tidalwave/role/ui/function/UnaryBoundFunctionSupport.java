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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import it.tidalwave.role.ui.ChangingSource;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public abstract class UnaryBoundFunctionSupport<SOURCE, TARGET> extends BoundFunctionSupport<SOURCE, TARGET>
  {
    @Nonnull
    protected final ChangingSource<SOURCE> sourceProperty;

    protected TARGET value;

    protected UnaryBoundFunctionSupport (final @Nonnull ChangingSource<SOURCE> sourceProperty)
      {
        this.sourceProperty = sourceProperty;
        sourceProperty.addPropertyChangeListener(new PropertyChangeListener()
          {
            @Override
            public void propertyChange (final @Nonnull PropertyChangeEvent event)
              {
                onSourceChange((SOURCE)event.getOldValue(), (SOURCE)event.getNewValue());
              }
          });
      }

    protected abstract void onSourceChange (SOURCE oldValue, SOURCE newValue);

    @Nonnull
    protected abstract TARGET function (final SOURCE value);

    @Override @Nonnull
    public final TARGET get()
      {
        return value;
      }
  }
