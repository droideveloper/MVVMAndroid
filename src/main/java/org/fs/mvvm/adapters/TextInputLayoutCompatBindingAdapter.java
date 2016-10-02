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
import android.databinding.adapters.ListenerUtil;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Locale;
import org.fs.mvvm.R;
import org.fs.mvvm.data.IValidator;
import org.fs.mvvm.data.Validation;
import org.fs.mvvm.listeners.SimpleTextWatcher;

public final class TextInputLayoutCompatBindingAdapter {

  private final static String ANDROID_VALIDATOR     = "android:validator";
  private final static String ANDROID_ERROR_STRING  = "android:errorString";

  private TextInputLayoutCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Registers validator on TextInputLayout with given String validator
   * if it is not valid we provide errorString to be shown
   *
   * @param viewTextLayout viewTextLayout we look through
   * @param validator validator that shows us how we do it
   * @param errorString error String
   */
  @BindingAdapter({
      ANDROID_VALIDATOR,
      ANDROID_ERROR_STRING
  })
  public static void registerValidator(TextInputLayout viewTextLayout, IValidator<String> validator, String errorString) {
    TextView viewText = null;
    for (int i = 0, z = viewTextLayout.getChildCount(); i < z; i ++) {
      View child = viewTextLayout.getChildAt(i);
      if (child instanceof EditText) {
        viewText = (TextView) child;
        break;
      }
    }
    if (viewText != null) {
      if (validator == null && errorString == null) {
        final TextWatcher oldListener = ListenerUtil.getListener(viewText, R.id.textLayoutWatcher);
        if (oldListener != null) {
          viewText.removeTextChangedListener(oldListener);
        }
      } else {
        final TextWatcher newListener = new SimpleTextWatcher() {
          //change track on edit text of that view
          @Override public void afterTextChanged(Editable s) {
            Validation validation = null;
            if (validator != null) {
              validation = validator.validate(s.toString(), Locale.getDefault());
            }
            if (validation != null) {
              if (!validation.isSuccess()) {
                viewTextLayout.setErrorEnabled(true);
                viewTextLayout.setError(errorString);
              } else {
                viewTextLayout.setErrorEnabled(false);
                viewTextLayout.setError(null);
              }
            }
          }
        };
        //using different name because txtWatcher id might be registered on same view with different story on it
        //for instance that we might be watching it for textChange on other property
        //this way we do not interrupt other listeners just to be safe
        final TextWatcher oldListener = ListenerUtil.trackListener(viewText, newListener, R.id.textLayoutWatcher);
        if (oldListener != null) {
          viewText.removeTextChangedListener(oldListener);
        }
        viewText.addTextChangedListener(newListener);
      }
    }
  }
}
