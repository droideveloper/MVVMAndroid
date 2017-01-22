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
package org.fs.mvvm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public final class Permissions {

  private Permissions() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  /**
   * if permission granted previously or not checks that.
   * @param context context to check permission on
   * @param permission permission to check if there is any.
   * @return true or false
   */
  public static boolean checkSelfPermission(Context context, String permission) {
    return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_DENIED;
  }

  /**
   * Request Permission from use on given parameters if there is needed uses to show user custom text
   * @param context context
   * @param permission permission to ask
   * @param requestCode request code of permission
   * @param titleRes res id of title string
   * @param messageRes res id of message string
   */
  public static void requestPermission(Context context, String permission, int requestCode, int titleRes, int messageRes) {
    requestPermission(context, permission, requestCode, context.getString(titleRes), context.getString(messageRes));
  }

  /**
   * Request Permission from use on given parameters if there is needed uses to show user custom text
   * @param context Context
   * @param permission permission to ask
   * @param requestCode requestCode of permission
   * @param title title of permission
   * @param message message of permission
   */
  public static void requestPermission(Context context, String permission, int requestCode,  CharSequence title, CharSequence message) {
    if (!checkSelfPermission(context, permission)) {
      if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setCancelable(true)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(android.R.string.cancel, (d, which) ->
              Log.i(Permissions.class.getSimpleName(), "user canceled permission " + permission))
            .setPositiveButton(android.R.string.ok, (d, which) ->
              ActivityCompat.requestPermissions((Activity) context, new String[] { permission }, requestCode))
            .create();
        dialog.show();
      } else {
        ActivityCompat.requestPermissions((Activity) context, new String[] { permission }, requestCode);
      }
    }
  }

  /**
   * Checks the permission result is ok or not
   * @param requestCode requestCode of permission
   * @param result result of the permission of user response
   * @param requestPermissionCode requestPermissionCode
   * @return true or false
   */
  public static boolean permissionResult(int requestCode, int[] result, int requestPermissionCode) {
    if (requestCode == requestPermissionCode) {
      return result != null && result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED;
    }
    return false;
  }
}