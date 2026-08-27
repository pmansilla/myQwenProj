package me.panpf.sketch.shaper;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.panpf.sketch.request.ShapeSize;

/* loaded from: classes2.dex */
public class CircleImageShaper implements ImageShaper {
    private Rect boundsBack;
    private Path path;
    private int strokeColor;
    private Paint strokePaint;
    private int strokeWidth;

    private void updatePaint() {
        if (this.strokeColor == 0 || this.strokeWidth <= 0) {
            return;
        }
        if (this.strokePaint == null) {
            this.strokePaint = new Paint();
            this.strokePaint.setStyle(Paint.Style.STROKE);
            this.strokePaint.setAntiAlias(true);
        }
        this.strokePaint.setColor(this.strokeColor);
        this.strokePaint.setStrokeWidth(this.strokeWidth);
    }

    @Override // me.panpf.sketch.shaper.ImageShaper
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {
        float width = rect.width() / 2.0f;
        float height = rect.height() / 2.0f;
        float f = rect.left + width;
        float f2 = rect.top + height;
        float min = Math.min(width, height);
        paint.setAntiAlias(true);
        canvas.drawCircle(f, f2, min, paint);
        if (this.strokeColor == 0 || this.strokeWidth <= 0 || this.strokePaint == null) {
            return;
        }
        canvas.drawCircle(f, f2, min - (this.strokeWidth / 2.0f), this.strokePaint);
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
        this.path.addCircle(rect.centerX(), rect.centerX(), Math.max(r0 - rect.left, r1 - rect.top), Path.Direction.CW);
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
    public CircleImageShaper setStroke(int i, int i2) {
        this.strokeColor = i;
        this.strokeWidth = i2;
        updatePaint();
        return this;
    }
}
