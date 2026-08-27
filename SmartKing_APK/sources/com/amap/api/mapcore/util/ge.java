package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.amap.api.mapcore.util.gc;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BasePointOverlay;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.GL3DModel;
import com.amap.api.maps.model.Marker;
import com.autonavi.ae.gmap.GLMapState;
import com.autonavi.ae.gmap.listener.AMapWidgetListener;
import com.autonavi.amap.mapcore.FPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.MapConfig;

/* compiled from: MapOverlayViewGroup.java */
/* loaded from: classes.dex */
public class ge extends ViewGroup implements bg {
    gf a;
    bh b;
    private ad c;
    private Context d;
    private gh e;
    private gd f;
    private gb g;
    private gg h;
    private ga i;
    private gc j;
    private gi k;
    private View l;
    private v m;
    private Drawable n;
    private boolean o;
    private View p;
    private boolean q;
    private boolean r;

    /* compiled from: MapOverlayViewGroup.java */
    /* loaded from: classes.dex */
    public static class a extends ViewGroup.LayoutParams {
        public FPoint a;
        public int b;
        public int c;
        public int d;

        public a(int i, int i2, FPoint fPoint, int i3, int i4, int i5) {
            super(i, i2);
            this.a = null;
            this.b = 0;
            this.c = 0;
            this.d = 51;
            this.a = fPoint;
            this.b = i3;
            this.c = i4;
            this.d = i5;
        }
    }

    public ge(Context context, ad adVar) {
        super(context);
        this.n = null;
        int i = 1;
        this.o = true;
        this.r = true;
        try {
            this.c = adVar;
            this.d = context;
            this.a = new gf();
            this.i = new ga(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
            if (this.c.m() != null) {
                addView(this.c.m(), 0, layoutParams);
            } else {
                i = 0;
            }
            addView(this.i, i, layoutParams);
            if (this.r) {
                return;
            }
            a(context);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void a(Context context) {
        this.e = new gh(context, this.c);
        this.h = new gg(context, this.c);
        this.j = new gc(context);
        this.k = new gi(context, this.c);
        this.f = new gd(context, this.c);
        this.g = new gb(context, this.c);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -1);
        addView(this.e, layoutParams);
        addView(this.h, layoutParams);
        addView(this.j, new ViewGroup.LayoutParams(-2, -2));
        addView(this.k, new a(-2, -2, new FPoint(0.0f, 0.0f), 0, 0, 83));
        addView(this.f, new a(-2, -2, FPoint.obtain(0.0f, 0.0f), 0, 0, 83));
        addView(this.g, new a(-2, -2, FPoint.obtain(0.0f, 0.0f), 0, 0, 51));
        this.g.setVisibility(8);
        this.c.a(new AMapWidgetListener() { // from class: com.amap.api.mapcore.util.ge.1
            @Override // com.autonavi.ae.gmap.listener.AMapWidgetListener
            public void invalidateCompassView() {
                if (ge.this.g == null) {
                    return;
                }
                ge.this.g.post(new Runnable() { // from class: com.amap.api.mapcore.util.ge.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ge.this.g.b();
                    }
                });
            }

            @Override // com.autonavi.ae.gmap.listener.AMapWidgetListener
            public void invalidateScaleView() {
                if (ge.this.h == null) {
                    return;
                }
                ge.this.h.post(new Runnable() { // from class: com.amap.api.mapcore.util.ge.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ge.this.h.b();
                    }
                });
            }

            @Override // com.autonavi.ae.gmap.listener.AMapWidgetListener
            public void invalidateZoomController(final float f) {
                if (ge.this.k == null) {
                    return;
                }
                ge.this.k.post(new Runnable() { // from class: com.amap.api.mapcore.util.ge.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        ge.this.k.a(f);
                    }
                });
            }

            @Override // com.autonavi.ae.gmap.listener.AMapWidgetListener
            public void setFrontViewVisibility(boolean z) {
            }
        });
        try {
            if (this.c.h().isMyLocationButtonEnabled()) {
                return;
            }
            this.f.setVisibility(8);
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImpGLSurfaceView", "locationView gone");
            th.printStackTrace();
        }
    }

    private void a(View view, int i, int i2) throws RemoteException {
        int i3;
        int i4;
        if (view == null) {
            return;
        }
        if (this.l != null) {
            if (view == this.l) {
                return;
            }
            this.l.clearFocus();
            removeView(this.l);
        }
        this.l = view;
        ViewGroup.LayoutParams layoutParams = this.l.getLayoutParams();
        this.l.setDrawingCacheEnabled(true);
        this.l.setDrawingCacheQuality(0);
        this.m.i();
        if (layoutParams != null) {
            int i5 = layoutParams.width;
            i4 = layoutParams.height;
            i3 = i5;
        } else {
            i3 = -2;
            i4 = -2;
        }
        addView(this.l, new a(i3, i4, this.m.a(), i, i2, 81));
    }

    private void a(View view, int i, int i2, int i3, int i4, int i5) {
        int i6 = i5 & 7;
        int i7 = i5 & 112;
        if (i6 == 5) {
            i3 -= i;
        } else if (i6 == 1) {
            i3 -= i / 2;
        }
        if (i7 == 80) {
            i4 -= i2;
        } else if (i7 == 17) {
            i4 -= i2 / 2;
        } else if (i7 == 16) {
            i4 = (i4 / 2) - (i2 / 2);
        }
        view.layout(i3, i4, i3 + i, i4 + i2);
        if (view instanceof ae) {
            this.c.b(i, i2);
        }
    }

    private void a(View view, int i, int i2, int[] iArr) {
        View view2;
        if ((view instanceof ListView) && (view2 = (View) view.getParent()) != null) {
            iArr[0] = view2.getWidth();
            iArr[1] = view2.getHeight();
        }
        if (i <= 0 || i2 <= 0) {
            view.measure(0, 0);
        }
        if (i == -2) {
            iArr[0] = view.getMeasuredWidth();
        } else if (i == -1) {
            iArr[0] = getMeasuredWidth();
        } else {
            iArr[0] = i;
        }
        if (i2 == -2) {
            iArr[1] = view.getMeasuredHeight();
        } else if (i2 == -1) {
            iArr[1] = getMeasuredHeight();
        } else {
            iArr[1] = i2;
        }
    }

    private void a(View view, ViewGroup.LayoutParams layoutParams) {
        int[] iArr = new int[2];
        a(view, layoutParams.width, layoutParams.height, iArr);
        if (view instanceof gc) {
            a(view, iArr[0], iArr[1], 20, (this.c.l().y - 80) - iArr[1], 51);
        } else {
            a(view, iArr[0], iArr[1], 0, 0, 51);
        }
    }

    private void a(View view, a aVar) {
        int[] iArr = new int[2];
        a(view, aVar.width, aVar.height, iArr);
        if (view instanceof gi) {
            a(view, iArr[0], iArr[1], getWidth() - iArr[0], getHeight(), aVar.d);
            return;
        }
        if (view instanceof gd) {
            a(view, iArr[0], iArr[1], getWidth() - iArr[0], iArr[1], aVar.d);
            return;
        }
        if (view instanceof gb) {
            a(view, iArr[0], iArr[1], 0, 0, aVar.d);
            return;
        }
        if (aVar.a != null) {
            IPoint obtain = IPoint.obtain();
            MapConfig mapConfig = this.c.getMapConfig();
            GLMapState c = this.c.c();
            if (mapConfig != null && c != null) {
                FPoint obtain2 = FPoint.obtain();
                c.p20ToScreenPoint(mapConfig.getSX() + ((int) aVar.a.x), mapConfig.getSY() + ((int) aVar.a.y), obtain2);
                obtain.x = (int) obtain2.x;
                obtain.y = (int) obtain2.y;
                obtain2.recycle();
            }
            obtain.x += aVar.b;
            obtain.y += aVar.c;
            a(view, iArr[0], iArr[1], obtain.x, obtain.y, aVar.d);
            obtain.recycle();
        }
    }

    private View b(v vVar) throws RemoteException {
        View view;
        View view2;
        View view3 = null;
        if (vVar instanceof dv) {
            Marker marker = new Marker((dv) vVar);
            try {
                if (this.n == null) {
                    this.n = fg.a(this.d, "infowindow_bg.9.png");
                }
            } catch (Throwable th) {
                ic.c(th, "MapOverlayViewGroup", "showInfoWindow decodeDrawableFromAsset");
                th.printStackTrace();
            }
            try {
                if (this.q) {
                    view2 = this.b.a((BasePointOverlay) marker);
                    if (view2 == null) {
                        try {
                            view2 = this.b.b((BasePointOverlay) marker);
                        } catch (Throwable th2) {
                            th = th2;
                            view3 = view2;
                            ic.c(th, "MapOverlayViewGroup", "getInfoWindow or getInfoContents");
                            th.printStackTrace();
                            return view3;
                        }
                    }
                    this.p = view2;
                    this.q = false;
                } else {
                    view2 = this.p;
                }
                if (view2 != null) {
                    view3 = view2;
                } else {
                    if (!this.b.a()) {
                        return null;
                    }
                    view3 = this.b.a((BasePointOverlay) marker);
                }
                if (view3 != null && view3.getBackground() == null) {
                    view3.setBackground(this.n);
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } else {
            try {
                if (this.n == null) {
                    this.n = fg.a(this.d, "infowindow_bg.9.png");
                }
            } catch (Throwable th4) {
                ic.c(th4, "MapOverlayViewGroup", "showInfoWindow decodeDrawableFromAsset");
                th4.printStackTrace();
            }
            try {
                GL3DModel gL3DModel = new GL3DModel((dg) vVar);
                if (this.q) {
                    view = this.b.a(gL3DModel);
                    if (view == null) {
                        try {
                            view = this.b.b(gL3DModel);
                        } catch (Throwable th5) {
                            th = th5;
                            view3 = view;
                            ic.c(th, "MapOverlayViewGroup", "getInfoWindow or getInfoContents");
                            th.printStackTrace();
                            return view3;
                        }
                    }
                    this.p = view;
                    this.q = false;
                } else {
                    view = this.p;
                }
                if (view != null) {
                    view3 = view;
                } else {
                    if (!this.b.a()) {
                        return null;
                    }
                    view3 = this.b.a(gL3DModel);
                }
                if (view3.getBackground() == null) {
                    view3.setBackground(this.n);
                }
                return view3;
            } catch (Throwable th6) {
                th = th6;
            }
        }
        return view3;
    }

    private void o() {
        if (this.h == null) {
            this.a.a(this, new Object[0]);
        } else {
            if (this.h == null || this.h.getVisibility() != 0) {
                return;
            }
            this.h.postInvalidate();
        }
    }

    public float a(int i) {
        if (this.e == null) {
            return 0.0f;
        }
        o();
        return this.e.d(i);
    }

    public void a(Canvas canvas) {
        Bitmap drawingCache;
        if (this.l == null || this.m == null || (drawingCache = this.l.getDrawingCache(true)) == null) {
            return;
        }
        canvas.drawBitmap(drawingCache, this.l.getLeft(), this.l.getTop(), new Paint());
    }

    @Override // com.amap.api.mapcore.util.bg
    public void a(bh bhVar) {
        this.b = bhVar;
    }

    public void a(gc.a aVar) {
        if (this.j == null) {
            this.a.a(this, aVar);
        } else {
            this.j.a(aVar);
            Log.d("MapOverlayViewGroup", "setOnIndoorFloorSwitchListener");
        }
    }

    @Override // com.amap.api.mapcore.util.bg
    public void a(v vVar) {
        if (vVar == null) {
            return;
        }
        try {
            if (!(this.b != null && this.b.a() && vVar.getTitle() == null && vVar.getSnippet() == null) && vVar.isInfoWindowEnable()) {
                if (this.m != null && !this.m.getId().equals(vVar.getId())) {
                    a_();
                }
                if (this.b != null) {
                    this.m = vVar;
                    vVar.a(true);
                    this.q = true;
                }
            }
        } catch (Throwable unused) {
        }
    }

    public void a(CameraPosition cameraPosition) {
        if (this.e == null) {
            this.a.a(this, cameraPosition);
            return;
        }
        if (this.c.h().isLogoEnable()) {
            if (MapsInitializer.isLoadWorldGridMap() && cameraPosition.zoom >= 7.0f && !fk.a(cameraPosition.target.latitude, cameraPosition.target.longitude)) {
                this.e.setVisibility(8);
            } else if (this.c.o() == -1) {
                this.e.setVisibility(0);
            }
        }
    }

    public void a(Boolean bool) {
        if (this.j == null) {
            this.a.a(this, bool);
        } else if (this.j != null && bool.booleanValue() && this.c.n()) {
            this.j.a(true);
        }
    }

    public void a(Float f) {
        if (this.k == null) {
            this.a.a(this, f);
        } else if (this.k != null) {
            this.k.a(f.floatValue());
        }
    }

    public void a(Integer num) {
        if (this.k == null) {
            this.a.a(this, num);
        } else if (this.k != null) {
            this.k.a(num.intValue());
        }
    }

    public void a(Integer num, Float f) {
        if (this.e != null) {
            this.a.a(this, num, f);
        } else if (this.e != null) {
            this.e.a(num.intValue(), f.floatValue());
            o();
        }
    }

    public void a(String str, Boolean bool, Integer num) {
        if (this.e == null) {
            this.a.a(this, str, bool, num);
        } else {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            this.e.a(str, num.intValue());
            this.e.b(bool.booleanValue());
        }
    }

    @Override // com.amap.api.mapcore.util.bg
    public boolean a(MotionEvent motionEvent) {
        return (this.l == null || this.m == null || !fr.a(new Rect(this.l.getLeft(), this.l.getTop(), this.l.getRight(), this.l.getBottom()), (int) motionEvent.getX(), (int) motionEvent.getY())) ? false : true;
    }

    @Override // com.amap.api.mapcore.util.bg
    public void a_() {
        if (this.c == null || this.c.getMainHandler() == null) {
            return;
        }
        this.c.getMainHandler().post(new Runnable() { // from class: com.amap.api.mapcore.util.ge.2
            @Override // java.lang.Runnable
            public void run() {
                if (ge.this.l != null) {
                    ge.this.l.clearFocus();
                    ge.this.removeView(ge.this.l);
                    fr.a(ge.this.l.getBackground());
                    fr.a(ge.this.n);
                    ge.this.l = null;
                }
            }
        });
        if (this.m != null) {
            this.m.a(false);
        }
        this.m = null;
    }

    @Override // com.amap.api.mapcore.util.bg
    public void b() {
        try {
            if (this.m == null || !this.m.h()) {
                if (this.l == null || this.l.getVisibility() != 0) {
                    return;
                }
                this.l.setVisibility(8);
                return;
            }
            if (this.o) {
                int e = this.m.e() + this.m.c();
                int f = this.m.f() + this.m.d() + 2;
                View b = b(this.m);
                if (b == null) {
                    return;
                }
                a(b, e, f);
                if (this.l != null) {
                    a aVar = (a) this.l.getLayoutParams();
                    if (aVar != null) {
                        aVar.a = this.m.a();
                        aVar.b = e;
                        aVar.c = f;
                    }
                    onLayout(false, 0, 0, 0, 0);
                    if (this.b.a()) {
                        this.b.a(this.m.getTitle(), this.m.getSnippet());
                    }
                    if (this.l.getVisibility() == 8) {
                        this.l.setVisibility(0);
                    }
                }
            }
        } catch (Throwable th) {
            ic.c(th, "MapOverlayViewGroup", "redrawInfoWindow");
            th.printStackTrace();
        }
    }

    public void b(Boolean bool) {
        if (this.k == null) {
            this.a.a(this, bool);
        } else {
            this.k.a(bool.booleanValue());
        }
    }

    public void b(Integer num) {
        if (this.e == null) {
            this.a.a(this, num);
        } else if (this.e != null) {
            this.e.a(num.intValue());
            this.e.postInvalidate();
            o();
        }
    }

    public Point c() {
        if (this.e == null) {
            return null;
        }
        return this.e.c();
    }

    public void c(Boolean bool) {
        if (this.f == null) {
            this.a.a(this, bool);
        } else if (bool.booleanValue()) {
            this.f.setVisibility(0);
        } else {
            this.f.setVisibility(8);
        }
    }

    public void c(Integer num) {
        if (this.e == null) {
            this.a.a(this, num);
        } else if (this.e != null) {
            this.e.b(num.intValue());
            o();
        }
    }

    public void d(Boolean bool) {
        if (this.g == null) {
            this.a.a(this, bool);
        } else {
            this.g.a(bool.booleanValue());
        }
    }

    public void d(Integer num) {
        if (this.e == null) {
            this.a.a(this, num);
        } else if (this.e != null) {
            this.e.c(num.intValue());
            o();
        }
    }

    public boolean d() {
        if (this.e != null) {
            return this.e.e();
        }
        return false;
    }

    public void e() {
        if (this.e == null) {
            this.a.a(this, new Object[0]);
        } else if (this.e != null) {
            this.e.d();
        }
    }

    public void e(Boolean bool) {
        if (this.h == null) {
            this.a.a(this, bool);
        } else {
            this.h.a(bool.booleanValue());
        }
    }

    public ga f() {
        return this.i;
    }

    public void f(Boolean bool) {
        if (this.e == null) {
            this.a.a(this, bool);
        } else {
            this.e.setVisibility(bool.booleanValue() ? 0 : 8);
        }
    }

    public gc g() {
        return this.j;
    }

    public void g(Boolean bool) {
        if (this.e == null) {
            this.a.a(this, bool);
            return;
        }
        if (this.e != null && bool.booleanValue()) {
            this.e.a(true);
        } else if (this.e != null) {
            this.e.a(false);
        }
    }

    public gd h() {
        return this.f;
    }

    public void h(Boolean bool) {
        if (this.f == null) {
            this.a.a(this, bool);
        } else {
            this.f.a(bool.booleanValue());
        }
    }

    public gh i() {
        return this.e;
    }

    public void i(Boolean bool) {
        if (this.j == null) {
            this.a.a(this, bool);
        } else {
            this.j.a(bool.booleanValue());
        }
    }

    public void j() {
        if (this.k != null) {
            this.k.a();
        }
        if (this.h != null) {
            this.h.a();
        }
        if (this.e != null) {
            this.e.a();
        }
        if (this.f != null) {
            this.f.a();
        }
        if (this.g != null) {
            this.g.a();
        }
        if (this.j != null) {
            this.j.b();
        }
    }

    public void j(Boolean bool) {
        if (this.e == null) {
            this.a.a(this, bool);
        } else {
            bool.booleanValue();
            this.e.setVisibility(4);
        }
    }

    public void k() {
        a_();
        fr.a(this.n);
        j();
        removeAllViews();
        this.p = null;
    }

    public void l() {
    }

    public void m() {
        if (this.g == null) {
            this.a.a(this, new Object[0]);
        } else {
            this.g.b();
        }
    }

    public void n() {
        if (!this.r || this.d == null) {
            return;
        }
        a(this.d);
        if (this.a != null) {
            this.a.a();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        try {
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                if (childAt != null) {
                    if (childAt.getLayoutParams() instanceof a) {
                        a(childAt, (a) childAt.getLayoutParams());
                    } else {
                        a(childAt, childAt.getLayoutParams());
                    }
                }
            }
            if (this.e != null) {
                this.e.d();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
