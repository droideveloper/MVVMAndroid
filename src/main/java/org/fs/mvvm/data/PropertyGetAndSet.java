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
package org.fs.mvvm.data;

import android.util.Log;
import android.view.View;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java8.util.stream.StreamSupport;
import org.fs.mvvm.utils.Preconditions;

public final class PropertyGetAndSet<T, A> {

  private final String setMethodName;
  private final String getMethodName;

  private T target;
  private final Class<A> ancestorType;

  private IFindAncestor helper;

  public PropertyGetAndSet(Class<A> ancestorType, String setMethodName, String getMethodName) {
    Preconditions.checkNotNull(ancestorType, "ancestorType is null");
    Preconditions.checkNotNull(setMethodName, "setter is null");
    Preconditions.checkNotNull(getMethodName, "getter is null");

    this.ancestorType = ancestorType;
    this.setMethodName = setMethodName;
    this.getMethodName = getMethodName;
  }

  public void setTarget(T target) {
    Preconditions.checkNotNull(target, "target is null");
    this.target = target;
  }

  public void setHelper(IFindAncestor helper) {
    Preconditions.checkNotNull(helper, "helper is null");
    this.helper = helper;
  }

  public void execute() {
    if (helper == null) {
      throw new IllegalArgumentException("helper is null");
    }
    View rawView = helper.findAncestor();
    if(rawView == null) {
      throw new IllegalArgumentException("can not find ancestor as " + ancestorType.getName());
    }
    A ancestor = ancestorType.cast(rawView);
    if (ancestor == null) {
      throw new IllegalArgumentException("ancestor is not true type, check it please as " + rawView.getClass().getName());
    }
    if (target == null) {
      throw new IllegalArgumentException("target is null");
    }
    Method get = findMethod(ancestor.getClass(), getMethodName);
    Method set = findMethod(target.getClass(), setMethodName);
    try {
      if (get != null) {
        get.setAccessible(true);
      } else {
        log(Log.ERROR,
            String.format(Locale.ENGLISH, "can not find get %s on %s", getMethodName, ancestor.getClass().getName())
        );
      }
      Object value = get != null ? get.invoke(ancestor) : null;
      if (set != null) {
        set.setAccessible(true);
        set.invoke(target, value);
      } else {
        log(Log.ERROR,
            String.format(Locale.ENGLISH, "can not find set %s on %s", setMethodName, target.getClass().getName())
        );
      }
    } catch (Exception error) {
      log(error);
      throw new IllegalArgumentException(error);
    }
  }

  private Method findMethod(Class clazz, String methodName) {
    return StreamSupport.stream(toMethods(clazz))
        .filter(method -> methodName.equalsIgnoreCase(method.getName()))
        .findFirst()
        .orElse(null);
  }

  private List<Method> toMethods(Class clazz) {
    List<Method> methods = new ArrayList<>();
    methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
    methods.addAll(Arrays.asList(clazz.getMethods()));
    return methods;
  }

  protected void log(String msg) {
    log(Log.DEBUG, msg);
  }

  protected void log(Exception exp) {
    StringWriter strWriter = new StringWriter(128);
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    exp.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  protected void log(int lv, String msg) {
    if (isLogEnabled()) {
      Log.println(lv, getClassTag(), msg);
    }
  }

  protected boolean isLogEnabled() {
    return true;
  }

  protected String getClassTag() {
    return PropertyGetAndSet.class.getSimpleName();
  }
}
