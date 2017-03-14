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
package org.fs.mvvm.metadata.view;

import android.view.View;
import android.view.ViewGroup;
import org.fs.mvvm.data.MetadataInfoType;
import org.fs.mvvm.utils.Objects;

public final class ViewMargin implements MetadataInfoType<View, Integer[]> {

  private final View view;

  public ViewMargin(final View view) {
    if (Objects.isNullOrEmpty(view)) {
      throw new RuntimeException("view can not be null");
    }
    this.view = view;
  }

  @Override public String named() {
    return "margin";
  }

  @Override public void set(Integer[] value) {
    if (value.length != 4) {
      throw new RuntimeException("margin should be 4 in length");
    }
    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    params.setMargins(value[0], value[1], value[2], value[3]);
    view.requestLayout();
  }

  @Override public Integer[] get() {
    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    return new Integer[] {
        params.leftMargin,
        params.topMargin,
        params.rightMargin,
        params.bottomMargin
    };
  }
}
