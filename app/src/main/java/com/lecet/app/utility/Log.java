package com.lecet.app.utility;

import com.lecet.app.BuildConfig;

/**
 * Internal Log Utility
 * Funnels log statements destined for android.util.log.
 * Can be used to universally turn off all logging statements for release builds.
 */

public class Log {
    static final boolean LOG = BuildConfig.DEBUG;

    public static void i(String tag, String string) {
        if (LOG) android.util.Log.i(tag, string);
    }
    public static void e(String tag, String string) {
        if (LOG) android.util.Log.e(tag, string);
    }
    public static void d(String tag, String string) {
        if (LOG) android.util.Log.d(tag, string);
    }
    public static void v(String tag, String string) {
        if (LOG) android.util.Log.v(tag, string);
    }
    public static void w(String tag, String string) {
        if (LOG) android.util.Log.w(tag, string);
    }
}