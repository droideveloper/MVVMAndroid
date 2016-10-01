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
import android.support.v4.widget.SwipeRefreshLayout;
import java.util.Locale;
import org.fs.mvvm.data.IConverter;
import org.fs.mvvm.utils.Preconditions;

public class SwipeRefreshLayoutCompatBindingAdapter {

  private final static String ANDROID_IS_REFRESHING_CALLBACK = "android:refreshCallback";

  private final static String ANDROID_IS_REFRESHING = "android:isRefreshing";
  private final static String ANDROID_CONVERTER = "android:converter";
  private final static String ANDROID_IS_REFRESHING_ATTR_CHANGED = "android:isRefreshingAttrChanged";

  private SwipeRefreshLayoutCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  /**
   * Sets boolean value on viewSwipeLayout
   *
   * @param viewSwipeLayout layout instance
   * @param isRefreshing true or false
   */
  @BindingAdapter(
      ANDROID_IS_REFRESHING
  )
  public static void registerIsRefreshing(SwipeRefreshLayout viewSwipeLayout, boolean isRefreshing) {
    if (viewSwipeLayout.isRefreshing() != isRefreshing) {
      viewSwipeLayout.setRefreshing(isRefreshing);
    }
  }

  /**
   * Gets boolean value of viewSwipeLayout
   *
   * @param viewSwipeLayout layout instance
   * @return true or false
   */
  @InverseBindingAdapter(
      attribute = ANDROID_IS_REFRESHING,
      event = ANDROID_IS_REFRESHING_ATTR_CHANGED
  )
  public static boolean registerIsRefresing(SwipeRefreshLayout viewSwipeLayout) {
    return viewSwipeLayout.isRefreshing();
  }

  /**
   * Sets any object to isRefreshing property and it will
   * require converter instance if there is not any
   * then error occur
   *
   * @param viewSwipeLayout layout instance
   * @param object object that needs conversion to boolean
   * @param converter object to boolean converter
   * @param isRefreshingAttrChanged attr that system will tract for two-way binding
   * @param <T> type of object we can convert
   */
  @BindingAdapter(
      value = {
        ANDROID_IS_REFRESHING,
        ANDROID_CONVERTER,
        ANDROID_IS_REFRESHING_ATTR_CHANGED
      },
      requireAll = false
  )
  public static <T> void registerIsRefreshing(SwipeRefreshLayout viewSwipeLayout, T object, IConverter<T, Boolean> converter, InverseBindingListener isRefreshingAttrChanged) {
    Preconditions.checkNotNull(converter, "converter is null");
    Boolean newValue = converter.convert(object, Locale.getDefault());
    viewSwipeLayout.setRefreshing(newValue);
    if (isRefreshingAttrChanged != null) {
      isRefreshingAttrChanged.onChange();
    }
  }

  /**
   * Binds on refreshing on layout with listener
   *
   * @param viewSwipeLayout layout instance
   * @param callback callback
   * @param isRefreshingAttrChanged attrChanged callback that binding will be tracking if present
   */
  @BindingAdapter(
      value = {
          ANDROID_IS_REFRESHING_CALLBACK,
          ANDROID_IS_REFRESHING_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void registerRefreshCallback(SwipeRefreshLayout viewSwipeLayout, SwipeRefreshLayout.OnRefreshListener callback, InverseBindingListener isRefreshingAttrChanged) {
    Preconditions.checkNotNull(callback, "callback is null");
    viewSwipeLayout.setOnRefreshListener(() -> {
      callback.onRefresh();
      if (isRefreshingAttrChanged != null) {
        isRefreshingAttrChanged.onChange();
      }
    });
  }
}
