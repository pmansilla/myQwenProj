package com.amap.api.mapcore.util;

import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.maps.model.LatLng;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.AMapNativeRenderer;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IOverlay;

/* compiled from: ArcDelegateImp.java */
/* loaded from: classes.dex */
public class dd implements di {
    float a;
    float b;
    float c;
    float d;
    private LatLng e;
    private LatLng f;
    private LatLng g;
    private String l;
    private ad m;
    private float[] n;
    private float h = 10.0f;
    private int i = -16777216;
    private float j = 0.0f;
    private boolean k = true;
    private int o = 0;
    private boolean p = false;
    private double q = 0.0d;
    private double r = 0.0d;
    private double s = 0.0d;

    public dd(ad adVar) {
        this.m = adVar;
        try {
            this.l = getId();
        } catch (RemoteException e) {
            ic.c(e, "ArcDelegateImp", "create");
            e.printStackTrace();
        }
    }

    private double a(double d, double d2, double d3, double d4) {
        double d5 = (d2 - d4) / this.q;
        if (Math.abs(d5) > 1.0d) {
            d5 = Math.signum(d5);
        }
        double asin = Math.asin(d5);
        return asin >= 0.0d ? d3 < d ? 3.141592653589793d - Math.abs(asin) : asin : d3 < d ? 3.141592653589793d - asin : asin + 6.283185307179586d;
    }

    private FPoint a(GLMapState gLMapState, double d, double d2, double d3) {
        int cos = (int) (d2 + (Math.cos(d) * this.q));
        int i = (int) (d3 + ((-Math.sin(d)) * this.q));
        FPoint obtain = FPoint.obtain();
        if (this.m.getMapConfig() != null) {
            obtain.x = cos - r8.getSX();
            obtain.y = i - r8.getSY();
        }
        return obtain;
    }

    private boolean d() {
        return Math.abs((((this.e.latitude - this.f.latitude) * (this.f.longitude - this.g.longitude)) * Math.pow(10.0d, 6.0d)) - (((this.e.longitude - this.f.longitude) * (this.f.latitude - this.g.latitude)) * Math.pow(10.0d, 6.0d))) >= 1.0E-6d;
    }

    private DPoint e() {
        IPoint obtain = IPoint.obtain();
        this.m.a(this.e.latitude, this.e.longitude, obtain);
        IPoint obtain2 = IPoint.obtain();
        this.m.a(this.f.latitude, this.f.longitude, obtain2);
        IPoint obtain3 = IPoint.obtain();
        this.m.a(this.g.latitude, this.g.longitude, obtain3);
        double d = obtain.x;
        double d2 = obtain.y;
        double d3 = obtain2.x;
        double d4 = obtain2.y;
        double d5 = obtain3.x;
        double d6 = obtain3.y;
        Double.isNaN(d6);
        Double.isNaN(d2);
        double d7 = d6 - d2;
        Double.isNaN(d4);
        Double.isNaN(d4);
        double d8 = d4 * d4;
        Double.isNaN(d2);
        Double.isNaN(d2);
        double d9 = d2 * d2;
        Double.isNaN(d3);
        Double.isNaN(d3);
        double d10 = d3 * d3;
        Double.isNaN(d);
        Double.isNaN(d);
        double d11 = d * d;
        Double.isNaN(d4);
        Double.isNaN(d2);
        double d12 = d4 - d2;
        Double.isNaN(d6);
        Double.isNaN(d6);
        double d13 = d6 * d6;
        Double.isNaN(d5);
        Double.isNaN(d5);
        double d14 = d5 * d5;
        Double.isNaN(d3);
        Double.isNaN(d);
        double d15 = d3 - d;
        Double.isNaN(d5);
        Double.isNaN(d);
        double d16 = d5 - d;
        double d17 = (((((d8 - d9) + d10) - d11) * d7) + ((((d9 - d13) + d11) - d14) * d12)) / (((d15 * 2.0d) * d7) - ((d16 * 2.0d) * d12));
        double d18 = (((((d10 - d11) + d8) - d9) * d16) + ((((d11 - d14) + d9) - d13) * d15)) / (((d12 * 2.0d) * d16) - ((d7 * 2.0d) * d15));
        Double.isNaN(d);
        double d19 = d - d17;
        Double.isNaN(d2);
        double d20 = d2 - d18;
        this.q = Math.sqrt((d19 * d19) + (d20 * d20));
        this.r = a(d17, d18, d, d2);
        double a = a(d17, d18, d3, d4);
        this.s = a(d17, d18, d5, d6);
        if (this.r < this.s) {
            if (a <= this.r || a >= this.s) {
                this.s -= 6.283185307179586d;
            }
        } else if (a <= this.s || a >= this.r) {
            this.s += 6.283185307179586d;
        }
        obtain.recycle();
        obtain2.recycle();
        obtain3.recycle();
        return DPoint.obtain(d17, d18);
    }

    public void a(LatLng latLng) {
        this.e = latLng;
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public void a(MapConfig mapConfig) throws RemoteException {
        if (this.e == null || this.f == null || this.g == null || !this.k) {
            return;
        }
        b();
        if (this.n != null && this.o > 0) {
            float mapLenWithWin = this.m.c().getMapLenWithWin((int) this.h);
            this.m.c().getMapLenWithWin(1);
            AMapNativeRenderer.nativeDrawLineByTextureID(this.n, this.n.length, mapLenWithWin, this.m.d(), this.b, this.c, this.d, this.a, 0.0f, false, true, false, this.m.x(), 3, 0);
        }
        this.p = true;
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean a() {
        return true;
    }

    public void b(LatLng latLng) {
        this.f = latLng;
    }

    public boolean b() throws RemoteException {
        FPoint[] fPointArr;
        int i;
        if (this.e == null || this.f == null || this.g == null || !this.k) {
            return false;
        }
        try {
            this.p = false;
            GLMapState c = this.m.c();
            if (!d()) {
                this.n = new float[r0.length * 3];
                FPoint obtain = FPoint.obtain();
                this.m.a(this.e.latitude, this.e.longitude, obtain);
                FPoint obtain2 = FPoint.obtain();
                this.m.a(this.f.latitude, this.f.longitude, obtain2);
                FPoint obtain3 = FPoint.obtain();
                this.m.a(this.g.latitude, this.g.longitude, obtain3);
                FPoint[] fPointArr2 = {obtain, obtain2, obtain3};
                for (int i2 = 0; i2 < 3; i2++) {
                    int i3 = i2 * 3;
                    this.n[i3] = fPointArr2[i2].x;
                    this.n[i3 + 1] = fPointArr2[i2].y;
                    this.n[i3 + 2] = 0.0f;
                }
                this.o = fPointArr2.length;
                return true;
            }
            DPoint e = e();
            int abs = (int) ((Math.abs(this.s - this.r) * 180.0d) / 3.141592653589793d);
            double d = this.s - this.r;
            double d2 = abs;
            Double.isNaN(d2);
            double d3 = d / d2;
            FPoint[] fPointArr3 = new FPoint[abs + 1];
            this.n = new float[fPointArr3.length * 3];
            int i4 = 0;
            while (i4 <= abs) {
                if (i4 == abs) {
                    FPoint obtain4 = FPoint.obtain();
                    this.m.a(this.g.latitude, this.g.longitude, obtain4);
                    fPointArr3[i4] = obtain4;
                    fPointArr = fPointArr3;
                    i = i4;
                } else {
                    double d4 = this.r;
                    double d5 = i4;
                    Double.isNaN(d5);
                    fPointArr = fPointArr3;
                    i = i4;
                    fPointArr[i] = a(c, (d5 * d3) + d4, e.x, e.y);
                }
                double d6 = this.r;
                double d7 = i;
                Double.isNaN(d7);
                fPointArr[i] = a(c, (d7 * d3) + d6, e.x, e.y);
                int i5 = i * 3;
                this.n[i5] = fPointArr[i].x;
                this.n[i5 + 1] = fPointArr[i].y;
                this.n[i5 + 2] = 0.0f;
                i4 = i + 1;
                fPointArr3 = fPointArr;
            }
            e.recycle();
            this.o = fPointArr3.length;
            return true;
        } catch (Throwable th) {
            ic.c(th, "ArcDelegateImp", "calMapFPoint");
            th.printStackTrace();
            return false;
        }
    }

    public void c(LatLng latLng) {
        this.g = latLng;
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean c() {
        return this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        try {
            this.e = null;
            this.f = null;
            this.g = null;
        } catch (Throwable th) {
            ic.c(th, "ArcDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "ArcDelegateImp destroy");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.l == null) {
            this.l = this.m.d("Arc");
        }
        return this.l;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IArc
    public int getStrokeColor() throws RemoteException {
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IArc
    public float getStrokeWidth() throws RemoteException {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.j;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() throws RemoteException {
        return 0;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.k;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.m.a(getId());
        this.m.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IArc
    public void setStrokeColor(int i) throws RemoteException {
        this.i = i;
        this.a = Color.alpha(i) / 255.0f;
        this.b = Color.red(i) / 255.0f;
        this.c = Color.green(i) / 255.0f;
        this.d = Color.blue(i) / 255.0f;
        this.m.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IArc
    public void setStrokeWidth(float f) throws RemoteException {
        this.h = f;
        this.m.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.k = z;
        this.m.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.j = f;
        this.m.f();
        this.m.setRunLowFrame(false);
    }
}
