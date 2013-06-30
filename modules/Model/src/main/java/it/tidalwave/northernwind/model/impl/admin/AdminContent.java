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
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.As;
import it.tidalwave.util.Finder;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.spring.SpringAsSupport;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.Resource;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourcePath;
import lombok.Delegate;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable(preConstruction = true)
public class AdminContent extends SpringAsSupport implements Content, As, SimpleComposite<Content>
  {
    @Inject @Nonnull
    private ModelFactory modelFactory;

    @Delegate @Nonnull
    private final Resource resource;

    public AdminContent (final @Nonnull ResourceFile file)
      {
        assert modelFactory != null;
        this.resource = modelFactory.createResource(file);
      }

    @Override @Nonnull
    public ResourcePath getExposedUri()
      throws NotFoundException, IOException
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override @Nonnull
    public Finder<Content> findChildren()
      {
        return new SimpleFinderSupport<Content>()
          {
            @Override
            protected List<? extends Content> computeResults()
              {
                // FIXME: it's not flyweight
                final List<Content> results = new ArrayList<>();

                for (final ResourceFile childFile : resource.getFile().getChildren())
                  {
                    if (childFile.isFolder())
                      {
                        results.add(modelFactory.createContent(childFile));
                      }
                  }

                return results;
              }
          };
      }

    // FIXME: this should be done by SpringAsSupport - see THESEFOOLISHTHINGS-100
    @Override @Nonnull
    public <T> T as (final @Nonnull Class<T> roleType)
      {
        if (SimpleComposite.class.isAssignableFrom(roleType))
          {
            return roleType.cast(this);
          }

        return super.as(roleType);
      }
  }
