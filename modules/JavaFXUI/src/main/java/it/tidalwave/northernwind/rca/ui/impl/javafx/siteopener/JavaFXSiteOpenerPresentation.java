/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
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
package it.tidalwave.northernwind.rca.ui.impl.javafx.siteopener;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.nio.file.Path;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import it.tidalwave.role.ui.javafx.Widget;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentation;
import org.springframework.beans.factory.annotation.Configurable;

/***********************************************************************************************************************
 *
 * @stereotype Presentation
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable
public class JavaFXSiteOpenerPresentation implements SiteOpenerPresentation
  {
    @Inject @Nonnull
    private JavaFXBinder binder;

    @Widget("btOpen")
    private Button button;

    @Widget("openSiteMenu")
    private MenuItem menuItem;

    private BoundProperty<Path> folderToOpen;

    @Override // FIXME: encapsulate args in Bindings
    public void bind (final @Nonnull UserAction action, final @Nonnull BoundProperty<Path> folderToOpen)
      {
        binder.bind(button, action);
        binder.bind(menuItem, action);
        this.folderToOpen = folderToOpen;
      }

    @Override
    public void notifyInvitationToSelectAFolder (final @Nonnull UserNotificationWithFeedback notification)
      {
        binder.openDirectoryChooserFor(notification, folderToOpen);
      }
  }
