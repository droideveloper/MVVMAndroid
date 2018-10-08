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
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.inject.Inject;
import org.fs.mvvm.data.AbstractViewModel;

public abstract class AbstractActivity<V extends AbstractViewModel<?>> extends
    AppCompatActivity implements HasSupportFragmentInjector {

  @Inject DispatchingAndroidInjector<Fragment> supportFragmentInjector;
  @Inject V viewModel;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);

    ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, layoutRes());
    viewDataBinding.setVariable(viewModelRes(), viewModel);
    viewModel.restoreState(savedInstanceState != null ? savedInstanceState: getIntent().getExtras());
    viewModel.onCreate();
  }

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return supportFragmentInjector;
  }

  @Override public void onStart() {
    super.onStart();
    viewModel.onStart();
  }

  @Override public void onStop() {
    viewModel.onStop();
    super.onStop();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return viewModel.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
  }

  @Override public void onResume() {
    super.onResume();
    viewModel.onResume();
  }

  @Override public void onPause() {
    viewModel.onPause();
    super.onPause();
  }

  @Override public void onDestroy() {
    viewModel.onDestroy();
    super.onDestroy();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    viewModel.activityResult(requestCode, resultCode, data);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    viewModel.requestPermissionResult(requestCode, permissions, grantResults);
  }

  @Override public void onBackPressed() {
    viewModel.onBackPressed();
  }

  public void showProgress() {
    throw new RuntimeException("You should implement this method and not call super");
  }

  public void hideProgress() {
    throw new RuntimeException("You should implement this method and not call super");
  }

  public void showError(String errorString) {
    final View view = getView();
    if(view != null) {
      Snackbar.make(view, errorString, Snackbar.LENGTH_LONG)
          .show();
    }
  }

  public void showError(String errorString, String actionTextString, final View.OnClickListener callback) {
    final View view = getView();
    if(view != null) {
      final Snackbar snackbar = Snackbar.make(view, errorString, Snackbar.LENGTH_LONG);
      snackbar.setAction(actionTextString, v -> {
        if (callback != null) {
          callback.onClick(v);
        }
        snackbar.dismiss();
      });
      snackbar.show();
    }
  }

  @LayoutRes protected abstract int layoutRes();
  protected abstract int viewModelRes();

  public String getStringRes(@StringRes int stringId, Object... args) {
    return getString(stringId, args);
  }

  public Context getContext() {
    return this;
  }

  public boolean isAvailable() {
    return !isFinishing();
  }

  @Nullable protected View getView() {
    return findViewById(android.R.id.content);
  }

  protected abstract String getClassTag();
  protected abstract boolean isLogEnabled();

  protected void log(final String str) {
    log(Log.DEBUG, str);
  }

  protected void log(Throwable error) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter  printWriter  = new PrintWriter(stringWriter);
    error.printStackTrace(printWriter);
    log(Log.ERROR, stringWriter.toString());
  }

  protected void log(final int lv, final String str) {
    if(isLogEnabled()) {
      Log.println(lv, getClassTag(), str);
    }
  }
}
