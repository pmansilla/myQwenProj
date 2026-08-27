package me.panpf.sketch.zoom.gestures;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/* loaded from: classes2.dex */
public class CupcakeScaleDragGestureDetector implements ScaleDragGestureDetector {
    private static final String NAME = "CupcakeGestureDetector";
    protected ActionListener actionListener;
    private boolean mIsDragging;
    protected float mLastTouchX;
    protected float mLastTouchY;
    protected OnScaleDragGestureListener mListener;
    protected final float mMinimumVelocity;
    protected final float mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public CupcakeScaleDragGestureDetector(Context context) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    }

    protected float getActiveX(MotionEvent motionEvent) {
        return motionEvent.getX();
    }

    protected float getActiveY(MotionEvent motionEvent) {
        return motionEvent.getY();
    }

    @Override // me.panpf.sketch.zoom.gestures.ScaleDragGestureDetector
    public boolean isDragging() {
        return this.mIsDragging;
    }

    @Override // me.panpf.sketch.zoom.gestures.ScaleDragGestureDetector
    public boolean isScaling() {
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00ee, code lost:
    
        return true;
     */
    @Override // me.panpf.sketch.zoom.gestures.ScaleDragGestureDetector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r12) {
        /*
            Method dump skipped, instructions count: 252
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: me.panpf.sketch.zoom.gestures.CupcakeScaleDragGestureDetector.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // me.panpf.sketch.zoom.gestures.ScaleDragGestureDetector
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override // me.panpf.sketch.zoom.gestures.ScaleDragGestureDetector
    public void setOnGestureListener(OnScaleDragGestureListener onScaleDragGestureListener) {
        this.mListener = onScaleDragGestureListener;
    }
}
