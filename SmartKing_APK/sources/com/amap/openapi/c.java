package com.amap.openapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import com.amap.location.common.log.ALLog;
import com.amap.location.security.Core;
import com.loc.fc;
import com.sun.mail.imap.IMAPStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: LocationCloudScheduler.java */
/* loaded from: classes.dex */
public class c {
    private d a;
    private Context b;
    private Handler c;
    private com.amap.openapi.a d;
    private a g;
    private boolean h;
    private ReentrantReadWriteLock e = new ReentrantReadWriteLock();
    private final List<f> f = new ArrayList();
    private Runnable i = new Runnable() { // from class: com.amap.openapi.c.3
        @Override // java.lang.Runnable
        public void run() {
            c.this.e();
        }
    };

    /* compiled from: LocationCloudScheduler.java */
    /* loaded from: classes.dex */
    private final class a extends HandlerThread {
        protected volatile boolean a;

        public a(String str, int i) {
            super(str, i);
        }

        @Override // android.os.HandlerThread
        protected final void onLooperPrepared() {
            c.this.e.writeLock().lock();
            try {
                if (this.a) {
                    Looper looper = getLooper();
                    if (looper != null) {
                        looper.quit();
                    }
                } else {
                    c.this.c = new Handler(Looper.myLooper());
                    try {
                        c.this.b();
                        c.this.c();
                    } catch (Throwable unused) {
                    }
                }
            } finally {
                c.this.e.writeLock().unlock();
            }
        }
    }

    private void a(com.amap.openapi.a aVar) {
        synchronized (this.f) {
            for (int i = 0; i < this.f.size(); i++) {
                this.f.get(i).a(aVar);
            }
        }
    }

    private void a(String str) {
        String str2;
        String str3;
        SharedPreferences sharedPreferences = this.b.getSharedPreferences("LocationCloudConfig", 0);
        com.amap.openapi.a aVar = new com.amap.openapi.a();
        if (aVar.a(str)) {
            long currentTimeMillis = System.currentTimeMillis();
            sharedPreferences.edit().putString(IMAPStore.ID_COMMAND, str).putLong("lasttime", currentTimeMillis).commit();
            aVar.d = currentTimeMillis;
            this.d = aVar;
            a(aVar);
            this.e.readLock().lock();
            if (this.c != null) {
                this.c.postDelayed(this.i, this.d.a);
            }
            this.e.readLock().unlock();
            str2 = "@_2_1_@";
            str3 = "@_2_1_8_@";
        } else {
            h();
            str2 = "@_2_1_@";
            str3 = "@_2_1_9_@";
        }
        ALLog.trace(str2, str3);
    }

    private void a(byte[] bArr) {
        String b = b(bArr);
        if (b != null) {
            a(b);
        } else {
            ALLog.trace("@_2_1_@", "@_2_1_7_@");
            h();
        }
    }

    private String b(byte[] bArr) {
        if (bArr != null) {
            try {
                byte[] xxt = Core.xxt(com.amap.location.common.util.d.b(bArr), -1);
                if (xxt == null) {
                    return null;
                }
                String intern = new String(xxt, "utf-8").intern();
                ALLog.trace("@_2_1_@", "@_2_1_10_@" + intern);
                if (e.a(intern)) {
                    return intern;
                }
            } catch (Exception unused) {
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        SharedPreferences sharedPreferences = this.b.getSharedPreferences("LocationCloudConfig", 0);
        String string = sharedPreferences.getString(IMAPStore.ID_COMMAND, "");
        long j = sharedPreferences.getLong("lasttime", 0L);
        if (!TextUtils.isEmpty(string) && e.a(string)) {
            com.amap.openapi.a aVar = new com.amap.openapi.a();
            if (aVar.a(string)) {
                aVar.d = j;
                this.d = aVar;
                a(aVar);
                ALLog.trace("@_2_1_@", "@_2_1_3_@");
                return;
            }
        }
        ALLog.trace("@_2_1_@", "@_2_1_4_@");
        g();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        this.e.readLock().lock();
        try {
            if (this.c != null) {
                if (d()) {
                    this.c.post(this.i);
                } else {
                    this.c.postDelayed(this.i, this.d.a);
                }
            }
        } finally {
            this.e.readLock().unlock();
        }
    }

    private boolean d() {
        if (this.d == null) {
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis() - this.d.d;
        return currentTimeMillis >= this.d.a || currentTimeMillis < 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        ALLog.trace("@_2_1_@", "@_2_1_5_@");
        byte[] f = f();
        if (f != null) {
            a(e.a(d.a ? "http://aps.testing.amap.com/conf/r?type=3&mid=300&sver=140" : "http://control.aps.amap.com/conf/r?type=3&mid=300&sver=140", f, this.a));
        } else {
            ALLog.trace("@_2_1_@", "@_2_1_6_@");
            h();
        }
    }

    private byte[] f() {
        try {
            fc fcVar = new fc();
            int a2 = fcVar.a(this.b.getPackageName());
            int a3 = fcVar.a(this.a.b());
            int a4 = fcVar.a(com.amap.location.common.a.c(this.b));
            String e = this.a.e();
            if (TextUtils.isEmpty(e)) {
                e = com.amap.location.common.a.b(this.b);
            }
            int a5 = fcVar.a(e);
            int a6 = fcVar.a(com.amap.location.common.a.a(this.b));
            int a7 = fcVar.a(com.amap.location.common.a.d(this.b));
            int a8 = fcVar.a(com.amap.location.common.a.c());
            int a9 = fcVar.a(com.amap.location.common.a.b());
            int a10 = fcVar.a(this.a.d());
            int a11 = fcVar.a(this.a.c());
            bl.a(fcVar);
            bl.a(fcVar, this.a.a());
            bl.a(fcVar, a2);
            bl.b(fcVar, a3);
            bl.b(fcVar, (byte) com.amap.location.common.a.d());
            bl.c(fcVar, a4);
            bl.d(fcVar, a5);
            bl.e(fcVar, a6);
            bl.f(fcVar, a7);
            bl.a(fcVar, com.amap.location.common.a.e(this.b));
            bl.g(fcVar, a8);
            bl.h(fcVar, a9);
            bl.i(fcVar, a10);
            bl.j(fcVar, a11);
            fcVar.h(bl.b(fcVar));
            return Core.xxt(fcVar.f(), 1);
        } catch (Error | Exception unused) {
            return null;
        }
    }

    private void g() {
        this.h = true;
        synchronized (this.f) {
            for (int i = 0; i < this.f.size(); i++) {
                this.f.get(i).a();
            }
        }
    }

    private void h() {
        this.e.readLock().lock();
        try {
            if (this.c != null) {
                this.c.postDelayed(this.i, DateUtils.MILLIS_PER_HOUR);
            }
        } finally {
            this.e.readLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a() {
        ALLog.trace("@_2_1_@", "@_2_1_2_@");
        if (this.g != null) {
            this.g.a = true;
        }
        this.e.writeLock().lock();
        final Handler handler = this.c;
        this.c = null;
        this.e.writeLock().unlock();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler.post(new Runnable() { // from class: com.amap.openapi.c.2
                @Override // java.lang.Runnable
                public void run() {
                    Looper looper = handler.getLooper();
                    if (looper != null) {
                        looper.quit();
                    }
                }
            });
        }
        synchronized (this.f) {
            this.f.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(Context context, d dVar) {
        ALLog.trace("@_2_1_@", "@_2_1_1_@");
        this.b = context;
        this.a = dVar;
        this.g = new a("LocationCloudScheduler", 10);
        this.g.a = false;
        this.g.start();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(final f fVar) {
        if (fVar != null) {
            synchronized (this.f) {
                if (this.f.contains(fVar)) {
                    return;
                }
                this.e.readLock().lock();
                try {
                    Handler handler = this.c;
                    if (handler != null) {
                        handler.post(new Runnable() { // from class: com.amap.openapi.c.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (c.this.d == null) {
                                    if (c.this.h) {
                                        fVar.a();
                                    }
                                } else {
                                    com.amap.openapi.a aVar = new com.amap.openapi.a();
                                    aVar.e = c.this.d.e;
                                    aVar.b = c.this.d.b;
                                    fVar.a(aVar);
                                }
                            }
                        });
                    }
                    this.e.readLock().unlock();
                    this.f.add(fVar);
                } catch (Throwable th) {
                    this.e.readLock().unlock();
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void b(f fVar) {
        if (fVar != null) {
            synchronized (this.f) {
                if (this.f.contains(fVar)) {
                    this.f.remove(fVar);
                }
            }
        }
    }
}
