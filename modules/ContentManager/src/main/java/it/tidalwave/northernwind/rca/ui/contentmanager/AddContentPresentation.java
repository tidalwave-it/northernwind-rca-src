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
package it.tidalwave.northernwind.rca.ui.contentmanager;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.ZonedDateTime;

/***********************************************************************************************************************
 *
 * The presentation for the "add content" feature.
 *
 * @stereotype presentation
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface AddContentPresentation
  {
    @EqualsAndHashCode @ToString
    public static class Bindings
      {
        public final BoundProperty<String> folder = new BoundProperty<>("");
        public final BoundProperty<String> title = new BoundProperty<>("");
        public final BoundProperty<String> tags = new BoundProperty<>("");
        public final BoundProperty<String> exposedUri = new BoundProperty<>("");
        public final BoundProperty<ZonedDateTime> publishingDateTime = new BoundProperty<ZonedDateTime>(null);

        public final BoundProperty<Boolean> folderValid = new BoundProperty<>(false);
        public final BoundProperty<Boolean> titleValid = new BoundProperty<>(false);
        public final BoundProperty<Boolean> tagsValid = new BoundProperty<>(false);
        public final BoundProperty<Boolean> exposedUriValid = new BoundProperty<>(false);
        public final BoundProperty<Boolean> publishingDateTimeValid = new BoundProperty<>(false);

        public final BoundProperty<Boolean> valid = new BoundProperty<>(false);
      }

    /*******************************************************************************************************************
     *
     * Binds to a model
     *
     * @param  bindings  the model
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull Bindings bindings);

    /*******************************************************************************************************************
     *
     * Renders the dialog and provides a confirm/cancel feedback.
     *
     * @param  notification  the user notification
     *
     ******************************************************************************************************************/
    public void showUp (@Nonnull UserNotificationWithFeedback notification);
  }
