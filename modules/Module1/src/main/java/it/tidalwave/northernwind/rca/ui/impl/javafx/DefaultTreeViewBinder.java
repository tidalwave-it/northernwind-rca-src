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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.application.Platform;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultTreeViewBinder implements TreeViewBinder
  {
    @Override
    public void bind (final @Nonnull PresentationModel pm,
                      final @Nonnull TreeView<Object> treeView)
      {
        treeView.setRoot(createTreeItem(pm));

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Object>>()
          {
            @Override
            public void changed (final @Nonnull ObservableValue<? extends TreeItem<Object>> ov,
                                 final @Nonnull TreeItem<Object> oldItem,
                                 final @Nonnull TreeItem<Object> item)
              {
                log.info("selected {}", item);
              }
          });
      }

    @Nonnull
    private TreeItem<Object> createTreeItem (final @Nonnull PresentationModel pm)
      {
        assert Platform.isFxApplicationThread() : "Must run in the JavaFX Application Thread";

        final TreeItem<Object> rootItem = new TreeItem<Object>(getRootName(pm));

        addChildren(pm, rootItem);

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

    // FIXME: add on demand, upon node expansion
    private void addChildren (final @Nonnull As datum,
                              final @Nonnull TreeItem<Object> parentItem)
      {
        final SimpleComposite<? extends As> composite = datum.as(SimpleComposite.class);
        final List<? extends As> objects = composite.findChildren().results();

        for (final As object : objects)
          {
            final TreeItem<Object> item = new TreeItem<Object>(object.as(Displayable.class).getDisplayName());
            addChildren(object, item);
            parentItem.getChildren().add(item);
          }
      }
  }
