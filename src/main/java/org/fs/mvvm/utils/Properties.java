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
package org.fs.mvvm.utils;

import android.util.SparseArray;
import android.view.View;
import java.util.WeakHashMap;
import org.fs.mvvm.data.PropertyInfo;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

public class Properties {

  private final static SparseArray<WeakHashMap<View, PropertyInfo<?>>> sProperties = new SparseArray<>();

  /**
   * Gets property on view reference with provides ids on view using pre defined ids are advised.
   *
   * @param view view instance we store those values
   * @param propertyId pre defined property id
   * @param <T> type of property value
   * @return propertyInfo previously changed or stored
   */
  public static <T> PropertyInfo<T> getPropertyInfo(View view, int propertyId) {
    if (requireSupportCompat()) {
      synchronized (sProperties) {
        WeakHashMap<View, PropertyInfo<?>> properties = sProperties.get(propertyId);
        if (properties == null) {
          return null;
        }
        final PropertyInfo<?> propertyInfo = properties.get(view);
        if (propertyInfo == null) {
          return null;
        } else {
          return Objects.toObject(propertyInfo);
        }
      }
    } else {
      Object propertyInfo = view.getTag(propertyId);
      if (propertyInfo != null) {
        return Objects.toObject(propertyInfo);
      } else {
        return null;
      }
    }
  }

  /**
   * Sets property on view reference with provided ids on view using pre defined ids are advised.
   *
   * @param view view instance we store those values
   * @param propertyInfo property information
   * @param propertyId pre defined property id
   * @param <T> type of property value
   */
  public static <T> void setPropertyInfo(View view, PropertyInfo<T> propertyInfo, int propertyId) {
    if (requireSupportCompat()) {
      synchronized (sProperties) {
        WeakHashMap<View, PropertyInfo<?>> properties = sProperties.get(propertyId);
        if (properties == null) {
          properties = new WeakHashMap<>();
          sProperties.put(propertyId, properties);
        }
        if (propertyInfo != null) {
          properties.put(view, propertyInfo);
        }
      }
    } else {
      view.setTag(propertyId, propertyInfo);
    }
  }

  /**
   * Checks compatibility mode
   * @return true if compat usage else api calls
   */
  private static boolean requireSupportCompat() {
    return VERSION.SDK_INT < VERSION_CODES.ICE_CREAM_SANDWICH;
  }
}