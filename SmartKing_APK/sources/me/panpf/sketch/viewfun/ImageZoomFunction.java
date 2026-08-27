package me.panpf.sketch.viewfun;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.ImageView;
import me.panpf.sketch.zoom.ImageZoomer;

/* loaded from: classes2.dex */
public class ImageZoomFunction extends ViewFunction {
    private ImageZoomer zoomer;

    public ImageZoomFunction(FunctionPropertyView functionPropertyView) {
        this.zoomer = new ImageZoomer(functionPropertyView);
    }

    @NonNull
    public ImageView.ScaleType getScaleType() {
        return this.zoomer.getScaleType();
    }

    @NonNull
    public ImageZoomer getZoomer() {
        return this.zoomer;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onAttachedToWindow() {
        this.zoomer.reset("onAttachedToWindow");
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDetachedFromWindow() {
        recycle("onDetachedFromWindow");
        return false;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        this.zoomer.onDraw(canvas);
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDrawableChanged(@NonNull String str, Drawable drawable, Drawable drawable2) {
        this.zoomer.reset("onDrawableChanged");
        return false;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        this.zoomer.reset("onSizeChanged");
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        return this.zoomer.onTouchEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recycle(@NonNull String str) {
        this.zoomer.recycle(str);
    }

    public void setScaleType(@NonNull ImageView.ScaleType scaleType) {
        this.zoomer.setScaleType(scaleType);
    }
}
