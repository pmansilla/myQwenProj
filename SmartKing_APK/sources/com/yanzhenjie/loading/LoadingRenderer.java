package com.yanzhenjie.loading;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

/* loaded from: classes2.dex */
public abstract class LoadingRenderer {
    private static final long ANIMATION_DURATION = 1333;
    private final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.yanzhenjie.loading.LoadingRenderer.1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            LoadingRenderer.this.computeRender(((Float) valueAnimator.getAnimatedValue()).floatValue());
            LoadingRenderer.this.invalidateSelf();
        }
    };
    protected final Rect mBounds = new Rect();
    private Drawable.Callback mCallback;
    protected long mDuration;
    protected float mHeight;
    private ValueAnimator mRenderAnimator;
    protected float mWidth;

    public LoadingRenderer(Context context) {
        float dip2px = Utils.dip2px(context, 56.0f);
        this.mHeight = dip2px;
        this.mWidth = dip2px;
        this.mDuration = ANIMATION_DURATION;
        setupAnimators();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateSelf() {
        this.mCallback.invalidateDrawable(null);
    }

    private void setupAnimators() {
        this.mRenderAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mRenderAnimator.setRepeatCount(-1);
        this.mRenderAnimator.setRepeatMode(1);
        this.mRenderAnimator.setDuration(this.mDuration);
        this.mRenderAnimator.setInterpolator(new LinearInterpolator());
        this.mRenderAnimator.addUpdateListener(this.mAnimatorUpdateListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addRenderListener(Animator.AnimatorListener animatorListener) {
        this.mRenderAnimator.addListener(animatorListener);
    }

    protected abstract void computeRender(float f);

    /* JADX INFO: Access modifiers changed from: protected */
    public void draw(Canvas canvas) {
        draw(canvas, this.mBounds);
    }

    @Deprecated
    protected void draw(Canvas canvas, Rect rect) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRunning() {
        return this.mRenderAnimator.isRunning();
    }

    protected abstract void reset();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void setAlpha(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBounds(Rect rect) {
        this.mBounds.set(rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCallback(Drawable.Callback callback) {
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void setColorFilter(ColorFilter colorFilter);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        reset();
        this.mRenderAnimator.addUpdateListener(this.mAnimatorUpdateListener);
        this.mRenderAnimator.setRepeatCount(-1);
        this.mRenderAnimator.setDuration(this.mDuration);
        this.mRenderAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop() {
        this.mRenderAnimator.removeUpdateListener(this.mAnimatorUpdateListener);
        this.mRenderAnimator.setRepeatCount(0);
        this.mRenderAnimator.setDuration(0L);
        this.mRenderAnimator.end();
    }
}
