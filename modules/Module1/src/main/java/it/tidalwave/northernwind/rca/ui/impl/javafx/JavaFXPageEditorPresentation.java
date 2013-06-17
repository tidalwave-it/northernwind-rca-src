/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import it.tidalwave.northernwind.core.model.ResourceFile;
import it.tidalwave.northernwind.rca.ui.PageEditorPresentation;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class JavaFXPageEditorPresentation implements PageEditorPresentation
  {
    @Nonnull
    private final WebView wpHtmlEditor;

    @Override
    public void open (final @Nonnull ResourceFile file)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                try
                  {
                    wpHtmlEditor.getEngine().loadContent(file.asText("UTF-8"));
                  }
                catch (IOException e)
                  {
                    e.printStackTrace();
                  }
              }
          });
      }
  }
