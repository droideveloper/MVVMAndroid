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
package org.fs.mvvm.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

public abstract class AbstractOrmliteHelper extends OrmLiteSqliteOpenHelper {

  /**
   * Public constructor
   *
   * @param context context
   * @param dbName dbName in final static
   * @param dbVersion dbVersion in final static
   * @param dbConfig R.raw.ormlite_config.txt
   */
  public AbstractOrmliteHelper(Context context, String dbName, int dbVersion, int dbConfig) {
    super(context, dbName, null, dbVersion, dbConfig);
  }

  /**
   * onCreate lifecycle for sql db
   *
   * @param database database
   * @param connectionSource connection
   */
  @Override public final void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    try {
      createTables(connectionSource);
    } catch (SQLException e) {
      log(e);
    }
  }

  /**
   * onUpdate lifecycle for sql db
   *
   * @param database database
   * @param connectionSource connection
   * @param oldVersion prev version
   * @param newVersion new version
   */
  @Override public final void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    try {
      dropTables(connectionSource);
      onCreate(database, connectionSource);
    } catch (SQLException e) {
      log(e);
    }
  }

  /**
   * Log string message with Debug level.
   *
   * @param msg a string for log.
   */
  protected void log(String msg) {
    log(Log.DEBUG, msg);
  }

  /**
   * Log error or exception with Error level using its stackTrace as message.
   *
   * @param error an exception for log.
   */
  protected void log(Throwable error) {
    StringWriter strWriter = new StringWriter();
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    error.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  /**
   * End level of printing logs on android monitor.
   *
   * @param level a level of log.
   * @param msg a string message for log.
   */
  protected void log(int level, String msg) {
    if (isLogEnabled()) {
      Log.println(level, getClassTag(), msg);
    }
  }

  /**
   * Logging enabled for this class or not.
   *
   * @return a boolean.
   */
  protected abstract boolean isLogEnabled();

  /**
   * Tag for this class in order to use it in logging
   *
   * @return a string.
   */
  protected abstract String getClassTag();

  /**
   * createTable lifecycle
   *
   * @param cs connection
   * @throws SQLException error
   */
  protected abstract void createTables(ConnectionSource cs) throws SQLException;

  /**
   * dropTable lifecycle
   *
   * @param cs connection
   * @throws SQLException error
   */
  protected abstract void dropTables(ConnectionSource cs) throws SQLException;
}
