package me.panpf.sketch.zoom.gestures;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

@TargetApi(8)
/* loaded from: classes2.dex */
public class FroyoScaleDragGestureDetector extends EclairScaleDragGestureDetector {
    protected final ScaleGestureDetector mDetector;

    public FroyoScaleDragGestureDetector(Context context) {
        super(context);
        this.mDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() { // from class: me.panpf.sketch.zoom.gestures.FroyoScaleDragGestureDetector.1
            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                float scaleFactor = scaleGestureDetector.getScaleFactor();
                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }
                FroyoScaleDragGestureDetector.this.mListener.onScale(scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                return true;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return FroyoScaleDragGestureDetector.this.mListener.onScaleBegin();
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
                FroyoScaleDragGestureDetector.this.mListener.onScaleEnd();
            }
        });
    }

    @Override // me.panpf.sketch.zoom.gestures.CupcakeScaleDragGestureDetector, me.panpf.sketch.zoom.gestures.ScaleDragGestureDetector
    public boolean isScaling() {
        return this.mDetector.isInProgress();
    }

    @Override // me.panpf.sketch.zoom.gestures.EclairScaleDragGestureDetector, me.panpf.sketch.zoom.gestures.CupcakeScaleDragGestureDetector, me.panpf.sketch.zoom.gestures.ScaleDragGestureDetector
    public boolean onTouchEvent(MotionEvent motionEvent) {
        try {
            this.mDetector.onTouchEvent(motionEvent);
            return super.onTouchEvent(motionEvent);
        } catch (IllegalArgumentException unused) {
            return true;
        }
    }
}
