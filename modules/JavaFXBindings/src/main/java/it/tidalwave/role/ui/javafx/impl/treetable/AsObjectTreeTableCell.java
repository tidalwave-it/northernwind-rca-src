/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.role.ui.javafx.impl.treetable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javafx.scene.control.TreeTableCell;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.As;
import it.tidalwave.role.ui.javafx.impl.ContextMenuBuilder;
import it.tidalwave.role.ui.javafx.impl.Utils;
import static it.tidalwave.role.Displayable.Displayable;

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
        getStyleClass().setAll(Utils.getRoleStyles(item));
      }
  }
