package com.amap.api.mapcore.util;

import com.amap.api.mapcore.util.jz;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* compiled from: ThreadPool.java */
/* loaded from: classes.dex */
public final class jy {
    private static jy a;
    private ExecutorService b;
    private ConcurrentHashMap<jz, Future<?>> c = new ConcurrentHashMap<>();
    private jz.a d = new jz.a() { // from class: com.amap.api.mapcore.util.jy.1
        @Override // com.amap.api.mapcore.util.jz.a
        public void a(jz jzVar) {
        }

        @Override // com.amap.api.mapcore.util.jz.a
        public void b(jz jzVar) {
            jy.this.a(jzVar, false);
        }

        @Override // com.amap.api.mapcore.util.jz.a
        public void c(jz jzVar) {
            jy.this.a(jzVar, true);
        }
    };

    private jy(int i) {
        try {
            this.b = new ThreadPoolExecutor(i, i, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(256));
        } catch (Throwable th) {
            ic.c(th, "TPool", "ThreadPool");
            th.printStackTrace();
        }
    }

    public static synchronized jy a(int i) {
        jy jyVar;
        synchronized (jy.class) {
            if (a == null) {
                a = new jy(i);
            }
            jyVar = a;
        }
        return jyVar;
    }

    public static synchronized void a() {
        synchronized (jy.class) {
            try {
                if (a != null) {
                    a.b();
                    a = null;
                }
            } catch (Throwable th) {
                ic.c(th, "TPool", "onDestroy");
                th.printStackTrace();
            }
        }
    }

    private synchronized void a(jz jzVar, Future<?> future) {
        try {
            this.c.put(jzVar, future);
        } catch (Throwable th) {
            ic.c(th, "TPool", "addQueue");
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(jz jzVar, boolean z) {
        try {
            Future<?> remove = this.c.remove(jzVar);
            if (z && remove != null) {
                remove.cancel(true);
            }
        } catch (Throwable th) {
            ic.c(th, "TPool", "removeQueue");
            th.printStackTrace();
        }
    }

    public static jy b(int i) {
        return new jy(i);
    }

    private void b() {
        try {
            Iterator<Map.Entry<jz, Future<?>>> it = this.c.entrySet().iterator();
            while (it.hasNext()) {
                Future<?> future = this.c.get(it.next().getKey());
                if (future != null) {
                    try {
                        future.cancel(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.c.clear();
            this.b.shutdown();
        } catch (Throwable th) {
            ic.c(th, "TPool", "destroy");
            th.printStackTrace();
        }
    }

    private synchronized boolean b(jz jzVar) {
        boolean z;
        try {
            z = this.c.containsKey(jzVar);
        } catch (Throwable th) {
            ic.c(th, "TPool", "contain");
            th.printStackTrace();
            z = false;
        }
        return z;
    }

    public void a(jz jzVar) throws hc {
        try {
            if (!b(jzVar) && this.b != null && !this.b.isShutdown()) {
                jzVar.d = this.d;
                try {
                    Future<?> submit = this.b.submit(jzVar);
                    if (submit == null) {
                        return;
                    }
                    a(jzVar, submit);
                } catch (RejectedExecutionException unused) {
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
            ic.c(th, "TPool", "addTask");
            throw new hc("thread pool has exception");
        }
    }
}
