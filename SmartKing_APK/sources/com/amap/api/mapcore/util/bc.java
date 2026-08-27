package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/* compiled from: ScaleGestureDetector.java */
/* loaded from: classes.dex */
public class bc {
    private final Context a;
    private final a b;
    private boolean c;
    private MotionEvent d;
    private MotionEvent e;
    private float f;
    private float g;
    private float h;
    private float i;
    private float j;
    private float k;
    private float l;
    private float m;
    private float n;
    private float o;
    private float p;
    private long q;
    private final float r;
    private float s;
    private float t;
    private boolean u;
    private boolean v;
    private int w;
    private int x;
    private boolean y;

    /* compiled from: ScaleGestureDetector.java */
    /* loaded from: classes.dex */
    public interface a {
        boolean a(bc bcVar);

        boolean b(bc bcVar);

        void c(bc bcVar);
    }

    public bc(Context context, a aVar) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.a = context;
        this.b = aVar;
        this.r = viewConfiguration.getScaledEdgeSlop();
    }

    private static float a(MotionEvent motionEvent, int i) {
        if (i < 0) {
            return Float.MIN_VALUE;
        }
        if (i == 0) {
            return motionEvent.getRawX();
        }
        return motionEvent.getX(i) + (motionEvent.getRawX() - motionEvent.getX());
    }

    private int a(MotionEvent motionEvent, int i, int i2) {
        int pointerCount = motionEvent.getPointerCount();
        int findPointerIndex = motionEvent.findPointerIndex(i);
        for (int i3 = 0; i3 < pointerCount; i3++) {
            if (i3 != i2 && i3 != findPointerIndex) {
                float f = this.r;
                float f2 = this.s;
                float f3 = this.t;
                float a2 = a(motionEvent, i3);
                float b = b(motionEvent, i3);
                if (a2 >= f && b >= f && a2 <= f2 && b <= f3) {
                    return i3;
                }
            }
        }
        return -1;
    }

    private static float b(MotionEvent motionEvent, int i) {
        if (i < 0) {
            return Float.MIN_VALUE;
        }
        if (i == 0) {
            return motionEvent.getRawY();
        }
        return motionEvent.getY(i) + (motionEvent.getRawY() - motionEvent.getY());
    }

    private void b(MotionEvent motionEvent) {
        if (this.e != null) {
            this.e.recycle();
        }
        this.e = MotionEvent.obtain(motionEvent);
        this.l = -1.0f;
        this.m = -1.0f;
        this.n = -1.0f;
        MotionEvent motionEvent2 = this.d;
        int findPointerIndex = motionEvent2.findPointerIndex(this.w);
        int findPointerIndex2 = motionEvent2.findPointerIndex(this.x);
        int findPointerIndex3 = motionEvent.findPointerIndex(this.w);
        int findPointerIndex4 = motionEvent.findPointerIndex(this.x);
        if (findPointerIndex < 0 || findPointerIndex2 < 0 || findPointerIndex3 < 0 || findPointerIndex4 < 0) {
            this.v = true;
            if (this.c) {
                this.b.c(this);
                return;
            }
            return;
        }
        float x = motionEvent2.getX(findPointerIndex);
        float y = motionEvent2.getY(findPointerIndex);
        float x2 = motionEvent2.getX(findPointerIndex2);
        float y2 = motionEvent2.getY(findPointerIndex2);
        float x3 = motionEvent.getX(findPointerIndex3);
        float y3 = motionEvent.getY(findPointerIndex3);
        float x4 = motionEvent.getX(findPointerIndex4) - x3;
        float y4 = motionEvent.getY(findPointerIndex4) - y3;
        this.h = x2 - x;
        this.i = y2 - y;
        this.j = x4;
        this.k = y4;
        this.f = x3 + (x4 * 0.5f);
        this.g = y3 + (y4 * 0.5f);
        this.q = motionEvent.getEventTime() - motionEvent2.getEventTime();
        this.o = motionEvent.getPressure(findPointerIndex3) + motionEvent.getPressure(findPointerIndex4);
        this.p = motionEvent2.getPressure(findPointerIndex) + motionEvent2.getPressure(findPointerIndex2);
    }

    private void l() {
        if (this.d != null) {
            this.d.recycle();
            this.d = null;
        }
        if (this.e != null) {
            this.e.recycle();
            this.e = null;
        }
        this.u = false;
        this.c = false;
        this.w = -1;
        this.x = -1;
        this.v = false;
    }

    public MotionEvent a() {
        return this.e;
    }

    public boolean a(MotionEvent motionEvent) {
        int i;
        int i2;
        int i3;
        int i4;
        int a2;
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            l();
        }
        boolean z = false;
        if (this.v) {
            return false;
        }
        if (this.c) {
            switch (action) {
                case 1:
                    l();
                    return true;
                case 2:
                    b(motionEvent);
                    if (this.o / this.p <= 0.67f || !this.b.a(this)) {
                        return true;
                    }
                    this.d.recycle();
                    this.d = MotionEvent.obtain(motionEvent);
                    return true;
                case 3:
                    this.b.c(this);
                    l();
                    return true;
                case 4:
                default:
                    return true;
                case 5:
                    this.b.c(this);
                    int i5 = this.w;
                    int i6 = this.x;
                    l();
                    this.d = MotionEvent.obtain(motionEvent);
                    if (!this.y) {
                        i5 = i6;
                    }
                    this.w = i5;
                    if (Build.VERSION.SDK_INT >= 8) {
                        this.x = motionEvent.getPointerId(motionEvent.getActionIndex());
                    } else {
                        this.x = motionEvent.getPointerId(1);
                    }
                    this.y = false;
                    int findPointerIndex = motionEvent.findPointerIndex(this.w);
                    if (findPointerIndex < 0 || this.w == this.x) {
                        this.w = motionEvent.getPointerId(a(motionEvent, this.w != this.x ? this.x : -1, findPointerIndex));
                    }
                    b(motionEvent);
                    this.c = this.b.b(this);
                    return true;
                case 6:
                    int pointerCount = motionEvent.getPointerCount();
                    int actionIndex = Build.VERSION.SDK_INT >= 8 ? motionEvent.getActionIndex() : 0;
                    int pointerId = motionEvent.getPointerId(actionIndex);
                    if (pointerCount <= 2) {
                        z = true;
                    } else if (pointerId == this.w) {
                        int a3 = a(motionEvent, this.x, actionIndex);
                        if (a3 >= 0) {
                            this.b.c(this);
                            this.w = motionEvent.getPointerId(a3);
                            this.y = true;
                            this.d = MotionEvent.obtain(motionEvent);
                            b(motionEvent);
                            this.c = this.b.b(this);
                            this.d.recycle();
                            this.d = MotionEvent.obtain(motionEvent);
                            b(motionEvent);
                        }
                        z = true;
                        this.d.recycle();
                        this.d = MotionEvent.obtain(motionEvent);
                        b(motionEvent);
                    } else {
                        if (pointerId == this.x) {
                            int a4 = a(motionEvent, this.w, actionIndex);
                            if (a4 >= 0) {
                                this.b.c(this);
                                this.x = motionEvent.getPointerId(a4);
                                this.y = false;
                                this.d = MotionEvent.obtain(motionEvent);
                                b(motionEvent);
                                this.c = this.b.b(this);
                            }
                            z = true;
                        }
                        this.d.recycle();
                        this.d = MotionEvent.obtain(motionEvent);
                        b(motionEvent);
                    }
                    if (!z) {
                        return true;
                    }
                    b(motionEvent);
                    int i7 = pointerId == this.w ? this.x : this.w;
                    int findPointerIndex2 = motionEvent.findPointerIndex(i7);
                    this.f = motionEvent.getX(findPointerIndex2);
                    this.g = motionEvent.getY(findPointerIndex2);
                    this.b.c(this);
                    l();
                    this.w = i7;
                    this.y = true;
                    return true;
            }
        }
        switch (action) {
            case 0:
                this.w = motionEvent.getPointerId(0);
                this.y = true;
                return true;
            case 1:
                l();
                return true;
            case 2:
                if (!this.u) {
                    return true;
                }
                float f = this.r;
                float f2 = this.s;
                float f3 = this.t;
                int findPointerIndex3 = motionEvent.findPointerIndex(this.w);
                int findPointerIndex4 = motionEvent.findPointerIndex(this.x);
                float a5 = a(motionEvent, findPointerIndex3);
                float b = b(motionEvent, findPointerIndex3);
                float a6 = a(motionEvent, findPointerIndex4);
                float b2 = b(motionEvent, findPointerIndex4);
                boolean z2 = a5 < f || b < f || a5 > f2 || b > f3;
                boolean z3 = a6 < f || b2 < f || a6 > f2 || b2 > f3;
                if (!z2 || (i = a(motionEvent, this.x, findPointerIndex3)) < 0) {
                    i = findPointerIndex3;
                } else {
                    this.w = motionEvent.getPointerId(i);
                    a(motionEvent, i);
                    b(motionEvent, i);
                    z2 = false;
                }
                if (!z3 || (i2 = a(motionEvent, this.w, findPointerIndex4)) < 0) {
                    i2 = findPointerIndex4;
                } else {
                    this.x = motionEvent.getPointerId(i2);
                    a(motionEvent, i2);
                    b(motionEvent, i2);
                    z3 = false;
                }
                if (z2 && z3) {
                    this.f = -1.0f;
                    this.g = -1.0f;
                    return true;
                }
                if (z2) {
                    this.f = motionEvent.getX(i2);
                    this.g = motionEvent.getY(i2);
                    return true;
                }
                if (z3) {
                    this.f = motionEvent.getX(i);
                    this.g = motionEvent.getY(i);
                    return true;
                }
                this.u = false;
                this.c = this.b.b(this);
                return true;
            case 3:
            case 4:
            default:
                return true;
            case 5:
                DisplayMetrics displayMetrics = this.a.getResources().getDisplayMetrics();
                this.s = displayMetrics.widthPixels - this.r;
                this.t = displayMetrics.heightPixels - this.r;
                if (this.d != null) {
                    this.d.recycle();
                }
                this.d = MotionEvent.obtain(motionEvent);
                this.q = 0L;
                if (Build.VERSION.SDK_INT >= 8) {
                    i3 = motionEvent.getActionIndex();
                    i4 = motionEvent.findPointerIndex(this.w);
                    this.x = motionEvent.getPointerId(i3);
                    if (i4 < 0 || i4 == i3) {
                        i4 = a(motionEvent, i4 != i3 ? this.x : -1, i4);
                        this.w = motionEvent.getPointerId(i4);
                    }
                } else if (motionEvent.getPointerCount() > 0) {
                    i3 = motionEvent.findPointerIndex(1);
                    i4 = motionEvent.findPointerIndex(this.w);
                    this.x = motionEvent.getPointerId(i3);
                } else {
                    i3 = 0;
                    i4 = 0;
                }
                this.y = false;
                b(motionEvent);
                float f4 = this.r;
                float f5 = this.s;
                float f6 = this.t;
                float a7 = a(motionEvent, i4);
                float b3 = b(motionEvent, i4);
                float a8 = a(motionEvent, i3);
                float b4 = b(motionEvent, i3);
                boolean z4 = a7 < f4 || b3 < f4 || a7 > f5 || b3 > f6;
                boolean z5 = a8 < f4 || b4 < f4 || a8 > f5 || b4 > f6;
                if (z4 && z5) {
                    this.f = -1.0f;
                    this.g = -1.0f;
                    this.u = true;
                    return true;
                }
                if (z4) {
                    this.f = motionEvent.getX(i3);
                    this.g = motionEvent.getY(i3);
                    this.u = true;
                    return true;
                }
                if (!z5) {
                    this.u = false;
                    this.c = this.b.b(this);
                    return true;
                }
                this.f = motionEvent.getX(i4);
                this.g = motionEvent.getY(i4);
                this.u = true;
                return true;
            case 6:
                if (!this.u) {
                    return true;
                }
                int pointerCount2 = motionEvent.getPointerCount();
                int actionIndex2 = Build.VERSION.SDK_INT >= 8 ? motionEvent.getActionIndex() : 0;
                int pointerId2 = motionEvent.getPointerId(actionIndex2);
                if (pointerCount2 > 2) {
                    if (pointerId2 == this.w) {
                        int a9 = a(motionEvent, this.x, actionIndex2);
                        if (a9 < 0) {
                            return true;
                        }
                        this.w = motionEvent.getPointerId(a9);
                        return true;
                    }
                    if (pointerId2 != this.x || (a2 = a(motionEvent, this.w, actionIndex2)) < 0) {
                        return true;
                    }
                    this.x = motionEvent.getPointerId(a2);
                    return true;
                }
                int findPointerIndex5 = motionEvent.findPointerIndex(pointerId2 == this.w ? this.x : this.w);
                if (findPointerIndex5 < 0) {
                    this.v = true;
                    if (this.c) {
                        this.b.c(this);
                    }
                    return false;
                }
                this.w = motionEvent.getPointerId(findPointerIndex5);
                this.y = true;
                this.x = -1;
                this.f = motionEvent.getX(findPointerIndex5);
                this.g = motionEvent.getY(findPointerIndex5);
                return true;
        }
    }

    public float b() {
        return this.f;
    }

    public float c() {
        return this.g;
    }

    public float d() {
        if (this.l == -1.0f) {
            float f = this.j;
            float f2 = this.k;
            this.l = (float) Math.sqrt((f * f) + (f2 * f2));
        }
        return this.l;
    }

    public float e() {
        return this.j;
    }

    public float f() {
        return this.k;
    }

    public float g() {
        if (this.m == -1.0f) {
            float f = this.h;
            float f2 = this.i;
            this.m = (float) Math.sqrt((f * f) + (f2 * f2));
        }
        return this.m;
    }

    public float h() {
        return this.h;
    }

    public float i() {
        return this.i;
    }

    public float j() {
        if (this.n == -1.0f) {
            this.n = d() / g();
        }
        return this.n;
    }

    public long k() {
        return this.q;
    }
}
