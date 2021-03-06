/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import it.tidalwave.util.PreferencesHandler;
import javafx.application.Platform;
import java.nio.file.Path;
import org.springframework.context.ApplicationContext;
import it.tidalwave.ui.javafx.JavaFXSpringApplication;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentationControl;
import it.tidalwave.northernwind.rca.ui.siteopener.SiteOpenerPresentationControl;
import static it.tidalwave.util.PreferencesHandler.KEY_INITIAL_SIZE;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public class Main extends JavaFXSpringApplication
  {
    public static void main (@Nonnull final String ... args)
      {
        try
          {
            PreferencesHandler.setAppName("Zephyr");
            final PreferencesHandler preferenceHandler = PreferencesHandler.getInstance();
            preferenceHandler.setDefaultProperty(KEY_INITIAL_SIZE, 0.8);
            final Path logFolder = preferenceHandler.getLogFolder();
            final Path configFolder = preferenceHandler.getAppFolder().resolve("config").toAbsolutePath();
            final String logConfigOverride = configFolder.resolve("logback-override.xml").toString();
            System.err.println("Logback override: " + logConfigOverride);
            System.setProperty("it.tidalwave.northernwind.rca.logFolder", logFolder.toString());
            System.setProperty("it.tidalwave.northernwind.rca.logConfigOverride", logConfigOverride);
            Platform.setImplicitExit(true);
            launch(args);
          }
        catch (Throwable t)
          {
            // Don't use logging facilities here, they could be not initialized
            t.printStackTrace();
            System.exit(-1);
          }
      }

    @Override
    public void init()
      {
//        setUseAquaFxOnMacOsX(true);
        super.init();
      }

    @Override
    protected void onStageCreated (@Nonnull final ApplicationContext applicationContext)
      {
        // FIXME: controllers can't initialize in postconstruct
        // Too bad because with PAC+EventBus we'd get rid of the control interfaces
        // Use PowerOnNotification message as in blueMarine II
        applicationContext.getBean(ContentEditorPresentationControl.class).initialize();
        applicationContext.getBean(SiteOpenerPresentationControl.class).initialize();
      }
  }