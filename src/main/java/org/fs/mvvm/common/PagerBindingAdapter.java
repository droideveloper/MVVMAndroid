/*
 * MVVM Copyright (C) 2017 Fatih.
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
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.fs.mvvm.utils.Objects;
import org.fs.mvvm.utils.Preconditions;

public abstract class PagerBindingAdapter<D extends BaseObservable, V extends AbstractPagerBindingHolder<D>> extends PagerAdapter {

  private ObservableList<D> dataSource;

  public PagerBindingAdapter(ObservableList<D> dataSource) {
    Preconditions.checkNotNull(dataSource, "data source can not be null");
    this.dataSource = dataSource;
    this.dataSource.addOnListChangedCallback(itemSourceObserver);
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    View view = Objects.toObject(object);
    container.removeView(view);
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    final Context context = container.getContext();
    final LayoutInflater factory = factory(context);
    final int viewType = getItemViewType(position);
    if (factory != null) {
      final ViewDataBinding dataBinding = DataBindingUtil.inflate(factory, layoutResource(viewType), container, false);
      V viewHolder = createViewHolder(dataBinding, viewType);
      final D item = getItemAt(position);
      bindViewHolder(item, viewHolder, position);
      return dataBinding.getRoot();
    }
    throw new RuntimeException("View context is killed yet you try to access it.");
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public int getCount() {
    return dataSource != null ? dataSource.size() : 0;
  }

  public final D getItemAt(int position) {
    if (position >= 0 && position < getCount()) {
      return dataSource.get(position);
    }
    return null;
  }

  protected final LayoutInflater factory(Context context) {
    return LayoutInflater.from(context);
  }

  protected void log(String msg) {
    log(Log.DEBUG, msg);
  }

  protected void log(Throwable exp) {
    StringWriter strWriter = new StringWriter(128);
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    exp.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  protected void log(int lv, String msg) {
    if (isLogEnabled()) {
      Log.println(lv, getClassTag(), msg);
    }
  }

  @LayoutRes
  protected abstract int layoutResource(int viewType);

  protected abstract int getItemViewType(int position);

  protected abstract V createViewHolder(ViewDataBinding binding, int viewType);

  protected abstract void bindViewHolder(D item, V viewHolder, int position);

  protected abstract boolean isLogEnabled();

  protected abstract String getClassTag();

  /**
   * Observer for DataSet that might change after its lifecycle
   */
  private final ObservableList.OnListChangedCallback<ObservableList<D>> itemSourceObserver
      = new ObservableList.OnListChangedCallback<ObservableList<D>>() {

    @Override public void onChanged(ObservableList<D> itemSource) {
      notifyDataSetChanged();
    }

    @Override public void onItemRangeChanged(ObservableList<D> itemSource, int start, int count) {
      notifyDataSetChanged();
    }

    @Override public void onItemRangeInserted(ObservableList<D> itemSource, int start, int count) {
      notifyDataSetChanged();
    }

    @Override public void onItemRangeMoved(ObservableList<D> itemSource, int start, int to, int count) {
      notifyDataSetChanged();
    }

    @Override public void onItemRangeRemoved(ObservableList<D> itemSource, int start, int count) {
      notifyDataSetChanged();
    }
  };
}
