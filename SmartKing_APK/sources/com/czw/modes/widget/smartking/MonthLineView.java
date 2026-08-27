package com.czw.modes.widget.smartking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.amap.location.common.model.AmapLoc;
import com.czw.R;
import com.czw.utils.MathUtils;

/* loaded from: classes.dex */
public class MonthLineView extends View {
    private float bottomMargin;
    private Context context;
    private int[] dataVal;
    private float leftMargin;
    private int lineColor;
    private float marginY;
    private Paint paint;
    private float rightMargin;
    private float textSize;
    private float topMargin;

    public MonthLineView(Context context) {
        super(context);
        this.context = null;
        this.paint = null;
        this.marginY = 2.0f;
        this.textSize = 20.0f;
        this.lineColor = -1;
        this.dataVal = new int[28];
        init(context);
    }

    public MonthLineView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = null;
        this.paint = null;
        this.marginY = 2.0f;
        this.textSize = 20.0f;
        this.lineColor = -1;
        this.dataVal = new int[28];
        init(context);
    }

    public MonthLineView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.context = null;
        this.paint = null;
        this.marginY = 2.0f;
        this.textSize = 20.0f;
        this.lineColor = -1;
        this.dataVal = new int[28];
        init(context);
    }

    private void drawLines(Canvas canvas) {
        int length = this.dataVal.length;
        if (this.dataVal != null) {
            if (length < 1) {
                return;
            }
            int max = MathUtils.getMax(this.dataVal);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setColor(this.lineColor);
            if (max <= 0) {
                float height = (getHeight() - this.bottomMargin) - this.marginY;
                canvas.drawLine(this.leftMargin, height, getWidth() - this.rightMargin, height, this.paint);
                return;
            }
            Path path = new Path();
            float height2 = ((getHeight() - this.topMargin) - this.bottomMargin) - (this.marginY * 2.0f);
            float width = ((getWidth() - this.leftMargin) - this.rightMargin) / (length - 1);
            float f = max;
            path.moveTo(this.leftMargin, this.topMargin + this.marginY + ((1.0f - ((this.dataVal[0] * 1.0f) / f)) * height2));
            for (int i = 1; i < length; i++) {
                path.lineTo(this.leftMargin + (i * width), this.topMargin + this.marginY + ((1.0f - ((this.dataVal[i] * 1.0f) / f)) * height2));
            }
            canvas.drawPath(path, this.paint);
        }
    }

    private void drawTexts(Canvas canvas) {
        int length = this.dataVal.length;
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setColor(-1);
        if (this.dataVal == null || length < 1) {
            return;
        }
        float width = ((getWidth() - this.leftMargin) - this.rightMargin) / (length - 1);
        float height = (getHeight() - this.bottomMargin) + this.textSize + (this.marginY * 2.0f);
        canvas.drawText(AmapLoc.RESULT_TYPE_WIFI_ONLY, this.leftMargin, height, this.paint);
        StringBuilder sb = new StringBuilder();
        int i = length / 2;
        sb.append(i + 1);
        sb.append("");
        canvas.drawText(sb.toString(), this.leftMargin + (width * i), height, this.paint);
        canvas.drawText(length + "", getWidth() - this.rightMargin, height, this.paint);
    }

    private void drawTopAndBottomLine(Canvas canvas) {
        this.paint.setColor(-1);
        float f = this.topMargin - this.marginY;
        canvas.drawLine(this.leftMargin, f, getWidth() - this.rightMargin, f, this.paint);
        float height = (getHeight() - this.bottomMargin) + this.marginY;
        canvas.drawLine(this.leftMargin, height, getWidth() - this.rightMargin, height, this.paint);
    }

    private void init(Context context) {
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.leftMargin = getResources().getDimension(R.dimen.DIMEN_40PX);
        this.rightMargin = getResources().getDimension(R.dimen.DIMEN_40PX);
        this.topMargin = getResources().getDimension(R.dimen.DIMEN_12PX);
        this.bottomMargin = getResources().getDimension(R.dimen.DIMEN_40PX);
        this.marginY = getResources().getDimension(R.dimen.DIMEN_4PX);
        this.textSize = getResources().getDimension(R.dimen.DIMEN_20PX);
        this.paint.setTextSize(this.textSize);
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_2PX));
    }

    public void initCfg(int i) {
        this.lineColor = i;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTopAndBottomLine(canvas);
        drawLines(canvas);
        drawTexts(canvas);
    }

    public void updateShow(int[] iArr) {
        this.dataVal = iArr;
        invalidate();
    }
}
