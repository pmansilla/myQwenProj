package com.czw.utils;

import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes.dex */
public class LogUtil {
    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;
    public static CustomLogger customLogger = null;
    public static String customTagPrefix = "";

    /* loaded from: classes.dex */
    public interface CustomLogger {
        void d(String str, String str2);

        void d(String str, String str2, Throwable th);

        void e(String str, String str2);

        void e(String str, String str2, Throwable th);

        void i(String str, String str2);

        void i(String str, String str2, Throwable th);

        void v(String str, String str2);

        void v(String str, String str2, Throwable th);

        void w(String str, String str2);

        void w(String str, String str2, Throwable th);

        void w(String str, Throwable th);

        void wtf(String str, String str2);

        void wtf(String str, String str2, Throwable th);

        void wtf(String str, Throwable th);
    }

    private LogUtil() {
    }

    public static void d(String str) {
        if (allowD) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.d(generateTag, str);
            } else {
                Log.d(generateTag, str);
            }
        }
    }

    public static void d(String str, Throwable th) {
        if (allowD) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.d(generateTag, str, th);
            } else {
                Log.d(generateTag, str, th);
            }
        }
    }

    public static void e(String str) {
        if (allowE) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.e(generateTag, str);
            } else {
                Log.e(generateTag, str);
            }
        }
    }

    public static void e(String str, Throwable th) {
        if (allowE) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.e(generateTag, str, th);
            } else {
                Log.e(generateTag, str, th);
            }
        }
    }

    private static String generateTag(StackTraceElement stackTraceElement) {
        String className = stackTraceElement.getClassName();
        String format = String.format("%s.%s(L:%d)", className.substring(className.lastIndexOf(".") + 1), stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber()));
        if (TextUtils.isEmpty(customTagPrefix)) {
            return format;
        }
        return customTagPrefix + ":" + format;
    }

    public static void i(String str) {
        if (allowI) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.i(generateTag, str);
            } else {
                Log.i(generateTag, str);
            }
        }
    }

    public static void i(String str, Throwable th) {
        if (allowI) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.i(generateTag, str, th);
            } else {
                Log.i(generateTag, str, th);
            }
        }
    }

    public static void v(String str) {
        if (allowV) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.v(generateTag, str);
            } else {
                Log.v(generateTag, str);
            }
        }
    }

    public static void v(String str, Throwable th) {
        if (allowV) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.v(generateTag, str, th);
            } else {
                Log.v(generateTag, str, th);
            }
        }
    }

    public static void w(String str) {
        if (allowW) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.w(generateTag, str);
            } else {
                Log.w(generateTag, str);
            }
        }
    }

    public static void w(String str, Throwable th) {
        if (allowW) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.w(generateTag, str, th);
            } else {
                Log.w(generateTag, str, th);
            }
        }
    }

    public static void w(Throwable th) {
        if (allowW) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.w(generateTag, th);
            } else {
                Log.w(generateTag, th);
            }
        }
    }

    public static void wtf(String str) {
        if (allowWtf) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.wtf(generateTag, str);
            } else {
                Log.wtf(generateTag, str);
            }
        }
    }

    public static void wtf(String str, Throwable th) {
        if (allowWtf) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.wtf(generateTag, str, th);
            } else {
                Log.wtf(generateTag, str, th);
            }
        }
    }

    public static void wtf(Throwable th) {
        if (allowWtf) {
            String generateTag = generateTag(OtherUtils.getCallerStackTraceElement());
            if (customLogger != null) {
                customLogger.wtf(generateTag, th);
            } else {
                Log.wtf(generateTag, th);
            }
        }
    }
}
