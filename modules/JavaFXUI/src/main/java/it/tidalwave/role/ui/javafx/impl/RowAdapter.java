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
import javax.annotation.concurrent.Immutable;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.control.TableColumn.CellDataFeatures;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ui.PresentationModel;
import static it.tidalwave.role.ui.Row.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable
public class RowAdapter<T> implements Callback<CellDataFeatures<PresentationModel, T>, ObservableValue<T>>
  {
    @Override @Nonnull
    public ObservableValue<T> call (final @Nonnull CellDataFeatures<PresentationModel, T> cell)
      {
        return new ObservableValueBase<T>() // FIXME: use a concrete specialization?
          {
            @Override
            public T getValue()
              {
                try
                  {
                    return cell.getValue().as(Row).getValue(new Key<T>(cell.getTableColumn().getId()));
                  }
                catch (NotFoundException e)
                  {
                    return null;
                  }
              };
          };
      }
  }
