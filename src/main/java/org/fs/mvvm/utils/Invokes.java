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

import java8.util.function.Function;
import org.fs.mvvm.commands.Action;

public final class Invokes {

  private Invokes() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Lambda helper that executes action with one param
   *
   * @param action lambda
   * @param object lambda param
   * @param <T> type of param
   */
  public static <T> void invoke(Action<T> action, T object) {
    Preconditions.checkNotNull(action, "action is null");
    action.execute(object);
  }

  /**
   * Lambda helper that executes func with two params
   *
   * @param func lambda
   * @param object lambda param
   * @param <T> type of param
   * @param <R> type of return
   * @return R type or null
   */
  public static <T, R> R invoke(Function<T, R> func, T object) {
    Preconditions.checkNotNull(func, "func is null");
    return func.apply(object);
  }
}
