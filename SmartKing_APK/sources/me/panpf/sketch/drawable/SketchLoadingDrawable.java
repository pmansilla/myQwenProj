package me.panpf.sketch.drawable;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;
import me.panpf.sketch.request.DisplayRequest;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.util.DrawableWrapper;

/* loaded from: classes2.dex */
public class SketchLoadingDrawable extends DrawableWrapper implements SketchRefDrawable {
    private SketchRefDrawable refDrawable;
    private SketchDrawable sketchDrawable;
    private WeakReference<DisplayRequest> weakReference;

    /* JADX WARN: Multi-variable type inference failed */
    public SketchLoadingDrawable(Drawable drawable, DisplayRequest displayRequest) {
        super(drawable);
        this.weakReference = new WeakReference<>(displayRequest);
        if (drawable instanceof SketchRefDrawable) {
            this.refDrawable = (SketchRefDrawable) drawable;
        }
        if (drawable instanceof SketchDrawable) {
            this.sketchDrawable = (SketchDrawable) drawable;
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

    public DisplayRequest getRequest() {
        return this.weakReference.get();
    }

    @Override // me.panpf.sketch.drawable.SketchDrawable
    public String getUri() {
        if (this.sketchDrawable != null) {
            return this.sketchDrawable.getUri();
        }
        return null;
    }

    @Override // me.panpf.sketch.drawable.SketchRefDrawable
    public boolean isRecycled() {
        return this.refDrawable != null && this.refDrawable.isRecycled();
    }

    @Override // me.panpf.sketch.drawable.SketchRefDrawable
    public void setIsDisplayed(String str, boolean z) {
        if (this.refDrawable != null) {
            this.refDrawable.setIsDisplayed(str, z);
        }
    }

    @Override // me.panpf.sketch.drawable.SketchRefDrawable
    public void setIsWaitingUse(String str, boolean z) {
        if (this.refDrawable != null) {
            this.refDrawable.setIsWaitingUse(str, z);
        }
    }
}
