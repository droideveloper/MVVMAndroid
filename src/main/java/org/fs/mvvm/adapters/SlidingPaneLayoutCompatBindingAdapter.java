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
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import org.fs.mvvm.listeners.OnLayoutOpenedOrClosed;
import org.fs.mvvm.listeners.OnLayoutSlided;

public class SlidingPaneLayoutCompatBindingAdapter {

  private final static String ANDROID_ON_SLIDED         = "android:onSlided";
  private final static String ANDROID_ON_OPEN_OR_CLOSE  = "android:onOpenOrClose";

  private final static String ANDROID_IS_OPEN = "android:isOpen";
  private final static String ANDROID_IS_OPEN_ATTR_CHANGED = "android:isOpenAttrChanged";

  private SlidingPaneLayoutCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  /**
   * Sets if viewSlidingPageLayout is open or close
   *
   * @param viewSlidingPaneLayout SlidingPaneLayout instance
   * @param isOpen true or false
   */
  @BindingAdapter(
      ANDROID_IS_OPEN
  )
  public static void setOpenOrClose(SlidingPaneLayout viewSlidingPaneLayout, boolean isOpen) {
    if (viewSlidingPaneLayout.isOpen() != isOpen) {
      if (isOpen) {
        viewSlidingPaneLayout.closePane();
      } else {
        viewSlidingPaneLayout.openPane();
      }
    }
  }

  /**
   * Gets if viewSlidingPageLayout is open or not
   *
   * @param viewSlidingPaneLayout SlidingPaneLayout instance
   * @return true or false
   */
  @InverseBindingAdapter(
      event = ANDROID_IS_OPEN_ATTR_CHANGED,
      attribute = ANDROID_IS_OPEN
  )
  public static boolean provideIsOpen(SlidingPaneLayout viewSlidingPaneLayout) {
    return viewSlidingPaneLayout.isOpen();
  }

  /**
   * Registers listeners for slide, openOrClose and attrChange states
   *
   * @param viewSlidingPaneLayout SlidingPaneLayout instance
   * @param slided slided listener
   * @param openOrClose openOrClose listener
   * @param isOpenAttrChanged attrChanged listener
   */
  @BindingAdapter(
      value = {
          ANDROID_ON_SLIDED,
          ANDROID_ON_OPEN_OR_CLOSE,
          ANDROID_IS_OPEN_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void registerSlidingPaneLayoutListener(SlidingPaneLayout viewSlidingPaneLayout, OnLayoutSlided slided,
      OnLayoutOpenedOrClosed openOrClose, InverseBindingListener isOpenAttrChanged) {
    if (slided == null && openOrClose == null && isOpenAttrChanged == null) {
      viewSlidingPaneLayout.setPanelSlideListener(null);
    } else {
      viewSlidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
        @Override public void onPanelSlide(View panel, float slideOffset) {
          if (slided != null) {
            slided.onLayoutSlided(panel, slideOffset);
          }
        }

        @Override public void onPanelOpened(View panel) {
          if (openOrClose != null) {
            openOrClose.onLayoutOpenedOrClosed(true);
          }
          if (isOpenAttrChanged != null) {
            isOpenAttrChanged.onChange();
          }
        }

        @Override public void onPanelClosed(View panel) {
          if (openOrClose != null) {
            openOrClose.onLayoutOpenedOrClosed(false);
          }
          if (isOpenAttrChanged != null) {
            isOpenAttrChanged.onChange();
          }
        }
      });
    }
  }
}
