package me.panpf.sketch.display;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import me.panpf.sketch.SketchView;

/* loaded from: classes2.dex */
public class ZoomOutImageDisplayer implements ImageDisplayer {
    private static final String KEY = "ZoomOutImageDisplayer";
    private boolean alwaysUse;
    private int duration;
    private float fromX;
    private float fromY;
    private Interpolator interpolator;

    public ZoomOutImageDisplayer() {
        this(1.5f, 1.5f, new AccelerateDecelerateInterpolator(), ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public ZoomOutImageDisplayer(float f, float f2) {
        this(f, f2, new AccelerateDecelerateInterpolator(), ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public ZoomOutImageDisplayer(float f, float f2, Interpolator interpolator) {
        this(f, f2, interpolator, ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public ZoomOutImageDisplayer(float f, float f2, Interpolator interpolator, int i) {
        this(f, f2, interpolator, i, false);
    }

    public ZoomOutImageDisplayer(float f, float f2, Interpolator interpolator, int i, boolean z) {
        this.duration = i;
        this.fromX = f;
        this.fromY = f2;
        this.interpolator = interpolator;
        this.alwaysUse = z;
    }

    public ZoomOutImageDisplayer(float f, float f2, Interpolator interpolator, boolean z) {
        this(f, f2, interpolator, ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    public ZoomOutImageDisplayer(float f, float f2, boolean z) {
        this(f, f2, new AccelerateDecelerateInterpolator(), ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    public ZoomOutImageDisplayer(int i) {
        this(1.5f, 1.5f, new AccelerateDecelerateInterpolator(), i, false);
    }

    public ZoomOutImageDisplayer(int i, boolean z) {
        this(1.5f, 1.5f, new AccelerateDecelerateInterpolator(), i, z);
    }

    public ZoomOutImageDisplayer(Interpolator interpolator) {
        this(1.5f, 1.5f, interpolator, ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public ZoomOutImageDisplayer(Interpolator interpolator, boolean z) {
        this(1.5f, 1.5f, interpolator, ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    public ZoomOutImageDisplayer(boolean z) {
        this(1.5f, 1.5f, new AccelerateDecelerateInterpolator(), ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    @Override // me.panpf.sketch.display.ImageDisplayer
    public void display(@NonNull SketchView sketchView, @NonNull Drawable drawable) {
        if (drawable == null) {
            return;
        }
        ScaleAnimation scaleAnimation = new ScaleAnimation(this.fromX, 1.0f, this.fromY, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(this.interpolator);
        scaleAnimation.setDuration(this.duration);
        sketchView.clearAnimation();
        sketchView.setImageDrawable(drawable);
        sketchView.startAnimation(scaleAnimation);
    }

    @Override // me.panpf.sketch.display.ImageDisplayer
    public int getDuration() {
        return this.duration;
    }

    public float getFromX() {
        return this.fromX;
    }

    public float getFromY() {
        return this.fromY;
    }

    public Interpolator getInterpolator() {
        return this.interpolator;
    }

    @Override // me.panpf.sketch.display.ImageDisplayer
    public boolean isAlwaysUse() {
        return this.alwaysUse;
    }

    @NonNull
    public String toString() {
        Object[] objArr = new Object[6];
        objArr[0] = KEY;
        objArr[1] = Integer.valueOf(this.duration);
        objArr[2] = Float.valueOf(this.fromX);
        objArr[3] = Float.valueOf(this.fromY);
        objArr[4] = this.interpolator != null ? this.interpolator.getClass().getSimpleName() : null;
        objArr[5] = Boolean.valueOf(this.alwaysUse);
        return String.format("%s(duration=%d,fromX=%s,fromY=%s,interpolator=%s,alwaysUse=%s)", objArr);
    }
}
