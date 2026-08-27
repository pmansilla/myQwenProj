package com.amap.api.mapcore.util;

import android.opengl.GLES20;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.mapcore.util.ef;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BaseHoleOptions;
import com.amap.api.maps.model.CircleHoleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolygonHoleOptions;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* compiled from: CircleDelegateImp.java */
/* loaded from: classes.dex */
public class df implements dk {
    private String i;
    private ad j;
    private FloatBuffer k;
    private List<BaseHoleOptions> p;
    private List<BaseHoleOptions> q;
    private int r;
    private int s;
    private FloatBuffer t;
    private FloatBuffer u;
    private ef.e x;
    private static Object v = new Object();
    private static float y = 4.0075016E7f;
    private static int z = 256;
    private static int A = 20;
    private static double B = 1.0E10d;
    private LatLng b = null;
    private double c = 0.0d;
    private float d = 10.0f;
    private int e = -16777216;
    private int f = 0;
    private float g = 0.0f;
    private boolean h = true;
    private int l = 0;
    private boolean m = false;
    private IPoint n = IPoint.obtain();
    private FPoint o = FPoint.obtain();
    private int w = -1;
    float a = 0.0f;

    public df(ad adVar) {
        this.j = adVar;
        try {
            this.i = getId();
        } catch (RemoteException e) {
            ic.c(e, "CircleDelegateImp", "create");
            e.printStackTrace();
        }
    }

    private float a(double d) {
        double cos = Math.cos((d * 3.141592653589793d) / 180.0d);
        double d2 = y;
        Double.isNaN(d2);
        double d3 = cos * d2;
        double d4 = z << A;
        Double.isNaN(d4);
        return (float) (d3 / d4);
    }

    private List<IPoint> a(List<LatLng> list) throws RemoteException {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            LatLng latLng = null;
            for (LatLng latLng2 : list) {
                if (!latLng2.equals(latLng)) {
                    IPoint obtain = IPoint.obtain();
                    this.j.a(latLng2.latitude, latLng2.longitude, obtain);
                    arrayList.add(obtain);
                    latLng = latLng2;
                }
            }
            int size = arrayList.size();
            if (size > 1) {
                IPoint iPoint = (IPoint) arrayList.get(0);
                int i = size - 1;
                IPoint iPoint2 = (IPoint) arrayList.get(i);
                if (iPoint.x == iPoint2.x && iPoint.y == iPoint2.y) {
                    arrayList.remove(i);
                }
            }
        }
        if (fr.a(arrayList, 0, arrayList.size())) {
            Collections.reverse(arrayList);
        }
        return arrayList;
    }

    private void a(List<IPoint> list, int i, int i2) throws RemoteException {
        if (list.size() < 2) {
            return;
        }
        float[] fArr = new float[list.size() * 3];
        IPoint[] iPointArr = new IPoint[list.size()];
        int i3 = 0;
        for (IPoint iPoint : list) {
            int i4 = i3 * 3;
            fArr[i4] = iPoint.x - i;
            fArr[i4 + 1] = iPoint.y - i2;
            fArr[i4 + 2] = 0.0f;
            iPointArr[i3] = iPoint;
            i3++;
        }
        IPoint[] a = a(iPointArr);
        if (a.length == 0) {
            if (B == 1.0E10d) {
                B = 1.0E8d;
            } else {
                B = 1.0E10d;
            }
            a = a(iPointArr);
        }
        float[] fArr2 = new float[a.length * 3];
        int i5 = 0;
        for (IPoint iPoint2 : a) {
            int i6 = i5 * 3;
            fArr2[i6] = iPoint2.x - i;
            fArr2[i6 + 1] = iPoint2.y - i2;
            fArr2[i6 + 2] = 0.0f;
            i5++;
        }
        this.r = iPointArr.length;
        this.s = a.length;
        this.t = fr.a(fArr);
        this.u = fr.a(fArr2);
    }

    private boolean a(PolygonHoleOptions polygonHoleOptions) {
        boolean z2 = true;
        try {
            List<LatLng> points = polygonHoleOptions.getPoints();
            int i = 0;
            while (i < points.size()) {
                boolean contains = contains(points.get(i));
                if (!contains) {
                    return contains;
                }
                i++;
                z2 = contains;
            }
            return z2;
        } catch (Throwable th) {
            ic.c(th, "CircleDelegateImp", "isPolygonInCircle");
            th.printStackTrace();
            return z2;
        }
    }

    static IPoint[] a(IPoint[] iPointArr) {
        int length = iPointArr.length;
        double[] dArr = new double[length * 2];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            double d = iPointArr[i].x;
            double d2 = B;
            Double.isNaN(d);
            dArr[i2] = d * d2;
            double d3 = iPointArr[i].y;
            double d4 = B;
            Double.isNaN(d3);
            dArr[i2 + 1] = d3 * d4;
        }
        fn a = new ew().a(dArr);
        int i3 = a.b;
        IPoint[] iPointArr2 = new IPoint[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            iPointArr2[i4] = new IPoint();
            iPointArr2[i4].x = (int) (dArr[a.a(i4) * 2] / B);
            iPointArr2[i4].y = (int) (dArr[(a.a(i4) * 2) + 1] / B);
        }
        return iPointArr2;
    }

    private double b(double d) {
        double a = a(d);
        Double.isNaN(a);
        return 1.0d / a;
    }

    private boolean b(CircleHoleOptions circleHoleOptions) {
        try {
            return ((double) AMapUtils.calculateLineDistance(circleHoleOptions.getCenter(), getCenter())) <= getRadius() - circleHoleOptions.getRadius();
        } catch (Throwable th) {
            ic.c(th, "CircleDelegateImp", "isCircleInCircle");
            th.printStackTrace();
            return true;
        }
    }

    private void e() {
        if (this.j != null) {
            this.x = (ef.e) this.j.u(3);
        }
    }

    private void f() throws RemoteException {
        MapConfig mapConfig = this.j.getMapConfig();
        if (this.p == null || this.p.size() <= 0) {
            return;
        }
        GLES20.glClearStencil(0);
        GLES20.glStencilMask(255);
        GLES20.glClear(1024);
        GLES20.glFlush();
        GLES20.glEnable(2960);
        GLES20.glColorMask(false, false, false, false);
        GLES20.glStencilFunc(512, 1, 255);
        GLES20.glStencilOp(7681, 7680, 7680);
        for (int i = 0; i < this.p.size(); i++) {
            BaseHoleOptions baseHoleOptions = this.p.get(i);
            boolean z2 = baseHoleOptions instanceof PolygonHoleOptions;
            if (z2) {
                a(a(((PolygonHoleOptions) baseHoleOptions).getPoints()), mapConfig.getSX(), mapConfig.getSY());
            } else if (baseHoleOptions instanceof CircleHoleOptions) {
                this.j.f();
                a((CircleHoleOptions) baseHoleOptions);
            }
            if (this.t != null && this.r > 0) {
                if (this.x == null || this.x.c()) {
                    e();
                }
                if (z2) {
                    ez.a(this.x, -1, this.f, this.t, getStrokeWidth(), this.u, this.r, this.s, this.j.x());
                } else if (baseHoleOptions instanceof CircleHoleOptions) {
                    ez.a(this.x, -1, -1, this.t, 10.0f, this.r, this.j.x(), 0.0f, 0);
                }
            }
        }
        GLES20.glColorMask(true, true, true, true);
        GLES20.glStencilFunc(517, 1, 255);
        GLES20.glStencilMask(0);
    }

    private void g() throws RemoteException {
        GLES20.glClearStencil(0);
        GLES20.glClear(1024);
        GLES20.glDisable(2960);
        MapConfig mapConfig = this.j.getMapConfig();
        if (this.p == null || this.p.size() <= 0) {
            return;
        }
        for (int i = 0; i < this.p.size(); i++) {
            BaseHoleOptions baseHoleOptions = this.p.get(i);
            boolean z2 = baseHoleOptions instanceof PolygonHoleOptions;
            if (z2) {
                a(a(((PolygonHoleOptions) baseHoleOptions).getPoints()), mapConfig.getSX(), mapConfig.getSY());
            } else if (baseHoleOptions instanceof CircleHoleOptions) {
                this.j.f();
                a((CircleHoleOptions) baseHoleOptions);
            }
            if (this.t != null && this.r > 0) {
                if (this.x == null || this.x.c()) {
                    e();
                }
                if (z2) {
                    ez.b(this.x, 0, this.e, this.t, this.d, this.u, this.r, this.s, this.j.x());
                } else if (baseHoleOptions instanceof CircleHoleOptions) {
                    ez.b(this.x, 0, this.e, this.t, this.d, this.r, this.j.x(), this.a, -1);
                }
            }
        }
    }

    public void a(CircleHoleOptions circleHoleOptions) throws RemoteException {
        if (circleHoleOptions.getCenter() != null) {
            IPoint obtain = IPoint.obtain();
            FPoint obtain2 = FPoint.obtain();
            GLMapState.lonlat2Geo(circleHoleOptions.getCenter().longitude, circleHoleOptions.getCenter().latitude, obtain);
            float[] fArr = new float[1086];
            double b = b(circleHoleOptions.getCenter().latitude) * circleHoleOptions.getRadius();
            obtain2.x = obtain.x - this.j.getMapConfig().getSX();
            obtain2.y = obtain.y - this.j.getMapConfig().getSY();
            int i = 0;
            fArr[0] = obtain2.x;
            fArr[1] = obtain2.y;
            fArr[2] = 0.0f;
            while (i < 361) {
                double d = i;
                Double.isNaN(d);
                double d2 = (d * 3.141592653589793d) / 180.0d;
                double sin = Math.sin(d2) * b;
                double cos = Math.cos(d2) * b;
                Double.isNaN(obtain.x);
                Double.isNaN(obtain.y);
                obtain2.x = ((int) (r11 + sin)) - this.j.getMapConfig().getSX();
                obtain2.y = ((int) (r10 + cos)) - this.j.getMapConfig().getSY();
                i++;
                int i2 = i * 3;
                fArr[i2] = obtain2.x;
                fArr[i2 + 1] = obtain2.y;
                fArr[i2 + 2] = 0.0f;
            }
            this.r = fArr.length / 3;
            this.t = fr.a(fArr);
            obtain.recycle();
            obtain2.recycle();
        }
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public void a(MapConfig mapConfig) throws RemoteException {
        if (this.b == null || this.c <= 0.0d || !this.h) {
            return;
        }
        b();
        f();
        if (this.k != null && this.l > 0) {
            if (this.x == null || this.x.c()) {
                e();
            }
            this.a = this.j.getMapConfig().getMapPerPixelUnitLength();
            ez.a(this.x, this.f, this.e, this.k, this.d, this.l, this.j.x(), this.a, this.j.f(this.w));
        }
        g();
        this.m = true;
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean a() {
        return true;
    }

    public boolean b() throws RemoteException {
        synchronized (v) {
            int i = 0;
            this.m = false;
            if (this.b != null) {
                float[] fArr = new float[1086];
                double b = b(this.b.latitude) * this.c;
                this.j.c();
                this.o.x = this.n.x - this.j.getMapConfig().getSX();
                this.o.y = this.n.y - this.j.getMapConfig().getSY();
                fArr[0] = this.o.x;
                fArr[1] = this.o.y;
                fArr[2] = 0.0f;
                while (i < 361) {
                    double d = i;
                    Double.isNaN(d);
                    double d2 = (d * 3.141592653589793d) / 180.0d;
                    double sin = Math.sin(d2) * b;
                    double cos = Math.cos(d2) * b;
                    double d3 = this.n.x;
                    Double.isNaN(d3);
                    int i2 = (int) (d3 + sin);
                    double d4 = this.n.y;
                    Double.isNaN(d4);
                    int i3 = (int) (d4 + cos);
                    this.o.x = i2 - this.j.getMapConfig().getSX();
                    this.o.y = i3 - this.j.getMapConfig().getSY();
                    i++;
                    int i4 = i * 3;
                    fArr[i4] = this.o.x;
                    fArr[i4 + 1] = this.o.y;
                    fArr[i4 + 2] = 0.0f;
                }
                this.l = fArr.length / 3;
                this.k = fr.a(fArr);
            }
        }
        return true;
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean c() {
        return this.m;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public boolean contains(LatLng latLng) throws RemoteException {
        if (this.p != null && this.p.size() > 0) {
            Iterator<BaseHoleOptions> it = this.p.iterator();
            while (it.hasNext()) {
                if (fr.a(it.next(), latLng)) {
                    return false;
                }
            }
        }
        return this.c >= ((double) AMapUtils.calculateLineDistance(this.b, latLng));
    }

    void d() {
        this.l = 0;
        if (this.k != null) {
            this.k.clear();
        }
        this.j.setRunLowFrame(false);
        setHoleOptions(this.q);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        try {
            this.b = null;
            if (this.k != null) {
                this.k.clear();
                this.k = null;
            }
            if (this.t != null) {
                this.t.clear();
                this.t = null;
            }
            if (this.u != null) {
                this.u.clear();
                this.u = null;
            }
            if (this.p != null) {
                this.p.clear();
            }
            if (this.q != null) {
                this.q.clear();
            }
            this.p = null;
            this.q = null;
        } catch (Throwable th) {
            ic.c(th, "CircleDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "CircleDelegateImp destroy");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public LatLng getCenter() throws RemoteException {
        return this.b;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public int getDottedLineType() {
        return this.w;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public int getFillColor() throws RemoteException {
        return this.f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public List<BaseHoleOptions> getHoleOptions() throws RemoteException {
        return this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.i == null) {
            this.i = this.j.d("Circle");
        }
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public double getRadius() throws RemoteException {
        return this.c;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public int getStrokeColor() throws RemoteException {
        return this.e;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public float getStrokeWidth() throws RemoteException {
        return this.d;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.g;
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
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.j.a(getId());
        this.j.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z2) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setCenter(LatLng latLng) throws RemoteException {
        synchronized (v) {
            if (latLng != null) {
                try {
                    this.b = latLng;
                    GLMapState.lonlat2Geo(latLng.longitude, latLng.latitude, this.n);
                    d();
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setDottedLineType(int i) {
        this.w = i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setFillColor(int i) throws RemoteException {
        this.f = i;
        this.j.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setHoleOptions(List<BaseHoleOptions> list) {
        try {
            this.q = list;
            if (this.p == null) {
                this.p = new ArrayList();
            } else {
                this.p.clear();
            }
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    BaseHoleOptions baseHoleOptions = list.get(i);
                    if (baseHoleOptions instanceof PolygonHoleOptions) {
                        PolygonHoleOptions polygonHoleOptions = (PolygonHoleOptions) baseHoleOptions;
                        if (a(polygonHoleOptions) && !fr.a(this.p, polygonHoleOptions)) {
                            this.p.add(polygonHoleOptions);
                        }
                    } else if (baseHoleOptions instanceof CircleHoleOptions) {
                        CircleHoleOptions circleHoleOptions = (CircleHoleOptions) baseHoleOptions;
                        if (b(circleHoleOptions) && !fr.a(this.p, circleHoleOptions)) {
                            this.p.add(circleHoleOptions);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            ic.c(th, "PolygonDelegateImp", "setHoleOptions");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setRadius(double d) throws RemoteException {
        this.c = d;
        d();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setStrokeColor(int i) throws RemoteException {
        this.e = i;
        this.j.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.ICircle
    public void setStrokeWidth(float f) throws RemoteException {
        this.d = f;
        this.j.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z2) throws RemoteException {
        this.h = z2;
        this.j.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.g = f;
        this.j.f();
        this.j.setRunLowFrame(false);
    }
}
