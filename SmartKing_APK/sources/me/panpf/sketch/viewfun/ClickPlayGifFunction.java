package me.panpf.sketch.viewfun;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import me.panpf.sketch.drawable.SketchGifDrawable;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.RedisplayListener;
import me.panpf.sketch.state.OldStateImage;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ClickPlayGifFunction extends ViewFunction {
    private int cacheViewHeight;
    private int cacheViewWidth;
    private boolean canClickPlay;
    private int iconDrawLeft;
    private int iconDrawTop;
    private Drawable lastDrawable;
    private Drawable playIconDrawable;
    private PlayGifRedisplayListener redisplayListener;
    private FunctionCallbackView view;

    /* loaded from: classes2.dex */
    private static class PlayGifRedisplayListener implements RedisplayListener {
        private PlayGifRedisplayListener() {
        }

        @Override // me.panpf.sketch.request.RedisplayListener
        public void onPreCommit(String str, DisplayOptions displayOptions) {
            displayOptions.setLoadingImage(new OldStateImage());
            displayOptions.setDecodeGifImage(true);
        }
    }

    public ClickPlayGifFunction(FunctionCallbackView functionCallbackView) {
        this.view = functionCallbackView;
    }

    private boolean canClickPlay(Drawable drawable) {
        if (drawable == null) {
            return false;
        }
        Drawable lastDrawable = SketchUtils.getLastDrawable(drawable);
        return SketchUtils.isGifImage(lastDrawable) && !(lastDrawable instanceof SketchGifDrawable);
    }

    public boolean isClickable() {
        return this.canClickPlay;
    }

    public boolean onClick(View view) {
        if (!isClickable()) {
            return false;
        }
        if (this.redisplayListener == null) {
            this.redisplayListener = new PlayGifRedisplayListener();
        }
        this.view.redisplay(this.redisplayListener);
        return true;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onDraw(@NonNull Canvas canvas) {
        Drawable drawable = this.view.getDrawable();
        if (drawable != this.lastDrawable) {
            this.canClickPlay = canClickPlay(drawable);
            this.lastDrawable = drawable;
        }
        if (this.canClickPlay) {
            if (this.cacheViewWidth != this.view.getWidth() || this.cacheViewHeight != this.view.getHeight()) {
                this.cacheViewWidth = this.view.getWidth();
                this.cacheViewHeight = this.view.getHeight();
                int width = ((this.view.getWidth() - this.view.getPaddingLeft()) - this.view.getPaddingRight()) - this.playIconDrawable.getBounds().width();
                int height = ((this.view.getHeight() - this.view.getPaddingTop()) - this.view.getPaddingBottom()) - this.playIconDrawable.getBounds().height();
                this.iconDrawLeft = this.view.getPaddingLeft() + (width / 2);
                this.iconDrawTop = this.view.getPaddingTop() + (height / 2);
            }
            canvas.save();
            canvas.translate(this.iconDrawLeft, this.iconDrawTop);
            this.playIconDrawable.draw(canvas);
            canvas.restore();
        }
    }

    public boolean setPlayIconDrawable(@NonNull Drawable drawable) {
        if (this.playIconDrawable == drawable) {
            return false;
        }
        this.playIconDrawable = drawable;
        this.playIconDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return true;
    }
}
