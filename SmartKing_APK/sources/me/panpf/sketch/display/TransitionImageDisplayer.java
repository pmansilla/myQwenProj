package me.panpf.sketch.display;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.drawable.SketchDrawable;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.drawable.SketchLoadingDrawable;
import me.panpf.sketch.drawable.SketchTransitionDrawable;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class TransitionImageDisplayer implements ImageDisplayer {
    private static final String KEY = "TransitionImageDisplayer";
    private boolean alwaysUse;
    private int duration;

    public TransitionImageDisplayer() {
        this(ImageDisplayer.DEFAULT_ANIMATION_DURATION, false);
    }

    public TransitionImageDisplayer(int i) {
        this(i, false);
    }

    public TransitionImageDisplayer(int i, boolean z) {
        this.duration = i;
        this.alwaysUse = z;
    }

    public TransitionImageDisplayer(boolean z) {
        this(ImageDisplayer.DEFAULT_ANIMATION_DURATION, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // me.panpf.sketch.display.ImageDisplayer
    public void display(@NonNull SketchView sketchView, @NonNull Drawable drawable) {
        if (drawable == 0) {
            return;
        }
        if (drawable instanceof SketchGifDrawable) {
            sketchView.clearAnimation();
            sketchView.setImageDrawable(drawable);
            return;
        }
        Drawable lastDrawable = SketchUtils.getLastDrawable(sketchView.getDrawable());
        if (lastDrawable == null) {
            lastDrawable = new ColorDrawable(0);
        }
        if ((lastDrawable instanceof SketchDrawable) && !(lastDrawable instanceof SketchLoadingDrawable) && (drawable instanceof SketchDrawable) && ((SketchDrawable) lastDrawable).getKey().equals(((SketchDrawable) drawable).getKey())) {
            sketchView.setImageDrawable(drawable);
            return;
        }
        SketchTransitionDrawable sketchTransitionDrawable = new SketchTransitionDrawable(lastDrawable, drawable);
        sketchView.clearAnimation();
        sketchView.setImageDrawable(sketchTransitionDrawable);
        sketchTransitionDrawable.setCrossFadeEnabled(true);
        sketchTransitionDrawable.startTransition(this.duration);
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
