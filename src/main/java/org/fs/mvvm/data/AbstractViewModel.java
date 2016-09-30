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
package org.fs.mvvm.data;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.fs.mvvm.utils.Preconditions;

//TODO create instance of this class via Dagger2 for better understanding and
//TODO if you want to use how awesome it is then go ahead execute it
//TODO when you done this that way you will be understand it through the scope of dependency injection
public abstract class AbstractViewModel<V extends IView> extends BaseObservable {

  protected final V view;

  /**
   * View interface for accessing its public properties
   *
   * @param view view interface extension of IView.
   */
  public AbstractViewModel(V view) {
    Preconditions.checkNotNull(view, "view is null");
    this.view = view;
  }

  /**
   * View's resume lifecycle
   */
  public void onResume()  {}

  /**
   * View's pause lifecycle
   */
  public void onPause()   {}

  /**
   * View's start lifecycle
   */
  public void onStart()   {}

  /**
   * View's stop lifecycle
   */
  public void onStop()    {}

  /**
   * View's create lifecycle
   */
  public void onCreate()  {}

  /**
   * View's destroy lifecycle
   */
  public void onDestroy() {}

  /**
   * View's restoreState lifecycle
   *
   * @param restoreState previous state of view.
   */
  public void restoreState(Bundle restoreState) {}

  /**
   * View's storeState lifecycle
   *
   * @param storeState current state of view.
   */
  public void storeState(Bundle storeState)     {}

  /**
   * View's activityResult lifecycle
   *
   * @param requestCode requestCode of call for result
   * @param resultCode responseCode of called for result
   * @param data data of called for result
   */
  public void activityResult(int requestCode, int resultCode, Intent data) {}

  /**
   * View's requestPermissionResult lifecycle for API M and above
   *
   * @param requestCode requestCode of call for result
   * @param permissions permissions of called for result
   * @param grants grants of called for result
   */
  public void requestPermissionResult(int requestCode, String[] permissions, int[] grants) {}

  /**
   * View's optionsItemSelected lifecycle callbacks
   *
   * @param item item that its actions destroyed
   * @return true if handled false otherwise
   */
  public boolean onOptionsItemSelected(MenuItem item) { return false; }

  /**
   * Log string message with Debug level.
   *
   * @param msg a string for log.
   */
  protected void log(String msg) {
    log(Log.DEBUG, msg);
  }

  /**
   * Log error or exception with Error level using its stackTrace as message.
   *
   * @param error an exception for log.
   */
  protected void log(Throwable error) {
    StringWriter strWriter = new StringWriter();
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    error.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  /**
   * End level of printing logs on android monitor.
   *
   * @param level a level of log.
   * @param msg a string message for log.
   */
  protected void log(int level, String msg) {
    if (isLogEnabled()) {
      Log.println(level, getClassTag(), msg);
    }
  }

  /**
   * Logging enabled for this class or not.
   *
   * @return a boolean.
   */
  protected abstract boolean isLogEnabled();

  /**
   * Tag for this class in order to use it in logging
   *
   * @return a string.
   */
  protected abstract String getClassTag();
}
