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

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class JavaFXApplicationWithSplash extends Application
  {
    private Splash splash = new Splash(this);

    @Override
    public void init()
      {
        log.info("init()");
        splash.init();
      }

    @Override
    public void start (final @Nonnull Stage stage)
      throws Exception
      {
        log.info("start({})", stage);
        splash.show();

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable()
          {
            @Override
            public void run()
              {
                initializeInBackground();
                Platform.runLater(new Runnable()
                  {
                    @Override
                    public void run()
                      {
                        try
                          {
                            final Parent application = createParent();
                            final Scene scene = new Scene(application);
                            stage.setScene(scene);
                            stage.show();
                            splash.dismiss();
                          }
                        catch (IOException e)
                          {
                            log.error("", e);
                          }
                      }
                  });
              }
          });
      }

    @Nonnull
    protected abstract Parent createParent()
      throws IOException;

    protected abstract void initializeInBackground();
  }
