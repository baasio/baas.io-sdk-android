/*
 * Copyright 2012 Google Inc.
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

package com.kth.common.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.kth.baasio.BuildConfig;
import com.kth.baasio.utils.ObjectUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Helper methods that make logging more consistent throughout the app.
 */
public final class LogUtils {
    private static final String LOG_PREFIX = "baas.io_";

    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();

    private static final int MAX_LOG_TAG_LENGTH = 23;

    private static boolean LOG_TO_FILE = false;

    private static String packageName;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    /**
     * WARNING: Don't use this when obfuscating class names with Proguard!
     */
    public static String makeLogTag(Class<?> cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);

            appendLog(tag, message);
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message, cause);

            appendLog(tag, message, cause);
        }
    }

    public static void LOGV(final String tag, String message) {
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (BuildConfig.SDK_DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message);

            appendLog(tag, message);
        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        // noinspection PointlessBooleanExpression,ConstantConditions
        if (BuildConfig.SDK_DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message, cause);

            appendLog(tag, message, cause);
        }
    }

    public static void LOGI(final String tag, String message) {
        Log.i(tag, message);

        appendLog(tag, message);
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        Log.i(tag, message, cause);

        appendLog(tag, message, cause);
    }

    public static void LOGW(final String tag, String message) {
        Log.w(tag, message);

        appendLog(tag, message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        Log.w(tag, message, cause);

        appendLog(tag, message, cause);
    }

    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);

        appendLog(tag, message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        Log.e(tag, message, cause);

        appendLog(tag, message, cause);
    }

    private LogUtils() {
    }

    public static void setEnableLogToFile(Context context) {
        String packageName = context.getPackageName();

        if (!ObjectUtils.isEmpty(packageName)) {
            LogUtils.packageName = packageName;

            LOG_TO_FILE = true;
        } else {
            Log.e("LogUtils", "Can't write log to file");

            LOG_TO_FILE = false;
        }
    }

    public static void appendLog(String tag, String text) {
        appendLog(tag, text, null);
    }

    public static void appendLog(String tag, String text, Throwable cause) {
        if (!BuildConfig.SDK_DEBUG) {
            return;
        }

        if (!LOG_TO_FILE) {
            return;
        }

        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String current = dateFormat.format(currentDate);

        File myFilesDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Android/data/" + packageName + "/baas.io_log");

        if (!myFilesDir.exists() || !myFilesDir.isDirectory()) {
            myFilesDir.mkdirs();
        }

        File logFile = new File(myFilesDir + "/" + tag + "_" + current + ".txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            // BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));

            String currentDateTimeString = SimpleDateFormat.getDateTimeInstance().format(
                    currentDate);
            buf.append(currentDateTimeString);
            buf.newLine();
            buf.append(text);
            buf.newLine();
            if (cause != null) {
                buf.append(cause.getStackTrace().toString());
            }
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
