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
import java.util.Collection;
import java8.util.Objects;
import org.fs.mvvm.R;
import org.fs.mvvm.common.AbstractBindingAdapter;
import org.fs.mvvm.common.AbstractBindingHolder;
import org.fs.mvvm.data.PropertyInfo;
import org.fs.mvvm.listeners.SimpleListViewScrollListener;
import org.fs.mvvm.utils.Properties;
import android.widget.ListView;

public final class ListViewCompatBindingAdapter {

  private final static String ANDROID_ITEM_SOURCE = "android:itemSource";

  private final static String ANDROID_SELECTED_POSITION = "android:selectedPosition";
  private final static String ANDROID_SELECTED_POSITION_ATTR_CHANGED = "android:selectedPositionAttrChanged";

  private final static String ANDROID_SELECTED_ITEM = "android:selectedItem";
  private final static String ANDROID_SELECTED_ITEM_ATTR_CHANGED = "android:selectedItem";

  private final static String ANDROID_SELECTED_POSITIONS = "android:selectedPositions";
  private final static String ANDROID_SELECTED_POSITIONS_ATTR_CHANGED = "android:selectedPositionsAttrChanged";

  private final static String ANDROID_SELECTED_ITEMS = "android:selectedItems";
  private final static String ANDROID_SELECTED_ITEMS_ATTR_CHANGED = "android:selectedItems";

  private final static String ANDROID_LOAD_MORE = "android:loadMore";
  private final static String ANDROID_LOAD_MORE_ATTR_CHANGED = "android:loadMoreAttrChanged";

  private ListViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  /**
   * Getter of SelectedPositions on viewList
   *
   * @param viewList viewList instance
   * @return null or Collection of integer
   */
  @InverseBindingAdapter(
      event = ANDROID_SELECTED_POSITIONS_ATTR_CHANGED,
      attribute = ANDROID_SELECTED_POSITIONS
  )
  public static Collection<Integer> provideSelectedPositions(ListView viewList) {
    PropertyInfo<Collection<Integer>> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedPositions);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return null;
  }

  /**
   * Setter of SelectedPositions on viewList
   *
   * @param viewList viewList instance
   * @param positions selected positions
   */
  @BindingAdapter(
      ANDROID_SELECTED_POSITIONS
  )
  public static void setSelectedPositions(ListView viewList, Collection<Integer> positions) {
    PropertyInfo<Collection<Integer>> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedPositions);
    if (propertyInfo != null) {
      if (!Objects.equals(positions, propertyInfo.getPropertyValue())) {
        propertyInfo = new PropertyInfo<>(positions);
      }
    } else {
      propertyInfo = new PropertyInfo<>(positions);
    }
    Properties.setPropertyInfo(viewList, propertyInfo, R.id.viewList_selectedPositions);
  }

  /**
   * Getter of SelectedItems on viewList
   *
   * @param viewList viewList instance
   * @param <D> type of item
   * @return null or Collection of items
   */
  @InverseBindingAdapter(
      event = ANDROID_SELECTED_ITEMS_ATTR_CHANGED,
      attribute = ANDROID_SELECTED_ITEMS
  )
  public static <D> Collection<D> provideSelectedItems(ListView viewList) {
    PropertyInfo<Collection<D>> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedItems);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return null;
  }

  /**
   * Setter of SelectedItems on viewList
   *
   * @param viewList viewList instance
   * @param items selected items
   * @param <D> type of item
   */
  @BindingAdapter(
      ANDROID_SELECTED_ITEMS
  )
  public static <D> void setSelectedItems(ListView viewList, Collection<D> items) {
    PropertyInfo<Collection<D>> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedItems);
    if (propertyInfo != null) {
      if (!Objects.equals(items, propertyInfo.getPropertyValue())) {
        propertyInfo = new PropertyInfo<>(items);
      }
    } else {
      propertyInfo = new PropertyInfo<>(items);
    }
    Properties.setPropertyInfo(viewList, propertyInfo, R.id.viewList_selectedItems);
  }

  /**
   * Getter of SelectedItem on viewList
   *
   * @param viewList viewList instance
   * @param <D> type of item
   * @return null or item
   */
  @InverseBindingAdapter(
      event =  ANDROID_SELECTED_ITEM_ATTR_CHANGED,
      attribute = ANDROID_SELECTED_ITEM
  )
  public static <D> D provideSelectedItem(ListView viewList) {
    PropertyInfo<D> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedItem);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return null;
  }

  /**
   * Setter of SelectedItem on viewList
   *
   * @param viewList viewList instance
   * @param item selected item
   * @param <D> type of item
   */
  @BindingAdapter(
      ANDROID_SELECTED_ITEM
  )
  public static <D> void setSelectedItem(ListView viewList, D item) {
    PropertyInfo<D> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedItem);
    if (propertyInfo != null) {
      if (!Objects.equals(item, propertyInfo.getPropertyValue())) {
        propertyInfo = new PropertyInfo<>(item);
      }
    } else {
      propertyInfo = new PropertyInfo<>(item);
    }
    Properties.setPropertyInfo(viewList, propertyInfo, R.id.viewList_selectedItem);
  }

  /**
   * Gets value from here
   *
   * @param viewList selection change
   * @return int position
   */
  @InverseBindingAdapter(
      event = ANDROID_SELECTED_POSITION_ATTR_CHANGED,
      attribute = ANDROID_SELECTED_POSITION
  )
  public static int provideSelectedPosition(ListView viewList) {
    PropertyInfo<Integer> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedPosition);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return -1;
  }

  /**
   * Register selected position for this viewList
   *
   * @param viewList view instance
   * @param selectedPosition position as int
   */
  @BindingAdapter(
      ANDROID_SELECTED_POSITION
  )
  public static void setSelectedPosition(ListView viewList, int selectedPosition) {
    PropertyInfo<Integer> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedPosition);
    if (propertyInfo != null) {
      if (propertyInfo.getPropertyValue() != selectedPosition) {
        propertyInfo = new PropertyInfo<>(selectedPosition);
      }
    } else {
      propertyInfo = new PropertyInfo<>(selectedPosition);
    }
    Properties.setPropertyInfo(viewList, propertyInfo, R.id.viewList_selectedPosition);
  }

  /**
   * getter for two way loadMore
   *
   * @param viewList viewList
   * @return true or false
   */
  @InverseBindingAdapter(
      event = ANDROID_LOAD_MORE_ATTR_CHANGED,
      attribute = ANDROID_LOAD_MORE
  )
  public static boolean provideIsLoadMore(ListView viewList) {
    PropertyInfo<Boolean> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_isLoadMore);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return false;
  }

  /**
   * setter for two way loadMore
   *
   * @param viewList viewList
   * @param isLoadMore true or false
   */
  @BindingAdapter(
      ANDROID_LOAD_MORE
  )
  public static void registerLoadMore(ListView viewList, boolean isLoadMore) {
    PropertyInfo<Boolean> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_isLoadMore);
    if (propertyInfo != null) {
      if (propertyInfo.getPropertyValue() != isLoadMore) {
        propertyInfo = new PropertyInfo<>(isLoadMore);
      }
    } else {
      propertyInfo = new PropertyInfo<>(isLoadMore);
    }
    Properties.setPropertyInfo(viewList, propertyInfo, R.id.viewList_isLoadMore);
  }

  /**
   * tracks child position and sets load more property if needed
   *
   * @param viewList viewList
   * @param loadMoreAttrChanged event bubbling
   */
  @BindingAdapter(
      ANDROID_LOAD_MORE_ATTR_CHANGED
  )
  public static void registerScrollListener(ListView viewList, InverseBindingListener loadMoreAttrChanged) {
    PropertyInfo<Boolean> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_isLoadMore);
    if (propertyInfo != null) {
      final AbsListView.OnScrollListener newListener = new SimpleListViewScrollListener() {
        @Override public void onScroll(AbsListView view, int first, int visible, int total) {
          if (first + visible == total && total != 0) {
            if (!propertyInfo.getPropertyValue()) {
              Properties.setPropertyInfo(viewList, new PropertyInfo<>(true), R.id.viewList_isLoadMore);
              if (loadMoreAttrChanged != null) {
                loadMoreAttrChanged.onChange();
              }
            }
          } else {
            if (propertyInfo.getPropertyValue()) {
              Properties.setPropertyInfo(viewList, new PropertyInfo<>(false), R.id.viewList_isLoadMore);
              if (loadMoreAttrChanged != null) {
                loadMoreAttrChanged.onChange();
              }
            }
          }
        }
      };
      viewList.setOnScrollListener(newListener);
    }
  }


  /**
   * Registers adapter on view and checks for position
   *
   * @param viewList viewList to set adapter and track selection changes
   * @param itemSource itemSource instance
   * @param <T> type of adapter
   * @param <D> type of entity
   * @param <V> type of viewHolder
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
  public static <T extends AbstractBindingAdapter<D, V>, D extends BaseObservable, V extends AbstractBindingHolder<D>>
    void registerItemSource(ListView viewList, T itemSource,
      InverseBindingListener selectedPositionAttrChanged, InverseBindingListener selectedItemAttrChanged,
      InverseBindingListener selectedPositionsAttrChanged, InverseBindingListener selectedItemsAttrChanged) {
    //if our adapter not null we can use it
    if (itemSource != null) {
      if (itemSource.isSingleMode()) {
        itemSource.setSingleItemCallback((item) -> {
          Properties.setPropertyInfo(viewList, new PropertyInfo<>(item), R.id.viewList_selectedItem);
          if (selectedItemAttrChanged != null) {
            selectedItemAttrChanged.onChange();
          }
        });
        itemSource.setSinglePositionCallback((position) -> {
          Properties.setPropertyInfo(viewList, new PropertyInfo<>(position), R.id.viewList_selectedPosition);
          if (selectedPositionAttrChanged != null) {
            selectedPositionAttrChanged.onChange();
          }
        });
      } else {
        itemSource.setMultiItemCallback((items) -> {
          Properties.setPropertyInfo(viewList, new PropertyInfo<>(items), R.id.viewList_selectedItems);
          if (selectedItemsAttrChanged != null) {
            selectedItemsAttrChanged.onChange();
          }
        });
        itemSource.setMultiPositionCallback((positions) -> {
          Properties.setPropertyInfo(viewList, new PropertyInfo<>(positions), R.id.viewRecycler_selectedPositions);
          if (selectedPositionsAttrChanged != null) {
            selectedPositionsAttrChanged.onChange();
          }
        });
      }
    } else {
      viewList.setAdapter(null);
    }
  }
}
