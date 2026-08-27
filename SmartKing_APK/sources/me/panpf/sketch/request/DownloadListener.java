package me.panpf.sketch.request;

import android.support.annotation.NonNull;

/* loaded from: classes2.dex */
public interface DownloadListener extends Listener {
    void onCompleted(@NonNull DownloadResult downloadResult);

    @Override // me.panpf.sketch.request.Listener
    void onStarted();
}
