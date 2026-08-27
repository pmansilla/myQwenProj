package me.panpf.sketch.state;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.cache.MemoryCache;
import me.panpf.sketch.drawable.SketchBitmapDrawable;
import me.panpf.sketch.drawable.SketchRefBitmap;
import me.panpf.sketch.drawable.SketchShapeBitmapDrawable;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.request.ShapeSize;
import me.panpf.sketch.shaper.ImageShaper;

/* loaded from: classes2.dex */
public class MemoryCacheStateImage implements StateImage {
    private String memoryCacheKey;
    private StateImage whenEmptyImage;

    public MemoryCacheStateImage(String str, StateImage stateImage) {
        this.memoryCacheKey = str;
        this.whenEmptyImage = stateImage;
    }

    @Override // me.panpf.sketch.state.StateImage
    @Nullable
    public Drawable getDrawable(@NonNull Context context, @NonNull SketchView sketchView, @NonNull DisplayOptions displayOptions) {
        MemoryCache memoryCache = Sketch.with(context).getConfiguration().getMemoryCache();
        SketchRefBitmap sketchRefBitmap = memoryCache.get(this.memoryCacheKey);
        if (sketchRefBitmap != null) {
            if (!sketchRefBitmap.isRecycled()) {
                SketchBitmapDrawable sketchBitmapDrawable = new SketchBitmapDrawable(sketchRefBitmap, ImageFrom.MEMORY_CACHE);
                ShapeSize shapeSize = displayOptions.getShapeSize();
                ImageShaper shaper = displayOptions.getShaper();
                return (shapeSize == null && shaper == null) ? sketchBitmapDrawable : new SketchShapeBitmapDrawable(context, sketchBitmapDrawable, shapeSize, shaper);
            }
            memoryCache.remove(this.memoryCacheKey);
        }
        if (this.whenEmptyImage != null) {
            return this.whenEmptyImage.getDrawable(context, sketchView, displayOptions);
        }
        return null;
    }

    public String getMemoryCacheKey() {
        return this.memoryCacheKey;
    }

    public StateImage getWhenEmptyImage() {
        return this.whenEmptyImage;
    }
}
