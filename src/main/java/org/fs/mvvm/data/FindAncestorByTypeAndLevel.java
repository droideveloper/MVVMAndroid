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

import android.view.View;
import android.view.ViewParent;
import org.fs.mvvm.utils.Preconditions;

public final class FindAncestorByTypeAndLevel<T> implements IFindAncestor {

  private View ancestorOf;
  private final Class<T>  ancestorType;
  private final int       ancestorLevel;

  public FindAncestorByTypeAndLevel(Class<T> ancestorType, int ancestorLevel) {
    Preconditions.checkNotNull(ancestorType, "ancestorType looking for is null");
    Preconditions.checkConditionMeet(ancestorLevel > 0, "ancestorLevel should be positive");
    this.ancestorType = ancestorType;
    this.ancestorLevel = ancestorLevel;
  }

  @Override public void setAncestorOf(View ancestorOf) {
    Preconditions.checkNotNull(ancestorOf, "ancestorOf is null");
    this.ancestorOf = ancestorOf;
  }

  @Override public View findAncestor() {
    ViewParent viewParent = forLevel(ancestorOf, ancestorLevel);
    if (viewParent != null) {
      if (ancestorType.isInstance(viewParent)) {
        return (View) viewParent;
      }
    }
    return null;
  }

  private ViewParent forLevel(View view, int level) {
    if (view == null) {
      return null;
    }
    if (level <= 1) {
      return view.getParent();
    } else {
      level--;
      return forLevel((View) view.getParent(), level);
    }
  }
}
