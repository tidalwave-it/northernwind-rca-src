/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.role.ui.javafx.impl.filechooser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.io.File;
import java.nio.file.Path;
import it.tidalwave.util.ui.UserNotificationWithFeedback;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.role.ui.javafx.impl.DelegateSupport;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class FileChooserBindings extends DelegateSupport
  {
    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public FileChooserBindings (final @Nonnull Executor executor)
      {
        super(executor);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    public void openFileChooserFor (final @Nonnull UserNotificationWithFeedback notification,
                                    final @Nonnull BoundProperty<Path> selectedFile)
      {
        log.debug("openFileChooserFor({}, {})", notification, selectedFile);
        assertIsFxApplicationThread();

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(notification.getCaption());
        fileChooser.setInitialDirectory(selectedFile.get().toFile());

        // It seems we need to take care of modality: https://javafx-jira.kenai.com/browse/RT-13949
        final File file = runWhileDisabling(mainWindow, new Callable<File>()
          {
            @Override
            public File call()
              {
                return fileChooser.showOpenDialog(mainWindow);
              }
          });

        notifyFile(file, notification, selectedFile);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    public void openDirectoryChooserFor (final @Nonnull UserNotificationWithFeedback notification,
                                         final @Nonnull BoundProperty<Path> selectedFolder)
      {
        log.debug("openDirectoryChooserFor({}, {})", notification, selectedFolder);
        assertIsFxApplicationThread();

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(notification.getCaption());
        directoryChooser.setInitialDirectory(selectedFolder.get().toFile());

        // It seems we need to take care of modality: https://javafx-jira.kenai.com/browse/RT-13949
        final File file = runWhileDisabling(mainWindow, new Callable<File>()
          {
            @Override
            public File call()
              {
                return directoryChooser.showDialog(mainWindow);
              }
          });

        notifyFile(file, notification, selectedFolder);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void notifyFile (final @Nullable File file,
                             final @Nonnull UserNotificationWithFeedback notification,
                             final @Nonnull BoundProperty<Path> selectedFile)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                try
                  {
                    if (file == null)
                      {
                        notification.cancel();
                      }
                    else
                      {
                        selectedFile.set(file.toPath());
                        notification.confirm();
                      }
                  }
                catch (Exception e)
                  {
                    log.warn("", e);
                  }
              }
          });
      }
  }
