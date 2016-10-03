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
import android.support.v7.widget.helper.ItemTouchHelper;
import org.fs.mvvm.common.AbstractRecyclerBindingAdapter;
import org.fs.mvvm.common.AbstractRecyclerBindingHolder;
import org.fs.mvvm.widget.RecyclerView;


public final class RecyclerViewCompatBindingAdapter {

  private final static String ANDROID_ITEM_SOURCE     = "android:itemSource";
  private final static String ANDROID_LAYOUT_MANAGER  = "android:layoutManager";
  private final static String ANDROID_ITEM_ANIMATOR   = "android:itemAnimator";
  private final static String ANDROID_TOUCH_HELPER    = "android:touchHelper";

  private final static String ANDROID_SELECTED_POSITION = "android:selectedPosition";
  private final static String ANDROID_SELECTED_POSITION_ATTR_CHANGED = "android:selectedPositionAttrChanged";

  private RecyclerViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Gets value from here
   *
   * @param viewRecycler selection change
   * @return int position
   */
  @InverseBindingAdapter(
      event = ANDROID_SELECTED_POSITION_ATTR_CHANGED,
      attribute = ANDROID_SELECTED_POSITION
  )
  public static int provideSelectedPosition(RecyclerView viewRecycler) {
    return viewRecycler.getSelectedPosition();
  }

  /**
   * Register selected position for this viewRecycler
   *
   * @param viewRecycler view instance
   * @param selectedPosition position as int
   */
  @BindingAdapter(
      ANDROID_SELECTED_POSITION
  )
  public static void registerSelectedPosition(RecyclerView viewRecycler, int selectedPosition) {
    if (viewRecycler.getSelectedPosition() != selectedPosition) {
      viewRecycler.setSelectedPosition(selectedPosition);
    }
  }

  /**
   * Registers touchHelper instance on recyclerView
   *
   * @param viewRecycler viewRecycler to be observed for touchEvents
   * @param touchHelper touchHelper that will track of touch it's items
   */
  @BindingAdapter(
      ANDROID_TOUCH_HELPER
  )
  public static void registerTouchHelper(RecyclerView viewRecycler, ItemTouchHelper touchHelper) {
    if (touchHelper != null) {
      touchHelper.attachToRecyclerView(viewRecycler);
    }
  }

  /**
   * Registers itemAnimator on RecyclerView that will animate it's entities state changes ex. move, remove or add etc.
   *
   * @param viewRecycler viewRecycler to have animation on its children
   * @param itemAnimator animator for viewRecycler
   */
  @BindingAdapter(
      ANDROID_ITEM_ANIMATOR
  )
  public static void registerItemAnimator(RecyclerView viewRecycler, RecyclerView.ItemAnimator itemAnimator) {
    if (itemAnimator != null) {
      viewRecycler.setItemAnimator(itemAnimator);
    }
  }

  /**
   * Registers layoutManager instance for this recyclerView for it's child positioning
   *
   * @param viewRecycler viewRecycler that will contain layoutManager for it's children
   * @param layoutManager layoutManager instance that will take care of layout.
   */
  @BindingAdapter(
      ANDROID_LAYOUT_MANAGER
  )
  public static void registerLayoutManager(RecyclerView viewRecycler, RecyclerView.LayoutManager layoutManager) {
    if (layoutManager != null) {
      viewRecycler.setLayoutManager(layoutManager);
      viewRecycler.setHasFixedSize(true);
    }
  }

  /**
   * Registers adapter on recyclerView that will help us easily access it's data and others.
   *
   * @param viewRecycler viewRecycler that will register this adapter on itself.
   * @param itemSource adapter that contains data for this recyclerView.
   * @param <T> T type of the Adapter
   * @param <D> D type of the Adapter Entity.
   * @param <V> V type of the ViewHolder of Adapter.
   */
  @BindingAdapter(
    value = {
        ANDROID_ITEM_SOURCE,
        ANDROID_SELECTED_POSITION_ATTR_CHANGED
    },
    requireAll = false
  )
  public static <T extends AbstractRecyclerBindingAdapter<D, V>, D extends BaseObservable, V extends AbstractRecyclerBindingHolder<D>>
    void registerAdapter(RecyclerView viewRecycler, T itemSource, InverseBindingListener selectedPositionAttrChanged) {
    //do not throw error if adapter is null, just ignore
      if (itemSource != null) {
        itemSource.addInverseCallback((position) -> {
          viewRecycler.setSelectedPosition(position);
          if (selectedPositionAttrChanged != null) {
            selectedPositionAttrChanged.onChange();
          }
        });
        viewRecycler.setAdapter(itemSource);
      }
  }
}
