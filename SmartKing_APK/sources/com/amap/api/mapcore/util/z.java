package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.amap.api.mapcore.util.ba;
import com.amap.api.mapcore.util.bb;
import com.amap.api.mapcore.util.bd;
import com.amap.api.mapcore.util.be;
import com.amap.api.maps.model.AMapGestureListener;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.autonavi.ae.gmap.gesture.EAMapPlatformGestureInfo;
import com.autonavi.amap.mapcore.message.HoverGestureMapMessage;
import com.autonavi.amap.mapcore.message.MoveGestureMapMessage;
import com.autonavi.amap.mapcore.message.RotateGestureMapMessage;
import com.autonavi.amap.mapcore.message.ScaleGestureMapMessage;

/* compiled from: GlMapGestureDetector.java */
/* loaded from: classes.dex */
public class z {
    ad a;
    Context b;
    GestureDetector c;
    public AMapGestureListener d;
    private bd e;
    private bb f;
    private ba g;
    private be h;
    private boolean i = false;
    private int j = 0;
    private int k = 0;
    private int l = 0;
    private int m = 0;
    private int n = 0;
    private boolean o = false;
    private boolean p = false;
    private boolean q = true;
    private Handler r = new Handler(Looper.getMainLooper());

    /* compiled from: GlMapGestureDetector.java */
    /* loaded from: classes.dex */
    private class a implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
        float a;
        long b;
        private int d;
        private EAMapPlatformGestureInfo e;

        private a() {
            this.d = 0;
            this.a = 0.0f;
            this.e = new EAMapPlatformGestureInfo();
            this.b = 0L;
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            z.this.c.setIsLongpressEnabled(false);
            this.d = motionEvent.getPointerCount();
            if (z.this.d != null) {
                z.this.d.onDoubleTap(motionEvent.getX(), motionEvent.getY());
            }
            return false;
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            if (this.d < motionEvent.getPointerCount()) {
                this.d = motionEvent.getPointerCount();
            }
            int action = motionEvent.getAction() & 255;
            if (this.d != 1) {
                return false;
            }
            try {
                if (!z.this.a.h().isZoomGesturesEnabled()) {
                    return false;
                }
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onDoubleTapEvent");
                th.printStackTrace();
            }
            if (action == 0) {
                this.e.mGestureState = 1;
                this.e.mGestureType = 9;
                this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
                int a = z.this.a.a(this.e);
                this.a = motionEvent.getY();
                z.this.a.a(a, ScaleGestureMapMessage.obtain(100, 1.0f, 0, 0));
                this.b = SystemClock.uptimeMillis();
                return true;
            }
            if (action == 2) {
                z.this.o = true;
                float y = this.a - motionEvent.getY();
                if (Math.abs(y) < 20) {
                    return true;
                }
                this.e.mGestureState = 2;
                this.e.mGestureType = 9;
                this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
                int a2 = z.this.a.a(this.e);
                float mapHeight = (4.0f * y) / z.this.a.getMapHeight();
                if (y > 0.0f) {
                    z.this.a.a(a2, ScaleGestureMapMessage.obtain(101, mapHeight, 0, 0));
                } else {
                    z.this.a.a(a2, ScaleGestureMapMessage.obtain(101, mapHeight, 0, 0));
                }
                this.a = motionEvent.getY();
                return true;
            }
            this.e.mGestureState = 3;
            this.e.mGestureType = 9;
            this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
            int a3 = z.this.a.a(this.e);
            z.this.c.setIsLongpressEnabled(true);
            z.this.a.a(a3, ScaleGestureMapMessage.obtain(102, 1.0f, 0, 0));
            if (action != 1) {
                z.this.o = false;
                return true;
            }
            z.this.a.a(a3, 3);
            long uptimeMillis = SystemClock.uptimeMillis() - this.b;
            if (!z.this.o || uptimeMillis < 200) {
                return z.this.a.b(a3, motionEvent);
            }
            z.this.o = false;
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            z.this.o = false;
            return true;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (z.this.d != null) {
                z.this.d.onFling(f, f2);
            }
            try {
                if (z.this.a.h().isScrollGesturesEnabled() && z.this.m <= 0 && z.this.k <= 0 && z.this.l == 0 && !z.this.q) {
                    this.e.mGestureState = 3;
                    this.e.mGestureType = 3;
                    this.e.mLocation = new float[]{motionEvent2.getX(), motionEvent2.getY()};
                    int a = z.this.a.a(this.e);
                    z.this.a.onFling();
                    z.this.a.a().startMapSlidAnim(a, new Point((int) motionEvent2.getX(), (int) motionEvent2.getY()), f, f2);
                }
                return true;
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onFling");
                th.printStackTrace();
                return true;
            }
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            if (z.this.n == 1) {
                this.e.mGestureState = 3;
                this.e.mGestureType = 7;
                this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
                z.this.a.a(z.this.a.a(this.e), motionEvent);
                if (z.this.d != null) {
                    z.this.d.onLongPress(motionEvent.getX(), motionEvent.getY());
                }
            }
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (z.this.d == null) {
                return false;
            }
            z.this.d.onScroll(f, f2);
            return false;
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
            try {
                this.e.mGestureState = 3;
                this.e.mGestureType = 7;
                this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
                z.this.a.a().clearAnimations(z.this.a.a(this.e), false);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        @Override // android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (z.this.n != 1) {
                return false;
            }
            this.e.mGestureState = 3;
            this.e.mGestureType = 8;
            this.e.mLocation = new float[]{motionEvent.getX(), motionEvent.getY()};
            int a = z.this.a.a(this.e);
            if (z.this.d != null) {
                try {
                    z.this.d.onSingleTap(motionEvent.getX(), motionEvent.getY());
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            return z.this.a.c(a, motionEvent);
        }

        @Override // android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }
    }

    /* compiled from: GlMapGestureDetector.java */
    /* loaded from: classes.dex */
    private class b implements ba.a {
        private EAMapPlatformGestureInfo b;

        private b() {
            this.b = new EAMapPlatformGestureInfo();
        }

        @Override // com.amap.api.mapcore.util.ba.a
        public boolean a(ba baVar) {
            this.b.mGestureState = 2;
            this.b.mGestureType = 6;
            boolean z = false;
            this.b.mLocation = new float[]{baVar.c().getX(), baVar.c().getY()};
            try {
                if (!z.this.a.h().isTiltGesturesEnabled()) {
                    return true;
                }
                int a = z.this.a.a(this.b);
                if (z.this.a.d(a) || z.this.l > 3) {
                    return false;
                }
                float f = baVar.d().x;
                float f2 = baVar.d().y;
                if (!z.this.i) {
                    PointF a2 = baVar.a(0);
                    PointF a3 = baVar.a(1);
                    if ((a2.y > 10.0f && a3.y > 10.0f) || (a2.y < -10.0f && a3.y < -10.0f)) {
                        z = true;
                    }
                    if (z) {
                        float f3 = 10;
                        if (Math.abs(f2) > f3 && Math.abs(f) < f3) {
                            z.this.i = true;
                        }
                    }
                }
                if (z.this.i) {
                    z.this.i = true;
                    float f4 = f2 / 6.0f;
                    if (Math.abs(f4) > 1.0f) {
                        z.this.a.a(a, HoverGestureMapMessage.obtain(101, f4));
                        z.m(z.this);
                        return true;
                    }
                }
                return true;
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onHove");
                th.printStackTrace();
                return true;
            }
        }

        @Override // com.amap.api.mapcore.util.ba.a
        public boolean b(ba baVar) {
            this.b.mGestureState = 1;
            this.b.mGestureType = 6;
            this.b.mLocation = new float[]{baVar.c().getX(), baVar.c().getY()};
            try {
                if (!z.this.a.h().isTiltGesturesEnabled()) {
                    return true;
                }
                int a = z.this.a.a(this.b);
                if (z.this.a.d(a)) {
                    return false;
                }
                z.this.a.a(a, HoverGestureMapMessage.obtain(100, z.this.a.o(a)));
                return true;
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onHoveBegin");
                th.printStackTrace();
                return true;
            }
        }

        @Override // com.amap.api.mapcore.util.ba.a
        public void c(ba baVar) {
            this.b.mGestureState = 3;
            this.b.mGestureType = 6;
            this.b.mLocation = new float[]{baVar.c().getX(), baVar.c().getY()};
            try {
                if (z.this.a.h().isTiltGesturesEnabled()) {
                    int a = z.this.a.a(this.b);
                    if (z.this.a.d(a)) {
                        return;
                    }
                    if (z.this.a.o(a) >= 0.0f && z.this.m > 0) {
                        z.this.a.a(a, 7);
                    }
                    z.this.i = false;
                    z.this.a.a(a, HoverGestureMapMessage.obtain(102, z.this.a.o(a)));
                }
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onHoveEnd");
                th.printStackTrace();
            }
        }
    }

    /* compiled from: GlMapGestureDetector.java */
    /* loaded from: classes.dex */
    private class c implements bb.a {
        private EAMapPlatformGestureInfo b;

        private c() {
            this.b = new EAMapPlatformGestureInfo();
        }

        @Override // com.amap.api.mapcore.util.bb.a
        public boolean a(bb bbVar) {
            if (z.this.i) {
                return true;
            }
            try {
                if (z.this.a.h().isScrollGesturesEnabled()) {
                    if (!z.this.p) {
                        this.b.mGestureState = 2;
                        this.b.mGestureType = 3;
                        this.b.mLocation = new float[]{bbVar.c().getX(), bbVar.c().getY()};
                        int a = z.this.a.a(this.b);
                        PointF d = bbVar.d();
                        float f = z.this.j == 0 ? 4.0f : 1.0f;
                        if (Math.abs(d.x) <= f && Math.abs(d.y) <= f) {
                            return false;
                        }
                        if (z.this.j == 0) {
                            z.this.a.a().clearAnimations(a, false);
                        }
                        z.this.a.a(a, MoveGestureMapMessage.obtain(101, d.x, d.y));
                        z.l(z.this);
                        return true;
                    }
                }
                return true;
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onMove");
                th.printStackTrace();
                return true;
            }
        }

        @Override // com.amap.api.mapcore.util.bb.a
        public boolean b(bb bbVar) {
            try {
                if (!z.this.a.h().isScrollGesturesEnabled()) {
                    return true;
                }
                this.b.mGestureState = 1;
                this.b.mGestureType = 3;
                this.b.mLocation = new float[]{bbVar.c().getX(), bbVar.c().getY()};
                z.this.a.a(z.this.a.a(this.b), MoveGestureMapMessage.obtain(100, 0.0f, 0.0f));
                return true;
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onMoveBegin");
                th.printStackTrace();
                return true;
            }
        }

        @Override // com.amap.api.mapcore.util.bb.a
        public void c(bb bbVar) {
            try {
                if (z.this.a.h().isScrollGesturesEnabled()) {
                    this.b.mGestureState = 3;
                    this.b.mGestureType = 3;
                    this.b.mLocation = new float[]{bbVar.c().getX(), bbVar.c().getY()};
                    int a = z.this.a.a(this.b);
                    if (z.this.j > 0) {
                        z.this.a.a(a, 5);
                    }
                    z.this.a.a(a, MoveGestureMapMessage.obtain(102, 0.0f, 0.0f));
                }
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onMoveEnd");
                th.printStackTrace();
            }
        }
    }

    /* compiled from: GlMapGestureDetector.java */
    /* loaded from: classes.dex */
    private class d extends bd.a {
        private boolean b;
        private boolean c;
        private boolean d;
        private Point e;
        private float[] f;
        private float g;
        private float[] h;
        private float i;
        private EAMapPlatformGestureInfo j;

        private d() {
            this.b = false;
            this.c = false;
            this.d = false;
            this.e = new Point();
            this.f = new float[10];
            this.g = 0.0f;
            this.h = new float[10];
            this.i = 0.0f;
            this.j = new EAMapPlatformGestureInfo();
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x012d A[Catch: Throwable -> 0x01a8, TryCatch #4 {Throwable -> 0x01a8, blocks: (B:21:0x011f, B:23:0x012d, B:25:0x0137, B:27:0x013b, B:29:0x0145, B:31:0x014d, B:32:0x014f, B:34:0x0153, B:45:0x0175, B:55:0x0165), top: B:20:0x011f }] */
        /* JADX WARN: Removed duplicated region for block: B:62:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:82:0x00fc A[Catch: Throwable -> 0x010d, TryCatch #0 {Throwable -> 0x010d, blocks: (B:80:0x00d2, B:82:0x00fc, B:83:0x0104, B:85:0x00bc), top: B:84:0x00bc }] */
        /* JADX WARN: Removed duplicated region for block: B:83:0x0104 A[Catch: Throwable -> 0x010d, TRY_LEAVE, TryCatch #0 {Throwable -> 0x010d, blocks: (B:80:0x00d2, B:82:0x00fc, B:83:0x0104, B:85:0x00bc), top: B:84:0x00bc }] */
        @Override // com.amap.api.mapcore.util.bd.a
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean a(com.amap.api.mapcore.util.bd r18) {
            /*
                Method dump skipped, instructions count: 436
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.z.d.a(com.amap.api.mapcore.util.bd):boolean");
        }

        @Override // com.amap.api.mapcore.util.bd.a
        public boolean b(bd bdVar) {
            this.j.mGestureState = 1;
            this.j.mGestureType = 4;
            this.j.mLocation = new float[]{bdVar.a().getX(), bdVar.a().getY()};
            int a = z.this.a.a(this.j);
            int b = (int) bdVar.b();
            int c = (int) bdVar.c();
            this.d = false;
            this.e.x = b;
            this.e.y = c;
            this.b = false;
            this.c = false;
            z.this.a.a(a, ScaleGestureMapMessage.obtain(100, 1.0f, b, c));
            try {
                if (z.this.a.h().isRotateGesturesEnabled() && !z.this.a.e(a)) {
                    z.this.a.a(a, RotateGestureMapMessage.obtain(100, z.this.a.n(a), b, c));
                }
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onScaleRotateBegin");
                th.printStackTrace();
            }
            return true;
        }

        @Override // com.amap.api.mapcore.util.bd.a
        public void c(bd bdVar) {
            float f;
            float f2;
            float f3;
            this.j.mGestureState = 3;
            this.j.mGestureType = 4;
            boolean z = true;
            this.j.mLocation = new float[]{bdVar.a().getX(), bdVar.a().getY()};
            int a = z.this.a.a(this.j);
            this.d = false;
            z.this.a.a(a, ScaleGestureMapMessage.obtain(102, 1.0f, 0, 0));
            if (z.this.k > 0) {
                int i = z.this.k > 10 ? 10 : z.this.k;
                float f4 = 0.0f;
                for (int i2 = 0; i2 < 10; i2++) {
                    f4 += this.f[i2];
                    this.f[i2] = 0.0f;
                }
                float f5 = f4 / i;
                if (0.004f <= f5) {
                    float f6 = f5 * 300.0f;
                    float f7 = f6 < 1.5f ? f6 : 1.5f;
                    if (this.g < 0.0f) {
                        f7 = -f7;
                    }
                    f3 = f7 + z.this.a.a(a);
                } else {
                    f3 = -9999.0f;
                }
                this.g = 0.0f;
                f = f3;
            } else {
                f = -9999.0f;
            }
            if (z.this.a.e(a)) {
                f2 = -9999.0f;
            } else {
                try {
                    if (z.this.a.h().isRotateGesturesEnabled()) {
                        z.this.a.a(a, RotateGestureMapMessage.obtain(102, z.this.a.n(a), 0, 0));
                    }
                } catch (Throwable th) {
                    ic.c(th, "GLMapGestrureDetector", "onScaleRotateEnd");
                    th.printStackTrace();
                }
                if (z.this.l > 0) {
                    z.this.a.a(a, 6);
                    int i3 = z.this.l > 10 ? 10 : z.this.l;
                    float f8 = 0.0f;
                    for (int i4 = 0; i4 < 10; i4++) {
                        f8 += this.h[i4];
                        this.h[i4] = 0.0f;
                    }
                    float f9 = f8 / i3;
                    if (0.1f <= f9) {
                        float f10 = f9 * 200.0f;
                        int n = ((int) z.this.a.n(a)) % SpatialRelationUtil.A_CIRCLE_DEGREE;
                        float f11 = f10 < 60.0f ? f10 : 60.0f;
                        if (this.i < 0.0f) {
                            f11 = -f11;
                        }
                        f2 = ((int) (n + f11)) % SpatialRelationUtil.A_CIRCLE_DEGREE;
                        this.g = 0.0f;
                    }
                }
                f2 = -9999.0f;
                this.g = 0.0f;
            }
            if (f == -9999.0f && f2 == -9999.0f) {
                z = false;
            }
            if (z) {
                z.this.a.a().startPivotZoomRotateAnim(a, this.e, f, (int) f2, 500);
            }
        }
    }

    /* compiled from: GlMapGestureDetector.java */
    /* loaded from: classes.dex */
    private class e extends be.b {
        EAMapPlatformGestureInfo a;

        private e() {
            this.a = new EAMapPlatformGestureInfo();
        }

        @Override // com.amap.api.mapcore.util.be.b, com.amap.api.mapcore.util.be.a
        public void a(be beVar) {
            try {
                if (z.this.a.h().isZoomGesturesEnabled()) {
                    float f = 10;
                    if (Math.abs(beVar.d()) > f || Math.abs(beVar.e()) > f || beVar.b() >= 200) {
                        return;
                    }
                    z.this.q = true;
                    this.a.mGestureState = 2;
                    this.a.mGestureType = 2;
                    this.a.mLocation = new float[]{beVar.c().getX(), beVar.c().getY()};
                    int a = z.this.a.a(this.a);
                    z.this.a.a(a, 4);
                    z.this.a.c(a);
                }
            } catch (Throwable th) {
                ic.c(th, "GLMapGestrureDetector", "onZoomOut");
                th.printStackTrace();
            }
        }
    }

    public z(ad adVar) {
        this.b = adVar.v();
        this.a = adVar;
        a aVar = new a();
        this.c = new GestureDetector(this.b, aVar, this.r);
        this.c.setOnDoubleTapListener(aVar);
        this.e = new bd(this.b, new d());
        this.f = new bb(this.b, new c());
        this.g = new ba(this.b, new b());
        this.h = new be(this.b, new e());
    }

    static /* synthetic */ int g(z zVar) {
        int i = zVar.k;
        zVar.k = i + 1;
        return i;
    }

    static /* synthetic */ int h(z zVar) {
        int i = zVar.l;
        zVar.l = i + 1;
        return i;
    }

    static /* synthetic */ int l(z zVar) {
        int i = zVar.j;
        zVar.j = i + 1;
        return i;
    }

    static /* synthetic */ int m(z zVar) {
        int i = zVar.m;
        zVar.m = i + 1;
        return i;
    }

    public void a() {
        this.j = 0;
        this.l = 0;
        this.k = 0;
        this.m = 0;
        this.n = 0;
    }

    public void a(AMapGestureListener aMapGestureListener) {
        this.d = aMapGestureListener;
    }

    public boolean a(MotionEvent motionEvent) {
        if (this.n < motionEvent.getPointerCount()) {
            this.n = motionEvent.getPointerCount();
        }
        if ((motionEvent.getAction() & 255) == 0) {
            this.p = false;
            this.q = false;
        }
        if (motionEvent.getAction() == 6 && motionEvent.getPointerCount() > 0) {
            this.p = true;
        }
        if (this.o && this.n >= 2) {
            this.o = false;
        }
        try {
            int[] iArr = {0, 0};
            if (this.a != null && this.a.m() != null) {
                this.a.m().getLocationOnScreen(iArr);
            }
            if (this.d != null) {
                if (motionEvent.getAction() == 0) {
                    this.d.onDown(motionEvent.getX(), motionEvent.getY());
                } else if (motionEvent.getAction() == 1) {
                    this.d.onUp(motionEvent.getX(), motionEvent.getY());
                }
            }
            this.c.onTouchEvent(motionEvent);
            boolean c2 = this.g.c(motionEvent, iArr[0], iArr[1]);
            if (this.i && this.m > 0) {
                return c2;
            }
            this.h.c(motionEvent, iArr[0], iArr[1]);
            if (this.o) {
                return c2;
            }
            this.e.a(motionEvent);
            return this.f.c(motionEvent, iArr[0], iArr[1]);
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public void b() {
        if (this.r != null) {
            this.r.removeCallbacks(null);
            this.r = null;
        }
    }
}
