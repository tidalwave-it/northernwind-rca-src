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
package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.io.File;
import it.tidalwave.util.Callback;
import lombok.AllArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
@AllArgsConstructor @Slf4j
public class ProcessExecutor
  {
    @Nonnull
    private final String executable;

    @With
    private final List<String> arguments;

    @With
    private final Callback postMortemTask;

    public ProcessExecutor()
      {
        this("", new ArrayList<>(), Callback.EMPTY);
      }

    @Nonnull
    public ProcessExecutor withArguments2 (@Nonnull final String ... arguments)
      {
        return withArguments(Arrays.asList(arguments));
      }

    @Nonnull
    public static ProcessExecutor forExecutable (@Nonnull final String executable)
      {
        return new ProcessExecutor(executable, new ArrayList<>(), Callback.EMPTY);
      }

    public void execute()
      {
        final List<String> args = new ArrayList<>();
        args.add("open");
        args.add("-W");
        args.add("-a");

        final String[] paths =
          {
            "/Applications/", System.getProperty("user.home") + "/Applications/"
          };

        for (final String path : paths)
          {
            final File file = new File(new File(path), executable);

            if (file.exists())
              {
                args.add(file.getAbsolutePath());
                break;
              }
          }

        args.addAll(arguments);

        try
          {
            log.info("Executing {}", args);
            final Process process = Runtime.getRuntime().exec(args.toArray(new String[0]));
            // FIXME; inject the executor
            Executors.newSingleThreadExecutor().submit(() ->
              {
                try
                  {
                    log.debug(">>>> waiting for the process to terminate...");
                    process.waitFor();
                    log.debug(">>>> process terminated");
                    postMortemTask.call();
                  }
                catch (Throwable e)
                  {
                    log.error("", e);
                  }
              });
          }
        catch (IOException e)
          {
            log.error("", e); // shouldn't happen
          }
      }
  }
