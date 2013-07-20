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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import lombok.Delegate;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public abstract class BoundFunctionSupport<DOMAIN_TYPE, CODOMAIN_TYPE>
                  implements BoundFunction<DOMAIN_TYPE, CODOMAIN_TYPE>
  {
    @Delegate // FIXME: weak
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void unbindAll()
      {
        for (final PropertyChangeListener listener : pcs.getPropertyChangeListeners().clone())
          {
            pcs.removePropertyChangeListener(listener);
          }
      }
  }
