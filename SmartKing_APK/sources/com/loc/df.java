package com.loc;

import java.util.concurrent.ThreadFactory;

/* loaded from: classes.dex */
final class df implements ThreadFactory {
    @Override // java.util.concurrent.ThreadFactory
    public final Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("httpdns worker");
        thread.setDaemon(false);
        thread.setUncaughtExceptionHandler(new dm());
        return thread;
    }
}
