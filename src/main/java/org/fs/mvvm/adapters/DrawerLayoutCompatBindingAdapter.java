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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import org.fs.mvvm.R;
import org.fs.mvvm.listeners.OnLayoutOpenedOrClosed;
import org.fs.mvvm.listeners.OnLayoutSlided;
import org.fs.mvvm.listeners.OnLayoutStateChanged;

public class DrawerLayoutCompatBindingAdapter {

  private final static String ANDROID_ON_SLIDED         = "android:onSlided";
  private final static String ANDROID_ON_OPEN_OR_CLOSE  = "android:onOpenOrClose";
  private final static String ANDROID_ON_STATE_CHANGE   = "android:onStateChange";

  private final static String ANDROID_IS_OPEN = "android:isOpen";
  private final static String ANDROID_IS_OPEN_ATTR_CHANGED = "android:isOpenAttrChanged";

  private DrawerLayoutCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  /**
   * Setter of value for isOpen
   *
   * @param viewDrawerLayout DrawerLayout instance
   * @param isOpen true or false depending on desired state
   */
  @BindingAdapter({
      ANDROID_IS_OPEN
  })
  public static void setDrawerIsOpen(DrawerLayout viewDrawerLayout, boolean isOpen) {
    if (isDrawerOpen(viewDrawerLayout) && isOpen) {
      closeDrawer(viewDrawerLayout);
    } else if (!isDrawerOpen(viewDrawerLayout) && !isOpen) {
      openDrawer(viewDrawerLayout);
    }
  }

  /**
   * Getter of value for isOpen
   *
   * @param viewDrawerLayout DrawerLayout instance
   * @return true or false depending on open or close
   */
  @InverseBindingAdapter(
      event = ANDROID_IS_OPEN_ATTR_CHANGED,
      attribute = ANDROID_IS_OPEN
  )
  public static boolean provideDrawerIsOpen(DrawerLayout viewDrawerLayout) {
    return isDrawerOpen(viewDrawerLayout);
  }

  /**
   * Drawer layout listener split into 3 slided, openOrClose and stateChanged also
   * tracking view openOrClose for two-way binding
   *
   * @param viewDrawerLayout DrawerLayout instance
   * @param slided slided callback
   * @param openOrClose openOrClose callback
   * @param stateChanged stateChanged callback
   * @param isOpenAttrChanged attr changed two-way callback
   */
  @BindingAdapter(
      value = {
          ANDROID_ON_SLIDED,
          ANDROID_ON_OPEN_OR_CLOSE,
          ANDROID_ON_STATE_CHANGE,
          ANDROID_IS_OPEN_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void registerDrawerLayoutListener(DrawerLayout viewDrawerLayout,
      OnLayoutSlided slided, OnLayoutOpenedOrClosed openOrClose,
      OnLayoutStateChanged stateChanged, InverseBindingListener isOpenAttrChanged) {

    final DrawerLayout.DrawerListener newListener;
    if (slided == null && openOrClose == null && stateChanged == null && isOpenAttrChanged == null) {
      newListener = null;
    } else {
      newListener = new DrawerLayout.SimpleDrawerListener() {
        @Override public void onDrawerSlide(View drawerView, float slideOffset) {
          if (slided != null) {
            slided.onLayoutSlided(drawerView, slideOffset);
          }
        }

        @Override public void onDrawerOpened(View drawerView) {
          if (openOrClose != null) {
            openOrClose.onLayoutOpenedOrClosed(true);
          }
          if (isOpenAttrChanged != null) {
            isOpenAttrChanged.onChange();
          }
        }

        @Override public void onDrawerClosed(View drawerView) {
          if (openOrClose != null) {
            openOrClose.onLayoutOpenedOrClosed(false);
          }
          if (isOpenAttrChanged != null) {
            isOpenAttrChanged.onChange();
          }
        }

        @Override public void onDrawerStateChanged(int newState) {
          if (stateChanged != null) {
            stateChanged.onLayoutStateChanged(newState);
          }
        }
      };
    }
    DrawerLayout.DrawerListener oldListener
        = ListenerUtil.trackListener(viewDrawerLayout, newListener, R.id.drawerListener);
    if (oldListener != null) {
      viewDrawerLayout.removeDrawerListener(oldListener);
    }
    if (newListener != null) {
      viewDrawerLayout.addDrawerListener(newListener);
    }
  }

  private static boolean isDrawerOpen(DrawerLayout viewDrawerLayout) {
    return viewDrawerLayout.isDrawerOpen(GravityCompat.START) || viewDrawerLayout.isDrawerOpen(GravityCompat.END);
  }

  private static void closeDrawer(DrawerLayout viewDrawerLayout) {
    viewDrawerLayout.closeDrawer(GravityCompat.START);
    viewDrawerLayout.closeDrawer(GravityCompat.END);
  }

  private static void openDrawer(DrawerLayout viewDrawerLayout) {
    viewDrawerLayout.openDrawer(GravityCompat.START);
    viewDrawerLayout.openDrawer(GravityCompat.END);
  }
}
