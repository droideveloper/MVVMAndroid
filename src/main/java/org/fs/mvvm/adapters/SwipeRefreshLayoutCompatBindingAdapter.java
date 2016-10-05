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
import org.fs.mvvm.listeners.OnRefreshed;
import org.fs.mvvm.utils.Preconditions;

public class SwipeRefreshLayoutCompatBindingAdapter {

  private final static String BIND_IS_REFRESHING_CALLBACK      = "bindings:refreshCallback";
  private final static String BIND_IS_REFRESHING               = "bindings:isRefreshing";
  private final static String BIND_CONVERTER                   = "bindings:converter";
  private final static String BIND_IS_REFRESHING_ATTR_CHANGED  = "bindings:isRefreshingAttrChanged";

  private SwipeRefreshLayoutCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @BindingAdapter({ BIND_IS_REFRESHING })
  public static void viewSwipeRefreshLayoutRegisterIsRefreshing(SwipeRefreshLayout viewSwipeLayout, boolean isRefreshing) {
    if (viewSwipeLayout.isRefreshing() != isRefreshing) {
      viewSwipeLayout.setRefreshing(isRefreshing);
    }
  }

  @InverseBindingAdapter(attribute = BIND_IS_REFRESHING,
      event = BIND_IS_REFRESHING_ATTR_CHANGED)
  public static boolean viewSwipeRefreshLayoutRetreiveIsRefreshing(SwipeRefreshLayout viewSwipeLayout) {
    return viewSwipeLayout.isRefreshing();
  }

  @BindingAdapter(
      value = {
        BIND_IS_REFRESHING,
        BIND_CONVERTER
      }
  )
  public static <T> void viewSwipeRefreshLayoutRegisterObject(SwipeRefreshLayout viewSwipeLayout, T object, IConverter<T, Boolean> converter) {
    Preconditions.checkNotNull(converter, "converter is null");
    Boolean newValue = converter.convert(object, Locale.getDefault());
    viewSwipeLayout.setRefreshing(newValue);
  }

  @BindingAdapter(
      value = {
          BIND_IS_REFRESHING_CALLBACK,
          BIND_IS_REFRESHING_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewSwipeRefreshLayoutRegisterCallback(SwipeRefreshLayout viewSwipeLayout, OnRefreshed callback, InverseBindingListener isRefreshingAttrChanged) {
    if (callback == null && isRefreshingAttrChanged == null) {
      viewSwipeLayout.setOnRefreshListener(null);
    } else {
      viewSwipeLayout.setOnRefreshListener(() -> {
        if (isRefreshingAttrChanged != null) {
          isRefreshingAttrChanged.onChange();
        }
        if (callback != null) {
          callback.onRefreshed();
        }
      });
    }
  }
}
