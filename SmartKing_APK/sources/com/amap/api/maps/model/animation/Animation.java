package com.amap.api.maps.model.animation;

import android.view.animation.Interpolator;
import com.autonavi.amap.mapcore.animation.GLAnimation;

/* loaded from: classes.dex */
public abstract class Animation {
    public static final int FILL_MODE_BACKWARDS = 1;
    public static final int FILL_MODE_FORWARDS = 0;
    public static final int INFINITE = -1;
    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    private int a = 0;
    public GLAnimation glAnimation;

    /* loaded from: classes.dex */
    public interface AnimationListener {
        void onAnimationEnd();

        void onAnimationStart();
    }

    public Animation() {
        this.glAnimation = null;
        this.glAnimation = new GLAnimation();
    }

    private void a(boolean z) {
        if (this.glAnimation != null) {
            this.glAnimation.setFillEnabled(z);
        }
    }

    private void b(boolean z) {
        if (this.glAnimation != null) {
            this.glAnimation.setFillAfter(z);
        }
    }

    private void c(boolean z) {
        if (this.glAnimation != null) {
            this.glAnimation.setFillBefore(z);
        }
    }

    public int getFillMode() {
        return this.a;
    }

    public int getRepeatCount() {
        if (this.glAnimation != null) {
            return this.glAnimation.getRepeatCount();
        }
        return 0;
    }

    public int getRepeatMode() {
        if (this.glAnimation != null) {
            return this.glAnimation.getRepeatMode();
        }
        return 1;
    }

    public void setAnimationListener(AnimationListener animationListener) {
        this.glAnimation.setAnimationListener(animationListener);
    }

    public abstract void setDuration(long j);

    public void setFillMode(int i) {
        this.a = i;
        if (this.a == 0) {
            b(true);
            c(false);
            a(false);
        } else {
            b(false);
            a(true);
            c(true);
        }
    }

    public abstract void setInterpolator(Interpolator interpolator);

    public void setRepeatCount(int i) {
        if (this.glAnimation != null) {
            this.glAnimation.setRepeatCount(i);
        }
    }

    public void setRepeatMode(int i) {
        if (this.glAnimation != null) {
            this.glAnimation.setRepeatMode(i);
        }
    }
}
