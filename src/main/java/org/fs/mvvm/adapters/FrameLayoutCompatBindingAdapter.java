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
import android.support.annotation.AnimRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import org.fs.mvvm.R;

public class FrameLayoutCompatBindingAdapter {

  private final static String BIND_FRAGMENT         = "bindings:fragment";
  private final static String BIND_FRAGMENT_MANAGER = "bindings:fragmentManager";
  private final static String BIND_ENTER_ANIM       = "bindings:enterAnim";
  private final static String BIND_EXIT_ANIM        = "bindings:exitAnim";
  private final static String BIND_ANIM_REVERSE     = "bindings:animReverse";

  private FrameLayoutCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @BindingAdapter(
      value = {
          BIND_FRAGMENT,
          BIND_FRAGMENT_MANAGER,
          BIND_ENTER_ANIM,
          BIND_EXIT_ANIM,
          BIND_ANIM_REVERSE
      },
      requireAll = false
  )
  public static void viewFrameLayoutReplaceFragment(FrameLayout viewFrameLayout, Fragment fragment,
      FragmentManager fragmentManager, @AnimRes int enterAnim, @AnimRes int exitAnim, boolean animReverse) {
    if (viewFrameLayout.getId() == -1) {
      viewFrameLayout.setId(R.id.viewFragment_ContainerLayout);
    }
    if (fragment != null && fragmentManager != null) {
      FragmentTransaction trans = fragmentManager.beginTransaction();
      trans.replace(viewFrameLayout.getId(), fragment);
      if (enterAnim != -1 && exitAnim != -1) {
        if (animReverse) {
          trans.setCustomAnimations(enterAnim, exitAnim, enterAnim, exitAnim);
        } else {
          trans.setCustomAnimations(enterAnim, exitAnim);
        }
      }
      trans.commit();
    }
  }
}
