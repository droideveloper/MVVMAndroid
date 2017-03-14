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
package org.fs.mvvm.metadata.absListView;

import android.widget.AbsListView;
import org.fs.mvvm.data.MetadataInfoType;
import org.fs.mvvm.utils.Objects;

public final class AbsListViewSelectedPosition implements MetadataInfoType<AbsListView, Integer> {

  private final AbsListView listView;

  public AbsListViewSelectedPosition(final AbsListView listView) {
    if (Objects.isNullOrEmpty(listView)) {
      throw new RuntimeException("view can not be null");
    }
    this.listView = listView;
  }

  @Override public String named() {
    return "selectedPosition";
  }

  @Override public void set(Integer value) {
    listView.setSelection(value);
  }

  @Override public Integer get() {
    return listView.getSelectedItemPosition();
  }
}
