package me.panpf.sketch.display;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import me.panpf.sketch.SketchView;

/* loaded from: classes2.dex */
public class ColorTransitionImageDisplayer implements ImageDisplayer {
    private static final String KEY = "ColorTransitionImageDisplayer";
    private boolean alwaysUse;
    private int color;
    private int duration;

    public ColorTransitionImageDisplayer(int i) {
        this(i, ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public ColorTransitionImageDisplayer(int i, int i2) {
        this(i, i2, false);
    }

    public ColorTransitionImageDisplayer(int i, int i2, boolean z) {
        this.color = i;
        this.duration = i2;
        this.alwaysUse = z;
    }

    public ColorTransitionImageDisplayer(int i, boolean z) {
        this(i, ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    @Override // me.panpf.sketch.display.ImageDisplayer
    public void display(@NonNull SketchView sketchView, @NonNull Drawable drawable) {
        if (drawable == null) {
            return;
        }
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{new ColorDrawable(this.color), drawable});
        sketchView.clearAnimation();
        sketchView.setImageDrawable(transitionDrawable);
        transitionDrawable.setCrossFadeEnabled(true);
        transitionDrawable.startTransition(this.duration);
    }

    public int getColor() {
        return this.color;
    }

    @Override // me.panpf.sketch.display.ImageDisplayer
    public int getDuration() {
        return this.duration;
    }

    @Override // me.panpf.sketch.display.ImageDisplayer
    public boolean isAlwaysUse() {
        return this.alwaysUse;
    }

    @NonNull
    public String toString() {
        return String.format("%s(duration=%d,color=%d,alwaysUse=%s)", KEY, Integer.valueOf(this.duration), Integer.valueOf(this.color), Boolean.valueOf(this.alwaysUse));
    }
}
