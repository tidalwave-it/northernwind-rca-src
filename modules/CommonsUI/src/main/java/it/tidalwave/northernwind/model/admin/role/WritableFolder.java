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
package it.tidalwave.northernwind.model.admin.role;

import javax.annotation.Nonnull;
import java.io.IOException;
import it.tidalwave.role.io.Marshallable;

/***********************************************************************************************************************
 *
 * A role for folders that can be written into.
 *
 * @stereotype role
 *
 * @author  Fabrizio Giudici
 *
 **********************************************************************************************************************/
public interface WritableFolder
  {
    public static final Class<WritableFolder> _WritableFolder_ = WritableFolder.class;

    /*******************************************************************************************************************
     *
     * Writes a text into the specified file.
     *
     * @param  fileName     the name of the file inside the folder to write to
     * @param  text         the text to write
     * @throws IOException  in case of I/O error
     *
     ******************************************************************************************************************/
    public void write (@Nonnull String fileName, @Nonnull String text)
      throws IOException;

    /*******************************************************************************************************************
     *
     * Writes a marshallable object into the specified file.
     *
     * @param  fileName     the name of the file inside the folder to write to
     * @param  marshallable the object to write
     * @throws IOException  in case of I/O error
     *
     ******************************************************************************************************************/
    public void write (@Nonnull String fileName, @Nonnull Marshallable marshallable)
      throws IOException;
  }
