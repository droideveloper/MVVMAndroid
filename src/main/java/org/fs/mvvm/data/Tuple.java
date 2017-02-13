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

import java.util.Arrays;
import java.util.List;
import org.fs.mvvm.utils.Objects;

public final class Tuple<T> {

  private final List<T> args;

  /**
   * Arguments used for tuple that can take n argument of T
   * @param args params
   */
  public Tuple(T... args) {
    if(Objects.isNullOrEmpty(args)) {
      throw new IllegalArgumentException("You should not pass non arguments on constructor since this is tuple we require args at leas 2 use RelayCommand for your command.");
    }
    this.args = Arrays.asList(args);
  }

  /**
   * Get nth argument at index
   * @param index argument at
   * @return argument T or null if index is out of bounds or args is null
   */
  public T get(final int index) {
    if(index >= 0 && index < args.size()) {
      return args.get(index);
    }
    return null;
  }

  /**
   * Gets size of args
   * @return 0 if null or empty else it will return count
   */
  public final int size() {
    return Objects.isNullOrEmpty(args) ? 0 : args.size();
  }
}
