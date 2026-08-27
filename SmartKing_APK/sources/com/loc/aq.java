package com.loc;

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
public final class aq extends an implements Thread.UncaughtExceptionHandler {
    private static ExecutorService e;
    private static WeakReference<Context> g;
    private Context d;
    private List<Object> i;
    private static Set<Integer> f = Collections.synchronizedSet(new HashSet());
    private static final ThreadFactory h = new ThreadFactory() { // from class: com.loc.aq.2
        private final AtomicInteger a = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, "pama#" + this.a.getAndIncrement());
        }
    };

    private aq(Context context) {
        this.d = context;
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

    public static synchronized aq a(Context context, ac acVar) throws t {
        synchronized (aq.class) {
            if (acVar == null) {
                throw new t("sdk info is null");
            }
            if (acVar.a() == null || "".equals(acVar.a())) {
                throw new t("sdk name is invalid");
            }
            try {
                new as().a(context);
            } catch (Throwable th) {
                th.printStackTrace();
            }
            if (!f.add(Integer.valueOf(acVar.hashCode()))) {
                return (aq) an.a;
            }
            if (an.a == null) {
                an.a = new aq(context);
            } else {
                an.a.c = false;
            }
            an.a.a(context, acVar, an.a.c);
            return (aq) an.a;
        }
    }

    public static void a(ac acVar, String str, t tVar) {
        if (tVar != null) {
            a(acVar, str, tVar.c(), tVar.d(), tVar.b());
        }
    }

    public static void a(ac acVar, String str, String str2, String str3, String str4) {
        try {
            if (an.a != null) {
                an.a.a(acVar, "path:" + str + ",type:" + str2 + ",gsid:" + str3 + ",code:" + str4, "networkError");
            }
        } catch (Throwable unused) {
        }
    }

    public static synchronized void b() {
        synchronized (aq.class) {
            try {
                if (e != null) {
                    e.shutdown();
                }
                bc.a();
            } catch (Throwable th) {
                th.printStackTrace();
            }
            try {
                if (an.a != null && Thread.getDefaultUncaughtExceptionHandler() == an.a && an.a.b != null) {
                    Thread.setDefaultUncaughtExceptionHandler(an.a.b);
                }
                an.a = null;
            } catch (Throwable th2) {
                th2.printStackTrace();
            }
        }
    }

    public static void b(ac acVar, String str, String str2) {
        try {
            if (an.a != null) {
                an.a.a(acVar, str, str2);
            }
        } catch (Throwable unused) {
        }
    }

    public static void b(Throwable th, String str, String str2) {
        try {
            if (an.a != null) {
                an.a.a(th, 1, str, str2);
            }
        } catch (Throwable unused) {
        }
    }

    public static void c() {
        if (g != null && g.get() != null) {
            ao.b(g.get());
        } else if (an.a != null) {
            an.a.a();
        }
    }

    public static synchronized ExecutorService d() {
        ExecutorService executorService;
        synchronized (aq.class) {
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

    @Override // com.loc.an
    protected final void a() {
        ao.b(this.d);
    }

    @Override // com.loc.an
    protected final void a(final Context context, final ac acVar, final boolean z) {
        try {
            ExecutorService d = d();
            if (d != null && !d.isShutdown()) {
                d.submit(new Runnable() { // from class: com.loc.aq.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            synchronized (Looper.getMainLooper()) {
                                new ba(context, true).a(acVar);
                            }
                            if (z) {
                                ar.a(aq.this.d);
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

    @Override // com.loc.an
    protected final void a(ac acVar, String str, String str2) {
        ar.b(acVar, this.d, str2, str);
    }

    @Override // com.loc.an
    protected final void a(Throwable th, int i, String str, String str2) {
        ar.a(this.d, th, i, str, str2);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final void uncaughtException(Thread thread, Throwable th) {
        for (int i = 0; i < this.i.size() && i < 10; i++) {
            try {
                this.i.get(i);
            } catch (Throwable unused) {
            }
        }
        if (th == null) {
            return;
        }
        a(th, 0, null, null);
        if (this.b != null) {
            try {
                Thread.setDefaultUncaughtExceptionHandler(this.b);
            } catch (Throwable unused2) {
            }
            this.b.uncaughtException(thread, th);
        }
    }
}
