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
package it.tidalwave.northernwind.model.impl.admin;

import javax.annotation.Nonnull;
import it.tidalwave.util.Finder;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.core.model.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * Workaround to TFT-206. AdminContent indirectly implements SimpleComposite8<Content> but it's not discovered
 * by RoleManagerSupport as a SimpleComposite, in spite of SimpleComposite8 extending SimpleComposite.
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@DciRole(datumType = AdminContent.class)
@RequiredArgsConstructor @Slf4j
public class WorkaroundAdminContentSimpleComposite implements SimpleComposite<Content>
  {
    @Nonnull
    private final AdminContent owner;

    @Nonnull @Override
    public Finder<? extends Content> findChildren()
      {
        return owner.findChildren();
      }
  }