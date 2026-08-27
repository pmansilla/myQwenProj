package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.autonavi.amap.mapcore.AMapNativePolyline;
import com.autonavi.amap.mapcore.AMapNativeRenderer;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.FPoint3;
import com.autonavi.amap.mapcore.FPointBounds;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* compiled from: PolylineDelegateImp.java */
/* loaded from: classes.dex */
public class ea implements ds {
    private float J;
    private float K;
    private float L;
    private float M;
    private float[] P;
    private int[] Q;
    private int[] R;
    private PolylineOptions U;
    private ef Y;
    private ab e;
    private String f;
    private FloatBuffer p;
    private List<IPoint> g = new ArrayList();
    private List<FPoint> h = new ArrayList();
    private List<LatLng> i = new ArrayList();
    private List<BitmapDescriptor> j = new ArrayList();
    private List<am> k = new ArrayList();
    private List<Integer> l = new ArrayList();
    private List<Integer> m = new ArrayList();
    private List<Integer> n = new ArrayList();
    private List<Integer> o = new ArrayList();
    private BitmapDescriptor q = null;
    private Object r = new Object();
    private boolean s = true;
    private boolean t = true;
    private boolean u = false;
    private boolean v = false;
    private boolean w = false;
    private boolean x = true;
    private boolean y = false;
    private boolean z = false;
    private boolean A = true;
    private int B = 0;
    private int C = 0;
    private int D = -16777216;
    private int E = 0;
    private int F = 0;
    private float G = 10.0f;
    private float H = 0.0f;
    private float I = 0.0f;
    private float N = 1.0f;
    private float O = 0.0f;
    private boolean S = false;
    private FPointBounds T = null;
    Rect a = null;
    private int V = 0;
    private PolylineOptions.LineJoinType W = PolylineOptions.LineJoinType.LineJoinBevel;
    private PolylineOptions.LineCapType X = PolylineOptions.LineCapType.LineCapRound;
    private long Z = 0;
    private boolean aa = false;
    private float ab = -1.0f;
    private float ac = -1.0f;
    private float ad = -1.0f;
    private int ae = -1;
    private List<IPoint> af = new ArrayList();
    int b = 0;
    ArrayList<FPoint> c = new ArrayList<>();
    long d = 0;

    public ea(ab abVar, PolylineOptions polylineOptions) {
        this.e = abVar;
        setOptions(polylineOptions);
        try {
            this.f = getId();
        } catch (RemoteException e) {
            ic.c(e, "PolylineDelegateImp", "create");
            e.printStackTrace();
        }
    }

    private double a(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d5 - d3;
        double d8 = d - d3;
        double d9 = d6 - d4;
        double d10 = d2 - d4;
        double d11 = (d7 * d8) + (d9 * d10);
        if (d11 <= 0.0d) {
            return Math.sqrt((d8 * d8) + (d10 * d10));
        }
        double d12 = (d7 * d7) + (d9 * d9);
        if (d11 >= d12) {
            double d13 = d - d5;
            double d14 = d2 - d6;
            return Math.sqrt((d13 * d13) + (d14 * d14));
        }
        double d15 = d11 / d12;
        double d16 = d - (d3 + (d7 * d15));
        double d17 = (d4 + (d9 * d15)) - d2;
        return Math.sqrt((d16 * d16) + (d17 * d17));
    }

    private double a(FPoint fPoint, FPoint fPoint2, FPoint fPoint3) {
        return a(fPoint.x, fPoint.y, fPoint2.x, fPoint2.y, fPoint3.x, fPoint3.y);
    }

    private int a(boolean z, BitmapDescriptor bitmapDescriptor, boolean z2) {
        if (z2) {
            f();
        }
        int i = 0;
        am amVar = null;
        if (z && (amVar = this.e.a(bitmapDescriptor)) != null) {
            int k = amVar.k();
            a(amVar);
            return k;
        }
        if (amVar == null) {
            amVar = new am(bitmapDescriptor, 0);
        }
        Bitmap bitmap = bitmapDescriptor.getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            i = g();
            if (z) {
                amVar.a(i);
                this.e.g().a(amVar);
            }
            a(amVar);
            fr.b(i, bitmap, true);
        }
        return i;
    }

    private void a(float f, MapConfig mapConfig) {
        List<FPoint> list;
        int[] iArr;
        if (!this.w) {
            try {
                if (this.j != null) {
                    this.R = new int[this.j.size()];
                    boolean z = Build.VERSION.SDK_INT >= 12;
                    f();
                    Iterator<BitmapDescriptor> it = this.j.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        this.R[i] = a(z, it.next(), false);
                        i++;
                    }
                    this.w = true;
                }
            } catch (Throwable th) {
                ic.c(th, "MarkerDelegateImp", "loadtexture");
                return;
            }
        }
        FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
        try {
            List<FPoint> list2 = this.h;
            if (a(clipMapRect)) {
                synchronized (this.r) {
                    list = fr.b(clipMapRect, this.h, false);
                }
            } else {
                list = list2;
            }
            if (list.size() >= 2) {
                d(list);
                synchronized (this.n) {
                    iArr = new int[this.n.size()];
                    for (int i2 = 0; i2 < iArr.length; i2++) {
                        int intValue = this.n.get(i2).intValue();
                        if (intValue < 0) {
                            intValue = 0;
                        }
                        iArr[i2] = this.R[intValue];
                    }
                }
                if (true && (this.Q != null)) {
                    AMapNativeRenderer.nativeDrawLineByMultiTextureID(this.P, this.b, f, iArr, iArr.length, this.Q, this.Q.length, 1.0f - this.N, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
                }
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    private void a(am amVar) {
        if (amVar != null) {
            this.k.add(amVar);
            amVar.l();
        }
    }

    private boolean a(FPoint fPoint, FPoint fPoint2) {
        return ((fPoint instanceof FPoint3) && (fPoint2 instanceof FPoint3) && ((FPoint3) fPoint).colorIndex != ((FPoint3) fPoint2).colorIndex) || Math.abs(fPoint2.x - fPoint.x) >= this.O || Math.abs(fPoint2.y - fPoint.y) >= this.O;
    }

    private boolean a(FPoint[] fPointArr) {
        this.I = this.e.g().g();
        e();
        if (this.I <= (this.g.size() > 10000 ? 7 : 3)) {
            return false;
        }
        try {
            if (this.e.g() == null) {
                return false;
            }
            if (fr.a(this.T.northeast, fPointArr)) {
                return !fr.a(this.T.southwest, fPointArr);
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    private FPoint b(LatLng latLng) {
        IPoint obtain = IPoint.obtain();
        this.e.g().a(latLng.latitude, latLng.longitude, obtain);
        FPoint obtain2 = FPoint.obtain();
        this.e.g().a(obtain.x, obtain.y, obtain2);
        obtain.recycle();
        return obtain2;
    }

    private void b(float f, MapConfig mapConfig) {
        List<FPoint> list;
        int[] iArr = new int[this.m.size()];
        for (int i = 0; i < this.m.size(); i++) {
            iArr[i] = this.m.get(i).intValue();
        }
        FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
        try {
            List<FPoint> list2 = this.h;
            if (a(clipMapRect)) {
                synchronized (this.r) {
                    list = fr.b(clipMapRect, this.h, false);
                }
            } else {
                list = list2;
            }
            if (list.size() >= 2) {
                d(list);
                int[] iArr2 = new int[this.o.size()];
                for (int i2 = 0; i2 < iArr2.length; i2++) {
                    iArr2[i2] = this.o.get(i2).intValue();
                }
                if (true && (this.Q != null)) {
                    AMapNativeRenderer.nativeDrawGradientColorLine(this.P, this.b, f, iArr2, iArr2.length, this.Q, this.Q.length, this.e.g().d(), this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void b(MapConfig mapConfig) {
        float mapLenWithWin = this.e.g().c().getMapLenWithWin((int) this.G);
        switch (this.B) {
            case 0:
                f(mapLenWithWin, mapConfig);
                return;
            case 1:
                if (this.A) {
                    d(mapLenWithWin, mapConfig);
                    return;
                } else {
                    f(mapLenWithWin, mapConfig);
                    return;
                }
            case 2:
                if (this.F == -1) {
                    f(mapLenWithWin, mapConfig);
                    return;
                } else {
                    e(mapLenWithWin, mapConfig);
                    return;
                }
            case 3:
                c(mapLenWithWin, mapConfig);
                return;
            case 4:
                b(mapLenWithWin, mapConfig);
                return;
            case 5:
                if (this.A) {
                    a(mapLenWithWin, mapConfig);
                    return;
                } else {
                    c(mapLenWithWin, mapConfig);
                    return;
                }
            default:
                return;
        }
    }

    private void c(float f, MapConfig mapConfig) {
        List<FPoint> list;
        int[] iArr = new int[this.m.size()];
        for (int i = 0; i < this.m.size(); i++) {
            iArr[i] = this.m.get(i).intValue();
        }
        FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
        try {
            List<FPoint> list2 = this.h;
            if (a(clipMapRect)) {
                synchronized (this.r) {
                    list = fr.b(clipMapRect, this.h, false);
                }
            } else {
                list = list2;
            }
            if (list.size() >= 2) {
                d(list);
                int[] iArr2 = new int[this.o.size()];
                for (int i2 = 0; i2 < iArr2.length; i2++) {
                    iArr2[i2] = this.o.get(i2).intValue();
                }
                if (true && (this.Q != null)) {
                    AMapNativeRenderer.nativeDrawLineByMultiColor(this.P, this.b, f, this.e.g().d(), iArr2, iArr2.length, this.Q, this.Q.length, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void d(float f, MapConfig mapConfig) {
        List<FPoint> list;
        if (!this.w) {
            synchronized (this) {
                try {
                    try {
                        if (this.q != null) {
                            this.C = a(Build.VERSION.SDK_INT >= 12, this.q, true);
                            this.w = true;
                        }
                    } catch (Throwable th) {
                        ic.c(th, "MarkerDelegateImp", "loadtexture");
                        return;
                    }
                } finally {
                }
            }
        }
        try {
            if (mapConfig.getChangeRatio() == 1.0d && this.P != null) {
                this.V++;
                if (this.V > 2) {
                    AMapNativeRenderer.nativeDrawLineByTextureID(this.P, this.b, f, this.C, this.K, this.L, this.M, this.J, 1.0f - this.N, false, false, false, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
                    return;
                }
            }
            this.V = 0;
            FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
            List<FPoint> list2 = this.h;
            if (a(clipMapRect)) {
                synchronized (this.r) {
                    list = fr.a(clipMapRect, this.h, false);
                }
            } else {
                list = list2;
            }
            if (list.size() >= 2) {
                d(list);
                AMapNativeRenderer.nativeDrawLineByTextureID(this.P, this.b, f, this.C, this.K, this.L, this.M, this.J, 1.0f - this.N, false, false, false, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
            }
        } catch (Throwable unused) {
        }
    }

    private void d(List<FPoint> list) throws RemoteException {
        int i;
        this.c.clear();
        int size = list.size();
        if (size < 2) {
            return;
        }
        int i2 = 0;
        FPoint fPoint = list.get(0);
        this.c.add(fPoint);
        FPoint fPoint2 = fPoint;
        int i3 = 1;
        while (true) {
            i = size - 1;
            if (i3 >= i) {
                break;
            }
            FPoint fPoint3 = list.get(i3);
            if (i3 == 1 || a(fPoint2, fPoint3)) {
                this.c.add(fPoint3);
                fPoint2 = fPoint3;
            } else {
                this.c.set(this.c.size() - 1, fPoint3);
            }
            i3++;
        }
        this.c.add(list.get(i));
        int size2 = this.c.size() * 3;
        this.b = size2;
        if (this.P == null || this.P.length < this.b) {
            this.P = new float[size2];
        }
        if (this.B != 5 && this.B != 3 && this.B != 4) {
            Iterator<FPoint> it = this.c.iterator();
            while (it.hasNext()) {
                FPoint next = it.next();
                int i4 = i2 * 3;
                this.P[i4] = next.x;
                this.P[i4 + 1] = next.y;
                this.P[i4 + 2] = 0.0f;
                i2++;
            }
            return;
        }
        int[] iArr = new int[this.c.size()];
        ArrayList arrayList = new ArrayList();
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < size2 / 3; i7++) {
            FPoint3 fPoint32 = (FPoint3) this.c.get(i7);
            int i8 = i7 * 3;
            this.P[i8] = fPoint32.x;
            this.P[i8 + 1] = fPoint32.y;
            this.P[i8 + 2] = 0.0f;
            int i9 = fPoint32.colorIndex;
            if (i7 == 0) {
                arrayList.add(Integer.valueOf(i9));
            } else if (i9 != i5) {
                if (i9 == -1) {
                    i9 = i5;
                }
                arrayList.add(Integer.valueOf(i9));
            }
            iArr[i6] = i7;
            i6++;
            i5 = i9;
        }
        this.Q = new int[arrayList.size()];
        System.arraycopy(iArr, 0, this.Q, 0, this.Q.length);
        this.n = arrayList;
        this.o = arrayList;
    }

    private void e() {
        float mapPerPixelUnitLength = this.e.g().getMapConfig().getMapPerPixelUnitLength();
        if (this.E <= 5000) {
            this.O = mapPerPixelUnitLength * 2.0f;
            return;
        }
        if (this.I > 12) {
            this.O = mapPerPixelUnitLength * 10.0f;
            return;
        }
        float f = (this.G / 2.0f) + (this.I / 2.0f);
        if (f > 200.0f) {
            f = 200.0f;
        }
        this.O = mapPerPixelUnitLength * f;
    }

    private void e(float f, MapConfig mapConfig) {
        if (!this.w) {
            synchronized (this) {
                try {
                    try {
                        if (this.q != null) {
                            this.C = a(Build.VERSION.SDK_INT >= 12, this.q, true);
                            this.w = true;
                        }
                    } catch (Throwable th) {
                        ic.c(th, "MarkerDelegateImp", "loadtexture");
                        return;
                    }
                } finally {
                }
            }
        }
        try {
            List<FPoint> list = this.h;
            if (this.e.g() == null) {
                return;
            }
            if (mapConfig.getChangeRatio() == 1.0d && this.P != null) {
                this.V++;
                if (this.V > 2) {
                    AMapNativeRenderer.nativeDrawLineByTextureID(this.P, this.b, f, this.e.g().f(this.F), this.K, this.L, this.M, this.J, 0.0f, true, true, false, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
                    return;
                }
            }
            this.V = 0;
            FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
            if (a(clipMapRect)) {
                synchronized (this.r) {
                    list = fr.a(clipMapRect, this.h, false);
                }
            }
            if (list.size() >= 2) {
                d(list);
                AMapNativeRenderer.nativeDrawLineByTextureID(this.P, this.b, f, this.e.g().f(this.F), this.K, this.L, this.M, this.J, 0.0f, true, true, false, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
            }
        } catch (Throwable unused) {
        }
    }

    private void e(List<BitmapDescriptor> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (list.size() <= 1) {
            setCustomTexture(list.get(0));
            return;
        }
        this.t = false;
        this.B = 5;
        this.j = list;
        this.e.g().setRunLowFrame(false);
    }

    private List<Integer> f(List<Integer> list) {
        int[] iArr = new int[list.size()];
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < list.size(); i3++) {
            int intValue = list.get(i3).intValue();
            if (i3 == 0) {
                arrayList.add(Integer.valueOf(intValue));
            } else if (intValue != i) {
                arrayList.add(Integer.valueOf(intValue));
            }
            iArr[i2] = i3;
            i2++;
            i = intValue;
        }
        this.Q = new int[arrayList.size()];
        System.arraycopy(iArr, 0, this.Q, 0, this.Q.length);
        return arrayList;
    }

    private void f() {
        if (this.k != null) {
            for (am amVar : this.k) {
                if (amVar != null && this.e != null) {
                    this.e.a(amVar);
                }
            }
            this.k.clear();
        }
    }

    private void f(float f, MapConfig mapConfig) {
        try {
            List<FPoint> list = this.h;
            if (this.e.g() == null) {
                return;
            }
            if (mapConfig.getChangeRatio() == 1.0d && this.P != null) {
                this.V++;
                if (this.V > 2) {
                    if (this.Z == 0 || this.Y == null) {
                        AMapNativeRenderer.nativeDrawLineByTextureID(this.P, this.b, f, this.e.g().d(), this.K, this.L, this.M, this.J, 0.0f, false, true, false, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
                        return;
                    } else {
                        AMapNativePolyline.nativeDrawLineByTextureID(this.Z, this.P, this.b, f, this.e.g().d(), this.K, this.L, this.M, this.J, 0.0f, false, true, false, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
                        return;
                    }
                }
            }
            this.V = 0;
            FPoint[] clipMapRect = mapConfig.getGeoRectangle().getClipMapRect();
            if (a(clipMapRect)) {
                synchronized (this.r) {
                    list = fr.a(clipMapRect, this.h, false);
                }
            }
            if (list.size() >= 2) {
                d(list);
                if (this.Z == 0 || this.Y == null) {
                    AMapNativeRenderer.nativeDrawLineByTextureID(this.P, this.b, f, this.e.g().d(), this.K, this.L, this.M, this.J, 0.0f, false, true, false, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
                } else {
                    AMapNativePolyline.nativeDrawLineByTextureID(this.Z, this.P, this.b, f, this.e.g().d(), this.K, this.L, this.M, this.J, 0.0f, false, true, false, this.e.h(), this.X.getTypeValue(), this.W.getTypeValue());
                }
            }
        } catch (Throwable unused) {
        }
    }

    private int g() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    private ArrayList<FPoint> h() {
        ArrayList<FPoint> arrayList = new ArrayList<>();
        int i = 0;
        while (i < this.P.length) {
            float f = this.P[i];
            int i2 = i + 1;
            arrayList.add(FPoint.obtain(f, this.P[i2]));
            i = i2 + 1 + 1;
        }
        return arrayList;
    }

    IPoint a(IPoint iPoint, IPoint iPoint2, IPoint iPoint3, double d, int i) {
        IPoint obtain = IPoint.obtain();
        double d2 = iPoint2.x - iPoint.x;
        double d3 = iPoint2.y - iPoint.y;
        Double.isNaN(d3);
        Double.isNaN(d3);
        Double.isNaN(d2);
        Double.isNaN(d2);
        double d4 = i;
        Double.isNaN(d4);
        double sqrt = (d4 * d) / Math.sqrt(((d3 * d3) / (d2 * d2)) + 1.0d);
        double d5 = iPoint3.y;
        Double.isNaN(d5);
        obtain.y = (int) (sqrt + d5);
        double d6 = iPoint3.y - obtain.y;
        Double.isNaN(d6);
        Double.isNaN(d3);
        Double.isNaN(d2);
        double d7 = (d6 * d3) / d2;
        double d8 = iPoint3.x;
        Double.isNaN(d8);
        obtain.x = (int) (d7 + d8);
        return obtain;
    }

    public void a(int i) {
        this.F = i;
    }

    public void a(ef efVar) {
        this.Y = efVar;
    }

    void a(LatLng latLng, LatLng latLng2, List<IPoint> list, LatLngBounds.Builder builder) {
        double abs = (Math.abs(latLng.longitude - latLng2.longitude) * 3.141592653589793d) / 180.0d;
        LatLng latLng3 = new LatLng((latLng2.latitude + latLng.latitude) / 2.0d, (latLng2.longitude + latLng.longitude) / 2.0d, false);
        builder.include(latLng).include(latLng3).include(latLng2);
        int i = latLng3.latitude > 0.0d ? -1 : 1;
        IPoint obtain = IPoint.obtain();
        this.e.g().a(latLng.latitude, latLng.longitude, obtain);
        IPoint obtain2 = IPoint.obtain();
        this.e.g().a(latLng2.latitude, latLng2.longitude, obtain2);
        IPoint obtain3 = IPoint.obtain();
        this.e.g().a(latLng3.latitude, latLng3.longitude, obtain3);
        double d = abs * 0.5d;
        double cos = Math.cos(d);
        IPoint a = a(obtain, obtain2, obtain3, Math.hypot(obtain.x - obtain2.x, obtain.y - obtain2.y) * 0.5d * Math.tan(d), i);
        ArrayList arrayList = new ArrayList();
        arrayList.add(obtain);
        arrayList.add(a);
        arrayList.add(obtain2);
        a(arrayList, list, cos);
        obtain.recycle();
        a.recycle();
        obtain2.recycle();
    }

    public void a(PolylineOptions.LineCapType lineCapType) {
        this.X = lineCapType;
    }

    public void a(PolylineOptions.LineJoinType lineJoinType) {
        this.W = lineJoinType;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x005b A[Catch: all -> 0x01c4, DONT_GENERATE, TryCatch #1 {, blocks: (B:11:0x0028, B:13:0x0030, B:15:0x0036, B:18:0x003d, B:20:0x0044, B:22:0x004a, B:25:0x0053, B:27:0x005b, B:30:0x005d, B:34:0x0080, B:37:0x00a0, B:41:0x00a6, B:40:0x00ab, B:45:0x0137, B:90:0x00ae, B:92:0x00b7, B:94:0x00c6, B:95:0x00c8, B:114:0x010e, B:115:0x010f, B:118:0x0121, B:122:0x0127, B:121:0x012c, B:126:0x01c2, B:128:0x0040, B:97:0x00c9, B:99:0x00cd, B:101:0x00d1, B:103:0x00d9, B:104:0x010a, B:106:0x00e9, B:108:0x00ed, B:110:0x00f8), top: B:10:0x0028, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x005d A[Catch: all -> 0x01c4, TryCatch #1 {, blocks: (B:11:0x0028, B:13:0x0030, B:15:0x0036, B:18:0x003d, B:20:0x0044, B:22:0x004a, B:25:0x0053, B:27:0x005b, B:30:0x005d, B:34:0x0080, B:37:0x00a0, B:41:0x00a6, B:40:0x00ab, B:45:0x0137, B:90:0x00ae, B:92:0x00b7, B:94:0x00c6, B:95:0x00c8, B:114:0x010e, B:115:0x010f, B:118:0x0121, B:122:0x0127, B:121:0x012c, B:126:0x01c2, B:128:0x0040, B:97:0x00c9, B:99:0x00cd, B:101:0x00d1, B:103:0x00d9, B:104:0x010a, B:106:0x00e9, B:108:0x00ed, B:110:0x00f8), top: B:10:0x0028, inners: #3 }] */
    @Override // com.amap.api.mapcore.util.Cdo
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(com.autonavi.amap.mapcore.MapConfig r17) throws android.os.RemoteException {
        /*
            Method dump skipped, instructions count: 455
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.ea.a(com.autonavi.amap.mapcore.MapConfig):void");
    }

    void a(List<LatLng> list) throws RemoteException {
        boolean z;
        ArrayList arrayList = new ArrayList();
        LatLngBounds.Builder builder = LatLngBounds.builder();
        if (list != null) {
            LatLng latLng = null;
            z = false;
            for (LatLng latLng2 : list) {
                if (!this.u) {
                    IPoint obtain = IPoint.obtain();
                    this.e.g().a(latLng2.latitude, latLng2.longitude, obtain);
                    arrayList.add(obtain);
                    builder.include(latLng2);
                } else if (latLng != null) {
                    if (Math.abs(latLng2.longitude - latLng.longitude) < 0.01d) {
                        IPoint obtain2 = IPoint.obtain();
                        this.e.g().a(latLng.latitude, latLng.longitude, obtain2);
                        arrayList.add(obtain2);
                        builder.include(latLng);
                        IPoint obtain3 = IPoint.obtain();
                        this.e.g().a(latLng2.latitude, latLng2.longitude, obtain3);
                        arrayList.add(obtain3);
                        builder.include(latLng2);
                    } else {
                        a(latLng, latLng2, arrayList, builder);
                    }
                }
                if (latLng2 != null) {
                    if (!z && latLng2.longitude < -180.0d) {
                        this.aa = true;
                        z = true;
                    }
                    if (!this.aa && latLng2.longitude > 180.0d) {
                        this.aa = true;
                    }
                }
                latLng = latLng2;
            }
        } else {
            z = false;
        }
        this.g = arrayList;
        this.E = 0;
        if (this.a == null) {
            this.a = new Rect();
        }
        fr.a(this.a);
        for (IPoint iPoint : this.g) {
            if (z) {
                iPoint.x += AMapEngineUtils.MAX_P20_WIDTH;
            }
            fr.b(this.a, iPoint.x, iPoint.y);
        }
        this.a.sort();
        this.e.g().setRunLowFrame(false);
    }

    void a(List<IPoint> list, List<IPoint> list2, double d) {
        if (list.size() != 3) {
            return;
        }
        int i = 10;
        int i2 = 0;
        int i3 = 0;
        while (i3 <= i) {
            float f = i3;
            float f2 = f / 10.0f;
            IPoint obtain = IPoint.obtain();
            double d2 = f2;
            Double.isNaN(d2);
            double d3 = 1.0d - d2;
            double d4 = d3 * d3;
            double d5 = list.get(i2).x;
            Double.isNaN(d5);
            double d6 = 2.0f * f2;
            Double.isNaN(d6);
            double d7 = d6 * d3;
            double d8 = list.get(1).x;
            Double.isNaN(d8);
            double d9 = (d5 * d4) + (d8 * d7 * d);
            float f3 = f2 * f2;
            double d10 = list.get(2).x * f3;
            Double.isNaN(d10);
            double d11 = d9 + d10;
            double d12 = list.get(i2).y;
            Double.isNaN(d12);
            double d13 = list.get(1).y;
            Double.isNaN(d13);
            double d14 = (d12 * d4) + (d13 * d7 * d);
            double d15 = list.get(2).y * f3;
            Double.isNaN(d15);
            double d16 = d4 + (d7 * d);
            double d17 = f3;
            Double.isNaN(d17);
            double d18 = d16 + d17;
            obtain.x = (int) (d11 / d18);
            obtain.y = (int) ((d14 + d15) / d18);
            list2.add(obtain);
            i3 = (int) (f + 1.0f);
            i = 10;
            i2 = 0;
        }
    }

    public void a(boolean z) {
        this.A = z;
        this.e.g().setRunLowFrame(false);
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean a() {
        if (this.aa) {
            return true;
        }
        Rectangle geoRectangle = this.e.g().getMapConfig().getGeoRectangle();
        return this.a == null || geoRectangle == null || geoRectangle.isOverlap(this.a);
    }

    @Override // com.amap.api.mapcore.util.ds
    public boolean a(LatLng latLng) {
        float[] fArr = new float[this.P.length];
        System.arraycopy(this.P, 0, fArr, 0, this.P.length);
        if (fArr.length / 3 < 2) {
            return false;
        }
        try {
            ArrayList<FPoint> h = h();
            if (h != null) {
                if (h.size() >= 1) {
                    double mapLenWithWin = this.e.g().c().getMapLenWithWin(((int) this.G) / 4);
                    double mapLenWithWin2 = this.e.g().c().getMapLenWithWin(5);
                    FPoint b = b(latLng);
                    FPoint fPoint = null;
                    int i = 0;
                    while (i < h.size() - 1) {
                        if (i == 0) {
                            fPoint = h.get(i);
                        }
                        i++;
                        FPoint fPoint2 = h.get(i);
                        double a = a(b, fPoint, fPoint2);
                        Double.isNaN(mapLenWithWin2);
                        Double.isNaN(mapLenWithWin);
                        if ((mapLenWithWin2 + mapLenWithWin) - a >= 0.0d) {
                            h.clear();
                            return true;
                        }
                        fPoint = fPoint2;
                    }
                    h.clear();
                    return false;
                }
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public void b(boolean z) {
        if (!z || this.m == null || this.m.size() <= 1) {
            return;
        }
        this.B = 4;
        this.e.g().setRunLowFrame(false);
    }

    public boolean b() throws RemoteException {
        if (this.ab == -1.0f && this.ac == -1.0f && this.ad == -1.0f) {
            b(this.g);
            return true;
        }
        b(this.af);
        return true;
    }

    public boolean b(List<IPoint> list) {
        synchronized (this.r) {
            FPointBounds.Builder builder = new FPointBounds.Builder();
            this.h.clear();
            int i = 0;
            this.z = false;
            this.P = new float[list.size() * 3];
            this.b = this.P.length;
            for (IPoint iPoint : list) {
                FPoint3 fPoint3 = new FPoint3();
                this.e.g().a(iPoint.x, iPoint.y, (FPoint) fPoint3);
                int i2 = i * 3;
                this.P[i2] = fPoint3.x;
                this.P[i2 + 1] = fPoint3.y;
                this.P[i2 + 2] = 0.0f;
                if (this.l != null) {
                    synchronized (this.l) {
                        if (this.l == null || this.l.size() <= i) {
                            if (this.m != null && this.m.size() > i) {
                                if (this.ae <= 0) {
                                    fPoint3.setColorIndex(this.m.get(i).intValue());
                                } else if (this.ae + i < this.m.size()) {
                                    fPoint3.setColorIndex(this.m.get(this.ae + i).intValue());
                                }
                            }
                        } else if (this.ae <= 0) {
                            fPoint3.setColorIndex(this.l.get(i).intValue());
                        } else if (this.ae + i < this.l.size()) {
                            fPoint3.setColorIndex(this.l.get(this.ae + i).intValue());
                        }
                    }
                }
                this.h.add(fPoint3);
                builder.include(fPoint3);
                i++;
            }
            this.T = builder.build();
            if (!this.A) {
                this.p = fr.a(this.P);
            }
            this.E = list.size();
            e();
        }
        return true;
    }

    public void c(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.m = list;
        if (list.size() <= 1) {
            setColor(list.get(0).intValue());
            return;
        }
        this.t = false;
        this.o = f(list);
        this.B = 3;
        this.e.g().setRunLowFrame(false);
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean c() {
        return this.z;
    }

    public void d() {
        this.w = false;
        this.C = 0;
        if (this.R != null) {
            Arrays.fill(this.R, 0);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        try {
            remove();
            if (this.k != null && this.k.size() > 0) {
                for (int i = 0; i < this.k.size(); i++) {
                    am amVar = this.k.get(i);
                    if (amVar != null) {
                        this.e.a(amVar);
                        this.e.g().c(amVar.o());
                    }
                }
                this.k.clear();
            }
            if (this.P != null) {
                this.P = null;
            }
            if (this.p != null) {
                this.p.clear();
                this.p = null;
            }
            if (this.j != null && this.j.size() > 0) {
                Iterator<BitmapDescriptor> it = this.j.iterator();
                while (it.hasNext()) {
                    it.next().recycle();
                }
            }
            synchronized (this) {
                if (this.q != null) {
                    this.q.recycle();
                }
            }
            if (this.m != null) {
                this.m.clear();
                this.m = null;
            }
            if (this.l != null) {
                synchronized (this.l) {
                    this.l.clear();
                    this.l = null;
                }
            }
            if (this.i != null) {
                this.i.clear();
                this.i = null;
            }
            this.U = null;
            if (this.Z != 0) {
                AMapNativePolyline.nativeDestroy(this.Z);
            }
        } catch (Throwable th) {
            ic.c(th, "PolylineDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "PolylineDelegateImp destroy");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public int getColor() throws RemoteException {
        return this.D;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.f == null) {
            this.f = this.e.a("Polyline");
        }
        return this.f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public LatLng getNearestLatLng(LatLng latLng) {
        if (latLng == null || this.i == null || this.i.size() == 0) {
            return null;
        }
        int i = 0;
        float f = 0.0f;
        for (int i2 = 0; i2 < this.i.size(); i2++) {
            try {
                if (i2 == 0) {
                    f = AMapUtils.calculateLineDistance(latLng, this.i.get(i2));
                } else {
                    float calculateLineDistance = AMapUtils.calculateLineDistance(latLng, this.i.get(i2));
                    if (f > calculateLineDistance) {
                        i = i2;
                        f = calculateLineDistance;
                    }
                }
            } catch (Throwable th) {
                ic.c(th, "PolylineDelegateImp", "getNearestLatLng");
                th.printStackTrace();
                return null;
            }
        }
        return this.i.get(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public PolylineOptions getOptions() {
        return this.U;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public List<LatLng> getPoints() throws RemoteException {
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public float getShownRatio() {
        return this.ab;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public float getWidth() throws RemoteException {
        return this.G;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.H;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() throws RemoteException {
        return super.hashCode();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return this.S;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public boolean isDottedLine() {
        return this.v;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public boolean isGeodesic() {
        return this.u;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() throws RemoteException {
        return this.s;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.e.removeOverlay(getId());
        setVisible(false);
        this.e.g().setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
        this.S = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setColor(int i) {
        if (this.B == 0 || this.B == 2) {
            this.D = i;
            this.J = Color.alpha(i) / 255.0f;
            this.K = Color.red(i) / 255.0f;
            this.L = Color.green(i) / 255.0f;
            this.M = Color.blue(i) / 255.0f;
            if (this.t) {
                if (this.v) {
                    this.B = 2;
                } else {
                    this.B = 0;
                }
            }
            this.e.g().setRunLowFrame(false);
        }
        this.U.color(i);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setCustemTextureIndex(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        try {
            synchronized (this.l) {
                this.l.clear();
                this.l.addAll(list);
                this.n = f(list);
                this.y = true;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setCustomTexture(BitmapDescriptor bitmapDescriptor) {
        long nanoTime = System.nanoTime();
        if (nanoTime - this.d < 16) {
            return;
        }
        this.d = nanoTime;
        if (bitmapDescriptor == null) {
            return;
        }
        synchronized (this) {
            if (bitmapDescriptor.equals(this.q)) {
                return;
            }
            this.t = false;
            this.w = false;
            this.B = 1;
            this.q = bitmapDescriptor;
            this.e.g().setRunLowFrame(false);
            if (this.U != null) {
                this.U.setCustomTexture(bitmapDescriptor);
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setCustomTextureList(List<BitmapDescriptor> list) {
        e(list);
        setCustemTextureIndex(this.U.getCustomTextureIndex());
        d();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setDottedLine(boolean z) {
        if (this.B == 2 || this.B == 0) {
            this.v = z;
            if (z && this.t) {
                this.B = 2;
            } else if (!z && this.t) {
                this.B = 0;
            }
            this.e.g().setRunLowFrame(false);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setGeodesic(boolean z) throws RemoteException {
        this.u = z;
        this.e.g().setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setOptions(PolylineOptions polylineOptions) {
        if (polylineOptions == null) {
            return;
        }
        this.U = polylineOptions;
        try {
            setColor(polylineOptions.getColor());
            setGeodesic(polylineOptions.isGeodesic());
            setDottedLine(polylineOptions.isDottedLine());
            a(polylineOptions.getDottedLineType());
            setAboveMaskLayer(polylineOptions.isAboveMaskLayer());
            setVisible(polylineOptions.isVisible());
            setWidth(polylineOptions.getWidth());
            setZIndex(polylineOptions.getZIndex());
            a(polylineOptions.isUseTexture());
            setTransparency(polylineOptions.getTransparency());
            a(polylineOptions.getLineCapType());
            a(polylineOptions.getLineJoinType());
            if (polylineOptions.getColorValues() != null) {
                c(polylineOptions.getColorValues());
                b(polylineOptions.isUseGradient());
            }
            if (polylineOptions.getCustomTexture() != null) {
                setCustomTexture(polylineOptions.getCustomTexture());
                d();
            }
            if (polylineOptions.getCustomTextureList() != null) {
                e(polylineOptions.getCustomTextureList());
                setCustemTextureIndex(polylineOptions.getCustomTextureIndex());
                d();
            }
            setPoints(polylineOptions.getPoints());
            setShownRatio(polylineOptions.getShownRatio());
            setShowRange(polylineOptions.getShownRangeBegin(), polylineOptions.getShownRangeEnd());
        } catch (RemoteException e) {
            ic.c(e, "PolylineDelegateImp", "setOptions");
            e.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setPoints(List<LatLng> list) throws RemoteException {
        try {
            this.i = list;
            synchronized (this.r) {
                a(list);
            }
            this.x = true;
            this.e.g().setRunLowFrame(false);
            this.U.setPoints(list);
        } catch (Throwable th) {
            ic.c(th, "PolylineDelegateImp", "setPoints");
            this.g.clear();
            th.printStackTrace();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0059 A[Catch: all -> 0x01a2, TryCatch #0 {, blocks: (B:4:0x000d, B:6:0x0016, B:11:0x001c, B:12:0x0028, B:16:0x002f, B:19:0x0036, B:21:0x003b, B:25:0x0054, B:27:0x0059, B:29:0x0061, B:30:0x0068, B:32:0x006a, B:33:0x0075, B:35:0x0092, B:39:0x017d, B:41:0x00a6, B:43:0x00aa, B:45:0x00ca, B:47:0x00e3, B:53:0x00ec, B:55:0x0108, B:57:0x0121, B:49:0x0129, B:66:0x0137, B:69:0x0140, B:71:0x015c, B:73:0x0175, B:59:0x0188, B:60:0x018a, B:76:0x0046, B:78:0x004b, B:80:0x0051), top: B:3:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0092 A[Catch: all -> 0x01a2, TryCatch #0 {, blocks: (B:4:0x000d, B:6:0x0016, B:11:0x001c, B:12:0x0028, B:16:0x002f, B:19:0x0036, B:21:0x003b, B:25:0x0054, B:27:0x0059, B:29:0x0061, B:30:0x0068, B:32:0x006a, B:33:0x0075, B:35:0x0092, B:39:0x017d, B:41:0x00a6, B:43:0x00aa, B:45:0x00ca, B:47:0x00e3, B:53:0x00ec, B:55:0x0108, B:57:0x0121, B:49:0x0129, B:66:0x0137, B:69:0x0140, B:71:0x015c, B:73:0x0175, B:59:0x0188, B:60:0x018a, B:76:0x0046, B:78:0x004b, B:80:0x0051), top: B:3:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0188 A[Catch: all -> 0x01a2, TryCatch #0 {, blocks: (B:4:0x000d, B:6:0x0016, B:11:0x001c, B:12:0x0028, B:16:0x002f, B:19:0x0036, B:21:0x003b, B:25:0x0054, B:27:0x0059, B:29:0x0061, B:30:0x0068, B:32:0x006a, B:33:0x0075, B:35:0x0092, B:39:0x017d, B:41:0x00a6, B:43:0x00aa, B:45:0x00ca, B:47:0x00e3, B:53:0x00ec, B:55:0x0108, B:57:0x0121, B:49:0x0129, B:66:0x0137, B:69:0x0140, B:71:0x015c, B:73:0x0175, B:59:0x0188, B:60:0x018a, B:76:0x0046, B:78:0x004b, B:80:0x0051), top: B:3:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0186 A[EDGE_INSN: B:75:0x0186->B:58:0x0186 BREAK  A[LOOP:0: B:34:0x0090->B:39:0x017d], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0046 A[Catch: all -> 0x01a2, TryCatch #0 {, blocks: (B:4:0x000d, B:6:0x0016, B:11:0x001c, B:12:0x0028, B:16:0x002f, B:19:0x0036, B:21:0x003b, B:25:0x0054, B:27:0x0059, B:29:0x0061, B:30:0x0068, B:32:0x006a, B:33:0x0075, B:35:0x0092, B:39:0x017d, B:41:0x00a6, B:43:0x00aa, B:45:0x00ca, B:47:0x00e3, B:53:0x00ec, B:55:0x0108, B:57:0x0121, B:49:0x0129, B:66:0x0137, B:69:0x0140, B:71:0x015c, B:73:0x0175, B:59:0x0188, B:60:0x018a, B:76:0x0046, B:78:0x004b, B:80:0x0051), top: B:3:0x000d }] */
    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setShowRange(float r20, float r21) {
        /*
            Method dump skipped, instructions count: 421
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.ea.setShowRange(float, float):void");
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setShownRatio(float f) {
        this.ab = f;
        synchronized (this.r) {
            int size = this.g.size();
            if (size < 2) {
                return;
            }
            float f2 = this.ab;
            if (f2 < 0.0f) {
                f2 = 0.0f;
            } else if (f2 >= size) {
                f2 = size - 1;
            }
            if (this.u) {
                if (this.i.size() < 2) {
                    return;
                } else {
                    f2 = (f2 / (r5 - 1)) * (size - 1);
                }
            }
            this.af.clear();
            int floor = (int) Math.floor(f2);
            IPoint iPoint = null;
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                }
                IPoint iPoint2 = this.g.get(i);
                if (i > floor) {
                    float f3 = f2 - floor;
                    if (f != 0.0f && iPoint != null) {
                        IPoint iPoint3 = new IPoint();
                        iPoint3.x = (int) (iPoint.x + ((iPoint2.x - iPoint.x) * f3));
                        iPoint3.y = (int) (iPoint.y + ((iPoint2.y - iPoint.y) * f3));
                        this.af.add(iPoint3);
                    }
                } else {
                    this.af.add(iPoint2);
                    i++;
                    iPoint = iPoint2;
                }
            }
            this.x = true;
            this.e.g().setRunLowFrame(false);
            this.U.setShownRatio(f);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setTransparency(float f) {
        this.N = (float) Math.min(1.0d, Math.max(0.0d, f));
        this.e.g().setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.s = z;
        this.e.g().setRunLowFrame(false);
        if (this.U != null) {
            this.U.visible(z);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IPolyline
    public void setWidth(float f) throws RemoteException {
        this.G = f;
        this.e.g().setRunLowFrame(false);
        this.U.width(f);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.H = f;
        this.e.e();
        this.e.g().setRunLowFrame(false);
        if (this.U != null) {
            this.U.zIndex(f);
        }
    }
}
