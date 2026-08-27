package me.panpf.sketch.request;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import me.panpf.sketch.decode.ImageAttrs;

/* loaded from: classes2.dex */
public interface DisplayListener extends Listener {
    void onCompleted(@NonNull Drawable drawable, @NonNull ImageFrom imageFrom, @NonNull ImageAttrs imageAttrs);

    @Override // me.panpf.sketch.request.Listener
    void onStarted();
}
