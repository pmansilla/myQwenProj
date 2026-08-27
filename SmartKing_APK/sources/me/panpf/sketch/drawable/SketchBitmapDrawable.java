package me.panpf.sketch.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public class SketchBitmapDrawable extends BitmapDrawable implements SketchRefDrawable {
    private ImageFrom imageFrom;
    private SketchRefBitmap refBitmap;

    public SketchBitmapDrawable(SketchRefBitmap sketchRefBitmap, ImageFrom imageFrom) {
        super((Resources) null, sketchRefBitmap.getBitmap());
        if (sketchRefBitmap.isRecycled()) {
            throw new IllegalArgumentException("refBitmap recycled. " + sketchRefBitmap.getInfo());
        }
        this.refBitmap = sketchRefBitmap;
        this.imageFrom = imageFrom;
        setTargetDensity(sketchRefBitmap.getBitmap().getDensity());
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public Bitmap.Config getBitmapConfig() {
        return this.refBitmap.getBitmapConfig();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getByteCount() {
        return this.refBitmap.getByteCount();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getExifOrientation() {
        return this.refBitmap.getAttrs().getExifOrientation();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public ImageFrom getImageFrom() {
        return this.imageFrom;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getInfo() {
        return this.refBitmap.getInfo();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getKey() {
        return this.refBitmap.getKey();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getMimeType() {
        return this.refBitmap.getAttrs().getMimeType();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getOriginHeight() {
        return this.refBitmap.getAttrs().getHeight();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getOriginWidth() {
        return this.refBitmap.getAttrs().getWidth();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getUri() {
        return this.refBitmap.getUri();
    }

    @Override // me.panpf.sketch.drawable.SketchRefDrawable
    public boolean isRecycled() {
        return this.refBitmap.isRecycled();
    }

    @Override // me.panpf.sketch.drawable.SketchRefDrawable
    public void setIsDisplayed(String str, boolean z) {
        this.refBitmap.setIsDisplayed(str, z);
    }

    @Override // me.panpf.sketch.drawable.SketchRefDrawable
    public void setIsWaitingUse(String str, boolean z) {
        this.refBitmap.setIsWaitingUse(str, z);
    }
}
