package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

/* compiled from: MoveGestureDetector.java */
/* loaded from: classes.dex */
public class bb extends az {
    private static final PointF a = new PointF();
    private final a b;
    private PointF c;
    private PointF d;
    private PointF l;
    private PointF m;

    /* compiled from: MoveGestureDetector.java */
    /* loaded from: classes.dex */
    public interface a {
        boolean a(bb bbVar);

        boolean b(bb bbVar);

        void c(bb bbVar);
    }

    public bb(Context context, a aVar) {
        super(context);
        this.l = new PointF();
        this.m = new PointF();
        this.b = aVar;
    }

    @Override // com.amap.api.mapcore.util.az
    protected void a(int i, MotionEvent motionEvent) {
        switch (i) {
            case 1:
            case 3:
                this.b.c(this);
                a();
                return;
            case 2:
                a(motionEvent);
                if (this.i / this.j <= 0.67f || motionEvent.getPointerCount() > 1 || !this.b.a(this)) {
                    return;
                }
                this.g.recycle();
                this.g = MotionEvent.obtain(motionEvent);
                return;
            default:
                return;
        }
    }

    @Override // com.amap.api.mapcore.util.az
    protected void a(int i, MotionEvent motionEvent, int i2, int i3) {
        if (i == 0) {
            a();
            this.g = MotionEvent.obtain(motionEvent);
            this.k = 0L;
            a(motionEvent);
            return;
        }
        if (i == 2) {
            this.f = this.b.b(this);
        } else {
            if (i != 5) {
                return;
            }
            if (this.g != null) {
                this.g.recycle();
            }
            this.g = MotionEvent.obtain(motionEvent);
            a(motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.az
    public void a(MotionEvent motionEvent) {
        super.a(motionEvent);
        MotionEvent motionEvent2 = this.g;
        this.c = b(motionEvent);
        this.d = b(motionEvent2);
        boolean z = this.g.getPointerCount() != motionEvent.getPointerCount();
        this.m = z ? a : new PointF(this.c.x - this.d.x, this.c.y - this.d.y);
        if (z) {
            this.g.recycle();
            this.g = MotionEvent.obtain(motionEvent);
        }
        this.l.x += this.m.x;
        this.l.y += this.m.y;
    }

    public PointF d() {
        return this.m;
    }
}
