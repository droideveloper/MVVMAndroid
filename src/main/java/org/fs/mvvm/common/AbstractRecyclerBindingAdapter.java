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
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.fs.mvvm.managers.EventType;
import org.fs.mvvm.managers.SelectedEventType;
import org.fs.mvvm.utils.Preconditions;

import static android.databinding.ObservableList.OnListChangedCallback;

public abstract class AbstractRecyclerBindingAdapter<D extends BaseObservable, V extends AbstractRecyclerBindingHolder<D>> extends RecyclerView.Adapter<V>
  implements Consumer<EventType> {

  public final static int SINGLE_SELECTION_MODE   = 0x01;
  public final static int MULTIPLE_SELECTION_MODE = 0x02;

  private final ObservableList<D>             itemSource;
  private final WeakReference<Context>        contextReference;
  private final PublishSubject<SelectedEventType<D>> observer;
  private final List<Integer>                 selection;
  private final int                           selectionMode;

  private Consumer<Integer>                    singlePositionCallback;
  private Consumer<List<Integer>>              multiPositionCallback;

  private Consumer<D>                          singleItemCallback;
  private Consumer<List<D>>                    multiItemCallback;

  private Disposable disposable;

  public AbstractRecyclerBindingAdapter(Context context, ObservableList<D> itemSource, int selectionMode) {
    this.itemSource = itemSource;
    this.observer = PublishSubject.create();
    this.selection = new ArrayList<>();
    Preconditions.checkConditionMeet(selectionMode >= SINGLE_SELECTION_MODE && selectionMode <= MULTIPLE_SELECTION_MODE,
        "invalid selection mode, select proper one.");
    this.selectionMode = selectionMode;
    this.contextReference = context != null ? new WeakReference<>(context) : null;
  }

  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    disposable = observer.subscribe(this);
    itemSource.addOnListChangedCallback(itemSourceObserver);
  }

  @Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    if (contextReference != null) {
      contextReference.clear();
    }
    itemSource.removeOnListChangedCallback(itemSourceObserver);
    if (!disposable.isDisposed()) {
      disposable.dispose();
    }
    disposable = null;
  }
  //singleMode selectedPosition
  public final void setSinglePositionCallback(Consumer<Integer> singlePositionCallback) {
    this.singlePositionCallback = singlePositionCallback;
  }
  //singleMode selectedItem
  public final void setSingleItemCallback(Consumer<D> singleItemCallback) {
    this.singleItemCallback = singleItemCallback;
  }
  //multiMode selectedPositions
  public final void setMultiPositionCallback(Consumer<List<Integer>> multiPositionCallback) {
    this.multiPositionCallback = multiPositionCallback;
  }
  //multiMode selectedItems
  public final void setMultiItemCallback(Consumer<List<D>> multiItemCallback) {
    this.multiItemCallback = multiItemCallback;
  }

  @Override public final void onViewDetachedFromWindow(V viewHolder) {
    viewHolder.onDetach();
  }

  @Override public final void onViewAttachedToWindow(V viewHolder) {
    viewHolder.onAttach();
  }

  @Override public final void onBindViewHolder(V viewHolder, int position) {
    final D item = getItemAt(position);
    viewHolder.setActivated(isSelected(position));
    bindDataViewHolder(item, viewHolder);
  }

  @Override public final V onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater factory = factory();
    if (factory != null) {
      ViewDataBinding binding = DataBindingUtil.inflate(factory, layoutResource(viewType), parent, false);
      return createDataViewHolder(binding, observer, viewType);
    }
    throw new IllegalArgumentException("we can not create factory inflater since our context instance got clean up, check out caller");
  }

  @Override public final int getItemCount() {
    return itemSource != null ? itemSource.size() : 0;
  }

  @Override public final void accept(EventType evt) throws Exception {
    if (evt instanceof SelectedEventType) {
      SelectedEventType<D> event = (SelectedEventType<D>) evt;
      if (isSingleMode()) {
        addSelectionIndex(event.selectedItemAdapterPosition(), true);
      } else {
        addSelectionIndex(event.selectedItemAdapterPosition(), false);
      }
    }
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

  private boolean isSelected(int position) {
    return selection.contains(position);
  }

  private void addSelectionIndex(int position, boolean clearBefore) {
    //don't have it so select it.
    if (!selection.contains(position)) {
      if (clearBefore) {
        for (int i = 0, z = selection.size(); i < z; i++) {
          notifyItemChanged(selection.get(i));
        }
        selection.clear();
      }
      selection.add(position);
    } else {
      //already have it so un-select it.
      int innerPosition = selection.indexOf(position);
      selection.remove(innerPosition);
    }
    //notify viewModel if we provide callback
    if (multiPositionCallback != null) {
      try {
        multiPositionCallback.accept(selection);
      } catch (Exception error) {
        throw new AndroidRuntimeException("selection not executable " + selection, error);
      }
    }
    //notification
    if (multiItemCallback != null) {
      try {
        List<D> newCollection = new ArrayList<>();
        for(int i = 0, z = selection.size(); i < z; i ++) {
          int index = selection.get(i);
          newCollection.add(itemSource.get(index));
        }
        multiItemCallback.accept(newCollection);
      } catch (Exception error) {
        throw new AndroidRuntimeException("selection not executable " + selection, error);
      }
    }
    //notification
    if (singlePositionCallback != null) {
      try {
        singlePositionCallback.accept(selection.get(0));
      } catch (Exception error) {
        throw new AndroidRuntimeException("selection not executable " + selection, error);
      }
    }
    //notification
    if (singleItemCallback != null) {
      try {
        singleItemCallback.accept(itemSource.get(selection.get(0)));
      } catch (Exception error) {
        throw new AndroidRuntimeException("selection not executable " + selection, error);
      }
    }
    notifyItemChanged(position);
  }

  public final boolean isSingleMode() {
    return selectionMode == SINGLE_SELECTION_MODE;
  }

  private final Context context() {
    return contextReference != null ? contextReference.get() : null;
  }

  private final LayoutInflater factory() {
    Context context = context();
    if (context != null) {
      return LayoutInflater.from(context);
    }
    return null;
  }

  protected abstract boolean isLogEnabled();

  protected abstract String getClassTag();

  protected abstract void bindDataViewHolder(D item, V viewHolder);

  protected abstract V createDataViewHolder(ViewDataBinding binding, Observer<SelectedEventType<D>> observer, int viewType);

  @LayoutRes
  protected abstract int layoutResource(int viewType);

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
