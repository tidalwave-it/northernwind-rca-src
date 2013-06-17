/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.application.Platform;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class JavaFXStructureEditorPresentation implements StructureEditorPresentation
  {
    @Nonnull
    private final Pane myContainer;

    @Nonnull
    private final Pane otherContainer;

    @Nonnull
    private final WebView webView;

    @Override
    public void showUp()
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                log.info("setting visible: {} setting not visible: {}", myContainer, otherContainer);
                otherContainer.setVisible(false);
                myContainer.setVisible(true);
              }
          });
      }

    @Override
    public void populate (final @Nonnull String text)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                webView.getEngine().loadContent(text);
              }
          });
      }
  }
