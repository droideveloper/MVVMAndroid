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

import org.fs.mvvm.utils.Objects;
import org.fs.mvvm.utils.Preconditions;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public final class BusManager {

  private final static BusManager IMPL = new BusManager();

  /**
   * Pool that we keep track of our busManager in hand.
   */
  private final Subject<Object, Object> rxBus = new SerializedSubject<>(PublishSubject.create());

  /**
   * sends object to every subscription we made for this bus.
   *
   * @param event event instance to send
   */
  public <T extends IEvent> void send(T event) {
    Preconditions.checkNotNull(event, "event is null");
    rxBus.onNext(event);
  }

  /**
   * registers for this pool for event with only call callback.
   *
   * @param callback callback to be called for event.
   * @return Subscription instance holds onto it.
   */
  public <T extends IEvent> Subscription register(Action1<T> callback) {
    Preconditions.checkNotNull(callback, "callback as action is null");
    return rxBus.subscribe((e) -> {
      T event = Objects.toObject(e);
      if (!Objects.isNullOrEmpty(event)) {
        callback.call(event);
      }
    });
  }

  /**
   * registers for this pool for event with all call callback.
   *
   * @param callback callback to be called for event.
   * @return Subscription instance holds onto it.
   */
  public <T extends IEvent> Subscription register(Subscriber<T> callback) {
    Preconditions.checkNotNull(callback, "callback as subscriber is null");
    //registered as this don't know better or not
    return rxBus.subscribe(new Subscriber<Object>() {
      @Override public void onStart() {
        super.onStart();
        callback.onStart();
      }

      @Override public void onNext(Object o) {
        T event = Objects.toObject(o);
        if (!Objects.isNullOrEmpty(event)) {
          callback.onNext(event);
        }
      }

      @Override public void onCompleted() {
        callback.onCompleted();
      }

      @Override public void onError(Throwable e) {
        callback.onError(e);
      }
    });
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

  /**
   * Send clone
   * @param event event to send be aware event must be implementation of IEvent
   * @param <E> type of Event
   */
  public static <E extends IEvent> void Send(E event) {
    if (!Objects.isNullOrEmpty(IMPL)) {
      IMPL.send(event);
    }
  }

  /**
   * Register clone
   * @param callback action to register on success event
   * @param <E> type of event
   * @return subscription of this callback
   */
  public static <E extends IEvent> Subscription Register(Action1<E> callback) {
    if (!Objects.isNullOrEmpty(IMPL)) {
      return IMPL.register(callback);
    }
    return null;
  }

  /**
   * Register clone
   * @param callback callback that registered for 3 method, success, error and completion
   * @param <E> type of event listened for
   * @return subscription of this callback
   */
  public static <E extends IEvent> Subscription Register(Subscriber<E> callback) {
    if (!Objects.isNullOrEmpty(IMPL)) {
      return IMPL.register(callback);
    }
    return null;
  }

  /**
   * Unregister clone
   * @param callback callback to be unregistered
   */
  public static void Unregister(Subscriber callback) {
    if (!Objects.isNullOrEmpty(IMPL)) {
      IMPL.unregister(callback);
    }
  }
}
