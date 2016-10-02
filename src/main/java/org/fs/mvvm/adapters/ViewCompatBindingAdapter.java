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
import android.databinding.adapters.ListenerUtil;
import android.view.View;
import org.fs.mvvm.R;
import org.fs.mvvm.commands.ICommand;
import org.fs.mvvm.data.IFindAncestor;
import org.fs.mvvm.data.PropertyGetAndSet;
import org.fs.mvvm.utils.Objects;

public final class ViewCompatBindingAdapter {

  private final static String ANDROID_COMMAND = "android:command";
  private final static String ANDROID_COMMAND_PARAMETER = "android:commandParameter";

  private final static String ANDROID_FIND_ANCESTOR = "android:findAncestor";
  private final static String ANDROID_PROPERTY_GET_AND_SET = "android:propertyGetAndSet";

  private ViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Find provided ancestorFinder and propertyGetAndSet
   *
   * @param view view we will invoke find ancestor on
   * @param helper find ancestor helper
   * @param propertyGetAndSet property retriever and setter uses reflection so avoid this usage
   * @param <T> T type of the view (target view)
   * @param <S> S type of the view (ancestor view)
   */
  @BindingAdapter({
      ANDROID_FIND_ANCESTOR,
      ANDROID_PROPERTY_GET_AND_SET
  })
  public static <T, S extends View> void registerPropertySetAndGetWithFindAncestor(View view, IFindAncestor helper, PropertyGetAndSet<S, T> propertyGetAndSet) {
    helper.setAncestorOf(view);
    propertyGetAndSet.setTarget(Objects.toObject(view));
    propertyGetAndSet.setHelper(helper);
    propertyGetAndSet.execute();
  }

  /**
   * Registers view with ParameterizedRelayCommand that has parameter type of T.
   *
   * @param view view to register for click
   * @param command command to execute
   * @param param command parameter
   * @param <T> type of parameter
   */
  @BindingAdapter(
      value = {
        ANDROID_COMMAND,
        ANDROID_COMMAND_PARAMETER
      },
      requireAll = false
  )
  public static <T> void registerCommandAndParameter(View view, ICommand<T> command, T param) {
    final View.OnClickListener newListener;
    if (command == null) {
      newListener = null;
    } else {
      final View.OnClickListener oldListener = ListenerUtil.getListener(view, R.id.onClickListener);
      newListener = v -> {
        if (command.canExecute(param)) {
          command.execute(param);
        }
        if (oldListener != null) {
          oldListener.onClick(view);
        }
      };
    }
    view.setOnClickListener(newListener);
  }
}
