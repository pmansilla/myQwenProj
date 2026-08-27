package me.panpf.sketch.display;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import me.panpf.sketch.SketchView;

/* loaded from: classes2.dex */
public class FadeInImageDisplayer implements ImageDisplayer {
    private static final String KEY = "FadeInImageDisplayer";
    private boolean alwaysUse;
    private int duration;

    public FadeInImageDisplayer() {
        this(ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public FadeInImageDisplayer(int i) {
        this(i, false);
    }

    public FadeInImageDisplayer(int i, boolean z) {
        this.duration = i;
        this.alwaysUse = z;
    }

    public FadeInImageDisplayer(boolean z) {
        this(ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    @Override // me.panpf.sketch.display.ImageDisplayer
    public void display(@NonNull SketchView sketchView, @NonNull Drawable drawable) {
        if (drawable == null) {
            return;
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());
        alphaAnimation.setDuration(this.duration);
        sketchView.clearAnimation();
        sketchView.setImageDrawable(drawable);
        sketchView.startAnimation(alphaAnimation);
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
        return String.format("%s(duration=%d,alwaysUse=%s)", KEY, Integer.valueOf(this.duration), Boolean.valueOf(this.alwaysUse));
    }
}
