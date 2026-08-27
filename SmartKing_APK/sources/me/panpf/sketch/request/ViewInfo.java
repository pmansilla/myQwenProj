package me.panpf.sketch.request;

import android.widget.ImageView;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.SketchView;

/* loaded from: classes2.dex */
public class ViewInfo {
    private FixedSize fixedSize;
    private ImageView.ScaleType scaleType;
    private boolean useSmallerThumbnails;

    public ViewInfo() {
    }

    public ViewInfo(ViewInfo viewInfo) {
        copy(viewInfo);
    }

    public void copy(ViewInfo viewInfo) {
        this.scaleType = viewInfo.scaleType;
        this.fixedSize = viewInfo.fixedSize;
        this.useSmallerThumbnails = viewInfo.useSmallerThumbnails;
    }

    public FixedSize getFixedSize() {
        return this.fixedSize;
    }

    public ImageView.ScaleType getScaleType() {
        return this.scaleType;
    }

    public boolean isUseSmallerThumbnails() {
        return this.useSmallerThumbnails;
    }

    public void reset(SketchView sketchView, Sketch sketch) {
        if (sketchView != null) {
            this.scaleType = sketchView.getScaleType();
            this.fixedSize = sketch.getConfiguration().getSizeCalculator().calculateImageFixedSize(sketchView);
            this.useSmallerThumbnails = sketchView.isUseSmallerThumbnails();
        } else {
            this.scaleType = null;
            this.fixedSize = null;
            this.useSmallerThumbnails = false;
        }
    }
}
