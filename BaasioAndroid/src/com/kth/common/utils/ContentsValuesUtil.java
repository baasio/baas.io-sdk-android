
package com.kth.common.utils;

import android.content.ContentValues;

public class ContentsValuesUtil {

    public static void putContentValue(ContentValues values, String key, String value) {
        if (value == null)
            return;

        values.put(key, value);
    }

    public static void putContentValue(ContentValues values, String key, int value) {
        values.put(key, value);
    }

    public static void putContentValue(ContentValues values, String key, long value) {
        values.put(key, value);
    }

    public static void putContentValue(ContentValues values, String key, double value) {
        values.put(key, value);
    }

}
