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
import android.databinding.adapters.ListenerUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import java.util.Locale;
import org.fs.mvvm.R;
import org.fs.mvvm.common.AbstractEntity;
import org.fs.mvvm.common.AbstractPagerBindingAdapter;
import org.fs.mvvm.common.AbstractPagerStateBindingAdapter;
import org.fs.mvvm.common.PagerBindingAdapter;
import org.fs.mvvm.data.PropertyInfo;
import org.fs.mvvm.listeners.OnPageScrollStateChanged;
import org.fs.mvvm.listeners.OnPageScrolled;
import org.fs.mvvm.listeners.OnPageSelected;
import org.fs.mvvm.utils.Properties;

public final class ViewPagerCompatBindingAdapter {

  private final static String BIND_ITEM_SOURCE    = "bindings:itemSource";
  private final static String BIND_PAGE_ANIMATOR  = "bindings:pageAnimator";

  private final static String BIND_PAGE_SCROLLED              = "bindings:onPageScrolled";
  private final static String BIND_PAGE_SELECTED              = "bindings:onPageSelected";
  private final static String BIND_PAGE_SCROLL_STATE_CHANGED  = "bindings:onPageScrollStateChanged";

  private final static String BIND_SELECTED_PAGE              = "bindings:selectedPage";
  private final static String BIND_SELECTED_PAGE_ATTR_CHANGED = "bindings:selectedPageAttrChanged";

  private final static String BIND_SELECTED_ITEM              = "bindings:item";
  private final static String BIND_SELECTED_ITEM_ATTR_CHANGED = "bindings:itemAttrChanged";

  private ViewPagerCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  @InverseBindingAdapter(attribute = BIND_SELECTED_PAGE,
      event = BIND_SELECTED_PAGE_ATTR_CHANGED
  )
  public int viewPagerProvideSelectedPage(ViewPager viewPager) {
    return viewPager.getCurrentItem();
  }

   @BindingAdapter({ BIND_SELECTED_PAGE })
  public static void viewPagerRegisterSelectedPage(ViewPager viewPager, int position) {
    if (viewPager.getCurrentItem() != position) {
      viewPager.setCurrentItem(position, true);//do smooth scroll
    }
  }

  @InverseBindingAdapter(attribute = BIND_SELECTED_ITEM,
      event = BIND_SELECTED_ITEM_ATTR_CHANGED
  )
  public static <D extends AbstractEntity> D viewPagerRetreiveItem(ViewPager viewPager) {
    PropertyInfo<D> propertyInfo = Properties.getPropertyInfo(viewPager, R.id.viewPager_selectedItem);
    return propertyInfo != null ? propertyInfo.getPropertyValue() : null;
  }

  @BindingAdapter({ BIND_SELECTED_ITEM })
  public static <D extends AbstractEntity> void viewPagerRegisterItem(ViewPager viewPager, D item) {
    PropertyInfo<D> propertyInfo = Properties.getPropertyInfo(viewPager, R.id.viewPager_selectedItem);
    if (propertyInfo != null) {
      if (propertyInfo.getPropertyValue() != item) {
        propertyInfo = new PropertyInfo<>(item);
      }
    } else {
      propertyInfo = new PropertyInfo<>(item);
    }
    Properties.setPropertyInfo(viewPager, propertyInfo, R.id.viewPager_selectedItem);
  }

  @BindingAdapter(
      value = {
        BIND_PAGE_SCROLLED,
        BIND_PAGE_SELECTED,
        BIND_PAGE_SCROLL_STATE_CHANGED,
        BIND_SELECTED_PAGE_ATTR_CHANGED,
        BIND_SELECTED_ITEM_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewPagerRegisterListeners(ViewPager viewPager, OnPageScrolled pageScrolled,
      OnPageSelected pageSelected, OnPageScrollStateChanged pageScrollStateChanged,
      InverseBindingListener selectedPageAttrChanged, InverseBindingListener itemAttrChanged) {
    final ViewPager.OnPageChangeListener newListener;
    if (pageScrolled == null && pageSelected == null && pageScrollStateChanged == null
        && selectedPageAttrChanged == null && itemAttrChanged == null) {
      newListener = null;
    } else {
      newListener = new ViewPager.OnPageChangeListener() {
        @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
          if (pageScrolled != null) {
            pageScrolled.onPageScrolled(position, positionOffset, positionOffsetPixels);
          }
        }

        @Override public void onPageSelected(int position) {
          if(pageSelected != null) {
            pageSelected.onPageSelected(position);
          }
          if (selectedPageAttrChanged != null) {
            selectedPageAttrChanged.onChange();
          }
          //notify item selection change if our adapters are in spot
          final PagerAdapter pagerAdapter = viewPager.getAdapter();
          if (pagerAdapter instanceof AbstractPagerBindingAdapter<?>) {
            AbstractPagerBindingAdapter<?> typedAdapter = (AbstractPagerBindingAdapter<?>) pagerAdapter;
            Properties.setPropertyInfo(viewPager, new PropertyInfo<>(typedAdapter.getItemAt(position)), R.id.viewPager_selectedItem);
            if (itemAttrChanged != null) {
              itemAttrChanged.onChange();
            }
          } else if (pagerAdapter instanceof AbstractPagerStateBindingAdapter<?>) {
            AbstractPagerStateBindingAdapter<?> typedAdapter = (AbstractPagerStateBindingAdapter<?>) pagerAdapter;
            Properties.setPropertyInfo(viewPager, new PropertyInfo<>(typedAdapter.getItemAt(position)), R.id.viewPager_selectedItem);
            if (itemAttrChanged != null) {
              itemAttrChanged.onChange();
            }
          } else if (pagerAdapter instanceof PagerBindingAdapter<?, ?>) {
            PagerBindingAdapter<?, ?> typedAdapter = (PagerBindingAdapter<?, ?>) pagerAdapter;
            Properties.setPropertyInfo(viewPager, new PropertyInfo<>(typedAdapter.getItemAt(position)), R.id.viewPager_selectedItem);
            if (itemAttrChanged != null) {
              itemAttrChanged.onChange();
            }
          } else {
            Log.e(ViewPagerCompatBindingAdapter.class.getSimpleName(),
                String.format(Locale.ENGLISH,
                    "if you want to register selectedItem with your viewModel you should extend one of the pagerAdapterAbstractions\n%s\nor\n%s.",
                    AbstractPagerBindingAdapter.class.getName(), AbstractPagerStateBindingAdapter.class.getName())
            );
          }
        }

        @Override public void onPageScrollStateChanged(int state) {
          if (pageScrollStateChanged != null) {
            pageScrollStateChanged.onPageScrollStateChanged(state);
          }
        }
      };
    }
    final ViewPager.OnPageChangeListener oldListener = ListenerUtil.trackListener(viewPager,
        newListener, R.id.onPageChangeListener);
    if (oldListener != null) {
      viewPager.removeOnPageChangeListener(oldListener);
    }
    if (newListener != null) {
      viewPager.addOnPageChangeListener(newListener);
    }
  }


  @BindingAdapter({ BIND_PAGE_ANIMATOR })
  public static void viewPagerRegisterPageAnimator(ViewPager viewPager, ViewPager.PageTransformer pageAnimator) {
    if (pageAnimator == null) {
      viewPager.setPageTransformer(false, null);
    } else {
      viewPager.setPageTransformer(false, pageAnimator);
    }
  }

  @BindingAdapter({ BIND_ITEM_SOURCE })
  public static void viewPagerRegisterItemSource(ViewPager viewPager, PagerAdapter itemSource) {
    if (itemSource != null) {
      viewPager.setAdapter(itemSource);
    } else {
      viewPager.setAdapter(null);
    }
  }
}
