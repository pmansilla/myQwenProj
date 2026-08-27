package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

/* compiled from: HoverGestureDetectorAbstract.java */
/* loaded from: classes.dex */
public class ba extends ay {
    private static final PointF l = new PointF();
    private final a m;
    private boolean n;
    private PointF o;
    private PointF p;
    private PointF q;
    private PointF r;

    /* compiled from: HoverGestureDetectorAbstract.java */
    /* loaded from: classes.dex */
    public interface a {
        boolean a(ba baVar);

        boolean b(ba baVar);

        void c(ba baVar);
    }

    public ba(Context context, a aVar) {
        super(context);
        this.q = new PointF();
        this.r = new PointF();
        this.m = aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.az
    public void a() {
        super.a();
        this.n = false;
    }

    @Override // com.amap.api.mapcore.util.az
    protected void a(int i, MotionEvent motionEvent) {
        if (i == 6) {
            a(motionEvent);
            if (!this.n) {
                this.m.c(this);
            }
            a();
            return;
        }
        switch (i) {
            case 2:
                a(motionEvent);
                if (this.i / this.j <= 0.67f || !this.m.a(this)) {
                    return;
                }
                this.g.recycle();
                this.g = MotionEvent.obtain(motionEvent);
                return;
            case 3:
                if (!this.n) {
                    this.m.c(this);
                }
                a();
                return;
            default:
                return;
        }
    }

    @Override // com.amap.api.mapcore.util.az
    protected void a(int i, MotionEvent motionEvent, int i2, int i3) {
        if (i == 2) {
            if (this.n) {
                this.n = b(motionEvent, i2, i3);
                if (this.n) {
                    return;
                }
                this.f = this.m.b(this);
                return;
            }
            return;
        }
        switch (i) {
            case 5:
                a();
                this.g = MotionEvent.obtain(motionEvent);
                this.k = 0L;
                a(motionEvent);
                this.n = b(motionEvent, i2, i3);
                if (this.n) {
                    return;
                }
                this.f = this.m.b(this);
                return;
            case 6:
                boolean z = this.n;
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.ay, com.amap.api.mapcore.util.az
    public void a(MotionEvent motionEvent) {
        super.a(motionEvent);
        MotionEvent motionEvent2 = this.g;
        this.o = b(motionEvent);
        this.p = b(motionEvent2);
        this.r = this.g.getPointerCount() != motionEvent.getPointerCount() ? l : new PointF(this.o.x - this.p.x, this.o.y - this.p.y);
        this.q.x += this.r.x;
        this.q.y += this.r.y;
    }

    public PointF d() {
        return this.r;
    }
}
