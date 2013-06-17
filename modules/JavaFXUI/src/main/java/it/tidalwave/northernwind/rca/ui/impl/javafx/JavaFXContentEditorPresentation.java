/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import javafx.scene.web.WebView;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class JavaFXContentEditorPresentation implements ContentEditorPresentation
  {
    @Nonnull
    private final Pane myContainer;

    @Nonnull
    private final Pane otherContainer;

    @Nonnull
    private final WebView webView;

    @Nonnull
    private final TextField contentTitle;

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

    @Override
    public void populateTitle (final @Nonnull String title)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                contentTitle.setText(title);
              }
          });
      }
  }
