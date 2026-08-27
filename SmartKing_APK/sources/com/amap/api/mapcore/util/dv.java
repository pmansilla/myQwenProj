package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.RemoteException;
import android.util.Log;
import android.view.animation.AnimationUtils;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.animation.GLAnimation;
import com.autonavi.amap.mapcore.animation.GLAnimationSet;
import com.autonavi.amap.mapcore.animation.GLTransformation;
import com.autonavi.amap.mapcore.animation.GLTranslateAnimation;
import com.autonavi.amap.mapcore.interfaces.IAnimation;
import com.autonavi.amap.mapcore.interfaces.IMarkerAction;
import com.autonavi.amap.mapcore.interfaces.IOverlayImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: MarkerDelegateImp.java */
/* loaded from: classes.dex */
public class dv extends v implements dm, IAnimation, IMarkerAction {
    private static int i;
    private MarkerOptions D;
    private float P;
    private float Q;
    private am T;
    private String W;
    private LatLng X;
    private LatLng Y;
    private String Z;
    float[] a;
    private String aa;
    private aj af;
    private Object ag;
    private int ap;
    private int aq;
    float[] b;
    GLAnimation f;
    GLAnimation g;
    private boolean j;
    private float k;
    private int v;
    private int w;
    private boolean l = false;
    private boolean m = false;
    private boolean n = false;
    private float o = 0.0f;
    private float p = 0.0f;
    private boolean q = false;
    private int r = 0;
    private int s = 0;
    private int t = 0;
    private int u = 0;
    private FPoint x = FPoint.obtain();
    private float[] y = {-1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    private float z = 0.0f;
    private float A = 1.0f;
    private float B = 1.0f;
    private float C = 1.0f;
    private boolean E = false;
    private boolean F = true;
    private int G = 5;
    private boolean H = true;
    private boolean I = true;
    private boolean J = false;
    private boolean K = false;
    private boolean L = false;
    private boolean M = true;
    private FPoint N = FPoint.obtain();
    private Point O = new Point();
    private int R = 0;
    private int S = 0;
    private am[] U = null;
    Rect c = new Rect(0, 0, 0, 0);
    private boolean V = false;
    private float ab = 0.5f;
    private float ac = 1.0f;
    private boolean ad = false;
    private boolean ae = true;
    private boolean ah = false;
    private List<BitmapDescriptor> ai = new CopyOnWriteArrayList();
    private boolean aj = false;
    private boolean ak = false;
    GLTransformation d = null;
    GLTransformation e = null;
    private boolean al = true;
    private int am = 0;
    private int an = 20;
    private boolean ao = false;
    private long ar = 0;
    Object h = new Object();
    private float as = Float.MAX_VALUE;
    private float at = Float.MIN_VALUE;
    private float au = Float.MIN_VALUE;
    private float av = Float.MAX_VALUE;

    public dv(MarkerOptions markerOptions, aj ajVar) {
        this.af = ajVar;
        setMarkerOptions(markerOptions);
    }

    private Bitmap a(Bitmap bitmap) {
        return (bitmap == null || bitmap.getConfig() == Bitmap.Config.ARGB_8888) ? bitmap : bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    private static String a(String str) {
        i++;
        return str + i;
    }

    private void a(double d, double d2) {
        if (!this.ao) {
            a((int) d, (int) d2);
            return;
        }
        IPoint obtain = IPoint.obtain();
        this.af.c().a((int) d, (int) d2, obtain);
        a(obtain.x, obtain.y);
        obtain.recycle();
        this.ao = true;
    }

    private void a(int i2, int i3) {
        this.v = i2;
        this.w = i3;
        DPoint obtain = DPoint.obtain();
        GLMapState.geo2LonLat(this.v, this.w, obtain);
        this.X = new LatLng(obtain.y, obtain.x, false);
        if (this.af != null && this.af.c() != null) {
            this.x.x = this.v - this.af.c().getMapConfig().getSX();
            this.x.y = this.w - this.af.c().getMapConfig().getSY();
        }
        obtain.recycle();
        s();
    }

    private void a(ad adVar, float f, int i2, int i3) throws RemoteException {
        float f2 = ((int) (this.A * i2)) * f;
        float f3 = ((int) (this.B * i3)) * f;
        float f4 = this.x.x;
        float f5 = this.x.y;
        float sc = adVar.getMapConfig().getSC();
        float f6 = this.o;
        if (this.ai != null && this.ai.size() > 0) {
            if (this.j) {
                int length = this.U.length;
                int i4 = (int) (this.o / this.k);
                if (i4 > length) {
                    i4 = 0;
                } else {
                    f6 = this.o % this.k;
                }
                this.T = this.U[(i4 + length) % length];
            } else {
                this.am++;
                if (this.am >= this.an * this.ai.size()) {
                    this.am = 0;
                }
                if (this.an == 0) {
                    this.an = 1;
                }
                this.T = this.U[this.am / this.an];
                if (!this.al) {
                    s();
                }
            }
        }
        if (this.q) {
            f6 -= adVar.getMapConfig().getSR();
            sc = 0.0f;
        }
        float f7 = this.C;
        if (f7 < 0.0f) {
            f7 = 0.0f;
        }
        if (f7 > 1.0f) {
            f7 = 1.0f;
        }
        this.y[0] = f4 - (this.ab * f2);
        this.y[1] = ((1.0f - this.ac) * f3) + f5;
        this.y[2] = f4;
        this.y[3] = f5;
        this.y[6] = f6;
        this.y[7] = sc;
        this.y[8] = f7;
        this.y[9] = ((1.0f - this.ab) * f2) + f4;
        this.y[10] = ((1.0f - this.ac) * f3) + f5;
        this.y[11] = f4;
        this.y[12] = f5;
        this.y[15] = f6;
        this.y[16] = sc;
        this.y[17] = f7;
        this.y[18] = ((1.0f - this.ab) * f2) + f4;
        this.y[19] = f5 - (this.ac * f3);
        this.y[20] = f4;
        this.y[21] = f5;
        this.y[24] = f6;
        this.y[25] = sc;
        this.y[26] = f7;
        this.y[27] = f4 - (f2 * this.ab);
        this.y[28] = f5 - (f3 * this.ac);
        this.y[29] = f4;
        this.y[30] = f5;
        this.y[33] = f6;
        this.y[34] = sc;
        this.y[35] = f7;
    }

    private void a(GLAnimation gLAnimation) {
        if (gLAnimation instanceof GLTranslateAnimation) {
            if (this.ao) {
                this.X = getPosition();
                setPosition(this.X);
                this.ao = true;
            }
            if (this.ao) {
                GLTranslateAnimation gLTranslateAnimation = (GLTranslateAnimation) gLAnimation;
                gLTranslateAnimation.mFromXDelta = this.ap;
                gLTranslateAnimation.mFromYDelta = this.aq;
                IPoint obtain = IPoint.obtain();
                this.af.c().b(gLTranslateAnimation.mToYDelta, gLTranslateAnimation.mToXDelta, obtain);
                gLTranslateAnimation.mToXDelta = obtain.x;
                gLTranslateAnimation.mToYDelta = obtain.y;
                obtain.recycle();
                return;
            }
            GLTranslateAnimation gLTranslateAnimation2 = (GLTranslateAnimation) gLAnimation;
            gLTranslateAnimation2.mFromXDelta = this.v;
            gLTranslateAnimation2.mFromYDelta = this.w;
            IPoint obtain2 = IPoint.obtain();
            GLMapState.lonlat2Geo(gLTranslateAnimation2.mToXDelta, gLTranslateAnimation2.mToYDelta, obtain2);
            gLTranslateAnimation2.mToXDelta = obtain2.x;
            gLTranslateAnimation2.mToYDelta = obtain2.y;
            obtain2.recycle();
        }
    }

    private void a(float[] fArr, int i2) {
        if (this.U == null || this.U.length <= 0) {
            return;
        }
        System.arraycopy(this.y, 0, fArr, i2, this.y.length);
    }

    private void s() {
        if (this.af.c() != null) {
            this.af.c().setRunLowFrame(false);
        }
    }

    private void t() {
        try {
            if (this.T.a()) {
                this.y[4] = this.T.d();
                this.y[5] = this.T.c();
                this.y[13] = this.T.b();
                this.y[14] = this.T.c();
                this.y[22] = this.T.b();
                this.y[23] = this.T.e();
                this.y[31] = this.T.d();
                this.y[32] = this.T.e();
            } else {
                this.y[4] = this.T.g();
                this.y[5] = this.T.i();
                this.y[13] = this.T.h();
                this.y[14] = this.T.i();
                this.y[22] = this.T.h();
                this.y[23] = this.T.f();
                this.y[31] = this.T.g();
                this.y[32] = this.T.f();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void u() {
        if (!this.M && this.f != null && !this.f.hasEnded()) {
            s();
            synchronized (this.h) {
                if (this.e == null) {
                    this.e = new GLTransformation();
                    this.e.scaleX = this.A;
                    this.e.scaleY = this.B;
                    this.e.rotate = this.o;
                    this.e.x = this.v;
                    this.e.y = this.w;
                    this.e.alpha = this.C;
                }
                if (this.d == null) {
                    this.d = new GLTransformation();
                }
                this.d.clear();
                this.f.getTransformation(AnimationUtils.currentAnimationTimeMillis(), this.d);
                if (this.d != null) {
                    if (!Double.isNaN(this.d.scaleX) && !Double.isNaN(this.d.scaleY)) {
                        this.A = (float) this.d.scaleX;
                        this.B = (float) this.d.scaleY;
                    }
                    if (!Double.isNaN(this.d.rotate)) {
                        setRotateAngle((float) this.d.rotate);
                    }
                    if (!Double.isNaN(this.d.x) && !Double.isNaN(this.d.y)) {
                        a(this.d.x, this.d.y);
                    }
                    if (!Double.isNaN(this.d.alpha)) {
                        this.C = (float) this.d.alpha;
                    }
                }
            }
            this.n = true;
            this.al = false;
            return;
        }
        if (this.f != null && (this.d != null || this.e != null)) {
            if (this.d != null && !Double.isNaN(this.d.scaleX) && !Double.isNaN(this.d.scaleY)) {
                this.A = (float) this.d.scaleX;
                this.B = (float) this.d.scaleY;
            } else if (this.e != null && !Double.isNaN(this.e.scaleX) && !Double.isNaN(this.e.scaleY) && (this.A != this.e.scaleX || this.B != this.e.scaleY)) {
                this.A = (float) this.e.scaleX;
                this.B = (float) this.e.scaleY;
            }
            if (this.d != null && !Double.isNaN(this.d.rotate)) {
                setRotateAngle((float) this.d.rotate);
            } else if (this.e != null && !Double.isNaN(this.e.rotate) && this.o != this.e.rotate) {
                setRotateAngle((float) this.e.rotate);
            }
            if (this.d != null && !Double.isNaN(this.d.x) && !Double.isNaN(this.d.y)) {
                a(this.d.x, this.d.y);
            } else if (this.e != null && !Double.isNaN(this.e.x) && !Double.isNaN(this.e.y) && (this.v != this.e.x || this.w != this.e.y)) {
                a(this.d.x, this.d.y);
            }
            if (this.d != null && !Double.isNaN(this.d.alpha)) {
                this.C = (float) this.d.alpha;
            } else if (this.e != null && !Double.isNaN(this.e.alpha) && this.C != this.e.alpha) {
                this.C = (float) this.e.alpha;
            }
        }
        this.M = true;
        this.d = null;
        this.e = null;
        if (this.ai == null || this.ai.size() != 1) {
            return;
        }
        this.al = true;
    }

    private int v() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    private void w() {
        if (this.af.c() == null || this.af.c().getMapConfig() == null) {
            return;
        }
        this.P = this.af.c().getMapConfig().getMapPerPixelUnitLength() * n();
        this.Q = this.af.c().getMapConfig().getMapPerPixelUnitLength() * o();
    }

    @Override // com.amap.api.mapcore.util.ah
    public FPoint a() {
        return this.x;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x005b A[Catch: all -> 0x00eb, Throwable -> 0x00ed, TryCatch #0 {Throwable -> 0x00ed, blocks: (B:5:0x0005, B:7:0x000a, B:9:0x0010, B:11:0x0014, B:13:0x0019, B:16:0x001c, B:18:0x0023, B:21:0x0037, B:22:0x003e, B:24:0x0044, B:26:0x004c, B:28:0x0052, B:30:0x005b, B:32:0x0062, B:34:0x0068, B:36:0x006e, B:38:0x0086, B:40:0x008f, B:41:0x0092, B:44:0x0096, B:46:0x009e, B:48:0x00c7, B:51:0x00bb, B:43:0x00ca, B:58:0x00d5, B:60:0x00dd, B:61:0x00e2, B:63:0x00e0), top: B:4:0x0005, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0086 A[Catch: all -> 0x00eb, Throwable -> 0x00ed, TryCatch #0 {Throwable -> 0x00ed, blocks: (B:5:0x0005, B:7:0x000a, B:9:0x0010, B:11:0x0014, B:13:0x0019, B:16:0x001c, B:18:0x0023, B:21:0x0037, B:22:0x003e, B:24:0x0044, B:26:0x004c, B:28:0x0052, B:30:0x005b, B:32:0x0062, B:34:0x0068, B:36:0x006e, B:38:0x0086, B:40:0x008f, B:41:0x0092, B:44:0x0096, B:46:0x009e, B:48:0x00c7, B:51:0x00bb, B:43:0x00ca, B:58:0x00d5, B:60:0x00dd, B:61:0x00e2, B:63:0x00e0), top: B:4:0x0005, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0096 A[Catch: all -> 0x00eb, Throwable -> 0x00ed, TryCatch #0 {Throwable -> 0x00ed, blocks: (B:5:0x0005, B:7:0x000a, B:9:0x0010, B:11:0x0014, B:13:0x0019, B:16:0x001c, B:18:0x0023, B:21:0x0037, B:22:0x003e, B:24:0x0044, B:26:0x004c, B:28:0x0052, B:30:0x005b, B:32:0x0062, B:34:0x0068, B:36:0x006e, B:38:0x0086, B:40:0x008f, B:41:0x0092, B:44:0x0096, B:46:0x009e, B:48:0x00c7, B:51:0x00bb, B:43:0x00ca, B:58:0x00d5, B:60:0x00dd, B:61:0x00e2, B:63:0x00e0), top: B:4:0x0005, outer: #1 }] */
    @Override // com.amap.api.mapcore.util.dp
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(com.amap.api.mapcore.util.ad r13) {
        /*
            Method dump skipped, instructions count: 250
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.dv.a(com.amap.api.mapcore.util.ad):void");
    }

    @Override // com.amap.api.mapcore.util.dp
    public void a(ad adVar, float[] fArr, int i2, float f) {
        if (this.V || (this.X == null && !this.ao) || this.ai == null) {
            return;
        }
        try {
            if (!this.l) {
                this.ar = System.currentTimeMillis();
                this.l = true;
            }
            if (this.ao && this.I) {
                IPoint obtain = IPoint.obtain();
                adVar.a(this.ap, this.aq, obtain);
                this.v = obtain.x;
                this.w = obtain.y;
                obtain.recycle();
            }
            this.x.x = this.v - adVar.getMapConfig().getSX();
            if (this.x.x > 1.34217728E8f) {
                this.x.x -= 2.68435456E8f;
            } else if (this.x.x < -1.34217728E8f) {
                this.x.x += 2.68435456E8f;
            }
            this.x.y = this.w - adVar.getMapConfig().getSY();
            int n = n();
            int o = o();
            u();
            a(adVar, f, n, o);
            if (!this.J || !this.al) {
                t();
                this.J = true;
            }
            a(fArr, i2);
            if (this.n && isInfoWindowShown()) {
                this.af.c().j();
                if (System.currentTimeMillis() - this.ar > 1000) {
                    this.n = false;
                }
            }
        } catch (Throwable th) {
            ic.c(th, "MarkerDelegateImp", "drawMarker");
        }
    }

    public synchronized void a(ArrayList<BitmapDescriptor> arrayList) {
        m();
        if (arrayList != null) {
            Iterator<BitmapDescriptor> it = arrayList.iterator();
            while (it.hasNext()) {
                BitmapDescriptor next = it.next();
                if (next != null) {
                    this.ai.add(next);
                }
            }
        }
        if (this.ai.size() > 0) {
            this.R = this.ai.get(0).getWidth();
            this.S = this.ai.get(0).getHeight();
        } else {
            this.ai.add(BitmapDescriptorFactory.defaultMarker());
            this.R = this.ai.get(0).getWidth();
            this.S = this.ai.get(0).getHeight();
        }
    }

    @Override // com.amap.api.mapcore.util.ah
    public void a(boolean z) {
        this.m = z;
        this.n = true;
    }

    @Override // com.amap.api.mapcore.util.ah
    public LatLng b() {
        try {
            if (!this.ao) {
                return this.aj ? this.Y : this.X;
            }
            DPoint obtain = DPoint.obtain();
            this.af.c().b(this.ap, this.aq, obtain);
            LatLng latLng = new LatLng(obtain.y, obtain.y);
            obtain.recycle();
            return latLng;
        } catch (Throwable th) {
            ic.c(th, "MarkerDelegateImp", "getRealPosition");
            return null;
        }
    }

    @Override // com.amap.api.mapcore.util.dp
    public void b(boolean z) {
        this.L = z;
    }

    @Override // com.amap.api.mapcore.util.ah
    public int c() {
        return this.r;
    }

    @Override // com.amap.api.mapcore.util.ah
    public int d() {
        return this.s;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void destroy(boolean z) {
        try {
            this.V = true;
            if (z) {
                remove();
            }
            if (this.af != null) {
                for (int i2 = 0; this.U != null && i2 < this.U.length; i2++) {
                    am amVar = this.U[i2];
                    if (amVar != null) {
                        this.af.a(amVar);
                        this.af.c().c(amVar.o());
                    }
                }
            }
            for (int i3 = 0; this.ai != null && i3 < this.ai.size(); i3++) {
                this.ai.get(i3).recycle();
            }
            this.X = null;
            this.ag = null;
            this.U = null;
        } catch (Throwable th) {
            ic.c(th, "MarkerDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "MarkerDelegateImp destroy");
        }
    }

    @Override // com.amap.api.mapcore.util.ah
    public int e() {
        return this.t;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public boolean equalsRemote(IOverlayImage iOverlayImage) throws RemoteException {
        return equals(iOverlayImage) || iOverlayImage.getId().equals(getId());
    }

    @Override // com.amap.api.mapcore.util.ah
    public int f() {
        return this.u;
    }

    @Override // com.amap.api.mapcore.util.ah
    public boolean g() {
        return this.ao;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public float getAlpha() {
        return this.C;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getAnchorU() {
        return this.ab;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getAnchorV() {
        return this.ac;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public int getDisplayLevel() {
        return this.G;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public IPoint getGeoPoint() {
        IPoint obtain = IPoint.obtain();
        if (this.ao) {
            this.af.c().a(this.ap, this.aq, obtain);
            return obtain;
        }
        obtain.set(this.v, this.w);
        return obtain;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public IMarkerAction getIMarkerAction() {
        return this;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public synchronized ArrayList<BitmapDescriptor> getIcons() {
        if (this.ai == null || this.ai.size() <= 0) {
            return null;
        }
        ArrayList<BitmapDescriptor> arrayList = new ArrayList<>();
        Iterator<BitmapDescriptor> it = this.ai.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public String getId() {
        if (this.W == null) {
            this.W = a("Marker");
        }
        return this.W;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public Object getObject() {
        return this.ag;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public MarkerOptions getOptions() {
        return this.D;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public int getPeriod() {
        return this.an;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public LatLng getPosition() {
        if (!this.ao || this.x == null) {
            return this.X;
        }
        DPoint obtain = DPoint.obtain();
        IPoint obtain2 = IPoint.obtain();
        q();
        if (this.af.c() == null) {
            return this.X;
        }
        this.af.c().a(this.x.x, this.x.y, obtain2);
        GLMapState.geo2LonLat(obtain2.x, obtain2.y, obtain);
        LatLng latLng = new LatLng(obtain.y, obtain.x);
        obtain2.recycle();
        obtain.recycle();
        return latLng;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getRotateAngle() {
        s();
        return this.p;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public String getSnippet() {
        return this.aa;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public String getTitle() {
        return this.Z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getZIndex() {
        return this.z;
    }

    @Override // com.amap.api.mapcore.util.ah
    public boolean h() {
        if (this.ao) {
            return true;
        }
        try {
            if (this.x == null) {
                return false;
            }
            if (!this.M) {
                return true;
            }
            this.O.x = this.v;
            this.O.y = this.w;
            Rectangle geoRectangle = this.af.c().getMapConfig().getGeoRectangle();
            if (geoRectangle.contains(this.v, this.w)) {
                return true;
            }
            w();
            int i2 = (int) (this.A * this.P);
            int i3 = (int) (this.B * this.Q);
            int i4 = (int) (this.v - (i2 * this.ab));
            int i5 = (int) (this.w - (i3 * this.ac));
            if (geoRectangle.contains(i4, i5)) {
                return true;
            }
            return geoRectangle.isOverlap(i4, i5, i2, i3);
        } catch (Throwable th) {
            ic.c(th, "MarkerDelegateImp", "checkInBounds");
            return false;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public int hashCodeRemote() {
        return super.hashCode();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void hideInfoWindow() {
        if (isInfoWindowShown()) {
            this.af.b(this);
            s();
            this.m = false;
        }
        this.n = false;
    }

    @Override // com.amap.api.mapcore.util.ah
    public Rect i() {
        if (this.y == null) {
            this.c.set(0, 0, 0, 0);
            return this.c;
        }
        try {
            GLMapState c = this.af.c().c();
            if (c == null) {
                return new Rect(0, 0, 0, 0);
            }
            int n = n();
            int o = o();
            FPoint obtain = FPoint.obtain();
            if (this.ao) {
                obtain.x = this.ap;
                obtain.y = this.aq;
            } else {
                c.p20ToScreenPoint(this.v, this.w, obtain);
            }
            Matrix.setIdentityM(this.a, 0);
            Matrix.rotateM(this.a, 0, -this.o, 0.0f, 0.0f, 1.0f);
            if (this.q) {
                Matrix.rotateM(this.a, 0, this.af.c().getMapConfig().getSC(), 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(this.a, 0, this.af.c().getMapConfig().getSR(), 0.0f, 0.0f, 1.0f);
            }
            float[] fArr = new float[4];
            float f = -n;
            this.b[0] = this.ab * f;
            float f2 = o;
            this.b[1] = this.ac * f2;
            this.b[2] = 0.0f;
            this.b[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, this.b, 0);
            this.c.set((int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]), (int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]));
            float f3 = n;
            this.b[0] = (1.0f - this.ab) * f3;
            this.b[1] = f2 * this.ac;
            this.b[2] = 0.0f;
            this.b[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, this.b, 0);
            this.c.union((int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]));
            this.b[0] = f3 * (1.0f - this.ab);
            float f4 = -o;
            this.b[1] = (1.0f - this.ac) * f4;
            this.b[2] = 0.0f;
            this.b[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, this.b, 0);
            this.c.union((int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]));
            this.b[0] = f * this.ab;
            this.b[1] = f4 * (1.0f - this.ac);
            this.b[2] = 0.0f;
            this.b[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, this.b, 0);
            this.c.union((int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]));
            this.t = (int) (this.c.centerX() - obtain.x);
            this.u = (int) (this.c.top - obtain.y);
            obtain.recycle();
            return this.c;
        } catch (Throwable th) {
            ic.c(th, "MarkerDelegateImp", "getRect");
            th.printStackTrace();
            return new Rect(0, 0, 0, 0);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public boolean isClickable() {
        return this.H;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isDraggable() {
        return this.ad;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isFlat() {
        return this.q;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public boolean isInfoWindowAutoOverturn() {
        return this.E;
    }

    @Override // com.amap.api.mapcore.util.ah
    public boolean isInfoWindowEnable() {
        return this.F;
    }

    @Override // com.amap.api.mapcore.util.dp, com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isInfoWindowShown() {
        return this.m;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isPerspective() {
        return this.ah;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isRemoved() {
        try {
            return !this.af.c(this);
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public boolean isVisible() {
        return this.ae;
    }

    @Override // com.amap.api.mapcore.util.dp
    public boolean j() {
        return this.al;
    }

    @Override // com.amap.api.mapcore.util.dp
    public int k() {
        try {
            if (this.ai != null && this.ai.size() > 0) {
                return this.T.k();
            }
            return 0;
        } catch (Throwable unused) {
            return 0;
        }
    }

    @Override // com.amap.api.mapcore.util.dp
    public boolean l() {
        return this.L;
    }

    synchronized void m() {
        if (this.ai != null) {
            this.ai.clear();
        }
    }

    public int n() {
        try {
            return this.R;
        } catch (Throwable unused) {
            return 0;
        }
    }

    public int o() {
        try {
            return this.S;
        } catch (Throwable unused) {
            return 0;
        }
    }

    public boolean p() {
        return this.af.c(this);
    }

    public boolean q() {
        try {
            if (this.af != null && this.af.c() != null && this.af.c().c() != null) {
                if (this.x == null) {
                    this.x = FPoint.obtain();
                }
                if (!this.ao) {
                    this.af.c().a(this.v, this.w, this.x);
                    return true;
                }
                IPoint obtain = IPoint.obtain();
                this.af.c().a(this.ap, this.aq, obtain);
                this.v = obtain.x;
                this.w = obtain.y;
                obtain.recycle();
                this.af.c().a(this.v, this.w, this.x);
                return true;
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public IAnimation r() {
        return this;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public boolean remove() {
        s();
        this.ae = false;
        if (this.af != null) {
            return this.af.a((dp) this);
        }
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void set2Top() {
        this.af.a((dm) this);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setAlpha(float f) {
        this.C = f;
        this.D.alpha(f);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setAnchor(float f, float f2) {
        if (this.ab == f && this.ac == f2) {
            return;
        }
        this.D.anchor(f, f2);
        this.ab = f;
        this.ac = f2;
        this.n = true;
        s();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setAnimation(Animation animation) {
        IAnimation r = r();
        if (r != null) {
            r.setAnimation(animation == null ? null : animation.glAnimation);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IAnimation
    public void setAnimation(GLAnimation gLAnimation) {
        if (gLAnimation == null) {
            return;
        }
        this.g = gLAnimation;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker, com.autonavi.amap.mapcore.interfaces.IAnimation
    public void setAnimationListener(Animation.AnimationListener animationListener) {
        if (this.g != null) {
            this.g.setAnimationListener(animationListener);
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setAutoOverturnInfoWindow(boolean z) {
        this.E = z;
        this.D.autoOverturnInfoWindow(z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setBelowMaskLayer(boolean z) {
        this.K = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setClickable(boolean z) {
        this.H = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setDisplayLevel(int i2) {
        this.G = i2;
        this.D.displayLevel(i2);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setDraggable(boolean z) {
        this.ad = z;
        this.D.draggable(z);
        s();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setFixingPointEnable(boolean z) {
        this.I = z;
        if (!z) {
            boolean z2 = this.ao;
            this.X = getPosition();
            setPosition(this.X);
            if (z2) {
                this.ao = true;
                return;
            }
            return;
        }
        if (!this.ao || this.X == null) {
            return;
        }
        FPoint obtain = FPoint.obtain();
        this.af.c().c().p20ToScreenPoint(this.v, this.w, obtain);
        this.ap = (int) obtain.x;
        this.aq = (int) obtain.y;
        obtain.recycle();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setFlat(boolean z) throws RemoteException {
        this.q = z;
        s();
        this.D.setFlat(z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setGeoPoint(IPoint iPoint) {
        this.ao = false;
        a(iPoint.x, iPoint.y);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setIcon(BitmapDescriptor bitmapDescriptor) {
        if (bitmapDescriptor != null) {
            try {
                if (this.ai == null) {
                    return;
                }
                synchronized (this) {
                    this.j = false;
                    this.ai.clear();
                    this.ai.add(bitmapDescriptor);
                    this.J = false;
                    this.ak = false;
                    this.l = false;
                    s();
                    this.n = true;
                    this.R = bitmapDescriptor.getWidth();
                    this.S = bitmapDescriptor.getHeight();
                }
            } catch (Throwable th) {
                ic.c(th, "MarkerDelegateImp", "setIcon");
                th.printStackTrace();
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public synchronized void setIcons(ArrayList<BitmapDescriptor> arrayList) {
        if (arrayList != null) {
            try {
            } catch (Throwable th) {
                ic.c(th, "MarkerDelegateImp", "setIcons");
                th.printStackTrace();
            }
            if (this.ai != null) {
                this.j = false;
                a(arrayList);
                this.ak = false;
                this.l = false;
                this.J = false;
                s();
                this.n = true;
            }
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setInfoWindowEnable(boolean z) {
        this.F = z;
        if (!z) {
            hideInfoWindow();
        }
        this.D.infoWindowEnable(z);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setMarkerOptions(MarkerOptions markerOptions) {
        if (markerOptions == null) {
            return;
        }
        this.D = markerOptions;
        this.X = this.D.getPosition();
        IPoint obtain = IPoint.obtain();
        this.aj = this.D.isGps();
        if (this.D.getPosition() != null) {
            if (this.aj) {
                try {
                    double[] a = ka.a(this.D.getPosition().longitude, this.D.getPosition().latitude);
                    this.Y = new LatLng(a[1], a[0]);
                    GLMapState.lonlat2Geo(a[0], a[1], obtain);
                } catch (Throwable th) {
                    ic.c(th, "MarkerDelegateImp", "create");
                    this.Y = this.D.getPosition();
                }
            } else {
                GLMapState.lonlat2Geo(this.X.longitude, this.X.latitude, obtain);
            }
        }
        this.v = obtain.x;
        this.w = obtain.y;
        this.ab = this.D.getAnchorU();
        this.ac = this.D.getAnchorV();
        this.r = this.D.getInfoWindowOffsetX();
        this.s = this.D.getInfoWindowOffsetY();
        this.an = this.D.getPeriod();
        this.z = this.D.getZIndex();
        this.K = this.D.isBelowMaskLayer();
        q();
        a(this.D.getIcons());
        this.j = this.D.isRotatingMode();
        this.k = this.D.getAngleOffset();
        this.ae = this.D.isVisible();
        this.aa = this.D.getSnippet();
        this.Z = this.D.getTitle();
        this.ad = this.D.isDraggable();
        this.W = getId();
        this.ah = this.D.isPerspective();
        this.q = this.D.isFlat();
        this.K = this.D.isBelowMaskLayer();
        this.C = this.D.getAlpha();
        setRotateAngle(this.D.getRotateAngle());
        this.G = this.D.getDisplayLevel();
        this.E = this.D.isInfoWindowAutoOverturn();
        this.F = this.D.isInfoWindowEnable();
        this.a = new float[16];
        this.b = new float[4];
        obtain.recycle();
        fb.a().a(this.X, this.Z, this.aa);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setObject(Object obj) {
        this.ag = obj;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setPeriod(int i2) {
        if (i2 <= 1) {
            this.an = 1;
        } else {
            this.an = i2;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setPerspective(boolean z) {
        this.ah = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setPosition(LatLng latLng) {
        if (latLng == null) {
            ic.c(new AMapException("非法坐标值 latlng is null"), "setPosition", "Marker");
            return;
        }
        this.X = latLng;
        IPoint obtain = IPoint.obtain();
        if (this.aj) {
            try {
                double[] a = ka.a(latLng.longitude, latLng.latitude);
                this.Y = new LatLng(a[1], a[0]);
                GLMapState.lonlat2Geo(a[0], a[1], obtain);
            } catch (Throwable unused) {
                this.Y = latLng;
            }
        } else {
            GLMapState.lonlat2Geo(latLng.longitude, latLng.latitude, obtain);
        }
        this.v = obtain.x;
        this.w = obtain.y;
        this.ao = false;
        q();
        s();
        this.n = true;
        obtain.recycle();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void setPositionByPixels(int i2, int i3) {
        this.ap = i2;
        this.aq = i3;
        this.ao = true;
        q();
        s();
        this.n = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setPositionNotUpdate(LatLng latLng) {
        setPosition(latLng);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setRotateAngle(float f) {
        this.D.rotateAngle(f);
        this.p = f;
        this.o = (((-f) % 360.0f) + 360.0f) % 360.0f;
        this.n = true;
        s();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setRotateAngleNotUpdate(float f) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker, com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setSnippet(String str) {
        this.aa = str;
        s();
        this.D.snippet(str);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker, com.autonavi.amap.mapcore.interfaces.IMarkerAction
    public void setTitle(String str) {
        this.Z = str;
        s();
        this.D.title(str);
        fb.a().a(this.X, this.Z, this.aa);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setVisible(boolean z) {
        if (this.ae == z) {
            return;
        }
        this.D.visible(z);
        this.ae = z;
        if (!z) {
            this.L = false;
            if (isInfoWindowShown()) {
                this.af.b(this);
            }
        }
        s();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setZIndex(float f) {
        this.z = f;
        this.D.zIndex(f);
        if (this.L) {
            this.L = false;
            this.af.a();
        }
        this.af.f();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker
    public void showInfoWindow() {
        if (this.ae && p() && isInfoWindowEnable()) {
            this.af.a((v) this);
            s();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IMarker, com.autonavi.amap.mapcore.interfaces.IAnimation
    public boolean startAnimation() {
        if (this.g != null) {
            synchronized (this.h) {
                if (this.g instanceof GLAnimationSet) {
                    GLAnimationSet gLAnimationSet = (GLAnimationSet) this.g;
                    for (GLAnimation gLAnimation : gLAnimationSet.getAnimations()) {
                        a(gLAnimation);
                        gLAnimation.setDuration(gLAnimationSet.getDuration());
                    }
                } else {
                    a(this.g);
                }
                this.M = false;
                this.f = this.g;
                this.f.start();
            }
            s();
        }
        return false;
    }
}
