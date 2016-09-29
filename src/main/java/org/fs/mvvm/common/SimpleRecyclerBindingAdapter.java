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

import android.content.Context;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import org.fs.mvvm.BR;
import org.fs.mvvm.BuildConfig;
import org.fs.mvvm.R;
import org.fs.mvvm.data.SimpleListItem;
import org.fs.mvvm.managers.BusManager;
import org.fs.mvvm.managers.SelectedEvent;

public final class SimpleRecyclerBindingAdapter extends AbstractRecyclerBindingAdapter<SimpleListItem, SimpleRecyclerBindingHolder> {

  public SimpleRecyclerBindingAdapter(Context context, ObservableList<SimpleListItem> itemSource, int selectionMode) {
    super(context, itemSource, selectionMode);
  }

  @Override protected void bindDataViewHolder(SimpleListItem item, SimpleRecyclerBindingHolder viewHolder) {
    viewHolder.setItem(BR.item, item);
  }

  @Override protected SimpleRecyclerBindingHolder createDataViewHolder(ViewDataBinding binding, BusManager<SelectedEvent<SimpleListItem>> busManager, int viewType) {
    return new SimpleRecyclerBindingHolder(binding, busManager);
  }

  @Override protected int layoutResource(int viewType) {
    return R.layout.view_simple_list_item;
  }

  @Override public int getItemViewType(int position) {
    return super.getItemViewType(position);
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override protected String getClassTag() {
    return SimpleRecyclerBindingAdapter.class.getSimpleName();
  }
}
