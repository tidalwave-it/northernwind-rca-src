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
import javax.servlet.http.HttpServletRequest;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.Media;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.Request;
import it.tidalwave.northernwind.core.model.Resource;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.model.Site;
import it.tidalwave.northernwind.core.model.SiteNode;
import it.tidalwave.northernwind.frontend.ui.Layout;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class AdminModelFactory implements ModelFactory
  {
    @Override
    public Resource createResource (final @Nonnull ResourceFile file)
      {
        return new AdminResource(file);
      }

    @Override
    public Content createContent (final @Nonnull ResourceFile file)
      {
        return new AdminContent(file);
      }

    @Override
    public Media createMedia(ResourceFile rf)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public SiteNode createSiteNode (final @Nonnull Site site, final @Nonnull ResourceFile file)
      throws IOException, NotFoundException
      {
        return new AdminSiteNode(file);
      }

    @Override
    public Layout.Builder createLayout()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public Request createRequest()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public Request createRequestFrom(HttpServletRequest hsr)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public ResourceProperties.Builder createProperties()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public Site.Builder createSite()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }
  }
