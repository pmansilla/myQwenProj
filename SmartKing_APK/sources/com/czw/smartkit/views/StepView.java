package com.czw.smartkit.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.czw.smartkit.R;
import com.czw.smartkit.util.BitmapUtils;

/* loaded from: classes.dex */
public class StepView extends SquareView {
    private static final int bgColor = -2236963;
    private static final int progressColor = -6040320;
    private Bitmap bitmap;
    private float value;

    public StepView(Context context) {
        super(context);
        this.value = 0.0f;
    }

    public StepView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.value = 0.0f;
    }

    public StepView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.value = 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.views.SquareView
    public void init(Context context) {
        super.init(context);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.bitmap = BitmapUtils.resizeBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bmp_step), (int) getResources().getDimension(R.dimen.DIMEN_80PX));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        RectF rectF = new RectF(25.0f, 25.0f, this.width - 25.0f, this.height - 25.0f);
        this.paint.setColor(bgColor);
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_2PX));
        canvas.drawArc(rectF, 135.0f, 330.0f, false, this.paint);
        float width = getWidth() / 2;
        double sqrt = Math.sqrt(3.0d) / 2.0d;
        Double.isNaN(r1);
        Double.isNaN(r1);
        canvas.drawBitmap(this.bitmap, (width / 2.0f) - (this.bitmap.getWidth() / 2), ((float) (r1 + (sqrt * r1))) - ((this.bitmap.getHeight() / 2) + 20.0f), this.paint);
        if (this.value >= 330.0f) {
            this.value = 330.0f;
        }
        this.paint.setColor(progressColor);
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_16PX));
        canvas.drawArc(rectF, 135.0f, this.value, false, this.paint);
    }

    public void updateShow(float f) {
        this.value = f * 330.0f;
        invalidate();
    }
}
