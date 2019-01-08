package io.github.mcxinyu.housi.util;

import android.util.Log;

import io.github.mcxinyu.housi.BuildConfig;

/**
 * Created by 跃峰 on 2016/9/7.
 */
public class LogUtils {
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg + "");
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.i(tag, msg + "", tr);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg + "");
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.d(tag, msg + "", tr);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg + "");
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.e(tag, msg + "", tr);
        }
    }
}
