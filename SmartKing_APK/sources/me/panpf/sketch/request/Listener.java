package me.panpf.sketch.request;

import android.support.annotation.NonNull;

/* loaded from: classes2.dex */
public interface Listener {
    void onCanceled(@NonNull CancelCause cancelCause);

    void onError(@NonNull ErrorCause errorCause);

    void onStarted();
}
