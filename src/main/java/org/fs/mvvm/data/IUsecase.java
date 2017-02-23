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
package org.fs.mvvm.data;

import org.fs.mvvm.listeners.Callback;

public interface IUsecase<T, V> {

  /**
   * checks subscription is unsubscribed or not
   *
   * @return a boolean.
   */
  boolean isDisposed();

  /**
   * cancels if subscription is active.
   */
  void dispose();

  /**
   * Gets observer of usecase
   *
   * @return an observer type of T
   */
  V sync();

  /**
   * async execution of usecase
   *
   * @param callback callback that we get results
   */
  void async(Callback<T> callback);
}
