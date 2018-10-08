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
package org.fs.mvvm.commands;

import org.fs.mvvm.utils.Objects;
import org.fs.mvvm.utils.Preconditions;

public final class ParameterizedRelayCommandType<T> implements CommandType<T> {

  private final ParameterizedActionType<T> execution;
  private final PredicateType<T> isExecution;

  public ParameterizedRelayCommandType(ParameterizedActionType<T> execution) {
    this(execution, param -> !Objects.isNullOrEmpty(param));
  }

  public ParameterizedRelayCommandType(ParameterizedActionType<T> execution, PredicateType<T> isExecution) {
    Preconditions.checkNotNull(execution, "action is null");
    Preconditions.checkNotNull(isExecution, "check is null");
    this.execution = execution;
    this.isExecution = isExecution;
  }

  @Override public boolean canExecute(T param) {
    return this.isExecution != null
        && this.isExecution.canExecute(param);
  }

  @Override public void execute(T param) {
    this.execution.execute(param);
  }
}
