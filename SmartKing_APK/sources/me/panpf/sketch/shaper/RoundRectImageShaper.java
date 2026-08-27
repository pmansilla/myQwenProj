package me.panpf.sketch.shaper;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.request.ShapeSize;

/* loaded from: classes2.dex */
public class RoundRectImageShaper implements ImageShaper {
    private Path bitmapPath;
    private Rect boundsBack;
    private Rect boundsCached;
    private Path clipPath;
    private Path innerStrokePath;
    private float[] outerRadii;
    private Path outerStrokePath;
    private Path path;
    private RectF rectF;
    private int strokeColor;
    private Paint strokePaint;
    private int strokeWidth;

    public RoundRectImageShaper(float f) {
        this(f, f, f, f);
    }

    public RoundRectImageShaper(float f, float f2, float f3, float f4) {
        this(new float[]{f, f, f2, f2, f3, f3, f4, f4});
    }

    public RoundRectImageShaper(float[] fArr) {
        this.boundsCached = new Rect();
        this.bitmapPath = new Path();
        if (fArr == null || fArr.length < 8) {
            throw new ArrayIndexOutOfBoundsException("outer radii must have >= 8 values");
        }
        this.outerRadii = fArr;
    }

    private boolean hasStroke() {
        return this.strokeColor != 0 && this.strokeWidth > 0;
    }

    private void updatePaint() {
        if (hasStroke()) {
            if (this.strokePaint == null) {
                this.strokePaint = new Paint();
                this.strokePaint.setStyle(Paint.Style.STROKE);
                this.strokePaint.setAntiAlias(true);
            }
            this.strokePaint.setColor(this.strokeColor);
            this.strokePaint.setStrokeWidth(this.strokeWidth);
            if (this.innerStrokePath == null) {
                this.innerStrokePath = new Path();
            }
            if (this.outerStrokePath == null) {
                this.outerStrokePath = new Path();
            }
            if (this.clipPath == null) {
                this.clipPath = new Path();
            }
        }
    }

    @Override // me.panpf.sketch.shaper.ImageShaper
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {
        if (!this.boundsCached.equals(rect)) {
            RectF rectF = new RectF(rect);
            this.bitmapPath.reset();
            this.bitmapPath.addRoundRect(rectF, this.outerRadii, Path.Direction.CW);
            if (hasStroke()) {
                float f = this.strokeWidth / 2.0f;
                rectF.set(rect.left + f, rect.top + f, rect.right - f, rect.bottom - f);
                this.innerStrokePath.reset();
                this.innerStrokePath.addRoundRect(rectF, this.outerRadii, Path.Direction.CW);
                this.outerStrokePath.reset();
                rectF.set(rect.left, rect.top, rect.right, rect.bottom);
                this.outerStrokePath.addRoundRect(rectF, this.outerRadii, Path.Direction.CW);
                rectF.set(rect);
                this.clipPath.addRoundRect(rectF, this.outerRadii, Path.Direction.CW);
            }
        }
        paint.setAntiAlias(true);
        canvas.drawPath(this.bitmapPath, paint);
        if (!hasStroke() || this.strokePaint == null) {
            return;
        }
        canvas.clipPath(this.clipPath);
        canvas.drawPath(this.innerStrokePath, this.strokePaint);
        canvas.drawPath(this.outerStrokePath, this.strokePaint);
    }

    public float[] getOuterRadii() {
        return this.outerRadii;
    }

    @Override // me.panpf.sketch.shaper.ImageShaper
    @NonNull
    public Path getPath(@NonNull Rect rect) {
        if (this.path != null && this.boundsBack != null && this.boundsBack.equals(rect)) {
            return this.path;
        }
        if (this.boundsBack == null) {
            this.boundsBack = new Rect();
        }
        this.boundsBack.set(rect);
        if (this.path == null) {
            this.path = new Path();
        }
        this.path.reset();
        if (this.rectF == null) {
            this.rectF = new RectF();
        }
        this.rectF.set(this.boundsBack);
        this.path.addRoundRect(this.rectF, this.outerRadii, Path.Direction.CW);
        return this.path;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    @Override // me.panpf.sketch.shaper.ImageShaper
    public void onUpdateShaderMatrix(@NonNull Matrix matrix, @NonNull Rect rect, int i, int i2, @Nullable ShapeSize shapeSize, @NonNull Rect rect2) {
    }

    @NonNull
    public RoundRectImageShaper setStroke(int i, int i2) {
        this.strokeColor = i;
        this.strokeWidth = i2;
        updatePaint();
        return this;
    }
}
