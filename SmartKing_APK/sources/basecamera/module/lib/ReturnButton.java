package basecamera.module.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/* loaded from: classes.dex */
public class ReturnButton extends View {
    private int center_X;
    private int center_Y;
    private Paint paint;
    Path path;
    private int size;
    private float strokeWidth;

    public ReturnButton(Context context) {
        super(context);
    }

    public ReturnButton(Context context, int i) {
        this(context);
        this.size = i;
        int i2 = i / 2;
        this.center_X = i2;
        this.center_Y = i2;
        this.strokeWidth = i / 15.0f;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(-1);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.strokeWidth);
        this.path = new Path();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.path.moveTo(this.strokeWidth, this.strokeWidth / 2.0f);
        this.path.lineTo(this.center_X, this.center_Y - (this.strokeWidth / 2.0f));
        this.path.lineTo(this.size - this.strokeWidth, this.strokeWidth / 2.0f);
        canvas.drawPath(this.path, this.paint);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(this.size, this.size / 2);
    }
}
