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
package it.tidalwave.northernwind.model.impl.admin;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.Finder;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.Resource;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourcePath;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.frontend.ui.Layout;
import lombok.Delegate;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable(preConstruction = true)
public class AdminSiteNode implements SiteNode, ResourceWithAs
  {
    @Inject @Nonnull
    private ModelFactory modelFactory;

    @Delegate @Nonnull
    private final Resource resource;

    public AdminSiteNode (final @Nonnull ResourceFile file)
      {
        this.resource = modelFactory.createResource(file);
      }

    @Override
    public Layout getLayout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResourcePath getRelativeUri() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T as(Class<T> clazz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T as(Class<T> clazz, NotFoundBehaviour<T> notFoundBehaviour) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Finder<Content> findChildren() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
  }
