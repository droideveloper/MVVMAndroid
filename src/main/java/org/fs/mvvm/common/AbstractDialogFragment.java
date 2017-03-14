/*
 * MVVM Copyright (C) 2016 Fatih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fs.mvvm.common;

import android.support.v4.app.DialogFragment;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.fs.mvvm.data.AbstractViewModel;
import org.fs.mvvm.utils.Objects;

public abstract class AbstractDialogFragment <V extends AbstractViewModel<?>>
    extends DialogFragment {

  public AbstractDialogFragment(int style) {
    setStyle(style, getTheme());
  }

  public boolean isAvailable() {
    return !Objects.isNullOrEmpty(getActivity()) && isAdded();
  }

  public void finish() {
    throw new IllegalArgumentException("you can not finish, dialogFragment with finish, try use dismiss.");
  }

  protected void log(String msg) {
    log(Log.DEBUG, msg);
  }

  protected void log(Throwable error) {
    StringWriter strWriter = new StringWriter();
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    error.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  protected void log(int level, String msg) {
    if (isLogEnabled()) {
      Log.println(level, getClassTag(), msg);
    }
  }

  protected abstract boolean isLogEnabled();

  protected abstract String getClassTag();
}
