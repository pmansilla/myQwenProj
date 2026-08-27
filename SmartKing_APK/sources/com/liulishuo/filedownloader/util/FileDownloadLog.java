package com.liulishuo.filedownloader.util;

import android.util.Log;

/* loaded from: classes.dex */
public class FileDownloadLog {
    public static boolean NEED_LOG = false;
    private static final String TAG = "FileDownloader.";

    public static void d(Object obj, String str, Object... objArr) {
        log(3, obj, str, objArr);
    }

    public static void e(Object obj, String str, Object... objArr) {
        log(6, obj, str, objArr);
    }

    public static void e(Object obj, Throwable th, String str, Object... objArr) {
        log(6, obj, th, str, objArr);
    }

    private static String getTag(Object obj) {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append((obj instanceof Class ? (Class) obj : obj.getClass()).getSimpleName());
        return sb.toString();
    }

    public static void i(Object obj, String str, Object... objArr) {
        log(4, obj, str, objArr);
    }

    private static void log(int i, Object obj, String str, Object... objArr) {
        log(i, obj, null, str, objArr);
    }

    private static void log(int i, Object obj, Throwable th, String str, Object... objArr) {
        if ((i >= 5) || NEED_LOG) {
            Log.println(i, getTag(obj), FileDownloadUtils.formatString(str, objArr));
            if (th != null) {
                th.printStackTrace();
            }
        }
    }

    public static void v(Object obj, String str, Object... objArr) {
        log(2, obj, str, objArr);
    }

    public static void w(Object obj, String str, Object... objArr) {
        log(5, obj, str, objArr);
    }
}
