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
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.control.TableColumn;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.Aggregate;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.spi.DefaultPresentationModel;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class AggregateAdapter implements Callback<TableColumn.CellDataFeatures<PresentationModel, PresentationModel>, 
                                         ObservableValue<PresentationModel>>
  {
    private final static PresentationModel EMPTY = new DefaultPresentationModel(new DefaultDisplayable("???"));
    
    @Override @Nonnull
    public ObservableValue<PresentationModel> call 
        (final @Nonnull TableColumn.CellDataFeatures<PresentationModel, PresentationModel> cell) 
      {
        return new ObservableValueBase<PresentationModel>() // FIXME: use a concrete specialization?
          {
            @Override @Nonnull
            public PresentationModel getValue()
              {
                try
                  {
                    // FIXME: uses the column header names, should be an internal id instead
                    final Aggregate<PresentationModel> aggregate = cell.getValue().as(Aggregate.class);
                    return aggregate.getByName(cell.getTableColumn().getText());
                  }
                catch (NotFoundException e)
                  {
                    return EMPTY;
                  }
              };
          };
      }
  }
