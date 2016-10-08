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
import android.support.design.widget.AppBarLayout;
import org.fs.mvvm.R;
import org.fs.mvvm.data.PropertyInfo;
import org.fs.mvvm.utils.Properties;

public class AppBarLayoutCompatBindingAdapter {

  private final static String BIND_ON_OFFSET_CHANGE = "bindings:onOffsetChanged";

  private final static String BIND_OFFSET = "bindings:offset";
  private final static String BIND_OFFSET_ATTR_CHANGED = "bindings:offsetAttrChanged";

  private AppBarLayoutCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @BindingAdapter({ BIND_OFFSET })
  public static void viewAppBarLayoutRegisterOffset(AppBarLayout viewAppBarLayout, int offset) {
    PropertyInfo<Integer> propertyInfo = Properties.getPropertyInfo(viewAppBarLayout, R.id.viewAppBarLayout_offset);
    if (propertyInfo != null) {
      if (propertyInfo.getPropertyValue() != offset) {
        propertyInfo = new PropertyInfo<>(offset);
      }
    } else {
      propertyInfo = new PropertyInfo<>(offset);
    }
    Properties.setPropertyInfo(viewAppBarLayout, propertyInfo, R.id.viewAppBarLayout_offset);
  }

  @InverseBindingAdapter(attribute = BIND_OFFSET,
      event = BIND_OFFSET_ATTR_CHANGED)
  public static int viewAppBarLayoutRetreiveOffset(AppBarLayout viewAppBarLayout) {
    PropertyInfo<Integer> propertyInfo = Properties.getPropertyInfo(viewAppBarLayout, R.id.viewAppBarLayout_offset);
    return propertyInfo != null ? propertyInfo.getPropertyValue() : -1;
  }

  @BindingAdapter(
      value = {
          BIND_ON_OFFSET_CHANGE,
          BIND_OFFSET_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewAppBarLayoutRegisterOffsetListener(AppBarLayout viewAppBarLayout,
      AppBarLayout.OnOffsetChangedListener offsetListener, InverseBindingListener offsetAttrChanged) {
    final AppBarLayout.OnOffsetChangedListener newListener;
    if (offsetAttrChanged == null && offsetListener == null) {
      newListener = null;
    } else {
      newListener = (appBarLayout, verticalOffset) -> {
        Properties.setPropertyInfo(appBarLayout, new PropertyInfo<>(verticalOffset), R.id.viewAppBarLayout_offset);
        if (offsetListener != null) {
          offsetListener.onOffsetChanged(appBarLayout, verticalOffset);
        }
        if (offsetAttrChanged != null) {
          offsetAttrChanged.onChange();
        }
      };
    }
    final AppBarLayout.OnOffsetChangedListener oldListener = ListenerUtil.trackListener(viewAppBarLayout, newListener, R.id.offsetChangeListener);
    if (oldListener != null) {
      viewAppBarLayout.removeOnOffsetChangedListener(oldListener);
    }
    if (newListener != null) {
      viewAppBarLayout.addOnOffsetChangedListener(newListener);
    }
  }
}
