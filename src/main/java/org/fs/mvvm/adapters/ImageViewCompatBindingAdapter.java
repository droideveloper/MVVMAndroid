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
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import org.fs.mvvm.utils.Objects;

public final class ImageViewCompatBindingAdapter {

  private final static String ANDROID_IMAGE_URL = "android:imageUrl";
  private final static String ANDROID_ERROR_IMAGE = "android:errorImage";
  private final static String ANDROID_PLACEHOLDER_IMAGE = "android:placeHolderImage";


  private ImageViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  /**
   * Registers url of Image, Remote preferred and with placeholder Drawable and error Drawable
   *
   * @param viewImage imageView to bind remote image if exists.
   * @param imageUrl remote image url as string.
   * @param placeholder placeholder drawable shown pre-loading.
   * @param error error drawable shown if error occured.
   */
  @BindingAdapter(
      value = {
        ANDROID_IMAGE_URL,
        ANDROID_PLACEHOLDER_IMAGE,
        ANDROID_ERROR_IMAGE
      },
      requireAll = false
  )
  public static void registerImageLoad(ImageView viewImage, String imageUrl, Drawable placeholder, Drawable error) {
    //instead of throwing error we just ignore binding if there is no url present
    if (!Objects.isNullOrEmpty(imageUrl)) {
      DrawableRequestBuilder<String> loadRequest = Glide.with(viewImage.getContext()).load(imageUrl);
      if (placeholder != null) {
        loadRequest = loadRequest.placeholder(placeholder);
      }
      if (error != null) {
        loadRequest = loadRequest.error(error);
      }
      loadRequest.crossFade().into(viewImage);
    }
  }
}
