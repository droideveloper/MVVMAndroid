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
import org.fs.mvvm.R;
import org.fs.mvvm.managers.BusManager;
import org.fs.mvvm.managers.SelectedEvent;

public abstract class AbstractRecyclerBindingHolder<D extends BaseObservable> extends RecyclerView.ViewHolder
    implements View.OnClickListener {

  /**
   * BusManager that will handle communication with adapter
   */
  private final BusManager busManager;
  /**
   * ViewDataBinding that will manipulate my data on view
   */
  private final ViewDataBinding binding;
  /**
   * My data that needs to extend BaseObservable
   */
  private D item;

  /**
   * just to be sure we track it it might mostly null
   */
  private View.OnClickListener previousListener;

  /**
   * Public constructor that needs to take 2 args.
   *
   * @param binding viewDataBinding instance.
   * @param busManager busManager instance.
   */
  public AbstractRecyclerBindingHolder(ViewDataBinding binding, BusManager busManager) {
    super(binding.getRoot());
    this.binding = binding;
    this.busManager = busManager;
  }

  /**
   * Sets variable on layout
   *
   * @param id BR.item should be, make sure your variable in layout named as 'item'
   * @param item variable value extends BaseObservable
   */
  public final void setItem(int id, D item) {
    this.item = item;
    binding.setVariable(id, item);
    binding.executePendingBindings();
  }

  /**
   * sets selection of view
   * @param isSelected true or false
   */
  public final void setSelected(boolean isSelected) {
    view().setSelected(isSelected);
  }

  /**
   * Gets viewDataBinding
   * @return viewDataBinding instance for this view.
   */
  public final ViewDataBinding binding() {
    return this.binding;
  }

  /**
   * Gets view
   * @return view as root
   */
  public final View view() {
    return this.itemView;
  }

  /**
   * Gets data item
   * @return data item
   */
  public final D item() {
    return this.item;
  }

  /**
   * tracks attach for adding listener to root view
   */
  public final void onAttach() {
    previousListener = ListenerUtil.getListener(view(), R.id.onClickListener);
    view().setOnClickListener(this);
  }

  /**
   * tracks detach for removing listener to root view
   */
  public final void onDetach() {
    previousListener = null;
    view().setOnClickListener(null);
  }

  /**
   * recieves click of view on layout root view
   * @param v view of root.
   */
  @Override public final void onClick(View v) {
    busManager.send(new SelectedEvent<>(item(), getAdapterPosition()));
    if (previousListener != null) {
      previousListener.onClick(v);
    }
  }
}
