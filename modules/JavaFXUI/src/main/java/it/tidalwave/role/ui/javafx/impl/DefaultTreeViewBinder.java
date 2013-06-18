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
package it.tidalwave.role.ui.javafx.impl;

import javax.annotation.Nonnull;
import java.util.List;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.application.Platform;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.role.ui.javafx.TreeViewBinder;
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
    private final Callback<TreeView<As>, TreeCell<As>> cellFactory = new Callback<TreeView<As>, TreeCell<As>>()
      {
        @Override @Nonnull
        public TreeCell<As> call (final @Nonnull TreeView<As> p)
          {
            final TextFieldTreeCell<As> cell = new TextFieldTreeCell<>();
            cell.setConverter(new StringConverter<As>()
              {
                @Override
                public String toString (final @Nonnull As object)
                  {
                    try
                      {
                        return object.as(Displayable.class).getDisplayName();
                      }
                    catch (AsException e)
                      {
                        return object.toString();
                      }
                  }

                @Override
                public As fromString (final @Nonnull String string)
                  {
                    throw new UnsupportedOperationException("Not supported yet.");
                  }
              });

            return cell;
          }
      };

    private final ChangeListener<TreeItem<As>> changeListener = new ChangeListener<TreeItem<As>>()
      {
        @Override
        public void changed (final @Nonnull ObservableValue<? extends TreeItem<As>> ov,
                             final @Nonnull TreeItem<As> oldItem,
                             final @Nonnull TreeItem<As> item)
          {
            try
              {
                item.getValue().as(Selectable.class).select();
              }
            catch (AsException e)
              {
                e.printStackTrace();
                // ok, do nothing
              }
          }
      };

    @Override
    public void bind (final @Nonnull PresentationModel pm,
                      final @Nonnull TreeView<As> treeView)
      {
        assert Platform.isFxApplicationThread() : "Must run in the JavaFX Application Thread";

        treeView.setRoot(createTreeItem(pm));
        treeView.setCellFactory(cellFactory);
        treeView.getSelectionModel().selectedItemProperty().addListener(changeListener);
     }

    @Nonnull
    private TreeItem<As> createTreeItem (final @Nonnull PresentationModel pm)
      {
        final TreeItem<As> rootItem = new TreeItem<As>(pm);

        addChildren(pm, rootItem);

        return rootItem;
      }

    // FIXME: add on demand, upon node expansion
    private void addChildren (final @Nonnull As datum,
                              final @Nonnull TreeItem<As> parentItem)
      {
        final SimpleComposite<? extends As> composite = datum.as(SimpleComposite.class);
        final List<? extends As> objects = composite.findChildren().results();

        for (final As object : objects)
          {
            final TreeItem<As> item = new TreeItem<>(object);
            addChildren(object, item);
            parentItem.getChildren().add(item);
          }
      }
  }
