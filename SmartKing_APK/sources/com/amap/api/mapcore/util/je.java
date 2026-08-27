package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import java.lang.ref.WeakReference;

/* compiled from: MarkInfoManager.java */
/* loaded from: classes.dex */
public class je {
    static WeakReference<jc> a;

    public static void a(final Context context) {
        ic.d().submit(new Runnable() { // from class: com.amap.api.mapcore.util.je.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (je.class) {
                    jc a2 = jj.a(je.a);
                    jj.a(context, a2, ia.j, 50, 102400, "10");
                    if (a2.g == null) {
                        a2.g = new jn(new jm(context, new jr(), new hs(new hw(new hu())), "WImFwcG5hbWUiOiIlcyIsInBrZyI6IiVzIiwiZGl1IjoiJXMi", hd.b(context), hd.c(context), je.c(context)));
                    }
                    a2.h = 14400000;
                    if (TextUtils.isEmpty(a2.i)) {
                        a2.i = "eKey";
                    }
                    if (a2.f == null) {
                        a2.f = new jv(context, a2.h, a2.i, new js(5, a2.a, new jx(context, false)));
                    }
                    jd.a(a2);
                }
            }
        });
    }

    public static void a(final String str, final Context context) {
        ic.d().submit(new Runnable() { // from class: com.amap.api.mapcore.util.je.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (je.class) {
                    try {
                        String a2 = hl.a(hp.a(str));
                        jc a3 = jj.a(je.a);
                        jj.a(context, a3, ia.j, 50, 102400, "10");
                        if (a3.e == null) {
                            a3.e = new hs(new hw(new hu()));
                        }
                        jd.a(a2, hp.a(" \"timestamp\":\"" + hp.a(System.currentTimeMillis(), "yyyyMMdd HH:mm:ss") + "\",\"details\":" + str), a3);
                    } catch (Throwable th) {
                        ic.c(th, "mam", "ap");
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String c(Context context) {
        String u = hi.u(context);
        if (!TextUtils.isEmpty(u)) {
            return u;
        }
        String h = hi.h(context);
        if (!TextUtils.isEmpty(h)) {
            return h;
        }
        String m = hi.m(context);
        return !TextUtils.isEmpty(m) ? m : hi.b(context);
    }
}
