/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.contenteditor.impl;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.ClassPathResource;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;
import it.tidalwave.northernwind.rca.ui.contenteditor.spi.PropertyBinder;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datumType = ResourceProperties.class)
@Configurable @RequiredArgsConstructor
public class ResourcePropertiesBinder implements PropertyBinder
  {
    @Nonnull
    private final ResourceProperties properties;

    @VisibleForTesting final static String EDITOR_PROLOG =
            "it/tidalwave/northernwind/rca/ui/contenteditor/spi/EditorProlog.txt";

    @VisibleForTesting final static String EDITOR_EPILOG =
            "it/tidalwave/northernwind/rca/ui/contenteditor/spi/EditorEpilog.txt";

    @VisibleForTesting String editorProlog = "";

    @VisibleForTesting String editorEpilog = "";

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
      {
        editorProlog = loadResource(EDITOR_PROLOG);
        editorEpilog = loadResource(EDITOR_EPILOG);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public <T> void bind (final @Nonnull Key<T> propertyName,
                          final @Nonnull BoundProperty<T> boundProperty,
                          final @Nonnull UpdateCallback callback)
      throws NotFoundException, IOException
      {
        boundProperty.set(properties.getProperty(propertyName));
        boundProperty.addPropertyChangeListener(new PropertyChangeListener()
          {
            @Override
            public void propertyChange (final @Nonnull PropertyChangeEvent event)
              {
                callback.notify(properties.withProperty(propertyName, boundProperty.get()));
              }
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    public EmbeddedServer.Document createBoundDocument (final @Nonnull Key<String> propertyName,
                                                        final @Nonnull UpdateCallback callback)
      {
        try
          {
            final String text = properties.getProperty(propertyName, "");
            final HtmlDocument originalDocument = HtmlDocument.createFromText(text);
            final HtmlDocument editableDocument = originalDocument.withProlog(editorProlog)
                                                                  .withEpilog(editorEpilog);
            // FIXME: mime type - XHTML?
            return new EmbeddedServer.Document().withMimeType("text/html")
                                                .withContent(editableDocument.asString())
                                                .withUpdateListener(new EmbeddedServer.Document.UpdateListener()
              {
                @Override
                public void onUpdate (final @Nonnull String text)
                  {
                    callback.notify(properties.withProperty(propertyName, originalDocument.withBody(text).asString()));
                  }
              });
          }
        catch (IOException e)
          {
            throw new RuntimeException(e);
          }
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    @VisibleForTesting String loadResource (final @Nonnull String path)
      {
        try
          {
            final @Cleanup Reader r = new InputStreamReader(new ClassPathResource(path).getInputStream(), "UTF-8");
            return CharStreams.toString(r);
          }
        catch (IOException e)
          {
            throw new RuntimeException(e);
          }
      }
  }
