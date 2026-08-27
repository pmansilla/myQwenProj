package me.panpf.sketch.viewfun;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import me.panpf.sketch.drawable.SketchDrawable;
import me.panpf.sketch.drawable.SketchLoadingDrawable;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.uri.UriModel;
import me.panpf.sketch.util.SketchUtils;

/* loaded from: classes2.dex */
public class ShowImageFromFunction extends ViewFunction {
    private static final int FROM_FLAG_COLOR_DISK_CACHE = -1996488960;
    private static final int FROM_FLAG_COLOR_LOCAL = -2013265665;
    private static final int FROM_FLAG_COLOR_MEMORY = -2002771728;
    private static final int FROM_FLAG_COLOR_MEMORY_CACHE = -2013200640;
    private static final int FROM_FLAG_COLOR_NETWORK = -1996554240;
    private ImageFrom imageFrom;
    private Paint imageFromPaint;
    private Path imageFromPath;
    private View view;

    public ShowImageFromFunction(View view) {
        this.view = view;
    }

    private void initImageFromPath() {
        if (this.imageFromPath == null) {
            this.imageFromPath = new Path();
        } else {
            this.imageFromPath.reset();
        }
        int width = this.view.getWidth() / 10;
        int width2 = this.view.getWidth() / 10;
        int paddingLeft = this.view.getPaddingLeft();
        float f = paddingLeft;
        float paddingTop = this.view.getPaddingTop();
        this.imageFromPath.moveTo(f, paddingTop);
        this.imageFromPath.lineTo(paddingLeft + width, paddingTop);
        this.imageFromPath.lineTo(f, r3 + width2);
        this.imageFromPath.close();
    }

    public ImageFrom getImageFrom() {
        return this.imageFrom;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDetachedFromWindow() {
        this.imageFrom = null;
        return false;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onDraw(@NonNull Canvas canvas) {
        if (this.imageFrom == null) {
            return;
        }
        if (this.imageFromPath == null) {
            initImageFromPath();
        }
        if (this.imageFromPaint == null) {
            this.imageFromPaint = new Paint();
            this.imageFromPaint.setAntiAlias(true);
        }
        switch (this.imageFrom) {
            case MEMORY_CACHE:
                this.imageFromPaint.setColor(FROM_FLAG_COLOR_MEMORY_CACHE);
                break;
            case DISK_CACHE:
                this.imageFromPaint.setColor(FROM_FLAG_COLOR_DISK_CACHE);
                break;
            case NETWORK:
                this.imageFromPaint.setColor(FROM_FLAG_COLOR_NETWORK);
                break;
            case LOCAL:
                this.imageFromPaint.setColor(FROM_FLAG_COLOR_LOCAL);
                break;
            case MEMORY:
                this.imageFromPaint.setColor(FROM_FLAG_COLOR_MEMORY);
                break;
            default:
                return;
        }
        canvas.drawPath(this.imageFromPath, this.imageFromPaint);
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onDrawableChanged(@NonNull String str, Drawable drawable, Drawable drawable2) {
        ImageFrom imageFrom = this.imageFrom;
        Object lastDrawable = SketchUtils.getLastDrawable(drawable2);
        ImageFrom imageFrom2 = ((lastDrawable instanceof SketchLoadingDrawable) || !(lastDrawable instanceof SketchDrawable)) ? null : ((SketchDrawable) lastDrawable).getImageFrom();
        this.imageFrom = imageFrom2;
        return imageFrom != imageFrom2;
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        initImageFromPath();
    }

    @Override // me.panpf.sketch.viewfun.ViewFunction
    public boolean onReadyDisplay(@Nullable UriModel uriModel) {
        this.imageFrom = null;
        return true;
    }

    public void setImageFrom(ImageFrom imageFrom) {
        this.imageFrom = imageFrom;
    }
}
