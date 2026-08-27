package com.amap.api.mapcore.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: AbstractAsyncTask.java */
/* loaded from: classes.dex */
public abstract class er<Params, Progress, Result> {
    public static final Executor b;
    public static final Executor c;
    private static final c f;
    private static volatile Executor g;
    private static final ThreadFactory d = new ThreadFactory() { // from class: com.amap.api.mapcore.util.er.1
        private final AtomicInteger a = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "AbstractAsyncTask #" + this.a.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> e = new LinkedBlockingQueue(10);
    public static final Executor a = new ThreadPoolExecutor(5, 128, 1, TimeUnit.SECONDS, e, d, new ThreadPoolExecutor.DiscardOldestPolicy());
    private volatile e j = e.PENDING;
    private final AtomicBoolean k = new AtomicBoolean();
    private final AtomicBoolean l = new AtomicBoolean();
    private final a<Params, Result> h = new a<Params, Result>() { // from class: com.amap.api.mapcore.util.er.2
        @Override // java.util.concurrent.Callable
        public Result call() throws Exception {
            er.this.l.set(true);
            return (Result) er.this.d(er.this.a((Object[]) this.b));
        }
    };
    private final FutureTask<Result> i = new FutureTask<Result>(this.h) { // from class: com.amap.api.mapcore.util.er.3
        @Override // java.util.concurrent.FutureTask
        protected void done() {
            try {
                er.this.c((er) er.this.i.get());
            } catch (InterruptedException e2) {
                Log.w("AbstractAsyncTask", e2);
            } catch (CancellationException unused) {
                er.this.c((er) null);
            } catch (ExecutionException e3) {
                throw new RuntimeException("An error occured while executing doInBackground()", e3.getCause());
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractAsyncTask.java */
    /* loaded from: classes.dex */
    public static abstract class a<Params, Result> implements Callable<Result> {
        Params[] b;

        private a() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractAsyncTask.java */
    /* loaded from: classes.dex */
    public static class b<Data> {
        final er a;
        final Data[] b;

        b(er erVar, Data... dataArr) {
            this.a = erVar;
            this.b = dataArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: AbstractAsyncTask.java */
    /* loaded from: classes.dex */
    public static class c extends Handler {
        public c(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.obj == null || !(message.obj instanceof b)) {
                return;
            }
            b bVar = (b) message.obj;
            switch (message.what) {
                case 1:
                    bVar.a.e(bVar.b[0]);
                    return;
                case 2:
                    bVar.a.b((Object[]) bVar.b);
                    return;
                default:
                    return;
            }
        }
    }

    /* compiled from: AbstractAsyncTask.java */
    /* loaded from: classes.dex */
    private static class d implements Executor {
        final ArrayDeque<Runnable> a;
        Runnable b;

        private d() {
            this.a = new ArrayDeque<>();
        }

        protected synchronized void a() {
            Runnable poll = this.a.poll();
            this.b = poll;
            if (poll != null) {
                er.a.execute(this.b);
            }
        }

        @Override // java.util.concurrent.Executor
        public synchronized void execute(final Runnable runnable) {
            this.a.offer(new Runnable() { // from class: com.amap.api.mapcore.util.er.d.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        runnable.run();
                    } finally {
                        d.this.a();
                    }
                }
            });
            if (this.b == null) {
                a();
            }
        }
    }

    /* compiled from: AbstractAsyncTask.java */
    /* loaded from: classes.dex */
    public enum e {
        PENDING,
        RUNNING,
        FINISHED
    }

    static {
        b = fr.c() ? new d() : new ThreadPoolExecutor(1, 2, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new fe("AMapSERIAL_EXECUTOR"), new ThreadPoolExecutor.AbortPolicy());
        c = new ThreadPoolExecutor(2, 2, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new fe("AMapDUAL_THREAD_EXECUTOR"), new ThreadPoolExecutor.AbortPolicy());
        f = new c(Looper.getMainLooper());
        g = b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(Result result) {
        if (this.l.get()) {
            return;
        }
        d(result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Result d(Result result) {
        f.obtainMessage(1, new b(this, result)).sendToTarget();
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(Result result) {
        if (d()) {
            b((er<Params, Progress, Result>) result);
        } else {
            a((er<Params, Progress, Result>) result);
        }
        this.j = e.FINISHED;
    }

    public final e a() {
        return this.j;
    }

    public final er<Params, Progress, Result> a(Executor executor, Params... paramsArr) {
        if (this.j != e.PENDING) {
            switch (this.j) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
            }
        }
        this.j = e.RUNNING;
        b();
        this.h.b = paramsArr;
        executor.execute(this.i);
        return this;
    }

    protected abstract Result a(Params... paramsArr);

    protected void a(Result result) {
    }

    public final boolean a(boolean z) {
        this.k.set(true);
        return this.i.cancel(z);
    }

    protected void b() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void b(Result result) {
        c();
    }

    protected void b(Progress... progressArr) {
    }

    public final er<Params, Progress, Result> c(Params... paramsArr) {
        return a(g, paramsArr);
    }

    protected void c() {
    }

    public final boolean d() {
        return this.k.get();
    }
}
