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
package it.tidalwave.northernwind.rca.ui.impl.javafx.contentmanager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation.Bindings;
import org.springframework.beans.factory.annotation.Configurable;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable
public class JavaFXAddContentPresentationDelegate implements AddContentPresentation
  {
    @Inject @Nonnull
    private JavaFXBinder binder;

    @FXML
    private TextField tfFolder;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfTags;

    @FXML
    private TextField tfExposedUri;

    @FXML
    private TextField tfPublishingDateTime;

    @Override
    public void bind (final @Nonnull Bindings bindings)
      {
        binder.bindBidirectionally(tfFolder, bindings.folder, bindings.folderValid);
        binder.bindBidirectionally(tfTitle, bindings.title, bindings.titleValid);
        binder.bindBidirectionally(tfExposedUri, bindings.exposedUri, bindings.exposedUriValid);
        binder.bindBidirectionally(tfTags, bindings.tags, bindings.tagsValid);
        binder.bindBidirectionally(tfPublishingDateTime, bindings.publishingDateTime,
                                                         bindings.publishingDateTimeValid);
      }

    @Override
    public void showUp (final @Nonnull UserNotificationWithFeedback notification)
      {
        // never called - FIXME: get rid of this?
      }
  }
