package me.panpf.sketch.display;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import me.panpf.sketch.SketchView;

/* loaded from: classes2.dex */
public class ZoomInImageDisplayer implements ImageDisplayer {
    private static final float DEFAULT_FROM = 0.5f;
    private static final String KEY = "ZoomInImageDisplayer";
    private boolean alwaysUse;
    private int duration;
    private float fromX;
    private float fromY;
    private Interpolator interpolator;

    public ZoomInImageDisplayer() {
        this(0.5f, 0.5f, new AccelerateDecelerateInterpolator(), ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public ZoomInImageDisplayer(float f, float f2) {
        this(f, f2, new AccelerateDecelerateInterpolator(), ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public ZoomInImageDisplayer(float f, float f2, Interpolator interpolator) {
        this(f, f2, interpolator, ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public ZoomInImageDisplayer(float f, float f2, Interpolator interpolator, int i) {
        this(f, f2, interpolator, i, false);
    }

    public ZoomInImageDisplayer(float f, float f2, Interpolator interpolator, int i, boolean z) {
        this.duration = i;
        this.fromY = f2;
        this.fromX = f;
        this.interpolator = interpolator;
        this.alwaysUse = z;
    }

    public ZoomInImageDisplayer(float f, float f2, Interpolator interpolator, boolean z) {
        this(f, f2, interpolator, ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    public ZoomInImageDisplayer(float f, float f2, boolean z) {
        this(f, f2, new AccelerateDecelerateInterpolator(), ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    public ZoomInImageDisplayer(int i) {
        this(0.5f, 0.5f, new AccelerateDecelerateInterpolator(), i, false);
    }

    public ZoomInImageDisplayer(int i, boolean z) {
        this(0.5f, 0.5f, new AccelerateDecelerateInterpolator(), i, z);
    }

    public ZoomInImageDisplayer(Interpolator interpolator) {
        this(0.5f, 0.5f, interpolator, ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public ZoomInImageDisplayer(Interpolator interpolator, boolean z) {
        this(0.5f, 0.5f, interpolator, ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    public ZoomInImageDisplayer(boolean z) {
        this(0.5f, 0.5f, new AccelerateDecelerateInterpolator(), ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
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
