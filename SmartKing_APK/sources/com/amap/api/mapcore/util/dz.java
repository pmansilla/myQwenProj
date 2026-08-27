package com.amap.api.mapcore.util;

import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.os.RemoteException;
import com.amap.api.mapcore.util.ef;
import com.amap.api.maps.model.BaseHoleOptions;
import com.amap.api.maps.model.CircleHoleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolygonHoleOptions;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/* compiled from: PolygonDelegateImp.java */
/* loaded from: classes.dex */
public class dz implements dr {
    private static double C = 1.0E10d;
    private static float v = 4.0075016E7f;
    private static int w = 256;
    private static int x = 20;
    private FloatBuffer A;
    private FloatBuffer B;
    private ad b;
    private String e;
    private float f;
    private int g;
    private int h;
    private List<LatLng> i;
    private List<BaseHoleOptions> l;
    private FloatBuffer m;
    private FloatBuffer n;
    private ef.e u;
    private int y;
    private int z;
    private float c = 0.0f;
    private boolean d = true;
    private List<IPoint> j = new Vector();
    private List<BaseHoleOptions> k = new Vector();
    private int o = 0;
    private int p = 0;
    private boolean q = false;
    private float r = 0.0f;
    private Object s = new Object();
    Rect a = null;
    private float t = 0.0f;

    public dz(ad adVar) {
        this.b = adVar;
        try {
            this.e = getId();
        } catch (RemoteException e) {
            ic.c(e, "PolygonDelegateImp", "create");
            e.printStackTrace();
        }
    }

    private float a(double d) {
        double cos = Math.cos((d * 3.141592653589793d) / 180.0d);
        double d2 = v;
        Double.isNaN(d2);
        double d3 = cos * d2;
        double d4 = w << x;
        Double.isNaN(d4);
        return (float) (d3 / d4);
    }

    private void a(List<LatLng> list) throws RemoteException {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        if (this.a == null) {
            this.a = new Rect();
        }
        fr.a(this.a);
        this.j.clear();
        if (list != null) {
            Object obj = null;
            for (LatLng latLng : list) {
                if (!latLng.equals(obj)) {
                    IPoint obtain = IPoint.obtain();
                    this.b.a(latLng.latitude, latLng.longitude, obtain);
                    this.j.add(obtain);
                    fr.b(this.a, obtain.x, obtain.y);
                    builder.include(latLng);
                    obj = latLng;
                }
            }
            int size = this.j.size();
            if (size > 1) {
                IPoint iPoint = this.j.get(0);
                int i = size - 1;
                IPoint iPoint2 = this.j.get(i);
                if (iPoint.x == iPoint2.x && iPoint.y == iPoint2.y) {
                    this.j.remove(i);
                }
            }
        }
        this.a.sort();
        if (this.m != null) {
            this.m.clear();
        }
        if (this.n != null) {
            this.n.clear();
        }
        if (fr.a(this.j, 0, this.j.size())) {
            Collections.reverse(this.j);
        }
        this.o = 0;
        this.p = 0;
        this.b.setRunLowFrame(false);
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
            if (C == 1.0E10d) {
                C = 1.0E8d;
            } else {
                C = 1.0E10d;
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
        this.y = iPointArr.length;
        this.z = a.length;
        this.A = fr.a(fArr);
        this.B = fr.a(fArr2);
    }

    private boolean a(PolygonHoleOptions polygonHoleOptions) {
        boolean z = true;
        try {
            List<LatLng> points = polygonHoleOptions.getPoints();
            int i = 0;
            while (i < points.size()) {
                boolean a = fr.a(points.get(i), getPoints());
                if (!a) {
                    return a;
                }
                i++;
                z = a;
            }
            return z;
        } catch (Throwable th) {
            ic.c(th, "PolygonDelegateImp", "isPolygonInPolygon");
            th.printStackTrace();
            return z;
        }
    }

    private boolean a(IPoint iPoint, IPoint iPoint2) {
        return ((float) (iPoint2.x - iPoint.x)) >= this.r || ((float) (iPoint2.x - iPoint.x)) <= (-this.r) || ((float) (iPoint2.y - iPoint.y)) >= this.r || ((float) (iPoint2.y - iPoint.y)) <= (-this.r);
    }

    private boolean a(Rectangle rectangle) {
        this.t = this.b.g();
        f();
        if (this.t <= 10 || rectangle == null) {
            return false;
        }
        try {
            return !rectangle.contains(this.a);
        } catch (Throwable unused) {
            return false;
        }
    }

    static IPoint[] a(IPoint[] iPointArr) {
        int length = iPointArr.length;
        double[] dArr = new double[length * 2];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            double d = iPointArr[i].x;
            double d2 = C;
            Double.isNaN(d);
            dArr[i2] = d * d2;
            double d3 = iPointArr[i].y;
            double d4 = C;
            Double.isNaN(d3);
            dArr[i2 + 1] = d3 * d4;
        }
        fn a = new ew().a(dArr);
        int i3 = a.b;
        IPoint[] iPointArr2 = new IPoint[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            iPointArr2[i4] = new IPoint();
            iPointArr2[i4].x = (int) (dArr[a.a(i4) * 2] / C);
            iPointArr2[i4].y = (int) (dArr[(a.a(i4) * 2) + 1] / C);
        }
        return iPointArr2;
    }

    private double b(double d) {
        double a = a(d);
        Double.isNaN(a);
        return 1.0d / a;
    }

    private List<IPoint> b(List<LatLng> list) throws RemoteException {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            LatLng latLng = null;
            for (LatLng latLng2 : list) {
                if (!latLng2.equals(latLng)) {
                    IPoint obtain = IPoint.obtain();
                    this.b.a(latLng2.latitude, latLng2.longitude, obtain);
                    arrayList.add(obtain);
                    fr.b(this.a, obtain.x, obtain.y);
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

    private void b() {
        if (this.b != null) {
            this.u = (ef.e) this.b.u(3);
        }
    }

    private void b(List<IPoint> list, int i, int i2) throws RemoteException {
        int i3;
        f();
        ArrayList arrayList = new ArrayList();
        int size = list.size();
        if (size < 2) {
            return;
        }
        IPoint iPoint = list.get(0);
        arrayList.add(iPoint);
        IPoint iPoint2 = iPoint;
        int i4 = 1;
        while (true) {
            i3 = size - 1;
            if (i4 >= i3) {
                break;
            }
            IPoint iPoint3 = list.get(i4);
            if (a(iPoint2, iPoint3)) {
                arrayList.add(iPoint3);
                iPoint2 = iPoint3;
            }
            i4++;
        }
        arrayList.add(list.get(i3));
        float[] fArr = new float[arrayList.size() * 3];
        IPoint[] iPointArr = new IPoint[arrayList.size()];
        Iterator it = arrayList.iterator();
        int i5 = 0;
        while (it.hasNext()) {
            IPoint iPoint4 = (IPoint) it.next();
            int i6 = i5 * 3;
            fArr[i6] = iPoint4.x - i;
            fArr[i6 + 1] = iPoint4.y - i2;
            fArr[i6 + 2] = 0.0f;
            iPointArr[i5] = iPoint4;
            i5++;
        }
        IPoint[] a = a(iPointArr);
        if (a.length == 0) {
            if (C == 1.0E10d) {
                C = 1.0E8d;
            } else {
                C = 1.0E10d;
            }
            a = a(iPointArr);
        }
        float[] fArr2 = new float[a.length * 3];
        int i7 = 0;
        for (IPoint iPoint5 : a) {
            int i8 = i7 * 3;
            fArr2[i8] = iPoint5.x - i;
            fArr2[i8 + 1] = iPoint5.y - i2;
            fArr2[i8 + 2] = 0.0f;
            i7++;
        }
        this.o = iPointArr.length;
        this.p = a.length;
        this.m = fr.a(fArr);
        this.n = fr.a(fArr2);
    }

    private boolean b(CircleHoleOptions circleHoleOptions) {
        try {
            if (fr.b(getPoints(), circleHoleOptions)) {
                return false;
            }
            return contains(circleHoleOptions.getCenter());
        } catch (Throwable th) {
            ic.c(th, "PolygonDelegateImp", "isCircleInPolygon");
            th.printStackTrace();
            return false;
        }
    }

    private void d() throws RemoteException {
        MapConfig mapConfig = this.b.getMapConfig();
        if (this.k == null || this.k.size() <= 0) {
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
        for (int i = 0; i < this.k.size(); i++) {
            BaseHoleOptions baseHoleOptions = this.k.get(i);
            boolean z = baseHoleOptions instanceof PolygonHoleOptions;
            if (z) {
                a(b(((PolygonHoleOptions) baseHoleOptions).getPoints()), mapConfig.getSX(), mapConfig.getSY());
            } else if (baseHoleOptions instanceof CircleHoleOptions) {
                this.b.f();
                a((CircleHoleOptions) baseHoleOptions);
            }
            if (this.A != null && this.y > 0) {
                if (this.u == null || this.u.c()) {
                    b();
                }
                if (z) {
                    ez.a(this.u, -1, this.h, this.A, this.f, this.B, this.y, this.z, this.b.x());
                } else if (baseHoleOptions instanceof CircleHoleOptions) {
                    ez.a(this.u, Color.argb(200, 80, 1, 1), Color.argb(200, 1, 1, 1), this.A, 5.0f, this.y, this.b.x(), 0.0f, 0);
                }
            }
        }
        GLES20.glColorMask(true, true, true, true);
        GLES20.glStencilFunc(517, 1, 255);
        GLES20.glStencilMask(0);
    }

    private void e() throws RemoteException {
        GLES20.glClearStencil(0);
        GLES20.glClear(1024);
        GLES20.glDisable(2960);
        MapConfig mapConfig = this.b.getMapConfig();
        if (this.k == null || this.k.size() <= 0) {
            return;
        }
        for (int i = 0; i < this.k.size(); i++) {
            BaseHoleOptions baseHoleOptions = this.k.get(i);
            boolean z = baseHoleOptions instanceof PolygonHoleOptions;
            if (z) {
                a(b(((PolygonHoleOptions) baseHoleOptions).getPoints()), mapConfig.getSX(), mapConfig.getSY());
            } else if (baseHoleOptions instanceof CircleHoleOptions) {
                this.b.f();
                a((CircleHoleOptions) baseHoleOptions);
            }
            if (this.A != null && this.y > 0) {
                if (this.u == null || this.u.c()) {
                    b();
                }
                if (z) {
                    ez.b(this.u, 0, this.h, this.A, this.f, this.B, this.y, this.z, this.b.x());
                } else if (baseHoleOptions instanceof CircleHoleOptions) {
                    ez.b(this.u, 0, this.h, this.A, this.f, this.y, this.b.x(), 1.0f, -1);
                }
            }
        }
    }

    private void f() {
        float g = this.b.g();
        if (this.j.size() <= 5000) {
            this.r = this.b.c().getMapLenWithWin(2);
        } else if (g > 12) {
            this.r = this.b.c().getMapLenWithWin(10);
        } else {
            float f = (this.f / 2.0f) + (g / 2.0f);
            this.r = this.b.c().getMapLenWithWin((int) (f <= 200.0f ? f : 200.0f));
        }
    }

    public void a(CircleHoleOptions circleHoleOptions) throws RemoteException {
        if (circleHoleOptions.getCenter() != null) {
            IPoint obtain = IPoint.obtain();
            FPoint obtain2 = FPoint.obtain();
            GLMapState.lonlat2Geo(circleHoleOptions.getCenter().longitude, circleHoleOptions.getCenter().latitude, obtain);
            float[] fArr = new float[1086];
            double b = b(circleHoleOptions.getCenter().latitude) * circleHoleOptions.getRadius();
            int sx = this.b.getMapConfig().getSX();
            int sy = this.b.getMapConfig().getSY();
            obtain2.x = obtain.x - sx;
            obtain2.y = obtain.y - sy;
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
                int i2 = i;
                double d3 = obtain.x;
                Double.isNaN(d3);
                int i3 = (int) (d3 + sin);
                double d4 = obtain.y;
                Double.isNaN(d4);
                int i4 = (int) (d4 + cos);
                obtain2.x = i3 - sx;
                obtain2.y = i4 - sy;
                obtain2.x = i3 - this.b.getMapConfig().getSX();
                obtain2.y = i4 - this.b.getMapConfig().getSY();
                i = i2 + 1;
                int i5 = i * 3;
                fArr[i5] = obtain2.x;
                fArr[i5 + 1] = obtain2.y;
                fArr[i5 + 2] = 0.0f;
            }
            this.y = fArr.length / 3;
            this.A = fr.a(fArr);
            obtain.recycle();
            obtain2.recycle();
        }
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public void a(MapConfig mapConfig) throws RemoteException {
        if (this.j == null || this.j.size() == 0) {
            return;
        }
        Rectangle geoRectangle = mapConfig.getGeoRectangle();
        geoRectangle.getClipRect();
        List<IPoint> list = this.j;
        a(geoRectangle);
        d();
        if (list.size() > 2) {
            b(list, mapConfig.getSX(), mapConfig.getSY());
            if (this.m != null && this.n != null && this.o > 0 && this.p > 0) {
                if (this.u == null || this.u.c()) {
                    b();
                }
                ez.a(this.u, this.g, this.h, this.m, this.f, this.n, this.o, this.p, this.b.x());
            }
        }
        e();
        this.q = true;
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean a() {
        return this.b.getMapConfig().getGeoRectangle().isOverlap(this.a);
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean c() {
        return this.q;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public boolean contains(LatLng latLng) throws RemoteException {
        if (latLng == null) {
            return false;
        }
        try {
            if (this.k != null && this.k.size() > 0) {
                Iterator<BaseHoleOptions> it = this.k.iterator();
                while (it.hasNext()) {
                    if (fr.a(it.next(), latLng)) {
                        return false;
                    }
                }
            }
            return fr.a(latLng, getPoints());
        } catch (Throwable th) {
            ic.c(th, "PolygonDelegateImp", "contains");
            th.printStackTrace();
            return false;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        try {
            if (this.m != null) {
                this.m.clear();
                this.m = null;
            }
            if (this.n != null) {
                this.n = null;
            }
            if (this.A != null) {
                this.A.clear();
                this.A = null;
            }
            if (this.B != null) {
                this.B.clear();
                this.B = null;
            }
            if (this.k != null) {
                this.k.clear();
            }
            if (this.l != null) {
                this.l.clear();
            }
            this.k = null;
            this.l = null;
        } catch (Throwable th) {
            ic.c(th, "PolygonDelegateImp", "destroy");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public int getFillColor() throws RemoteException {
        return this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public List<BaseHoleOptions> getHoleOptions() {
        return this.k;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.e == null) {
            this.e = this.b.d("Polygon");
        }
        return this.e;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public List<LatLng> getPoints() throws RemoteException {
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public int getStrokeColor() throws RemoteException {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public float getStrokeWidth() throws RemoteException {
        return this.f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.c;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() throws RemoteException {
        return super.hashCode();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.d;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.b.a(getId());
        this.b.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setFillColor(int i) throws RemoteException {
        this.g = i;
        this.b.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setHoleOptions(List<BaseHoleOptions> list) {
        try {
            this.l = list;
            if (this.k == null) {
                this.k = new ArrayList();
            } else {
                this.k.clear();
            }
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    BaseHoleOptions baseHoleOptions = list.get(i);
                    if (baseHoleOptions instanceof PolygonHoleOptions) {
                        PolygonHoleOptions polygonHoleOptions = (PolygonHoleOptions) baseHoleOptions;
                        if (a(polygonHoleOptions) && !fr.a(this.k, polygonHoleOptions)) {
                            this.k.add(polygonHoleOptions);
                        }
                    } else if (baseHoleOptions instanceof CircleHoleOptions) {
                        CircleHoleOptions circleHoleOptions = (CircleHoleOptions) baseHoleOptions;
                        if (b(circleHoleOptions) && !fr.a(this.k, circleHoleOptions)) {
                            this.k.add(circleHoleOptions);
                        }
                    }
                }
            } else {
                this.k.clear();
            }
        } catch (Throwable th) {
            ic.c(th, "PolygonDelegateImp", "setHoleOptions");
            th.printStackTrace();
        }
        this.b.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setPoints(List<LatLng> list) throws RemoteException {
        synchronized (this.s) {
            this.i = list;
            a(list);
            this.b.setRunLowFrame(false);
            setHoleOptions(this.l);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setStrokeColor(int i) throws RemoteException {
        this.h = i;
        this.b.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolygon
    public void setStrokeWidth(float f) throws RemoteException {
        this.f = f;
        this.b.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.d = z;
        this.b.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.c = f;
        this.b.f();
        this.b.setRunLowFrame(false);
    }
}
