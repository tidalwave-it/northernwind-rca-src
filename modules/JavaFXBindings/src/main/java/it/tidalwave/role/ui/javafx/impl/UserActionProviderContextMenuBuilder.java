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
package it.tidalwave.role.ui.javafx.impl;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import it.tidalwave.util.As;
import it.tidalwave.util.AsException;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.UserActionProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import static it.tidalwave.role.Displayable.Displayable;
import static it.tidalwave.role.ui.UserActionProvider.UserActionProvider;

/***********************************************************************************************************************
 *
 * An implementation of {@link ContextMenuBuilder} that extracts information from a {@link UserActionProvider}.
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class UserActionProviderContextMenuBuilder implements ContextMenuBuilder
  {
    @Nonnull
    private final ExecutorService executorService;

    public UserActionProviderContextMenuBuilder()
      {
        this(Executors.newSingleThreadExecutor());
      }

    @Override @CheckForNull
    public ContextMenu createContextMenu (final @Nonnull As asObject)
      {
        final List<MenuItem> menuItems = createMenuItems(asObject);
        return (menuItems == null) ? null : new ContextMenu(menuItems.toArray(new MenuItem[0]));
      }

    @CheckForNull
    @VisibleForTesting List<MenuItem> createMenuItems (final @Nonnull As asObject)
      {
        try
          {
            final Collection<? extends UserAction> actions = asObject.as(UserActionProvider).getActions();
            final List<MenuItem> menuItems = new ArrayList<>();

            for (final UserAction action : actions)
              {
                final EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>()
                  {
                    @Override
                    public void handle (final @Nonnull ActionEvent event)
                      {
                        executorService.submit(new Runnable()
                          {
                            @Override
                            public void run()
                              {
                                action.actionPerformed();
                              }
                          });
                      }
                  };

                 menuItems.add(MenuItemBuilder.create().text(action.as(Displayable).getDisplayName())
                                                       .onAction(eventHandler)
                                                       .build());
              }

            return menuItems;
          }
        catch (AsException e)
          {
            return null; // ok, no context actions
          }
      }
  }
