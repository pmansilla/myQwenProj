package com.loc;

import android.content.Context;
import java.util.zip.Adler32;

/* compiled from: DeviceInfo.java */
/* loaded from: classes.dex */
public final class cy {
    static String a = "d6fc3a4a06adbde89223bvefedc24fecde188aaa9161";
    static final Object b = new Object();
    private static cx c;

    public static synchronized cx a(Context context) {
        synchronized (cy.class) {
            if (c != null) {
                return c;
            }
            if (context == null) {
                return null;
            }
            cx b2 = b(context);
            c = b2;
            return b2;
        }
    }

    private static cx b(Context context) {
        long j;
        if (context == null) {
            return null;
        }
        new cx();
        synchronized (b) {
            String a2 = cz.a(context).a();
            if (dw.a(a2)) {
                return null;
            }
            if (a2.endsWith("\n")) {
                a2 = a2.substring(0, a2.length() - 1);
            }
            cx cxVar = new cx();
            long currentTimeMillis = System.currentTimeMillis();
            String a3 = dv.a(context);
            String b2 = dv.b(context);
            cxVar.c(a3);
            cxVar.a(a3);
            cxVar.b(currentTimeMillis);
            cxVar.b(b2);
            cxVar.d(a2);
            String format = String.format("%s%s%s%s%s", cxVar.e(), cxVar.d(), Long.valueOf(cxVar.a()), cxVar.c(), cxVar.b());
            if (dw.a(format)) {
                j = 0;
            } else {
                Adler32 adler32 = new Adler32();
                adler32.reset();
                adler32.update(format.getBytes());
                j = adler32.getValue();
            }
            cxVar.a(j);
            return cxVar;
        }
    }
}
