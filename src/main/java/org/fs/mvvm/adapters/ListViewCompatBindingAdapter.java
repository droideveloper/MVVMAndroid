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

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.widget.AbsListView;
import org.fs.mvvm.common.AbstractBindingAdapter;
import org.fs.mvvm.common.AbstractBindingHolder;
import org.fs.mvvm.listeners.SimpleListViewScrollListener;
import org.fs.mvvm.utils.Preconditions;
import org.fs.mvvm.widget.ListView;

public final class ListViewCompatBindingAdapter {

  private final static String ANDROID_ITEM_SOURCE = "android:itemSource";

  private final static String ANDROID_SELECTED_POSITION = "android:selectedPosition";
  private final static String ANDROID_SELECTED_POSITION_ATTR_CHANGED = "android:selectedPositionAttrChanged";

  private final static String ANDROID_LOAD_MORE = "android:loadMore";
  private final static String ANDROID_LOAD_MORE_ATTR_CHANGED = "android:loadMoreAttrChanged";

  private ListViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  /**
   * Two-way binder for selection change
   *
   * @param viewList binder changed
   * @return int change
   */
  @InverseBindingAdapter(
      attribute = ANDROID_SELECTED_POSITION,
      event = ANDROID_SELECTED_POSITION_ATTR_CHANGED
  )
  public static int provideSelectedPosition(ListView viewList) {
    return viewList.getSelectedPosition();
  }

  /**
   * setter for two way builder to apply change
   *
   * @param viewList view
   * @param selectedPosition position value
   */
  @BindingAdapter(
      ANDROID_SELECTED_POSITION
  )
  public static void registerSelectedPosition(ListView viewList, int selectedPosition) {
    if (viewList.getSelectedPosition() != selectedPosition) {
      viewList.setSelectedPosition(selectedPosition);
    }
  }

  @InverseBindingAdapter(
      event = ANDROID_LOAD_MORE_ATTR_CHANGED,
      attribute = ANDROID_LOAD_MORE
  )
  public static boolean provideIsLoadMore(ListView viewList) {
    return viewList.isLoadMore();
  }

  @BindingAdapter(
      ANDROID_LOAD_MORE
  )
  public static void registerLoadMore(ListView viewList, boolean isLoadMore) {
    if (viewList.isLoadMore() != isLoadMore) {
      viewList.setLoadMore(isLoadMore);
    }
  }

  @BindingAdapter(
      ANDROID_LOAD_MORE_ATTR_CHANGED
  )
  public static void registerScrollListener(ListView viewList, InverseBindingListener loadMoreAttrChanged) {
    final AbsListView.OnScrollListener newListener = new SimpleListViewScrollListener() {
      @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount
            && totalItemCount != 0) {
          if (!viewList.isLoadMore()) {
            viewList.setLoadMore(true);
          }
        } else {
          if (viewList.isLoadMore()) {
            viewList.setLoadMore(false);
          }
        }
        if (loadMoreAttrChanged != null) {
          loadMoreAttrChanged.onChange();
        }
      }
    };
    viewList.setOnScrollListener(newListener);
  }


  /**
   * Registers adapter on view and checks for position
   *
   * @param viewList viewList to set adapter and track selection changes
   * @param adapter adapter instance
   * @param <T> type of adapter
   * @param <D> type of entity
   * @param <V> type of viewHolder
   */
  @BindingAdapter(
      value = {
        ANDROID_ITEM_SOURCE,
        ANDROID_SELECTED_POSITION_ATTR_CHANGED
      },
      requireAll = false
  )
  public static <T extends AbstractBindingAdapter<D, V>, D extends BaseObservable, V extends AbstractBindingHolder<D>>
    void registerItemSource(ListView viewList, T adapter, InverseBindingListener selectedPositionAttrChanged) {
    Preconditions.checkNotNull(adapter, "itemSource is null");
    adapter.setSelectedPositionCallback(position -> {
      viewList.setSelectedPosition(position);
      if (selectedPositionAttrChanged != null) {
        selectedPositionAttrChanged.onChange();
      }
    });
    viewList.setAdapter(adapter);
  }
}
