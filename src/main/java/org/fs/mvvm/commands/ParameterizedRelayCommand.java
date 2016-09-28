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
package org.fs.mvvm.commands;

import org.fs.mvvm.utils.Objects;
import org.fs.mvvm.utils.Preconditions;

public final class ParameterizedRelayCommand<T> implements ICommand<T> {

  private final Action<T> execution;
  private final Predicate<T> isExecution;

  /**
   * Constructor with Execution provided.
   *
   * @param execution Action command provided.
   */
  public ParameterizedRelayCommand(Action<T> execution) {
    this(execution, (param) -> !Objects.isNullOrEmpty(param));
  }

  /**
   * Constructor with Execution and Parameter control provided.
   *
   * @param execution Action command provided.
   * @param isExecution Predicate command provided.
   */
  public ParameterizedRelayCommand(Action<T> execution, Predicate<T> isExecution) {
    Preconditions.checkNotNull(execution, "action is null");
    Preconditions.checkNotNull(isExecution, "check is null");
    this.execution = execution;
    this.isExecution = isExecution;
  }

  /**
   * Checks if action can be executed
   *
   * @param param object param.
   * @return true or false
   */
  @Override public boolean canExecute(T param) {
    return this.isExecution != null
        && this.isExecution.canExecute(param);
  }

  /**
   * Execute action.
   *
   * @param param object param.
   */
  @Override public void execute(T param) {
    this.execution.execute(param);
  }
}
