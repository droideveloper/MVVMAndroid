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
package org.fs.mvvm.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class RecyclerView extends android.support.v7.widget.RecyclerView {

  private int selectedPosition;

  public RecyclerView(Context context) {
    super(context);
  }

  public RecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public RecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  /**
   * Sets this in here since we do not have such property in recyclerView
   *
   * @param selectedPosition selectedPosition of recyclerView
   */
  public void setSelectedPosition(int selectedPosition) {
    this.selectedPosition = selectedPosition;
  }

  /**
   * Gets selectedPosition
   *
   * @return an int.
   */
  public int getSelectedPosition() {
    return this.selectedPosition;
  }
}
