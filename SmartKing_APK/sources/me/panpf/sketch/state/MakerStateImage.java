package me.panpf.sketch.state;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.Configuration;
import me.panpf.sketch.Sketch;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.cache.BitmapPool;
import me.panpf.sketch.cache.BitmapPoolUtils;
import me.panpf.sketch.cache.MemoryCache;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.drawable.SketchBitmapDrawable;
import me.panpf.sketch.drawable.SketchRefBitmap;
import me.panpf.sketch.drawable.SketchShapeBitmapDrawable;
import me.panpf.sketch.process.ImageProcessor;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.request.Resize;
import me.panpf.sketch.request.ShapeSize;
import me.panpf.sketch.shaper.ImageShaper;
import me.panpf.sketch.uri.DrawableUriModel;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class MakerStateImage implements StateImage {
    private int resId;

    public MakerStateImage(int i) {
        this.resId = i;
    }

    private Drawable makeDrawable(Sketch sketch, DisplayOptions displayOptions) {
        Bitmap drawableToBitmap;
        boolean z;
        Configuration configuration = sketch.getConfiguration();
        ImageProcessor processor = displayOptions.getProcessor();
        Resize resize = displayOptions.getResize();
        BitmapPool bitmapPool = configuration.getBitmapPool();
        if (processor == null && resize == null) {
            return configuration.getContext().getResources().getDrawable(this.resId);
        }
        String makeUri = DrawableUriModel.makeUri(this.resId);
        UriModel match = UriModel.match(sketch, makeUri);
        String makeRequestKey = match != null ? SketchUtils.makeRequestKey(makeUri, match, displayOptions.makeStateImageKey()) : null;
        MemoryCache memoryCache = configuration.getMemoryCache();
        SketchRefBitmap sketchRefBitmap = makeRequestKey != null ? memoryCache.get(makeRequestKey) : null;
        if (sketchRefBitmap != null) {
            if (!sketchRefBitmap.isRecycled()) {
                return new SketchBitmapDrawable(sketchRefBitmap, ImageFrom.MEMORY_CACHE);
            }
            memoryCache.remove(makeRequestKey);
        }
        boolean z2 = configuration.isLowQualityImageEnabled() || displayOptions.isLowQualityImage();
        Drawable drawable = configuration.getContext().getResources().getDrawable(this.resId);
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            drawableToBitmap = SketchUtils.drawableToBitmap(drawable, z2, bitmapPool);
            z = true;
        } else {
            drawableToBitmap = ((BitmapDrawable) drawable).getBitmap();
            z = false;
        }
        if (drawableToBitmap == null || drawableToBitmap.isRecycled()) {
            return null;
        }
        if (processor == null && resize != null) {
            processor = sketch.getConfiguration().getResizeProcessor();
        }
        try {
            Bitmap process = processor.process(sketch, drawableToBitmap, resize, z2);
            if (process != drawableToBitmap) {
                if (z) {
                    BitmapPoolUtils.freeBitmapToPool(drawableToBitmap, bitmapPool);
                }
                if (process.isRecycled()) {
                    return null;
                }
                z = true;
            } else {
                process = drawableToBitmap;
            }
            if (!z) {
                return drawable;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(configuration.getContext().getResources(), this.resId, options);
            SketchRefBitmap sketchRefBitmap2 = new SketchRefBitmap(process, makeRequestKey, DrawableUriModel.makeUri(this.resId), new ImageAttrs(options.outMimeType, options.outWidth, options.outHeight, 0), bitmapPool);
            memoryCache.put(makeRequestKey, sketchRefBitmap2);
            return new SketchBitmapDrawable(sketchRefBitmap2, ImageFrom.LOCAL);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            sketch.getConfiguration().getErrorTracker().onProcessImageError(e, DrawableUriModel.makeUri(this.resId), processor);
            if (z) {
                BitmapPoolUtils.freeBitmapToPool(drawableToBitmap, bitmapPool);
            }
            return null;
        }
    }

    @Override // me.panpf.sketch.state.StateImage
    @Nullable
    public Drawable getDrawable(@NonNull Context context, @NonNull SketchView sketchView, @NonNull DisplayOptions displayOptions) {
        Drawable makeDrawable = makeDrawable(Sketch.with(context), displayOptions);
        ShapeSize shapeSize = displayOptions.getShapeSize();
        ImageShaper shaper = displayOptions.getShaper();
        return ((shapeSize == null && shaper == null) || makeDrawable == null || !(makeDrawable instanceof BitmapDrawable)) ? makeDrawable : new SketchShapeBitmapDrawable(context, (BitmapDrawable) makeDrawable, shapeSize, shaper);
    }

    public int getResId() {
        return this.resId;
    }
}
