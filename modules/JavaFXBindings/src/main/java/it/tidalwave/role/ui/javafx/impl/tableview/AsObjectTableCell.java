/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
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
package it.tidalwave.role.ui.javafx.impl.tableview;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Configurable;
import javafx.scene.control.cell.TextFieldTableCell;
import it.tidalwave.util.As;
import it.tidalwave.role.ui.javafx.impl.ContextMenuBuilder;
import static it.tidalwave.role.Displayable.*;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable
public class AsObjectTableCell<T extends As> extends TextFieldTableCell<T, T>
  {
    @Inject @Nonnull
    @VisibleForTesting ContextMenuBuilder contextMenuBuilder;

    @Override
    public void updateItem (T item, boolean empty)
      {
        super.updateItem(item, empty); 
        
        setText((item == null) ? "" : item.as(Displayable).getDisplayName());
        setContextMenu((item == null) ? null : contextMenuBuilder.createContextMenu(item));
      }
  }
