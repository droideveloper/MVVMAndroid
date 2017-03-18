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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.fs.mvvm.utils.Objects;

import static android.databinding.ObservableList.OnListChangedCallback;


public abstract class AbstractPagerBindingAdapter<D extends BaseObservable> extends FragmentPagerAdapter {

  protected final ObservableList<D> itemSource;

  public AbstractPagerBindingAdapter(FragmentManager fragmentManager, ObservableList<D> itemSource) {
    super(fragmentManager);
    this.itemSource = itemSource;
    this.itemSource.addOnListChangedCallback(itemSourceObserver);
  }

  public void clearAdapter() {
    if (itemSource != null) {
      itemSource.removeOnListChangedCallback(itemSourceObserver);
    }
  }

  @Override public int getCount() {
    return itemSource != null ? itemSource.size() : 0;
  }

  @Override public CharSequence getPageTitle(int position) {
    final D item = getItemAt(position);
    return Objects.isNullOrEmpty(item) ? super.getPageTitle(position) : item.toString();
  }

  @Override public final Fragment getItem(int position) {
    final D item = getItemAt(position);
    final int viewType = getItemViewType(position);
    return onBindView(item, viewType);
  }

  public final D getItemAt(int position) {
    if (position >= 0 && position < getCount()) {
      return itemSource.get(position);
    }
    return null;
  }

  public final int getIndexOf(D item) {
    if (item == null) {
      return  -1;
    } else {
      return itemSource.indexOf(item);
    }
  }

  protected abstract Fragment onBindView(final D item, int viewType);

  protected abstract int getItemViewType(int position);

  protected abstract boolean isLogEnabled();

  protected abstract String getClassTag();

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

  private final OnListChangedCallback<ObservableList<D>> itemSourceObserver
      = new OnListChangedCallback<ObservableList<D>>() {

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