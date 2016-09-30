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

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;

public interface IView {

  /**
   * Shows user error on SnackBar instance with
   * long duration
   *
   * @param errorString errorString to show
   */
  void showError(String errorString);

  /**
   * Shows user error on SnackBar instance with
   * long duration and button provided text also
   * callback can be provided for it's click
   *
   * @param errorString errorString to show
   * @param buttonViewText action to provide for user
   * @param callback callback to receive action
   */
  void showError(String errorString, Button buttonViewText, View.OnClickListener callback);

  /**
   * Returns string value from R.string.xxx
   *
   * @param stringId string id to look for
   * @return String value of id
   */
  String getStringResource(@IdRes int stringId);

  /**
   * Checks whether view is available or not
   *
   * @return true or false
   */
  boolean isAvailable();

  /**
   * Context from view
   * @return context instance
   */
  Context getContext();
}
