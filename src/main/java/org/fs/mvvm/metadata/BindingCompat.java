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

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.fs.mvvm.data.AncestorInfo;
import org.fs.mvvm.data.IConverter;
import org.fs.mvvm.data.MetadataInfo;
import org.fs.mvvm.metadata.absListView.AbsListViewSelectedPosition;
import org.fs.mvvm.metadata.textView.TextViewText;
import org.fs.mvvm.metadata.textView.TextViewTextColor;
import org.fs.mvvm.metadata.textView.TextViewTextSize;
import org.fs.mvvm.metadata.view.ViewDrawable;
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

    sMetadataLoaders.put("width", defaultsViewInteger());
    sMetadataLoaders.put("height", defaultsViewInteger());
    sMetadataLoaders.put("margin", defaultsViewIntegerArray());
    sMetadataLoaders.put("padding", defaultsViewIntegerArray());
    sMetadataLoaders.put("background", new MetadataLoader<View, Drawable>() {
      @Override public MetadataInfo<View, Drawable> bind(View view, String property) {
        return new ViewDrawable(view);
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
    sMetadataLoaders.put("textSize", new MetadataLoader<TextView, Float>() {
      @Override public MetadataInfo<TextView, Float> bind(TextView view, String property) {
        return new TextViewTextSize(view);
      }
    });
    sMetadataLoaders.put("selectedPosition", new MetadataLoader<AbsListView, Integer>() {
      @Override public MetadataInfo<AbsListView, Integer> bind(AbsListView view, String property) {
        return new AbsListViewSelectedPosition(view);
      }
    });
  }

  static {
    sAncestorLoaders  = new HashMap<>();
    sAncestorLoaders.put(Pattern.compile("level=(\\d+)"), LevelAncestor::new);
    sAncestorLoaders.put(Pattern.compile("id=(\\w+)"), ResourceAncestor::new);
  }

  /**
   * At least for now register all the views I support for this kind of binding
   */
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

  public static <T1, V1, V2> void bind(String[] source, final View view, IConverter<V2, V1> parser) {
    String property = source[0];
    property = forValue(property);
    MetadataInfo<T1, V1> metadata = forProperty(property, Objects.toObject(view));
    RelativeSource relativeSource = new RelativeSource(source[1], view);
    relativeSource.bind(metadata, parser);
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

  private static MetadataLoader<View, Integer> defaultsViewInteger() {
    return (view, property) -> {
      if (property.equalsIgnoreCase("width")) {
        return new ViewWidth(view);
      } else if (property.equalsIgnoreCase("height")) {
        return new ViewHeight(view);
      } else {
        throw new RuntimeException("not implemented\t" + property);
      }
    };
  }

  private static MetadataLoader<View, Integer[]> defaultsViewIntegerArray() {
    return (view, property) -> {
      if (property.equalsIgnoreCase("margin")) {
        return new ViewMargin(view);
      } else if(property.equalsIgnoreCase("padding")) {
        return new ViewPadding(view);
      } else {
        throw new RuntimeException("not implemented\t" + property);
      }
    };
  }
}
