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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fs.mvvm.data.AncestorInfo;
import org.fs.mvvm.data.IConverter;
import org.fs.mvvm.data.MetadataInfo;
import org.fs.mvvm.utils.Objects;

public class RelativeSource {

  private final static Pattern PATTERN = Pattern.compile("relativeSource=\\{(.+)\\}");
  private final static String  REGEX = ", ";
  private final static String  EQUAL = "=";

  private final static int PAIR_SIZE = 2;
  private final static int INDEX_SOURCE   = 0;
  private final static int INDEX_ANCESTOR = 1;

  private final View view;
  private final String source;
  private final Matcher matcher;

  public RelativeSource(final String source, final View view) {
    if (Objects.isNullOrEmpty(view)) {
      throw new RuntimeException("view can not be null");
    }
    this.view = view;
    if (Objects.isNullOrEmpty(source)) {
      throw new RuntimeException("binding can not be null");
    }
    this.source = source;
    this.matcher = PATTERN.matcher(source);
    if (!matcher.find()) {
      throw new RuntimeException("binding is invalid");
    }
  }

  public <T1, V1, T2, V2> void bind(MetadataInfo<T1, V1> setter, IConverter<V2, V1> parser) {
    final String source = property(INDEX_SOURCE);
    final String[] properties = matcher.group(1).split(REGEX, 2);
    final AncestorInfo ancestor = BindingCompat.forPattern(properties[INDEX_ANCESTOR], view);
    if(ancestor != null) {
      final View view = ancestor.view();
      if (view != null) {
        final MetadataInfo<T2, V2> getter = BindingCompat.forProperty(source, Objects.toObject(ancestor.view()));
        if(getter != null) {
          if (parser != null) {
            setter.set(parser.convert(getter.get(), Locale.getDefault()));
          } else {
            setter.set(Objects.toObject(getter.get()));
          }
        }
      }
    }
  }

  private String property(int index) {
    return matcher.group(1)
        .split(REGEX, PAIR_SIZE)[index].trim()
        .split(EQUAL)[1].trim();
  }
}
