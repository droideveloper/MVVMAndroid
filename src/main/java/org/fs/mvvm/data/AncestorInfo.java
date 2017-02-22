/*
 * MVVM Copyright (C) 2017 Fatih.
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

import android.view.View;
import org.fs.mvvm.utils.Objects;

public abstract class AncestorInfo {

  protected final String str;

  public AncestorInfo(final String str) {
    if (Objects.isNullOrEmpty(str)) {
      throw new RuntimeException("ancestor can not be null or empty.");
    }
    this.str = str;
  }

  public abstract Class<?> typeof();

  public abstract View view();

}