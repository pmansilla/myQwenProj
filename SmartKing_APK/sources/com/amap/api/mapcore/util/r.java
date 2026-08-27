package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.amap.api.mapcore.util.s;
import com.autonavi.amap.mapcore.MapConfig;
import java.lang.ref.WeakReference;

/* compiled from: AuthProTask.java */
/* loaded from: classes.dex */
public class r extends Thread {
    private static int c = 0;
    private static int d = 3;
    private static long e = 30000;
    private static boolean g = false;
    private WeakReference<Context> a;
    private ad b;
    private a f = null;
    private Handler h = new Handler(Looper.getMainLooper()) { // from class: com.amap.api.mapcore.util.r.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (r.g) {
                return;
            }
            if (r.this.f == null) {
                r.this.f = new a(r.this.b, r.this.a == null ? null : (Context) r.this.a.get());
            }
            fq.a().a(r.this.f);
        }
    };

    /* compiled from: AuthProTask.java */
    /* loaded from: classes.dex */
    static class a implements Runnable {
        private WeakReference<ad> a;
        private WeakReference<Context> b;
        private s c;

        public a(ad adVar, Context context) {
            this.a = null;
            this.b = null;
            this.a = new WeakReference<>(adVar);
            if (context != null) {
                this.b = new WeakReference<>(context);
            }
        }

        private void a() {
            final ad adVar;
            if (this.a == null || this.a.get() == null || (adVar = this.a.get()) == null || adVar.getMapConfig() == null) {
                return;
            }
            adVar.queueEvent(new Runnable() { // from class: com.amap.api.mapcore.util.r.a.1
                @Override // java.lang.Runnable
                public void run() {
                    if (adVar == null || adVar.getMapConfig() == null) {
                        return;
                    }
                    MapConfig mapConfig = adVar.getMapConfig();
                    mapConfig.setProFunctionAuthEnable(false);
                    if (mapConfig.isUseProFunction()) {
                        adVar.a(mapConfig.isCustomStyleEnable(), true);
                        adVar.w();
                        et.a(a.this.b == null ? null : (Context) a.this.b.get());
                    }
                }
            });
        }

        @Override // java.lang.Runnable
        public void run() {
            s.a a;
            try {
                if (r.g) {
                    return;
                }
                if (this.c == null && this.b != null && this.b.get() != null) {
                    this.c = new s(this.b.get(), "");
                }
                r.c();
                if (r.c > r.d) {
                    boolean unused = r.g = true;
                    a();
                } else {
                    if (this.c == null || (a = this.c.a()) == null) {
                        return;
                    }
                    if (!a.d) {
                        a();
                    }
                    boolean unused2 = r.g = true;
                }
            } catch (Throwable th) {
                ic.c(th, "authForPro", "loadConfigData_uploadException");
            }
        }
    }

    public r(Context context, ad adVar) {
        this.a = null;
        if (context != null) {
            this.a = new WeakReference<>(context);
        }
        this.b = adVar;
        a();
    }

    public static void a() {
        c = 0;
        g = false;
    }

    static /* synthetic */ int c() {
        int i = c;
        c = i + 1;
        return i;
    }

    private void f() {
        if (g) {
            return;
        }
        int i = 0;
        while (i <= d) {
            i++;
            this.h.sendEmptyMessageDelayed(0, i * e);
        }
    }

    @Override // java.lang.Thread
    public void interrupt() {
        this.b = null;
        this.a = null;
        if (this.h != null) {
            this.h.removeCallbacksAndMessages(null);
        }
        this.h = null;
        this.f = null;
        a();
        super.interrupt();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            f();
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImpGLSurfaceView", "mVerfy");
            th.printStackTrace();
        }
    }
}
