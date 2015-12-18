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
package it.tidalwave.northernwind.model.admin.role;

import javax.annotation.Nonnull;
import it.tidalwave.northernwind.core.model.ResourceFile;

/***********************************************************************************************************************
 *
 * A role for objects that can be saved.
 *
 * @stereotype role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface Saveable
  {
    public static final Class<Saveable> Saveable = Saveable.class;

    /***
     *
     * FIXME: this should be save() with no arguments. Requires as far as ResourceProperties doesn't have any reference
     * to its owner, hence to the folder where it should be saved.
     *
     * @param content
     */
    public void saveIn (@Nonnull ResourceFile folder);
  }
