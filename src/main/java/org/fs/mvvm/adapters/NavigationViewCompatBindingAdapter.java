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
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import java8.util.stream.IntStreams;
import org.fs.mvvm.listeners.OnNavigationSelected;

public class NavigationViewCompatBindingAdapter {

  private final static int NO_ID = -1;

  private final static String ANDROID_ON_NAVIGATION_SELECTED = "android:onNavigationSelected";

  private final static String ANDROID_SELECTED_ITEM = "android:selectedItem";

  private NavigationViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  /**
   * Gets selected id of viewNavigation
   *
   * @param viewNavigation viewNavigation instance
   * @return id of selection else -1
   */
  @InverseBindingAdapter(
      event = ANDROID_ON_NAVIGATION_SELECTED,
      attribute = ANDROID_SELECTED_ITEM
  )
  @IdRes public int provideSelectedItem(NavigationView viewNavigation) {
    final int size = viewNavigation.getMenu().size();
    return IntStreams.range(0, size)
        .mapToObj(viewNavigation.getMenu()::getItem)
        .filter(MenuItem::isChecked)
        .mapToInt(MenuItem::getItemId)
        .findFirst()
        .orElse(NO_ID);
  }

  /**
   * Sets selected item id on viewNavigation menu
   *
   * @param viewNavigation viewNavigation instance
   * @param selectedId selected id of menu
   */
  @BindingAdapter(
      ANDROID_SELECTED_ITEM
  )
  public void setSelectedItem(NavigationView viewNavigation, @IdRes int selectedId) {
    viewNavigation.setCheckedItem(selectedId);
  }

  /**
   * Registers OnNavigationSelected listener on NavigationView
   *
   * @param viewNavigation viewNavigation instance
   * @param selectedListener listener to track changes on view
   * @param selectedItemAttrChanged attr notifier for two-way bindings
   */
  @BindingAdapter(
      value = {
          ANDROID_SELECTED_ITEM,
          ANDROID_ON_NAVIGATION_SELECTED
      },
      requireAll = false
  )
  public static void registerNavigationSelectedListener(NavigationView viewNavigation, OnNavigationSelected selectedListener, InverseBindingListener selectedItemAttrChanged) {
    if (selectedListener == null && selectedItemAttrChanged == null) {
      viewNavigation.setNavigationItemSelectedListener(null);
    } else {
      viewNavigation.setNavigationItemSelectedListener(item -> {
        if (selectedItemAttrChanged != null) {
          selectedItemAttrChanged.onChange();
        }
        if (selectedListener != null) {
          return selectedListener.onNavigationSelected(item);
        }
        return false;
      });
    }
  }
}
