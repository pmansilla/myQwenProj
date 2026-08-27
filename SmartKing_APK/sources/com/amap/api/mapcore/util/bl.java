package com.amap.api.mapcore.util;

import android.graphics.Rect;
import android.opengl.Matrix;
import android.os.RemoteException;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.MapProjection;
import com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* compiled from: MultiPointOverlayDelegate.java */
/* loaded from: classes.dex */
public class bl implements IMultiPointOverlay {
    private static int E;
    private String B;
    List<MultiPointItem> h;
    IPoint m;
    bm n;
    BitmapDescriptor a = BitmapDescriptorFactory.defaultMarker();
    BitmapDescriptor b = null;
    float c = 0.0f;
    float d = 0.0f;
    float e = 0.0f;
    float f = 0.5f;
    float g = 0.5f;
    bn i = null;
    bk j = null;
    bk k = new bk(0, 1, 0, 1);
    List<MultiPointItem> l = new ArrayList();
    private float[] C = {-0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f};
    private boolean D = true;
    List<bj> o = new ArrayList();
    private ExecutorService F = null;
    private List<String> G = new ArrayList();
    private float[] H = new float[bj.a * 3];
    float[] p = new float[16];
    float[] q = new float[4];
    float[] r = new float[4];
    Rect s = new Rect();
    bk t = null;
    bk u = null;
    int v = 0;
    int w = 0;
    float[] x = new float[12];
    String y = "precision highp float;\nattribute vec3 aVertex;//顶点数组,三维坐标\nuniform mat4 aMVPMatrix;//mvp矩阵\nvoid main(){\n  gl_Position = aMVPMatrix * vec4(aVertex, 1.0);\n}";
    String z = "//有颜色 没有纹理\nprecision highp float;\nvoid main(){\n  gl_FragColor = vec4(0,0,1,1.0);\n}";
    int A = -1;

    public bl(MultiPointOverlayOptions multiPointOverlayOptions, bm bmVar) {
        this.n = bmVar;
        a(multiPointOverlayOptions);
        bj bjVar = new bj(a(), this);
        bjVar.a(bmVar.a());
        bjVar.a(this.b);
        this.o.add(bjVar);
    }

    private static String a(String str) {
        E++;
        return str + E;
    }

    private void a(float f, float f2, float f3, float f4) {
        if (this.k == null) {
            this.k = new bk(0, 1, 0, 1);
        }
        this.s.set(0, 0, 0, 0);
        IPoint iPoint = new IPoint();
        float f5 = this.f;
        float f6 = this.g;
        Matrix.setIdentityM(this.p, 0);
        Matrix.rotateM(this.p, 0, -f3, 0.0f, 0.0f, 1.0f);
        this.r[0] = 0.0f;
        this.r[1] = 0.0f;
        this.r[2] = 0.0f;
        this.r[3] = 0.0f;
        float f7 = (-f) * f5;
        this.q[0] = f7;
        float f8 = f2 * f6;
        this.q[1] = f8;
        this.q[2] = 0.0f;
        this.q[3] = 1.0f;
        Matrix.multiplyMV(this.r, 0, this.p, 0, this.q, 0);
        this.s.set((int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]), (int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]));
        float f9 = f * (1.0f - f5);
        this.q[0] = f9;
        this.q[1] = f8;
        this.q[2] = 0.0f;
        this.q[3] = 1.0f;
        Matrix.multiplyMV(this.r, 0, this.p, 0, this.q, 0);
        this.s.union((int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]));
        this.q[0] = f9;
        float f10 = (-f2) * (1.0f - f6);
        this.q[1] = f10;
        this.q[2] = 0.0f;
        this.q[3] = 1.0f;
        Matrix.multiplyMV(this.r, 0, this.p, 0, this.q, 0);
        this.s.union((int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]));
        this.q[0] = f7;
        this.q[1] = f10;
        this.q[2] = 0.0f;
        this.q[3] = 1.0f;
        Matrix.multiplyMV(this.r, 0, this.p, 0, this.q, 0);
        this.s.union((int) (iPoint.x + this.r[0]), (int) (iPoint.y - this.r[1]));
        this.k.a(this.s.left, this.s.right, this.s.top, this.s.bottom);
    }

    private void a(MultiPointOverlayOptions multiPointOverlayOptions) {
        if (multiPointOverlayOptions != null) {
            if (multiPointOverlayOptions.getIcon() == null || multiPointOverlayOptions.getIcon().getBitmap() == null || multiPointOverlayOptions.getIcon().getBitmap().isRecycled()) {
                this.b = this.a;
            } else {
                this.b = multiPointOverlayOptions.getIcon();
            }
            this.f = multiPointOverlayOptions.getAnchorU();
            this.g = multiPointOverlayOptions.getAnchorV();
        }
    }

    private void a(MapConfig mapConfig) {
        if (mapConfig != null) {
            Rect rect = mapConfig.getGeoRectangle().getRect();
            if (this.j == null) {
                this.j = new bk(rect.left, rect.right, rect.top, rect.bottom);
            } else {
                this.j.a(rect.left, rect.right, rect.top, rect.bottom);
            }
        }
    }

    private float[] a() {
        if (this.C == null) {
            return null;
        }
        float[] fArr = (float[]) this.C.clone();
        float f = this.f - 0.5f;
        float f2 = this.g - 0.5f;
        fArr[0] = fArr[0] + f;
        fArr[1] = fArr[1] - f2;
        fArr[6] = fArr[6] + f;
        fArr[7] = fArr[7] - f2;
        fArr[12] = fArr[12] + f;
        fArr[13] = fArr[13] - f2;
        fArr[18] = fArr[18] + f;
        fArr[19] = fArr[19] - f2;
        return fArr;
    }

    private bk b() {
        if (this.h == null || this.h.size() == 0) {
            return null;
        }
        Iterator<MultiPointItem> it = this.h.iterator();
        MultiPointItem next = it.next();
        int i = next.getIPoint().x;
        int i2 = next.getIPoint().x;
        int i3 = next.getIPoint().y;
        int i4 = next.getIPoint().y;
        while (it.hasNext()) {
            MultiPointItem next2 = it.next();
            int i5 = next2.getIPoint().x;
            int i6 = next2.getIPoint().y;
            if (i5 < i) {
                i = i5;
            }
            if (i5 > i2) {
                i2 = i5;
            }
            if (i6 < i3) {
                i3 = i6;
            }
            if (i6 > i4) {
                i4 = i6;
            }
        }
        return new bk(i, i2, i3, i4);
    }

    private void c() {
        if (this.F == null) {
            this.F = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue(), new fe("MultiPointOverlay"), new ThreadPoolExecutor.AbortPolicy());
        }
        for (final bj bjVar : this.o) {
            if (bjVar != null && !bjVar.b()) {
                final String str = bjVar.hashCode() + "";
                if (!this.G.contains(str)) {
                    this.G.add(str);
                    this.F.execute(new Runnable() { // from class: com.amap.api.mapcore.util.bl.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (bjVar.b()) {
                                return;
                            }
                            bjVar.a();
                            bl.this.G.remove(str);
                        }
                    });
                }
            }
        }
    }

    private void d() {
        if (this.n != null) {
            this.n.d();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void addItem(MultiPointItem multiPointItem) {
        d();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void addItems(List<MultiPointItem> list) {
        bk b;
        if (list != null) {
            try {
                if (list.size() == 0) {
                    return;
                }
                synchronized (this) {
                    if (this.h == null) {
                        this.h = new ArrayList();
                    }
                    this.h.clear();
                    this.h.addAll(list);
                    int size = this.h.size();
                    for (int i = 0; i < size; i++) {
                        if (this.h == null) {
                            return;
                        }
                        MultiPointItem multiPointItem = this.h.get(i);
                        if (multiPointItem != null && multiPointItem.getLatLng() != null && multiPointItem.getIPoint() == null) {
                            IPoint iPoint = new IPoint();
                            MapProjection.lonlat2Geo(multiPointItem.getLatLng().longitude, multiPointItem.getLatLng().latitude, iPoint);
                            multiPointItem.setIPoint(iPoint);
                        }
                    }
                    if (this.i == null && (b = b()) != null) {
                        this.i = new bn(b);
                    }
                    if (this.h != null) {
                        int size2 = this.h.size();
                        for (int i2 = 0; i2 < size2; i2++) {
                            MultiPointItem multiPointItem2 = this.h.get(i2);
                            if (multiPointItem2 != null && multiPointItem2.getIPoint() != null && this.i != null) {
                                this.i.a(multiPointItem2);
                            }
                        }
                    }
                    d();
                }
            } catch (Throwable th) {
                ic.c(th, "MultiPointOverlayDelegate", "addItems");
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void destroy(boolean z) {
        remove(z);
        if (this.b != null) {
            this.b.recycle();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void draw(MapConfig mapConfig, float[] fArr, float[] fArr2) {
        int i;
        try {
            if (this.D) {
                c();
                if (this.o.size() < 1 || this.i == null || mapConfig == null) {
                    return;
                }
                float sr = mapConfig.getSR();
                float sc = mapConfig.getSC();
                if (mapConfig.getChangeRatio() != 1.0d || this.l.size() == 0) {
                    synchronized (this.l) {
                        a(mapConfig);
                        this.l.clear();
                        this.c = mapConfig.getMapPerPixelUnitLength();
                        this.d = this.c * this.b.getWidth();
                        this.e = this.c * this.b.getHeight();
                        double d = this.d * this.e * 16.0f;
                        a(this.d, this.e, sr, sc);
                        this.i.a(this.j, this.l, d);
                    }
                }
                if (this.m == null) {
                    this.m = new IPoint();
                }
                if (this.m != null && mapConfig != null) {
                    this.m.x = mapConfig.getSX();
                    this.m.y = mapConfig.getSY();
                }
                bj bjVar = this.o.get(0);
                synchronized (this.l) {
                    Iterator<MultiPointItem> it = this.l.iterator();
                    loop0: while (true) {
                        i = 0;
                        while (it.hasNext()) {
                            IPoint iPoint = it.next().getIPoint();
                            if (iPoint != null) {
                                int i2 = iPoint.x - this.m.x;
                                int i3 = iPoint.y - this.m.y;
                                if (bjVar != null && bjVar.b()) {
                                    if (!bjVar.d() && this.n != null) {
                                        bjVar.a(this.n.a());
                                    }
                                    int i4 = i * 3;
                                    this.H[i4 + 0] = i2;
                                    this.H[i4 + 1] = i3;
                                    this.H[i4 + 2] = 0.0f;
                                    i++;
                                    if (i >= bj.a) {
                                        break;
                                    }
                                }
                            }
                        }
                        bjVar.a(fArr, fArr2, this.H, this.d, this.e, sr, sc, i);
                    }
                }
                if (i > 0) {
                    bjVar.a(fArr, fArr2, this.H, this.d, this.e, sr, sc, i);
                }
            }
        } catch (Throwable th) {
            ic.c(th, "MultiPointOverlayDelegate", "draw");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public String getId() throws RemoteException {
        if (this.B == null) {
            this.B = a("MultiPointOverlay");
        }
        return this.B;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public MultiPointItem onClick(IPoint iPoint) {
        if (!this.D || this.i == null) {
            return null;
        }
        if (this.t == null) {
            this.t = new bk(0, 1, 0, 1);
        }
        int i = (int) (this.c * 8.0f);
        this.t.a(iPoint.x - i, iPoint.x + i, iPoint.y - i, iPoint.y + i);
        synchronized (this.l) {
            for (int size = this.l.size() - 1; size >= 0; size--) {
                MultiPointItem multiPointItem = this.l.get(size);
                IPoint iPoint2 = multiPointItem.getIPoint();
                if (iPoint2 != null) {
                    if (this.k == null) {
                        return null;
                    }
                    if (this.u == null) {
                        this.u = new bk(0, 1, 0, 1);
                    }
                    this.u.a(iPoint2.x + this.k.a, iPoint2.x + this.k.c, iPoint2.y + this.k.b, iPoint2.y + this.k.d);
                    if (this.u.a(this.t)) {
                        return multiPointItem;
                    }
                }
            }
            return null;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void remove(boolean z) {
        this.D = false;
        this.v = 0;
        this.w = 0;
        if (this.a != null) {
            this.a.recycle();
        }
        synchronized (this) {
            if (this.h != null) {
                this.h.clear();
                this.h = null;
            }
        }
        if (this.i != null) {
            this.i.a();
            this.i = null;
        }
        if (this.l != null) {
            this.l.clear();
        }
        if (this.F != null) {
            this.F.shutdownNow();
            this.F = null;
        }
        if (this.G != null) {
            this.G.clear();
        }
        if (this.o != null) {
            for (bj bjVar : this.o) {
                if (bjVar != null) {
                    bjVar.c();
                }
            }
            this.o.clear();
        }
        if (z && this.n != null) {
            this.n.a(this);
            this.n.d();
        }
        this.n = null;
        this.C = null;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void setAnchor(float f, float f2) {
        this.f = f;
        this.g = f2;
        d();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMultiPointOverlay
    public void setVisible(boolean z) {
        if (this.D != z) {
            d();
        }
        this.D = z;
    }
}
