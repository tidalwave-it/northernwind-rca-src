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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.northernwind.core.model.Resource;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.core.model.spi.ResourceSupport;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.io.Unmarshallable.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@Slf4j
public class AdminResource extends ResourceSupport
  {
    @Nonnull
    private final PatchedTextResourcePropertyResolver propertyResolver;

    public AdminResource (@Nonnull final Resource.Builder builder)
      {
        super(builder);
        propertyResolver = new PatchedTextResourcePropertyResolver(getFile());
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

    @Override
    public boolean isPlaceHolder()
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
        final ResourceFile file = getFile();
        log.debug("loadProperties() for {}", file.getPath().asString());

        ResourceProperties properties = modelFactory.createProperties().withPropertyResolver(propertyResolver).build();

        try
          {
            final ResourceFile propertyFile = file.findChildren().withName("Properties.xml").result(); // FIXME reuse the inheritance helper
    //        log.trace(">>>> reading properties from {} ({})...", propertyFile.getPath().asString(), locale);
            @Cleanup final InputStream is = propertyFile.getInputStream();
            final ResourceProperties tempProperties =
    //            modelFactory.createProperties().build().as(_Unmarshallable_).unmarshal(is);
                    modelFactory.createProperties().withPropertyResolver(propertyResolver).build().as(_Unmarshallable_).unmarshal(is);
    //        log.trace(">>>>>>>> read properties: {} ({})", tempProperties, locale);
            properties = properties.merged(tempProperties);
          }
        catch (NotFoundException e)
          {
            // ok, no properties
          }

        return properties;
      }
  }
