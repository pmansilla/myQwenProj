package me.panpf.sketch.zoom;

import android.content.Context;
import android.widget.ImageView;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.decode.ImageSizeCalculator;

/* loaded from: classes2.dex */
public class Scales {
    private static final float DEFAULT_MINIMUM_SCALE = 1.0f;
    public float fillZoomScale;
    public float fullZoomScale;
    public float originZoomScale;
    private static final float DEFAULT_MAXIMIZE_SCALE = 1.75f;
    private static final float[] DEFAULT_DOUBLE_CLICK_ZOOM_SCALES = {1.0f, DEFAULT_MAXIMIZE_SCALE};
    public float minZoomScale = 1.0f;
    public float maxZoomScale = DEFAULT_MAXIMIZE_SCALE;
    public float[] doubleClickZoomScales = DEFAULT_DOUBLE_CLICK_ZOOM_SCALES;

    public void clean() {
        this.originZoomScale = 1.0f;
        this.fillZoomScale = 1.0f;
        this.fullZoomScale = 1.0f;
        this.minZoomScale = 1.0f;
        this.maxZoomScale = DEFAULT_MAXIMIZE_SCALE;
        this.doubleClickZoomScales = DEFAULT_DOUBLE_CLICK_ZOOM_SCALES;
    }

    public void reset(Context context, Sizes sizes, ImageView.ScaleType scaleType, float f, boolean z) {
        float f2 = f % 180.0f;
        int width = f2 == 0.0f ? sizes.drawableSize.getWidth() : sizes.drawableSize.getHeight();
        int height = f2 == 0.0f ? sizes.drawableSize.getHeight() : sizes.drawableSize.getWidth();
        int width2 = f2 == 0.0f ? sizes.imageSize.getWidth() : sizes.imageSize.getHeight();
        int height2 = f2 == 0.0f ? sizes.imageSize.getHeight() : sizes.imageSize.getWidth();
        float f3 = width;
        float width3 = sizes.viewSize.getWidth() / f3;
        float f4 = height;
        float height3 = sizes.viewSize.getHeight() / f4;
        boolean z2 = width > sizes.viewSize.getWidth() || height > sizes.viewSize.getHeight();
        this.fullZoomScale = Math.min(width3, height3);
        this.fillZoomScale = Math.max(width3, height3);
        this.originZoomScale = Math.max(width2 / f3, height2 / f4);
        if (scaleType == ImageView.ScaleType.MATRIX) {
            scaleType = ImageView.ScaleType.FIT_CENTER;
        }
        if (scaleType == ImageView.ScaleType.CENTER || (scaleType == ImageView.ScaleType.CENTER_INSIDE && !z2)) {
            this.minZoomScale = 1.0f;
            this.maxZoomScale = Math.max(this.originZoomScale, this.fillZoomScale);
        } else if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            this.minZoomScale = this.fillZoomScale;
            this.maxZoomScale = Math.max(this.originZoomScale, this.fillZoomScale * 1.5f);
        } else if (scaleType == ImageView.ScaleType.FIT_START || scaleType == ImageView.ScaleType.FIT_CENTER || scaleType == ImageView.ScaleType.FIT_END || (scaleType == ImageView.ScaleType.CENTER_INSIDE && z2)) {
            this.minZoomScale = this.fullZoomScale;
            ImageSizeCalculator sizeCalculator = Sketch.with(context).getConfiguration().getSizeCalculator();
            if (z && (sizeCalculator.canUseReadModeByHeight(width2, height2) || sizeCalculator.canUseReadModeByWidth(width2, height2))) {
                this.maxZoomScale = Math.max(this.originZoomScale, this.fillZoomScale);
            } else {
                if (this.originZoomScale <= this.fillZoomScale || this.fillZoomScale * 1.2f < this.originZoomScale) {
                    this.maxZoomScale = Math.max(this.originZoomScale, this.fillZoomScale);
                } else {
                    this.maxZoomScale = this.fillZoomScale;
                }
                this.maxZoomScale = Math.max(this.maxZoomScale, this.minZoomScale * 1.5f);
            }
        } else if (scaleType == ImageView.ScaleType.FIT_XY) {
            this.minZoomScale = this.fullZoomScale;
            this.maxZoomScale = this.fullZoomScale;
        } else {
            this.minZoomScale = this.fullZoomScale;
            this.maxZoomScale = this.fullZoomScale;
        }
        if (this.minZoomScale > this.maxZoomScale) {
            this.minZoomScale += this.maxZoomScale;
            this.maxZoomScale = this.minZoomScale - this.maxZoomScale;
            this.minZoomScale -= this.maxZoomScale;
        }
        this.doubleClickZoomScales = new float[]{this.minZoomScale, this.maxZoomScale};
    }
}
