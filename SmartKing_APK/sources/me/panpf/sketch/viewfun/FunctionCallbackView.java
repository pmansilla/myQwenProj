package me.panpf.sketch.viewfun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import me.panpf.sketch.SketchView;
import me.panpf.sketch.request.DisplayCache;
import me.panpf.sketch.request.DisplayListener;
import me.panpf.sketch.request.DisplayOptions;
import me.panpf.sketch.request.DownloadProgressListener;
import me.panpf.sketch.uri.UriModel;

/* loaded from: classes2.dex */
public abstract class FunctionCallbackView extends ImageView implements SketchView {
    private OnClickListenerProxy clickListenerProxy;
    private DisplayListenerProxy displayListenerProxy;
    private ViewFunctions functions;
    View.OnLongClickListener longClickListener;
    private ProgressListenerProxy progressListenerProxy;
    View.OnClickListener wrappedClickListener;
    DisplayListener wrappedDisplayListener;
    DownloadProgressListener wrappedProgressListener;

    public FunctionCallbackView(Context context) {
        super(context);
        init();
    }

    public FunctionCallbackView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public FunctionCallbackView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.displayListenerProxy = new DisplayListenerProxy(this);
        this.progressListenerProxy = new ProgressListenerProxy(this);
        this.clickListenerProxy = new OnClickListenerProxy(this);
        super.setOnClickListener(this.clickListenerProxy);
        updateClickable();
    }

    private void setDrawable(@NonNull String str, @Nullable Drawable drawable, @Nullable Drawable drawable2) {
        if (drawable == drawable2 || !getFunctions().onDrawableChanged(str, drawable, drawable2)) {
            return;
        }
        invalidate();
    }

    @Override // me.panpf.sketch.SketchView
    @Nullable
    public DisplayCache getDisplayCache() {
        return getFunctions().requestFunction.getDisplayCache();
    }

    @Override // me.panpf.sketch.SketchView
    @Nullable
    public DisplayListener getDisplayListener() {
        return this.displayListenerProxy;
    }

    @Override // me.panpf.sketch.SketchView
    @Nullable
    public DownloadProgressListener getDownloadProgressListener() {
        if (getFunctions().showDownloadProgressFunction == null && this.wrappedProgressListener == null) {
            return null;
        }
        return this.progressListenerProxy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewFunctions getFunctions() {
        if (this.functions == null) {
            synchronized (this) {
                if (this.functions == null) {
                    this.functions = new ViewFunctions(this);
                }
            }
        }
        return this.functions;
    }

    public View.OnClickListener getOnClickListener() {
        return this.clickListenerProxy;
    }

    public View.OnLongClickListener getOnLongClickListener() {
        return this.longClickListener;
    }

    @Override // me.panpf.sketch.SketchView
    @NonNull
    public DisplayOptions getOptions() {
        return getFunctions().requestFunction.getDisplayOptions();
    }

    @Override // me.panpf.sketch.SketchView
    public boolean isUseSmallerThumbnails() {
        return isZoomEnabled();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getFunctions().onAttachedToWindow();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getFunctions().onDetachedFromWindow()) {
            super.setImageDrawable(null);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getFunctions().onDraw(canvas);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        getFunctions().onLayout(z, i, i2, i3, i4);
    }

    @Override // me.panpf.sketch.SketchView
    public void onReadyDisplay(UriModel uriModel) {
        if (getFunctions().onReadyDisplay(uriModel)) {
            invalidate();
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        getFunctions().onSizeChanged(i, i2, i3, i4);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return getFunctions().onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
    }

    @Override // me.panpf.sketch.SketchView
    public void setDisplayCache(@NonNull DisplayCache displayCache) {
        getFunctions().requestFunction.setDisplayCache(displayCache);
    }

    @Override // me.panpf.sketch.SketchView
    public void setDisplayListener(@Nullable DisplayListener displayListener) {
        this.wrappedDisplayListener = displayListener;
    }

    @Override // me.panpf.sketch.SketchView
    public void setDownloadProgressListener(@Nullable DownloadProgressListener downloadProgressListener) {
        this.wrappedProgressListener = downloadProgressListener;
    }

    @Override // android.widget.ImageView, me.panpf.sketch.SketchView
    public void setImageDrawable(@Nullable Drawable drawable) {
        Drawable drawable2 = getDrawable();
        super.setImageDrawable(drawable);
        setDrawable("setImageDrawable", drawable2, getDrawable());
    }

    @Override // android.widget.ImageView
    public void setImageResource(@DrawableRes int i) {
        Drawable drawable = getDrawable();
        super.setImageResource(i);
        setDrawable("setImageResource", drawable, getDrawable());
    }

    @Override // android.widget.ImageView
    public void setImageURI(@Nullable Uri uri) {
        Drawable drawable = getDrawable();
        super.setImageURI(uri);
        setDrawable("setImageURI", drawable, getDrawable());
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.wrappedClickListener = onClickListener;
        updateClickable();
    }

    @Override // android.view.View
    public void setOnLongClickListener(@Nullable View.OnLongClickListener onLongClickListener) {
        super.setOnLongClickListener(onLongClickListener);
        this.longClickListener = onLongClickListener;
    }

    @Override // me.panpf.sketch.SketchView
    public void setOptions(@Nullable DisplayOptions displayOptions) {
        if (displayOptions == null) {
            getFunctions().requestFunction.getDisplayOptions().reset();
        } else {
            getFunctions().requestFunction.getDisplayOptions().copy(displayOptions);
        }
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        ImageZoomFunction imageZoomFunction = getFunctions().zoomFunction;
        if (imageZoomFunction == null || !imageZoomFunction.getZoomer().isWorking() || scaleType == ImageView.ScaleType.MATRIX) {
            super.setScaleType(scaleType);
        } else {
            imageZoomFunction.setScaleType(scaleType);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateClickable() {
        setClickable(this.clickListenerProxy.isClickable());
    }
}
