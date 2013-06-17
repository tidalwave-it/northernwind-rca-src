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
import javax.inject.Inject;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceFileSystemProvider;
import it.tidalwave.northernwind.rca.ui.ContentExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.ContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.PresentationModelUtil;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * The default implementation for {@link ContentExplorerPresentationControl}.
 *
 * @stereotype Control
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class DefaultContentExplorerPresentationControl implements ContentExplorerPresentationControl
  {
    @Inject @Nonnull
    private ResourceFileSystemProvider fileSystemProvider;

    private final RoleFactory<ResourceFileWrapper> roleFactory = new RoleFactory<ResourceFileWrapper>()
      {
        @Override
        public Object createRoleFor (final @Nonnull ResourceFileWrapper datum)
          {
            return new Selectable()
              {
                @Override
                public void select()
                  {
                    log.info("Selected {}", datum);
                  }
              };
          }
      };

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void initialize (final @Nonnull ContentExplorerPresentation presentation)
      {
        try
          {
            final ResourceFile root = fileSystemProvider.getFileSystem().findFileByPath("/content/document");
            final ResourceFileWrapper wrapper = new ResourceFileWrapper(root);
            presentation.populate(new PresentationModelUtil().createPresentationModel(wrapper, roleFactory));
          }
        catch (IOException ex)
          {
            ex.printStackTrace();
          }
      }
  }
