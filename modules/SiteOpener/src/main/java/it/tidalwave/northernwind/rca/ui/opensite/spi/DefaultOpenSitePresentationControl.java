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
package it.tidalwave.northernwind.rca.ui.opensite.spi;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import it.tidalwave.role.ui.UserActionSupport;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.northernwind.rca.ui.opensite.OpenSitePresentation;
import it.tidalwave.northernwind.rca.ui.opensite.OpenSitePresentationControl;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @stereotype Control
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultOpenSitePresentationControl implements OpenSitePresentationControl
  {
    @Inject @Named("applicationMessageBus") @Nonnull
    private MessageBus messageBus;

    private OpenSitePresentation presentation;

    private final UserAction action = new UserActionSupport()
      {
        @Override
        public void actionPerformed()
          {
            try
              {
                messageBus.publish(new OpenSiteEvent(new File("/Users/fritz/Personal/WebSites/StoppingDown.net").toPath()));
              }
            catch (IOException e)
              {
                log.error("", e);
              }
          }
      };

    @Override
    public void initialize (final @Nonnull OpenSitePresentation presentation)
      {
        this.presentation = presentation;
        presentation.bind(action);
      }
  }
