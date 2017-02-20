/*
 * MVVM Copyright (C) 2017 Fatih.
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
package org.fs.mvvm.metadata;

import android.view.View;
import android.view.ViewGroup;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fs.mvvm.data.AncestorInfo;
import org.fs.mvvm.utils.Objects;

public class LevelAncestor extends AncestorInfo{

  private final static Pattern PATTERN = Pattern.compile("ancestor=\\{(.+)\\}");
  private final static String  REGEX = ", ";
  private final static String  EQUAL = "=";

  private final static int PAIR_SIZE = 2;
  private final static int INDEX_TYPEOF = 0;
  private final static int INDEX_LEVEL  = 1;

  private final Matcher matcher;
  private final View    view;

  public LevelAncestor(final String str, final View view) {
    super(str);
    this.matcher = PATTERN.matcher(str);
    if (!matcher.find()) {
      throw new RuntimeException("LevelAncestor is not valid checkout documents");
    }
    if(Objects.isNullOrEmpty(view)) {
      throw new RuntimeException("view can not be null.");
    }
    this.view = view;
  }

  @Override public Class<?> typeof() {
    return BindingCompat.forName(property(INDEX_TYPEOF));
  }

  @Override public View view() {
    try {
      int index = Integer.parseInt(property(INDEX_LEVEL));
      Class<?> type = typeof();
      ViewGroup parent = (ViewGroup) view.getParent();
      while(index != 0) {
        parent = (ViewGroup) parent.getParent();
        index--;
      }
      if (parent != null) {
        for (int i = 0, z = parent.getChildCount(); i < z; i++) {
          final View view = parent.getChildAt(i);
          if (type.isInstance(view)) {
            return view;
          }
        }
      }
      return null;
    } catch (NumberFormatException error) {
      throw new RuntimeException("invalid number format for level property");
    }
  }

  private String property(int index) {
    return matcher.group(1)
        .split(REGEX, PAIR_SIZE)[index].trim()
        .split(EQUAL)[1].trim();
  }
}
