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

  /**
   * Pool that we keep track of our busManager in hand.
   */
  private final PublishSubject<IEvent> rxBus = PublishSubject.create();

  /**
   * sends object to every subscription we made for this bus.
   *
   * @param event event instance to send
   */
  public <T extends IEvent> void post(T event) {
    Preconditions.checkNotNull(event, "event is null");
    rxBus.onNext(event);
  }

  /**
   * registers for this pool for event with only call consumer.
   *
   * @param consumer consumer to be called for event.
   * @return Subscription instance holds onto it.
   */
  public Disposable register(Consumer<? super IEvent> consumer) {
    Preconditions.checkNotNull(consumer, "consumer as action is null");
    return rxBus.subscribe(consumer);
  }


  /**
   * unregisters from this pool.
   *
   * @param disposable disposable to be removed from pool.
   */
  public void unregister(Disposable disposable) {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  /**
   * Send clone
   * @param event event to send be aware event must be implementation of IEvent
   * @param <E> type of Event
   */
  public static <E extends IEvent> void send(E event) {
    IMPL.post(event);
  }

  /**
   * Register clone
   * @param callback callback that registered for 3 method, success, error and completion
   * @return subscription of this callback
   */
  public static Disposable add(Consumer<? super IEvent> callback) {
    return IMPL.register(callback);
  }

  /**
   * Unregister clone
   * @param disposable callback to be unregistered
   */
  public static void remove(Disposable disposable) {
    IMPL.unregister(disposable);
  }
}
