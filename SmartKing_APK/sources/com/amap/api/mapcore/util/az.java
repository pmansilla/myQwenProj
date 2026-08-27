package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.view.MotionEvent;

/* compiled from: BaseGestureDetector.java */
/* loaded from: classes.dex */
public abstract class az {
    protected final Context e;
    protected boolean f;
    protected MotionEvent g;
    protected MotionEvent h;
    protected float i;
    protected float j;
    protected long k;

    public az(Context context) {
        this.e = context;
    }

    public static PointF b(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i = 0; i < pointerCount; i++) {
            f += motionEvent.getX(i);
            f2 += motionEvent.getY(i);
        }
        float f3 = pointerCount;
        return new PointF(f / f3, f2 / f3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a() {
        if (this.g != null) {
            this.g.recycle();
            this.g = null;
        }
        if (this.h != null) {
            this.h.recycle();
            this.h = null;
        }
        this.f = false;
    }

    protected abstract void a(int i, MotionEvent motionEvent);

    protected abstract void a(int i, MotionEvent motionEvent, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = this.g;
        if (this.h != null) {
            this.h.recycle();
            this.h = null;
        }
        this.h = MotionEvent.obtain(motionEvent);
        this.k = motionEvent.getEventTime() - motionEvent2.getEventTime();
        if (Build.VERSION.SDK_INT >= 8) {
            this.i = motionEvent.getPressure(motionEvent.getActionIndex());
            this.j = motionEvent2.getPressure(motionEvent2.getActionIndex());
        } else {
            this.i = motionEvent.getPressure(0);
            this.j = motionEvent2.getPressure(0);
        }
    }

    public long b() {
        return this.k;
    }

    public MotionEvent c() {
        return this.h;
    }

    public boolean c(MotionEvent motionEvent, int i, int i2) {
        int action = motionEvent.getAction() & 255;
        if (this.f) {
            a(action, motionEvent);
            return true;
        }
        a(action, motionEvent, i, i2);
        return true;
    }
}
