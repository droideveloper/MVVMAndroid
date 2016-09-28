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

import android.text.TextUtils;
import java.util.Collection;

public final class Objects {

  /**
   * Private constructor
   */
  private Objects() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Checks if object null or empty if object instance is Collection or String
   *
   * @param object object instance to check
   * @param <T> T type of the object
   * @return true or false
   */
  public static <T> boolean isNullOrEmpty(T object) {
    if (object == null) return true;
    if (object instanceof String) {
      String str = (String) object;
      return TextUtils.isEmpty(str);
    }
    if (object instanceof Collection<?>) {
      Collection<?> collection = (Collection<?>) object;
      return collection.isEmpty();
    }
    return false;
  }
}
