package me.panpf.sketch.zoom.gestures;

import android.view.MotionEvent;

/* loaded from: classes2.dex */
public interface ActionListener {
    void onActionCancel(MotionEvent motionEvent);

    void onActionDown(MotionEvent motionEvent);

    void onActionUp(MotionEvent motionEvent);
}
