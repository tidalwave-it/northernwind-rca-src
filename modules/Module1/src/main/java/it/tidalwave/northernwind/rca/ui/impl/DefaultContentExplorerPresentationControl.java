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
package it.tidalwave.northernwind.rca.ui.impl;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.spi.DefaultSimpleComposite;
import it.tidalwave.northernwind.rca.ui.ContentExplorerPresentation;
import it.tidalwave.northernwind.rca.ui.ContentExplorerPresentationControl;
import it.tidalwave.northernwind.rca.ui.PresentationModelUtil;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * The default implementation for {@link ContentExplorerPresentationControl}.
 *
 * @stereotype Control
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class DefaultContentExplorerPresentationControl implements ContentExplorerPresentationControl
  {
    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void initialize (final @Nonnull ContentExplorerPresentation presentation)
      {
        final Finder<Mock> finder = new SimpleFinderSupport<Mock>()
          {
            @Override
            protected List<? extends Mock> computeResults()
              {
                return Arrays.asList(new Mock("content 1"), new Mock("content 2"));
              }
          };

        final SimpleComposite<Mock> composite = new DefaultSimpleComposite<>(finder);
        presentation.populate(new PresentationModelUtil().createPresentationModel(composite));
      }
  }
