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

import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.databinding.adapters.ListenerUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import io.reactivex.Observer;
import org.fs.mvvm.R;
import org.fs.mvvm.managers.SelectedEventType;

public abstract class AbstractRecyclerBindingHolder<D extends BaseObservable> extends RecyclerView.ViewHolder
    implements View.OnClickListener {

  private final Observer<SelectedEventType<D>> observable;

  private final ViewDataBinding binding;

  private D item;


  private View.OnClickListener previousListener;

  public AbstractRecyclerBindingHolder(ViewDataBinding binding, Observer<SelectedEventType<D>> observable) {
    super(binding.getRoot());
    this.binding = binding;
    this.observable = observable;
  }

  public final void setItem(int id, D item) {
    this.item = item;
    binding.setVariable(id, item);
    binding.executePendingBindings();
  }

  public final void setActivated(boolean isActivated) {
    view().setActivated(isActivated);
  }

  public final ViewDataBinding binding() {
    return this.binding;
  }

  public final View view() {
    return this.itemView;
  }

  public final D item() {
    return this.item;
  }

  public void onAttach() {
    //this viewHolder set's its listener on first so we can track it in our listener
    ListenerUtil.trackListener(view(), this, R.id.onClickListener);
    view().setOnClickListener(this);
  }

  public void onDetach() {
    view().setOnClickListener(null);
  }

  @Override public final void onClick(View v) {
    observable.onNext(new SelectedEventType<>(item(), getAdapterPosition()));
  }
}
