/*
 * MVVM Android Copyright (C) 2017 Fatih.
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
package org.fs.mvvm.utils;

import io.reactivex.CompletableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;
import org.fs.mvvm.data.ViewType;

public final class RxUtility {

  public static <T> ObservableTransformer<T, T> asyncObservable() {
    return source -> source
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }

  public static <T> ObservableTransformer<T, T> asyncObservableAndUI(@Nullable final ViewType view) {
    return source -> source
      .compose(RxUtility.asyncObservable())
      .doOnSubscribe(d -> {
        if (view != null && view.isAvailable()) {
          view.showProgress();
        }
      }).doFinally(() -> {
        if (view != null && view.isAvailable()) {
          view.hideProgress();
        }
      });
  }

  public static <T> SingleTransformer<T, T> asyncSingle() {
    return source -> source
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }

  public static <T> SingleTransformer<T, T> asyncSingleAndUI(@Nullable final ViewType view) {
    return source -> source
      .compose(asyncSingle())
      .doOnSuccess(d -> {
        if (view != null && view.isAvailable()) {
          view.showProgress();
        }
      }).doFinally(() -> {
        if (view != null && view.isAvailable()) {
          view.hideProgress();
        }
      });
  }

  public static <T> MaybeTransformer<T, T> asyncMaybe() {
    return source -> source
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }

  public static <T> MaybeTransformer<T, T> asyncMaybeAndUI(@Nullable final ViewType view) {
    return source -> source
      .compose(asyncMaybe())
      .doOnSubscribe(d -> {
        if (view != null && view.isAvailable()) {
          view.showProgress();
        }
      })
      .doFinally(() -> {
        if (view != null && view.isAvailable()) {
          view.hideProgress();
        }
      });
  }

  public static CompletableTransformer asyncCompletable() {
    return source -> source
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }

  public static CompletableTransformer asyncCompletableAndUI(@NonNull final ViewType view) {
    return source -> source
      .compose(asyncCompletable())
      .doOnSubscribe(d -> {
        if (view != null && view.isAvailable()) {
          view.showProgress();
        }
      })
      .doFinally(() -> {
        if (view != null && view.isAvailable()) {
          view.hideProgress();
        }
      });
  }

  private RxUtility() {
    throw new RuntimeException("You can not have instance of this type");
  }
}