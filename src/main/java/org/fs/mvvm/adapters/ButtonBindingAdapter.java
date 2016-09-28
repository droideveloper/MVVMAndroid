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
package org.fs.mvvm.adapters;

import android.databinding.BindingAdapter;
import android.widget.Button;
import org.fs.mvvm.commands.ParameterizedRelayCommand;
import org.fs.mvvm.commands.RelayCommand;
import org.fs.mvvm.utils.Preconditions;

public final class ButtonBindingAdapter {

  private final static String ANDROID_COMMAND = "android:command";
  private final static String ANDROID_COMMAND_PARAMETER = "android:commandParameter";

  private ButtonBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Registers button with RelayCommand that has no parameters
   *
   * @param buttonView view to register for click
   * @param command command to execute
   */
  @BindingAdapter(
      ANDROID_COMMAND
  )
  public static void registerCommand(Button buttonView, RelayCommand command) {
    Preconditions.checkNotNull(command, "command is null");
    buttonView.setOnClickListener(view -> command.execute(null));
  }

  /**
   * Registers button with ParameterizedRelayCommand that has parameter type of T.
   *
   * @param buttonView view to register for click
   * @param command command to execute
   * @param param command parameter
   * @param <T> type of parameter
   */
  @BindingAdapter({
      ANDROID_COMMAND,
      ANDROID_COMMAND_PARAMETER
  })
  public static <T> void registerCommandAndParameter(Button buttonView, ParameterizedRelayCommand<T> command, T param) {
    Preconditions.checkNotNull(command, "command is null");
    buttonView.setOnClickListener(view -> {
      if (command.canExecute(param)) {
        command.execute(param);
      }
    });
  }
}
