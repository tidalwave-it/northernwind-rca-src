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
package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ApplicationHandler
  {
    @FXML
    private TreeView<String> tvStructure;

    @FXML
    private TreeView<String> tvContent;

    @FXML
    private void onOpen (final @Nonnull ActionEvent event)
      {
        System.err.println("open: " + event);
      }

    public void initialize()
      {
        tvStructure.setRoot(new TreeItem<>("Structure"));
        tvContent.setRoot(new TreeItem<>("Content"));
      }
  }
