/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.event;

import javax.annotation.Nonnull;
import it.tidalwave.northernwind.core.model.ResourceFile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Getter @ToString
public class ContentSelectedEvent
  {
    @Nonnull
    private final ResourceFile file;
  }