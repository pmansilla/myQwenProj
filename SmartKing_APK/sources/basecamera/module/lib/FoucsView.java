package basecamera.module.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import basecamera.module.lib.util.ScreenUtils;

/* loaded from: classes.dex */
public class FoucsView extends View {
    private int center_x;
    private int center_y;
    private int length;
    private Paint mPaint;
    private int size;

    public FoucsView(Context context) {
        this(context, null);
    }

    public FoucsView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FoucsView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.size = ScreenUtils.getScreenWidth(context) / 3;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setColor(-300503530);
        this.mPaint.setStrokeWidth(4.0f);
        this.mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(this.center_x - this.length, this.center_y - this.length, this.center_x + this.length, this.center_y + this.length, this.mPaint);
        canvas.drawLine(2.0f, getHeight() / 2, this.size / 10, getHeight() / 2, this.mPaint);
        canvas.drawLine(getWidth() - 2, getHeight() / 2, getWidth() - (this.size / 10), getHeight() / 2, this.mPaint);
        canvas.drawLine(getWidth() / 2, 2.0f, getWidth() / 2, this.size / 10, this.mPaint);
        canvas.drawLine(getWidth() / 2, getHeight() - 2, getWidth() / 2, getHeight() - (this.size / 10), this.mPaint);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        double d = this.size;
        Double.isNaN(d);
        this.center_x = (int) (d / 2.0d);
        double d2 = this.size;
        Double.isNaN(d2);
        this.center_y = (int) (d2 / 2.0d);
        Double.isNaN(this.size);
        this.length = ((int) (r3 / 2.0d)) - 2;
        setMeasuredDimension(this.size, this.size);
    }
}
