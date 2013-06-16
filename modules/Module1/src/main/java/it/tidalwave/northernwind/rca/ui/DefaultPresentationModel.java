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
package it.tidalwave.northernwind.rca.ui;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeListener;
import it.tidalwave.util.AsException;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.util.As;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultPresentationModel implements PresentationModel
  {
    @Nonnull
    private final Object datum;

    @Nonnull
    private final Object[] roles;

    public DefaultPresentationModel (final @Nonnull Object datum,
                                     final @Nonnull Object ... roles)
      {
        this.datum = datum;
        this.roles = roles;
      }

    @Override
    public void addPropertyChangeListener (final @Nonnull PropertyChangeListener listener)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public void addPropertyChangeListener (final @Nonnull String propertyName,
                                           final @Nonnull PropertyChangeListener listener)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public void removePropertyChangeListener (final @Nonnull PropertyChangeListener listener)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public void removePropertyChangeListener (final @Nonnull String propertyName,
                                              final @Nonnull PropertyChangeListener listener)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public boolean hasListeners(String propertyName)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners (final @Nonnull String propertyName)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public <T> T as (final @Nonnull Class<T> type)
      {
        for (final Object role : roles)
          {
            if (type.isAssignableFrom(role.getClass()))
              {
                return (T)role;
              }
          }

        if (datum instanceof As)
          {
            return ((As)datum).as(type);
          }

        throw new AsException(type);
      }

    @Override
    public <T> T as (Class<T> clazz, NotFoundBehaviour<T> notFoundBehaviour)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }
  }
