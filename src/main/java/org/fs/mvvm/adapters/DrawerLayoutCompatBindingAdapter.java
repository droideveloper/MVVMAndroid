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
import org.fs.mvvm.BuildConfig;
import org.fs.mvvm.R;
import org.fs.mvvm.listeners.OnLayoutOpenedOrClosed;
import org.fs.mvvm.listeners.OnLayoutSlided;
import org.fs.mvvm.listeners.OnLayoutStateChanged;

public class DrawerLayoutCompatBindingAdapter {

  private final static String BIND_ON_SLIDED         = "bindings:onSlided";
  private final static String BIND_ON_OPEN_OR_CLOSE  = "bindings:onOpenOrClose";
  private final static String BIND_ON_STATE_CHANGE   = "bindings:onStateChange";

  private final static String BIND_IS_OPEN              = "bindings:isOpen";
  private final static String BIND_IS_OPEN_ATTR_CHANGED = "bindings:isOpenAttrChanged";

  private DrawerLayoutCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @BindingAdapter({ BIND_IS_OPEN })
  public static void viewDrawerLayoutRegisterIsOpen(DrawerLayout viewDrawerLayout, boolean isOpen) {
    if (isDrawerOpen(viewDrawerLayout) && !isOpen) {
      closeDrawer(viewDrawerLayout);
    } else if (!isDrawerOpen(viewDrawerLayout) && isOpen) {
      openDrawer(viewDrawerLayout);
    }
  }

  @InverseBindingAdapter(attribute = BIND_IS_OPEN,
      event = BIND_IS_OPEN_ATTR_CHANGED)
  public static boolean viewDrawerLayoutRetreiveIsOpen(DrawerLayout viewDrawerLayout) {
    return isDrawerOpen(viewDrawerLayout);
  }

  @BindingAdapter(
      value = {
          BIND_ON_SLIDED,
          BIND_ON_OPEN_OR_CLOSE,
          BIND_ON_STATE_CHANGE,
          BIND_IS_OPEN_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewDrawerLayoutRegisterListeners(DrawerLayout viewDrawerLayout,
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
    //have to do it because they do not provide gravity that is gay
    try {
      viewDrawerLayout.closeDrawer(GravityCompat.START);
    } catch (IllegalArgumentException ignored) {
      if (BuildConfig.DEBUG) {
        ignored.printStackTrace();
      }
    }
    try {
      viewDrawerLayout.closeDrawer(GravityCompat.END);
    } catch (IllegalArgumentException ignored) {
      if (BuildConfig.DEBUG) {
        ignored.printStackTrace();
      }
    }
  }

  private static void openDrawer(DrawerLayout viewDrawerLayout) {
    try {
      viewDrawerLayout.openDrawer(GravityCompat.START);
    } catch (IllegalArgumentException ignored) {
      if (BuildConfig.DEBUG) {
        ignored.printStackTrace();
      }
    }
    try {
      viewDrawerLayout.openDrawer(GravityCompat.END);
    } catch (IllegalArgumentException ignored) {
      if (BuildConfig.DEBUG) {
        ignored.printStackTrace();
      }
    }
  }
}
