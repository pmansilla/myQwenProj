package com.litesuits.orm.log;

import android.util.Log;

/* loaded from: classes.dex */
public final class OrmLog {
    private static String defaultTag = "OrmLog";
    public static boolean isPrint = false;

    private OrmLog() {
    }

    public static int d(Object obj, String str) {
        if (isPrint) {
            return Log.d(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }

    public static int d(String str, String str2) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.d(str, str2);
    }

    public static int d(String str, String str2, Throwable th) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.d(str, str2, th);
    }

    public static int d(String str, Object... objArr) {
        if (isPrint) {
            return Log.d(str, getLogMessage(objArr));
        }
        return -1;
    }

    public static int e(Object obj, String str) {
        if (isPrint) {
            return Log.e(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }

    public static int e(String str, String str2) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.e(str, str2);
    }

    public static int e(String str, String str2, Throwable th) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.e(str, str2, th);
    }

    public static int e(String str, Object... objArr) {
        if (isPrint) {
            return Log.e(str, getLogMessage(objArr));
        }
        return -1;
    }

    private static String getLogMessage(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : objArr) {
            if (obj != null) {
                sb.append(obj.toString());
            }
        }
        return sb.toString();
    }

    public static int i(Object obj) {
        if (!isPrint || obj == null) {
            return -1;
        }
        return Log.i(defaultTag, obj.toString());
    }

    public static int i(Object obj, String str) {
        if (isPrint) {
            return Log.i(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }

    public static int i(String str) {
        if (!isPrint || str == null) {
            return -1;
        }
        return Log.i(defaultTag, str);
    }

    public static int i(String str, String str2) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.i(str, str2);
    }

    public static int i(String str, String str2, Throwable th) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.i(str, str2, th);
    }

    public static int i(String str, Object... objArr) {
        if (isPrint) {
            return Log.i(str, getLogMessage(objArr));
        }
        return -1;
    }

    public static void setTag(String str) {
        defaultTag = str;
    }

    public static int v(Object obj, String str) {
        if (isPrint) {
            return Log.v(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }

    public static int v(String str, String str2) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.v(str, str2);
    }

    public static int v(String str, String str2, Throwable th) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.v(str, str2, th);
    }

    public static int v(String str, Object... objArr) {
        if (isPrint) {
            return Log.v(str, getLogMessage(objArr));
        }
        return -1;
    }

    public static int w(Object obj, String str) {
        if (isPrint) {
            return Log.w(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }

    public static int w(String str, String str2) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.w(str, str2);
    }

    public static int w(String str, String str2, Throwable th) {
        if (!isPrint || str2 == null) {
            return -1;
        }
        return Log.w(str, str2, th);
    }

    public static int w(String str, Object... objArr) {
        if (isPrint) {
            return Log.w(str, getLogMessage(objArr));
        }
        return -1;
    }
}
