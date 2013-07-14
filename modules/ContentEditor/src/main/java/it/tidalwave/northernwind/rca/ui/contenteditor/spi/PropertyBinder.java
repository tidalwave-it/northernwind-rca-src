/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.contenteditor.spi;

import javax.annotation.Nonnull;
import java.io.IOException;
import it.tidalwave.util.Key;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.role.ui.BoundProperty;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer.Document;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface PropertyBinder
  {
    public static interface UpdateCallback
      {
        public void notify (@Nonnull ResourceProperties properties);
      }

    public static final Class<PropertyBinder> PropertyBinder = PropertyBinder.class;

    public <T> void bind (@Nonnull Key<T> property,
                          @Nonnull BoundProperty<T> boundProperty,
                          @Nonnull UpdateCallback callback)
      throws NotFoundException, IOException;

    @Nonnull
    public Document createBoundDocument (@Nonnull Key<String> property,
                                         @Nonnull UpdateCallback callback);
  }
