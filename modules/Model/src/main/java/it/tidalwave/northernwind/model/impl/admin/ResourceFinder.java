/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.northernwind.core.model.Resource;
import it.tidalwave.northernwind.core.model.ResourceFile;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public final class ResourceFinder<T extends Resource> extends SimpleFinderSupport<T>
  {
    @FunctionalInterface
    static interface ProductCreator<T>
      {
        @Nonnull
        public T createProduct (final @Nonnull ResourceFile folder)
          throws IOException, NotFoundException;
      }
    @Nonnull
    private final ResourceFile resourceFile;

    @Nonnull
    private final ProductCreator<T> productCreator;

    public ResourceFinder (final @Nonnull ResourceFinder<T> other, final @Nonnull Object override)
      {
        super(other, override);
        final ResourceFinder<T> source = getSource(ResourceFinder.class, other, override);
        this.resourceFile = source.resourceFile;
        this.productCreator = source.productCreator;
      }

    @Override @Nonnull
    protected List<? extends T> computeResults()
      {
        // FIXME: it's not flyweight
        final List<T> results = new ArrayList<>();

        for (final ResourceFile childFile : resourceFile.findChildren().results())
          {
            if (childFile.isFolder())
              {
                try
                  {
                    results.add(productCreator.createProduct(childFile));
                  }
                catch (IOException | NotFoundException e)
                  {
                    throw new RuntimeException(e);
                  }
              }
          }

        return results;
      }
  }
