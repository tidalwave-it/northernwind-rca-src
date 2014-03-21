/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - hg clone https://bitbucket.org/tidalwave/northernwind-src
 * %%
 * Copyright (C) 2013 - 2014 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.northernwind.rca.embeddedserver;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface EmbeddedServer
  {
    @Immutable @AllArgsConstructor(access = AccessLevel.PRIVATE) @NoArgsConstructor
    @EqualsAndHashCode(exclude = "updateListener") @ToString
    public static class Document
      {
        public static interface UpdateListener
          {
            public void onUpdate (final @Nonnull String content);

            public static final UpdateListener VOID = new UpdateListener()
              {
                @Override
                public void onUpdate (final @Nonnull String content)
                  {
                  }
              };
          }

        @Getter @Wither @Nonnull
        private String mimeType = "";

        @Getter @Wither @Nonnull
        private String content = "";

        @Getter @Wither @Nonnull
        private UpdateListener updateListener = UpdateListener.VOID;

        public void update (final @Nonnull String content)
          {
            updateListener.onUpdate(content);
          }
      }

    public int getPort();

    @Nonnull
    public String putDocument (@Nonnull String path, @Nonnull Document document);
  }
