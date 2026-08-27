package com.amap.api.mapcore.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ServiceInfo;
import com.amap.api.location.AMapLocationClient;
import com.autonavi.amap.mapcore.Inner_3dMap_locationListener;
import com.autonavi.amap.mapcore.Inner_3dMap_locationManagerBase;
import com.autonavi.amap.mapcore.Inner_3dMap_locationOption;

/* compiled from: AMapLocationClient.java */
/* loaded from: classes.dex */
public class gj {
    Context a;
    Inner_3dMap_locationManagerBase b = null;
    Object c = null;
    boolean d = false;
    kd e;
    ho f;

    public gj(Context context) {
        this.e = null;
        this.f = null;
        try {
            this.f = km.a();
        } catch (Throwable unused) {
        }
        this.e = new kd();
        a(context);
    }

    private void a(Context context) {
        ServiceInfo serviceInfo;
        try {
            if (context == null) {
                throw new IllegalArgumentException("Context参数不能为null");
            }
            this.a = context.getApplicationContext();
            try {
                Class<?> cls = Class.forName("com.amap.api.location.AMapLocationClient");
                try {
                    serviceInfo = this.a.getPackageManager().getServiceInfo(new ComponentName(this.a, "com.amap.api.location.APSService"), 128);
                } catch (Throwable unused) {
                    serviceInfo = null;
                }
                if (cls != null && serviceInfo != null) {
                    this.d = true;
                }
            } catch (Throwable unused2) {
                this.d = false;
            }
            if (this.d) {
                this.c = new AMapLocationClient(this.a);
            } else {
                this.b = new kf(context);
            }
        } catch (Throwable th) {
            kw.a(th, "AMapLocationClient", "AMapLocationClient 1");
        }
    }

    public void a() {
        try {
            if (this.d) {
                ((AMapLocationClient) this.c).startLocation();
            } else {
                this.b.startLocation();
            }
        } catch (Throwable th) {
            kw.a(th, "AMapLocationClient", "startLocation");
        }
    }

    public void a(Inner_3dMap_locationListener inner_3dMap_locationListener) {
        try {
            if (inner_3dMap_locationListener == null) {
                throw new IllegalArgumentException("listener参数不能为null");
            }
            if (this.d) {
                this.e.a(this.c, inner_3dMap_locationListener);
            } else {
                this.b.setLocationListener(inner_3dMap_locationListener);
            }
        } catch (Throwable th) {
            kw.a(th, "AMapLocationClient", "setLocationListener");
        }
    }

    public void a(Inner_3dMap_locationOption inner_3dMap_locationOption) {
        try {
            if (inner_3dMap_locationOption == null) {
                throw new IllegalArgumentException("LocationManagerOption参数不能为null");
            }
            if (this.d) {
                kd.a(this.c, inner_3dMap_locationOption);
            } else {
                this.b.setLocationOption(inner_3dMap_locationOption);
            }
        } catch (Throwable th) {
            kw.a(th, "AMapLocationClient", "setLocationOption");
        }
    }

    public void b() {
        try {
            if (this.d) {
                ((AMapLocationClient) this.c).stopLocation();
            } else {
                this.b.stopLocation();
            }
        } catch (Throwable th) {
            kw.a(th, "AMapLocationClient", "stopLocation");
        }
    }

    public void c() {
        try {
            if (this.d) {
                ((AMapLocationClient) this.c).onDestroy();
            } else {
                this.b.destroy();
            }
            if (this.e != null) {
                this.e = null;
            }
        } catch (Throwable th) {
            kw.a(th, "AMapLocationClient", "onDestroy");
        }
    }
}
