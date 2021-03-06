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
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourcePath;
import it.tidalwave.northernwind.core.model.Site;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.core.model.spi.SiteNodeSupport;
import it.tidalwave.northernwind.frontend.ui.Layout;
import lombok.Getter;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class AdminSiteNode extends SiteNodeSupport
  {
    @Getter @Nonnull
    private final Site site;

    public AdminSiteNode (@Nonnull final Site site,
                          @Nonnull final ModelFactory modelFactory,
                          @Nonnull final ResourceFile file)
      {
        super(modelFactory, file);
        this.site = site;
      }

    @Override @Nonnull
    public Layout getLayout()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public ResourcePath getRelativeUri()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public Finder<SiteNode> findChildren()
      {
        return new ResourceFinder<>(getFile(), folder -> modelFactory.createSiteNode(site, folder));
      }

    @Override @Nonnull
    public String toString()
      {
        return String.format("AdminSiteNode(%s)", getFile().getPath().asString());
      }
  }
