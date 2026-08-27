package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;
import java.io.File;

/* compiled from: WaterMarkerView.java */
/* loaded from: classes.dex */
public class gh extends View {
    private Bitmap a;
    private Bitmap b;
    private Bitmap c;
    private Bitmap d;
    private Bitmap e;
    private Bitmap f;
    private Paint g;
    private boolean h;
    private int i;
    private int j;
    private int k;
    private int l;
    private int m;
    private int n;
    private int o;
    private int p;
    private boolean q;
    private boolean r;
    private Context s;
    private float t;
    private float u;
    private boolean v;

    /* JADX WARN: Removed duplicated region for block: B:25:0x0119 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x010f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public gh(android.content.Context r5, com.amap.api.mapcore.util.ad r6) {
        /*
            Method dump skipped, instructions count: 290
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.gh.<init>(android.content.Context, com.amap.api.mapcore.util.ad):void");
    }

    private void f() {
        switch (this.n) {
            case 0:
                h();
                break;
            case 2:
                g();
                break;
        }
        this.l = this.o;
        this.m = (getHeight() - this.p) - this.i;
        if (this.l < 0) {
            this.l = 0;
        }
        if (this.m < 0) {
            this.m = 0;
        }
    }

    private void g() {
        if (this.v) {
            this.o = (int) (getWidth() * this.t);
        } else {
            this.o = (int) ((getWidth() * this.t) - this.j);
        }
        this.p = (int) (getHeight() * this.u);
    }

    private void h() {
        if (this.k == 1) {
            this.o = (getWidth() - this.j) / 2;
        } else if (this.k == 2) {
            this.o = (getWidth() - this.j) - 10;
        } else {
            this.o = 10;
        }
        this.p = 8;
    }

    public void a() {
        try {
            if (this.a != null) {
                this.a.recycle();
            }
            if (this.b != null) {
                this.b.recycle();
            }
            this.a = null;
            this.b = null;
            if (this.e != null) {
                this.e.recycle();
                this.e = null;
            }
            if (this.f != null) {
                this.f.recycle();
                this.f = null;
            }
            if (this.c != null) {
                this.c.recycle();
            }
            this.c = null;
            if (this.d != null) {
                this.d.recycle();
            }
            this.d = null;
            this.g = null;
        } catch (Throwable th) {
            ic.c(th, "WaterMarkerView", "destory");
            th.printStackTrace();
        }
    }

    public void a(int i) {
        this.n = 0;
        this.k = i;
        d();
    }

    public void a(int i, float f) {
        this.n = 2;
        float max = Math.max(0.0f, Math.min(f, 1.0f));
        switch (i) {
            case 0:
                this.t = max;
                this.v = true;
                break;
            case 1:
                this.t = 1.0f - max;
                this.v = false;
                break;
            case 2:
                this.u = 1.0f - max;
                break;
        }
        d();
    }

    public void a(String str, int i) {
        try {
            if (new File(str).exists()) {
                if (i == 0) {
                    Bitmap bitmap = this.c;
                    this.e = BitmapFactory.decodeFile(str);
                    this.c = fr.a(this.e, w.a);
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                } else if (i == 1) {
                    Bitmap bitmap2 = this.d;
                    this.e = BitmapFactory.decodeFile(str);
                    this.d = fr.a(this.e, w.a);
                    if (bitmap2 != null && !bitmap2.isRecycled()) {
                        bitmap2.recycle();
                    }
                }
            }
        } catch (Throwable th) {
            ic.c(th, "WaterMarkerView", "create");
            th.printStackTrace();
        }
    }

    public void a(boolean z) {
        try {
            this.h = z;
            if (z) {
                this.g.setColor(-1);
            } else {
                this.g.setColor(-16777216);
            }
        } catch (Throwable th) {
            ic.c(th, "WaterMarkerView", "changeBitmap");
            th.printStackTrace();
        }
    }

    public Bitmap b() {
        return this.h ? (!this.r || this.d == null) ? this.b : this.d : (!this.r || this.c == null) ? this.a : this.c;
    }

    public void b(int i) {
        this.n = 1;
        this.p = i;
        d();
    }

    public void b(boolean z) {
        if (this.r != z) {
            this.r = z;
            if (!z) {
                this.j = this.a.getWidth();
                this.i = this.a.getHeight();
            } else {
                if (this.h) {
                    if (this.d != null) {
                        this.j = this.d.getWidth();
                        this.i = this.d.getHeight();
                        return;
                    }
                    return;
                }
                if (this.c != null) {
                    this.j = this.c.getWidth();
                    this.i = this.c.getHeight();
                }
            }
        }
    }

    public Point c() {
        return new Point(this.l, this.m - 2);
    }

    public void c(int i) {
        this.n = 1;
        this.o = i;
        d();
    }

    public float d(int i) {
        switch (i) {
            case 0:
                return this.t;
            case 1:
                return 1.0f - this.t;
            case 2:
                return 1.0f - this.u;
            default:
                return 0.0f;
        }
    }

    public void d() {
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        f();
        postInvalidate();
    }

    public boolean e() {
        return this.h;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        try {
            if (getWidth() == 0 || getHeight() == 0 || this.b == null) {
                return;
            }
            if (!this.q) {
                f();
                this.q = true;
            }
            canvas.drawBitmap(b(), this.l, this.m, this.g);
        } catch (Throwable th) {
            ic.c(th, "WaterMarkerView", "onDraw");
            th.printStackTrace();
        }
    }
}
