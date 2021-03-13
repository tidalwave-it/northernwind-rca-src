/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.model.impl.admin;

import javax.annotation.Nonnull;
import java.util.UUID;
import it.tidalwave.util.Id;
import it.tidalwave.role.IdFactory;

/***********************************************************************************************************************
 *
 * The application factory for {@link Id}s.
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id$
 *
 **********************************************************************************************************************/
public class AdminIdFactory implements IdFactory
  {
    @Override @Nonnull
    public Id createId()
      {
        return new Id(UUID.randomUUID());
      }

    @Override @Nonnull
    public Id createId (final @Nonnull Class<?> objectClass)
      {
        return createId();
      }

    @Override @Nonnull
    public Id createId (final @Nonnull Class<?> objectClass, final @Nonnull Object object)
      {
        return createId(objectClass);
      }
  }
