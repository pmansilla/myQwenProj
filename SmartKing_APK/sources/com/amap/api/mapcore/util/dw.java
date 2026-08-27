package com.amap.api.mapcore.util;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.location.Location;
import android.os.RemoteException;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.IPoint;

/* compiled from: MyLocationOverlay.java */
/* loaded from: classes.dex */
public class dw {
    ValueAnimator b;
    private ad e;
    private Marker f;
    private Circle g;
    private LatLng i;
    private double j;
    private Context k;
    private ap l;
    private MyLocationStyle h = new MyLocationStyle();
    private int m = 4;
    private boolean n = false;
    private boolean o = false;
    private boolean p = false;
    private boolean q = false;
    private boolean r = false;
    private boolean s = false;
    a a = null;
    Animator.AnimatorListener c = new Animator.AnimatorListener() { // from class: com.amap.api.mapcore.util.dw.1
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            dw.this.i();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    };
    ValueAnimator.AnimatorUpdateListener d = new ValueAnimator.AnimatorUpdateListener() { // from class: com.amap.api.mapcore.util.dw.2
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            try {
                if (dw.this.g != null) {
                    LatLng latLng = (LatLng) valueAnimator.getAnimatedValue();
                    dw.this.g.setCenter(latLng);
                    dw.this.f.setPosition(latLng);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    };

    /* compiled from: MyLocationOverlay.java */
    /* loaded from: classes.dex */
    public static class a implements TypeEvaluator {
        @Override // android.animation.TypeEvaluator
        public Object evaluate(float f, Object obj, Object obj2) {
            LatLng latLng = (LatLng) obj;
            LatLng latLng2 = (LatLng) obj2;
            double d = latLng.latitude;
            double d2 = f;
            double d3 = latLng2.latitude - latLng.latitude;
            Double.isNaN(d2);
            double d4 = d + (d3 * d2);
            double d5 = latLng.longitude;
            double d6 = latLng2.longitude - latLng.longitude;
            Double.isNaN(d2);
            return new LatLng(d4, d5 + (d2 * d6));
        }
    }

    public dw(ad adVar, Context context) {
        this.k = context.getApplicationContext();
        this.e = adVar;
        this.l = new ap(this.k, adVar);
        a(4, true);
    }

    private void a(int i, boolean z) {
        this.m = i;
        this.n = false;
        this.p = false;
        this.o = false;
        this.r = false;
        this.s = false;
        switch (this.m) {
            case 1:
                this.o = true;
                this.p = true;
                this.q = true;
                break;
            case 2:
                this.o = true;
                this.q = true;
                break;
            case 3:
                this.o = true;
                this.s = true;
                break;
            case 4:
                this.o = true;
                this.r = true;
                break;
            case 5:
                this.r = true;
                break;
            case 7:
                this.s = true;
                break;
        }
        if (!this.r && !this.s) {
            if (this.f != null) {
                this.f.setFlat(false);
            }
            h();
            g();
            f();
            return;
        }
        if (this.s) {
            this.l.a(true);
            if (!z) {
                try {
                    this.e.a(aw.a(17.0f));
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            b(45.0f);
        } else {
            this.l.a(false);
        }
        this.l.a();
        if (this.f != null) {
            this.f.setFlat(true);
        }
    }

    private void a(LatLng latLng) {
        LatLng position = this.f.getPosition();
        if (position == null) {
            position = new LatLng(0.0d, 0.0d);
        }
        if (this.a == null) {
            this.a = new a();
        }
        if (this.b == null) {
            this.b = ValueAnimator.ofObject(new a(), position, latLng);
            this.b.addListener(this.c);
            this.b.addUpdateListener(this.d);
        } else {
            this.b.setObjectValues(position, latLng);
            this.b.setEvaluator(this.a);
        }
        if (position.latitude == 0.0d && position.longitude == 0.0d) {
            this.b.setDuration(1L);
        } else {
            this.b.setDuration(1000L);
        }
        this.b.start();
    }

    private void b(float f) {
        if (this.e == null) {
            return;
        }
        try {
            this.e.a(aw.c(f));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void c(float f) {
        if (this.e == null) {
            return;
        }
        try {
            this.e.a(aw.d(f));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void d(float f) {
        if (this.q) {
            float f2 = f % 360.0f;
            if (f2 > 180.0f) {
                f2 -= 360.0f;
            } else if (f2 < -180.0f) {
                f2 += 360.0f;
            }
            if (this.f != null) {
                this.f.setRotateAngle(-f2);
            }
        }
    }

    private void f() {
        this.l.b();
    }

    private void g() {
        b(0.0f);
    }

    private void h() {
        c(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        if (this.i != null && this.o) {
            if (this.p && this.n) {
                return;
            }
            this.n = true;
            try {
                IPoint obtain = IPoint.obtain();
                GLMapState.lonlat2Geo(this.i.longitude, this.i.latitude, obtain);
                this.e.b(aw.a(obtain));
            } catch (Throwable th) {
                ic.c(th, "MyLocationOverlay", "moveMapToLocation");
                th.printStackTrace();
            }
        }
    }

    private void j() {
        if (this.h == null) {
            this.h = new MyLocationStyle();
            this.h.myLocationIcon(BitmapDescriptorFactory.fromAsset("location_map_gps_locked.png"));
            l();
        } else {
            if (this.h.getMyLocationIcon() == null || this.h.getMyLocationIcon().getBitmap() == null) {
                this.h.myLocationIcon(BitmapDescriptorFactory.fromAsset("location_map_gps_locked.png"));
            }
            l();
        }
    }

    private void k() {
        if (this.g != null) {
            try {
                this.e.a(this.g.getId());
            } catch (Throwable th) {
                ic.c(th, "MyLocationOverlay", "locationIconRemove");
                th.printStackTrace();
            }
            this.g = null;
        }
        if (this.f != null) {
            this.f.remove();
            this.f = null;
            this.l.a((Marker) null);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x011b A[Catch: Throwable -> 0x0132, TryCatch #0 {Throwable -> 0x0132, blocks: (B:2:0x0000, B:4:0x0004, B:5:0x0017, B:7:0x001c, B:9:0x002c, B:10:0x0037, B:12:0x0045, B:13:0x0050, B:15:0x005e, B:16:0x0069, B:18:0x006d, B:19:0x0074, B:20:0x0080, B:22:0x0085, B:23:0x0096, B:25:0x009a, B:27:0x00aa, B:29:0x00cb, B:31:0x00d3, B:34:0x00e0, B:36:0x00e8, B:38:0x0100, B:39:0x0117, B:41:0x011b, B:42:0x010c, B:43:0x00ba, B:44:0x0127), top: B:1:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void l() {
        /*
            Method dump skipped, instructions count: 318
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.dw.l():void");
    }

    public MyLocationStyle a() {
        return this.h;
    }

    public void a(float f) {
        if (this.f != null) {
            this.f.setRotateAngle(f);
        }
    }

    public void a(int i) {
        a(i, false);
    }

    public void a(Location location) {
        if (location == null) {
            return;
        }
        a(this.h.isMyLocationShowing());
        if (this.h.isMyLocationShowing()) {
            this.i = new LatLng(location.getLatitude(), location.getLongitude());
            this.j = location.getAccuracy();
            if (this.f == null && this.g == null) {
                j();
            }
            if (this.g != null) {
                try {
                    if (this.j != -1.0d) {
                        this.g.setRadius(this.j);
                    }
                } catch (Throwable th) {
                    ic.c(th, "MyLocationOverlay", "setCentAndRadius");
                    th.printStackTrace();
                }
            }
            d(location.getBearing());
            if (this.i.equals(this.f.getPosition())) {
                i();
            } else {
                a(this.i);
            }
        }
    }

    public void a(MyLocationStyle myLocationStyle) {
        try {
            this.h = myLocationStyle;
            a(this.h.isMyLocationShowing());
            if (!this.h.isMyLocationShowing()) {
                this.l.a(false);
                this.m = this.h.getMyLocationType();
                return;
            }
            j();
            if (this.f == null && this.g == null) {
                return;
            }
            this.l.a(this.f);
            a(this.h.getMyLocationType());
        } catch (Throwable th) {
            ic.c(th, "MyLocationOverlay", "setMyLocationStyle");
            th.printStackTrace();
        }
    }

    public void a(boolean z) {
        if (this.g != null && this.g.isVisible() != z) {
            this.g.setVisible(z);
        }
        if (this.f == null || this.f.isVisible() == z) {
            return;
        }
        this.f.setVisible(z);
    }

    public void b() throws RemoteException {
        k();
        if (this.l != null) {
            f();
            this.l = null;
        }
    }

    public String c() {
        if (this.f != null) {
            return this.f.getId();
        }
        return null;
    }

    public String d() throws RemoteException {
        if (this.g != null) {
            return this.g.getId();
        }
        return null;
    }

    public void e() {
        this.g = null;
        this.f = null;
    }
}
