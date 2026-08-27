package com.czw.smartkit.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class NpHgView extends BaseView {
    private static final int borderColor = -3355444;
    private static final int hgLowColor = -1534891;
    private static final int hgMoreColor = -2069737;
    private Handler handler;
    boolean hasResult;
    int height;
    private boolean isRuning;
    private Path path;
    Path path1;
    Path path2;
    private float percent;
    Runnable run;
    int width;
    private int xTime;

    public NpHgView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.path1 = new Path();
        this.path2 = new Path();
        this.path = new Path();
        this.width = 0;
        this.height = 0;
        this.xTime = 5;
        this.handler = new Handler();
        this.run = new Runnable() { // from class: com.czw.smartkit.views.NpHgView.1
            @Override // java.lang.Runnable
            public void run() {
                NpHgView.this.path1.reset();
                NpHgView.this.path2.reset();
                NpHgView.this.path1.moveTo(38.0f, NpHgView.this.height - 46);
                NpHgView.this.path1.lineTo(38.0f, NpHgView.this.height - 120);
                NpHgView.this.path2.moveTo(38.0f, NpHgView.this.height - 46);
                NpHgView.this.path2.lineTo(38.0f, NpHgView.this.height - 120);
                for (float f = 38.0f; f <= (NpHgView.this.width - 30) - 8; f += 2.0f) {
                    float f2 = NpHgView.this.xTime + f;
                    if (NpHgView.this.hasResult) {
                        NpHgView.this.path1.lineTo(f, NpHgView.getY(f2, 0.8f, 25.0f, NpHgView.this.height * (1.0f - NpHgView.this.percent)));
                        NpHgView.this.path2.lineTo(f, NpHgView.getY(f2 * 1.2f, 0.8f, 25.0f, NpHgView.this.height * (1.0f - NpHgView.this.percent)));
                    } else {
                        NpHgView.this.path1.lineTo(f, NpHgView.getY(f2, 0.8f, 25.0f, NpHgView.this.height - 200));
                        NpHgView.this.path2.lineTo(f, NpHgView.getY(f2 * 1.2f, 0.8f, 25.0f, NpHgView.this.height - 200));
                    }
                }
                NpHgView.this.path1.lineTo((NpHgView.this.width - 30) - 8, NpHgView.this.height - 46);
                NpHgView.this.path2.lineTo((NpHgView.this.width - 30) - 8, NpHgView.this.height - 46);
                NpHgView.this.xTime += 10;
                NpHgView.this.invalidate();
                if (NpHgView.this.isRuning) {
                    NpHgView.this.postDelayed(this, 50L);
                }
            }
        };
        this.hasResult = false;
        this.isRuning = false;
        this.percent = 0.5f;
    }

    private void drawHG(Canvas canvas) {
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(hgLowColor);
        canvas.drawPath(this.path2, this.paint);
        this.paint.setColor(hgMoreColor);
        canvas.drawPath(this.path1, this.paint);
    }

    private void drawHGBorder(Canvas canvas) {
        initWH();
        int i = this.height - 30;
        RectF rectF = new RectF(0.0f, i - 15, this.width, this.height - 15);
        this.paint.setColor(borderColor);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeWidth(1.0f);
        float f = 15;
        canvas.drawRoundRect(rectF, f, f, this.paint);
        this.path.reset();
        float f2 = 30;
        this.path.moveTo(f2, this.width / 2);
        float f3 = i;
        this.path.lineTo(f2, f3);
        this.path.lineTo(this.width - 30, f3);
        this.path.lineTo(this.width - 30, this.width / 2);
        this.path.addArc(new RectF(f2, f2, this.width - 30, this.width - 30), 0.0f, -180.0f);
        this.paint.setStrokeWidth(16.0f);
        this.paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(this.path, this.paint);
    }

    public static float getY(float f, float f2, float f3, float f4) {
        double d = f3;
        double d2 = f * f2;
        Double.isNaN(d2);
        double sin = Math.sin(d2 * 0.017453292519943295d);
        Double.isNaN(d);
        double d3 = d * sin;
        double d4 = f4;
        Double.isNaN(d4);
        return (float) (d3 + d4);
    }

    private void initWH() {
        this.width = getWidth();
        this.height = getHeight();
    }

    @Override // com.czw.smartkit.views.BaseView
    public void afterMeasure() {
        super.afterMeasure();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.views.BaseView
    public void init(Context context) {
        super.init(context);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.viewUnit * 4.0f);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initWH();
        drawHGBorder(canvas);
        drawHG(canvas);
    }

    public void start() {
        this.isRuning = true;
        this.hasResult = false;
        this.handler.postDelayed(this.run, 100L);
    }

    public void stop() {
        this.isRuning = false;
        this.handler.removeCallbacks(this.run);
    }

    public void update(float f) {
        this.isRuning = false;
        this.hasResult = true;
        this.handler.removeCallbacks(this.run);
        this.percent = f;
        invalidate();
    }

    public void updateAndStop(float f) {
        update(f);
        stop();
    }
}
