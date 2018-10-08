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
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import io.reactivex.Observer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static android.databinding.ObservableList.OnListChangedCallback;

public abstract class AbstractRecyclerBindingAdapter<D extends BaseObservable, V extends AbstractRecyclerBindingHolder<D>> extends RecyclerView.Adapter<V> {

  private final ObservableList<D> itemSource;


  public AbstractRecyclerBindingAdapter(ObservableList<D> itemSource) {
    this.itemSource = itemSource;
  }

  @Override public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    itemSource.addOnListChangedCallback(itemSourceObserver);
  }

  @Override public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
    itemSource.removeOnListChangedCallback(itemSourceObserver);
    super.onDetachedFromRecyclerView(recyclerView);
  }


  @Override public final void onBindViewHolder(@NonNull V viewHolder, int position) {
    viewHolder.bind(getItemAt(position));
  }

  @Override public void onViewRecycled(@NonNull V holder) {
    holder.unbind();
    super.onViewRecycled(holder);
  }

  @Override public final int getItemCount() {
    return itemSource != null ? itemSource.size() : 0;
  }

  protected final D getItemAt(int position) {
    if (position >= 0 && position < getItemCount()) {
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

  private final OnListChangedCallback<ObservableList<D>> itemSourceObserver = new OnListChangedCallback<ObservableList<D>>() {

    @Override public void onChanged(ObservableList<D> itemSource) {
      notifyDataSetChanged();
    }

    @Override public void onItemRangeChanged(ObservableList<D> itemSource, int start, int count) {
      notifyItemRangeChanged(start, count);
    }

    @Override public void onItemRangeInserted(ObservableList<D> itemSource, int start, int count) {
      notifyItemRangeInserted(start, count);
    }

    @Override public void onItemRangeMoved(ObservableList<D> itemSource, int start, int to, int count) {
      List<Integer> starts = new ArrayList<>();
      for (; start < (start + count); start++) {
        starts.add(start);
      }
      List<Integer> ends = new ArrayList<>();
      for (; to < (to + count); to++) {
        ends.add(to);
      }
      for (int i = 0; i < count; i++) {
        notifyItemMoved(starts.get(i), ends.get(i));
      }
    }

    @Override public void onItemRangeRemoved(ObservableList<D> itemSource, int start, int count) {
      notifyItemRangeRemoved(start, count);
    }
  };
}
