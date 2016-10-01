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
package org.fs.mvvm.injections;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import dagger.Module;
import dagger.Provides;
import org.fs.mvvm.utils.Objects;
import org.fs.mvvm.utils.Preconditions;

@Module
public class AbstractActivityModule {

  @LayoutRes private final int layoutResourceId;
  private final Object view;

  public AbstractActivityModule(Object view, @LayoutRes int layoutResourceId) {
    Preconditions.checkNotNull(view, "view is null");
    Preconditions.checkConditionMeet(layoutResourceId >= 0, "layout id must be positive");
    this.view = view;
    this.layoutResourceId = layoutResourceId;
  }

  @Provides @ForActivity public Activity provideActivity() {
    return Objects.toObject(view);
  }

  @Provides @ForActivity public ViewDataBinding provideViewDataBinding(Activity activity) {
    return DataBindingUtil.setContentView(activity, layoutResourceId);
  }

  @Provides @ForActivity public Object provideView() {
    return view;
  }
}