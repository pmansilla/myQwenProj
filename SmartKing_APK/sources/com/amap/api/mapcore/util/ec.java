package com.amap.api.mapcore.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.TextOptions;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.Rectangle;
import com.autonavi.amap.mapcore.interfaces.IOverlayImage;
import java.util.ArrayList;
import java.util.List;

/* compiled from: TextDelegateImp.java */
/* loaded from: classes.dex */
public class ec implements dt {
    private static int a;
    private int A;
    private int g;
    private BitmapDescriptor h;
    private int i;
    private int j;
    private String k;
    private LatLng l;
    private boolean o;
    private aj p;
    private Object q;
    private String r;
    private int s;
    private int t;
    private int u;
    private Typeface v;
    private float w;
    private int z;
    private float b = 0.0f;
    private float c = 0.0f;
    private int d = 4;
    private int e = 32;
    private FPoint f = FPoint.obtain();
    private float m = 0.5f;
    private float n = 1.0f;
    private Rect x = new Rect();
    private Paint y = new Paint();
    private boolean B = false;
    private List<am> C = new ArrayList();
    private boolean D = false;
    private boolean E = false;
    private float[] F = {-1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};

    public ec(TextOptions textOptions, aj ajVar) throws RemoteException {
        this.o = true;
        this.p = ajVar;
        if (textOptions.getPosition() != null) {
            this.l = textOptions.getPosition();
        }
        setAlign(textOptions.getAlignX(), textOptions.getAlignY());
        this.o = textOptions.isVisible();
        this.r = textOptions.getText();
        this.s = textOptions.getBackgroundColor();
        this.t = textOptions.getFontColor();
        this.u = textOptions.getFontSize();
        this.q = textOptions.getObject();
        this.w = textOptions.getZIndex();
        this.v = textOptions.getTypeface();
        this.k = getId();
        setRotateAngle(textOptions.getRotate());
        b();
        a();
    }

    private int a(boolean z, BitmapDescriptor bitmapDescriptor) {
        am amVar;
        d();
        if (z) {
            amVar = this.p.c().a(bitmapDescriptor);
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
            i = e();
            amVar.a(i);
            if (z) {
                this.p.c().a(amVar);
            }
            a(amVar);
            fr.b(i, bitmap, true);
        }
        return i;
    }

    private static String a(String str) {
        a++;
        return str + a;
    }

    private void a(am amVar) {
        if (amVar != null) {
            this.C.add(amVar);
            amVar.l();
        }
    }

    private void b() {
        if (this.r == null || this.r.trim().length() <= 0) {
            return;
        }
        try {
            this.y.setTypeface(this.v);
            this.y.setSubpixelText(true);
            this.y.setAntiAlias(true);
            this.y.setStrokeWidth(5.0f);
            this.y.setStrokeCap(Paint.Cap.ROUND);
            this.y.setTextSize(this.u);
            this.y.setTextAlign(Paint.Align.CENTER);
            this.y.setColor(this.t);
            Paint.FontMetrics fontMetrics = this.y.getFontMetrics();
            int i = (int) (fontMetrics.descent - fontMetrics.ascent);
            int i2 = (int) (((i - fontMetrics.bottom) - fontMetrics.top) / 2.0f);
            this.y.getTextBounds(this.r, 0, this.r.length(), this.x);
            Bitmap createBitmap = Bitmap.createBitmap(this.x.width() + 6, i, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawColor(this.s);
            canvas.drawText(this.r, this.x.centerX() + 3, i2, this.y);
            this.h = BitmapDescriptorFactory.fromBitmap(createBitmap);
            this.i = this.h.getWidth();
            this.j = this.h.getHeight();
        } catch (Throwable th) {
            ic.c(th, "TextDelegateImp", "initBitmap");
        }
    }

    private void b(ad adVar, float[] fArr, int i, float f) throws RemoteException {
        float f2 = this.i * f;
        float f3 = f * this.j;
        float f4 = this.f.x;
        float f5 = this.f.y;
        float sc = adVar.getMapConfig().getSC();
        this.F[0] = f4 - (this.m * f2);
        this.F[1] = ((1.0f - this.n) * f3) + f5;
        this.F[2] = f4;
        this.F[3] = f5;
        this.F[6] = this.b;
        this.F[7] = sc;
        this.F[9] = ((1.0f - this.m) * f2) + f4;
        this.F[10] = ((1.0f - this.n) * f3) + f5;
        this.F[11] = f4;
        this.F[12] = f5;
        this.F[15] = this.b;
        this.F[16] = sc;
        this.F[18] = ((1.0f - this.m) * f2) + f4;
        this.F[19] = f5 - (this.n * f3);
        this.F[20] = f4;
        this.F[21] = f5;
        this.F[24] = this.b;
        this.F[25] = sc;
        this.F[27] = f4 - (f2 * this.m);
        this.F[28] = f5 - (f3 * this.n);
        this.F[29] = f4;
        this.F[30] = f5;
        this.F[33] = this.b;
        this.F[34] = sc;
        System.arraycopy(this.F, 0, fArr, i, this.F.length);
    }

    private void c() {
        if (this.p.c() != null) {
            this.p.c().setRunLowFrame(false);
        }
    }

    private void d() {
        if (this.C != null) {
            for (am amVar : this.C) {
                if (amVar != null && this.p != null) {
                    this.p.a(amVar);
                }
            }
            this.C.clear();
        }
    }

    private int e() {
        int[] iArr = {0};
        GLES20.glGenTextures(1, iArr, 0);
        return iArr[0];
    }

    private synchronized void f() {
        b();
        this.E = false;
        c();
    }

    @Override // com.amap.api.mapcore.util.dp
    public void a(ad adVar) {
        if (this.E) {
            return;
        }
        try {
            this.g = a(Build.VERSION.SDK_INT >= 12, this.h);
            this.E = true;
        } catch (Throwable th) {
            ic.c(th, "TextDelegateImp", "loadtexture");
            th.printStackTrace();
        }
    }

    @Override // com.amap.api.mapcore.util.dp
    public void a(ad adVar, float[] fArr, int i, float f) {
        if (!this.o || this.D || this.l == null || this.h == null) {
            return;
        }
        this.f.x = this.z - adVar.getMapConfig().getSX();
        this.f.y = this.A - adVar.getMapConfig().getSY();
        try {
            b(adVar, fArr, i, f);
        } catch (Throwable th) {
            ic.c(th, "TextDelegateImp", "drawMarker");
        }
    }

    public boolean a() {
        if (this.l == null) {
            return false;
        }
        IPoint obtain = IPoint.obtain();
        GLMapState.lonlat2Geo(this.l.longitude, this.l.latitude, obtain);
        this.z = obtain.x;
        this.A = obtain.y;
        this.p.c().a(this.l.latitude, this.l.longitude, this.f);
        obtain.recycle();
        return true;
    }

    @Override // com.amap.api.mapcore.util.dp
    public void b(boolean z) {
        this.B = z;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void destroy(boolean z) {
        try {
            this.D = true;
            if (z) {
                remove();
            }
            if (this.C != null && this.C.size() > 0) {
                for (int i = 0; i < this.C.size(); i++) {
                    am amVar = this.C.get(i);
                    if (amVar != null && this.p != null) {
                        this.p.a(amVar);
                        if (this.p.c() != null) {
                            this.p.c().c(amVar.o());
                        }
                    }
                }
                this.C.clear();
            }
            if (this.h != null) {
                this.h.recycle();
                this.h = null;
            }
            this.l = null;
            this.q = null;
        } catch (Throwable th) {
            ic.c(th, "TextDelegateImp", "destroy");
            th.printStackTrace();
            Log.d("destroy erro", "TextDelegateImp destroy");
        }
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public boolean equalsRemote(IOverlayImage iOverlayImage) throws RemoteException {
        return equals(iOverlayImage) || iOverlayImage.getId().equals(getId());
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getAlignX() throws RemoteException {
        return this.d;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getAlignY() {
        return this.e;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getAnchorU() {
        return this.m;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getAnchorV() {
        return this.n;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getBackgroundColor() throws RemoteException {
        return this.s;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getFontColor() throws RemoteException {
        return this.t;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public int getFontSize() throws RemoteException {
        return this.u;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public String getId() {
        if (this.k == null) {
            this.k = a("Text");
        }
        return this.k;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public Object getObject() {
        return this.q;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public LatLng getPosition() {
        return this.l;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getRotateAngle() {
        return this.c;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public String getText() throws RemoteException {
        return this.r;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public Typeface getTypeface() throws RemoteException {
        return this.v;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public float getZIndex() {
        return this.w;
    }

    @Override // com.amap.api.mapcore.util.dp
    public boolean h() {
        Rectangle geoRectangle = this.p.c().getMapConfig().getGeoRectangle();
        return geoRectangle != null && geoRectangle.contains(this.z, this.A);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public int hashCodeRemote() {
        return super.hashCode();
    }

    @Override // com.amap.api.mapcore.util.dp
    public Rect i() {
        return null;
    }

    @Override // com.amap.api.mapcore.util.dp, com.autonavi.amap.mapcore.interfaces.IMarker
    public boolean isInfoWindowShown() {
        return false;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public boolean isVisible() {
        return this.o;
    }

    @Override // com.amap.api.mapcore.util.dp
    public boolean j() {
        return true;
    }

    @Override // com.amap.api.mapcore.util.dp
    public int k() {
        try {
            return this.g;
        } catch (Throwable unused) {
            return 0;
        }
    }

    @Override // com.amap.api.mapcore.util.dp
    public boolean l() {
        return this.B;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public synchronized boolean remove() {
        c();
        this.o = false;
        return this.p.a(this);
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setAlign(int i, int i2) throws RemoteException {
        this.d = i;
        if (i != 4) {
            switch (i) {
                case 1:
                    this.m = 0.0f;
                    break;
                case 2:
                    this.m = 1.0f;
                    break;
                default:
                    this.m = 0.5f;
                    break;
            }
        } else {
            this.m = 0.5f;
        }
        this.e = i2;
        if (i2 == 8) {
            this.n = 0.0f;
        } else if (i2 == 16) {
            this.n = 1.0f;
        } else if (i2 != 32) {
            this.n = 0.5f;
        } else {
            this.n = 0.5f;
        }
        c();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setAnchor(float f, float f2) {
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setBackgroundColor(int i) throws RemoteException {
        this.s = i;
        f();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setFontColor(int i) throws RemoteException {
        this.t = i;
        f();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setFontSize(int i) throws RemoteException {
        this.u = i;
        f();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setObject(Object obj) {
        this.q = obj;
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setPosition(LatLng latLng) {
        this.l = latLng;
        a();
        c();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setRotateAngle(float f) {
        this.c = f;
        this.b = (((-f) % 360.0f) + 360.0f) % 360.0f;
        c();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setText(String str) throws RemoteException {
        this.r = str;
        f();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IText
    public void setTypeface(Typeface typeface) throws RemoteException {
        this.v = typeface;
        f();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setVisible(boolean z) {
        if (this.o == z) {
            return;
        }
        this.o = z;
        c();
    }

    @Override // com.autonavi.amap.mapcore.interfaces.IOverlayImage
    public void setZIndex(float f) {
        this.w = f;
        this.p.f();
    }
}
