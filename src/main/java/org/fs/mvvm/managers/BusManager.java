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
package org.fs.mvvm.managers;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import org.fs.mvvm.utils.Preconditions;

public final class BusManager {

  private final static BusManager IMPL = new BusManager();
  private final PublishSubject<EventType> rxBus = PublishSubject.create();

  <T extends EventType> void post(T event) {
    Preconditions.checkNotNull(event, "event is null");
    rxBus.onNext(event);
  }

  Disposable register(Consumer<? super EventType> consumer) {
    Preconditions.checkNotNull(consumer, "consumer as action is null");
    return rxBus.subscribe(consumer);
  }

  void unregister(Disposable disposable) {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  public static <E extends EventType> void send(E event) {
    IMPL.post(event);
  }

  public static Disposable add(Consumer<? super EventType> callback) {
    return IMPL.register(callback);
  }

  public static void remove(Disposable disposable) {
    IMPL.unregister(disposable);
  }
}
