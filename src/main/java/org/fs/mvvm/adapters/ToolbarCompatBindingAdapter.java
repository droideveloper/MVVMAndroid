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
import android.databinding.InverseBindingListener;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.TextView;
import org.fs.mvvm.commands.ICommand;
import org.fs.mvvm.listeners.OnNavigated;
import org.fs.mvvm.listeners.SimpleTextWatcher;
import org.fs.mvvm.utils.Invokes;
import org.fs.mvvm.utils.Objects;

public class ToolbarCompatBindingAdapter {

  private final static String FIELD_TITLE_TEXTVIEW      = "mTitleTextView";
  private final static String FIELD_SUB_TITLE_TEXTVIEW  = "mSubtitleTextView";

  private final static String BIND_TITLE_TEXT = "bindings:titleText";
  private final static String BIND_TITLE_TEXT_ATTR_CHANGED = "bindings:titleTextAttrChanged";

  private final static String BIND_SUB_TITLE_TEXT = "bindings:subTitleText";
  private final static String BIND_SUB_TITLE_TEXT_ATTR_CHANGED = "bindings:subTitleTextAttrChanged";

  private final static String BIND_NAVIGATION_COMMAND = "bindings:navigationCommand";
  private final static String BIND_NAVIGATION_COMMAND_PARAMETER = "bindings:navigationCommandParameter";
  private final static String BIND_ON_NAVIGATED = "bindings:onNavigated";

  private final static String BIND_NAVIGATION_ICON_COMPAT = "bindings:navigationIconCompat";

  private final static String BIND_MENU = "bindings:toolbarMenu";
  private final static String BIND_MENU_CALLBACK = "bindings:toolbarMenuCallback";

  private ToolbarCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @BindingAdapter({ BIND_NAVIGATION_ICON_COMPAT })
  public static void viewToolbarRegisterNavigationIcon(Toolbar viewToolbar, Drawable icon) {
    if (icon != null) {
      viewToolbar.setNavigationIcon(icon);
    }
  }

  @BindingAdapter({ BIND_TITLE_TEXT })
  public static <T extends CharSequence> void viewToolbarRegisterTitle(Toolbar viewToolbar, T titleText) {
    if (!TextUtils.equals(viewToolbar.getTitle(), titleText)) {
      viewToolbar.setTitle(titleText);
    }
  }

  @InverseBindingAdapter(attribute = BIND_TITLE_TEXT,
      event = BIND_TITLE_TEXT_ATTR_CHANGED)
  public static <T extends CharSequence> T viewToolbarRetreiveTitle(Toolbar viewToolbar) {
    return Objects.toObject(viewToolbar.getTitle());
  }

  @BindingAdapter({ BIND_SUB_TITLE_TEXT })
  public static <T extends CharSequence> void viewToolbarRegisterSubTitle(Toolbar viewToolbar, T subTitleText) {
    if (!TextUtils.equals(viewToolbar.getSubtitle(), subTitleText)) {
      viewToolbar.setSubtitle(subTitleText);
    }
  }

  @InverseBindingAdapter(attribute = BIND_SUB_TITLE_TEXT,
      event = BIND_SUB_TITLE_TEXT_ATTR_CHANGED)
  public static <T extends CharSequence> T viewToolbarRetreiveSubTitle(Toolbar viewToolbar) {
    return Objects.toObject(viewToolbar.getSubtitle());
  }

  @BindingAdapter({ BIND_MENU, BIND_MENU_CALLBACK })
  public static void viewToolbarRegisterMenuAndMenuCallback(Toolbar viewToolbar, int menu, Toolbar.OnMenuItemClickListener callback) {
    if (menu >= 0 && callback != null) {
      viewToolbar.setOnMenuItemClickListener(callback);
      viewToolbar.inflateMenu(menu);
    }
  }

  @BindingAdapter(
      value = {
          BIND_ON_NAVIGATED,
          BIND_NAVIGATION_COMMAND_PARAMETER,
          BIND_NAVIGATION_COMMAND,
          BIND_SUB_TITLE_TEXT_ATTR_CHANGED,
          BIND_TITLE_TEXT_ATTR_CHANGED
      },
      requireAll = false
  )
  public static <T> void viewToolbarRegisterNavigationListener(Toolbar viewToolbar, OnNavigated callback, T param, ICommand<T> command,
      InverseBindingListener subTitleAttrChanged, InverseBindingListener titleAttrChanged) {
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
    if (subTitleAttrChanged != null || titleAttrChanged != null) {
      if (subTitleAttrChanged != null) {
        TextView subTitleTextView = Invokes.getFieldValue(Invokes.findFieldByName(FIELD_SUB_TITLE_TEXTVIEW, Toolbar.class), viewToolbar);
        if (subTitleTextView != null) {
          subTitleTextView.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void afterTextChanged(Editable s) {
              subTitleAttrChanged.onChange();
            }
          });
        }
      }
      if (titleAttrChanged != null) {
        TextView titleTextView = Invokes.getFieldValue(Invokes.findFieldByName(FIELD_TITLE_TEXTVIEW, Toolbar.class), viewToolbar);
        if (titleTextView != null) {
          titleTextView.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void afterTextChanged(Editable s) {
              titleAttrChanged.onChange();
            }
          });
        }
      }
    }
  }
}
