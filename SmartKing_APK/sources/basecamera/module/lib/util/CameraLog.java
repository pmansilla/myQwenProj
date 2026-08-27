package basecamera.module.lib.util;

import android.util.Log;

/* loaded from: classes.dex */
public class CameraLog {
    private static final String DEFAULT_TAG = "CJT";

    public static void d(String str) {
        d(DEFAULT_TAG, str);
    }

    public static void d(String str, String str2) {
    }

    public static void e(String str) {
        e(DEFAULT_TAG, str);
    }

    public static void e(String str, String str2) {
    }

    public static void i(String str) {
        i(DEFAULT_TAG, str);
    }

    public static void i(String str, String str2) {
        Log.i(str, str2);
    }

    public static void v(String str) {
        v(DEFAULT_TAG, str);
    }

    public static void v(String str, String str2) {
    }
}
