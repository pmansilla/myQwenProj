package com.amap.api.mapcore.util;

import android.text.TextUtils;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: NamedThreadFactory.java */
/* loaded from: classes.dex */
public class fe implements ThreadFactory {
    private static final AtomicInteger a = new AtomicInteger(1);
    private final AtomicInteger b;
    private final String c;
    private final boolean d;
    private final ThreadGroup e;

    public fe() {
        this("amap-threadpool-" + a.getAndIncrement(), false);
    }

    public fe(String str) {
        this(str, false);
    }

    public fe(String str, boolean z) {
        String str2;
        this.b = new AtomicInteger(1);
        if (TextUtils.isEmpty(str)) {
            str2 = "";
        } else {
            str2 = str + "-thread-";
        }
        this.c = str2;
        this.d = z;
        SecurityManager securityManager = System.getSecurityManager();
        this.e = securityManager == null ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(this.e, runnable, this.c + this.b.getAndIncrement(), 0L);
        thread.setDaemon(this.d);
        return thread;
    }
}
