/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class JavaFXPageEditorPresentation implements ContentEditorPresentation
  {
    @Nonnull
    private final WebView wpHtmlEditor;

    @Override
    public void populate (final @Nonnull String text)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                wpHtmlEditor.getEngine().loadContent(text);
              }
          });
      }
  }
