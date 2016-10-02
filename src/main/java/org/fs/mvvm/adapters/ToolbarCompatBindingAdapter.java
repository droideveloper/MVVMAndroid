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
package org.fs.mvvm.adapters;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import org.fs.mvvm.commands.ICommand;
import org.fs.mvvm.listeners.OnNavigated;
import org.fs.mvvm.utils.Objects;

public class ToolbarCompatBindingAdapter {

  private final static String ANDROID_TITLE_TEXT = "android:titleText";
  private final static String ANDROID_TITLE_TEXT_ATTR_CHANGED = "android:titleTextAttrChanged";

  private final static String ANDROID_SUB_TITLE_TEXT = "android:subTitleText";
  private final static String ANDROID_SUB_TITLE_TEXT_ATTR_CHANGED = "android:subTitleTextAttrChanged";

  private final static String ANDROID_NAVIGATION_COMMAND = "android:navigationCommand";
  private final static String ANDROID_NAVIGATION_COMMAND_PARAMETER = "android:navigationCommandParameter";
  private final static String ANDROID_ON_NAVIGATED = "android:onNavigated";

  private final static String ANDROID_NAVIGATION_ICON = "android:navIcon";

  private ToolbarCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  /**
   * Registers drawable icon for android
   *
   * @param viewToolbar viewToolbar to put navigation icon on
   * @param icon drawable icon
   */
  @BindingAdapter(
      ANDROID_NAVIGATION_ICON
  )
  public static void registerNavigationIcon(Toolbar viewToolbar, Drawable icon) {
    if (icon != null) {
      viewToolbar.setNavigationIcon(icon);
    }
  }

  /**
   * Setter of title on ToolbarView
   *
   * @param viewToolbar viewToolbar instance
   * @param titleText title
   * @param <T> type of title or null
   */
  @BindingAdapter(
      ANDROID_TITLE_TEXT
  )
  public static <T extends CharSequence> void setTitleText(Toolbar viewToolbar, T titleText) {
    if (!TextUtils.equals(viewToolbar.getTitle(), titleText)) {
      viewToolbar.setTitle(titleText);
    }
  }

  /**
   * Two-way of title on ToolbarView
   *
   * @param viewToolbar viewToolbar instance
   * @param <T> type of title
   * @return T type of title or null
   */
  @InverseBindingAdapter(
      attribute = ANDROID_TITLE_TEXT,
      event = ANDROID_TITLE_TEXT_ATTR_CHANGED
  )
  public static <T extends CharSequence> T provideTitleText(Toolbar viewToolbar) {
    return Objects.toObject(viewToolbar.getTitle());
  }

  /**
   * Setter of subTitle on ToolbarView
   *
   * @param viewToolbar viewToolbar instance
   * @param subTitleText subTitle
   * @param <T> type of subTitle or null
   */
  @BindingAdapter(
      ANDROID_SUB_TITLE_TEXT
  )
  public static <T extends CharSequence> void setSubTitleText(Toolbar viewToolbar, T subTitleText) {
    if (!TextUtils.equals(viewToolbar.getSubtitle(), subTitleText)) {
      viewToolbar.setSubtitle(subTitleText);
    }
  }

  /**
   * Two-way of subTitle on ToolbarView
   *
   * @param viewToolbar viewToolbar instance
   * @param <T> type of subTitle
   * @return T type of subTitle or null
   */
  @InverseBindingAdapter(
      attribute = ANDROID_SUB_TITLE_TEXT,
      event = ANDROID_SUB_TITLE_TEXT_ATTR_CHANGED
  )
  public static <T extends CharSequence> T provideSubTitleText(Toolbar viewToolbar) {
    return Objects.toObject(viewToolbar.getSubtitle());
  }

  /**
   * Registers command or listener or both or none
   * if both registered callback executed first then
   * command follows.
   *
   * @param viewToolbar viewToolbar instance
   * @param callback callback that will receive its click
   * @param param command parameter to pass on command
   * @param command command to execute if desired matches
   * @param <T> Type of commandParameter
   */
  @BindingAdapter(
      value = {
          ANDROID_ON_NAVIGATED,
          ANDROID_NAVIGATION_COMMAND_PARAMETER,
          ANDROID_NAVIGATION_COMMAND
      },
      requireAll = false
  )
  public static <T> void registerNavigationListener(Toolbar viewToolbar, OnNavigated callback, T param, ICommand<T> command) {
    if (callback == null && param == null && command == null) {
      viewToolbar.setNavigationOnClickListener(null);
    } else {
      viewToolbar.setNavigationOnClickListener(v -> {
        if (callback != null) {
          callback.onNavigated(v);
        }
        if (command != null) {
          if (command.canExecute(param)) {
            command.execute(param);
          }
        }
      });
    }
  }
}
