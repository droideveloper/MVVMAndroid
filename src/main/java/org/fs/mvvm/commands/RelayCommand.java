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

import org.fs.mvvm.utils.Preconditions;

public final class RelayCommand implements ICommand<Object> {

  private final IAction execution;

  /**
   * Public constructor that takes one argument.
   *
   * @param execution action to execute.
   */
  public RelayCommand(IAction execution) {
    Preconditions.checkNotNull(execution, "action is null.");
    this.execution = execution;
  }

  /**
   * Can execute command.
   *
   * @param param object param.
   * @return true or false
   */
  @Override public boolean canExecute(Object param) {
    return true;
  }

  /**
   * Execute command.
   *
   * @param param object param.
   */
  @Override public void execute(Object param) {
    this.execution.execute();
  }
}
