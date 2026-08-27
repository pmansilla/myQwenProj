package com.mob.mobapm.core;

import android.os.Handler;
import android.os.Message;
import com.mob.tools.MobHandlerThread;
import java.util.HashMap;

/* loaded from: classes.dex */
public class b implements Handler.Callback {
    private static b g;
    private long c;
    private long d;
    private long e;
    private long f;
    private Object b = new Object();
    private Handler a = MobHandlerThread.newHandler(this);

    private b() {
        new HashMap();
    }

    public static synchronized b e() {
        b bVar;
        synchronized (b.class) {
            if (g == null) {
                g = new b();
            }
            bVar = g;
        }
        return bVar;
    }

    public void a() {
        try {
            this.a.removeCallbacksAndMessages(null);
            long currentTimeMillis = System.currentTimeMillis();
            this.d = currentTimeMillis;
            this.f = (currentTimeMillis - this.c) - this.e;
            HashMap hashMap = new HashMap();
            hashMap.put("appLaunchTime", Long.valueOf(this.c));
            hashMap.put("appCloseTime", Long.valueOf(this.d));
            hashMap.put("appDuration", Long.valueOf(this.f));
            hashMap.put("clientTime", Long.valueOf(this.c));
            com.mob.mobapm.b.a.g(hashMap);
        } catch (Throwable unused) {
        }
    }

    public void b() {
        synchronized (this.b) {
            this.d = System.currentTimeMillis();
            this.a.sendEmptyMessageDelayed(0, 50000L);
        }
    }

    public void c() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.d > 0 && this.c > 0 && currentTimeMillis - this.d >= 30000) {
                this.f = (this.d - this.c) - this.e;
                HashMap hashMap = new HashMap();
                hashMap.put("appLaunchTime", Long.valueOf(this.c));
                hashMap.put("appCloseTime", Long.valueOf(this.d));
                hashMap.put("appDuration", Long.valueOf(this.f));
                hashMap.put("clientTime", Long.valueOf(this.c));
                com.mob.mobapm.b.a.f(hashMap);
                this.c = currentTimeMillis;
                this.d = 0L;
                this.e = 0L;
                this.f = 0L;
            } else if (this.d > 0 && this.c > 0 && currentTimeMillis - this.d < 30000) {
                this.e += currentTimeMillis - this.d;
                this.d = currentTimeMillis;
            } else if (this.c <= 0) {
                this.c = currentTimeMillis;
                this.e = 0L;
                this.f = 0L;
                this.d = 0L;
            }
            this.a.sendEmptyMessageDelayed(0, 50000L);
        } catch (Throwable unused) {
        }
    }

    public void d() {
        try {
            if (this.a != null) {
                this.a.removeCallbacksAndMessages(null);
            }
        } catch (Throwable th) {
            com.mob.mobapm.d.a.a().d("APM: stop work error: " + th, new Object[0]);
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what != 0 || !c.e) {
            return false;
        }
        b();
        return false;
    }
}
