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
package it.tidalwave.northernwind.rca.ui.impl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.northernwind.core.model.ResourceFile;
import lombok.Getter;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * FIXME: temporary wrapper, until ResourceFileSystem implements As, Composite
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ToString(of = "file")
public class ResourceFileWrapper implements As, SimpleComposite<ResourceFileWrapper>
  {
    @Getter @Nonnull
    private final ResourceFile file;

    private final List<Object> roles = new ArrayList<>();

    public ResourceFileWrapper (final @Nonnull ResourceFile file)
      {
        try
          {
            this.file = file;
            roles.add(this);
            roles.add(new DefaultDisplayable(URLDecoder.decode(file.getName(), "UTF-8")));
          }
        catch (UnsupportedEncodingException e)
          {
            throw new RuntimeException(e);
          }
      }

    @Override
    public <T> T as (final @Nonnull Class<T> type)
      {
        for (final Object role : roles)
          {
            if (type.isAssignableFrom(role.getClass()))
              {
                return (T) role;
              }
          }

        throw new AsException(type);
      }

    @Override
    public <T> T as (Class<T> clazz, NotFoundBehaviour<T> notFoundBehaviour)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public Finder<ResourceFileWrapper> findChildren()
      {
        return new SimpleFinderSupport<ResourceFileWrapper>()
          {
            @Override @Nonnull
            protected List<? extends ResourceFileWrapper> computeResults()
              {
                final List<ResourceFileWrapper> result = new ArrayList<>();

                for (final ResourceFile child : file.getChildren())
                  {
                    result.add(new ResourceFileWrapper(child));
                  }

                return result;
              }
          };
      }
  }
