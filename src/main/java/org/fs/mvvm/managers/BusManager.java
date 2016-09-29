/*
 * MVVM_Workspace Copyright (C) 2016 Fatih.
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

import org.fs.mvvm.utils.Preconditions;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public final class BusManager<T extends IEvent> {

  /**
   * Pool that we keep track of our busManager in hand.
   */
  private final Subject<T, T> rxBus = new SerializedSubject<>(PublishSubject.create());

  /**
   * sends object to every subscription we made for this bus.
   *
   * @param event event instance to send
   */
  public void send(T event) {
    Preconditions.checkNotNull(event, "event is null");
    rxBus.onNext(event);
  }

  /**
   * registers for this pool for event with only call callback.
   *
   * @param callback callback to be called for event.
   * @return Subscription instance holds onto it.
   */
  public Subscription register(Action1<T> callback) {
    Preconditions.checkNotNull(callback, "callback as action is null");
    return rxBus.subscribe(callback);
  }

  /**
   * registers for this pool for event with all call callback.
   *
   * @param callback callback to be called for event.
   * @return Subscription instance holds onto it.
   */
  public Subscription register(Subscriber<T> callback) {
    Preconditions.checkNotNull(callback, "callback as subscriber is null");
    return rxBus.subscribe(callback);
  }

  /**
   * unregisters from this pool.
   *
   * @param callback callback to be removed from pool.
   */
  public void unregister(Subscription callback) {
    Preconditions.checkNotNull(callback, "callback as subscription is null");
    if (!callback.isUnsubscribed()) {
      callback.unsubscribe();
    }
  }
}
