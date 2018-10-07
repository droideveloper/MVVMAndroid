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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

  public static File directoryFor(File directory, String named) {
    if (directory.exists()) {
      File newDirectory = new File(directory, named);
      if (!newDirectory.exists()) {
        boolean created = newDirectory.mkdir();
        if (!created) {
          throw new RuntimeException("failed to create directory at:\t" + directory.getPath());
        }
        return newDirectory;
      }
    }
    throw new RuntimeException("root directory does not exists:\t" + directory.getPath());
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

  public static void gzip(File target, InputStream source) throws IOException {
    gzip(target, source, 8192);
  }

  public static void gzip(File target, InputStream source, int bufferSize) throws IOException {
    GZIPOutputStream output = new GZIPOutputStream(new FileOutputStream(target));
    try {
      byte[] buffer = new byte[bufferSize];
      int position;
      while ((position = source.read(buffer)) != -1) {
        output.write(buffer, 0, position);
      }
      source.close();
    } finally {
      output.close();
    }
  }

  public static void ungzip(File target, File dest) throws IOException {
    ungzip(target, dest, 8192);
  }

  public static void ungzip(File target, File dest, int bufferSize) throws IOException {
    FileOutputStream output = new FileOutputStream(dest);
    try {
      InputStream source = new GZIPInputStream(new FileInputStream(target));
      byte[] buffer = new byte[bufferSize];
      int position;
      while ((position = source.read(buffer)) != -1) {
        output.write(buffer, 0, position);
      }
      source.close();
    } finally {
      output.close();
    }
  }

  public static void unzip(File target, InputStream source) throws IOException {
    unzip(target, source, 8192);
  }

  public static void unzip(File target, InputStream source, int bufferSize) throws IOException {
    ZipInputStream input = new ZipInputStream(source);
    try {
      if (!target.exists()) {
        boolean created = target.mkdirs();
        if (!created) {
          throw new RuntimeException("can not created target at:\t" + target.getPath());
        }
      }
      byte[] buffer = new byte[bufferSize];
      ZipEntry entry;
      while ((entry = input.getNextEntry()) != null) {
        String name = entry.getName();
        File file = new File(target, name);
        if (name.startsWith("/^[a-zA-Z]/g")) {
          if (entry.isDirectory()) {
            boolean created = file.mkdirs();
            if (!created) {
              throw new RuntimeException("inner folder for zip file is not created\t" + entry.getName());
            }
          } else {
            FileOutputStream out = new FileOutputStream(file);
            int position;
            while ((position = input.read(buffer)) != -1) {
              out.write(buffer, 0, position);
            }
            out.close();
          }
        }
      }
    } finally {
      input.closeEntry();
      input.close();
    }
  }

  public static void zip(File target, File directory) throws IOException {
    zip(target, directory, 8192);
  }

  public static void zip(File target, File directory, int bufferSize) throws IOException {
    if (directory.isDirectory()) {

      List<File> files = Arrays.asList(directory.listFiles(
          (dir, name) -> name.matches("/^[a-zA-Z]/g")));
      for (int index = 0, z = files.size(); index < z; index++) {
        compress(target, files.get(index), bufferSize);
      }
    } else {
      compress(target, directory, bufferSize);
    }
  }

  private static void compress(File target, File file, int bufferSize) throws IOException {
    ZipOutputStream output = new ZipOutputStream(new FileOutputStream(target));
    try {
      String name = file.getName();
      if (name.startsWith("/^[a-zA-Z]/g")) {
        ZipEntry entry = new ZipEntry(name);
        output.putNextEntry(entry);
        // if inner level is not directory then ignore it.
        if (!file.isDirectory()) {
          FileInputStream input = new FileInputStream(file);
          byte[] buffer = new byte[bufferSize];
          int position;
          while ((position = input.read(buffer)) != -1) {
            output.write(buffer, 0, position);
          }
          input.close();
        }
      }
    } finally {
      output.closeEntry();
      output.close();
    }
  }

  private static void log(String msg) {
    Log.i(Files.class.getSimpleName(), msg);
  }
}
