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
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Platform;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class Main extends ApplicationWithSplash
  {
    private ClassPathXmlApplicationContext applicationContext;

    @Override @Nonnull
    protected Parent createParent()
      throws IOException
      {
        return FXMLLoader.load(getClass().getResource("Application.fxml"));
      }

    @Override
    protected void initializeInBackground()
      {
        applicationContext = new ClassPathXmlApplicationContext("classpath*:/META-INF/*AutoBeans.xml");
      }

    public static void main (final @Nonnull String ... args)
      {
        try
          {
            Platform.setImplicitExit(true);
            launch(args);
            Thread.sleep(2000);
          }
        catch (Throwable t)
          {
            log.error("", t);
            System.exit(-1);
          }
      }
  }