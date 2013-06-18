/*
 * #%L
 * %%
 * %%
 * #L%
 */

package it.tidalwave.northernwind.rca.ui.impl;

import it.tidalwave.role.ui.PresentationModelProvider;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.IOException;
import it.tidalwave.util.Id;
import it.tidalwave.util.Key;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.spi.DefaultSimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.RowHashMap;
import it.tidalwave.dci.annotation.DciRole;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.ui.DefaultPresentationModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * A provider of a {@link PresentationModel} for {@link ResourceProperties}.
 *
 * @stereotype Role
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@DciRole(datum = ResourceProperties.class) @RequiredArgsConstructor @Slf4j
public class ResourcePropertiesPresentationModelProvider implements PresentationModelProvider
  {
    @Nonnull
    private final ResourceProperties properties;

    @Override @Nonnull
    public PresentationModel createPresentationModel()
      {
        final PresentationModel pmProperties = new DefaultPresentationModel(properties,
                new DefaultSimpleComposite<>(new SimpleFinderSupport<PresentationModel>()
          {
            @Override
            protected List<? extends PresentationModel> computeResults()
              {
                final List<PresentationModel> results = new ArrayList<>();
                final Collection<Id> groupIds = properties.getGroupIds();
                groupIds.add(new Id(""));

                for (final Id groupId : groupIds)
                  {
                    final ResourceProperties p2 = groupId.equals(new Id("")) ? properties : properties.getGroup(groupId);

                    for (final Key<?> key : p2.getKeys())
                      {
                        try
                          {
                            results.add(new DefaultPresentationModel(properties,
                                        RowHashMap.create().withColumn("name", key.stringValue())
                                                           .withColumn("value", p2.getProperty(key, null))));
                          }
                        catch (IOException e)
                          {
                            log.warn("", e);
                          }
                      }
                   }

                 return results;
               }
          }));

        return pmProperties;
      }
  }
