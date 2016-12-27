/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2016 Tidalwave s.a.s. (http://tidalwave.it)
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
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.fxml.FXML;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentation;

/***********************************************************************************************************************
 *
 * The JavaFX delegate for the function of opening a new site. It manages the related global menu and toolbar button.
 * Note that this delegate is not associated with a specific FXML resource, but it is injected with @FXML resources
 * copied from the application JavaFX delegate.
 *
 * @stereotype Presentation
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class JavaFXSiteOpenerPresentationDelegate implements SiteOpenerPresentation
  {
    @FXML
    private Button btOpen;

    @FXML
    private MenuItem openSiteMenu;

    @Inject
    private JavaFXBinder binder;

    private Bindings bindings;

    @Override
    public void bind (final @Nonnull Bindings bindings)
      {
        this.bindings = bindings;
        binder.bind(btOpen, bindings.openSiteAction);
        binder.bind(openSiteMenu, bindings.openSiteAction);
      }

    @Override
    public void notifyInvitationToSelectAFolder (final @Nonnull UserNotificationWithFeedback notification)
      {
        binder.openDirectoryChooserFor(notification, bindings.folderToOpen);
      }
  }
