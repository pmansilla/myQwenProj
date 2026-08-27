package com.amap.api.mapcore.util;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

/* compiled from: ZoomOutGestureDetectorAbstract.java */
/* loaded from: classes.dex */
public class be extends ay {
    private static final PointF n = new PointF();
    private final a l;
    private boolean m;
    private PointF o;
    private PointF p;
    private PointF q;
    private PointF r;

    /* compiled from: ZoomOutGestureDetectorAbstract.java */
    /* loaded from: classes.dex */
    public interface a {
        void a(be beVar);

        boolean b(be beVar);
    }

    /* compiled from: ZoomOutGestureDetectorAbstract.java */
    /* loaded from: classes.dex */
    public static class b implements a {
        @Override // com.amap.api.mapcore.util.be.a
        public void a(be beVar) {
        }

        @Override // com.amap.api.mapcore.util.be.a
        public boolean b(be beVar) {
            return true;
        }
    }

    public be(Context context, a aVar) {
        super(context);
        this.q = new PointF();
        this.r = new PointF();
        this.l = aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.az
    public void a() {
        super.a();
        this.m = false;
        this.q.x = 0.0f;
        this.r.x = 0.0f;
        this.q.y = 0.0f;
        this.r.y = 0.0f;
    }

    @Override // com.amap.api.mapcore.util.az
    protected void a(int i, MotionEvent motionEvent) {
        if (i == 3) {
            a();
        } else {
            if (i != 6) {
                return;
            }
            a(motionEvent);
            if (!this.m) {
                this.l.a(this);
            }
            a();
        }
    }

    @Override // com.amap.api.mapcore.util.az
    protected void a(int i, MotionEvent motionEvent, int i2, int i3) {
        if (i != 5) {
            return;
        }
        a();
        this.g = MotionEvent.obtain(motionEvent);
        this.k = 0L;
        a(motionEvent);
        this.m = b(motionEvent, i2, i3);
        if (this.m) {
            return;
        }
        this.f = this.l.b(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.amap.api.mapcore.util.ay, com.amap.api.mapcore.util.az
    public void a(MotionEvent motionEvent) {
        super.a(motionEvent);
        MotionEvent motionEvent2 = this.g;
        this.o = b(motionEvent);
        this.p = b(motionEvent2);
        this.r = this.g.getPointerCount() != motionEvent.getPointerCount() ? n : new PointF(this.o.x - this.p.x, this.o.y - this.p.y);
        this.q.x += this.r.x;
        this.q.y += this.r.y;
    }

    public float d() {
        return this.q.x;
    }

    public float e() {
        return this.q.y;
    }
}
