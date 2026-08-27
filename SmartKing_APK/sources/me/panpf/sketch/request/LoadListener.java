package me.panpf.sketch.request;

import android.support.annotation.NonNull;

/* loaded from: classes2.dex */
public interface LoadListener extends Listener {
    void onCompleted(@NonNull LoadResult loadResult);

    @Override // me.panpf.sketch.request.Listener
    void onStarted();
}
