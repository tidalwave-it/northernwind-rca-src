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
package it.tidalwave.ui.javafx;

import it.tidalwave.role.ui.javafx.JavaFXBinder;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class JavaFXSpringApplication extends JavaFXApplicationWithSplash
  {
    private ClassPathXmlApplicationContext applicationContext;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    protected Parent createParent()
      throws IOException
      {
        return FXMLLoader.load(getClass().getResource("Application.fxml"));
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    protected void initializeInBackground()
      {
        try
          {
            logProperties();
            applicationContext = new ClassPathXmlApplicationContext("classpath*:/META-INF/*AutoBeans.xml");
            applicationContext.registerShutdownHook(); // this actually seems not working, onClosing() does
          }
        catch (Throwable t)
          {
            log.error("", t);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    protected void onStageCreated (@Nonnull Stage stage)
      {
        applicationContext.getBean(JavaFXBinder.class).setMainWindow(stage);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    protected void onClosing()
      {
        applicationContext.close();
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void logProperties()
      {
        final SortedMap<Object, Object> map = new TreeMap<>(System.getProperties());

        for (final Entry<Object, Object> e : map.entrySet())
          {
            log.debug("{}: {}", e.getKey(), e.getValue());
          }
      }
  }