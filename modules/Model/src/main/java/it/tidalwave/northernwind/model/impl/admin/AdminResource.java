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
import java.io.InputStream;
import it.tidalwave.util.Id;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.spring.SpringAsSupport;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.Resource;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import lombok.Cleanup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.Delegate;
import static it.tidalwave.role.Unmarshallable.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class AdminResource implements Resource
  {
    @Nonnull
    private final ModelFactory modelFactory;

    @Getter @Nonnull
    private final ResourceFile file;

    @Nonnull
    private final PatchedTextResourcePropertyResolver propertyResolver;

    @Delegate
    private final SpringAsSupport asSupport = new SpringAsSupport(this);

    public AdminResource (final @Nonnull Resource.Builder builder)
      {
        this.modelFactory = builder.getModelFactory();
        this.file = builder.getFile();
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

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private ResourceProperties loadProperties()
      throws IOException
      {
        log.debug("loadProperties() for {}", file.getPath().asString());

        ResourceProperties properties = modelFactory.createProperties().withPropertyResolver(propertyResolver).build();

        try
          {
            final ResourceFile propertyFile = file.findChildren().withName("Properties.xml").result(); // FIXME reuse the inheritance helper
    //        log.trace(">>>> reading properties from {} ({})...", propertyFile.getPath().asString(), locale);
            @Cleanup final InputStream is = propertyFile.getInputStream();
            final ResourceProperties tempProperties =
    //            modelFactory.createProperties().build().as(Unmarshallable).unmarshal(is);
                    modelFactory.createProperties().withPropertyResolver(propertyResolver).build().as(Unmarshallable).unmarshal(is);
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
