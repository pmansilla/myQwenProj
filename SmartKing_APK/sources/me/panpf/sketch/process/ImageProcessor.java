package me.panpf.sketch.process;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.Key;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.request.Resize;

/* loaded from: classes2.dex */
public interface ImageProcessor extends Key {
    @NonNull
    Bitmap process(@NonNull Sketch sketch, @NonNull Bitmap bitmap, @Nullable Resize resize, boolean z);
}
