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
import android.view.View;
import io.reactivex.Observer;
import java.lang.ref.WeakReference;
import org.fs.mvvm.R;
import org.fs.mvvm.utils.Preconditions;

public abstract class AbstractBindingHolder<D extends BaseObservable> implements
    View.OnClickListener {

  private final WeakReference<View> view;
  private final Observer<SelectedEventType<D>> observer;
  private final ViewDataBinding binding;

  private D item;
  private int position;

  public AbstractBindingHolder(ViewDataBinding binding, Observer<SelectedEventType<D>> observer) {
    Preconditions.checkNotNull(binding, "binding is null");
    this.binding = binding;
    this.observer = observer;
    this.view = new WeakReference<>(binding.getRoot());
    ListenerUtil.trackListener(view(), this, R.id.onClickListener);
    view().setOnClickListener(this);
  }

  public final void setItem(int id, D item, int position) {
    this.item = item;
    this.position = position;
    this.binding.setVariable(id, item);
    this.binding.executePendingBindings();
  }

  public final View view() {
    return view.get();
  }

  public final D item() {
    return this.item;
  }

  public final void setActivated(boolean isActivated) {
    view().setActivated(isActivated);
  }

  @Override public final void onClick(View v) {
    observer.onNext(new SelectedEventType<>(item, position));
  }
}
