package com.loc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
final class de {
    private static final TimeUnit a = TimeUnit.SECONDS;
    private static final ThreadFactory b = new df();
    private static final ExecutorService c = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 1, a, new SynchronousQueue(), b);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ExecutorService a() {
        return c;
    }
}
