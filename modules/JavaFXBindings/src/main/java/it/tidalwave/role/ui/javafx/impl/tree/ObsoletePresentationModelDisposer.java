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
package it.tidalwave.role.ui.javafx.impl.tree;

import javax.annotation.Nonnull;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import it.tidalwave.role.ui.PresentationModel;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * This listener calls {@link PresentationModel#dispose()} on instances that have been detached from a {@link TreeView}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class ObsoletePresentationModelDisposer implements ChangeListener<TreeItem<PresentationModel>>
  {
    @Override
    public void changed (final @Nonnull ObservableValue<? extends TreeItem<PresentationModel>> ov,
                         final TreeItem<PresentationModel> oldTreeItem,
                         final TreeItem<PresentationModel> newTreeItem)
      {
        if (oldTreeItem != null)
          {
            disposeRecursively(oldTreeItem);
          }
      }

    private void disposeRecursively (final @Nonnull TreeItem<PresentationModel> treeItem)
      {
        treeItem.getValue().dispose();
        treeItem.setValue(null);

        for (final TreeItem<PresentationModel> childTreeItem : treeItem.getChildren())
          {
            disposeRecursively(childTreeItem);
          }
      }
  }