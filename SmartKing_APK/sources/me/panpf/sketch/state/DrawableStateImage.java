package me.panpf.sketch.state;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.drawable.SketchShapeBitmapDrawable;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.ShapeSize;
import me.panpf.sketch.shaper.ImageShaper;

/* loaded from: classes2.dex */
public class DrawableStateImage implements StateImage {
    private Drawable originDrawable;
    private int resId;

    public DrawableStateImage(int i) {
        this.resId = -1;
        this.resId = i;
    }

    public DrawableStateImage(Drawable drawable) {
        this.resId = -1;
        this.originDrawable = drawable;
    }

    @Override // me.panpf.sketch.state.StateImage
    @Nullable
    public Drawable getDrawable(@NonNull Context context, @NonNull SketchView sketchView, @NonNull DisplayOptions displayOptions) {
        Drawable drawable = this.originDrawable;
        if (drawable == null && this.resId != -1) {
            drawable = context.getResources().getDrawable(this.resId);
        }
        ShapeSize shapeSize = displayOptions.getShapeSize();
        ImageShaper shaper = displayOptions.getShaper();
        return ((shapeSize == null && shaper == null) || drawable == null || !(drawable instanceof BitmapDrawable)) ? drawable : new SketchShapeBitmapDrawable(context, (BitmapDrawable) drawable, shapeSize, shaper);
    }

    public Drawable getOriginDrawable() {
        return this.originDrawable;
    }

    public int getResId() {
        return this.resId;
    }
}
