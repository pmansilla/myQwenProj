package com.mob.mobapm.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class e {
    private static e b;
    private ExecutorService a = Executors.newScheduledThreadPool(5);

    private e() {
    }

    public static e b() {
        if (b == null) {
            synchronized (e.class) {
                if (b == null) {
                    b = new e();
                }
            }
        }
        return b;
    }

    public void a() {
        ExecutorService executorService = this.a;
        if (executorService == null || executorService.isShutdown()) {
            return;
        }
        this.a.shutdown();
    }

    public void a(Runnable runnable) {
        this.a.execute(runnable);
    }
}
