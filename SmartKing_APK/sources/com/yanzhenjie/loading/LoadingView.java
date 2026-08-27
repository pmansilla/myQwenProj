package com.yanzhenjie.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/* loaded from: classes2.dex */
public class LoadingView extends ImageView {
    private LoadingDrawable mLoadingDrawable;
    private LevelLoadingRenderer mLoadingRenderer;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLoadingRenderer = new LevelLoadingRenderer(context);
        this.mLoadingDrawable = new LoadingDrawable(this.mLoadingRenderer);
        setImageDrawable(this.mLoadingDrawable);
    }

    private void startAnimation() {
        if (this.mLoadingDrawable != null) {
            this.mLoadingDrawable.start();
        }
    }

    private void stopAnimation() {
        if (this.mLoadingDrawable != null) {
            this.mLoadingDrawable.stop();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    public void setCircleColors(int i, int i2, int i3) {
        this.mLoadingRenderer.setCircleColors(i, i2, i3);
    }
}
