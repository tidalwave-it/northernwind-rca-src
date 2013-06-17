/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import it.tidalwave.util.As;
import it.tidalwave.util.Finder;
import it.tidalwave.util.RoleFactory;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.SimpleComposite;
import it.tidalwave.role.ui.PresentationModel;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public class PresentationModelUtil
  {
    @Nonnull
    public <T extends As & SimpleComposite<T>> PresentationModel createPresentationModel (
            final @Nonnull T datum,
            final @Nonnull RoleFactory<T> roleFactory,
            final @Nonnull Object ... roles)
      {
        return new DefaultPresentationModel(datum, roleFactory.createRoleFor(datum),
                new SimpleComposite<PresentationModel>()
          {
            @Override @Nonnull
            public Finder<PresentationModel> findChildren()
              {
                return new SimpleFinderSupport<PresentationModel>()
                  {
                    @Override
                    protected List<? extends PresentationModel> computeResults()
                      {
                        final List<PresentationModel> results = new ArrayList<>();

                        for (final T object : datum.findChildren().results())
                          {
                            results.add(createPresentationModel(object, roleFactory, roles));
                          }

                        return results;
                      }
                  };
              }
          });
      }
  }
