package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.RemoteException;
import android.view.animation.AnimationUtils;
import com.amap.api.mapcore.util.ef;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.GL3DModelOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.animation.Animation;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.animation.GLAnimation;
import com.autonavi.amap.mapcore.animation.GLAnimationSet;
import com.autonavi.amap.mapcore.animation.GLTransformation;
import com.autonavi.amap.mapcore.animation.GLTranslateAnimation;
import com.autonavi.amap.mapcore.interfaces.IglModel;
import java.util.List;

/* compiled from: Gl3DModelImp.java */
/* loaded from: classes.dex */
public class dg extends v implements IglModel {
    private String H;
    private String I;
    float[] a;
    float[] b;
    private String f;
    private aa h;
    private BitmapDescriptor i;
    private ad j;
    private int k;
    private int l;
    private LatLng m;
    private GLAnimation n;
    private Bitmap q;
    private ef.b r;
    private float s;
    private Object t;
    private y y;
    private int z;
    private boolean e = true;
    private float[] g = new float[16];
    private boolean o = true;
    private boolean p = true;
    private float u = 18.0f;
    private float v = -1.0f;
    private float w = 0.0f;
    private boolean x = false;
    private boolean A = false;
    private boolean B = false;
    private FPoint C = FPoint.obtain();
    Rect c = new Rect(0, 0, 0, 0);
    private int D = 0;
    private int E = 0;
    private float F = 0.5f;
    private float G = 0.5f;
    float d = 1.0f;
    private float J = -1.0f;

    public dg(y yVar, GL3DModelOptions gL3DModelOptions, ad adVar) {
        this.a = new float[16];
        this.b = new float[16];
        if (gL3DModelOptions == null || adVar == null) {
            return;
        }
        this.y = yVar;
        this.j = adVar;
        this.i = gL3DModelOptions.getBitmapDescriptor();
        List<Float> textrue = gL3DModelOptions.getTextrue();
        List<Float> vertext = gL3DModelOptions.getVertext();
        this.m = gL3DModelOptions.getLatLng();
        this.s = gL3DModelOptions.getAngle();
        setModelFixedLength(gL3DModelOptions.getModelFixedLength());
        if (this.m != null) {
            IPoint obtain = IPoint.obtain();
            GLMapState.lonlat2Geo(this.m.longitude, this.m.latitude, obtain);
            this.k = obtain.x;
            this.l = obtain.y;
        }
        if (textrue != null && textrue.size() > 0 && vertext != null) {
            if ((vertext.size() > 0) & (this.i != null)) {
                this.h = new aa(vertext, textrue);
                this.h.a(this.s);
            }
        }
        this.a = new float[16];
        this.b = new float[4];
    }

    private int a(Bitmap bitmap) {
        if (bitmap != null) {
            this.q = bitmap;
        }
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(3553, iArr[0]);
        GLES20.glTexParameterf(3553, 10241, 9728.0f);
        GLES20.glTexParameterf(3553, 10240, 9729.0f);
        GLES20.glTexParameterf(3553, 10242, 33071.0f);
        GLES20.glTexParameterf(3553, 10243, 33071.0f);
        GLUtils.texImage2D(3553, 0, this.q, 0);
        return iArr[0];
    }

    private void a(GLAnimation gLAnimation) {
        if (gLAnimation instanceof GLTranslateAnimation) {
            GLTranslateAnimation gLTranslateAnimation = (GLTranslateAnimation) gLAnimation;
            gLTranslateAnimation.mFromXDelta = this.k;
            gLTranslateAnimation.mFromYDelta = this.l;
            IPoint obtain = IPoint.obtain();
            GLMapState.lonlat2Geo(gLTranslateAnimation.mToXDelta, gLTranslateAnimation.mToYDelta, obtain);
            gLTranslateAnimation.mToXDelta = obtain.x;
            gLTranslateAnimation.mToYDelta = obtain.y;
            obtain.recycle();
        }
    }

    private float m() {
        float mapPerPixelUnitLength = this.j.getMapConfig().getMapPerPixelUnitLength();
        if (this.j.getMapConfig().getSZ() < this.u) {
            return mapPerPixelUnitLength / this.v;
        }
        this.J = mapPerPixelUnitLength;
        return mapPerPixelUnitLength / this.J;
    }

    private float n() {
        return (this.j.getMapConfig().getMapPerPixelUnitLength() * this.w) / this.h.a();
    }

    private void o() {
        if (this.o || this.n == null || this.n.hasEnded()) {
            this.o = true;
            return;
        }
        p();
        GLTransformation gLTransformation = new GLTransformation();
        this.n.getTransformation(AnimationUtils.currentAnimationTimeMillis(), gLTransformation);
        if (Double.isNaN(gLTransformation.x) || Double.isNaN(gLTransformation.y)) {
            return;
        }
        double d = gLTransformation.x;
        double d2 = gLTransformation.y;
        this.k = (int) d;
        this.l = (int) d2;
    }

    private void p() {
        if (this.j != null) {
            this.j.setRunLowFrame(false);
        }
    }

    @Override // com.amap.api.mapcore.util.ah
    public FPoint a() {
        return this.C;
    }

    public void a(String str) {
        this.f = str;
    }

    @Override // com.amap.api.mapcore.util.ah
    public void a(boolean z) {
        this.A = z;
        this.B = true;
    }

    @Override // com.amap.api.mapcore.util.ah
    public LatLng b() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.ah
    public int c() {
        return 0;
    }

    @Override // com.amap.api.mapcore.util.ah
    public int d() {
        return 0;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void destroy() {
        if (this.q != null) {
            this.q.recycle();
        }
        this.y.a(this.z);
        this.h.c();
    }

    @Override // com.amap.api.mapcore.util.ah
    public int e() {
        return this.D;
    }

    @Override // com.amap.api.mapcore.util.ah
    public int f() {
        return this.E;
    }

    @Override // com.amap.api.mapcore.util.ah
    public boolean g() {
        return false;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public String getId() {
        return this.f;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public Object getObject() {
        return this.t;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public LatLng getPosition() {
        return this.m;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public float getRotateAngle() {
        return 0.0f;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public String getSnippet() {
        return this.H;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public String getTitle() {
        return this.I;
    }

    @Override // com.amap.api.mapcore.util.ah
    public boolean h() {
        return this.j.getMapConfig().getGeoRectangle().contains(this.k, this.l);
    }

    @Override // com.amap.api.mapcore.util.ah
    public Rect i() {
        try {
            GLMapState c = this.j.c();
            int k = k();
            int l = l();
            FPoint obtain = FPoint.obtain();
            c.p20ToScreenPoint(this.k, this.l, obtain);
            Matrix.setIdentityM(this.a, 0);
            Matrix.rotateM(this.a, 0, -this.s, 0.0f, 0.0f, 1.0f);
            Matrix.rotateM(this.a, 0, this.j.getMapConfig().getSC(), 1.0f, 0.0f, 0.0f);
            Matrix.rotateM(this.a, 0, this.j.getMapConfig().getSR(), 0.0f, 0.0f, 1.0f);
            float[] fArr = new float[4];
            float f = -k;
            this.b[0] = this.F * f;
            float f2 = l;
            this.b[1] = this.G * f2;
            this.b[2] = 0.0f;
            this.b[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, this.b, 0);
            this.c.set((int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]), (int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]));
            float f3 = k;
            this.b[0] = (1.0f - this.F) * f3;
            this.b[1] = f2 * this.G;
            this.b[2] = 0.0f;
            this.b[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, this.b, 0);
            this.c.union((int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]));
            this.b[0] = f3 * (1.0f - this.F);
            float f4 = -l;
            this.b[1] = (1.0f - this.G) * f4;
            this.b[2] = 0.0f;
            this.b[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, this.b, 0);
            this.c.union((int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]));
            this.b[0] = f * this.F;
            this.b[1] = f4 * (1.0f - this.G);
            this.b[2] = 0.0f;
            this.b[3] = 1.0f;
            Matrix.multiplyMV(fArr, 0, this.a, 0, this.b, 0);
            this.c.union((int) (obtain.x + fArr[0]), (int) (obtain.y - fArr[1]));
            this.D = this.c.centerX() - ((int) obtain.x);
            this.E = this.c.top - ((int) obtain.y);
            obtain.recycle();
            return this.c;
        } catch (Throwable th) {
            ic.c(th, "MarkerDelegateImp", "getRect");
            th.printStackTrace();
            return new Rect(0, 0, 0, 0);
        }
    }

    @Override // com.amap.api.mapcore.util.ah
    public boolean isInfoWindowEnable() {
        return true;
    }

    @Override // com.amap.api.mapcore.util.v, com.autonavi.amap.mapcore.interfaces.IglModel
    public boolean isVisible() {
        return this.p;
    }

    public void j() {
        try {
            if (this.h != null) {
                if (this.r == null) {
                    this.r = (ef.b) this.j.u(5);
                }
                if (this.v == -1.0f) {
                    this.v = this.j.v((int) this.u);
                }
                if (this.e) {
                    this.z = a(this.i.getBitmap());
                    this.h.a(this.z);
                    this.e = false;
                }
                o();
                float sx = this.k - this.j.getMapConfig().getSX();
                this.C.x = sx;
                float sy = this.l - this.j.getMapConfig().getSY();
                this.C.y = sy;
                Matrix.setIdentityM(this.g, 0);
                Matrix.multiplyMM(this.g, 0, this.j.getProjectionMatrix(), 0, this.j.getViewMatrix(), 0);
                Matrix.translateM(this.g, 0, sx, sy, 0.0f);
                if (this.x) {
                    this.d = n();
                } else {
                    this.d = m();
                }
                Matrix.scaleM(this.g, 0, this.d, this.d, this.d);
                this.h.a(this.r, this.g);
                if (this.B) {
                    this.j.j();
                    this.B = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int k() {
        return (int) ((this.h.b() * this.d) / this.j.getMapConfig().getMapPerPixelUnitLength());
    }

    public int l() {
        return (int) ((this.h.a() * this.d) / this.j.getMapConfig().getMapPerPixelUnitLength());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public boolean remove() {
        if (this.j == null) {
            return true;
        }
        this.j.b(this.f);
        return true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setAnimation(Animation animation) {
        if (animation == null) {
            return;
        }
        this.n = animation.glAnimation;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setGeoPoint(IPoint iPoint) {
        if (iPoint != null) {
            this.k = iPoint.x;
            this.l = iPoint.y;
            DPoint obtain = DPoint.obtain();
            GLMapState.geo2LonLat(this.k, this.l, obtain);
            this.m = new LatLng(obtain.y, obtain.x, false);
            obtain.recycle();
        }
        this.j.requestRender();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setModelFixedLength(int i) {
        if (i > 0) {
            this.w = i;
            this.x = true;
        } else {
            this.w = 0.0f;
            this.x = false;
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setObject(Object obj) {
        this.t = obj;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setPosition(LatLng latLng) {
        if (latLng != null) {
            this.m = latLng;
            IPoint obtain = IPoint.obtain();
            GLMapState.lonlat2Geo(latLng.longitude, latLng.latitude, obtain);
            this.k = obtain.x;
            this.l = obtain.y;
            obtain.recycle();
        }
        this.B = true;
        this.j.requestRender();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setRotateAngle(float f) {
        this.s = f;
        if (this.h != null) {
            this.h.a(this.s - this.j.getMapConfig().getSR());
        }
        this.B = true;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setSnippet(String str) {
        this.H = str;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setTitle(String str) {
        this.I = str;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setVisible(boolean z) {
        this.p = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void setZoomLimit(float f) {
        this.u = f;
        this.v = this.j.v((int) this.u);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public void showInfoWindow() {
        try {
            this.j.a(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IglModel
    public boolean startAnimation() {
        if (this.n != null) {
            if (this.n instanceof GLAnimationSet) {
                GLAnimationSet gLAnimationSet = (GLAnimationSet) this.n;
                for (GLAnimation gLAnimation : gLAnimationSet.getAnimations()) {
                    a(gLAnimation);
                    gLAnimation.setDuration(gLAnimationSet.getDuration());
                }
            } else {
                a(this.n);
            }
            this.o = false;
            this.n.start();
        }
        return false;
    }
}
