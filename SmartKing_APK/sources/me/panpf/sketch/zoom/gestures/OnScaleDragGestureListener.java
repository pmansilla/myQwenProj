package me.panpf.sketch.zoom.gestures;

/* loaded from: classes2.dex */
public interface OnScaleDragGestureListener {
    void onDrag(float f, float f2);

    void onFling(float f, float f2, float f3, float f4);

    void onScale(float f, float f2, float f3);

    boolean onScaleBegin();

    void onScaleEnd();
}
