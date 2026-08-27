package com.czw.smartkit.absimpl;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.DisplayListener;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes.dex */
public abstract class DisplayListenerImpl implements DisplayListener {
    @Override // me.panpf.sketch.request.Listener
    public void onCanceled(@NonNull CancelCause cancelCause) {
    }

    @Override // me.panpf.sketch.request.DisplayListener
    public void onCompleted(@NonNull Drawable drawable, @NonNull ImageFrom imageFrom, @NonNull ImageAttrs imageAttrs) {
    }

    @Override // me.panpf.sketch.request.Listener
    public void onError(@NonNull ErrorCause errorCause) {
    }

    @Override // me.panpf.sketch.request.DisplayListener, me.panpf.sketch.request.Listener
    public void onStarted() {
    }
}
