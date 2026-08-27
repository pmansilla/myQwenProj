package me.panpf.sketch.viewfun;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ShowGifFlagFunction extends ViewFunction {
    private int cacheViewHeight;
    private int cacheViewWidth;
    private Drawable gifFlagDrawable;
    private boolean gifImage;
    private float iconDrawLeft;
    private float iconDrawTop;
    private Drawable lastDrawable;
    private FunctionCallbackView view;

    public ShowGifFlagFunction(FunctionCallbackView functionCallbackView) {
        this.view = functionCallbackView;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onDraw(@NonNull Canvas canvas) {
        Drawable drawable = this.view.getDrawable();
        if (drawable != this.lastDrawable) {
            this.gifImage = SketchUtils.isGifImage(drawable);
            this.lastDrawable = drawable;
        }
        if (this.gifImage) {
            if (this.cacheViewWidth != this.view.getWidth() || this.cacheViewHeight != this.view.getHeight()) {
                this.cacheViewWidth = this.view.getWidth();
                this.cacheViewHeight = this.view.getHeight();
                this.iconDrawLeft = (this.view.getWidth() - this.view.getPaddingRight()) - this.gifFlagDrawable.getIntrinsicWidth();
                this.iconDrawTop = (this.view.getHeight() - this.view.getPaddingBottom()) - this.gifFlagDrawable.getIntrinsicHeight();
            }
            canvas.save();
            canvas.translate(this.iconDrawLeft, this.iconDrawTop);
            this.gifFlagDrawable.draw(canvas);
            canvas.restore();
        }
    }

    public boolean setGifFlagDrawable(Drawable drawable) {
        if (this.gifFlagDrawable == drawable) {
            return false;
        }
        this.gifFlagDrawable = drawable;
        this.gifFlagDrawable.setBounds(0, 0, this.gifFlagDrawable.getIntrinsicWidth(), this.gifFlagDrawable.getIntrinsicHeight());
        return true;
    }
}
