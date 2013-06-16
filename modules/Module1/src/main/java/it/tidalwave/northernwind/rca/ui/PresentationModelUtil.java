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
import it.tidalwave.util.Finder;
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
    public <T> PresentationModel createPresentationModel (final @Nonnull SimpleComposite<T> composite)
      {
        return new DefaultPresentationModel("",  new SimpleComposite<PresentationModel>()
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

                        for (final T object : composite.findChildren().results())
                          {
                            results.add(new DefaultPresentationModel(object));
                          }

                        return results;
                      }
                  };
              }
          });
      }
  }
