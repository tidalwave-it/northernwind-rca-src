/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2021 Tidalwave s.a.s. (http://tidalwave.it)
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
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.northernwind.rca.embeddedserver;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface EmbeddedServer
  {
    @Immutable @AllArgsConstructor // (access = AccessLevel.PRIVATE)
    @EqualsAndHashCode(exclude = "updateListener") @ToString
    public static class Document
      {
        public static interface UpdateListener
          {
            public void onUpdate (@Nonnull final String content);

            public static final UpdateListener VOID = content -> {};
          }

        @Getter @With @Nonnull
        private final String mimeType;

        @Getter @With @Nonnull
        private final String content;

        @Getter @With @Nonnull
        private final UpdateListener updateListener;

        public Document()
          {
            this("", "", UpdateListener.VOID);
          }

        public void update (@Nonnull final String content)
          {
            updateListener.onUpdate(content);
          }
      }

    public int getPort();

    @Nonnull
    public String putDocument (@Nonnull String path, @Nonnull Document document);
  }
