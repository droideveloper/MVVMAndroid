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

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fs.mvvm.data.AncestorInfo;
import org.fs.mvvm.data.MetadataInfo;
import org.fs.mvvm.metadata.textView.TextViewText;
import org.fs.mvvm.metadata.textView.TextViewTextColor;
import org.fs.mvvm.metadata.view.ViewHeight;
import org.fs.mvvm.metadata.view.ViewMargin;
import org.fs.mvvm.metadata.view.ViewPadding;
import org.fs.mvvm.metadata.view.ViewWidth;
import org.fs.mvvm.metadata.viewPager.ViewPagerSelectedPage;
import org.fs.mvvm.utils.Objects;

public final class BindingCompat {

  private final static Map<Pattern, AncestorLoader> sAncestorLoaders;
  private final static Map<String, MetadataLoader<?, ?>> sMetadataLoaders;

  private final static Map<String, Class<?>> sRegistry;

  static {
    sMetadataLoaders  = new HashMap<>();
    sMetadataLoaders.put("width", new MetadataLoader<View, Integer>() {
      @Override public MetadataInfo<View, Integer> bind(View view, String property) {
        return new ViewWidth(view);
      }
    });
    sMetadataLoaders.put("height", new MetadataLoader<View, Integer>() {
      @Override public MetadataInfo<View, Integer> bind(View view, String property) {
        return new ViewHeight(view);
      }
    });
    sMetadataLoaders.put("margin", new MetadataLoader<View, Integer[]>() {
      @Override public MetadataInfo<View, Integer[]> bind(View view, String property) {
        return new ViewMargin(view);
      }
    });
    sMetadataLoaders.put("padding", new MetadataLoader<View, Integer[]>() {
      @Override public MetadataInfo<View, Integer[]> bind(View view, String property) {
        return new ViewPadding(view);
      }
    });
    sMetadataLoaders.put("selectedPage", new MetadataLoader<ViewPager, Integer>() {
      @Override public MetadataInfo<ViewPager, Integer> bind(ViewPager view, String property) {
        return new ViewPagerSelectedPage(view);
      }
    });
    sMetadataLoaders.put("text", new MetadataLoader<TextView, CharSequence>() {
      @Override public MetadataInfo<TextView, CharSequence> bind(TextView view, String property) {
        return new TextViewText(view);
      }
    });
    sMetadataLoaders.put("textColor", new MetadataLoader<TextView, Integer>() {
      @Override public MetadataInfo<TextView, Integer> bind(TextView view, String property) {
        return new TextViewTextColor(view);
      }
    });
  }

  static {
    sAncestorLoaders  = new HashMap<>();
    sAncestorLoaders.put(Pattern.compile("level=(\\d+)"), LevelAncestor::new);
    sAncestorLoaders.put(Pattern.compile("id=(\\w+)"), ResourceAncestor::new);
  }

  static {
    sRegistry = new HashMap<>();
    sRegistry.put(TextView.class.getSimpleName(), TextView.class);
    sRegistry.put(ViewPager.class.getSimpleName(), ViewPager.class);
    sRegistry.put(View.class.getSimpleName(), View.class);
    sRegistry.put(LinearLayout.class.getSimpleName(), LinearLayout.class);
  }

  private BindingCompat() {
    throw new RuntimeException("you can not have instance of this class.");
  }

  public static Class<?> forName(String className) {
    if (sRegistry.containsKey(className)) {
      return sRegistry.get(className);
    }
    return View.class;
  }

  public static <T, V> void bind(String[] source, final View view) {
    String property = source[0];
    property = forValue(property);
    MetadataInfo<T, V> metadata = forProperty(property, Objects.toObject(view));
    RelativeSource relativeSource = new RelativeSource(source[1], view);
    relativeSource.bind(metadata, null);
  }

  public static AncestorInfo forPattern(String binding, View view) {
    for(Map.Entry<Pattern, AncestorLoader> entry: sAncestorLoaders.entrySet()) {
      final Pattern pattern = entry.getKey();
      final Matcher matcher = pattern.matcher(binding);
      if (matcher.find()) {
        final AncestorLoader loader = sAncestorLoaders.get(pattern);
        return loader.bind(binding, view);
      }
    }
    return null;
  }

  public static <T, V> MetadataInfo<T, V> forProperty(String property, T view) {
    for (Map.Entry<String, MetadataLoader<?, ?>> entry: sMetadataLoaders.entrySet()) {
      final String key = entry.getKey();
      if (key.equalsIgnoreCase(property)) {
        final MetadataLoader<T, V> loader = Objects.toObject(sMetadataLoaders.get(key));
        return loader.bind(view, property);
      }
    }
    return null;
  }

  private static String forValue(String pair) {
    return pair.split("=")[1].trim();
  }
}
