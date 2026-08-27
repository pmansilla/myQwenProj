package me.panpf.sketch.zoom.gestures;

import android.content.Context;
import android.os.Build;

/* loaded from: classes2.dex */
public class ScaleDragGestureDetectorCompat {
    public static ScaleDragGestureDetector newInstance(Context context, OnScaleDragGestureListener onScaleDragGestureListener) {
        int i = Build.VERSION.SDK_INT;
        ScaleDragGestureDetector froyoScaleDragGestureDetector = i >= 8 ? new FroyoScaleDragGestureDetector(context) : i >= 5 ? new EclairScaleDragGestureDetector(context) : new CupcakeScaleDragGestureDetector(context);
        froyoScaleDragGestureDetector.setOnGestureListener(onScaleDragGestureListener);
        return froyoScaleDragGestureDetector;
    }
}
