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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import org.fs.mvvm.listeners.OnNavigationSelected;

public class NavigationViewCompatBindingAdapter {

  private final static int    NO_ID = -1;
  private final static String BIND_ON_NAVIGATION_SELECTED = "bindings:onNavigationSelected";

  private final static String BIND_MENU_ITEM              = "bindings:menuItem";
  private final static String BIND_MENU_ITEM_ATTR_CHANGED = "bindings:menuItemAttrChanged";

  private NavigationViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @InverseBindingAdapter(attribute = BIND_MENU_ITEM,
      event = BIND_MENU_ITEM_ATTR_CHANGED)
  public static int viewNavigationRetreiveMenuItem(NavigationView viewNavigation) {
    final int size = viewNavigation.getMenu().size();
    final Menu menu = viewNavigation.getMenu();
    for (int i = 0; i < size; i++) {
      MenuItem item = menu.getItem(i);
      if (item.isChecked()) {
        return item.getItemId();
      }
    }
    return NO_ID;
  }

  @BindingAdapter({ BIND_MENU_ITEM })
  public static void viewNavigationRegisterMenuItem(NavigationView viewNavigation, int selectedId) {
    if (selectedId > 0) {
      viewNavigation.setCheckedItem(selectedId);
    }
  }

  @BindingAdapter(
      value = {
          BIND_ON_NAVIGATION_SELECTED,
          BIND_MENU_ITEM_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewNavigationRegisterSelectionListener(NavigationView viewNavigation,
      final OnNavigationSelected selectedListener, final InverseBindingListener menuItemAttrChanged) {
    if (selectedListener == null && menuItemAttrChanged == null) {
      viewNavigation.setNavigationItemSelectedListener(null);
    } else {
      viewNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          if (menuItemAttrChanged != null) {
            menuItemAttrChanged.onChange();
          }
          if (selectedListener != null) {
            return selectedListener.onNavigationSelected(item);
          }
          return false;
        }
      });
    }
  }
}
