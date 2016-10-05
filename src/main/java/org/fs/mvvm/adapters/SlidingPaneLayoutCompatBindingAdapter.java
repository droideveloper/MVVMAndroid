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

  private final static String BIND_ON_SLIDED         = "bindings:onSlided";
  private final static String BIND_ON_OPEN_OR_CLOSE  = "bindings:onOpenOrClose";

  private final static String BIND_IS_OPEN              = "bindings:isOpen";
  private final static String BIND_IS_OPEN_ATTR_CHANGED = "bindings:isOpenAttrChanged";

  private SlidingPaneLayoutCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @BindingAdapter({ BIND_IS_OPEN })
  public static void viewSlidingPaneLayoutRegisterIsOpen(SlidingPaneLayout viewSlidingPaneLayout, boolean isOpen) {
    if (viewSlidingPaneLayout.isOpen() != isOpen) {
      if (isOpen) {
        viewSlidingPaneLayout.closePane();
      } else {
        viewSlidingPaneLayout.openPane();
      }
    }
  }

  @InverseBindingAdapter(attribute = BIND_IS_OPEN,
      event = BIND_IS_OPEN_ATTR_CHANGED)
  public static boolean viewSlidingPaneLayoutRetreiveIsOpen(SlidingPaneLayout viewSlidingPaneLayout) {
    return viewSlidingPaneLayout.isOpen();
  }

  @BindingAdapter(
      value = {
          BIND_ON_SLIDED,
          BIND_ON_OPEN_OR_CLOSE,
          BIND_IS_OPEN_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewSlidingPaneLayoutRegisterListeners(SlidingPaneLayout viewSlidingPaneLayout, OnLayoutSlided slided,
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
