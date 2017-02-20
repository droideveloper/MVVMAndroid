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
package org.fs.mvvm.metadata.textView;

import android.widget.TextView;
import org.fs.mvvm.data.MetadataInfo;
import org.fs.mvvm.utils.Objects;

public class TextViewTextColor implements MetadataInfo<TextView, Integer> {

  private final TextView textView;

  public TextViewTextColor(final TextView textView) {
    if (Objects.isNullOrEmpty(textView)) {
      throw new RuntimeException("view can not be null");
    }
    this.textView = textView;
  }

  @Override public String named() {
    return "textColor";
  }

  @Override public void set(Integer value) {
    textView.setTextColor(value);
  }

  @Override public Integer get() {
    return textView.getCurrentTextColor();
  }
}
