package me.panpf.sketch.zoom;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import me.panpf.sketch.util.SketchUtils;
import me.panpf.sketch.viewfun.FunctionCallbackView;
import me.panpf.sketch.zoom.ImageZoomer;

/* loaded from: classes2.dex */
class TapHelper extends GestureDetector.SimpleOnGestureListener {
    private ImageZoomer imageZoomer;
    private GestureDetector tapGestureDetector;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TapHelper(Context context, ImageZoomer imageZoomer) {
        this.imageZoomer = imageZoomer;
        this.tapGestureDetector = new GestureDetector(context, this);
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTap(MotionEvent motionEvent) {
        float formatFloat = SketchUtils.formatFloat(this.imageZoomer.getZoomScale(), 2);
        float[] doubleClickZoomScales = this.imageZoomer.getDoubleClickZoomScales();
        if (doubleClickZoomScales.length < 2) {
            return true;
        }
        float f = doubleClickZoomScales[0];
        for (int length = doubleClickZoomScales.length - 1; length >= 0; length--) {
            float f2 = doubleClickZoomScales[length];
            if (formatFloat < SketchUtils.formatFloat(f2, 2)) {
                f = f2;
                break;
            }
        }
        try {
            this.imageZoomer.zoom(f, motionEvent.getX(), motionEvent.getY(), true);
        } catch (ArrayIndexOutOfBoundsException unused) {
        }
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
        FunctionCallbackView functionCallbackView;
        View.OnLongClickListener onLongClickListener;
        super.onLongPress(motionEvent);
        ImageView imageView = this.imageZoomer.getImageView();
        ImageZoomer.OnViewLongPressListener onViewLongPressListener = this.imageZoomer.getOnViewLongPressListener();
        if (onViewLongPressListener != null) {
            onViewLongPressListener.onViewLongPress(imageView, motionEvent.getX(), motionEvent.getY());
        } else if ((imageView instanceof FunctionCallbackView) && (onLongClickListener = (functionCallbackView = (FunctionCallbackView) imageView).getOnLongClickListener()) != null && functionCallbackView.isLongClickable()) {
            onLongClickListener.onLongClick(imageView);
        }
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        FunctionCallbackView functionCallbackView;
        View.OnClickListener onClickListener;
        ImageView imageView = this.imageZoomer.getImageView();
        ImageZoomer.OnViewTapListener onViewTapListener = this.imageZoomer.getOnViewTapListener();
        if (onViewTapListener != null) {
            onViewTapListener.onViewTap(imageView, motionEvent.getX(), motionEvent.getY());
            return true;
        }
        if (!(imageView instanceof FunctionCallbackView) || (onClickListener = (functionCallbackView = (FunctionCallbackView) imageView).getOnClickListener()) == null || !functionCallbackView.isClickable()) {
            return false;
        }
        onClickListener.onClick(imageView);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.tapGestureDetector.onTouchEvent(motionEvent);
    }
}
