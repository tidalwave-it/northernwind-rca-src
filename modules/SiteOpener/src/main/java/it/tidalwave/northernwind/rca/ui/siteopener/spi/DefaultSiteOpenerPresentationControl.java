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
package it.tidalwave.northernwind.rca.ui.siteopener.spi;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.spi.UserActionSupport;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.northernwind.rca.ui.event.OpenSiteEvent;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentation;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentationControl;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.util.ui.UserNotificationWithFeedback.*;

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
    @Inject @Named("applicationMessageBus") @Nonnull
    private MessageBus messageBus;

    private SiteOpenerPresentation presentation;

    @VisibleForTesting final BoundProperty<Path> folderToOpen = new BoundProperty<>();

    @VisibleForTesting final UserAction openSiteAction = new UserActionSupport()
      {
        @Override public void actionPerformed()
          {
            presentation.notifyFolderSelectionNeeded(notificationWithFeedback()
                                                    .withCaption("Select the site to open")
                                                    .withFeedback(new Feedback()
              {
                @Override public void onConfirm()
                  throws IOException
                  {
                    messageBus.publish(new OpenSiteEvent(folderToOpen.get()));
                  }
              }));
          }
      };

    @Override
    public void initialize (final @Nonnull SiteOpenerPresentation presentation)
      {
        this.presentation = presentation;
        folderToOpen.set(getHomeFolder());
        presentation.bind(openSiteAction, folderToOpen);
      }

    @Nonnull
    private Path getHomeFolder()
      {
        return new File(System.getProperty("user.home")).toPath();
      }
  }
