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
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.As;
import it.tidalwave.util.Id;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.Resource;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import lombok.Cleanup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.Unmarshallable.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class AdminResource implements Resource, As//, SimpleComposite<Content>
  {
    @Inject @Nonnull
    private ModelFactory modelFactory;

    @Getter @Nonnull
    private final ResourceFile file;

    @Nonnull
    private final PatchedTextResourcePropertyResolver propertyResolver;

    public AdminResource(ResourceFile file)
      {
        this.file = file;
        propertyResolver = new PatchedTextResourcePropertyResolver(file);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public ResourceProperties getProperties()
      {
        try
          {
            return loadProperties();
          }
        catch (IOException e)
          {
            throw new RuntimeException(e);
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public ResourceProperties getPropertyGroup (final @Nonnull Id id)
      {
        return getProperties().getGroup(id);
      }

    @Override
    public boolean isPlaceHolder()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public <T> T as(Class<T> clazz)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public <T> T as(Class<T> clazz, NotFoundBehaviour<T> notFoundBehaviour)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private ResourceProperties loadProperties()
      throws IOException
      {
        log.debug("loadProperties() for {}", file.getPath().asString());

        final ResourceFile propertyFile = file.getChildByName("Properties.xml"); // FIXME reuse the inheritance helper

        ResourceProperties properties = modelFactory.createProperties().withPropertyResolver(propertyResolver).build();

        if (propertyFile != null)
          {
    //        log.trace(">>>> reading properties from {} ({})...", propertyFile.getPath().asString(), locale);
            @Cleanup final InputStream is = propertyFile.getInputStream();
            final ResourceProperties tempProperties =
    //            modelFactory.createProperties().build().as(Unmarshallable).unmarshal(is);
                    modelFactory.createProperties().withPropertyResolver(propertyResolver).build().as(Unmarshallable).unmarshal(is);
    //        log.trace(">>>>>>>> read properties: {} ({})", tempProperties, locale);
            properties = properties.merged(tempProperties);
          }

        return properties;
      }
  }
