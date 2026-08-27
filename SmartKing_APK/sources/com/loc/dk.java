package com.loc;

import android.util.Log;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class dk {
    private static boolean a = false;
    private static int b = -1;

    private static String a() {
        if (b == -1) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            int length = stackTrace.length;
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (stackTrace[i].getMethodName().equals("getTraceInfo")) {
                    b = i2 + 1;
                    break;
                }
                i2++;
                i++;
            }
        }
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[b + 1];
        return stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + " - [" + stackTraceElement.getMethodName() + "]";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(String str) {
        if (!a || str == null) {
            return;
        }
        Log.d("HttpDnsSDK", Thread.currentThread().getId() + " - " + a() + " - " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Throwable th) {
        if (a) {
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(String str) {
        if (!a || str == null) {
            return;
        }
        Log.e("HttpDnsSDK", Thread.currentThread().getId() + " - " + a() + " - " + str);
    }
}
