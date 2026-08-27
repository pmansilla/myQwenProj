package me.panpf.sketch.cache.recycle;

import android.graphics.Bitmap;
import me.panpf.sketch.Key;

/* loaded from: classes2.dex */
public interface LruPoolStrategy extends Key {
    Bitmap get(int i, int i2, Bitmap.Config config);

    int getSize(Bitmap bitmap);

    String logBitmap(int i, int i2, Bitmap.Config config);

    String logBitmap(Bitmap bitmap);

    void put(Bitmap bitmap);

    Bitmap removeLast();
}
