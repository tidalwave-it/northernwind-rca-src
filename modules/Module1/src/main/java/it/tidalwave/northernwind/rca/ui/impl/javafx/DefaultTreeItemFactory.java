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
package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import java.util.List;
import javafx.scene.control.TreeItem;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import javafx.application.Platform;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultTreeItemFactory implements TreeItemFactory
  {
    @Override @Nonnull
    public TreeItem<Object> createTreeItem (final @Nonnull PresentationModel pm)
      {
        assert Platform.isFxApplicationThread() : "Must run in the JavaFX Application Thread";

        final TreeItem<Object> rootItem = new TreeItem<Object>(getRootName(pm));
        final SimpleComposite<? extends As> composite = pm.as(SimpleComposite.class);
        final List<? extends As> objects = composite.findChildren().results();

        for (final As object : objects)
          {
            final TreeItem<Object> item = new TreeItem<Object>(object.as(Displayable.class).getDisplayName());
            rootItem.getChildren().add(item);
          }

        return rootItem;
      }

    @Nonnull
    private String getRootName (final @Nonnull PresentationModel pm)
      {
        try
          {
            return pm.as(Displayable.class).getDisplayName();
          }
        catch (AsException e)
          {
            return "root";
          }
      }
  }
