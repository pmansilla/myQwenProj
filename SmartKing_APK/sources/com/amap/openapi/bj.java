package com.amap.openapi;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: UploadController.java */
/* loaded from: classes.dex */
public class bj {
    private Context d;
    private Handler e;
    private a f;
    private long g;
    private b h;
    private int i;
    private Executor j;
    private int a = 0;
    private ReentrantReadWriteLock b = new ReentrantReadWriteLock();
    private int c = 0;
    private Handler.Callback k = new Handler.Callback() { // from class: com.amap.openapi.bj.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            try {
                return bj.this.a(message);
            } catch (Exception unused) {
                bj.this.e();
                return true;
            }
        }
    };

    /* compiled from: UploadController.java */
    /* loaded from: classes.dex */
    public interface a {
        Object a(long j);

        void a();

        void a(int i);

        void a(int i, Object obj);

        boolean a(Object obj);

        void b();

        void b(Object obj);

        boolean b(int i);

        long c();

        long c(int i);

        int d();

        long d(int i);

        long e();

        int f();

        void g();

        Executor h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: UploadController.java */
    /* loaded from: classes.dex */
    public static class b implements Runnable {
        WeakReference<a> a;
        WeakReference<bj> b;
        volatile boolean c;
        Object d;
        final int e;

        b(bj bjVar, a aVar, Object obj, int i) {
            this.b = new WeakReference<>(bjVar);
            this.a = new WeakReference<>(aVar);
            this.d = obj;
            this.e = i;
        }

        public void a() {
            this.c = true;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z;
            if (this.c) {
                return;
            }
            bj bjVar = this.b.get();
            a aVar = this.a.get();
            if (bjVar == null || aVar == null) {
                return;
            }
            if (com.amap.location.common.util.f.a(bjVar.d) < this.e) {
                bjVar.a(this, false);
                return;
            }
            try {
                z = aVar.a(this.d);
            } catch (Throwable unused) {
                z = false;
            }
            if (this.c) {
                return;
            }
            bjVar.a(this, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(b bVar, boolean z) {
        try {
            this.b.readLock().lock();
            if (this.e != null) {
                this.e.obtainMessage(z ? 103 : 104, bVar).sendToTarget();
            }
        } finally {
            this.b.readLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0007. Please report as an issue. */
    public boolean a(Message message) {
        switch (message.what) {
            case 101:
                if (message.arg1 == 1) {
                    this.i = 0;
                }
                c();
                return true;
            case 102:
                this.f.a(this.h.e, this.h.d);
                this.h.a();
                this.h = null;
                this.i++;
                this.f.a(-1);
                e();
                return true;
            case 103:
                b bVar = (b) message.obj;
                if (message.obj == this.h) {
                    this.h = null;
                    try {
                        this.b.readLock().lock();
                        if (this.e != null) {
                            this.e.removeMessages(102);
                        }
                        this.b.readLock().unlock();
                        this.f.a(bVar.e, bVar.d);
                        this.f.b(bVar.d);
                        this.f.a(1);
                        c();
                    } finally {
                    }
                }
                return true;
            case 104:
                if (message.obj == this.h) {
                    this.f.a(this.h.e, this.h.d);
                    this.h = null;
                    try {
                        this.b.readLock().lock();
                        if (this.e != null) {
                            this.e.removeMessages(102);
                        }
                        this.b.readLock().unlock();
                        this.i++;
                        this.f.a(0);
                        e();
                    } finally {
                    }
                }
                return true;
            case 105:
                this.f.a();
                return true;
            case 106:
                b();
                return true;
            default:
                return true;
        }
    }

    private void b() {
        this.f.b();
        if (this.h != null) {
            this.h.a();
        }
        if (this.c == 2) {
            ((ExecutorService) this.j).shutdown();
        }
        this.j = null;
        this.f = null;
        this.h = null;
    }

    private void c() {
        if (this.h != null) {
            return;
        }
        int a2 = com.amap.location.common.util.f.a(this.d);
        if (a2 == -1) {
            e();
            return;
        }
        if (!this.f.b(a2)) {
            e();
            return;
        }
        long c = this.f.c(a2);
        if (c <= 0) {
            e();
            return;
        }
        long c2 = this.f.c();
        if (c2 <= 0) {
            e();
            return;
        }
        long min = Math.min(this.f.d(a2), c);
        if (c2 < min && SystemClock.elapsedRealtime() - this.g < this.f.e()) {
            e();
            return;
        }
        Object a3 = this.f.a(min);
        if (a3 == null) {
            e();
            return;
        }
        this.g = SystemClock.elapsedRealtime();
        if (a2 != com.amap.location.common.util.f.a(this.d)) {
            this.f.g();
            e();
            return;
        }
        try {
            this.b.readLock().lock();
            if (this.e != null) {
                this.h = new b(this, this.f, a3, a2);
                d().execute(this.h);
                this.e.sendEmptyMessageDelayed(102, this.f.f());
            }
        } catch (Throwable unused) {
        } finally {
            this.b.readLock().unlock();
        }
    }

    private Executor d() {
        if (this.j != null) {
            return this.j;
        }
        Executor h = this.f.h();
        if (h != null) {
            this.c = 1;
            this.j = h;
        } else {
            this.j = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(1024), new ThreadFactory() { // from class: com.amap.openapi.bj.2
                @Override // java.util.concurrent.ThreadFactory
                public Thread newThread(Runnable runnable) {
                    return new Thread(runnable, "UploadController");
                }
            });
            this.c = 2;
        }
        return this.j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        try {
            this.b.readLock().lock();
            if (this.e != null && ((this.f.d() <= 0 || this.i < this.f.d()) && !this.e.hasMessages(101))) {
                this.e.sendMessageDelayed(this.e.obtainMessage(101, 0, 0), this.f.e());
            }
        } finally {
            this.b.readLock().unlock();
        }
    }

    public void a() {
        try {
            this.b.writeLock().lock();
            if (this.a == 1) {
                this.a = 2;
                this.e.removeCallbacksAndMessages(null);
                if (this.e.getLooper() == Looper.myLooper()) {
                    b();
                } else {
                    this.e.sendEmptyMessage(106);
                }
                this.e = null;
            }
        } finally {
            this.b.writeLock().unlock();
        }
    }

    public void a(long j) {
        try {
            this.b.readLock().lock();
            if (this.e != null) {
                this.e.removeMessages(101);
                this.e.sendMessageDelayed(this.e.obtainMessage(101, 1, 0), Math.max(0L, j));
            }
        } finally {
            this.b.readLock().unlock();
        }
    }

    public void a(Context context, a aVar, Looper looper) {
        if (context == null || aVar == null || looper == null) {
            throw new RuntimeException("params not be null!");
        }
        try {
            this.b.writeLock().lock();
            if (this.a == 0) {
                this.d = context;
                this.f = aVar;
                this.e = new Handler(looper, this.k);
                if (Looper.myLooper() == looper) {
                    this.f.a();
                } else {
                    this.e.sendEmptyMessage(105);
                }
                this.a = 1;
            }
        } finally {
            this.b.writeLock().unlock();
        }
    }
}
