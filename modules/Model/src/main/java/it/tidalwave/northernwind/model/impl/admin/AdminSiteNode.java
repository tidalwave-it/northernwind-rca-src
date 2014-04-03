/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
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
package it.tidalwave.northernwind.model.impl.admin;

import javax.annotation.Nonnull;
import java.io.IOException;
import it.tidalwave.util.Finder;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourcePath;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.core.model.spi.SiteNodeSupport;
import it.tidalwave.northernwind.frontend.ui.Layout;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class AdminSiteNode extends SiteNodeSupport
  {
    public AdminSiteNode (final @Nonnull ModelFactory modelFactory,
                          final @Nonnull ResourceFile file)
      {
        super(modelFactory, file);
      }

    @Override
    public Layout getLayout()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public ResourcePath getRelativeUri()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public Finder<SiteNode> findChildren()
      {
        return new ResourceFinder<SiteNode>(getResource().getFile())
          {
            @Override @Nonnull
            protected SiteNode createProduct (final @Nonnull ResourceFile folder)
              throws IOException, NotFoundException
              {
                return modelFactory.createSiteNode(null, folder);
              }
          };
      }
  }
