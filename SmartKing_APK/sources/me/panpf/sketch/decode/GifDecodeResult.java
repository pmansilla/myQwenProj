package me.panpf.sketch.decode;

import android.support.annotation.NonNull;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public class GifDecodeResult implements DecodeResult {
    private boolean banProcess;
    private SketchGifDrawable gifDrawable;
    private ImageAttrs imageAttrs;
    private ImageFrom imageFrom;
    private boolean processed;

    public GifDecodeResult(@NonNull ImageAttrs imageAttrs, @NonNull SketchGifDrawable sketchGifDrawable) {
        this.imageAttrs = imageAttrs;
        this.gifDrawable = sketchGifDrawable;
    }

    @NonNull
    public SketchGifDrawable getGifDrawable() {
        return this.gifDrawable;
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
        if (this.gifDrawable != null) {
            this.gifDrawable.recycle();
        }
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public GifDecodeResult setBanProcess(boolean z) {
        this.banProcess = z;
        return this;
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public void setImageFrom(ImageFrom imageFrom) {
        this.imageFrom = imageFrom;
    }

    @Override // me.panpf.sketch.decode.DecodeResult
    public GifDecodeResult setProcessed(boolean z) {
        this.processed = z;
        return this;
    }
}
