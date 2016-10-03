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
import org.fs.mvvm.R;
import org.fs.mvvm.listeners.OnPageScrollStateChanged;
import org.fs.mvvm.listeners.OnPageScrolled;
import org.fs.mvvm.listeners.OnPageSelected;

public final class ViewPagerCompatBindingAdapter {

  private final static String ANDROID_ITEM_SOURCE = "android:itemSource";

  private final static String ANDROID_PAGE_ANIMATOR = "android:pageAnimator";

  private final static String ANDROID_PAGE_SCROLLED = "android:onPageScrolled";
  private final static String ANDROID_PAGE_SELECTED = "android:onPageSelected";
  private final static String ANDROID_PAGE_SCROLL_STATE_CHANGED = "android:onPageScrollStateChanged";

  private final static String ANDROID_SELECTED_PAGE = "android:selectedPage";
  private final static String ANDROID_SELECTED_PAGE_ATTR_CHANGED = "android:selectedPageAttrChanged";

  private ViewPagerCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Two-way binding for selectedPage
   *
   * @param viewPager viewPager
   * @return currentPage
   */
  @InverseBindingAdapter(
      event = ANDROID_SELECTED_PAGE_ATTR_CHANGED,
      attribute = ANDROID_SELECTED_PAGE
  )
  public int provideSelectedPage(ViewPager viewPager) {
    return viewPager.getCurrentItem();
  }

  /**
   * Registers position provided for viewPager
   *
   * @param viewPager viewPager we register this, two-way
   * @param position viewPager position
   */
  @BindingAdapter({
      ANDROID_SELECTED_PAGE
  })
  public static void registerSelectedPage(ViewPager viewPager, int position) {
    if (viewPager.getCurrentItem() != position) {
      viewPager.setCurrentItem(position, true);//do smooth scroll
    }
  }

  /**
   * Registers OnPageScrolled, OnPageSelected or OnPageScrollStateChanged on ViewPager
   * by single or multiple with any combination defined by user needs.
   *
   * @param viewPager viewPager to bind listeners.
   * @param pageScrolled pageScrolled listener to bind viewPager.
   * @param pageSelected pageSelected listener to bind viewPager.
   * @param pageScrollStateChanged pageScrollStateChanged listener to bind viewPager
   */
  @BindingAdapter(
      value = {
        ANDROID_PAGE_SCROLLED,
        ANDROID_PAGE_SELECTED,
        ANDROID_PAGE_SCROLL_STATE_CHANGED,
        ANDROID_SELECTED_PAGE_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void registerPageListener(ViewPager viewPager, OnPageScrolled pageScrolled,
      OnPageSelected pageSelected, OnPageScrollStateChanged pageScrollStateChanged, InverseBindingListener selectedPageAttrChanged) {
    final ViewPager.OnPageChangeListener newListener;
    if (pageScrolled == null && pageSelected == null && pageScrollStateChanged == null && selectedPageAttrChanged == null) {
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

  /**
   * Registers an ViewPager.PageTransformer on ViewPager
   *
   * @param viewPager viewPager to bind pagerAnimation
   * @param pageAnimator pagerAnimation to bind viewPager
   */
  @BindingAdapter(
      ANDROID_PAGE_ANIMATOR
  )
  public static void registerPageAnimator(ViewPager viewPager, ViewPager.PageTransformer pageAnimator) {
    if (pageAnimator == null) {
      viewPager.setPageTransformer(false, null);
    } else {
      viewPager.setPageTransformer(false, pageAnimator);
    }
  }

  /**
   * Registers an PagerAdapter instance with ViewPager
   *
   * @param viewPager viewPager to bind adapter
   * @param adapter adapter to bind viewPager
   */
  @BindingAdapter(
      ANDROID_ITEM_SOURCE
  )
  public static void registerPagerAdapter(ViewPager viewPager, PagerAdapter adapter) {
    if (adapter != null) {
      viewPager.setAdapter(adapter);
    }
  }
}
