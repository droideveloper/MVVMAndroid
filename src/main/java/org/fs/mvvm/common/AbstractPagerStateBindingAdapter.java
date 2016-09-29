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
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.fs.mvvm.utils.Objects;

import static android.databinding.ObservableList.OnListChangedCallback;

public abstract class AbstractPagerStateBindingAdapter<D extends BaseObservable> extends FragmentStatePagerAdapter {

  protected final ObservableList<D> itemSource;

  /**
   * Creates pagerStateAdapter for viewPager use
   *
   * @param fragmentManager fragmentManager instance if you call from fragment pass childFragmentManager.
   * @param itemSource dataSource we provide for this adapter.
   */
  public AbstractPagerStateBindingAdapter(FragmentManager fragmentManager, ObservableList<D> itemSource) {
    super(fragmentManager);
    this.itemSource = itemSource;
    this.itemSource.addOnListChangedCallback(itemSourceObserver);
  }

  /**
   * calls for clearing notifications from our adapter since we do not want it keep calling
   * notifications of itemSourceObserver.
   */
  public void clearAdapter() {
    if (itemSource != null) {
      itemSource.removeOnListChangedCallback(itemSourceObserver);
    }
  }

  /**
   * Gets size of the entities
   * @return int if null it returns 0
   */
  @Override public int getCount() {
    return itemSource != null ? itemSource.size() : 0;
  }

  /**
   * Returns object's toString value of implementation or
   * if you provide custom do modify it.
   *
   * @param position position of title required (specially for PagerTitleStrip or PagerTabStrip)
   * @return String value or nothing at all.
   */
  @Override public CharSequence getPageTitle(int position) {
    final D item = getItemAt(position);
    return Objects.isNullOrEmpty(item) ? super.getPageTitle(position) : item.toString();
  }

  /***
   * Returns fragment instance in given position
   * lower level
   * @param position position of fragment
   * @return Fragment instance or null
   */
  @Override public final Fragment getItem(int position) {
    final D item = getItemAt(position);
    final int viewType = getItemViewType(position);
    return onBindView(item, viewType);
  }

  /**
   * Converted in similar side as if it's recyclerAdapter for easy use all
   *
   * @param item item of this adapter for position
   * @param viewType view type of this adapter for position
   * @return Fragment instance or null
   */
  protected abstract Fragment onBindView(final D item, int viewType);

  /**
   * Returns viewType for selected position
   *
   * @param position position of viewType
   * @return int type of view
   */
  protected abstract int getItemViewType(int position);

  /**
   * Returns if log enabled for this adapter instance
   *
   * @return true or false
   */
  protected abstract boolean isLogEnabled();

  /**
   * Returns string representation of this class instance
   *
   * @return string value
   */
  protected abstract String getClassTag();

  /**
   * String message to log as Debug
   *
   * @param msg message to log
   */
  protected final void log(String msg) {
    log(Log.DEBUG, msg);
  }

  /**
   * String message to log as given priority
   *
   * @param level priority of log
   * @param msg message to log
   */
  protected final void log(int level, String msg) {
    if (isLogEnabled()) {
      Log.println(level, getClassTag(), msg);
    }
  }

  /**
   * Error to log as Error level
   *
   * @param error error caused
   */
  protected final void log(Throwable error) {
    StringWriter strWriter = new StringWriter();
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    error.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  /**
   * Gets item at position for this adapter entity
   *
   * @param position position of entity
   * @return D type of entity or null if position is invalid.
   */
  private final D getItemAt(int position) {
    if (position >= 0 && position < getCount()) {
      return itemSource.get(position);
    }
    return null;
  }

  /**
   * keeps and eye on my data source and if change occured it will notify adapter that we will do little less about this
   */
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