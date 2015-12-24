/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy withCallback the License at
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
package it.tidalwave.northernwind.rca.ui.siteopener.spi;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.spi.UserActionSupport8;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentation;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentation.Bindings;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentationControl;
import it.tidalwave.role.ui.BoundProperty;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.util.ui.Feedback8.feedback;
import static it.tidalwave.util.ui.UserNotificationWithFeedback.notificationWithFeedback;
import java.io.IOException;

/***********************************************************************************************************************
 *
 * @stereotype Control
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultSiteOpenerPresentationControl implements SiteOpenerPresentationControl
  {
    @Inject
    private MessageBus messageBus;

    @Inject
    private SiteOpenerPresentation presentation;

    /* visible for testing */ final Bindings bindings;

    public DefaultSiteOpenerPresentationControl()
      {
        bindings = new Bindings(new BoundProperty<>(getHomeFolder()),
                                UserActionSupport8.withCallback(this::askForOpeningSite));
      }

    @Override
    public void initialize()
      {
        presentation.bind(bindings);
      }

    private void askForOpeningSite()
      throws IOException
      {
        presentation.notifyInvitationToSelectAFolder(notificationWithFeedback()
            .withCaption("Select the site to open")
            .withFeedback(feedback()
                         .withOnConfirm(() -> messageBus.publish(OpenSiteEvent.of(bindings.folderToOpen.get())))));
      }

    @Nonnull
    private static Path getHomeFolder()
      {
        return Paths.get(System.getProperty("user.home"));
      }
  }
