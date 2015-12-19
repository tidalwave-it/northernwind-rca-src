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
package it.tidalwave.northernwind.rca.ui.impl.javafx.contentmanager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import javafx.scene.Node;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.ui.javafx.JavaFXSafeProxyCreator.NodeAndDelegate;
import it.tidalwave.northernwind.rca.ui.contentmanager.AddContentPresentation;
import it.tidalwave.role.ui.javafx.JavaFXBinder;
import lombok.Delegate;
import static it.tidalwave.ui.javafx.JavaFXSafeProxyCreator.createNodeAndDelegate;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class JavaFXAddContentPresentation implements AddContentPresentation
  {
    interface Exclusions
      {
        public void bind (Bindings bindings);
        public void showUp (UserNotificationWithFeedback notification);
      }

    @Inject
    private JavaFXBinder binder;

    private final Node node;

    private Bindings bindings;

    @Delegate(excludes = Exclusions.class)
    private final AddContentPresentation delegate;

    public JavaFXAddContentPresentation()
      throws IOException
      {
        final NodeAndDelegate nad = createNodeAndDelegate(getClass(), "AddContentPresentation.fxml");
        node = nad.getNode();
        delegate = nad.getDelegate();
      }

    @Override
    public void bind (final @Nonnull Bindings bindings)
      {
        this.bindings = bindings;
        delegate.bind(bindings);
      }

    @Override
    public void showUp (final @Nonnull UserNotificationWithFeedback notification)
      {
        assert bindings != null;
        binder.showInModalDialog(node, notification, bindings.valid);
      }
  }
