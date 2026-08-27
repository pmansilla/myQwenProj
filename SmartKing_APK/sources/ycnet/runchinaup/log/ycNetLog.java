package ycnet.runchinaup.log;

import android.util.Log;

/* loaded from: classes.dex */
public class ycNetLog {
    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;
    static String logTag = "ycNet";

    private ycNetLog() {
    }

    public static void d(String str) {
        if (allowD) {
            Log.d(logTag, str);
        }
    }

    public static void e(String str) {
        if (allowE) {
            Log.e(logTag, str);
        }
    }

    public static void setLogTag(String str) {
        logTag = str;
    }

    public static void w(String str) {
        if (allowW) {
            Log.w(logTag, str);
        }
    }
}
