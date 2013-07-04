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
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.rca.embeddedserver.EmbeddedServer;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public class MockDocumentProxyFactory implements DocumentProxyFactory
  {
    @Override @Nonnull
    public EmbeddedServer.Document createDocumentProxy (final @Nonnull Content content,
                                                        final @Nonnull Key<String> propertyName)
      {
        try
          {
            return new EmbeddedServer.Document().withMimeType("text/html")
                                                .withContent("proxy for: " + content.getProperties().getProperty(propertyName, ""));
          }
        catch (IOException e)
          {
            throw new RuntimeException(e); // never occurs
          }
      }
  }