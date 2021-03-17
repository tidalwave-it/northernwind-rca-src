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
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import javax.annotation.Nonnull;
import java.util.Map;
import java.io.IOException;
import it.tidalwave.util.Key;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ModelFactory;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.ui.event.ContentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.northernwind.model.admin.role.Saveable.*;

/***********************************************************************************************************************
 *
 * A default implementation of {@link ContentCreator}.
 *
 * @stereotype role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@DciRole(datumType = Content.class) @RequiredArgsConstructor @Slf4j
public class DefaultContentChildCreator implements ContentChildCreator
  {
    @Nonnull
    private final MessageBus messageBus;

    @Nonnull
    private final ModelFactory modelFactory; // TODO: get from content - depends on NW-197

    @Nonnull
    private final Content parentContent;

    @Override
    public Content createContent (final @Nonnull String folderName,
                                  final @Nonnull Map<Key<?>, Object> propertyValues)
      throws IOException
      {
        log.info("createContent({}, {}) - {}", folderName, propertyValues, parentContent);
        final ResourceFile newFolder = parentContent.getFile().createFolder(folderName);
        final Content content = modelFactory.createContent().withFolder(newFolder).build();
        final ResourceProperties properties = modelFactory.createProperties().withValues(propertyValues).build();
//        content.getProperties().merged(properties).as(Saveable).saveIn(content.getFile());
        properties.as(Saveable).saveIn(content.getFile());

        messageBus.publish(ContentCreatedEvent.of(parentContent, content));

        return content;
      }
  }
