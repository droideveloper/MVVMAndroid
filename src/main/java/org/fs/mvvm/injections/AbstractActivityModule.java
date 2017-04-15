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

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import dagger.Module;
import dagger.Provides;
import org.fs.mvvm.data.ViewType;
import org.fs.mvvm.utils.Preconditions;

@Module
public class AbstractActivityModule {

  @LayoutRes private final int layoutResourceId;
  protected final ViewType view;

  public AbstractActivityModule(ViewType view, @LayoutRes int layoutResourceId) {
    Preconditions.checkNotNull(view, "view is null");
    Preconditions.checkConditionMeet(layoutResourceId >= 0, "layout id must be positive");
    this.view = view;
    this.layoutResourceId = layoutResourceId;
  }

  @Provides @ForActivity public AppCompatActivity provideActivity() {
    return (AppCompatActivity) view.getContext();
  }

  @Provides @ForActivity public ViewDataBinding provideViewDataBinding(AppCompatActivity activity) {
    return DataBindingUtil.setContentView(activity, layoutResourceId);
  }

  @Provides @ForActivity public ViewType provideView() {
    return view;
  }
}
