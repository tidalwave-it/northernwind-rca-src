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
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import it.tidalwave.util.Finder;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.spi.SimpleFinderSupport;
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
        assert modelFactory != null;

        return new SimpleFinderSupport<SiteNode>()
          {
            @Override
            protected List<? extends SiteNode> computeResults()
              {
                // FIXME: it's not flyweight
                final List<SiteNode> results = new ArrayList<>();

                for (final ResourceFile childFile : getResource().getFile().findChildren().results())
                  {
                    if (childFile.isFolder())
                      {
                        try
                          {
                            results.add(modelFactory.createSiteNode(null, childFile));
                          }
                        catch (IOException | NotFoundException e)
                          {
                            throw new RuntimeException(e);
                          }
                      }
                  }

                return results;
              }
          };
      }
  }
