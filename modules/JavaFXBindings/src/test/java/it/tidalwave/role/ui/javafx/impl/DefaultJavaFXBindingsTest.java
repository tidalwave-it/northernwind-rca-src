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

import it.tidalwave.role.Displayable;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.JavaFXBindings;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultJavaFXBindingsTest
  {
    private DefaultJavaFXBindings fixture;

    @BeforeMethod
    public void setupFixture()
      {
        fixture = new DefaultJavaFXBindings();
      }

    @Test
    public void treeCellFactory_must_create_a_TextFieldTreeCell_whose_converter_uses_Displayable()
      {
        final TreeCell<PresentationModel> cell = fixture.treeCellFactory.call(new TreeView<PresentationModel>());

        assertThat(cell, is(instanceOf(TextFieldTreeCell.class)));
        final TextFieldTreeCell cell2 = (TextFieldTreeCell)cell;
        final StringConverter<PresentationModel> stringConverter = cell2.getConverter();

        final PresentationModel pm = mock(PresentationModel.class);
        when(pm.as(eq(Displayable.class))).thenReturn(new DefaultDisplayable("foo"));

        assertThat(stringConverter.toString(pm), is("foo"));
      }
  }