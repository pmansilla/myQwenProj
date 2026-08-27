package me.panpf.sketch.cache;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/* loaded from: classes2.dex */
public interface BitmapPool {
    void clear();

    void close();

    @Nullable
    Bitmap get(int i, int i2, @NonNull Bitmap.Config config);

    @Nullable
    Bitmap getDirty(int i, int i2, @NonNull Bitmap.Config config);

    int getMaxSize();

    @NonNull
    Bitmap getOrMake(int i, int i2, @NonNull Bitmap.Config config);

    int getSize();

    boolean isClosed();

    boolean isDisabled();

    boolean put(@NonNull Bitmap bitmap);

    void setDisabled(boolean z);

    void setSizeMultiplier(float f);

    void trimMemory(int i);
}
