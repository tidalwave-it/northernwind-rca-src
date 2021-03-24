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
import java.util.Collection;
import java.util.Collections;
import it.tidalwave.util.annotation.VisibleForTesting;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.role.ui.Displayable;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.UserActionProvider;
import it.tidalwave.role.ui.spi.DefaultUserActionProvider;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.event.CreateContentRequest;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * A {@link UserActionProvider} for {@link Content} that sends a {@link CreateContentRequest} for creating a new
 * {@code Content} child.
 *
 * @stereotype role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@DciRole(datumType = Content.class) @RequiredArgsConstructor @ToString(of = "content")
public class CreateContentRequestActionProvider extends DefaultUserActionProvider
  {
    @Nonnull
    private final MessageBus messageBus;

    @Nonnull
    private final Content content;

    @VisibleForTesting final UserAction sendCreateContentRequestAction =
            UserAction.of(this::publish, Displayable.of("New content"));

    @Override @Nonnull
    public Collection<? extends UserAction> getActions()
      {
        return Collections.singletonList(sendCreateContentRequestAction);
      }

    private void publish()
      {
        messageBus.publish(CreateContentRequest.of(content));
      }
  }
