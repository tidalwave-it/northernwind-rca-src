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

import it.tidalwave.role.ui.function.AndFunction;
import it.tidalwave.role.ui.function.CopyIfNotEmptyFunction;
import it.tidalwave.role.ui.function.NonEmptyFunction;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ValidatingBindings extends AddContentPresentation.Bindings
  {
    public ValidatingBindings()
      {
        titleValid.bind(new NonEmptyFunction(title));
        folderValid.bind(new NonEmptyFunction(folder));
        exposedUriValid.bind(new NonEmptyFunction(exposedUri));
        valid.bind(new AndFunction(titleValid, folderValid, exposedUriValid));

        folder.bind(new CopyIfNotEmptyFunction(title));
        exposedUri.bind(new CopyIfNotEmptyFunction(new UriNormalizerFunction(title)));
      }
  }
