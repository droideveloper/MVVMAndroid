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
package org.fs.mvvm.adapters;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.support.v7.widget.CardView;
import android.util.TypedValue;

public class CardViewCompatBindingAdapter {

  private static final String BINDING_ELEVATION = "bindings:elevationCompat";
  private static final String BINDING_ELEVATION_ATTR_CHANGED = "bindings:elevationCompatAttrChanged";

  private CardViewCompatBindingAdapter() {
    throw  new RuntimeException("you can not have instance of this class.");
  }

  @InverseBindingAdapter(attribute = BINDING_ELEVATION,
    event = BINDING_ELEVATION_ATTR_CHANGED)
  public static int viewRetrieveCardElevation(CardView viewCard) {
    return Math.round(viewCard.getCardElevation());
  }

  @BindingAdapter(
    value = {
      BINDING_ELEVATION,
      BINDING_ELEVATION_ATTR_CHANGED
    }, requireAll = false
  )
  public static void viewRegisterCardElevation(CardView viewCard, int value, InverseBindingListener elevationCompatAttrChanged) {
    if (value >= 0) {
      float dpi = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, viewCard.getResources().getDisplayMetrics());
      if (dpi != viewCard.getCardElevation()) {
        if (elevationCompatAttrChanged != null) {
          elevationCompatAttrChanged.onChange();
        }
      }
      viewCard.setCardElevation(dpi);
    }
  }
}
