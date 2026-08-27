package com.amap.openapi;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.amap.openapi.bi.a;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* compiled from: SaveController.java */
/* loaded from: classes.dex */
public class bi<Item extends a> {
    private b<Item> d;
    private Handler e;
    private long g;
    private boolean h;
    private Handler.Callback a = new Handler.Callback() { // from class: com.amap.openapi.bi.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            try {
                return bi.this.a(message);
            } catch (Exception unused) {
                return true;
            }
        }
    };
    private int b = 0;
    private ReentrantReadWriteLock c = new ReentrantReadWriteLock();
    private ArrayList<Item> f = new ArrayList<>();

    /* compiled from: SaveController.java */
    /* loaded from: classes.dex */
    public interface a {
        long a();
    }

    /* compiled from: SaveController.java */
    /* loaded from: classes.dex */
    public interface b<Item extends a> {
        void a();

        void a(ArrayList<Item> arrayList);

        boolean a(long j);

        void b();

        long c();

        long d();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean a(Message message) {
        switch (message.what) {
            case 1:
                b((a) message.obj);
                return true;
            case 2:
                d();
                return true;
            case 3:
                c();
                return true;
            case 4:
                this.d.a();
                return true;
            default:
                return true;
        }
    }

    private void b(Item item) {
        this.f.add(item);
        this.g += item.a();
        if (this.g < this.d.c()) {
            e();
            return;
        }
        try {
            this.c.readLock().lock();
            if (this.e != null) {
                this.e.removeMessages(2);
            }
            this.c.readLock().unlock();
            d();
        } catch (Throwable th) {
            this.c.readLock().unlock();
            throw th;
        }
    }

    private void c() {
        d();
        this.d.b();
        this.d = null;
    }

    private void d() {
        this.h = false;
        if (this.d.a(this.g)) {
            this.d.a(this.f);
        }
        this.f.clear();
        this.g = 0L;
    }

    private void e() {
        if (this.h) {
            return;
        }
        try {
            this.c.readLock().lock();
            if (this.e != null) {
                this.e.sendEmptyMessageDelayed(2, this.d.d());
            }
            this.c.readLock().unlock();
            this.h = true;
        } catch (Throwable th) {
            this.c.readLock().unlock();
            throw th;
        }
    }

    public void a() {
        this.c.writeLock().lock();
        try {
            if (this.b == 1) {
                this.b = 2;
                this.e.removeCallbacksAndMessages(null);
                if (this.e.getLooper() == Looper.myLooper()) {
                    c();
                } else {
                    this.e.sendEmptyMessage(3);
                }
                this.e = null;
            }
        } finally {
            this.c.writeLock().unlock();
        }
    }

    public void a(Item item) {
        try {
            this.c.readLock().lock();
            if (this.e != null) {
                if (this.e.getLooper() == Looper.myLooper()) {
                    b(item);
                } else {
                    this.e.obtainMessage(1, item).sendToTarget();
                }
            }
        } finally {
            this.c.readLock().unlock();
        }
    }

    public void a(b<Item> bVar, Looper looper) {
        if (bVar == null || looper == null) {
            throw new RuntimeException("business 和 looper 都不能为 null");
        }
        try {
            this.c.writeLock().lock();
            if (this.b == 0) {
                this.d = bVar;
                this.e = new Handler(looper, this.a);
                if (Looper.myLooper() == looper) {
                    this.d.a();
                } else {
                    this.e.sendEmptyMessage(4);
                }
                this.b = 1;
            }
        } finally {
            this.c.writeLock().unlock();
        }
    }

    public void b() {
        try {
            this.c.readLock().lock();
            if (this.e != null) {
                if (this.e.getLooper() == Looper.myLooper()) {
                    this.e.removeMessages(2);
                    d();
                } else {
                    this.e.removeMessages(2);
                    this.e.obtainMessage(2).sendToTarget();
                }
            }
        } finally {
            this.c.readLock().unlock();
        }
    }
}
