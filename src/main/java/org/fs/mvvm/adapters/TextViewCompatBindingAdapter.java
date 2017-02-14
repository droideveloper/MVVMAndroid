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
import org.fs.mvvm.listeners.OnSoftKeyboardAction;
import org.fs.mvvm.listeners.SimpleTextWatcher;
import org.fs.mvvm.utils.Invokes;
import org.fs.mvvm.utils.Objects;

import static android.R.attr.text;

public final class TextViewCompatBindingAdapter {

  private final static String BIND_REQUEST_FOCUS  = "bindings:requestFocus";

  private final static String BIND_BEFORE_CHANGED = "bindings:beforeChanged";
  private final static String BIND_AFTER_CHANGED  = "bindings:afterChanged";

  private final static String BIND_ON_SOFT_KEYBOARD_ACTION  = "bindings:onSoftKeyboardAction";

  private final static String BIND_IME_OPTIONS   = "bindings:imeOptions";

  private final static String BIND_FROM_OBJECT   = "bindings:fromObject";
  private final static String BIND_CONVERTER     = "bindings:converter";

  private final static String BIND_TEXT_ATTR_CHANGED = "bindings:textAttrChanged";

  private TextViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @BindingAdapter({ BIND_REQUEST_FOCUS })
  public static void viewTextViewRegisterRequestFocus(TextView viewText, boolean shouldReqeustFocus) {
    if (shouldReqeustFocus) {
      viewText.requestFocus();
    }
  }

  @BindingAdapter({ BIND_IME_OPTIONS })
  public static void viewTextViewRegisterImeOptions(TextView viewText, int imeOptions) {
    viewText.setImeOptions(imeOptions);
  }

  @BindingAdapter({ BIND_ON_SOFT_KEYBOARD_ACTION })
  public static void viewTextViewRegisterOnSoftKeyboardActionListener(TextView viewText, OnSoftKeyboardAction onSoftKeyboard) {
    if (onSoftKeyboard == null) {
      viewText.setOnEditorActionListener(null);
    } else {
      viewText.setOnEditorActionListener((v, id, e) -> onSoftKeyboard.onEditorAction(id));
    }
  }

  @BindingAdapter({ BIND_CONVERTER, BIND_FROM_OBJECT })
  public static <T, S extends CharSequence> void viewTextRegisterObject(TextView viewText, IConverter<T, S> converter, T object) {
    if (converter != null) {
      final S textStr = Invokes.invoke(o -> {
        return converter.convert(o, Locale.getDefault());
      }, object);
      if (!TextUtils.equals(textStr, viewText.getText())) {
        viewText.setText(textStr);
      }
    }
  }

  @BindingAdapter(
      value = {
        BIND_BEFORE_CHANGED,
        BIND_AFTER_CHANGED,
        BIND_TEXT_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewTextRegisterTextWatcher(TextView viewText, OnBeforeChanged beforeChanged, OnAfterChanged afterChanged, InverseBindingListener textAttrChanged) {
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
