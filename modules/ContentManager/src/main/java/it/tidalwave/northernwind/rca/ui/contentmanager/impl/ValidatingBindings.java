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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation;
import static it.tidalwave.role.ui.function.NonEmptyFunction.*;
import static it.tidalwave.role.ui.function.AndFunction.*;
import static it.tidalwave.role.ui.function.CopyIfEmptyOrConform.*;
import static it.tidalwave.northernwind.rca.ui.contentmanager.impl.UriNormalizedFunction.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class ValidatingBindings extends AddContentPresentation.Bindings
  {
    public ValidatingBindings()
      {
        titleValid.bind(nonEmpty(title));
        folderValid.bind(nonEmpty(folder));
        exposedUriValid.bind(nonEmpty(exposedUri));
        valid.bind(and(titleValid, folderValid, exposedUriValid));

        folder.bind(copyIfEmptyOrConform(title));
        exposedUri.bind(copyIfEmptyOrConform(uriNormalized(title)));
      }
  }
