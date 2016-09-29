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
package org.fs.mvvm.common;

import android.databinding.ViewDataBinding;
import org.fs.mvvm.data.SimpleListItem;
import org.fs.mvvm.managers.BusManager;
import org.fs.mvvm.managers.SelectedEvent;

public class SimpleRecyclerBindingHolder extends AbstractRecyclerBindingHolder<SimpleListItem> {
  /**
   * Public constructor that needs to take 2 args.
   *
   * @param binding viewDataBinding instance.
   * @param busManager busManager instance.
   */
  public SimpleRecyclerBindingHolder(ViewDataBinding binding, BusManager<SelectedEvent<SimpleListItem>> busManager) {
    super(binding, busManager);
  }
}
