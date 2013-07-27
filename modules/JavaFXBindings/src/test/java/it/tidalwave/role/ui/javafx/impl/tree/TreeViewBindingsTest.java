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

import javafx.scene.control.TreeItem;
import java.util.concurrent.Executors;
import it.tidalwave.util.spi.AsDelegateProvider;
import it.tidalwave.role.ContextManager;
import it.tidalwave.role.spi.DefaultContextManagerProvider;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.Selectable;
import it.tidalwave.role.ui.javafx.impl.EmptyAsDelegateProvider;
import it.tidalwave.role.ui.spi.DefaultPresentationModel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class TreeViewBindingsTest
  {
    private TreeViewBindings fixture;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        AsDelegateProvider.Locator.set(new EmptyAsDelegateProvider());
        ContextManager.Locator.set(new DefaultContextManagerProvider());
        fixture = new TreeViewBindings(Executors.newSingleThreadExecutor());
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void treeItemChangeListener_must_callback_a_Selectable_on_selection_change()
      {
        final Selectable selectable = mock(Selectable.class);
        final Object datum = new Object();
        final PresentationModel oldPm = new DefaultPresentationModel(datum, selectable);
        final PresentationModel pm = new DefaultPresentationModel(datum, selectable);

        fixture.treeItemChangeListener.changed(null, new TreeItem<>(oldPm), new TreeItem<>(pm));

        verify(selectable, times(1)).select();
        verifyNoMoreInteractions(selectable);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void treeItemChangeListener_must_do_nothing_when_there_is_no_Selectable_role()
      {
        final Object datum = new Object();
        final PresentationModel oldPm = new DefaultPresentationModel(datum);
        final PresentationModel pm = new DefaultPresentationModel(datum);

        fixture.treeItemChangeListener.changed(null, new TreeItem<>(oldPm), new TreeItem<>(pm));

        // we're testing that no exceptions are thrown
      }
  }
