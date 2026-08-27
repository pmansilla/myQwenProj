package com.loc;

/* loaded from: classes.dex */
final class dj {
    static String a;
    static final String[] b = new String[0];

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void a(String str) {
        synchronized (dj.class) {
            a = str;
        }
    }
}
