/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
 * %%
 * Copyright (C) 2013 - 2014 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.ui.contentmanager.impl;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.spi.DefaultUserActionProvider;
import it.tidalwave.role.ui.spi.UserActionSupport;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.ui.event.CreateContentRequest;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.role.ui.UserActionProvider;
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
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datumType = Content.class)
@Configurable @RequiredArgsConstructor @ToString(of = "content")
public class CreateContentRequestActionProvider extends DefaultUserActionProvider
  {
    @Nonnull
    private final Content content;

    @Inject @Named("applicationMessageBus")
    @VisibleForTesting MessageBus messageBus;

    @VisibleForTesting final UserAction sendCreateContentRequestAction = new UserActionSupport(
            new DefaultDisplayable("New content"))
      {
        @Override @Nonnull
        public void actionPerformed()
          {
            messageBus.publish(new CreateContentRequest(content));
          }
      };

    @Override @Nonnull
    public Collection<? extends UserAction> getActions()
      {
        return Collections.singletonList(sendCreateContentRequestAction);
      }
  }
