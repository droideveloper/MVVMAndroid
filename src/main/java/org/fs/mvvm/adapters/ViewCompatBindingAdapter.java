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

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.databinding.BindingAdapter;
import android.databinding.adapters.ListenerUtil;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fs.mvvm.R;
import org.fs.mvvm.commands.CommandType;
import org.fs.mvvm.commands.RelayCommandType;
import org.fs.mvvm.data.ConverterType;
import org.fs.mvvm.metadata.BindingCompat;
import org.fs.mvvm.utils.Objects;

public final class ViewCompatBindingAdapter {

  private final static Pattern validator = Pattern.compile("binding=(\\w*, )relativeSource=\\{source=(\\w*, )ancestor=\\{(.+)\\}\\}");
  private final static String splitter = ", ";

  private final static String BIND_BIND               = "bindings:bind";
  private final static String BIND_PARSER             = "bindings:parser";

  private final static String BIND_COMMAND            = "bindings:command";
  private final static String BIND_COMMAND_PARAMETER  = "bindings:commandParameter";

  private final static String BIND_ANIM               = "bindings:anim";
  private final static String BIND_ANIM_LISTENER      = "bindings:animListener";

  private final static String BIND_ANIMATOR           = "bindings:animator";
  private final static String BIND_INTERPOLATOR       = "bindings:interpolator";
  private final static String BIND_ANIMATOR_LISTENER  = "bindings:animatorListener";

  private final static String BIND_NOTIFY_TEXT        = "bindings:notifyText";
  private final static String BIND_ACTION_TEXT        = "bindings:actionText";
  private final static String BIND_RELAY_COMMAND      = "bindings:relayCommand";

  private ViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  @BindingAdapter(value = {
      BIND_BIND,
      BIND_PARSER
  }, requireAll = false)
  public static <V1, V2> void viewRegisterBinding(View view, String str, ConverterType<V2, V1> parser) {
    if (!Objects.isNullOrEmpty(str)) {
      final Matcher matcher = validator.matcher(str);
      if (matcher.find()) {
        final String[] binding = str.split(splitter, 2);
        if (binding.length != 2) {
          throw new RuntimeException("invalid property definition check bindings");
        }
        BindingCompat.bind(binding, view, parser);
      } else {
        throw new RuntimeException("you should use valid binding options");
      }
    }
  }

  @BindingAdapter(
    value = {
      BIND_ANIM,
      BIND_INTERPOLATOR,
      BIND_ANIM_LISTENER
    },
    requireAll = false
  )
  public static void viewRegisterAnim(View view, @AnimRes int anim, Interpolator interpolator, Animation.AnimationListener listener) {
    view.clearAnimation();
    if (anim > 0) {
      final Animation animation = AnimationUtils.loadAnimation(view.getContext(), anim);
      if (animation != null) {
        if (interpolator != null) {
          animation.setInterpolator(interpolator);
        }
        if (listener != null) {
          animation.setAnimationListener(listener);
        }
        view.setAnimation(animation);
        animation.start();
      }
    }
  }
  
  @BindingAdapter(
    value = {
      BIND_ANIMATOR,
      BIND_INTERPOLATOR,
      BIND_ANIMATOR_LISTENER
    },
    requireAll = false
  )
  public static void viewRegisterAnimator(View view, @AnimatorRes int anim, Interpolator interpolator, Animator.AnimatorListener listener) {
    view.clearAnimation();
    if (anim > 0) {
      final Animator animator = AnimatorInflater.loadAnimator(view.getContext(), anim);
      if (animator != null) {
        if (interpolator != null) {
          animator.setInterpolator(interpolator);
        }
        if (listener != null) {
          animator.addListener(listener);
        }
        animator.setTarget(view);
        animator.start();
      }
    }
  }

  @BindingAdapter({ BIND_NOTIFY_TEXT })
  public static <S extends CharSequence> void viewRegisterSnackbar(View view, S notifyText) {
    if (!Objects.isNullOrEmpty(notifyText)) {
      Snackbar.make(view, notifyText, Snackbar.LENGTH_LONG)
          .show();
    }
  }

  @BindingAdapter({ BIND_NOTIFY_TEXT, BIND_ACTION_TEXT, BIND_RELAY_COMMAND })
  public static <S extends CharSequence, T extends CharSequence> void viewRegisterSnackbar(View view, S notifyText, T actionText, RelayCommandType command) {
    if (!Objects.isNullOrEmpty(notifyText)) {
      final Snackbar snackbar = Snackbar.make(view, notifyText, Snackbar.LENGTH_LONG);
      snackbar.setAction(actionText, v -> {
        command.execute(null);
        snackbar.dismiss();
      });
      snackbar.show();
    }
  }

  @BindingAdapter(
      value = {
        BIND_COMMAND,
        BIND_COMMAND_PARAMETER
      },
      requireAll = false
  )
  public static <T> void viewRegisterCommandWithOrWithoutParameter(View view, CommandType<T> command, T param) {
    final View.OnClickListener newListener;
    if (command == null) {
      newListener = null;
    } else {
      final View.OnClickListener oldListener = ListenerUtil.getListener(view, R.id.onClickListener);
      newListener = v -> {
        if (command.canExecute(param)) {
          command.execute(param);
        }
        if (oldListener != null) {
          oldListener.onClick(view);
        }
      };
    }
    view.setOnClickListener(newListener);
  }
}
