package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import java.lang.ref.WeakReference;

/* compiled from: OfflineLocManager.java */
/* loaded from: classes.dex */
public class jg {
    static int a = 1000;
    static boolean b = false;
    static int c = 20;
    private static WeakReference<jc> d = null;
    private static int e = 10;

    @Deprecated
    public static synchronized void a(int i, boolean z) {
        synchronized (jg.class) {
            a = i;
            b = z;
        }
    }

    public static void a(final Context context) {
        ic.d().submit(new Runnable() { // from class: com.amap.api.mapcore.util.jg.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    jc a2 = jj.a(jg.d);
                    jj.a(context, a2, ia.i, jg.a, 2097152, AmapLoc.RESULT_TYPE_NO_LONGER_USED);
                    a2.h = 14400000;
                    if (a2.g == null) {
                        a2.g = new jn(new jm(context, new jr(), new hs(new hu(new hw())), new String(hx.a(10)), hd.f(context), hi.u(context), hi.m(context), hi.h(context), hi.a(), Build.MANUFACTURER, Build.DEVICE, hi.w(context), hd.c(context), Build.MODEL, hd.d(context), hd.b(context)));
                    }
                    if (TextUtils.isEmpty(a2.i)) {
                        a2.i = "fKey";
                    }
                    a2.f = new jv(context, a2.h, a2.i, new jt(context, jg.b, jg.e * 1024, jg.c * 1024));
                    jd.a(a2);
                } catch (Throwable th) {
                    ic.c(th, "ofm", "uold");
                }
            }
        });
    }

    public static synchronized void a(final jf jfVar, final Context context) {
        synchronized (jg.class) {
            ic.d().submit(new Runnable() { // from class: com.amap.api.mapcore.util.jg.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        synchronized (jg.class) {
                            String l = Long.toString(System.currentTimeMillis());
                            jc a2 = jj.a(jg.d);
                            jj.a(context, a2, ia.i, jg.a, 2097152, AmapLoc.RESULT_TYPE_NO_LONGER_USED);
                            if (a2.e == null) {
                                a2.e = new hs(new hu(new hw(new hu())));
                            }
                            jd.a(l, jfVar.a(), a2);
                        }
                    } catch (Throwable th) {
                        ic.c(th, "ofm", "aple");
                    }
                }
            });
        }
    }
}
