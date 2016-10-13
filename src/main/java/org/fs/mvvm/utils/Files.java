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

import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Files {

  private final static String FILE_TEMP_PREFIX    = "TEMP_";
  private final static String FILE_NAME_TIMESTAMP = "yyyyMMdd_HHmmssSSS";
  private final static char   EXTENSION_SEPARATOR = '.';

  private final static SimpleDateFormat FORMATTER = new SimpleDateFormat(FILE_NAME_TIMESTAMP,
      Locale.getDefault());

  private Files() {
    throw new IllegalArgumentException("you can not have instance of this object");
  }

  public static String getFileExtension(File file) {
    if (!Objects.isNullOrEmpty(file)) {
      String path = file.getAbsolutePath();
      int position = path.lastIndexOf(EXTENSION_SEPARATOR);
      return path.substring(position);
    }
    return null;
  }

  public static boolean isDirectory(File file) {
    return file != null && file.isDirectory();
  }

  public static boolean isFile(File file) {
    return file != null && !isDirectory(file);
  }

  public static File createTemporaryFile(String directory, String extension) {
    return createTemporaryFile(new File(directory), extension);
  }

  public static File createTemporaryFile(File directory, String extension) {
    Preconditions.checkNotNull(directory, "directory of file will be created null, please provide one.");
    Preconditions.checkNotNull(extension, "extension is null, please provide one.");
    if (!directory.exists()) {
      boolean created = directory.mkdirs();
      if (created) {
        log(String.format(Locale.ENGLISH,
            "%s is created as directory.", directory.getAbsolutePath()));
      }
    }
    String filename = FILE_TEMP_PREFIX + FORMATTER.format(new Date()) + extension;
    return new File(directory, filename);
  }

  private static void log(String msg) {
    Log.i(Files.class.getSimpleName(), msg);
  }
}
