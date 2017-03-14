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
package org.fs.mvvm.metadata.viewPager;

import android.support.v4.view.ViewPager;
import org.fs.mvvm.data.MetadataInfoType;
import org.fs.mvvm.utils.Objects;

public final class ViewPagerSelectedPage implements MetadataInfoType<ViewPager, Integer> {

  private final ViewPager viewPager;

  public ViewPagerSelectedPage(final ViewPager viewPager) {
    if (Objects.isNullOrEmpty(viewPager)) {
      throw new RuntimeException("viewPager can not be null");
    }
    this.viewPager = viewPager;
  }

  @Override public String named() {
    return "selectedPage";
  }

  @Override public void set(Integer value) {
    viewPager.setCurrentItem(value, true);
  }

  @Override public Integer get() {
    return viewPager.getCurrentItem();
  }
}
