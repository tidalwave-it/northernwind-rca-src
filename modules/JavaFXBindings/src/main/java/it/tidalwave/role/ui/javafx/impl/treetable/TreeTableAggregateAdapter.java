/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.role.ui.javafx.impl.treetable;

import it.tidalwave.role.Aggregate;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.spi.DefaultPresentationModel;
import it.tidalwave.util.AsException;
import it.tidalwave.util.NotFoundException;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
class TreeTableAggregateAdapter implements Callback<TreeTableColumn.CellDataFeatures<PresentationModel, PresentationModel>, ObservableValue<PresentationModel>> 
  {
    private final static PresentationModel EMPTY = new DefaultPresentationModel("???", new DefaultDisplayable("???"));
    
    @Override
    public ObservableValue<PresentationModel> call(TreeTableColumn.CellDataFeatures<PresentationModel, PresentationModel> cell) 
      {
        return new ObservableValueBase<PresentationModel>() // FIXME: use a concrete specialization?
          {
            @Override @Nonnull
            public PresentationModel getValue()
              {
                try
                  {
                    // FIXME: uses the column header names, should be an internal id instead
                    final Aggregate<PresentationModel> aggregate = cell.getValue().getValue().as(Aggregate.class);
                    return aggregate.getByName(cell.getTreeTableColumn().getText());
                  }
                catch (AsException | NullPointerException | NotFoundException e) // FIXME: NPE
                  {
                    return EMPTY;
                  }
              };
          };
      }
  }
