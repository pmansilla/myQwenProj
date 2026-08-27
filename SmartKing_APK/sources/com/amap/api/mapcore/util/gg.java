package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import basecamera.module.lib.JCameraView;
import com.autonavi.amap.mapcore.DPoint;
import com.autonavi.amap.mapcore.IPoint;
import com.autonavi.amap.mapcore.VirtualEarthProjection;
import com.tencent.bugly.BuglyStrategy;

/* compiled from: ScaleView.java */
/* loaded from: classes.dex */
public class gg extends View {
    private String a;
    private int b;
    private ad c;
    private Paint d;
    private Paint e;
    private Rect f;
    private IPoint g;
    private float h;
    private final int[] i;

    public gg(Context context, ad adVar) {
        super(context);
        this.a = "";
        this.b = 0;
        this.h = 0.0f;
        this.i = new int[]{10000000, 5000000, JCameraView.MEDIA_QUALITY_HIGH, 1000000, 500000, JCameraView.MEDIA_QUALITY_DESPAIR, 100000, 50000, BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 25, 10, 5};
        this.c = adVar;
        this.d = new Paint();
        this.f = new Rect();
        this.d.setAntiAlias(true);
        this.d.setColor(-16777216);
        this.d.setStrokeWidth(w.a * 2.0f);
        this.d.setStyle(Paint.Style.STROKE);
        this.e = new Paint();
        this.e.setAntiAlias(true);
        this.e.setColor(-16777216);
        this.e.setTextSize(w.a * 20.0f);
        this.h = fl.a(context, 1.0f);
        this.g = new IPoint();
    }

    public void a() {
        this.d = null;
        this.e = null;
        this.f = null;
        this.a = null;
        this.g = null;
    }

    public void a(int i) {
        this.b = i;
    }

    public void a(String str) {
        this.a = str;
    }

    public void a(boolean z) {
        if (z) {
            setVisibility(0);
            b();
        } else {
            a("");
            a(0);
            setVisibility(8);
        }
    }

    public void b() {
        if (this.c == null) {
            return;
        }
        try {
            float a = this.c.a(1);
            this.c.a(1, this.g);
            if (this.g == null) {
                return;
            }
            DPoint pixelsToLatLong = VirtualEarthProjection.pixelsToLatLong(this.g.x, this.g.y, 20);
            float t = this.c.t();
            double cos = (float) ((((Math.cos((pixelsToLatLong.y * 3.141592653589793d) / 180.0d) * 2.0d) * 3.141592653589793d) * 6378137.0d) / (Math.pow(2.0d, a) * 256.0d));
            int i = (int) a;
            double d = this.i[i];
            double d2 = t;
            Double.isNaN(cos);
            Double.isNaN(d2);
            Double.isNaN(d);
            int i2 = (int) (d / (cos * d2));
            String a2 = fr.a(this.i[i]);
            a(i2);
            a(a2);
            pixelsToLatLong.recycle();
            invalidate();
        } catch (Throwable th) {
            ic.c(th, "AMapDelegateImpGLSurfaceView", "changeScaleState");
            th.printStackTrace();
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Point l;
        if (this.a == null || "".equals(this.a) || this.b == 0 || (l = this.c.l()) == null) {
            return;
        }
        this.e.getTextBounds(this.a, 0, this.a.length(), this.f);
        int i = l.x;
        int height = (l.y - this.f.height()) + 5;
        canvas.drawText(this.a, ((this.b - this.f.width()) / 2) + i, height, this.e);
        float f = i;
        float height2 = height + (this.f.height() - 5);
        canvas.drawLine(f, height2 - (this.h * 2.0f), f, height2 + w.a, this.d);
        canvas.drawLine(f, height2, this.b + i, height2, this.d);
        canvas.drawLine(this.b + i, height2 - (this.h * 2.0f), i + this.b, height2 + w.a, this.d);
    }
}
