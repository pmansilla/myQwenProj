package com.amap.api.mapcore.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* compiled from: ThreadUtil.java */
/* loaded from: classes.dex */
public class fq {
    private static volatile fq c;
    private BlockingQueue<Runnable> a = new LinkedBlockingQueue();
    private ExecutorService b;

    private fq() {
        this.b = null;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.b = new ThreadPoolExecutor(availableProcessors, availableProcessors * 2, 1, TimeUnit.SECONDS, this.a, new fe("AMapThreadUtil"), new ThreadPoolExecutor.AbortPolicy());
    }

    public static fq a() {
        if (c == null) {
            synchronized (fq.class) {
                if (c == null) {
                    c = new fq();
                }
            }
        }
        return c;
    }

    public static void b() {
        if (c != null) {
            try {
                c.b.shutdownNow();
            } catch (Throwable th) {
                th.printStackTrace();
            }
            c.b = null;
            c = null;
        }
    }

    public void a(Runnable runnable) {
        if (this.b != null) {
            try {
                this.b.execute(runnable);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
