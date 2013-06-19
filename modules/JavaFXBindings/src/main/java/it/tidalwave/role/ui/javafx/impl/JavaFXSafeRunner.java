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
import javafx.application.Platform;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j @NoArgsConstructor(access=AccessLevel.PRIVATE)
public final class JavaFXSafeRunner
  {
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public static void runSafely (final @Nonnull Runnable runnable)
      {
        final Runnable guardedRunnable = new Runnable()
          {
            @Override
            public void run()
              {
                try
                  {
                    runnable.run();
                  }
                catch (Throwable t)
                  {
                    log.warn("", t);
                  }
              }
          };

        if (Platform.isFxApplicationThread())
          {
            guardedRunnable.run();
          }
        else
          {
            Platform.runLater(guardedRunnable);
          }
      }
  }
