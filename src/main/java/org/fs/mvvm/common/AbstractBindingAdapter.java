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
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.fs.mvvm.core.EventType;
import org.fs.mvvm.utils.Objects;
import org.fs.mvvm.utils.Preconditions;

public abstract class AbstractBindingAdapter<D extends BaseObservable, V extends AbstractBindingHolder<D>> extends
    BaseAdapter implements Consumer<EventType> {

  public final static int SINGLE_SELECTION_MODE   = 0x01;
  public final static int MULTIPLE_SELECTION_MODE = 0x02;

  private final ObservableList<D> itemSource;
  private final WeakReference<Context> contextReference;
  private final PublishSubject<SelectedEventType<D>> observer;
  private final List<Integer> selection;
  private final int selectionMode;

  private Consumer<Integer> singlePositionCallback;
  private Consumer<List<Integer>> multiPositionCallback;

  private Consumer<D> singleItemCallback;
  private Consumer<List<D>> multiItemCallback;

  private Disposable disposable;

  public AbstractBindingAdapter(Context context, ObservableList<D> itemSource, int selectionMode) {
    Preconditions.checkNotNull(itemSource, "itemSource is null");
    Preconditions.checkNotNull(context, "context is null");
    Preconditions.checkConditionMeet(selectionMode >= SINGLE_SELECTION_MODE && selectionMode <= MULTIPLE_SELECTION_MODE,
        "selectionMode is invalid");
    this.observer = PublishSubject.create();
    this.itemSource = itemSource;
    this.contextReference = new WeakReference<>(context);
    this.selectionMode = selectionMode;
    this.selection = new ArrayList<>();
    disposable = observer.subscribe(this);
    this.itemSource.addOnListChangedCallback(itemSourceObserver);
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

  public final void clearAll() {
    if (contextReference != null) {
      contextReference.clear();
    }
    if (!disposable.isDisposed()) {
      disposable.dispose();
      disposable = null;
    }
    if (itemSource != null) {
      itemSource.removeOnListChangedCallback(itemSourceObserver);
    }
  }

  @Override public int getCount() {
    return itemSource != null ? itemSource.size() : 0;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final int viewType = getItemViewType(position);
    final LayoutInflater factory = factory();
    if (factory == null) {
      throw new IllegalArgumentException("we can not created LayoutInflater factory since context might be garbage collected.");
    }
    if (convertView == null) {
      ViewDataBinding binding = DataBindingUtil.inflate(factory, layoutResource(viewType), parent, false);
      V viewHolder = createDataViewHolder(binding, observer, viewType);
      convertView = binding.getRoot();
      convertView.setTag(viewHolder);
    }
    V viewHolder = Objects.toObject(convertView.getTag());
    if (viewHolder == null) {
      //we try to reload those values via recursive call
      return getView(position, null, parent);
    }
    final D item = getItemAt(position);
    viewHolder.setActivated(isSelected(position));
    bindDataViewHolder(item, viewHolder, position);
    return convertView;
  }


  @Override public void accept(EventType evt) throws Exception {
    if (evt instanceof SelectedEventType) {
      SelectedEventType<D> event = (SelectedEventType<D>) evt;
      if (isSingleMode()) {
        addSelectionIndex(event.selectedItemAdapterPosition(), true);
      } else {
        addSelectionIndex(event.selectedItemAdapterPosition(), false);
      }
    }
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

  private boolean isSelected(int position) {
    return selection.contains(position);
  }

  private void addSelectionIndex(int position, boolean clearBefore) {
    //don't have it so select it.
    if (!selection.contains(position)) {
      if (clearBefore) {
        for(int i = 0, z = selection.size(); i < z; i ++) {
          notifyDataSetChanged();
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
    notifyDataSetChanged();
  }

  public final boolean isSingleMode() {
    return selectionMode == SINGLE_SELECTION_MODE;
  }

  protected abstract boolean isLogEnabled();

  protected abstract String getClassTag();

  protected abstract void bindDataViewHolder(D item, V viewHolder, int position);

  protected abstract V createDataViewHolder(ViewDataBinding binding, Observer<SelectedEventType<D>> observer, int viewType);

  @LayoutRes
  protected abstract int layoutResource(int viewType);

  private Context getContext() {
    return contextReference != null ? contextReference.get() : null;
  }

  private LayoutInflater factory() {
    Context context = getContext();
    if (context != null) {
      return LayoutInflater.from(context);
    }
    return null;
  }

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
