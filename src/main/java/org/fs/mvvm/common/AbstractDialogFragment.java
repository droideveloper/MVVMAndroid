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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.inject.Inject;
import org.fs.mvvm.data.AbstractViewModel;

public abstract class AbstractDialogFragment <V extends AbstractViewModel<?>>
    extends DialogFragment implements HasSupportFragmentInjector  {

  @Inject DispatchingAndroidInjector<Fragment> supportFragmentInjector;
  @Inject V viewModel;

  private ViewDataBinding viewDataBinding;

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return supportFragmentInjector;
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
      Bundle savedInstanceState) {
    viewDataBinding = DataBindingUtil.inflate(inflater, layoutRes(), container, false);
    return viewDataBinding.getRoot();
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    AndroidSupportInjection.inject(this);
    super.onActivityCreated(savedInstanceState);

    viewDataBinding.setVariable(viewModelRes(), viewModel);
    viewModel.restoreState(savedInstanceState != null ? savedInstanceState: getArguments());
    viewModel.onCreate();
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

  @LayoutRes protected abstract int layoutRes();
  protected abstract int viewModelRes();

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

  public String getStringRes(@StringRes int stringId, Object... args) {
    return getString(stringId, args);
  }

  public Context getContext() {
    return getActivity();
  }

  public boolean isAvailable() {
    return getActivity() != null && isAdded();
  }

  public void finish() {
    throw new IllegalArgumentException("fragment instances does not support finish options");
  }

  @Override public final void dismiss() {
    super.dismiss();//change of state loss
  }

  @Override public final int show(FragmentTransaction transaction, String tag) {
    return transaction.add(this, tag).commit();
  }

  @Override public final void show(FragmentManager manager, String tag) {
    FragmentTransaction trans = manager.beginTransaction();
    show(trans, tag);
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
