package me.panpf.sketch.decode;

import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public interface DecodeResult {
    ImageAttrs getImageAttrs();

    ImageFrom getImageFrom();

    boolean isBanProcess();

    boolean isProcessed();

    void recycle(BitmapPool bitmapPool);

    DecodeResult setBanProcess(boolean z);

    void setImageFrom(ImageFrom imageFrom);

    DecodeResult setProcessed(boolean z);
}
