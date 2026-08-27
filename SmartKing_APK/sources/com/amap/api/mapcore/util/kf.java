package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import com.autonavi.amap.mapcore.Inner_3dMap_locationListener;
import com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase;
import com.autonavi.amap.mapcore.Inner_3dMap_locationOption;
import com.example.otalib.boads.Constant;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: MapLocationManager.java */
/* loaded from: classes.dex */
public final class kf implements Inner_3dMap_locationManagerBase {
    Context a;
    ArrayList<Inner_3dMap_locationListener> b = new ArrayList<>();
    Object c = new Object();
    Handler d = null;
    a e = null;
    Handler f = null;
    Inner_3dMap_locationOption g = new Inner_3dMap_locationOption();
    kj h = null;
    Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode i = Inner_3dMap_locationOption.Inner_3dMap_Enum_LocationMode.Hight_Accuracy;
    boolean j = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MapLocationManager.java */
    /* loaded from: classes.dex */
    public static class a extends HandlerThread {
        kf a;

        public a(String str, kf kfVar) {
            super(str);
            this.a = kfVar;
        }

        @Override // android.os.HandlerThread
        protected final void onLooperPrepared() {
            try {
                this.a.h = new kj(this.a.a, this.a.d);
                super.onLooperPrepared();
            } catch (Throwable unused) {
            }
        }

        @Override // android.os.HandlerThread, java.lang.Thread, java.lang.Runnable
        public final void run() {
            try {
                super.run();
            } catch (Throwable unused) {
            }
        }
    }

    public kf(Context context) {
        this.a = null;
        if (context == null) {
            throw new IllegalArgumentException("Context参数不能为null");
        }
        this.a = context.getApplicationContext();
        e();
    }

    private Handler a(Looper looper) {
        Handler handler;
        synchronized (this.c) {
            this.f = new kg(looper, this);
            handler = this.f;
        }
        return handler;
    }

    private void a(int i) {
        synchronized (this.c) {
            if (this.f != null) {
                this.f.removeMessages(i);
            }
        }
    }

    private void a(int i, Object obj, long j) {
        synchronized (this.c) {
            if (this.f != null) {
                Message obtain = Message.obtain();
                obtain.what = i;
                obtain.obj = obj;
                this.f.sendMessageDelayed(obtain, j);
            }
        }
    }

    private void e() {
        try {
            this.d = Looper.myLooper() == null ? new kh(this.a.getMainLooper(), this) : new kh(this);
        } catch (Throwable th) {
            kw.a(th, "MapLocationManager", "initResultHandler");
        }
        try {
            this.e = new a("locaitonClientActionThread", this);
            this.e.setPriority(5);
            this.e.start();
            this.f = a(this.e.getLooper());
        } catch (Throwable th2) {
            kw.a(th2, "MapLocationManager", "initActionThreadAndActionHandler");
        }
    }

    private void f() {
        synchronized (this.c) {
            if (this.f != null) {
                this.f.removeCallbacksAndMessages(null);
            }
            this.f = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        try {
            if (this.j) {
                return;
            }
            this.j = true;
            a(Constant.MSG_WHAT_READ_PART, null, 0L);
        } catch (Throwable th) {
            kw.a(th, "MapLocationManager", "doStartLocation");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(Inner_3dMap_location inner_3dMap_location) {
        if (inner_3dMap_location != null) {
            try {
                if (km.a(inner_3dMap_location)) {
                    kd.a = inner_3dMap_location;
                }
            } catch (Throwable th) {
                kw.a(th, "MapLocationManager", "callBackLocation");
                return;
            }
        }
        if (this.j) {
            if (!"gps".equalsIgnoreCase(inner_3dMap_location.getProvider())) {
                inner_3dMap_location.setProvider("lbs");
            }
            inner_3dMap_location.setAltitude(la.b(inner_3dMap_location.getAltitude()));
            inner_3dMap_location.setBearing(la.a(inner_3dMap_location.getBearing()));
            inner_3dMap_location.setSpeed(la.a(inner_3dMap_location.getSpeed()));
            Iterator<Inner_3dMap_locationListener> it = this.b.iterator();
            while (it.hasNext()) {
                try {
                    it.next().onLocationChanged(inner_3dMap_location);
                } catch (Throwable unused) {
                }
            }
        }
        if (this.g.isOnceLocation()) {
            c();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        try {
            if (inner_3dMap_locationListener == null) {
                throw new IllegalArgumentException("listener参数不能为null");
            }
            if (this.b == null) {
                this.b = new ArrayList<>();
            }
            if (this.b.contains(inner_3dMap_locationListener)) {
                return;
            }
            this.b.add(inner_3dMap_locationListener);
        } catch (Throwable th) {
            kw.a(th, "MapLocationManager", "doSetLocationListener");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        this.g = inner_3dMap_locationOption;
        if (this.g == null) {
            this.g = new Inner_3dMap_locationOption();
        }
        if (this.h != null) {
            this.h.a(this.g);
        }
        if (this.j && !this.i.equals(inner_3dMap_locationOption.getLocationMode())) {
            c();
            a();
        }
        this.i = this.g.getLocationMode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b() {
        try {
            try {
                if (this.h != null) {
                    this.h.a();
                }
                if (this.g.isOnceLocation()) {
                    return;
                }
                a(Constant.MSG_WHAT_READ_PART, null, this.g.getInterval() >= 1000 ? this.g.getInterval() : 1000L);
            } catch (Throwable th) {
                kw.a(th, "MapLocationManager", "doGetLocation");
                if (this.g.isOnceLocation()) {
                    return;
                }
                a(Constant.MSG_WHAT_READ_PART, null, this.g.getInterval() >= 1000 ? this.g.getInterval() : 1000L);
            }
        } catch (Throwable th2) {
            if (!this.g.isOnceLocation()) {
                a(Constant.MSG_WHAT_READ_PART, null, this.g.getInterval() >= 1000 ? this.g.getInterval() : 1000L);
            }
            throw th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        if (inner_3dMap_locationListener != null) {
            try {
                if (!this.b.isEmpty() && this.b.contains(inner_3dMap_locationListener)) {
                    this.b.remove(inner_3dMap_locationListener);
                }
            } catch (Throwable th) {
                kw.a(th, "MapLocationManager", "doUnregisterListener");
                return;
            }
        }
        if (this.b.isEmpty()) {
            c();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void c() {
        try {
            this.j = false;
            a(1004);
            a(Constant.MSG_WHAT_READ_PART);
            if (this.h != null) {
                this.h.c();
            }
        } catch (Throwable th) {
            kw.a(th, "MapLocationManager", "doStopLocation");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void d() {
        c();
        if (this.h != null) {
            this.h.d();
        }
        if (this.b != null) {
            this.b.clear();
            this.b = null;
        }
        f();
        if (this.e != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                try {
                    ky.a(this.e, (Class<?>) HandlerThread.class, "quitSafely", new Object[0]);
                } catch (Throwable unused) {
                }
            }
            this.e.quit();
        }
        this.e = null;
        if (this.d != null) {
            this.d.removeCallbacksAndMessages(null);
            this.d = null;
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public final void destroy() {
        try {
            a(1007, null, 0L);
        } catch (Throwable th) {
            kw.a(th, "MapLocationManager", "stopLocation");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public final Inner_3dMap_location getLastKnownLocation() {
        return kd.a;
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public final void setLocationListener(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        try {
            a(1002, inner_3dMap_locationListener, 0L);
        } catch (Throwable th) {
            kw.a(th, "MapLocationManager", "setLocationListener");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public final void setLocationOption(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        try {
            a(1001, inner_3dMap_locationOption, 0L);
        } catch (Throwable th) {
            kw.a(th, "LocationClientManager", "setLocationOption");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public final void startLocation() {
        try {
            a(1004, null, 0L);
        } catch (Throwable th) {
            kw.a(th, "MapLocationManager", "startLocation");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public final void stopLocation() {
        try {
            a(1006, null, 0L);
        } catch (Throwable th) {
            kw.a(th, "MapLocationManager", "stopLocation");
        }
    }

    @Override // com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase
    public final void unRegisterLocationListener(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        try {
            a(1003, inner_3dMap_locationListener, 0L);
        } catch (Throwable th) {
            kw.a(th, "MapLocationManager", "stopLocation");
        }
    }
}
