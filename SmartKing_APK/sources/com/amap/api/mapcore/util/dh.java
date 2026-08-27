package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.mapcore.util.ef;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/* compiled from: GroundOverlayDelegateImp.java */
/* loaded from: classes.dex */
public class dh implements dl {
    float[] a;
    ef.c b;
    private ad c;
    private BitmapDescriptor d;
    private LatLng e;
    private float f;
    private float g;
    private LatLngBounds h;
    private float i;
    private float j;
    private boolean k;
    private float l;
    private float m;
    private float n;
    private float o;
    private String p;
    private FloatBuffer q;
    private FloatBuffer r;
    private int s;
    private boolean t;
    private boolean u;
    private List<am> v;
    private ab w;

    private dh(ad adVar) {
        this.k = true;
        this.l = 0.0f;
        this.m = 1.0f;
        this.n = 0.5f;
        this.o = 0.5f;
        this.q = null;
        this.t = false;
        this.u = false;
        this.v = new ArrayList();
        this.a = null;
        this.c = adVar;
        try {
            this.p = getId();
        } catch (RemoteException e) {
            ic.c(e, "GroundOverlayDelegateImp", "create");
            e.printStackTrace();
        }
    }

    public dh(ad adVar, ab abVar) {
        this(adVar);
        this.w = abVar;
    }

    private int a(boolean z, BitmapDescriptor bitmapDescriptor) {
        am amVar;
        h();
        if (z) {
            amVar = this.w.a(bitmapDescriptor);
            if (amVar != null) {
                int k = amVar.k();
                a(amVar);
                return k;
            }
        } else {
            amVar = null;
        }
        int i = 0;
        if (amVar == null) {
            amVar = new am(bitmapDescriptor, 0);
        }
        Bitmap bitmap = bitmapDescriptor.getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            i = i();
            amVar.a(i);
            if (z) {
                this.c.a(amVar);
            }
            a(amVar);
            fr.b(i, bitmap, true);
        }
        return i;
    }

    private void a(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        if (floatBuffer == null || floatBuffer2 == null) {
            return;
        }
        if (this.b == null || this.b.c()) {
            j();
        }
        this.b.a();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glBlendColor(this.m * 1.0f, this.m * 1.0f, this.m * 1.0f, this.m);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glEnableVertexAttribArray(this.b.b);
        GLES20.glVertexAttribPointer(this.b.b, 4, 5126, false, 16, (Buffer) floatBuffer);
        GLES20.glEnableVertexAttribArray(this.b.c);
        GLES20.glVertexAttribPointer(this.b.c, 2, 5126, false, 8, (Buffer) floatBuffer2);
        GLES20.glUniform4f(this.b.g, this.c.getMapConfig().getSX() / 10000, this.c.getMapConfig().getSY() / 10000, this.c.getMapConfig().getSX() % 10000, this.c.getMapConfig().getSY() % 10000);
        GLES20.glUniform4f(this.b.h, this.m * 1.0f, this.m * 1.0f, this.m * 1.0f, this.m);
        GLES20.glUniformMatrix4fv(this.b.a, 1, false, this.c.x(), 0);
        GLES20.glDrawArrays(6, 0, 4);
        GLES20.glBindTexture(3553, 0);
        GLES20.glDisableVertexAttribArray(this.b.b);
        GLES20.glDisableVertexAttribArray(this.b.c);
        GLES20.glDisable(3042);
        GLES20.glUseProgram(0);
    }

    private void a(am amVar) {
        if (amVar != null) {
            this.v.add(amVar);
            amVar.l();
        }
    }

    private void a(DPoint dPoint, double d, double d2, double d3, double d4, IPoint iPoint) {
        double d5 = this.n;
        Double.isNaN(d5);
        double d6 = d - (d3 * d5);
        double d7 = 1.0f - this.o;
        Double.isNaN(d7);
        double d8 = (d4 * d7) - d2;
        double d9 = -this.i;
        Double.isNaN(d9);
        double d10 = d9 * 0.01745329251994329d;
        iPoint.x = (int) (dPoint.x + (Math.cos(d10) * d6) + (Math.sin(d10) * d8));
        iPoint.y = (int) (dPoint.y + ((d8 * Math.cos(d10)) - (d6 * Math.sin(d10))));
    }

    private void d() {
        if (this.e == null) {
            return;
        }
        double d = this.f;
        double cos = Math.cos(this.e.latitude * 0.01745329251994329d) * 6371000.79d * 0.01745329251994329d;
        Double.isNaN(d);
        double d2 = d / cos;
        double d3 = this.g;
        Double.isNaN(d3);
        double d4 = d3 / 111194.94043265979d;
        try {
            double d5 = this.e.latitude;
            double d6 = 1.0f - this.o;
            Double.isNaN(d6);
            double d7 = d5 - (d6 * d4);
            double d8 = this.e.longitude;
            double d9 = this.n;
            Double.isNaN(d9);
            LatLng latLng = new LatLng(d7, d8 - (d9 * d2));
            double d10 = this.e.latitude;
            double d11 = this.o;
            Double.isNaN(d11);
            double d12 = d10 + (d11 * d4);
            double d13 = this.e.longitude;
            double d14 = 1.0f - this.n;
            Double.isNaN(d14);
            this.h = new LatLngBounds(latLng, new LatLng(d12, d13 + (d14 * d2)));
        } catch (Throwable th) {
            th.printStackTrace();
        }
        f();
    }

    private synchronized void e() {
        if (this.h == null) {
            return;
        }
        LatLng latLng = this.h.southwest;
        LatLng latLng2 = this.h.northeast;
        double d = latLng.latitude;
        double d2 = 1.0f - this.o;
        double d3 = latLng2.latitude - latLng.latitude;
        Double.isNaN(d2);
        double d4 = d + (d2 * d3);
        double d5 = latLng.longitude;
        double d6 = this.n;
        double d7 = latLng2.longitude - latLng.longitude;
        Double.isNaN(d6);
        this.e = new LatLng(d4, d5 + (d6 * d7));
        this.f = (float) (Math.cos(this.e.latitude * 0.01745329251994329d) * 6371000.79d * (latLng2.longitude - latLng.longitude) * 0.01745329251994329d);
        this.g = (float) ((latLng2.latitude - latLng.latitude) * 6371000.79d * 0.01745329251994329d);
        f();
    }

    private synchronized void f() {
        if (this.h == null) {
            return;
        }
        this.a = new float[16];
        IPoint obtain = IPoint.obtain();
        IPoint obtain2 = IPoint.obtain();
        IPoint obtain3 = IPoint.obtain();
        IPoint obtain4 = IPoint.obtain();
        GLMapState.lonlat2Geo(this.h.southwest.longitude, this.h.southwest.latitude, obtain);
        GLMapState.lonlat2Geo(this.h.northeast.longitude, this.h.southwest.latitude, obtain2);
        GLMapState.lonlat2Geo(this.h.northeast.longitude, this.h.northeast.latitude, obtain3);
        GLMapState.lonlat2Geo(this.h.southwest.longitude, this.h.northeast.latitude, obtain4);
        if (this.i != 0.0f) {
            double d = obtain2.x - obtain.x;
            double d2 = obtain2.y - obtain3.y;
            DPoint obtain5 = DPoint.obtain();
            double d3 = obtain.x;
            double d4 = this.n;
            Double.isNaN(d);
            Double.isNaN(d4);
            Double.isNaN(d3);
            obtain5.x = d3 + (d4 * d);
            double d5 = obtain.y;
            double d6 = 1.0f - this.o;
            Double.isNaN(d2);
            Double.isNaN(d6);
            Double.isNaN(d5);
            obtain5.y = d5 - (d6 * d2);
            a(obtain5, 0.0d, 0.0d, d, d2, obtain);
            a(obtain5, d, 0.0d, d, d2, obtain2);
            a(obtain5, d, d2, d, d2, obtain3);
            a(obtain5, 0.0d, d2, d, d2, obtain4);
            obtain5.recycle();
        }
        this.a[0] = obtain.x / 10000;
        this.a[1] = obtain.y / 10000;
        this.a[2] = obtain.x % 10000;
        this.a[3] = obtain.y % 10000;
        this.a[4] = obtain2.x / 10000;
        this.a[5] = obtain2.y / 10000;
        this.a[6] = obtain2.x % 10000;
        this.a[7] = obtain2.y % 10000;
        this.a[8] = obtain3.x / 10000;
        this.a[9] = obtain3.y / 10000;
        this.a[10] = obtain3.x % 10000;
        this.a[11] = obtain3.y % 10000;
        this.a[12] = obtain4.x / 10000;
        this.a[13] = obtain4.y / 10000;
        this.a[14] = obtain4.x % 10000;
        this.a[15] = obtain4.y % 10000;
        if (this.q == null) {
            this.q = fr.a(this.a);
        } else {
            this.q = fr.a(this.a, this.q);
        }
        obtain4.recycle();
        obtain.recycle();
        obtain2.recycle();
        obtain3.recycle();
    }

    private void g() {
        if (this.d == null && (this.d == null || this.d.getBitmap() == null)) {
            return;
        }
        int width = this.d.getWidth();
        float width2 = width / this.d.getBitmap().getWidth();
        float height = this.d.getHeight() / this.d.getBitmap().getHeight();
        this.r = fr.a(new float[]{0.0f, height, width2, height, width2, 0.0f, 0.0f, 0.0f});
    }

    private void h() {
        if (this.v != null) {
            for (am amVar : this.v) {
                if (amVar != null && this.w != null) {
                    this.w.a(amVar);
                }
            }
            this.v.clear();
        }
    }

    private int i() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    private void j() {
        if (this.c != null) {
            this.b = (ef.c) this.c.u(2);
        }
    }

    public void a(float f, float f2) throws RemoteException {
        this.n = f;
        this.o = f2;
        this.c.setRunLowFrame(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0019 A[Catch: all -> 0x0053, DONT_GENERATE, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0007, B:7:0x000b, B:9:0x000f, B:14:0x0019, B:17:0x001b), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x001b A[Catch: all -> 0x0053, DONT_GENERATE, TRY_LEAVE, TryCatch #1 {, blocks: (B:3:0x0001, B:5:0x0007, B:7:0x000b, B:9:0x000f, B:14:0x0019, B:17:0x001b), top: B:2:0x0001 }] */
    @Override // com.amap.api.mapcore.util.Cdo
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(com.autonavi.amap.mapcore.MapConfig r4) throws android.os.RemoteException {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r4 = r3.k     // Catch: java.lang.Throwable -> L53
            r0 = 0
            r1 = 1
            if (r4 == 0) goto L16
            com.amap.api.maps.model.LatLng r4 = r3.e     // Catch: java.lang.Throwable -> L53
            if (r4 != 0) goto Lf
            com.amap.api.maps.model.LatLngBounds r4 = r3.h     // Catch: java.lang.Throwable -> L53
            if (r4 == 0) goto L16
        Lf:
            com.amap.api.maps.model.BitmapDescriptor r4 = r3.d     // Catch: java.lang.Throwable -> L53
            if (r4 != 0) goto L14
            goto L16
        L14:
            r4 = 0
            goto L17
        L16:
            r4 = 1
        L17:
            if (r4 == 0) goto L1b
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L53
            return
        L1b:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L53
            r3.b()
            boolean r4 = r3.t
            if (r4 != 0) goto L34
            int r4 = android.os.Build.VERSION.SDK_INT
            r2 = 12
            if (r4 < r2) goto L2a
            r0 = 1
        L2a:
            com.amap.api.maps.model.BitmapDescriptor r4 = r3.d
            int r4 = r3.a(r0, r4)
            r3.s = r4
            r3.t = r1
        L34:
            float r4 = r3.f
            r0 = 0
            int r4 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r4 != 0) goto L42
            float r4 = r3.g
            int r4 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r4 != 0) goto L42
            return
        L42:
            monitor-enter(r3)
            int r4 = r3.s     // Catch: java.lang.Throwable -> L50
            java.nio.FloatBuffer r0 = r3.q     // Catch: java.lang.Throwable -> L50
            java.nio.FloatBuffer r2 = r3.r     // Catch: java.lang.Throwable -> L50
            r3.a(r4, r0, r2)     // Catch: java.lang.Throwable -> L50
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L50
            r3.u = r1
            return
        L50:
            r4 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L50
            throw r4
        L53:
            r4 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L53
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.dh.a(com.autonavi.amap.mapcore.MapConfig):void");
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean a() {
        return true;
    }

    public boolean b() throws RemoteException {
        synchronized (this) {
            if (this.a != null) {
                return false;
            }
            this.u = false;
            if (this.e == null) {
                e();
                return true;
            }
            if (this.h == null) {
                d();
                return true;
            }
            f();
            return true;
        }
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean c() {
        return this.u;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        Bitmap bitmap;
        try {
            remove();
            if (this.v != null && this.v.size() > 0) {
                for (int i = 0; i < this.v.size(); i++) {
                    am amVar = this.v.get(i);
                    if (amVar != null) {
                        if (this.w != null) {
                            this.w.a(amVar);
                        }
                        if (this.c != null) {
                            this.c.c(amVar.o());
                        }
                    }
                }
                this.v.clear();
            }
            if (this.d != null && (bitmap = this.d.getBitmap()) != null) {
                bitmap.recycle();
                this.d = null;
            }
            if (this.r != null) {
                this.r.clear();
                this.r = null;
            }
            synchronized (this) {
                if (this.q != null) {
                    this.q.clear();
                    this.q = null;
                }
                this.h = null;
            }
            this.e = null;
        } catch (Throwable th) {
            ic.c(th, "GroundOverlayDelegateImp", "destroy");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public float getBearing() throws RemoteException {
        return this.i;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public LatLngBounds getBounds() throws RemoteException {
        return this.h;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public float getHeight() throws RemoteException {
        return this.g;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() throws RemoteException {
        if (this.p == null) {
            this.p = this.c.d("GroundOverlay");
        }
        return this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public LatLng getPosition() throws RemoteException {
        return this.e;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public float getTransparency() throws RemoteException {
        return this.l;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public float getWidth() throws RemoteException {
        return this.f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() throws RemoteException {
        return this.j;
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
        return this.k;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
        this.c.a(getId());
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setBearing(float f) throws RemoteException {
        float f2 = ((f % 360.0f) + 360.0f) % 360.0f;
        if (Math.abs(this.i - f2) > 1.0E-7d) {
            this.i = f2;
            f();
        }
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setDimensions(float f) throws RemoteException {
        if (f <= 0.0f) {
            Log.w("GroundOverlayDelegateImp", "Width must be non-negative");
        }
        if (!this.t || this.f == f) {
            this.f = f;
            this.g = f;
        } else {
            this.f = f;
            this.g = f;
            d();
        }
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setDimensions(float f, float f2) throws RemoteException {
        if (f <= 0.0f || f2 <= 0.0f) {
            Log.w("GroundOverlayDelegateImp", "Width and Height must be non-negative");
        }
        if (!this.t || this.f == f || this.g == f2) {
            this.f = f;
            this.g = f2;
        } else {
            this.f = f;
            this.g = f2;
            d();
        }
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setImage(BitmapDescriptor bitmapDescriptor) throws RemoteException {
        if (bitmapDescriptor == null || bitmapDescriptor.getBitmap() == null || bitmapDescriptor.getBitmap().isRecycled()) {
            return;
        }
        this.d = bitmapDescriptor;
        g();
        if (this.t) {
            this.t = false;
        }
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setPosition(LatLng latLng) throws RemoteException {
        this.e = latLng;
        d();
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setPositionFromBounds(LatLngBounds latLngBounds) throws RemoteException {
        if (latLngBounds == null) {
            return;
        }
        this.h = latLngBounds;
        e();
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IGroundOverlay
    public void setTransparency(float f) throws RemoteException {
        this.l = (float) Math.min(1.0d, Math.max(0.0d, f));
        this.m = 1.0f - f;
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) throws RemoteException {
        this.k = z;
        this.c.setRunLowFrame(false);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) throws RemoteException {
        this.j = f;
        this.c.f();
        this.c.setRunLowFrame(false);
    }
}
