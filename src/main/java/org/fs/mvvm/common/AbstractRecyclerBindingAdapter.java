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
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import java8.util.stream.StreamSupport;
import org.fs.mvvm.commands.Action;
import org.fs.mvvm.managers.BusManager;
import org.fs.mvvm.managers.SelectedEvent;
import org.fs.mvvm.utils.Preconditions;
import rx.Subscription;
import rx.functions.Action1;

import static android.databinding.ObservableList.OnListChangedCallback;

public abstract class AbstractRecyclerBindingAdapter<D extends BaseObservable, V extends AbstractRecyclerBindingHolder<D>> extends RecyclerView.Adapter<V>
  implements Action1<SelectedEvent<D>> {

  public final static int SINGLE_SELECTION_MODE   = 0x01;
  public final static int MULTIPLE_SELECTION_MODE = 0x02;

  private final ObservableList<D>             itemSource;
  private final WeakReference<Context>        contextReference;
  private final BusManager                    busManager;
  private final List<Integer>                 selection;
  private final int                           selectionMode;

  private Action<List<Integer>>               selectedCallback;

  private Subscription eventSubs;

  public AbstractRecyclerBindingAdapter(Context context, ObservableList<D> itemSource, int selectionMode) {
    this.itemSource = itemSource;
    this.busManager = new BusManager();
    this.selection = new ArrayList<>();
    Preconditions.checkConditionMeet(selectionMode >= SINGLE_SELECTION_MODE && selectionMode <= MULTIPLE_SELECTION_MODE,
        "invalid selection mode, select proper one.");
    this.selectionMode = selectionMode;
    this.contextReference = context != null ? new WeakReference<>(context) : null;
  }

  @Override public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
    eventSubs = busManager.register(this);
    itemSource.addOnListChangedCallback(itemSourceObserver);
  }

  @Override public final void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    if (contextReference != null) {
      contextReference.clear();
    }
    itemSource.removeOnListChangedCallback(itemSourceObserver);
    busManager.unregister(eventSubs);
    eventSubs = null;
  }

  /**
   * Callback to receive result of selection as integers
   * @param selectedCallback selected callbacks
   */
  public final void setSelectedCallback(Action<List<Integer>> selectedCallback) {
    this.selectedCallback = selectedCallback;
  }

  @Override public final void onViewDetachedFromWindow(V viewHolder) {
    viewHolder.onDetach();
  }

  @Override public final void onViewAttachedToWindow(V viewHolder) {
    viewHolder.onAttach();
  }

  @Override public final void onBindViewHolder(V viewHolder, int position) {
    final D item = getItemAt(position);
    viewHolder.setSelected(isSelected(position));
    bindDataViewHolder(item, viewHolder);
  }

  @Override public final V onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater factory = factory();
    if (factory != null) {
      ViewDataBinding binding = DataBindingUtil.inflate(factory, layoutResource(viewType), parent, false);
      return createDataViewHolder(binding, busManager, viewType);
    }
    throw new IllegalArgumentException("we can not create factory inflater since our context instance got clean up, check out caller");
  }

  @Override public final int getItemCount() {
    return itemSource != null ? itemSource.size() : 0;
  }

  /**
   * Listens for click events of its child views on viewHolders
   * @param event selection event
   */
  @Override public final void call(SelectedEvent<D> event) {
    if (isSingleMode()) {
      addSelectionIndex(event.selectedItemAdapterPosition(), true);
    } else {
      addSelectionIndex(event.selectedItemAdapterPosition(), false);
    }
  }

  /**
   * Gets item at specified position
   *
   * @param position position of item
   * @return item object
   */
  protected final D getItemAt(int position) {
    if (position >= 0 && position < getItemCount()) {
      return itemSource.get(position);
    }
    return null;
  }

  /**
   * logs message with Debug level
   * @param msg log message
   */
  protected final void log(String msg) {
    log(Log.DEBUG, msg);
  }

  /**
   * logs messages with given log level
   * @param level level of log
   * @param msg log message
   */
  protected final void log(int level, String msg) {
    if (isLogEnabled()) {
      Log.println(level, getClassTag(), msg);
    }
  }

  /**
   * logs Exceptions on androidLog with Error level
   * @param error error to print
   */
  protected final void log(Throwable error) {
    StringWriter strWriter = new StringWriter();
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    error.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  /**
   * is position is selected or not.
   *
   * @param position position to check if selected.
   * @return true or false
   */
  private boolean isSelected(int position) {
    return selection.contains(position);
  }

  /**
   * helps to store selection on our list.
   * @param position position to check
   * @param clearBefore for singleMode usage
   */
  private void addSelectionIndex(int position, boolean clearBefore) {
    //don't have it so select it.
    if (!selection.contains(position)) {
      if (clearBefore) {
        StreamSupport.stream(selection)
                     .forEach(this::notifyItemChanged);
        selection.clear();
      }
      selection.add(position);
    } else {
      //already have it so un-select it.
      int innerPosition = selection.indexOf(position);
      selection.remove(innerPosition);
    }
    //notify viewModel if we provide callback
    if (selectedCallback != null) {
      selectedCallback.execute(selection);
    }
    notifyItemChanged(position);
  }

  /**
   * Returns if selection is singleMode or not
   * @return true or false
   */
  private boolean isSingleMode() {
    return selectionMode == SINGLE_SELECTION_MODE;
  }

  /**
   * Provides context instance it might be null.
   * @return context instance.
   */
  private final Context context() {
    return contextReference != null ? contextReference.get() : null;
  }

  /**
   * Provides layout inflater factory instance it might be null.
   * @return LayoutInflater instance
   */
  private final LayoutInflater factory() {
    Context context = context();
    if (context != null) {
      return LayoutInflater.from(context);
    }
    return null;
  }

  /**
   * true or false whether log enabled or not
   * <li>advised to use BuildConfig.DEBUG that only log for debug builds.</li>
   * @return true or false
   */
  protected abstract boolean isLogEnabled();

  /**
   * Tag for log provided for this class
   * @return string for this class
   */
  protected abstract String getClassTag();

  /**
   * Register viewHolder and Data item on viewHolder with viewBindings provide BR id of layout variable
   * possibly BR.item
   * @param item item data
   * @param viewHolder view holder
   */
  protected abstract void bindDataViewHolder(D item, V viewHolder);

  /**
   * Creates viewHolder for specific type of view.
   * @param binding binding of this layout
   * @param busManager busManager to communicate with viewHolder
   * @param viewType type of viewHolder we create according to viewType
   * @return ViewHolder instance for this adapter
   */
  protected abstract V createDataViewHolder(ViewDataBinding binding, BusManager busManager, int viewType);

  /**
   * Returns layout resources for viewType
   * @param viewType view type for layout
   * @return resource id of layout
   */
  @LayoutRes
  protected abstract int layoutResource(int viewType);

  /**
   * this listener will observe any change on ObservableList or ObservableArrayList then
   * we grab that change on source itself to notify our recycler adapter
   * with this we achieve quite nice adapter less boiler plate code.
   */
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
      //create start indexes
      final List<Integer> starts = IntStreams.iterate(start, next -> next + 1)
          .limit(count)
          .boxed()
          .collect(Collectors.toList());
      //create end indexes
      final List<Integer> ends = IntStreams.iterate(to, next -> next + 1)
          .limit(count)
          .boxed()
          .collect(Collectors.toList());
      //loop over them and notify adapter
      IntStreams.range(0, count)
          .mapToObj(index -> new Pair<>(starts.get(index), ends.get(index)))
          .forEach(position -> notifyItemMoved(position.first, position.second));
    }

    @Override public void onItemRangeRemoved(ObservableList<D> itemSource, int start, int count) {
      notifyItemRangeRemoved(start, count);
    }
  };
}
