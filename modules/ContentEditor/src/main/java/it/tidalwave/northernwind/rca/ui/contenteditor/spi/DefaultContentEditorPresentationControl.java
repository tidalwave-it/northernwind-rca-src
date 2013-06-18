/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
 * %%
 * Copyright (C) 2011 - 2013 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * *********************************************************************************************************************
 *
 * $Id$
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.ui.contenteditor.spi;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Configurable;
import it.tidalwave.util.Id;
import it.tidalwave.util.Key;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.role.spi.DefaultSimpleComposite;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.RowHashMap;
import it.tidalwave.messagebus.MessageBus;
import it.tidalwave.messagebus.MessageBus.Listener;
import it.tidalwave.northernwind.core.model.Content;
import it.tidalwave.northernwind.core.model.ResourceProperties;
import it.tidalwave.northernwind.rca.ui.DefaultPresentationModel;
import it.tidalwave.northernwind.rca.ui.event.ContentSelectedEvent;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentation;
import it.tidalwave.northernwind.rca.ui.contenteditor.ContentEditorPresentationControl;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Configurable @Slf4j
public class DefaultContentEditorPresentationControl implements ContentEditorPresentationControl
  {
    @Inject @Named("applicationMessageBus") @Nonnull
    private MessageBus messageBus;

    @Nonnull
    private ContentEditorPresentation presentation;

    public static final Key<String> PROPERTY_FULL_TEXT = new Key<>("fullText"); // FIXME copied
    public static final Key<String> PROPERTY_TITLE = new Key<>("title"); // FIXME copied

    /*******************************************************************************************************************
     *
     * TODO: refactor with @ListensTo
     *
     ******************************************************************************************************************/
    private final Listener<ContentSelectedEvent> siteNodeSelectionListener =
            new Listener<ContentSelectedEvent>()
      {
        @Override
        public void notify (final @Nonnull ContentSelectedEvent event)
          {
            log.debug("notify({})", event);
            final Content content = event.getContent();
            final ResourceProperties properties = content.getProperties();
            log.info("PROPERTIES {}", properties);

            try
              {
                presentation.populate(properties.getProperty(PROPERTY_FULL_TEXT, ""));
              }
            catch (IOException e)
              {
                presentation.populate(e.toString());
                log.warn("", e);
              }

            try
              {
                presentation.populateTitle(properties.getProperty(PROPERTY_TITLE, ""));
              }
            catch (IOException e)
              {
                presentation.populateTitle(e.toString());
                log.warn("", e);
              }

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

            presentation.populateProperties(pmProperties);
            presentation.showUp();
          }
      };

    @PostConstruct
    private void initialize()
      {
        messageBus.subscribe(ContentSelectedEvent.class, siteNodeSelectionListener);
      }

    @PreDestroy
    private void destroy()
      {
        messageBus.unsubscribe(siteNodeSelectionListener);
      }

    @Override
    public void initialize (final @Nonnull ContentEditorPresentation presentation)
      {
        this.presentation = presentation;
      }
  }
