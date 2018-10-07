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

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.fs.mvvm.data.AbstractViewModel;

public abstract class AbstractActivity<V extends AbstractViewModel<?>> extends
    AppCompatActivity {

  public void showProgress() {
    throw new RuntimeException("You should implement this method and not call super");
  }

  public void hideProgress() {
    throw new RuntimeException("You should implement this method and not call super");
  }

  public void showError(String errorString) {
    final View view = getView();
    if(view != null) {
      Snackbar.make(view, errorString, Snackbar.LENGTH_LONG)
          .show();
    }
  }

  public void showError(String errorString, String actionTextString, final View.OnClickListener callback) {
    final View view = getView();
    if(view != null) {
      final Snackbar snackbar = Snackbar.make(view, errorString, Snackbar.LENGTH_LONG);
      snackbar.setAction(actionTextString, v -> {
        if (callback != null) {
          callback.onClick(v);
        }
        snackbar.dismiss();
      });
      snackbar.show();
    }
  }

  public String getStringRes(@StringRes int stringId, Object... args) {
    return getString(stringId, args);
  }

  public Context getContext() {
    return this;
  }

  public boolean isAvailable() {
    return !isFinishing();
  }

  @Nullable protected View getView() {
    return findViewById(android.R.id.content);
  }

  protected abstract String getClassTag();
  protected abstract boolean isLogEnabled();

  protected void log(final String str) {
    log(Log.DEBUG, str);
  }

  protected void log(Throwable error) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter  printWriter  = new PrintWriter(stringWriter);
    error.printStackTrace(printWriter);
    log(Log.ERROR, stringWriter.toString());
  }

  protected void log(final int lv, final String str) {
    if(isLogEnabled()) {
      Log.println(lv, getClassTag(), str);
    }
  }
}
