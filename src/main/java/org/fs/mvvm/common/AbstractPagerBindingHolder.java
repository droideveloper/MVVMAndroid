/*
 * MVVM Copyright (C) 2017 Fatih.
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

import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;

public abstract class AbstractPagerBindingHolder<D extends BaseObservable> {

  private final WeakReference<View> viewReference;
  private final ViewDataBinding viewDataBinding;

  private D data;
  private int position;

  public AbstractPagerBindingHolder(ViewDataBinding viewDataBinding, View view) {
    this.viewDataBinding = viewDataBinding;
    this.viewReference = view != null ? new WeakReference<>(view) : null;
  }

  public final View getView() {
    return viewReference != null ? viewReference.get() : null;
  }

  public final D getData() {
    return data;
  }

  public final void setDataAndSync(int variableId, D data, int position) {
    this.position = position;
    this.data = data;
    viewDataBinding.setVariable(variableId, data);
    viewDataBinding.executePendingBindings();
  }

  protected void log(String msg) {
    log(Log.DEBUG, msg);
  }

  protected void log(Throwable exp) {
    StringWriter strWriter = new StringWriter(128);
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    exp.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  protected void log(int lv, String msg) {
    if (isLogEnabled()) {
      Log.println(lv, getClassTag(), msg);
    }
  }

  protected abstract boolean isLogEnabled();

  protected abstract String getClassTag();
}
