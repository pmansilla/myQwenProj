package me.panpf.sketch.drawable;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import me.panpf.sketch.request.ImageFrom;

/* loaded from: classes2.dex */
public class SketchTransitionDrawable extends TransitionDrawable implements SketchDrawable {
    private SketchDrawable sketchDrawable;

    public SketchTransitionDrawable(Drawable drawable, Drawable drawable2) {
        super(new Drawable[]{drawable, drawable2});
        if (drawable2 instanceof SketchDrawable) {
            this.sketchDrawable = (SketchDrawable) drawable2;
        }
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public Bitmap.Config getBitmapConfig() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getBitmapConfig();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getByteCount() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getByteCount();
        }
        return 0;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getExifOrientation() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getExifOrientation();
        }
        return 0;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public ImageFrom getImageFrom() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getImageFrom();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getInfo() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getInfo();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getKey() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getKey();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getMimeType() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getMimeType();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getOriginHeight() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getOriginHeight();
        }
        return 0;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public int getOriginWidth() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getOriginWidth();
        }
        return 0;
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getUri() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getUri();
        }
        return null;
    }
}
