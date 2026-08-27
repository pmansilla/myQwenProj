package com.amap.api.mapcore.util;

/* compiled from: MapLocFilter.java */
/* loaded from: classes.dex */
public final class ke {
    private static ke b;
    private ki c = null;
    private long d = 0;
    private long e = 0;
    long a = 0;

    private ke() {
    }

    public static synchronized ke a() {
        ke keVar;
        synchronized (ke.class) {
            if (b == null) {
                b = new ke();
            }
            keVar = b;
        }
        return keVar;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x00e8, code lost:
    
        if ((r9 - r18.e) > 30000) goto L42;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.amap.api.mapcore.util.ki a(com.amap.api.mapcore.util.ki r19) {
        /*
            Method dump skipped, instructions count: 318
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.ke.a(com.amap.api.mapcore.util.ki):com.amap.api.mapcore.util.ki");
    }
}
