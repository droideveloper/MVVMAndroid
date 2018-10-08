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

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.fs.mvvm.utils.Objects;

public class WebViewCompatBindingAdapter {

  private final static String BIND_BACK = "android:back";
  private final static String BIND_BACK_ATTR_CHANGED = "android:backAttrChanged";

  private final static String BIND_NEXT = "android:next";
  private final static String BIND_NEXT_ATTR_CHANGED = "android:nextAttrChanged";

  private final static String BIND_LOAD_URL = "android:loadUrl";

  private final static String BIND_CHROME_CLIENT = "android:chromeClient";
  private final static String BIND_WEB_CLIENT = "android:webClient";
  private final static String BIND_DOWNLOAD_CALLBACK = "android:downloadCallback";

  private final static String BIND_JS_BRIDGE = "android:jsBridge";
  private final static String BIND_BRIDGE_NAME = "android:bridgeName";

  private WebViewCompatBindingAdapter() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  @InverseBindingAdapter(attribute = BIND_BACK,
      event = BIND_BACK_ATTR_CHANGED)
  public static boolean viewWebRetrieveBack(WebView viewWeb) {
    return viewWeb.canGoBack();
  }

  @InverseBindingAdapter(attribute = BIND_NEXT,
      event = BIND_NEXT_ATTR_CHANGED)
  public static boolean viewWebRetrieveNext(WebView viewWeb) {
    return viewWeb.canGoForward();
  }

  @BindingAdapter(
      value = {
          BIND_NEXT,
          BIND_NEXT_ATTR_CHANGED,
          BIND_BACK_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewWebGoNext(WebView viewWeb, boolean next, InverseBindingListener nextAttrChanged, InverseBindingListener backAttrChanged) {
    if (next) {
      if (viewWeb.canGoForward()) {
        viewWeb.goForward();
        if (nextAttrChanged != null) {
          nextAttrChanged.onChange();
        }
        if (backAttrChanged != null) {
          backAttrChanged.onChange();
        }
      }
    }
  }

  @BindingAdapter(
      value = {
          BIND_BACK,
          BIND_NEXT_ATTR_CHANGED,
          BIND_BACK_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewWebGoBack(WebView viewWeb, boolean back, InverseBindingListener nextAttrChanged, InverseBindingListener backAttrChanged) {
    if (back) {
      if (viewWeb.canGoBack()) {
        viewWeb.goBack();
        if (nextAttrChanged != null) {
          nextAttrChanged.onChange();
        }
        if (backAttrChanged != null) {
          backAttrChanged.onChange();
        }
      }
    }
  }

  @BindingAdapter(
      value = {
          BIND_LOAD_URL,
          BIND_BACK_ATTR_CHANGED,
          BIND_NEXT_ATTR_CHANGED
      },
      requireAll = false
  )
  public static void viewWebRegisterUrl(WebView viewWeb, String url, InverseBindingListener backAttrChanged, InverseBindingListener nextAttrChanged) {
    if (!Objects.isNullOrEmpty(url)) {
      viewWeb.loadUrl(url);
      if (backAttrChanged != null) {
        backAttrChanged.onChange();
      }
      if (nextAttrChanged != null) {
        nextAttrChanged.onChange();
      }
    }
  }

  @BindingAdapter({ BIND_CHROME_CLIENT })
  public static void viewWebRegisterWebChromeClient(WebView viewWeb, WebChromeClient chromeClient) {
    if (chromeClient != null) {
      viewWeb.setWebChromeClient(chromeClient);
    } else {
      viewWeb.setWebChromeClient(null);
    }
  }

  @BindingAdapter({ BIND_WEB_CLIENT })
  public static void viewWebRegisterWebViewClient(WebView viewWeb, WebViewClient client) {
    if (client != null) {
      viewWeb.setWebViewClient(client);
    } else {
      viewWeb.setWebViewClient(null);
    }
  }

  @BindingAdapter({ BIND_DOWNLOAD_CALLBACK })
  public static void viewWebRegisterDownloadCallback(WebView viewWeb, DownloadListener downloadListener) {
    if (downloadListener != null) {
      viewWeb.setDownloadListener(downloadListener);
    } else {
      viewWeb.setDownloadListener(null);
    }
  }

  @BindingAdapter(
      value = {
          BIND_JS_BRIDGE,
          BIND_BRIDGE_NAME
      },
      requireAll = false
  )
  @SuppressLint("JavascriptInterface")
  public static void viewWebRegisterJavascriptBridge(WebView viewWeb, Object jsBridge, String bridgeName) {
    if (!Objects.isNullOrEmpty(jsBridge) && !Objects.isNullOrEmpty(bridgeName)) {
      viewWeb.addJavascriptInterface(jsBridge, bridgeName);
    } else if (!Objects.isNullOrEmpty(bridgeName)) {
      viewWeb.removeJavascriptInterface(bridgeName);
    }
  }
}
