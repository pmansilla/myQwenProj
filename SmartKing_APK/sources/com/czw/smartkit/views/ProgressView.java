package com.czw.smartkit.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class ProgressView extends BaseView {
    private int paintW;
    float precent;
    private int unit;

    public ProgressView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.unit = 1;
        this.paintW = 20;
        this.precent = 0.0f;
    }

    private void drawBgCircle(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        this.paint.setColor(Color.parseColor("#EEEEEE"));
        this.paint.setStrokeWidth(this.paintW);
        this.paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width / 2, height / 2, r0 - 10, this.paint);
    }

    private void drawProgress(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        this.paint.setColor(Color.parseColor("#1D91C1"));
        this.paint.setStrokeWidth(this.paintW);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(new RectF((this.paintW / 2) + 0, (this.paintW / 2) + 0, width - (this.paintW / 2), height - (this.paintW / 2)), -90.0f, this.precent * 360.0f, false, this.paint);
    }

    @Override // com.czw.smartkit.views.BaseView
    public /* bridge */ /* synthetic */ void afterMeasure() {
        super.afterMeasure();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.views.BaseView
    public void init(Context context) {
        super.init(context);
        this.paint.setStyle(Paint.Style.FILL);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBgCircle(canvas);
        drawProgress(canvas);
    }

    public void updateProgress(float f) {
        this.precent = f;
        invalidate();
    }
}
