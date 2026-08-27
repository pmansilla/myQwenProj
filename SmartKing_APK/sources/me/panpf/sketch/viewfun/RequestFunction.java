package me.panpf.sketch.viewfun;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.drawable.SketchDrawable;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.drawable.SketchLoadingDrawable;
import me.panpf.sketch.drawable.SketchRefDrawable;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.DisplayCache;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.DisplayRequest;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class RequestFunction extends ViewFunction {
    private DisplayCache displayCache;
    private DisplayOptions displayOptions = new DisplayOptions();
    private boolean newDrawableFromSketch;
    private boolean oldDrawableFromSketch;
    private SketchView sketchView;

    public RequestFunction(SketchView sketchView) {
        this.sketchView = sketchView;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean notifyDrawable(String str, Drawable drawable, boolean z) {
        DisplayRequest request;
        if (drawable == 0) {
            return false;
        }
        if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            int numberOfLayers = layerDrawable.getNumberOfLayers();
            boolean z2 = false;
            for (int i = 0; i < numberOfLayers; i++) {
                z2 |= notifyDrawable(str, layerDrawable.getDrawable(i), z);
            }
            return z2;
        }
        if (!z && (drawable instanceof SketchLoadingDrawable) && (request = ((SketchLoadingDrawable) drawable).getRequest()) != null && !request.isFinished()) {
            request.cancel(CancelCause.BE_REPLACED_ON_SET_DRAWABLE);
        }
        if (drawable instanceof SketchRefDrawable) {
            ((SketchRefDrawable) drawable).setIsDisplayed(str, z);
        } else if ((drawable instanceof SketchGifDrawable) && !z) {
            ((SketchGifDrawable) drawable).recycle();
        }
        return drawable instanceof SketchDrawable;
    }

    public DisplayCache getDisplayCache() {
        return this.displayCache;
    }

    public DisplayOptions getDisplayOptions() {
        return this.displayOptions;
    }

    public boolean isNewDrawableFromSketch() {
        return this.newDrawableFromSketch;
    }

    public boolean isOldDrawableFromSketch() {
        return this.oldDrawableFromSketch;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDetachedFromWindow() {
        DisplayRequest findDisplayRequest = SketchUtils.findDisplayRequest(this.sketchView);
        if (findDisplayRequest != null && !findDisplayRequest.isFinished()) {
            findDisplayRequest.cancel(CancelCause.ON_DETACHED_FROM_WINDOW);
        }
        Drawable drawable = this.sketchView.getDrawable();
        return drawable != null && notifyDrawable("onDetachedFromWindow", drawable, false);
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDrawableChanged(@NonNull String str, @Nullable Drawable drawable, @Nullable Drawable drawable2) {
        this.newDrawableFromSketch = notifyDrawable(str + ":newDrawable", drawable2, true);
        this.oldDrawableFromSketch = notifyDrawable(str + ":oldDrawable", drawable, false);
        if (!this.newDrawableFromSketch) {
            this.displayCache = null;
        }
        return false;
    }

    public void setDisplayCache(DisplayCache displayCache) {
        this.displayCache = displayCache;
    }
}
