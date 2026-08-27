package me.panpf.sketch.state;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.drawable.SketchLoadingDrawable;
import me.panpf.sketch.drawable.SketchShapeBitmapDrawable;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.ShapeSize;
import me.panpf.sketch.shaper.ImageShaper;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class OldStateImage implements StateImage {
    private StateImage whenEmptyImage;

    public OldStateImage() {
    }

    public OldStateImage(StateImage stateImage) {
        this.whenEmptyImage = stateImage;
    }

    @Override // me.panpf.sketch.state.StateImage
    @Nullable
    public Drawable getDrawable(@NonNull Context context, @NonNull SketchView sketchView, @NonNull DisplayOptions displayOptions) {
        Drawable drawable;
        Drawable lastDrawable = SketchUtils.getLastDrawable(sketchView.getDrawable());
        if (lastDrawable != null && (lastDrawable instanceof SketchLoadingDrawable)) {
            lastDrawable = ((SketchLoadingDrawable) lastDrawable).getWrappedDrawable();
        }
        if (lastDrawable != null) {
            ShapeSize shapeSize = displayOptions.getShapeSize();
            ImageShaper shaper = displayOptions.getShaper();
            if (shapeSize != null || shaper != null) {
                if (lastDrawable instanceof SketchShapeBitmapDrawable) {
                    drawable = new SketchShapeBitmapDrawable(context, ((SketchShapeBitmapDrawable) lastDrawable).getBitmapDrawable(), shapeSize, shaper);
                } else if (lastDrawable instanceof BitmapDrawable) {
                    drawable = new SketchShapeBitmapDrawable(context, (BitmapDrawable) lastDrawable, shapeSize, shaper);
                }
                return (drawable != null || this.whenEmptyImage == null) ? drawable : this.whenEmptyImage.getDrawable(context, sketchView, displayOptions);
            }
        }
        drawable = lastDrawable;
        if (drawable != null) {
            return drawable;
        }
    }
}
