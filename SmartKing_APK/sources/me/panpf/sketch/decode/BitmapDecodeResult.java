package me.panpf.sketch.decode;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public class BitmapDecodeResult implements DecodeResult {
    private boolean banProcess;
    private Bitmap bitmap;
    private ImageAttrs imageAttrs;
    private ImageFrom imageFrom;
    private boolean processed;

    public BitmapDecodeResult(@NonNull ImageAttrs imageAttrs, @NonNull Bitmap bitmap) {
        this.imageAttrs = imageAttrs;
        this.bitmap = bitmap;
    }

    @NonNull
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public ImageAttrs getImageAttrs() {
        return this.imageAttrs;
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public ImageFrom getImageFrom() {
        return this.imageFrom;
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public boolean isBanProcess() {
        return this.banProcess;
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public boolean isProcessed() {
        return this.processed;
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public void recycle(BitmapPool bitmapPool) {
        if (this.bitmap != null) {
            BitmapPoolUtils.freeBitmapToPool(this.bitmap, bitmapPool);
        }
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public BitmapDecodeResult setBanProcess(boolean z) {
        this.banProcess = z;
        return this;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public void setImageFrom(ImageFrom imageFrom) {
        this.imageFrom = imageFrom;
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public BitmapDecodeResult setProcessed(boolean z) {
        this.processed = z;
        return this;
    }
}
