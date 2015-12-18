/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2014 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.ui.contenteditor.spi;

import javax.annotation.Nonnull;
import java.io.IOException;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;

/***********************************************************************************************************************
 *
 * A role that allows to bind items of {@link ResourceProperties} to other objects, typically for managing a User
 * Interface.
 *
 * @stereotype role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface PropertyBinder
  {
    public static final Class<PropertyBinder> PropertyBinder = PropertyBinder.class;

    /*******************************************************************************************************************
     *
     * A callback that notifies when an item has been updated. Any change to bound objects will cause the creation of
     * a new instance of {@link ResourceProperties} with the updated values, notified through this callback.
     *
     ******************************************************************************************************************/
    public static interface UpdateCallback
      {
        public void notify (@Nonnull ResourceProperties properties);
      }

    /*******************************************************************************************************************
     *
     * Binds a given property to a {@link BoundProperty}. This is useful for any kind of property types, with the
     * exception of HTML documents.
     *
     * @param  propertyName   the property name in {@link ResourceProperties}
     * @param  boundProperty  the object to bind
     * @param  callback       the callback that will notify updates
     *
     ******************************************************************************************************************/
    public <T> void bind (@Nonnull Key<T> propertyName,
                          @Nonnull BoundProperty<T> boundProperty,
                          @Nonnull UpdateCallback callback)
      throws NotFoundException, IOException;

    /*******************************************************************************************************************
     *
     * Creates a Document bound to the given property. This is useful for HTML documents, since they undergo a special
     * processing for being edited in a HTML viewer (CSS and JavaScript resources are inserted in the head).
     *
     * FIXME: I don't like the asymmetry with bind(), that accepts a BoundProperty. This is due to the fact that
     * BoundProperty is mutable, while Document is immutable. Actually, a mutable Document would be acceptable.
     *
     * @param  propertyName   the property name in {@link ResourceProperties}
     * @param  callback       the callback that will notify updates
     * @return                the bound document
     *
     ******************************************************************************************************************/
    @Nonnull
    public Document createBoundDocument (@Nonnull Key<String> propertyName,
                                         @Nonnull UpdateCallback callback);
  }
