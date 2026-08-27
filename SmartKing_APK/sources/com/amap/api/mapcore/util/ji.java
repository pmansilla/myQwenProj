package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: StatisticsManager.java */
/* loaded from: classes.dex */
public class ji {
    private static WeakReference<jc> a;

    public static void a(final Context context) {
        ic.d().submit(new Runnable() { // from class: com.amap.api.mapcore.util.ji.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    jc a2 = jj.a(ji.a);
                    jj.a(context, a2, ia.h, 1000, 307200, AmapLoc.RESULT_TYPE_FUSED);
                    if (a2.g == null) {
                        a2.g = new jk(new jo(context, new jl(new jp(new jr()))));
                    }
                    a2.h = DateUtils.MILLIS_IN_HOUR;
                    if (TextUtils.isEmpty(a2.i)) {
                        a2.i = "cKey";
                    }
                    if (a2.f == null) {
                        a2.f = new jv(context, a2.h, a2.i, new js(30, a2.a, new jx(context, false)));
                    }
                    jd.a(a2);
                } catch (Throwable th) {
                    ic.c(th, "stm", "usd");
                }
            }
        });
    }

    public static synchronized void a(final jh jhVar, final Context context) {
        synchronized (ji.class) {
            ic.d().submit(new Runnable() { // from class: com.amap.api.mapcore.util.ji.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        synchronized (ji.class) {
                            ji.b(context, jhVar.a());
                        }
                    } catch (Throwable th) {
                        ic.c(th, "stm", "as");
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(Context context, byte[] bArr) throws IOException {
        jc a2 = jj.a(a);
        jj.a(context, a2, ia.h, 1000, 307200, AmapLoc.RESULT_TYPE_FUSED);
        if (a2.e == null) {
            a2.e = new hv();
        }
        try {
            jd.a(Integer.toString(new Random().nextInt(100)) + Long.toString(System.nanoTime()), bArr, a2);
        } catch (Throwable th) {
            ic.c(th, "stm", "wts");
        }
    }
}
