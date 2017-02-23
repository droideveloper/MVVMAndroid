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
package org.fs.mvvm.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.fs.mvvm.utils.Preconditions;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class GsonConverterFactory extends Converter.Factory {

  private final Gson gson;

  public static GsonConverterFactory create(Gson gson) {
    return new GsonConverterFactory(gson);
  }

  public static GsonConverterFactory create() {
    return new GsonConverterFactory(new Gson());
  }

  private GsonConverterFactory(final Gson gson) {
    Preconditions.checkNotNull(gson, "gson instance is null");
    this.gson = gson;
  }

  @Override public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
    TypeAdapter<?> typeAdapter = typeAdapterFromType(type);
    return new GsonResponseBodyConverter<>(typeAdapter);
  }

  @Override public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    TypeAdapter<?> typeAdapter = typeAdapterFromType(type);
    return new GsonRequestBodyConverter<>(typeAdapter);
  }

  private TypeAdapter<?> typeAdapterFromType(Type type) {
    return gson.getAdapter(TypeToken.get(type));
  }
}
