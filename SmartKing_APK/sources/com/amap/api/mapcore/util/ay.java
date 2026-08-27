package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/* compiled from: AbstractTwoFingerGestureDetector.java */
/* loaded from: classes.dex */
public abstract class ay extends az {
    protected float a;
    protected float b;
    protected float c;
    protected float d;
    private final float l;
    private float m;
    private float n;
    private float o;
    private float p;
    private float q;
    private float r;
    private float s;
    private float t;

    public ay(Context context) {
        super(context);
        this.q = 0.0f;
        this.r = 0.0f;
        this.s = 0.0f;
        this.t = 0.0f;
        this.l = ViewConfiguration.get(context).getScaledEdgeSlop();
    }

    protected static float a(MotionEvent motionEvent, int i) {
        float x = motionEvent.getX() - motionEvent.getRawX();
        if (i < motionEvent.getPointerCount()) {
            return motionEvent.getX(i) + x;
        }
        return 0.0f;
    }

    protected static float a(MotionEvent motionEvent, int i, int i2) {
        float y = (i2 + motionEvent.getY()) - motionEvent.getRawY();
        if (i < motionEvent.getPointerCount()) {
            return motionEvent.getY(i) + y;
        }
        return 0.0f;
    }

    public PointF a(int i) {
        return i == 0 ? new PointF(this.q, this.r) : new PointF(this.s, this.t);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.az
    public void a(MotionEvent motionEvent) {
        super.a(motionEvent);
        MotionEvent motionEvent2 = this.g;
        int pointerCount = this.g.getPointerCount();
        int pointerCount2 = motionEvent.getPointerCount();
        if (pointerCount2 == 2 && pointerCount2 == pointerCount) {
            this.o = -1.0f;
            this.p = -1.0f;
            float x = motionEvent2.getX(0);
            float y = motionEvent2.getY(0);
            float x2 = motionEvent2.getX(1);
            float y2 = motionEvent2.getY(1);
            this.a = x2 - x;
            this.b = y2 - y;
            float x3 = motionEvent.getX(0);
            float y3 = motionEvent.getY(0);
            float x4 = motionEvent.getX(1);
            float y4 = motionEvent.getY(1);
            this.c = x4 - x3;
            this.d = y4 - y3;
            this.q = x3 - x;
            this.r = y3 - y;
            this.s = x4 - x2;
            this.t = y4 - y2;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean b(MotionEvent motionEvent, int i, int i2) {
        DisplayMetrics displayMetrics = this.e.getResources().getDisplayMetrics();
        this.m = displayMetrics.widthPixels - this.l;
        this.n = displayMetrics.heightPixels - this.l;
        float f = this.l;
        float f2 = this.m;
        float f3 = this.n;
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        float a = a(motionEvent, 1);
        float a2 = a(motionEvent, 1, i2);
        boolean z = rawX < f || rawY < f || rawX > f2 || rawY > f3;
        boolean z2 = a < f || a2 < f || a > f2 || a2 > f3;
        return (z && z2) || z || z2;
    }
}
