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
package it.tidalwave.northernwind.rca.ui.structureeditor.spi;

import java.io.IOException;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.PresentationModelProvider;
import it.tidalwave.messagebus.annotation.SimpleMessageSubscriber;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.model.impl.admin.AdminContent;
import it.tidalwave.northernwind.model.impl.admin.AdminSiteNode;
import it.tidalwave.northernwind.rca.ui.event.SiteNodeSelectedEvent;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
//import static it.tidalwave.northernwind.rca.ui.contenteditor.spi.DefaultContentEditorPresentationControl.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultStructureEditorPresentationControlTest
  {
    private DefaultStructureEditorPresentationControl fixture;

    private StructureEditorPresentation presentation;

    private AdminSiteNode siteNode; // FIXME: use SiteNode

    private ResourceProperties properties;

    private PresentationModelProvider pmProvider;

    private PresentationModel pm;

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        fixture = new DefaultStructureEditorPresentationControl();
        presentation = mock(StructureEditorPresentation.class);
        siteNode = mock(AdminSiteNode.class);
        properties = mock(ResourceProperties.class);
        pmProvider = mock(PresentationModelProvider.class);
        pm = mock(PresentationModel.class);

        when(siteNode.getProperties()).thenReturn(properties);
        when(pmProvider.createPresentationModel(anyVararg())).thenReturn(pm);
        when(properties.as(eq(PresentationModelProvider.class))).thenReturn(pmProvider);

        fixture.initialize();
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_be_a_MessageSubscriber()
      {
        assertThat(fixture.getClass().getAnnotation(SimpleMessageSubscriber.class), is(not(nullValue())));
      }

//    /*******************************************************************************************************************
//     *
//     ******************************************************************************************************************/
//    @Test
//    public void must_bind()
//      {
//        verify(presentation).bind(same(fixture.fields));
//      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_clear_the_presentation_on_reception_of_an_empty_selection()
      {
        reset(presentation);

        fixture.onSiteNodeSelected(new SiteNodeSelectedEvent());

        verify(presentation).clear();
        verifyNoMoreInteractions(presentation);
      }

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    @Test
    public void must_populate_the_presentation_on_reception_of_selected_node()
      throws IOException
      {
//        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenReturn("full text");
//        when(properties.getProperty(eq(PROPERTY_TITLE), anyString())).thenReturn("title");

        reset(presentation);

        fixture.onSiteNodeSelected(new SiteNodeSelectedEvent(siteNode));

        verify(presentation).populate(matches("Viewer not implemented for .*"));
        verify(presentation).populateProperties(same(pm));
        verify(presentation).showUp();
        verifyNoMoreInteractions(presentation);
//        assertThat(fixture.fields.document.get(), is("full text"));
//        assertThat(fixture.fields.title.get(), is("title"));
      }

//    /*******************************************************************************************************************
//     *
//     ******************************************************************************************************************/
//    @Test
//    public void must_clear_the_presentation_on_error()
//      throws IOException
//      {
//        when(properties.getProperty(eq(PROPERTY_FULL_TEXT), anyString())).thenThrow(new IOException("test"));
//
//        reset(presentation);
//
//        fixture.onContentSelected(new ContentSelectedEvent(content));
//
//        verify(presentation).clear();
//        verifyNoMoreInteractions(presentation);
//      }
  }