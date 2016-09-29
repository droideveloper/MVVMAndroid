/*
 * MVVM_Workspace Copyright (C) 2016 Fatih.
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
package org.fs.mvvm.managers;

import android.databinding.BaseObservable;

public final class SelectedEvent<D extends BaseObservable> implements IEvent {

  private final D   selectedItem;
  private final int selectedItemAdapterPosition;

  public SelectedEvent(final D selectedItem, final int selectedItemAdapterPosition) {
    this.selectedItem = selectedItem;
    this.selectedItemAdapterPosition = selectedItemAdapterPosition;
  }

  public final D selectedItem() {
    return this.selectedItem;
  }

  public final int selectedItemAdapterPosition() {
    return this.selectedItemAdapterPosition;
  }
}
