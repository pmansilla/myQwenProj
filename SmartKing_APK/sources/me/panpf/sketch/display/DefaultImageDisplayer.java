package me.panpf.sketch.display;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import me.panpf.sketch.SketchView;

/* loaded from: classes2.dex */
public class DefaultImageDisplayer implements ImageDisplayer {
    private static final String KEY = "DefaultImageDisplayer";

    @Override // me.panpf.sketch.display.ImageDisplayer
    public void display(@NonNull SketchView sketchView, @NonNull Drawable drawable) {
        if (drawable == null) {
            return;
        }
        sketchView.clearAnimation();
        sketchView.setImageDrawable(drawable);
    }

    @Override // me.panpf.sketch.display.ImageDisplayer
    public int getDuration() {
        return 0;
    }

    @Override // me.panpf.sketch.display.ImageDisplayer
    public boolean isAlwaysUse() {
        return false;
    }

    @NonNull
    public String toString() {
        return KEY;
    }
}
