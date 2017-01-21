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
import android.databinding.adapters.ListenerUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import java.util.Collection;
import org.fs.mvvm.R;
import org.fs.mvvm.common.AbstractEntity;
import org.fs.mvvm.common.AbstractRecyclerBindingAdapter;
import org.fs.mvvm.common.AbstractRecyclerBindingHolder;
import org.fs.mvvm.data.PropertyInfo;
import org.fs.mvvm.listeners.SimpleRecyclerViewScrollListener;
import org.fs.mvvm.utils.Properties;

public final class RecyclerViewCompatBindingAdapter {

  private final static String BIND_ITEM_SOURCE        = "bindings:itemSource";
  private final static String BIND_LAYOUT_MANAGER     = "bindings:layoutManager";
  private final static String BIND_ITEM_ANIMATOR      = "bindings:itemAnimator";
  private final static String BIND_TOUCH_HELPER       = "bindings:touchHelper";

  //bind itemPosition two-way
  private final static String BIND_ITEM_POSITION              = "bindings:position";
  private final static String BIND_ITEM_POSITION_ATTR_CHANGED = "bindings:positionAttrChanged";

  //bind item two-way
  private final static String BIND_ITEM               = "bindings:item";
  private final static String BIND_ITEM_ATTR_CHANGED  = "bindings:itemAttrChanged";

  //bind item-positions two-way
  private final static String BIND_ITEM_POSITIONS               = "bindings:positions";
  private final static String BIND_ITEM_POSITIONS_ATTR_CHANGED  = "bindings:positionsAttrChanged";

  //bind items two-way
  private final static String BIND_ITEMS              = "bindings:items";
  private final static String BIND_ITEMS_ATTR_CHANGED = "bindings:itemsAttrChanged";

  //bind isLoading two-way
  private final static String BIND_IS_LOADING              = "bindings:isLoading";
  private final static String BIND_IS_LOADING_ATTR_CHANGED = "bindings:isLoadingAttrChanged";

  private RecyclerViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  @BindingAdapter({ BIND_ITEM_POSITION })
  public static void viewRecyclerRegisterItemPosition(RecyclerView viewRecycler, int position) {
    PropertyInfo<Integer> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedPosition);
    if (propertyInfo != null) {
      if (propertyInfo.getPropertyValue() != position) {
        propertyInfo = new PropertyInfo<>(position);
      }
    } else {
      propertyInfo = new PropertyInfo<>(position);
    }
    Properties.setPropertyInfo(viewRecycler, propertyInfo, R.id.viewRecycler_selectedPosition);
  }

  @InverseBindingAdapter(attribute = BIND_ITEM_POSITION,
      event = BIND_ITEM_POSITION_ATTR_CHANGED)
  public static int viewRecyclerRetrieveItemPosition(RecyclerView viewRecycler) {
    PropertyInfo<Integer> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedPosition);
    return propertyInfo != null ? propertyInfo.getPropertyValue() : -1;
  }

  @BindingAdapter({ BIND_ITEM })
  public static <D extends AbstractEntity> void viewRecyclerRegisterItem(RecyclerView viewRecycler, D item) {
    PropertyInfo<D> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedItem);
    if (propertyInfo != null) {
      if (item != propertyInfo.getPropertyValue()) {
        propertyInfo = new PropertyInfo<>(item);
      }
    } else {
      propertyInfo = new PropertyInfo<>(item);
    }
    Properties.setPropertyInfo(viewRecycler, propertyInfo, R.id.viewRecycler_selectedItem);
  }

  @InverseBindingAdapter(attribute = BIND_ITEM,
      event = BIND_ITEM_ATTR_CHANGED)
  public static <D extends AbstractEntity> D viewRecyclerRetrieveItem(RecyclerView viewRecycler) {
    PropertyInfo<D> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedItem);
    return propertyInfo != null ? propertyInfo.getPropertyValue() : null;
  }

  @BindingAdapter({ BIND_ITEM_POSITIONS })
  public static void viewRecyclerRegisterItemPositions(RecyclerView viewRecycler, Collection<Integer> positions) {
    PropertyInfo<Collection<Integer>> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedPositions);
    if (propertyInfo != null) {
      if (propertyInfo.getPropertyValue() != positions) {
        propertyInfo = new PropertyInfo<>(positions);
      }
    } else {
      propertyInfo = new PropertyInfo<>(positions);
    }
    Properties.setPropertyInfo(viewRecycler, propertyInfo, R.id.viewRecycler_selectedPositions);
  }

  @InverseBindingAdapter(attribute = BIND_ITEM_POSITIONS,
      event = BIND_ITEM_POSITIONS_ATTR_CHANGED)
  public static Collection<Integer> viewRecyclerRetrieveItemPositions(RecyclerView viewRecycler) {
    PropertyInfo<Collection<Integer>> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedPositions);
    return propertyInfo != null ? propertyInfo.getPropertyValue() : null;
  }

  @BindingAdapter({ BIND_ITEMS })
  public static <D extends AbstractEntity> void viewRecyclerRegisterItems(RecyclerView viewRecycler, Collection<D> items) {
    PropertyInfo<Collection<D>> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedItems);
    if (propertyInfo != null) {
      if (items != propertyInfo.getPropertyValue())  {
        propertyInfo = new PropertyInfo<>(items);
      }
    } else {
      propertyInfo = new PropertyInfo<>(items);
    }
    Properties.setPropertyInfo(viewRecycler, propertyInfo, R.id.viewRecycler_selectedItems);
  }

  @InverseBindingAdapter(attribute = BIND_ITEMS,
      event = BIND_ITEMS_ATTR_CHANGED)
  public static <D extends AbstractEntity> Collection<D> viewRecyclerRetreiveItems(RecyclerView viewRecycler) {
    PropertyInfo<Collection<D>> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_selectedItems);
    return propertyInfo != null  ? propertyInfo.getPropertyValue() : null;
  }

  @BindingAdapter({ BIND_IS_LOADING })
  public static void viewRecyclerRegisterIsLoading(RecyclerView viewRecycler, boolean isLoading) {
    PropertyInfo<Boolean> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_isLoadMore);
    if (propertyInfo != null) {
      if(propertyInfo.getPropertyValue() != isLoading) {
        propertyInfo = new PropertyInfo<>(isLoading);
      }
    } else {
      propertyInfo = new PropertyInfo<>(isLoading);
    }
    Properties.setPropertyInfo(viewRecycler, propertyInfo, R.id.viewRecycler_isLoadMore);
  }

  @InverseBindingAdapter(attribute = BIND_IS_LOADING,
      event = BIND_IS_LOADING_ATTR_CHANGED)
  public static boolean viewRecyclerRetreiveIsLoading(RecyclerView viewRecycler) {
    PropertyInfo<Boolean> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_isLoadMore);
    return propertyInfo != null ? propertyInfo.getPropertyValue() : false;
  }

  @BindingAdapter({ BIND_TOUCH_HELPER })
  public static void viewRecyclerAttachItemTouchHelper(RecyclerView viewRecycler, ItemTouchHelper itemTouchHelper) {
    if (itemTouchHelper != null) {
      itemTouchHelper.attachToRecyclerView(viewRecycler);
    }
  }

  @BindingAdapter({ BIND_ITEM_ANIMATOR })
  public static void viewRecyclerSetItemAnimator(RecyclerView viewRecycler, RecyclerView.ItemAnimator itemAnimator) {
    if (itemAnimator != null) {
      viewRecycler.setItemAnimator(itemAnimator);
    }
  }

  @BindingAdapter(
      value = {
        BIND_LAYOUT_MANAGER,
        BIND_IS_LOADING_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewRecyclerSetLayoutManager(RecyclerView viewRecycler, RecyclerView.LayoutManager layoutManager, InverseBindingListener isLoadingAttrChanged) {
    if (layoutManager != null) {
      viewRecycler.setLayoutManager(layoutManager);
      viewRecycler.setHasFixedSize(true);
    }
    final RecyclerView.OnScrollListener newScrollListener;
    if (isLoadingAttrChanged == null) {
      newScrollListener = null;
    } else {
      newScrollListener = new SimpleRecyclerViewScrollListener() {
        private final static int VISIBLE_THRESHOLD = 5;
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          if (layoutManager != null) {//we only listen it if it is LinearLayoutManager
            if (layoutManager instanceof LinearLayoutManager) {
              LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
              int total = linearLayoutManager.getItemCount();
              int last = linearLayoutManager.findLastVisibleItemPosition();
              PropertyInfo<Boolean> propertyInfo = Properties.getPropertyInfo(viewRecycler, R.id.viewRecycler_isLoadMore);
              boolean isLoading = propertyInfo != null ? propertyInfo.getPropertyValue() : false;
              if (!isLoading && total <= (last + VISIBLE_THRESHOLD)) {
                Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(true), R.id.viewRecycler_isLoadMore);
                isLoadingAttrChanged.onChange();
              } else {
                if (isLoading) {
                  Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(false), R.id.viewList_isLoadMore);
                  isLoadingAttrChanged.onChange();
                }
              }
            }
          }
        }
      };
    }
    final RecyclerView.OnScrollListener oldScrollListener = ListenerUtil.trackListener(viewRecycler, newScrollListener, R.id.recyclerScrollListener);
    if (oldScrollListener != null) {
      viewRecycler.removeOnScrollListener(oldScrollListener);
    }
    if (newScrollListener != null) {
      viewRecycler.addOnScrollListener(newScrollListener);
    }
  }
  
  @BindingAdapter(
    value = {
        BIND_ITEM_SOURCE,
        BIND_ITEM_POSITION_ATTR_CHANGED,
        BIND_ITEM_ATTR_CHANGED,
        BIND_ITEM_POSITIONS_ATTR_CHANGED,
        BIND_ITEMS_ATTR_CHANGED
    },
    requireAll = false
  )
  public static <T extends AbstractRecyclerBindingAdapter<D, V>, D extends BaseObservable, V extends AbstractRecyclerBindingHolder<D>>
    void viewRecyclerRegisterAdapter(RecyclerView viewRecycler, T itemSource,
      InverseBindingListener positionAttrChanged, InverseBindingListener itemAttrChanged,
      InverseBindingListener positionsAttrChanged, InverseBindingListener itemsAttrChanged) {
    //do not throw error if adapter is null, just ignore
      if (itemSource != null) {
        //singleMode
        if (itemSource.isSingleMode()) {
          itemSource.setSingleItemCallback((item) -> {
            Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(item), R.id.viewRecycler_selectedItem);
            if (itemAttrChanged != null) {
              itemAttrChanged.onChange();
            }
          });
          itemSource.setSinglePositionCallback((position) -> {
            Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(position), R.id.viewRecycler_selectedPosition);
            if (positionAttrChanged != null) {
              positionAttrChanged.onChange();
            }
          });
        } else {//multiMode
          itemSource.setMultiItemCallback((items) -> {
            Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(items), R.id.viewRecycler_selectedItems);
            if (itemsAttrChanged != null) {
              itemsAttrChanged.onChange();
            }
          });
          itemSource.setMultiPositionCallback((positions) -> {
            Properties.setPropertyInfo(viewRecycler, new PropertyInfo<>(positions), R.id.viewRecycler_selectedPositions);
            if (positionsAttrChanged != null) {
              positionsAttrChanged.onChange();
            }
          });
        }
        viewRecycler.setAdapter(itemSource);
      } else {
        viewRecycler.setAdapter(null);
      }
  }
}
