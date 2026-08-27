package com.amap.api.mapcore.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import com.autonavi.amap.mapcore.Inner_3dMap_location;
import java.lang.reflect.Constructor;

/* compiled from: MapGpsLocation.java */
/* loaded from: classes.dex */
public final class kc {
    Context a;
    LocationManager b;
    Object g;
    boolean i;
    volatile long c = 0;
    volatile boolean d = false;
    boolean e = false;
    volatile Inner_3dMap_location f = null;
    boolean h = false;
    LocationListener j = new LocationListener() { // from class: com.amap.api.mapcore.util.kc.1
        @Override // android.location.LocationListener
        public final void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }
            try {
                Inner_3dMap_location inner_3dMap_location = new Inner_3dMap_location(location);
                inner_3dMap_location.setProvider("gps");
                inner_3dMap_location.setLocationType(1);
                Bundle extras = location.getExtras();
                inner_3dMap_location.setSatellites(extras != null ? extras.getInt("satellites") : 0);
                inner_3dMap_location.setTime(kx.a(inner_3dMap_location.getTime(), System.currentTimeMillis()));
                kc.this.f = inner_3dMap_location;
                kc.this.c = la.b();
                kc.this.d = true;
            } catch (Throwable th) {
                kw.a(th, "MAPGPSLocation", "onLocationChanged");
            }
        }

        @Override // android.location.LocationListener
        public final void onProviderDisabled(String str) {
            try {
                if ("gps".equals(str)) {
                    kc.this.d = false;
                }
            } catch (Throwable th) {
                kw.a(th, "MAPGPSLocation", "onProviderDisabled");
            }
        }

        @Override // android.location.LocationListener
        public final void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public final void onStatusChanged(String str, int i, Bundle bundle) {
        }
    };

    public kc(Context context) {
        this.g = null;
        this.i = false;
        if (context == null) {
            return;
        }
        this.a = context;
        e();
        try {
            if (this.g == null && !this.i) {
                this.g = this.h ? Class.forName("com.amap.api.maps.CoordinateConverter").getConstructor(Context.class).newInstance(context) : Class.forName("com.amap.api.maps2d.CoordinateConverter").getConstructor(new Class[0]).newInstance(new Object[0]);
                if (context == null) {
                    this.i = true;
                }
            }
        } catch (Throwable unused) {
        }
        if (this.b == null) {
            this.b = (LocationManager) this.a.getSystemService("location");
        }
    }

    private void e() {
        try {
            if (Class.forName("com.amap.api.maps.CoordinateConverter") != null) {
                this.h = true;
            }
        } catch (Throwable unused) {
        }
    }

    private void f() {
        try {
            try {
                Looper myLooper = Looper.myLooper();
                if (myLooper == null) {
                    myLooper = this.a.getMainLooper();
                }
                Looper looper = myLooper;
                try {
                    this.b.sendExtraCommand("gps", "force_xtra_injection", new Bundle());
                } catch (Throwable unused) {
                }
                this.b.requestLocationUpdates("gps", 800L, 0.0f, this.j, looper);
            } catch (SecurityException unused2) {
            }
        } catch (Throwable th) {
            kw.a(th, "MAPGPSLocation", "requestLocationUpdates");
        }
    }

    private void g() {
        this.d = false;
        this.c = 0L;
        this.f = null;
    }

    public final void a() {
        if (this.e) {
            return;
        }
        f();
        this.e = true;
    }

    public final void b() {
        this.e = false;
        g();
        if (this.b == null || this.j == null) {
            return;
        }
        this.b.removeUpdates(this.j);
    }

    public final boolean c() {
        if (!this.d) {
            return false;
        }
        if (la.b() - this.c <= 10000) {
            return true;
        }
        this.f = null;
        return false;
    }

    public final Inner_3dMap_location d() {
        double[] a;
        Object a2;
        Constructor<?> constructor;
        Object[] objArr;
        if (this.f == null) {
            return null;
        }
        Inner_3dMap_location m20clone = this.f.m20clone();
        if (m20clone != null && m20clone.getErrorCode() == 0) {
            try {
                if (this.g != null) {
                    if (kw.a(m20clone.getLatitude(), m20clone.getLongitude())) {
                        Object[] objArr2 = {"GPS"};
                        Class[] clsArr = {String.class};
                        if (this.h) {
                            a2 = ky.a("com.amap.api.maps.CoordinateConverter$CoordType", "valueOf", objArr2, (Class<?>[]) clsArr);
                            constructor = Class.forName("com.amap.api.maps.model.LatLng").getConstructor(Double.TYPE, Double.TYPE);
                            objArr = new Object[]{Double.valueOf(m20clone.getLatitude()), Double.valueOf(m20clone.getLongitude())};
                        } else {
                            a2 = ky.a("com.amap.api.maps2d.CoordinateConverter$CoordType", "valueOf", objArr2, (Class<?>[]) clsArr);
                            constructor = Class.forName("com.amap.api.maps2d.model.LatLng").getConstructor(Double.TYPE, Double.TYPE);
                            objArr = new Object[]{Double.valueOf(m20clone.getLatitude()), Double.valueOf(m20clone.getLongitude())};
                        }
                        ky.a(this.g, "coord", constructor.newInstance(objArr));
                        ky.a(this.g, "from", a2);
                        Object a3 = ky.a(this.g, "convert", new Object[0]);
                        double doubleValue = ((Double) a3.getClass().getDeclaredField("latitude").get(a3)).doubleValue();
                        double doubleValue2 = ((Double) a3.getClass().getDeclaredField("longitude").get(a3)).doubleValue();
                        m20clone.setLatitude(doubleValue);
                        m20clone.setLongitude(doubleValue2);
                    }
                } else if (this.i && kw.a(m20clone.getLatitude(), m20clone.getLongitude()) && (a = ka.a(m20clone.getLongitude(), m20clone.getLatitude())) != null) {
                    m20clone.setLatitude(a[1]);
                    m20clone.setLongitude(a[0]);
                }
            } catch (Throwable unused) {
            }
        }
        return m20clone;
    }
}
