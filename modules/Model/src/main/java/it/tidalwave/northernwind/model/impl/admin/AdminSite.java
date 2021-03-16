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

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import it.tidalwave.northernwind.core.model.ResourceFileSystemProvider;
import it.tidalwave.northernwind.core.model.ResourcePath;
import it.tidalwave.northernwind.core.model.Site;
import it.tidalwave.northernwind.core.model.SiteFinder;
import it.tidalwave.northernwind.core.model.Template;
import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class AdminSite implements Site
  {
    @Override @Nonnull
    public String getContextPath()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public String createLink (final @Nonnull ResourcePath relativeUri)
      {
        return relativeUri.asString();
      }

    @Override @Nonnull
    public <Type> SiteFinder<Type> find (final @Nonnull Class<Type> type)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public ResourceFileSystemProvider getFileSystemProvider()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public List<Locale> getConfiguredLocales()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public Template getTemplate (@Nonnull Class<?> clazz,
                                 @Nonnull Optional<ResourcePath> templatePath,
                                 @Nonnull String embeddedResourceName)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public Optional<String> getTemplate (@Nonnull Class<?> clazz, @Nonnull ResourcePath templatePath)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }
  }
