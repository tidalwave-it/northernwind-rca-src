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
public abstract class UnaryBoundFunctionSupport<DOMAIN_TYPE, CODOMAIN_TYPE>
                   extends BoundFunctionSupport<DOMAIN_TYPE, CODOMAIN_TYPE>
  {
    @Nonnull
    protected final ChangingSource<DOMAIN_TYPE> source;

    protected CODOMAIN_TYPE value;

    protected UnaryBoundFunctionSupport (final @Nonnull ChangingSource<DOMAIN_TYPE> source)
      {
        this.source = source;
        source.addPropertyChangeListener(new PropertyChangeListener()
          {
            @Override
            public void propertyChange (final @Nonnull PropertyChangeEvent event)
              {
                onSourceChange((DOMAIN_TYPE)event.getOldValue(), (DOMAIN_TYPE)event.getNewValue());
              }
          });
      }

    protected void onSourceChange (final @Nonnull DOMAIN_TYPE oldValue, final @Nonnull DOMAIN_TYPE newValue)
      {
        final CODOMAIN_TYPE oldF = function(oldValue);
        value = function(newValue);
        pcs.firePropertyChange("value", oldF, value);
      }

    @Nonnull
    protected abstract CODOMAIN_TYPE function (final DOMAIN_TYPE value);

    @Override @Nonnull
    public final CODOMAIN_TYPE get()
      {
        return value;
      }
  }
