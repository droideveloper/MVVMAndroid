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
import java.lang.ref.WeakReference;
import org.fs.mvvm.R;
import org.fs.mvvm.managers.BusManager;
import org.fs.mvvm.managers.SelectedEvent;
import org.fs.mvvm.utils.Preconditions;

public abstract class AbstractBindingHolder<D extends BaseObservable> implements
    View.OnClickListener {

  private final WeakReference<View> view;
  private final BusManager          busManager;
  private final ViewDataBinding     binding;

  private D item;
  private int position;

  /**
   * Public constructor provided this viewCell
   *
   * @param binding viewDataBinding since type is not known but it's enough for setting it here.
   * @param busManager busManager we require to set in this spot.
   */
  public AbstractBindingHolder(ViewDataBinding binding, BusManager busManager) {
    Preconditions.checkNotNull(binding, "binding is null");
    this.binding = binding;
    this.busManager = busManager;
    this.view = new WeakReference<>(binding.getRoot());
    ListenerUtil.trackListener(view(), this, R.id.onClickListener);
    view().setOnClickListener(this);
  }

  /**
   * ViewDataBinding relay here we set
   * properties in this spot
   *
   * @param id id of variable set in layout, preferred is item
   * @param item item that we will set on variable declared
   * @param position position for activated track
   */
  public final void setItem(int id, D item, int position) {
    this.item = item;
    this.position = position;
    this.binding.setVariable(id, item);
    this.binding.executePendingBindings();
  }

  /**
   * returns viewCell itself
   *
   * @return viewCell
   */
  public final View view() {
    return view.get();
  }

  /**
   * tracks data item that is provided to viewCell
   *
   * @return returns data related to this view
   */
  public final D item() {
    return this.item;
  }

  /**
   * sets if view is activated or not
   *
   * @param isActivated activated state
   */
  public final void setActivated(boolean isActivated) {
    view().setActivated(isActivated);
  }

  /**
   * activated listener if selection occured first time it will selected if we do same on already selected item
   * it will deactivate it.
   *
   * @param v view to activate or deactivate
   */
  @Override public final void onClick(View v) {
    busManager.send(new SelectedEvent<>(item, position));
  }
}
