package com.mob.mcl.c;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/* compiled from: IoFuture.java */
/* loaded from: classes.dex */
public class e implements Future<g> {
    final CountDownLatch a = new CountDownLatch(1);
    final AtomicReference<g> b = new AtomicReference<>();

    @Override // java.util.concurrent.Future
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public g get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        if (this.a.await(j, timeUnit)) {
            return this.b.get();
        }
        throw new TimeoutException("tcp get msg timeout");
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z) {
        return false;
    }

    @Override // java.util.concurrent.Future
    public g get() throws ExecutionException, InterruptedException {
        this.a.await();
        return this.b.get();
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.a.getCount() == 0;
    }
}
