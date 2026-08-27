package me.panpf.sketch.viewfun;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public abstract class ViewFunction {
    public void onAttachedToWindow() {
    }

    public boolean onDetachedFromWindow() {
        return false;
    }

    public boolean onDisplayCanceled(@NonNull CancelCause cancelCause) {
        return false;
    }

    public boolean onDisplayCompleted(@NonNull Drawable drawable, @NonNull ImageFrom imageFrom, @NonNull ImageAttrs imageAttrs) {
        return false;
    }

    public boolean onDisplayError(@NonNull ErrorCause errorCause) {
        return false;
    }

    public boolean onDisplayStarted() {
        return false;
    }

    public void onDraw(@NonNull Canvas canvas) {
    }

    public boolean onDrawableChanged(@NonNull String str, @Nullable Drawable drawable, @Nullable Drawable drawable2) {
        return false;
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public boolean onReadyDisplay(@Nullable UriModel uriModel) {
        return false;
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        return false;
    }

    public boolean onUpdateDownloadProgress(int i, int i2) {
        return false;
    }
}
