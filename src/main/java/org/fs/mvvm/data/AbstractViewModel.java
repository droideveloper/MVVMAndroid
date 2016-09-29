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
package org.fs.mvvm.data;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.view.MenuItem;
import org.fs.mvvm.utils.Preconditions;

public abstract class AbstractViewModel<V extends IView> extends BaseObservable {

  protected final V view;

  public AbstractViewModel(V view) {
    Preconditions.checkNotNull(view, "view is null");
    this.view = view;
  }

  /* lifecycle might needed from viewModel */
  public void onResume()  {}
  public void onPause()   {}
  public void onStart()   {}
  public void onStop()    {}
  public void onCreate()  {}
  public void onDestroy() {}

  public void restoreState(Bundle restoreState) {}
  public void storeState(Bundle storeState)     {}
  public void activityResult(int requestCode, int resultCode, Intent data) {}
  public void requestPermissionResult(int requestCode, String[] permissions, int[] grants) {}
  public boolean onOptionsItemSelected(MenuItem item) { return false; }
}
