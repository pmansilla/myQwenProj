package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.amap.api.maps.model.BasePointOverlay;
import com.amap.api.maps.model.GL3DModel;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.animation.Animation;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;
import com.autonavi.amap.mapcore.animation.GLAnimation;
import com.autonavi.amap.mapcore.animation.GLTransformation;
import com.autonavi.amap.mapcore.interfaces.IInfoWindowManager;
import com.autonavi.amap.mapcore.interfaces.IMarker;
import com.autonavi.amap.mapcore.interfaces.IOverlay;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import kotlin.jvm.internal.LongCompanionObject;

/* compiled from: PopupOverlay.java */
/* loaded from: classes.dex */
public class eb implements bg, Cdo, IInfoWindowManager {
    private GLAnimation H;
    private GLAnimation I;
    ad a;
    a c;
    bh f;
    private Context g;
    private v h;
    private FPoint n;
    private FloatBuffer r;
    private boolean u;
    private Bitmap v;
    private Bitmap w;
    private int z;
    private boolean i = false;
    private int j = 0;
    private int k = 0;
    private int l = 0;
    private int m = 0;
    private FloatBuffer o = null;
    private boolean q = true;
    private float s = 0.5f;
    private float t = 1.0f;
    private Rect x = new Rect();
    private float y = 0.0f;
    private boolean A = true;
    private Bitmap B = null;
    private Bitmap C = null;
    private Bitmap D = null;
    private Bitmap E = null;
    private boolean F = false;
    private boolean G = false;
    float[] b = new float[12];
    float[] d = {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    long e = 0;
    private boolean J = false;
    private boolean K = true;
    private String p = getId();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PopupOverlay.java */
    /* loaded from: classes.dex */
    public static class a extends ee {
        int a;
        int b;
        int c;

        a(String str) {
            if (a(str)) {
                this.a = c("aMVP");
                this.b = b("aVertex");
                this.c = b("aTextureCoord");
            }
        }
    }

    public eb(ad adVar, Context context) {
        this.a = null;
        this.g = context;
        this.a = adVar;
    }

    private Bitmap a(View view) {
        if (view == null) {
            return null;
        }
        if ((view instanceof RelativeLayout) && this.g != null) {
            LinearLayout linearLayout = new LinearLayout(this.g);
            linearLayout.setOrientation(1);
            linearLayout.addView(view);
            view = linearLayout;
        }
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(0);
        return fr.a(view);
    }

    private void a(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        if (floatBuffer == null || floatBuffer2 == null || i == 0) {
            return;
        }
        if (this.c == null) {
            g();
        }
        this.c.a();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        GLES20.glBlendColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i);
        GLES20.glEnableVertexAttribArray(this.c.b);
        GLES20.glVertexAttribPointer(this.c.b, 3, 5126, false, 12, (Buffer) floatBuffer);
        GLES20.glEnableVertexAttribArray(this.c.c);
        GLES20.glVertexAttribPointer(this.c.c, 2, 5126, false, 8, (Buffer) floatBuffer2);
        GLES20.glUniformMatrix4fv(this.c.a, 1, false, this.d, 0);
        GLES20.glDrawArrays(6, 0, 4);
        GLES20.glDisableVertexAttribArray(this.c.b);
        GLES20.glDisableVertexAttribArray(this.c.c);
        GLES20.glBindTexture(3553, 0);
        GLES20.glUseProgram(0);
        GLES20.glDisable(3042);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(boolean z) {
        if (z) {
            b(k());
        } else {
            b(l());
        }
    }

    private synchronized void c(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    private void c(final boolean z) {
        if (this.I != null) {
            this.K = false;
            this.J = true;
            this.I.startNow();
            this.I.setAnimationListener(new Animation.AnimationListener() { // from class: com.amap.api.mapcore.util.eb.1
                @Override // com.amap.api.maps.model.animation.Animation.AnimationListener
                public void onAnimationEnd() {
                    if (eb.this.H != null) {
                        eb.this.J = true;
                        eb.this.H.startNow();
                        eb.this.b(z);
                    }
                }

                @Override // com.amap.api.maps.model.animation.Animation.AnimationListener
                public void onAnimationStart() {
                }
            });
            return;
        }
        if (this.H == null) {
            b(z);
            return;
        }
        this.J = true;
        this.H.startNow();
        b(z);
    }

    private synchronized void d(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                c(this.C);
                this.C = bitmap;
            }
        }
    }

    private synchronized void e(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                c(this.D);
                this.D = bitmap;
            }
        }
    }

    private synchronized void f(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                c(this.E);
                this.E = bitmap;
            }
        }
    }

    private boolean g(Bitmap bitmap) {
        if (this.B != null && bitmap.hashCode() == this.B.hashCode()) {
            return true;
        }
        if (this.D != null && bitmap.hashCode() == this.D.hashCode()) {
            return true;
        }
        if (this.C == null || bitmap.hashCode() != this.C.hashCode()) {
            return this.E != null && bitmap.hashCode() == this.E.hashCode();
        }
        return true;
    }

    private synchronized Bitmap k() {
        return this.B;
    }

    private synchronized Bitmap l() {
        return this.D;
    }

    private void m() {
        if (!this.K && this.I != null && !this.I.hasEnded()) {
            this.J = true;
            GLTransformation gLTransformation = new GLTransformation();
            this.I.getTransformation(AnimationUtils.currentAnimationTimeMillis(), gLTransformation);
            if (Double.isNaN(gLTransformation.scaleX) || Double.isNaN(gLTransformation.scaleY)) {
                return;
            }
            this.y = (float) gLTransformation.scaleX;
            return;
        }
        if (this.H == null || this.H.hasEnded()) {
            this.y = 1.0f;
            this.J = false;
            return;
        }
        this.K = false;
        this.J = true;
        this.j = this.l;
        this.k = this.m;
        GLTransformation gLTransformation2 = new GLTransformation();
        this.H.getTransformation(AnimationUtils.currentAnimationTimeMillis(), gLTransformation2);
        if (Double.isNaN(gLTransformation2.scaleX) || Double.isNaN(gLTransformation2.scaleY)) {
            return;
        }
        this.y = (float) gLTransformation2.scaleX;
    }

    private int n() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    private void o() {
        if (!this.A || this.v == null) {
            b(l());
        } else {
            c(false);
        }
        a(false);
    }

    private void p() {
        if (this.A || this.v == null) {
            b(k());
        } else {
            c(true);
        }
        a(true);
    }

    private synchronized void q() {
        Bitmap bitmap;
        if (this.v != null && (bitmap = this.v) != null) {
            bitmap.recycle();
            this.v = null;
        }
        if (this.w != null && !this.w.isRecycled()) {
            this.w.recycle();
            this.w = null;
        }
        if (this.B != null && !this.B.isRecycled()) {
            this.B.recycle();
        }
        if (this.C != null && !this.C.isRecycled()) {
            this.C.recycle();
        }
        if (this.D != null && !this.D.isRecycled()) {
            this.D.recycle();
        }
        if (this.E != null && !this.E.isRecycled()) {
            this.E.recycle();
        }
    }

    private void r() {
    }

    private Rect s() {
        return new Rect(this.x.left, this.x.top, this.x.right, this.x.top + u());
    }

    private Rect t() {
        return new Rect(this.x.left, this.x.top, this.x.right, this.x.top + v());
    }

    private int u() {
        if (this.B == null || this.B.isRecycled()) {
            return 0;
        }
        return this.B.getHeight();
    }

    private int v() {
        if (this.D == null || this.D.isRecycled()) {
            return 0;
        }
        return this.D.getHeight();
    }

    public synchronized void a(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                this.B = bitmap;
            }
        }
    }

    @Override // com.amap.api.mapcore.util.bg
    public void a(bh bhVar) {
        synchronized (this) {
            this.f = bhVar;
        }
    }

    @Override // com.amap.api.mapcore.util.bg
    public synchronized void a(v vVar) throws RemoteException {
        if (vVar == null) {
            return;
        }
        if (vVar.isInfoWindowEnable()) {
            if (this.h != null && !this.h.getId().equals(vVar.getId())) {
                a_();
            }
            if (this.f != null) {
                this.h = vVar;
                vVar.a(true);
                setVisible(true);
                j();
            }
            this.F = true;
        }
    }

    public void a(FPoint fPoint) {
        this.n = fPoint;
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public void a(MapConfig mapConfig) throws RemoteException {
    }

    public void a(boolean z) {
        this.A = z;
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean a() {
        return true;
    }

    public boolean a(int i, int i2) {
        GLMapState c = this.a.c();
        if (this.n == null || c == null) {
            return false;
        }
        IPoint obtain = IPoint.obtain();
        MapConfig mapConfig = this.a.getMapConfig();
        if (mapConfig != null && c != null) {
            FPoint obtain2 = FPoint.obtain();
            c.p20ToScreenPoint(mapConfig.getSX() + ((int) this.n.x), mapConfig.getSY() + ((int) this.n.y), obtain2);
            obtain.x = (int) obtain2.x;
            obtain.y = (int) obtain2.y;
            obtain2.recycle();
        }
        int e = e();
        int f = f();
        int i3 = (int) ((obtain.x + this.j) - (e * this.s));
        int i4 = (int) (obtain.y + this.k + (f * (1.0f - this.t)));
        obtain.recycle();
        if (i3 - e > i || i3 < (-e) * 2 || i4 < (-f) * 2 || i4 - f > i2 || this.v == null) {
            return false;
        }
        int width = this.v.getWidth();
        int height = this.v.getHeight();
        if (this.r == null) {
            this.r = fr.a(new float[]{0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f});
        }
        double d = 1.0f - this.y;
        Double.isNaN(d);
        double d2 = width;
        Double.isNaN(d2);
        int i5 = (int) (d * 0.5d * d2);
        int i6 = i3 + i5;
        float f2 = i6;
        this.b[0] = f2;
        this.x.left = i6;
        int i7 = i2 - i4;
        float f3 = i7;
        this.b[1] = f3;
        this.b[2] = 0.0f;
        int i8 = i3 + width;
        float f4 = i8 - i5;
        this.b[3] = f4;
        this.b[4] = f3;
        this.x.top = i4 - height;
        this.b[5] = 0.0f;
        this.b[6] = f4;
        this.x.right = i8;
        float f5 = i7 + height;
        this.b[7] = f5;
        this.x.bottom = i4;
        this.b[8] = 0.0f;
        this.b[9] = f2;
        this.b[10] = f5;
        this.b[11] = 0.0f;
        if (this.o == null) {
            this.o = fr.a(this.b);
        } else {
            this.o = fr.a(this.b, this.o);
        }
        return true;
    }

    @Override // com.amap.api.mapcore.util.bg
    public boolean a(MotionEvent motionEvent) {
        return this.q && this.h != null && this.F && fr.a(this.x, (int) motionEvent.getX(), (int) motionEvent.getY());
    }

    @Override // com.amap.api.mapcore.util.bg
    public synchronized void a_() {
        setVisible(false);
        q();
        this.F = false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x00ba, code lost:
    
        r4 = (((r10.h.f() + r10.h.d()) + 2) + r2.height()) + r7.height();
        o();
     */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0056 A[Catch: all -> 0x00f9, TryCatch #1 {, blocks: (B:15:0x003e, B:17:0x0044, B:19:0x0048, B:21:0x004c, B:26:0x0056, B:28:0x0060, B:30:0x0066, B:31:0x0069, B:34:0x006b, B:36:0x0070, B:38:0x0076, B:40:0x0084, B:41:0x00a2, B:48:0x00ba, B:49:0x00db, B:50:0x00d8, B:52:0x0093, B:53:0x00e8, B:54:0x00f7), top: B:14:0x003e, outer: #2 }] */
    @Override // com.amap.api.mapcore.util.bg
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void b() {
        /*
            Method dump skipped, instructions count: 314
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.eb.b():void");
    }

    public void b(int i, int i2) {
        if (!this.q || this.n == null || this.v == null) {
            return;
        }
        h();
        this.v.isRecycled();
        if (!this.G && !this.v.isRecycled()) {
            try {
                if (this.z != 0) {
                    GLES20.glDeleteTextures(1, new int[]{this.z}, 0);
                } else {
                    this.z = n();
                }
                synchronized (this) {
                    if (this.v != null && !this.v.isRecycled()) {
                        fr.b(this.z, this.v, false);
                        this.G = true;
                    }
                }
            } catch (Throwable th) {
                ic.c(th, "PopupOverlay", "drawMarker");
                th.printStackTrace();
                return;
            }
        }
        m();
        if (a(i, i2)) {
            Matrix.setIdentityM(this.d, 0);
            Matrix.orthoM(this.d, 0, 0.0f, i, 0.0f, i2, 1.0f, -1.0f);
            a(this.z, this.o, this.r);
            if (this.u) {
                this.u = false;
                r();
            }
        }
    }

    public synchronized void b(Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                if (this.v == null || this.v.hashCode() != bitmap.hashCode()) {
                    if (this.v != null) {
                        if (this.B == null && this.C == null && this.D == null && this.E == null) {
                            c(this.w);
                            this.w = this.v;
                        } else if (!g(this.v)) {
                            c(this.w);
                            this.w = this.v;
                        }
                    }
                    this.G = false;
                    this.v = bitmap;
                }
            }
        }
    }

    public void c(int i, int i2) throws RemoteException {
        if (this.J) {
            this.l = i;
            this.m = i2;
        } else {
            this.j = i;
            this.k = i2;
            this.l = i;
            this.m = i2;
        }
    }

    @Override // com.amap.api.mapcore.util.Cdo
    public boolean c() {
        return false;
    }

    public boolean d() {
        return this.A;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void destroy() {
        if (this.i) {
            try {
                remove();
                q();
                if (this.r != null) {
                    this.r.clear();
                    this.r = null;
                }
                if (this.o != null) {
                    this.o.clear();
                    this.o = null;
                }
                this.n = null;
                this.z = 0;
            } catch (Throwable th) {
                ic.c(th, "PopupOverlay", "realDestroy");
                th.printStackTrace();
            }
        }
    }

    public int e() {
        try {
            synchronized (this) {
                if (this.v == null || this.v.isRecycled()) {
                    return 0;
                }
                return this.v.getWidth();
            }
        } catch (Throwable unused) {
            return 0;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean equalsRemote(IOverlay iOverlay) throws RemoteException {
        return equals(iOverlay) || iOverlay.getId().equals(getId());
    }

    public int f() {
        try {
            if (this.v == null || this.v.isRecycled()) {
                return 0;
            }
            return this.v.getHeight();
        } catch (Throwable unused) {
            return 0;
        }
    }

    public void g() {
        this.c = new a("texture.glsl");
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public String getId() {
        if (this.p == null) {
            this.p = "PopupOverlay";
        }
        return this.p;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public float getZIndex() {
        return 0.0f;
    }

    protected void h() {
        long j;
        long c;
        synchronized (this) {
            j = 100;
            if (this.f != null) {
                if (this.h instanceof dv) {
                    c = this.f.c((BasePointOverlay) new Marker((IMarker) this.h));
                } else {
                    c = this.f.c(new GL3DModel((dg) this.h));
                }
                if (c <= 0) {
                    j = LongCompanionObject.MAX_VALUE;
                } else if (c > 100) {
                    j = c;
                }
            } else {
                j = 0;
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.e > j) {
            if (this.e != 0) {
                try {
                    a(this.h);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            this.e = currentTimeMillis;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public int hashCodeRemote() {
        return super.hashCode();
    }

    public boolean i() {
        return this.J;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isAboveMaskLayer() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public boolean isVisible() {
        return this.q;
    }

    protected void j() {
        View b;
        View b2;
        try {
            if (!(this.h instanceof dv)) {
                if (this.f != null) {
                    GL3DModel gL3DModel = new GL3DModel((dg) this.h);
                    Bitmap a2 = a(this.f.a(gL3DModel));
                    if (a2 == null && (b = this.f.b(gL3DModel)) != null) {
                        if (b.getBackground() == null) {
                            b.setBackground(this.f.f());
                        }
                        a2 = a(b);
                    }
                    a(a2);
                    return;
                }
                return;
            }
            Marker marker = new Marker((IMarker) this.h);
            if (this.f != null) {
                Bitmap a3 = a(this.f.a((BasePointOverlay) marker));
                if (a3 == null && (b2 = this.f.b((BasePointOverlay) marker)) != null) {
                    if (b2.getBackground() == null) {
                        b2.setBackground(this.f.f());
                    }
                    a3 = a(b2);
                }
                a(a3);
                d(a(this.f.a(marker)));
                e(a(this.f.b(marker)));
                f(a(this.f.c(marker)));
            }
        } catch (Throwable th) {
            ic.c(th, "PopupOverlay", "getInfoWindow");
            th.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void remove() throws RemoteException {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setAboveMaskLayer(boolean z) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowAnimation(Animation animation, Animation.AnimationListener animationListener) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowAppearAnimation(Animation animation) {
        if (this.I == null || !this.I.equals(animation.glAnimation)) {
            this.H = animation.glAnimation;
            return;
        }
        try {
            this.H = animation.glAnimation.mo22clone();
        } catch (Throwable th) {
            ic.c(th, "PopupOverlay", "setInfoWindowDisappearAnimation");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowBackColor(int i) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowBackEnable(boolean z) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowBackScale(float f, float f2) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowDisappearAnimation(Animation animation) {
        if (this.H == null || !this.H.equals(animation.glAnimation)) {
            this.I = animation.glAnimation;
            return;
        }
        try {
            this.I = animation.glAnimation.mo22clone();
        } catch (Throwable th) {
            ic.c(th, "PopupOverlay", "setInfoWindowDisappearAnimation");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void setInfoWindowMovingAnimation(Animation animation) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setVisible(boolean z) {
        if (!this.q && z) {
            this.u = true;
        }
        this.q = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlay
    public void setZIndex(float f) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IInfoWindowManager
    public void startAnimation() {
    }
}
