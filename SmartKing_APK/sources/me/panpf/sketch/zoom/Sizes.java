package me.panpf.sketch.zoom;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import me.panpf.sketch.drawable.SketchDrawable;
import me.panpf.sketch.drawable.SketchLoadingDrawable;
import me.panpf.sketch.util.SketchUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class Sizes {
    Size viewSize = new Size();
    Size imageSize = new Size();
    Size drawableSize = new Size();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clean() {
        this.viewSize.set(0, 0);
        this.imageSize.set(0, 0);
        this.drawableSize.set(0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.viewSize.isEmpty() || this.imageSize.isEmpty() || this.drawableSize.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public void resetSizes(ImageView imageView) {
        Drawable lastDrawable;
        int width = (imageView.getWidth() - imageView.getPaddingLeft()) - imageView.getPaddingRight();
        int height = (imageView.getHeight() - imageView.getPaddingTop()) - imageView.getPaddingBottom();
        if (width == 0 || height == 0 || (lastDrawable = SketchUtils.getLastDrawable(imageView.getDrawable())) == 0) {
            return;
        }
        int intrinsicWidth = lastDrawable.getIntrinsicWidth();
        int intrinsicHeight = lastDrawable.getIntrinsicHeight();
        if (intrinsicWidth == 0 || intrinsicHeight == 0) {
            return;
        }
        this.viewSize.set(width, height);
        this.drawableSize.set(intrinsicWidth, intrinsicHeight);
        if (!(lastDrawable instanceof SketchDrawable) || (lastDrawable instanceof SketchLoadingDrawable)) {
            this.imageSize.set(intrinsicWidth, intrinsicHeight);
        } else {
            SketchDrawable sketchDrawable = (SketchDrawable) lastDrawable;
            this.imageSize.set(sketchDrawable.getOriginWidth(), sketchDrawable.getOriginHeight());
        }
    }
}
