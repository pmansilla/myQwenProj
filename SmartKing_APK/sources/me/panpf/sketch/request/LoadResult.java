package me.panpf.sketch.request;

import android.graphics.Bitmap;
import me.panpf.sketch.decode.DecodeResult;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.drawable.SketchGifDrawable;

/* loaded from: classes2.dex */
public class LoadResult {
    private Bitmap bitmap;
    private SketchGifDrawable gifDrawable;
    private ImageAttrs imageAttrs;
    private ImageFrom imageFrom;

    public LoadResult(Bitmap bitmap, DecodeResult decodeResult) {
        this.bitmap = bitmap;
        this.imageAttrs = decodeResult.getImageAttrs();
        this.imageFrom = decodeResult.getImageFrom();
    }

    public LoadResult(SketchGifDrawable sketchGifDrawable, DecodeResult decodeResult) {
        this.gifDrawable = sketchGifDrawable;
        this.imageAttrs = decodeResult.getImageAttrs();
        this.imageFrom = decodeResult.getImageFrom();
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public SketchGifDrawable getGifDrawable() {
        return this.gifDrawable;
    }

    public ImageAttrs getImageAttrs() {
        return this.imageAttrs;
    }

    public ImageFrom getImageFrom() {
        return this.imageFrom;
    }
}
