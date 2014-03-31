/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.role.ui.javafx.impl.treetable;

import com.google.common.annotations.VisibleForTesting;
import static it.tidalwave.role.Displayable.Displayable;
import it.tidalwave.role.ui.javafx.impl.ContextMenuBuilder;
import it.tidalwave.util.As;
import javafx.scene.control.TreeTableCell;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Configurable;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable
class AsObjectTreeTableCell<T extends As> extends TreeTableCell<T, T> 
  {
    @Inject @Nonnull
    @VisibleForTesting ContextMenuBuilder contextMenuBuilder;

    
    @Override
    public void updateItem (final @CheckForNull T item, final boolean empty)
      {
        super.updateItem(item, empty);
        setText((item == null) ? "" : item.as(Displayable).getDisplayName());
        setContextMenu((item == null) ? null : contextMenuBuilder.createContextMenu(item));
      }
  }
