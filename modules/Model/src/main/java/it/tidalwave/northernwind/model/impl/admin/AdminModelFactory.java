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
import java.io.IOException;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.Resource;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.model.Site;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.core.model.spi.ModelFactorySupport;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class AdminModelFactory extends ModelFactorySupport
  {
    @Override @Nonnull
    public Resource createResource (final @Nonnull ResourceFile file)
      {
        return new AdminResource(file);
      }

    @Override @Nonnull
    public Content createContent (final @Nonnull ResourceFile file)
      {
        return new AdminContent(file);
      }

    @Override @Nonnull
    public SiteNode createSiteNode (final @Nonnull Site site, final @Nonnull ResourceFile file)
      throws IOException, NotFoundException
      {
        return new AdminSiteNode(file);
      }

    @Override @Nonnull
    public ResourceProperties build (final @Nonnull ResourceProperties.Builder builder)
      {
        return new it.tidalwave.northernwind.model.impl.admin.DefaultResourceProperties(builder);
      }
  }
