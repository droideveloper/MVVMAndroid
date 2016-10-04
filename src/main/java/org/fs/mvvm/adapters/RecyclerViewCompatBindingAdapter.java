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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import java.util.Collection;
import java8.util.Objects;
import org.fs.mvvm.R;
import org.fs.mvvm.common.AbstractRecyclerBindingAdapter;
import org.fs.mvvm.common.AbstractRecyclerBindingHolder;
import org.fs.mvvm.data.PropertyInfo;
import org.fs.mvvm.utils.Properties;


public final class RecyclerViewCompatBindingAdapter {

  private final static String ANDROID_ITEM_SOURCE     = "android:itemSource";
  private final static String ANDROID_LAYOUT_MANAGER  = "android:layoutManager";
  private final static String ANDROID_ITEM_ANIMATOR   = "android:itemAnimator";
  private final static String ANDROID_TOUCH_HELPER    = "android:touchHelper";

  private final static String ANDROID_SELECTED_POSITION = "android:selectedPosition";
  private final static String ANDROID_SELECTED_POSITION_ATTR_CHANGED = "android:selectedPositionAttrChanged";

  private final static String ANDROID_SELECTED_ITEM = "android:selectedItem";
  private final static String ANDROID_SELECTED_ITEM_ATTR_CHANGED = "android:selectedItem";

  private final static String ANDROID_SELECTED_POSITIONS = "android:selectedPositions";
  private final static String ANDROID_SELECTED_POSITIONS_ATTR_CHANGED = "android:selectedPositionsAttrChanged";

  private final static String ANDROID_SELECTED_ITEMS = "android:selectedItems";
  private final static String ANDROID_SELECTED_ITEMS_ATTR_CHANGED = "android:selectedItems";

  private RecyclerViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Getter of SelectedPositions on viewRecycler
   *
   * @param viewRecycler viewRecycler instance
   * @return null or Collection of integer
   */
  @InverseBindingAdapter(
      event = ANDROID_SELECTED_POSITIONS_ATTR_CHANGED,
      attribute = ANDROID_SELECTED_POSITIONS
  )
  public static Collection<Integer> provideSelectedPositions(RecyclerView viewRecycler) {
    PropertyInfo<Collection<Integer>> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedPositions);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return null;
  }

  /**
   * Setter of SelectedPositions on viewRecycler
   *
   * @param viewRecycler viewRecycler instance
   * @param positions selected positions
   */
  @BindingAdapter(
      ANDROID_SELECTED_POSITIONS
  )
  public static void setSelectedPositions(RecyclerView viewRecycler, Collection<Integer> positions) {
    PropertyInfo<Collection<Integer>> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedPositions);
    if (propertyInfo != null) {
      if (!Objects.equals(positions, propertyInfo.getPropertyValue())) {
        propertyInfo = new PropertyInfo<>(positions);
      }
    } else {
      propertyInfo = new PropertyInfo<>(positions);
    }
    Properties.setPropertyInfo(viewRecycler, propertyInfo, R.id.viewRecycler_selectedPositions);
  }

  /**
   * Getter of SelectedItems on viewRecycler
   *
   * @param viewRecycler viewRecycler instance
   * @param <D> type of item
   * @return null or Collection of items
   */
  @InverseBindingAdapter(
      event = ANDROID_SELECTED_ITEMS_ATTR_CHANGED,
      attribute = ANDROID_SELECTED_ITEMS
  )
  public static <D> Collection<D> provideSelectedItems(RecyclerView viewRecycler) {
    PropertyInfo<Collection<D>> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedItems);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return null;
  }

  /**
   * Setter of SelectedItems on viewRecycler
   *
   * @param viewRecycler viewRecycler instance
   * @param items selected items
   * @param <D> type of item
   */
  @BindingAdapter(
      ANDROID_SELECTED_ITEMS
  )
  public static <D> void setSelectedItems(RecyclerView viewRecycler, Collection<D> items) {
    PropertyInfo<Collection<D>> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedItems);
    if (propertyInfo != null) {
      if (!Objects.equals(items, propertyInfo.getPropertyValue())) {
        propertyInfo = new PropertyInfo<>(items);
      }
    } else {
      propertyInfo = new PropertyInfo<>(items);
    }
    Properties.setPropertyInfo(viewRecycler, propertyInfo, R.id.viewRecycler_selectedItems);
  }

  /**
   * Getter of SelectedItem on viewRecycler
   *
   * @param viewRecycler viewRecycler instance
   * @param <D> type of item
   * @return null or item
   */
  @InverseBindingAdapter(
      event =  ANDROID_SELECTED_ITEM_ATTR_CHANGED,
      attribute = ANDROID_SELECTED_ITEM
  )
  public static <D> D provideSelectedItem(RecyclerView viewRecycler) {
    PropertyInfo<D> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedItem);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return null;
  }

  /**
   * Setter of SelectedItem on viewRecycler
   *
   * @param viewRecycler viewRecycler instance
   * @param item selected item
   * @param <D> type of item
   */
  @BindingAdapter(
      ANDROID_SELECTED_ITEM
  )
  public static <D> void setSelectedItem(RecyclerView viewRecycler, D item) {
    PropertyInfo<D> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedItem);
    if (propertyInfo != null) {
      if (!Objects.equals(item, propertyInfo.getPropertyValue())) {
        propertyInfo = new PropertyInfo<>(item);
      }
    } else {
      propertyInfo = new PropertyInfo<>(item);
    }
    Properties.setPropertyInfo(viewRecycler, propertyInfo, R.id.viewRecycler_selectedItem);
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
    PropertyInfo<Integer> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedPosition);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return -1;
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
  public static void setSelectedPosition(RecyclerView viewRecycler, int selectedPosition) {
    PropertyInfo<Integer> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedPosition);
    if (propertyInfo != null) {
      if (propertyInfo.getPropertyValue() != selectedPosition) {
        propertyInfo = new PropertyInfo<>(selectedPosition);
      }
    } else {
      propertyInfo = new PropertyInfo<>(selectedPosition);
    }
    Properties.setPropertyInfo(viewRecycler, propertyInfo, R.id.viewRecycler_selectedPosition);
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
        ANDROID_SELECTED_POSITION_ATTR_CHANGED,
        ANDROID_SELECTED_ITEM_ATTR_CHANGED,
        ANDROID_SELECTED_POSITIONS_ATTR_CHANGED,
        ANDROID_SELECTED_ITEMS_ATTR_CHANGED
    },
    requireAll = false
  )
  public static <T extends AbstractRecyclerBindingAdapter<D, V>, D extends BaseObservable, V extends AbstractRecyclerBindingHolder<D>>
    void registerAdapter(RecyclerView viewRecycler, T itemSource,
      InverseBindingListener selectedPositionAttrChanged, InverseBindingListener selectedItemAttrChanged,
      InverseBindingListener selectedPositionsAttrChanged, InverseBindingListener selectedItemsAttrChanged) {
    //do not throw error if adapter is null, just ignore
      if (itemSource != null) {
        //singleMode
        if (itemSource.isSingleMode()) {
          itemSource.setSingleItemCallback((item) -> {
            Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(item), R.id.viewRecycler_selectedItem);
            if (selectedItemAttrChanged != null) {
              selectedItemAttrChanged.onChange();
            }
          });
          itemSource.setSinglePositionCallback((position) -> {
            Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(position), R.id.viewRecycler_selectedPosition);
            if (selectedPositionAttrChanged != null) {
              selectedPositionAttrChanged.onChange();
            }
          });
        } else {//multiMode
          itemSource.setMultiItemCallback((items) -> {
            Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(items), R.id.viewRecycler_selectedItems);
            if (selectedItemsAttrChanged != null) {
              selectedItemsAttrChanged.onChange();
            }
          });
          itemSource.setMultiPositionCallback((positions) -> {
            Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(positions), R.id.viewRecycler_selectedPositions);
            if (selectedPositionsAttrChanged != null) {
              selectedPositionsAttrChanged.onChange();
            }
          });
        }
        viewRecycler.setAdapter(itemSource);
      } else {
        viewRecycler.setAdapter(null);
      }
  }
}
