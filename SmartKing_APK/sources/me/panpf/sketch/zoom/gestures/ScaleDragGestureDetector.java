package me.panpf.sketch.zoom.gestures;

import android.view.MotionEvent;

/* loaded from: classes2.dex */
public interface ScaleDragGestureDetector {
    boolean isDragging();

    boolean isScaling();

    boolean onTouchEvent(MotionEvent motionEvent);

    void setActionListener(ActionListener actionListener);

    void setOnGestureListener(OnScaleDragGestureListener onScaleDragGestureListener);
}
