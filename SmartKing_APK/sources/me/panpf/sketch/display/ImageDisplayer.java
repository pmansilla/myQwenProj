package me.panpf.sketch.display;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import me.panpf.sketch.SketchView;

/* loaded from: classes2.dex */
public interface ImageDisplayer {
    public static final int DEFAULT_ANIMATION_DURATION = 400;

    void display(@NonNull SketchView sketchView, @NonNull Drawable drawable);

    int getDuration();

    boolean isAlwaysUse();
}
