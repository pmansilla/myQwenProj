package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Looper;
import java.lang.Thread;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: SDKLogHandler.java */
/* loaded from: classes.dex */
public class ic extends hz implements Thread.UncaughtExceptionHandler {
    private static ExecutorService e;
    private static WeakReference<Context> g;
    private Context d;
    private List<a> i;
    private static Set<Integer> f = Collections.synchronizedSet(new HashSet());
    private static final ThreadFactory h = new ThreadFactory() { // from class: com.amap.api.mapcore.util.ic.2
        private final AtomicInteger a = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "pama#" + this.a.getAndIncrement());
        }
    };

    /* compiled from: SDKLogHandler.java */
    /* loaded from: classes.dex */
    public interface a {
        void a(Thread thread, Throwable th);
    }

    private ic(Context context, ho hoVar) {
        this.d = context;
        f();
    }

    public static synchronized ic a(Context context, ho hoVar) throws hc {
        synchronized (ic.class) {
            if (hoVar == null) {
                throw new hc("sdk info is null");
            }
            if (hoVar.a() == null || "".equals(hoVar.a())) {
                throw new hc("sdk name is invalid");
            }
            try {
                new ie().a(context);
            } catch (Throwable th) {
                th.printStackTrace();
            }
            if (!f.add(Integer.valueOf(hoVar.hashCode()))) {
                return (ic) hz.a;
            }
            if (hz.a == null) {
                hz.a = new ic(context, hoVar);
            } else {
                hz.a.c = false;
            }
            hz.a.a(context, hoVar, hz.a.c);
            return (ic) hz.a;
        }
    }

    public static void a(Context context) {
        if (context == null) {
            return;
        }
        try {
            g = new WeakReference<>(context.getApplicationContext());
        } catch (Throwable unused) {
        }
    }

    public static void a(ho hoVar, String str, hc hcVar) {
        if (hcVar != null) {
            a(hoVar, str, hcVar.c(), hcVar.d(), hcVar.b());
        }
    }

    public static void a(ho hoVar, String str, String str2, String str3, String str4) {
        try {
            if (hz.a != null) {
                hz.a.a(hoVar, "path:" + str + ",type:" + str2 + ",gsid:" + str3 + ",code:" + str4, "networkError");
            }
        } catch (Throwable unused) {
        }
    }

    private void a(Thread thread, Throwable th) {
        for (int i = 0; i < this.i.size() && i < 10; i++) {
            try {
                a aVar = this.i.get(i);
                if (aVar != null) {
                    aVar.a(thread, th);
                }
            } catch (Throwable unused) {
                return;
            }
        }
    }

    public static synchronized void b() {
        synchronized (ic.class) {
            try {
                if (e != null) {
                    e.shutdown();
                }
                in.a();
            } catch (Throwable th) {
                th.printStackTrace();
            }
            try {
                if (hz.a != null && Thread.getDefaultUncaughtExceptionHandler() == hz.a && hz.a.b != null) {
                    Thread.setDefaultUncaughtExceptionHandler(hz.a.b);
                }
                hz.a = null;
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
    }

    public static void b(ho hoVar, String str, String str2) {
        try {
            if (hz.a != null) {
                hz.a.a(hoVar, str, str2);
            }
        } catch (Throwable unused) {
        }
    }

    public static void c() {
        if (g != null && g.get() != null) {
            ia.b(g.get());
        } else if (hz.a != null) {
            hz.a.a();
        }
    }

    public static void c(Throwable th, String str, String str2) {
        try {
            if (hz.a != null) {
                hz.a.a(th, 1, str, str2);
            }
        } catch (Throwable unused) {
        }
    }

    public static synchronized ExecutorService d() {
        ExecutorService executorService;
        synchronized (ic.class) {
            try {
                if (e == null || e.isShutdown()) {
                    e = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(256), h);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
            executorService = e;
        }
        return executorService;
    }

    public static synchronized ic e() {
        ic icVar;
        synchronized (ic.class) {
            icVar = (ic) hz.a;
        }
        return icVar;
    }

    private void f() {
        try {
            this.b = Thread.getDefaultUncaughtExceptionHandler();
            if (this.b == null) {
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.c = true;
                return;
            }
            String obj = this.b.toString();
            if (!obj.startsWith("com.amap.apis.utils.core.dynamiccore") && (obj.indexOf("com.amap.api") != -1 || obj.indexOf("com.loc") != -1)) {
                this.c = false;
            } else {
                Thread.setDefaultUncaughtExceptionHandler(this);
                this.c = true;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.hz
    protected void a() {
        ia.b(this.d);
    }

    @Override // com.amap.api.mapcore.util.hz
    protected void a(final Context context, final ho hoVar, final boolean z) {
        try {
            ExecutorService d = d();
            if (d != null && !d.isShutdown()) {
                d.submit(new Runnable() { // from class: com.amap.api.mapcore.util.ic.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            synchronized (Looper.getMainLooper()) {
                                new il(context, true).a(hoVar);
                            }
                            if (z) {
                                id.a(ic.this.d);
                            }
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                });
            }
        } catch (RejectedExecutionException unused) {
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.hz
    protected void a(ho hoVar, String str, String str2) {
        id.b(hoVar, this.d, str2, str);
    }

    @Override // com.amap.api.mapcore.util.hz
    protected void a(Throwable th, int i, String str, String str2) {
        id.a(this.d, th, i, str, str2);
    }

    public void b(Throwable th, String str, String str2) {
        if (th == null) {
            return;
        }
        try {
            a(th, 1, str, str2);
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        a(thread, th);
        if (th == null) {
            return;
        }
        a(th, 0, null, null);
        if (this.b != null) {
            try {
                Thread.setDefaultUncaughtExceptionHandler(this.b);
            } catch (Throwable unused) {
            }
            this.b.uncaughtException(thread, th);
        }
    }
}
