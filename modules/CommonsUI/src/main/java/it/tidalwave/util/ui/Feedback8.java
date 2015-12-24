/*
 * #%L
 * *********************************************************************************************************************
 *
 * NorthernWind - lightweight CMS
 * http://northernwind.tidalwave.it - git clone git@bitbucket.org:tidalwave/northernwind-rca-src.git
 * %%
 * Copyright (C) 2013 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
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
package it.tidalwave.util.ui;

import javax.annotation.Nonnull;
import it.tidalwave.util.Callback;
import it.tidalwave.util.ui.UserNotificationWithFeedback.Feedback;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Wither;
import static lombok.AccessLevel.PRIVATE;
import org.openide.util.Exceptions;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id$
 *
 **********************************************************************************************************************/
@AllArgsConstructor(access = PRIVATE)
public class Feedback8 extends Feedback
  {
    @Wither
    private final Callback onConfirm;

    @Wither
    private final Callback onCancel;

    @Nonnull
    public static Feedback8 feedback()
      {
        return new Feedback8(Callback.EMPTY, Callback.EMPTY);
      }

    @Override
//    @SneakyThrows(Throwable.class)
    public final void onConfirm()
      throws Exception
      {
        try
          {
            onConfirm.call();
          }
        catch (Throwable ex)
          {
            Exceptions.printStackTrace(ex);
          }
      }

    @Override
//    @SneakyThrows(Throwable.class)
    public final void onCancel()
      throws Exception
      {
        try
          {
            onCancel.call();
          }
        catch (Throwable ex)
          {
            Exceptions.printStackTrace(ex);
          }
      }
  }
