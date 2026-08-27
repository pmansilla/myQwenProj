package basecamera.module.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

/* loaded from: classes.dex */
public class TypeButton extends View {
    public static final int TYPE_CANCEL = 1;
    public static final int TYPE_CONFIRM = 2;
    private float button_radius;
    private int button_size;
    private int button_type;
    private float center_X;
    private float center_Y;
    private float index;
    private Paint mPaint;
    private Path path;
    private RectF rectF;
    private float strokeWidth;

    public TypeButton(Context context) {
        super(context);
    }

    public TypeButton(Context context, int i, int i2) {
        super(context);
        this.button_type = i;
        this.button_size = i2;
        float f = i2;
        float f2 = f / 2.0f;
        this.button_radius = f2;
        this.center_X = f2;
        this.center_Y = f2;
        this.mPaint = new Paint();
        this.path = new Path();
        this.strokeWidth = f / 50.0f;
        this.index = this.button_size / 12.0f;
        this.rectF = new RectF(this.center_X, this.center_Y - this.index, this.center_X + (this.index * 2.0f), this.center_Y + this.index);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.button_type == 1) {
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(-287515428);
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(this.center_X, this.center_Y, this.button_radius, this.mPaint);
            this.mPaint.setColor(-16777216);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(this.strokeWidth);
            this.path.moveTo(this.center_X - (this.index / 7.0f), this.center_Y + this.index);
            this.path.lineTo(this.center_X + this.index, this.center_Y + this.index);
            this.path.arcTo(this.rectF, 90.0f, -180.0f);
            this.path.lineTo(this.center_X - this.index, this.center_Y - this.index);
            canvas.drawPath(this.path, this.mPaint);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.path.reset();
            Path path = this.path;
            float f = this.center_X - this.index;
            double d = this.center_Y;
            double d2 = this.index;
            Double.isNaN(d2);
            Double.isNaN(d);
            path.moveTo(f, (float) (d - (d2 * 1.5d)));
            Path path2 = this.path;
            float f2 = this.center_X - this.index;
            double d3 = this.center_Y;
            double d4 = this.index;
            Double.isNaN(d4);
            Double.isNaN(d3);
            path2.lineTo(f2, (float) (d3 - (d4 / 2.3d)));
            Path path3 = this.path;
            double d5 = this.center_X;
            double d6 = this.index;
            Double.isNaN(d6);
            Double.isNaN(d5);
            path3.lineTo((float) (d5 - (d6 * 1.6d)), this.center_Y - this.index);
            this.path.close();
            canvas.drawPath(this.path, this.mPaint);
        }
        if (this.button_type == 2) {
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(-1);
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(this.center_X, this.center_Y, this.button_radius, this.mPaint);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(-16724992);
            this.mPaint.setStrokeWidth(this.strokeWidth);
            this.path.moveTo(this.center_X - (this.button_size / 6.0f), this.center_Y);
            this.path.lineTo(this.center_X - (this.button_size / 21.2f), this.center_Y + (this.button_size / 7.7f));
            this.path.lineTo(this.center_X + (this.button_size / 4.0f), this.center_Y - (this.button_size / 8.5f));
            this.path.lineTo(this.center_X - (this.button_size / 21.2f), this.center_Y + (this.button_size / 9.4f));
            this.path.close();
            canvas.drawPath(this.path, this.mPaint);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(this.button_size, this.button_size);
    }
}
