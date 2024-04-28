package com.pri.recordview.util;

import android.os.Build;
import android.util.Log;

public class LogUtils {

    public static final String TAG = "PriRecordView";
    public static final boolean FORCE_DEBUG = true;
    public static final boolean DEBUG = Build.TYPE.equals("eng") || Build.TYPE.equals("userdebug") || FORCE_DEBUG;

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(TAG, tag + ":" + msg);
        }
    }

    public static void i(String tag, String msg) {
        Log.i(TAG, tag + ":" + msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.i(TAG, tag + ":" + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.i(TAG, tag + ":" + msg);
        }
    }
}
