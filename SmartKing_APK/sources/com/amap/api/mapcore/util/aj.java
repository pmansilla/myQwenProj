package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import com.amap.api.mapcore.util.ef;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.interfaces.IMarker;
import com.tencent.bugly.BuglyStrategy;
import java.io.Serializable;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: MapOverlayImageView.java */
/* loaded from: classes.dex */
public final class aj {
    ad a;
    public ef.d c;
    private IPoint k;
    private v l;
    private dm m;
    private FloatBuffer p;
    private Handler r;
    private List<dp> f = new ArrayList(500);
    private List<am> g = new ArrayList();
    private List<dp> h = new ArrayList();
    private a i = new a();
    private boolean j = true;
    private int[] s = new int[1];
    float[] b = new float[180000];
    int d = 0;
    int e = 0;
    private Runnable t = new Runnable() { // from class: com.amap.api.mapcore.util.aj.2
        @Override // java.lang.Runnable
        public void run() {
            synchronized (aj.this.f) {
                aj.this.j();
            }
        }
    };
    private fx n = new fx(512, 1024);
    private ff o = new ff();
    private HandlerThread q = new HandlerThread("AMapZindexSortThread");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MapOverlayImageView.java */
    /* loaded from: classes.dex */
    public static class a implements Serializable, Comparator<Object> {
        a() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            dp dpVar = (dp) obj;
            dp dpVar2 = (dp) obj2;
            if (dpVar == null || dpVar2 == null) {
                return 0;
            }
            try {
                return Float.compare(dpVar.getZIndex(), dpVar2.getZIndex());
            } catch (Throwable th) {
                ic.c(th, "MapOverlayImageView", "compare");
                th.printStackTrace();
                return 0;
            }
        }
    }

    public aj(Context context, ad adVar) {
        this.a = adVar;
        this.q.start();
        this.r = new Handler(this.q.getLooper());
    }

    private void a(int i) {
        if (i > 5000) {
            i = 5000;
        }
        if (this.d == 0) {
            int[] iArr = new int[2];
            GLES20.glGenBuffers(2, iArr, 0);
            this.d = iArr[0];
            this.e = iArr[1];
            ShortBuffer b = this.o.b(BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH);
            short[] sArr = new short[BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH];
            for (int i2 = 0; i2 < 5000; i2++) {
                int i3 = i2 * 6;
                int i4 = i2 * 4;
                short s = (short) (i4 + 0);
                sArr[i3 + 0] = s;
                sArr[i3 + 1] = (short) (i4 + 1);
                short s2 = (short) (i4 + 2);
                sArr[i3 + 2] = s2;
                sArr[i3 + 3] = s;
                sArr[i3 + 4] = s2;
                sArr[i3 + 5] = (short) (i4 + 3);
            }
            b.put(sArr);
            b.flip();
            GLES20.glBindBuffer(34963, this.e);
            GLES20.glBufferData(34963, DateUtils.MILLIS_IN_MINUTE, b, 35044);
        }
        GLES20.glBindBuffer(34962, this.d);
        GLES20.glBufferData(34962, i * 36 * 4, this.p, 35044);
    }

    private void a(int i, int i2, int i3, int i4) {
        if (i2 == 0 || i == 0) {
            return;
        }
        this.p = this.o.c(i2 * 36);
        this.p.put(this.b, i3, i4);
        this.p.flip();
        a(i2);
        a(i, i4, i2, this.p, this.a.getMapConfig());
        this.o.a();
    }

    private void a(int i, int i2, int i3, FloatBuffer floatBuffer, MapConfig mapConfig) {
        if (i == 0 || floatBuffer == null || i3 == 0) {
            return;
        }
        if (this.c == null || this.c.c()) {
            k();
        }
        this.c.a();
        GLES20.glUniform1f(this.c.h, mapConfig.getSR());
        GLES20.glEnableVertexAttribArray(this.c.b);
        GLES20.glBindBuffer(34962, this.d);
        GLES20.glVertexAttribPointer(this.c.b, 4, 5126, false, 36, 0);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glEnableVertexAttribArray(this.c.c);
        GLES20.glBindBuffer(34962, this.d);
        GLES20.glVertexAttribPointer(this.c.c, 2, 5126, false, 36, 16);
        GLES20.glEnableVertexAttribArray(this.c.g);
        GLES20.glBindBuffer(34962, this.d);
        GLES20.glVertexAttribPointer(this.c.g, 3, 5126, false, 36, 24);
        GLES20.glUniformMatrix4fv(this.c.a, 1, false, b(), 0);
        GLES20.glBindBuffer(34963, this.e);
        GLES20.glDrawElements(4, i3 * 6, 5123, 0);
        GLES20.glBindBuffer(34962, 0);
        GLES20.glBindBuffer(34963, 0);
        GLES20.glBindTexture(3553, 0);
        GLES20.glDisableVertexAttribArray(this.c.b);
        GLES20.glDisableVertexAttribArray(this.c.c);
        GLES20.glDisable(3042);
        GLES20.glUseProgram(0);
    }

    private void d(dp dpVar) {
        try {
            this.f.add(dpVar);
            f();
        } catch (Throwable th) {
            ic.c(th, "MapOverlayImageView", "addMarker");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        try {
            Collections.sort(this.f, this.i);
        } catch (Throwable th) {
            ic.c(th, "MapOverlayImageView", "changeOverlayIndex");
        }
    }

    private void k() {
        if (this.c != null || this.a == null) {
            return;
        }
        this.c = (ef.d) this.a.u(1);
    }

    public v a(MotionEvent motionEvent) {
        synchronized (this.f) {
            for (int size = this.f.size() - 1; size >= 0; size--) {
                dp dpVar = this.f.get(size);
                if ((dpVar instanceof dv) && fr.a(dpVar.i(), (int) motionEvent.getX(), (int) motionEvent.getY())) {
                    this.l = (dv) dpVar;
                    return this.l;
                }
            }
            return null;
        }
    }

    public Marker a(MarkerOptions markerOptions) throws RemoteException {
        Marker marker;
        if (markerOptions == null) {
            return null;
        }
        dv dvVar = new dv(markerOptions, this);
        synchronized (this.f) {
            d(dvVar);
            fb.a(this.f.size());
            marker = new Marker(dvVar);
        }
        return marker;
    }

    public Text a(TextOptions textOptions) throws RemoteException {
        Text text;
        if (textOptions == null) {
            return null;
        }
        synchronized (this.f) {
            ec ecVar = new ec(textOptions, this);
            d(ecVar);
            text = new Text(ecVar);
        }
        return text;
    }

    public ArrayList<Marker> a(ArrayList<MarkerOptions> arrayList, boolean z) throws RemoteException {
        MarkerOptions markerOptions;
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        ArrayList<Marker> arrayList2 = new ArrayList<>();
        try {
            if (arrayList.size() != 1 || (markerOptions = arrayList.get(0)) == null) {
                final LatLngBounds.Builder builder = LatLngBounds.builder();
                for (int i = 0; i < arrayList.size(); i++) {
                    MarkerOptions markerOptions2 = arrayList.get(i);
                    if (arrayList.get(i) != null) {
                        arrayList2.add(a(markerOptions2));
                        if (markerOptions2.getPosition() != null) {
                            builder.include(markerOptions2.getPosition());
                        }
                    }
                }
                if (z && arrayList2.size() > 0) {
                    this.a.getMainHandler().postDelayed(new Runnable() { // from class: com.amap.api.mapcore.util.aj.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                aj.this.a.a(aw.a(builder.build(), 50));
                            } catch (Throwable unused) {
                            }
                        }
                    }, 50L);
                }
            } else {
                arrayList2.add(a(markerOptions));
                if (z && markerOptions.getPosition() != null) {
                    this.a.a(aw.a(markerOptions.getPosition(), 18.0f));
                }
            }
            return arrayList2;
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImpGLSurfaceView", "addMarkers");
            th.printStackTrace();
            return arrayList2;
        }
    }

    public void a() {
        this.m = null;
    }

    public void a(am amVar) {
        synchronized (this.g) {
            if (amVar != null) {
                try {
                    this.g.add(amVar);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public void a(dm dmVar) {
        try {
            if (this.m != null) {
                if (dmVar != null && dmVar.getId().equals(this.m.getId())) {
                    return;
                } else {
                    this.m.b(false);
                }
            }
            if (this.f.contains(dmVar)) {
                if (dmVar != null) {
                    dmVar.b(true);
                }
                this.m = dmVar;
            }
        } catch (Throwable th) {
            ic.c(th, "MapOverlayImageView", "set2Top");
        }
    }

    public void a(v vVar) {
        if (this.k == null) {
            this.k = IPoint.obtain();
        }
        Rect i = vVar.i();
        this.k = IPoint.obtain(i.left + (i.width() / 2), i.top);
        this.l = vVar;
        try {
            this.a.a(this.l);
        } catch (Throwable th) {
            ic.c(th, "MapOverlayImageView", "showInfoWindow");
            th.printStackTrace();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x001d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(java.lang.String r7) {
        /*
            r6 = this;
            r0 = 0
            if (r7 == 0) goto L12
            java.lang.String r1 = r7.trim()     // Catch: java.lang.Throwable -> L10
            int r1 = r1.length()     // Catch: java.lang.Throwable -> L10
            if (r1 != 0) goto Le
            goto L12
        Le:
            r1 = 0
            goto L13
        L10:
            r7 = move-exception
            goto L68
        L12:
            r1 = 1
        L13:
            r2 = 0
            r6.l = r2     // Catch: java.lang.Throwable -> L10
            r6.k = r2     // Catch: java.lang.Throwable -> L10
            r6.m = r2     // Catch: java.lang.Throwable -> L10
            java.util.List<com.amap.api.mapcore.util.dp> r3 = r6.f     // Catch: java.lang.Throwable -> L10
            monitor-enter(r3)     // Catch: java.lang.Throwable -> L10
            java.util.List<com.amap.api.mapcore.util.dp> r4 = r6.h     // Catch: java.lang.Throwable -> L65
            r4.clear()     // Catch: java.lang.Throwable -> L65
            if (r1 == 0) goto L2a
            java.util.List<com.amap.api.mapcore.util.dp> r7 = r6.f     // Catch: java.lang.Throwable -> L65
            r7.clear()     // Catch: java.lang.Throwable -> L65
            goto L63
        L2a:
            java.util.List<com.amap.api.mapcore.util.dp> r1 = r6.f     // Catch: java.lang.Throwable -> L65
            int r1 = r1.size()     // Catch: java.lang.Throwable -> L65
        L30:
            if (r0 >= r1) goto L49
            java.util.List<com.amap.api.mapcore.util.dp> r4 = r6.f     // Catch: java.lang.Throwable -> L65
            java.lang.Object r4 = r4.get(r0)     // Catch: java.lang.Throwable -> L65
            com.amap.api.mapcore.util.dp r4 = (com.amap.api.mapcore.util.dp) r4     // Catch: java.lang.Throwable -> L65
            java.lang.String r5 = r4.getId()     // Catch: java.lang.Throwable -> L65
            boolean r5 = r7.equals(r5)     // Catch: java.lang.Throwable -> L65
            if (r5 == 0) goto L46
            r2 = r4
            goto L49
        L46:
            int r0 = r0 + 1
            goto L30
        L49:
            java.util.List<com.amap.api.mapcore.util.dp> r7 = r6.f     // Catch: java.lang.Throwable -> L65
            r7.clear()     // Catch: java.lang.Throwable -> L65
            if (r2 == 0) goto L63
            java.util.List<com.amap.api.mapcore.util.dp> r7 = r6.f     // Catch: java.lang.Throwable -> L65
            r7.add(r2)     // Catch: java.lang.Throwable -> L65
            boolean r7 = r2.l()     // Catch: java.lang.Throwable -> L65
            if (r7 == 0) goto L63
            boolean r7 = r2 instanceof com.amap.api.mapcore.util.dm     // Catch: java.lang.Throwable -> L65
            if (r7 == 0) goto L63
            com.amap.api.mapcore.util.dm r2 = (com.amap.api.mapcore.util.dm) r2     // Catch: java.lang.Throwable -> L65
            r6.m = r2     // Catch: java.lang.Throwable -> L65
        L63:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L65
            goto L72
        L65:
            r7 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L65
            throw r7     // Catch: java.lang.Throwable -> L10
        L68:
            java.lang.String r0 = "MapOverlayImageView"
            java.lang.String r1 = "clear"
            com.amap.api.mapcore.util.ic.c(r7, r0, r1)
            r7.printStackTrace()
        L72:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.aj.a(java.lang.String):void");
    }

    public void a(boolean z) {
        int i;
        try {
            if (this.a == null) {
                return;
            }
            float mapPerPixelUnitLength = this.a.getMapConfig().getMapPerPixelUnitLength();
            synchronized (this.f) {
                h();
                if (this.f.size() == 0) {
                    return;
                }
                this.h.clear();
                int size = this.f.size();
                while (i < size) {
                    dp dpVar = this.f.get(i);
                    if (!z) {
                        i = dpVar.getZIndex() == 2.14748365E9f ? i + 1 : 0;
                    } else if (dpVar.getZIndex() != 2.14748365E9f) {
                    }
                    if (dpVar.isVisible() && !dpVar.l()) {
                        this.j = dpVar.j();
                        if (dpVar.h() || dpVar.isInfoWindowShown()) {
                            try {
                                this.h.add(dpVar);
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                        }
                    }
                }
                if (this.m != null && this.m.isVisible()) {
                    this.h.add(this.m);
                }
                if (this.h.size() > 0) {
                    int size2 = this.h.size();
                    int i2 = 0;
                    int i3 = 0;
                    int i4 = 0;
                    int i5 = 0;
                    for (int i6 = 0; i6 < size2; i6++) {
                        dp dpVar2 = this.h.get(i6);
                        synchronized (dpVar2) {
                            dpVar2.a(this.a);
                            if (i6 == 0) {
                                i3 = dpVar2.k();
                            } else {
                                int k = dpVar2.k();
                                if (k != i3) {
                                    a(i3, i2, i4, i5);
                                    i3 = k;
                                    i2 = 0;
                                    i4 = 0;
                                    i5 = 0;
                                }
                            }
                            dpVar2.a(this.a, this.b, i5, mapPerPixelUnitLength);
                            int k2 = dpVar2.k();
                            if (k2 != i3) {
                                a(i3, i2, i4, i5);
                                i4 = i5;
                                i3 = k2;
                                i2 = 0;
                                i5 = 0;
                            }
                            i5 += 36;
                            i2++;
                            if (i2 == 5000) {
                                a(i3, i2, i4, i5);
                                i2 = 0;
                                i4 = 0;
                                i5 = 0;
                            }
                        }
                    }
                    if (i2 > 0) {
                        a(i3, i2, i4, i5);
                    }
                }
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }

    public synchronized boolean a(Bitmap bitmap, am amVar) {
        if (this.n.a(bitmap.getWidth() + 1, bitmap.getHeight() + 1, amVar.o()) == null) {
            return false;
        }
        amVar.f(r6.a / this.n.a());
        amVar.e(r6.b / this.n.b());
        amVar.g(((r6.a + r6.c) - 1) / this.n.a());
        amVar.h(((r6.b + r6.d) - 1) / this.n.b());
        amVar.c((r6.a + 0.5f) / this.n.a());
        amVar.d((r6.b + 0.5f) / this.n.b());
        amVar.a((((r6.a + r6.c) - 1) - 0.5f) / this.n.a());
        amVar.b((((r6.b + r6.d) - 1) - 0.5f) / this.n.b());
        amVar.a(true);
        return true;
    }

    public boolean a(dp dpVar) {
        boolean remove;
        synchronized (this.f) {
            try {
                if (this.m != null && this.m.getId().equals(dpVar.getId())) {
                    this.m = null;
                }
                b(dpVar);
            } catch (Throwable th) {
                th.printStackTrace();
            }
            remove = this.f.remove(dpVar);
        }
        return remove;
    }

    public void b(dp dpVar) {
        try {
            if (dpVar.isInfoWindowShown()) {
                this.a.i();
                this.l = null;
            } else if (this.l != null && this.l.getId().equals(dpVar.getId())) {
                this.l = null;
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public boolean b(MotionEvent motionEvent) throws RemoteException {
        boolean z;
        Rect i;
        boolean a2;
        synchronized (this.f) {
            z = false;
            int size = this.f.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                dp dpVar = this.f.get(size);
                if ((dpVar instanceof dv) && dpVar.isVisible() && ((dv) dpVar).isClickable() && (a2 = fr.a((i = dpVar.i()), (int) motionEvent.getX(), (int) motionEvent.getY()))) {
                    this.k = IPoint.obtain(i.left + (i.width() / 2), i.top);
                    this.l = (dv) dpVar;
                    z = a2;
                    break;
                }
                size--;
            }
        }
        return z;
    }

    public float[] b() {
        return this.a != null ? this.a.x() : new float[16];
    }

    public ad c() {
        return this.a;
    }

    public boolean c(dp dpVar) {
        boolean contains;
        synchronized (this.f) {
            contains = this.f.contains(dpVar);
        }
        return contains;
    }

    public v d() {
        return this.l;
    }

    public List<Marker> e() {
        ArrayList arrayList;
        synchronized (this.f) {
            arrayList = new ArrayList();
            try {
                for (dp dpVar : this.f) {
                    if ((dpVar instanceof dv) && dpVar.h()) {
                        arrayList.add(new Marker((IMarker) dpVar));
                    }
                }
            } catch (Throwable th) {
                ic.c(th, "MapOverlayImageView", "getMapScreenMarkers");
                th.printStackTrace();
            }
        }
        return arrayList;
    }

    public void f() {
        if (this.r != null) {
            this.r.removeCallbacks(this.t);
            this.r.postDelayed(this.t, 10L);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int g() {
        int size;
        synchronized (this.f) {
            size = this.f.size();
        }
        return size;
    }

    public void h() {
        synchronized (this.g) {
            int e = this.a.e();
            for (int i = 0; i < this.g.size(); i++) {
                am amVar = this.g.get(i);
                if (amVar != null) {
                    amVar.m();
                    if (amVar.n() <= 0) {
                        if (amVar.k() == e) {
                            this.n.a(amVar.o());
                        } else {
                            this.s[0] = amVar.k();
                            GLES20.glDeleteTextures(1, this.s, 0);
                        }
                        if (this.a != null) {
                            this.a.c(amVar.o());
                        }
                    }
                }
            }
            this.g.clear();
        }
    }

    public void i() {
        try {
            for (dp dpVar : this.f) {
                if (dpVar != null) {
                    dpVar.destroy(false);
                }
            }
            a((String) null);
            if (this.q != null) {
                this.q.quit();
            }
            this.r = null;
            this.a = null;
        } catch (Throwable th) {
            ic.c(th, "MapOverlayImageView", "destroy");
            th.printStackTrace();
            Log.d("amapApi", "MapOverlayImageView clear erro" + th.getMessage());
        }
    }
}
