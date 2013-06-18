/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.impl.javafx;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.javafx.RowAdapter;
import it.tidalwave.role.ui.javafx.JavaFXBindings;
import it.tidalwave.northernwind.rca.ui.structureeditor.StructureEditorPresentation;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class JavaFXStructureEditorPresentation implements StructureEditorPresentation
  {
    @Inject @Nonnull
    private JavaFXBindings bindings;

    @Nonnull
    private final Pane myContainer;

    @Nonnull
    private final Pane otherContainer;

    @Nonnull
    private final WebView webView;

    @Nonnull
    private final TableView<PresentationModel> tableView;

    private final TableColumn<PresentationModel, String> nameColumn = new TableColumn<>("Name");

    private final TableColumn<PresentationModel, String> valueColumn = new TableColumn<>("Value");

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public JavaFXStructureEditorPresentation (final @Nonnull Pane myContainer,
                                              final @Nonnull Pane otherContainer,
                                              final @Nonnull WebView webView,
                                              final @Nonnull TableView<PresentationModel> tableView)
      {
        this.myContainer = myContainer;
        this.otherContainer = otherContainer;
        this.webView = webView;
        this.tableView = tableView;

        nameColumn.setId("name");
        valueColumn.setId("value");
        nameColumn.setCellValueFactory(new RowAdapter<String>());
        valueColumn.setCellValueFactory(new RowAdapter<String>());
        tableView.getColumns().setAll(nameColumn, valueColumn);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
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

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
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

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void populateProperties (final @Nonnull PresentationModel pm)
      {
        Platform.runLater(new Runnable()
          {
            @Override
            public void run()
              {
                bindings.bind(tableView, pm);
              }
          });
      }
  }
