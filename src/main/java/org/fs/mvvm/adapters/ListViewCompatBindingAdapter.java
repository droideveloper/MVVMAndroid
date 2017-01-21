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
import org.fs.mvvm.common.AbstractEntity;
import org.fs.mvvm.data.PropertyInfo;
import org.fs.mvvm.listeners.SimpleListViewScrollListener;
import org.fs.mvvm.utils.Properties;
import android.widget.ListView;

public final class ListViewCompatBindingAdapter {

  private final static String BIND_ITEM_SOURCE = "bindings:itemSource";

  private final static String BIND_POSITION              = "bindings:position";
  private final static String BIND_POSITION_ATTR_CHANGED = "bindings:positionAttrChanged";

  private final static String BIND_ITEM              = "bindings:item";
  private final static String BIND_ITEM_ATTR_CHANGED = "bindings:itemAttrChanged";

  private final static String BIND_POSITIONS               = "bindings:positions";
  private final static String BIND_POSITIONS_ATTR_CHANGED  = "bindings:positionsAttrChanged";

  private final static String BIND_ITEMS               = "bindings:items";
  private final static String BIND_ITEMS_ATTR_CHANGED  = "bindings:itemsAttrChanged";

  private final static String BIND_IS_LOADING                = "bindings:isLoading";
  private final static String BIND_IS_LOADING_ATTR_CHANGED   = "bindings:isLoadingAttrChanged";

  private ListViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @InverseBindingAdapter(attribute = BIND_POSITIONS,
      event = BIND_POSITIONS_ATTR_CHANGED)
  public static Collection<Integer> viewListRetreivePositions(ListView viewList) {
    PropertyInfo<Collection<Integer>> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedPositions);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return null;
  }
  
  @BindingAdapter({ BIND_POSITIONS })
  public static void viewListRegisterPositions(ListView viewList, Collection<Integer> positions) {
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

  @InverseBindingAdapter(attribute = BIND_ITEMS,
      event = BIND_ITEMS_ATTR_CHANGED)
  public static <D extends AbstractEntity> Collection<D> viewListRegisterItems(ListView viewList) {
    PropertyInfo<Collection<D>> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedItems);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return null;
  }
  
  @BindingAdapter({ BIND_ITEMS })
  public static <D extends AbstractEntity> void viewListRetreiveItems(ListView viewList, Collection<D> items) {
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

  @InverseBindingAdapter(attribute = BIND_ITEM,
      event =  BIND_ITEM_ATTR_CHANGED)
  public static <D extends AbstractEntity> D viewListRetreiveItem(ListView viewList) {
    PropertyInfo<D> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedItem);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return null;
  }
  
  @BindingAdapter({ BIND_ITEM })
  public static <D extends AbstractEntity> void viewListRegisterItem(ListView viewList, D item) {
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

  @InverseBindingAdapter(attribute = BIND_POSITION,
      event = BIND_POSITION_ATTR_CHANGED)
  public static int viewListRetreivePosition(ListView viewList) {
    PropertyInfo<Integer> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_selectedPosition);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return -1;
  }

  @BindingAdapter({ BIND_POSITION })
  public static void viewListRegisterPosition(ListView viewList, int selectedPosition) {
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
  
  @InverseBindingAdapter(attribute = BIND_IS_LOADING,
      event = BIND_IS_LOADING_ATTR_CHANGED)
  public static boolean viewListRetreiveIsLoading(ListView viewList) {
    PropertyInfo<Boolean> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_isLoadMore);
    if (propertyInfo != null) {
      return propertyInfo.getPropertyValue();
    }
    return false;
  }

  @BindingAdapter({ BIND_IS_LOADING })
  public static void viewListRegisterIsLoading(ListView viewList, boolean isLoadMore) {
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

  @BindingAdapter({ BIND_IS_LOADING_ATTR_CHANGED })
  public static void registerScrollListener(ListView viewList, InverseBindingListener isLoadingAttrChanged) {
    PropertyInfo<Boolean> propertyInfo = Properties.getPropertyInfo(viewList, R.id.viewList_isLoadMore);
    if (propertyInfo != null) {
      final AbsListView.OnScrollListener newListener = new SimpleListViewScrollListener() {
        @Override public void onScroll(AbsListView view, int first, int visible, int total) {
          if (first + visible == total && total != 0) {
            if (!propertyInfo.getPropertyValue()) {
              Properties.setPropertyInfo(viewList, new PropertyInfo<>(true), R.id.viewList_isLoadMore);
              if (isLoadingAttrChanged != null) {
                isLoadingAttrChanged.onChange();
              }
            }
          } else {
            if (propertyInfo.getPropertyValue()) {
              Properties.setPropertyInfo(viewList, new PropertyInfo<>(false), R.id.viewList_isLoadMore);
              if (isLoadingAttrChanged != null) {
                isLoadingAttrChanged.onChange();
              }
            }
          }
        }
      };
      viewList.setOnScrollListener(newListener);
    }
  }

  @BindingAdapter(
      value = {
          BIND_ITEM_SOURCE,
          BIND_POSITION_ATTR_CHANGED,
          BIND_ITEM_ATTR_CHANGED,
          BIND_POSITIONS_ATTR_CHANGED,
          BIND_ITEMS_ATTR_CHANGED
      },
      requireAll = false
  )
  public static <T extends AbstractBindingAdapter<D, V>, D extends BaseObservable, V extends AbstractBindingHolder<D>>
    void viewListRegisterAdapter(ListView viewList, T itemSource,
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
