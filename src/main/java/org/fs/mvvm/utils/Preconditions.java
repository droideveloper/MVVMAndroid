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

public final class Preconditions {

  /**
   * Private constructor.
   */
  private Preconditions() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Checks any object whether it is null or not if it is null throws exception
   * with provided error string.
   *
   * @param object object instance for check.
   * @param errorMessage message string if it is null.
   * @param <T> Type of the object
   */
  public static <T> void checkNotNull(T object, String errorMessage) {
    if (object == null) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  /**
   * Checks if any condition true or not if not true exception thrown with provided error string
   *
   * @param condition condition to check
   * @param errorMessage error message to be shown
   */
  public static void checkConditionMeet(boolean condition, String errorMessage) {
    if (!condition) {
      throw new IllegalArgumentException(errorMessage);
    }
  }
}
