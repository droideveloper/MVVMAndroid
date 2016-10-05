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

  private final static String BIND_IMAGE_URL          = "bindings:imageUrl";
  private final static String BIND_ERROR_IMAGE        = "bindings:errorImage";
  private final static String BIND_PLACEHOLDER_IMAGE  = "bindings:placeHolderImage";

  private ImageViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object.");
  }

  @BindingAdapter(
      value = {
        BIND_IMAGE_URL,
        BIND_PLACEHOLDER_IMAGE,
        BIND_ERROR_IMAGE
      },
      requireAll = false
  )
  public static void viewImageRegisterImageUrl(ImageView viewImage, String imageUrl, Drawable placeholder, Drawable error) {
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
