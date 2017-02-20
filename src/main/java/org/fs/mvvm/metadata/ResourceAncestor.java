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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fs.mvvm.data.AncestorInfo;
import org.fs.mvvm.utils.Objects;

public class ResourceAncestor extends AncestorInfo {

  private final static Pattern PATTERN = Pattern.compile("ancestor=\\{(.+)\\}");
  private final static String  REGEX = ", ";
  private final static String  EQUAL = "=";
  private final static String  TYPE  = "id";

  private final static int PAIR_SIZE = 2;
  private final static int INDEX_TYPEOF = 0;
  private final static int INDEX_ID     = 1;

  private final Matcher matcher;
  private final View    view;

  public ResourceAncestor(final String str, final View view) {
    super(str);
    this.matcher = PATTERN.matcher(str);
    if (!matcher.find()) {
      throw new RuntimeException("ResourceAncestor is not valid checkout documents");
    }
    if (Objects.isNullOrEmpty(view)) {
      throw new RuntimeException("view can not be null");
    }
    this.view = view;
  }

  @Override public Class<?> typeof() {
    return BindingCompat.forName(property(INDEX_TYPEOF));
  }

  @Override public View view() {
    ViewGroup parent = (ViewGroup) view.getParent();
    while(parent.getId() != android.R.id.content) {
      parent = (ViewGroup) parent.getParent();
    }
    final Context context = view.getContext();
    final String str = property(INDEX_ID);
    final int id = context.getResources().getIdentifier(str, TYPE, context.getPackageName());
    if (id != 0) {
      return parent.findViewById(id);
    }
    return null;
  }

  private String property(int index) {
    return matcher.group(1)
        .split(REGEX, PAIR_SIZE)[index].trim()
        .split(EQUAL)[1].trim();
  }
}
