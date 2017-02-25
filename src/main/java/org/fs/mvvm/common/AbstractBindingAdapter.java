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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.fs.mvvm.managers.BusManager;
import org.fs.mvvm.managers.IEvent;
import org.fs.mvvm.managers.SelectedEvent;
import org.fs.mvvm.utils.Objects;
import org.fs.mvvm.utils.Preconditions;

public abstract class AbstractBindingAdapter<D extends BaseObservable, V extends AbstractBindingHolder<D>> extends
    BaseAdapter implements Consumer<IEvent> {

  public final static int SINGLE_SELECTION_MODE   = 0x01;
  public final static int MULTIPLE_SELECTION_MODE = 0x02;

  private final ObservableList<D>       itemSource;
  private final WeakReference<Context>  contextReference;
  private final BusManager              busManager;
  private final List<Integer>           selection;
  private final int                     selectionMode;

  private Consumer<Integer>             singlePositionCallback;
  private Consumer<List<Integer>>       multiPositionCallback;

  private Consumer<D>                   singleItemCallback;
  private Consumer<List<D>>             multiItemCallback;

  private Disposable disposable;

  /**
   * Public constructor with selectionMode ex. singleMode or multiMode
   * itemSource and context provided.
   *
   * @param context context instance we provide.
   * @param itemSource ObservableList which is pretty easy use in next
   * @param selectionMode selectionMode to track on adapter
   */
  public AbstractBindingAdapter(Context context, ObservableList<D> itemSource, int selectionMode) {
    Preconditions.checkNotNull(itemSource, "itemSource is null");
    Preconditions.checkNotNull(context, "context is null");
    Preconditions.checkConditionMeet(selectionMode >= SINGLE_SELECTION_MODE && selectionMode <= MULTIPLE_SELECTION_MODE,
        "selectionMode is invalid");
    this.busManager = new BusManager();
    this.itemSource = itemSource;
    this.contextReference = new WeakReference<>(context);
    this.selectionMode = selectionMode;
    this.selection = new ArrayList<>();
    disposable = busManager.register(this);
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

  /**
   * when we done with all we need to
   * call this in order to clear stuff
   */
  public final void clearAll() {
    if (contextReference != null) {
      contextReference.clear();
    }
    if (disposable != null) {
      busManager.unregister(disposable);
      disposable = null;
    }
    if (itemSource != null) {
      itemSource.removeOnListChangedCallback(itemSourceObserver);
    }
  }

  /**
   * size of the itemSource
   * @return int size of the itemSource
   */
  @Override public int getCount() {
    return itemSource != null ? itemSource.size() : 0;
  }

  /**
   * CreateView old school createView method with ugly storage I would not advise you use this
   * crappy adapter thing.
   *
   * @param position position to createView or storeView from loaded
   * @param convertView convertView view instance we have previously or not
   * @param parent parent for inflating
   * @return convertView instance
   */
  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final int viewType = getItemViewType(position);
    final LayoutInflater factory = factory();
    if (factory == null) {
      throw new IllegalArgumentException("we can not created LayoutInflater factory since context might be garbage collected.");
    }
    if (convertView == null) {
      ViewDataBinding binding = DataBindingUtil.inflate(factory, layoutResource(viewType), parent, false);
      V viewHolder = createDataViewHolder(binding, busManager, viewType);
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

  /**
   * with viewHolder callback provides bridge with it's child without ugly reference on others
   * @param evt selectedEvent here provided.
   */
  @Override public void accept(IEvent evt) throws Exception {
    if (evt instanceof SelectedEvent) {
      SelectedEvent<D> event = (SelectedEvent<D>) evt;
      if (isSingleMode()) {
        addSelectionIndex(event.selectedItemAdapterPosition(), true);
      } else {
        addSelectionIndex(event.selectedItemAdapterPosition(), false);
      }
    }
  }

  /**
   * Item provided by itemSource
   *
   * @param position position of item in itemSource
   * @return type of D as item
   */
  protected D getItemAt(int position) {
    if (position >= 0 && position < getCount()) {
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
            .forEach(x -> notifyDataSetChanged());
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
        multiItemCallback.accept(StreamSupport.stream(selection)
                .map(itemSource::get)
                .collect(Collectors.toList())
        );
      } catch (Exception error) {
        throw new AndroidRuntimeException("selection not executable " + selection, error);
      }
    }
    //notification
    if (singlePositionCallback != null) {
      try {
        singlePositionCallback.accept(StreamSupport.stream(selection)
                .findFirst()
                .orElse(-1)
        );
      } catch (Exception error) {
        throw new AndroidRuntimeException("selection not executable " + selection, error);
      }
    }
    //notification
    if (singleItemCallback != null) {
      try {
        singleItemCallback.accept(StreamSupport.stream(selection)
            .filter(x -> x >= 0)
            .map(itemSource::get)
            .findFirst()
            .orElse(null));
      } catch (Exception error) {
        throw new AndroidRuntimeException("selection not executable " + selection, error);
      }
    }
    notifyDataSetChanged();
  }

  /**
   * Returns if selection is singleMode or not
   * @return true or false
   */
  public final boolean isSingleMode() {
    return selectionMode == SINGLE_SELECTION_MODE;
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
  protected abstract void bindDataViewHolder(D item, V viewHolder, int position);

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
   * Context provided stored weakly
   * @return context or null
   */
  private Context getContext() {
    return contextReference != null ? contextReference.get() : null;
  }

  /**
   * LayoutInflater created with context if context is null then inflater is null
   * @return null or layoutInflater instance
   */
  private LayoutInflater factory() {
    Context context = getContext();
    if (context != null) {
      return LayoutInflater.from(context);
    }
    return null;
  }

  /**
   * itemSource will be tracked by listener
   */
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
