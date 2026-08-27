package com.czw.smartkit.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.czw.smartkit.R;
import com.czw.utils.MathUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StepTableView extends View {
    private float bottomMargin;
    private Context context;
    private int countLine;
    private float leftMargin;
    private Paint paint;
    private ArrayList<RectF> rectFArrayList;
    private float rightMargin;
    private float textSize;
    private float topMargin;
    private int[] valArr;

    /* loaded from: classes.dex */
    public static class StepTableData {
        private int xValue;
        private int yValue;

        public StepTableData(int i, int i2) {
            this.xValue = i;
            this.yValue = i2;
        }

        public String toString() {
            return "StepTableData{xValue=" + this.xValue + ", yValue=" + this.yValue + '}';
        }
    }

    public StepTableView(Context context) {
        super(context);
        this.paint = null;
        this.leftMargin = 180.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.textSize = 20.0f;
        this.rectFArrayList = new ArrayList<>();
        this.countLine = 4;
        this.valArr = new int[24];
        init(context);
    }

    public StepTableView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = null;
        this.leftMargin = 180.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.textSize = 20.0f;
        this.rectFArrayList = new ArrayList<>();
        this.countLine = 4;
        this.valArr = new int[24];
        init(context);
    }

    public StepTableView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.paint = null;
        this.leftMargin = 180.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.textSize = 20.0f;
        this.rectFArrayList = new ArrayList<>();
        this.countLine = 4;
        this.valArr = new int[24];
        init(context);
    }

    private void drawStepBar(Canvas canvas) {
        if (this.rectFArrayList.size() < 1) {
            return;
        }
        this.paint.setColor(-6171136);
        this.paint.setStyle(Paint.Style.FILL);
        float height = (getHeight() - this.topMargin) - this.bottomMargin;
        float max = MathUtils.getMax(this.valArr) + 50.0f;
        int size = this.rectFArrayList.size();
        for (int i = 0; i < size - 1; i++) {
            RectF rectF = this.rectFArrayList.get(i);
            rectF.top = ((1.0f - (this.valArr[i] / max)) * height) + this.topMargin;
            float width = rectF.width() / 3.0f;
            RectF rectF2 = new RectF();
            rectF2.left = rectF.centerX() - width;
            rectF2.right = rectF.centerX() + width;
            rectF2.top = rectF.top;
            rectF2.bottom = rectF.bottom;
            canvas.drawRoundRect(rectF2, rectF2.width() / 2.0f, rectF2.width() / 2.0f, this.paint);
            rectF2.top = rectF2.bottom - (rectF2.height() / 2.0f);
            canvas.drawRect(rectF2, this.paint);
        }
    }

    private void drawTime(Canvas canvas) {
        this.rectFArrayList.clear();
        this.paint.setColor(-1);
        this.paint.setTextAlign(Paint.Align.CENTER);
        float width = ((getWidth() - this.leftMargin) - this.rightMargin) / 25.0f;
        int i = 0;
        while (i < 25) {
            int i2 = i + 1;
            RectF rectF = new RectF(this.leftMargin + (i * width), this.topMargin, this.leftMargin + (i2 * width), getHeight() - this.bottomMargin);
            this.paint.setStyle(Paint.Style.STROKE);
            this.rectFArrayList.add(rectF);
            this.paint.setStyle(Paint.Style.FILL);
            if (i == 1 || ((i % 6 == 0 && i > 1 && i < 24) || i == 23)) {
                canvas.drawText(i + "", rectF.centerX(), getHeight() - (this.bottomMargin / 2.0f), this.paint);
            }
            i = i2;
        }
    }

    private void drawUnitAndLine(Canvas canvas) {
        float height = getHeight();
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setTextAlign(Paint.Align.CENTER);
        float f = ((height - this.topMargin) - this.bottomMargin) / (this.countLine - 1);
        float f2 = 20.0f;
        if (this.valArr != null && this.valArr.length > 1) {
            float max = MathUtils.getMax(this.valArr) + 50.0f;
            if (max != 0.0f) {
                f2 = max;
            }
        }
        int i = (int) (f2 / (this.countLine - 1));
        for (int i2 = 0; i2 < this.countLine; i2++) {
            float f3 = this.topMargin + (i2 * f);
            this.paint.setColor(Color.parseColor("#E0E0E0"));
            canvas.drawLine(this.leftMargin, f3, getWidth() - this.rightMargin, f3, this.paint);
            this.paint.setColor(-1);
            canvas.drawText(String.format("%d", Integer.valueOf(Float.valueOf(f2 - (i * i2)).intValue())), this.leftMargin / 2.0f, f3 + 10.0f, this.paint);
        }
    }

    private void init(Context context) {
        this.context = context;
        this.textSize = getResources().getDimension(R.dimen.DIMEN_20PX);
        this.leftMargin = getResources().getDimension(R.dimen.DIMEN_64PX);
        this.rightMargin = getResources().getDimension(R.dimen.DIMEN_64PX);
        this.topMargin = getResources().getDimension(R.dimen.DIMEN_20PX);
        this.bottomMargin = getResources().getDimension(R.dimen.DIMEN_48PX);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_2PX) / 2.0f);
        this.paint.setColor(-1);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setTextSize(this.textSize);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawUnitAndLine(canvas);
        drawTime(canvas);
        drawStepBar(canvas);
    }

    public void updateData(List<StepTableData> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        this.valArr = new int[24];
        int size = list.size();
        for (int i = 0; i < size; i++) {
            StepTableData stepTableData = list.get(i);
            if (stepTableData.xValue >= 0 && stepTableData.xValue <= 24) {
                this.valArr[stepTableData.xValue] = stepTableData.yValue;
            }
        }
        invalidate();
    }
}
