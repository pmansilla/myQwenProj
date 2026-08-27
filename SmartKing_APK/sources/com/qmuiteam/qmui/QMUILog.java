package com.qmuiteam.qmui;

/* loaded from: classes2.dex */
public class QMUILog {
    private static QMUILogDelegate sDelegete;

    /* loaded from: classes2.dex */
    public interface QMUILogDelegate {
        void d(String str, String str2, Object... objArr);

        void e(String str, String str2, Object... objArr);

        void i(String str, String str2, Object... objArr);

        void printErrStackTrace(String str, Throwable th, String str2, Object... objArr);

        void w(String str, String str2, Object... objArr);
    }

    public static void d(String str, String str2, Object... objArr) {
        if (sDelegete != null) {
            sDelegete.d(str, str2, objArr);
        }
    }

    public static void e(String str, String str2, Object... objArr) {
        if (sDelegete != null) {
            sDelegete.e(str, str2, objArr);
        }
    }

    public static void i(String str, String str2, Object... objArr) {
        if (sDelegete != null) {
            sDelegete.i(str, str2, objArr);
        }
    }

    public static void printErrStackTrace(String str, Throwable th, String str2, Object... objArr) {
        if (sDelegete != null) {
            sDelegete.printErrStackTrace(str, th, str2, objArr);
        }
    }

    public static void setDelegete(QMUILogDelegate qMUILogDelegate) {
        sDelegete = qMUILogDelegate;
    }

    public static void w(String str, String str2, Object... objArr) {
        if (sDelegete != null) {
            sDelegete.w(str, str2, objArr);
        }
    }
}
