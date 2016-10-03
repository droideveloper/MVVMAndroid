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
package org.fs.mvvm.adapters;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.adapters.ListenerUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;
import java.util.Locale;
import org.fs.mvvm.R;
import org.fs.mvvm.data.IConverter;
import org.fs.mvvm.listeners.OnAfterChanged;
import org.fs.mvvm.listeners.OnBeforeChanged;
import org.fs.mvvm.listeners.SimpleTextWatcher;
import org.fs.mvvm.utils.Invokes;
import org.fs.mvvm.utils.Objects;

public final class TextViewCompatBindingAdapter {

  private final static String ANDROID_BEFORE_CHANGED = "android:beforeChanged";
  private final static String ANDROID_AFTER_CHANGED  = "android:afterChanged";

  private final static String ANDROID_FROM_OBJECT   = "android:fromObject";
  private final static String ANDROID_CONVERTER     = "android:converter";

  private final static String ANDROID_TEXT_ATTR_CHANGED = "android:textAttrChanged";

  private TextViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  /**
   * Sets TextView fromObject with any kind of object if
   * converter provided
   *
   * @param viewText textView
   * @param converter converter for this view
   * @param object object instance
   * @param <T> type of object in param
   * @param <S> type of object in result
   */
  @BindingAdapter({
      ANDROID_CONVERTER,
      ANDROID_FROM_OBJECT
  })
  public static <T, S extends CharSequence> void registerConvertor(TextView viewText, IConverter<T, S> converter, T object) {
    if (converter != null) {
      final S textStr = Invokes.invoke(o -> {
        return converter.convert(o, Locale.getDefault());
      }, object);
      if (!TextUtils.equals(textStr, viewText.getText())) {
        viewText.setText(textStr);
      }
    }
  }


  /**
   * Registers TextView with provided listeners or listener
   * can be done by combinations
   *
   * @param viewText textView instance for watch on change
   * @param beforeChanged before listener
   * @param afterChanged after listener
   */
  @BindingAdapter(value = {
      ANDROID_BEFORE_CHANGED,
      ANDROID_AFTER_CHANGED,
      ANDROID_TEXT_ATTR_CHANGED
  }, requireAll = false)
  public static void registerTextWatcher(TextView viewText, OnBeforeChanged beforeChanged, OnAfterChanged afterChanged, InverseBindingListener textAttrChanged) {
    final TextWatcher newListener;
    if (Objects.isNullOrEmpty(beforeChanged) && Objects.isNullOrEmpty(afterChanged) && Objects.isNullOrEmpty(textAttrChanged)) {
      newListener = null;
    } else {
      newListener = new SimpleTextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          if (beforeChanged != null) {
            beforeChanged.beforeChanged(s, start, count, after);
          }
        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
          if (textAttrChanged != null) {
            textAttrChanged.onChange();
          }
        }

        @Override public void afterTextChanged(Editable s) {
          if (afterChanged != null) {
            afterChanged.afterChanged(s);
          }
        }
      };
    }
    final TextWatcher oldListener = ListenerUtil.trackListener(viewText, newListener, R.id.textWatcher);
    if (oldListener != null) {
      viewText.removeTextChangedListener(oldListener);
    }
    if (newListener != null) {
      viewText.addTextChangedListener(newListener);
    }
  }
}
