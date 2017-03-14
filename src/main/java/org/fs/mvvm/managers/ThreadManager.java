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
package org.fs.mvvm.managers;

import android.os.Handler;
import android.os.Looper;
import org.fs.mvvm.utils.Objects;
import org.fs.mvvm.utils.Preconditions;

public final class ThreadManager {

  private final static Handler uiThread = new Handler(Looper.getMainLooper());

  private final static long DEFAULT_DELAY = 300L;

  private ThreadManager() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  public static void runOnUiThread(Runnable action) {
    Preconditions.checkNotNull(action, "action is null");
    if (!Objects.isNullOrEmpty(uiThread)) {
      uiThread.post(action);
    }
  }

  public static void runOnUiThreadDelayed(Runnable action) {
    runOnUiThreadDelayed(action, DEFAULT_DELAY);
  }

  public static void runOnUiThreadDelayed(Runnable action, long delay) {
    Preconditions.checkNotNull(action, "action is null");
    Preconditions.checkConditionMeet(delay > 0L, "delay must be positive");
    if (!Objects.isNullOrEmpty(uiThread)) {
      uiThread.postDelayed(action, delay);
    }
  }

  public static void clearAll() {
    if (!Objects.isNullOrEmpty(uiThread)) {
      uiThread.removeCallbacksAndMessages(null);
    }
  }
}
