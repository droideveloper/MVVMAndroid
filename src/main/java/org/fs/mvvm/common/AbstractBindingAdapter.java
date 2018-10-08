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
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.fs.mvvm.utils.Objects;
import org.fs.mvvm.utils.Preconditions;

public abstract class AbstractBindingAdapter<D extends BaseObservable, V extends AbstractBindingHolder<D>> extends
    BaseAdapter {

  private final ObservableList<D> itemSource;

  public AbstractBindingAdapter(ObservableList<D> itemSource) {
    Preconditions.checkNotNull(itemSource, "itemSource is null");
    this.itemSource = itemSource;
    this.itemSource.addOnListChangedCallback(itemSourceObserver);
  }

  public final void clearAll() {
    if (itemSource != null) {
      itemSource.removeOnListChangedCallback(itemSourceObserver);
    }
  }

  @NonNull protected abstract V onCreateViewHolder(ViewGroup parent, int viewType);

  protected void onBindViewHolder(V viewHolder, int position) {
    viewHolder.bind(getItemAt(position));
  }

  @Override public int getCount() {
    return itemSource != null ? itemSource.size() : 0;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final int viewType = getItemViewType(position);
    if (convertView == null) {
      V viewHolder = onCreateViewHolder(parent, viewType);
      convertView = viewHolder.itemView;
    }
    V viewHolder = Objects.toObject(convertView.getTag());
    if (viewHolder == null) {
      //we try to reload those values via recursive call
      return getView(position, null, parent);
    }
    onBindViewHolder(viewHolder, position);
    return convertView;
  }


  protected D getItemAt(int position) {
    if (position >= 0 && position < getCount()) {
      return itemSource.get(position);
    }
    return null;
  }

  protected final void log(String msg) {
    log(Log.DEBUG, msg);
  }

  protected final void log(int level, String msg) {
    if (isLogEnabled()) {
      Log.println(level, getClassTag(), msg);
    }
  }

  protected final void log(Throwable error) {
    StringWriter strWriter = new StringWriter();
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    error.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  protected abstract boolean isLogEnabled();

  protected abstract String getClassTag();


  private final ObservableList.OnListChangedCallback<ObservableList<D>>
    itemSourceObserver = new ObservableList.OnListChangedCallback<ObservableList<D>>() {

    @Override public void onChanged(ObservableList<D> itemSource) {
      AbstractBindingAdapter.this.notifyDataSetChanged();
    }

    @Override public void onItemRangeChanged(ObservableList<D> itemSource, int start, int count) {
      AbstractBindingAdapter.this.notifyDataSetChanged();
    }

    @Override public void onItemRangeInserted(ObservableList<D> itemSource, int start, int count) {
      AbstractBindingAdapter.this.notifyDataSetChanged();
    }

    @Override public void onItemRangeMoved(ObservableList<D> itemSource, int start, int to, int count) {
      AbstractBindingAdapter.this.notifyDataSetChanged();
    }

    @Override public void onItemRangeRemoved(ObservableList<D> itemSource, int start, int count) {
      AbstractBindingAdapter.this.notifyDataSetChanged();
    }
  };
}
