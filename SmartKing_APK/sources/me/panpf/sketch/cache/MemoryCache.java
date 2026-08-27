package me.panpf.sketch.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.drawable.SketchRefBitmap;

/* loaded from: classes2.dex */
public interface MemoryCache {
    void clear();

    void close();

    @Nullable
    SketchRefBitmap get(@NonNull String str);

    long getMaxSize();

    long getSize();

    boolean isClosed();

    boolean isDisabled();

    void put(@NonNull String str, @NonNull SketchRefBitmap sketchRefBitmap);

    @Nullable
    SketchRefBitmap remove(@NonNull String str);

    void setDisabled(boolean z);

    void trimMemory(int i);
}
