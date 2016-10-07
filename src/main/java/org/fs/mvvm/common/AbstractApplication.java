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

import android.app.Application;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class AbstractApplication extends Application{

  private static boolean debug;

  public AbstractApplication(boolean debug) {
    AbstractApplication.debug = debug;
  }

  /**
   * Log or not
   *
   * @return true or false, a boolean.
   */
  public static boolean isDebug() {
    return debug;
  }

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
