/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.role.ui.spi;

import javax.annotation.Nonnull;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.PresentationModelProvider;
import it.tidalwave.northernwind.model.impl.admin.role.DefaultPresentationModel;
import lombok.RequiredArgsConstructor;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class SimplePresentationModelProvider implements PresentationModelProvider
  {
    @Nonnull
    private final Object owner;

    @Override @Nonnull
    public PresentationModel createPresentationModel (final @Nonnull Object... localRoles)
      {
        return new DefaultPresentationModel(owner, localRoles);
      }
  }