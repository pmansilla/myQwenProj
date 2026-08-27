package com.czw.smartkit.views.measureView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.czw.smartkit.R;
import com.czw.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MeasureLineShowView extends View {
    private static List<Float> lineDataBeanArrayList = new ArrayList();
    private Bitmap bitmap;
    private Canvas bmpCanvas;
    private float bottomMargin;
    private int cursorLineDateColor;
    private float downX;
    private float lastX;
    private float leftMargin;
    private int lineColor;
    private int lineCount;
    private int maxValue;
    private int minValue;
    private float moveX;
    private Paint paint;
    private int pointColor;
    private float pointRadius;
    private float rightMargin;
    private float startPointSpaceLeft;
    private float textSize;
    private float topMargin;
    private float totalHeight;
    private float totalWidth;
    private float tranlateX;
    private String unitString;
    private int valueSpace;
    private float xSpacePointWidth;
    private float ySpaceLineHeight;

    static {
        lineDataBeanArrayList.clear();
    }

    public MeasureLineShowView(Context context) {
        super(context);
        this.bitmap = null;
        this.bmpCanvas = null;
        this.paint = null;
        this.leftMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.textSize = 10.0f;
        this.lineColor = -514716;
        this.pointColor = -514716;
        this.cursorLineDateColor = -3355444;
        this.unitString = "";
        this.lineCount = 10;
        this.valueSpace = 10;
        this.maxValue = 140;
        this.minValue = 40;
        this.ySpaceLineHeight = 0.0f;
        this.pointRadius = 0.0f;
        this.downX = 0.0f;
        this.moveX = 0.0f;
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        init(context);
    }

    public MeasureLineShowView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.bitmap = null;
        this.bmpCanvas = null;
        this.paint = null;
        this.leftMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.textSize = 10.0f;
        this.lineColor = -514716;
        this.pointColor = -514716;
        this.cursorLineDateColor = -3355444;
        this.unitString = "";
        this.lineCount = 10;
        this.valueSpace = 10;
        this.maxValue = 140;
        this.minValue = 40;
        this.ySpaceLineHeight = 0.0f;
        this.pointRadius = 0.0f;
        this.downX = 0.0f;
        this.moveX = 0.0f;
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        init(context);
    }

    public MeasureLineShowView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.bitmap = null;
        this.bmpCanvas = null;
        this.paint = null;
        this.leftMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.textSize = 10.0f;
        this.lineColor = -514716;
        this.pointColor = -514716;
        this.cursorLineDateColor = -3355444;
        this.unitString = "";
        this.lineCount = 10;
        this.valueSpace = 10;
        this.maxValue = 140;
        this.minValue = 40;
        this.ySpaceLineHeight = 0.0f;
        this.pointRadius = 0.0f;
        this.downX = 0.0f;
        this.moveX = 0.0f;
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        init(context);
    }

    private void clearBitmap() {
        if (this.bmpCanvas == null) {
            return;
        }
        this.bmpCanvas.drawColor(-1, PorterDuff.Mode.CLEAR);
    }

    private void drawCursorLine() {
        float f = (this.totalHeight - this.topMargin) - this.bottomMargin;
        this.lineCount = ((this.maxValue - this.minValue) / this.valueSpace) + 1;
        this.ySpaceLineHeight = f / (this.lineCount - 1.0f);
        this.paint.setColor(this.cursorLineDateColor);
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_1PX));
        for (int i = 0; i < this.lineCount; i++) {
            float f2 = this.topMargin + (this.ySpaceLineHeight * i);
            this.bmpCanvas.drawLine(this.leftMargin, f2, this.totalWidth - this.rightMargin, f2, this.paint);
        }
    }

    private void drawDataPathLineAndDate() {
        if (lineDataBeanArrayList == null || lineDataBeanArrayList.size() < 1) {
            return;
        }
        int size = lineDataBeanArrayList.size();
        float f = (this.totalHeight - this.topMargin) - this.bottomMargin;
        this.paint.setColor(this.pointColor);
        this.paint.setTextAlign(Paint.Align.CENTER);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            Float f2 = lineDataBeanArrayList.get(i2);
            float f3 = this.leftMargin + this.startPointSpaceLeft + (this.xSpacePointWidth * i2);
            float floatValue = this.topMargin + ((1.0f - ((f2.floatValue() - (this.minValue * 1.0f)) / (this.maxValue - this.minValue))) * f);
            this.paint.setColor(this.pointColor);
            this.bmpCanvas.drawCircle(f3, floatValue, this.pointRadius, this.paint);
            arrayList.add(Float.valueOf(f3));
            arrayList2.add(Float.valueOf(floatValue));
        }
        int size2 = arrayList.size();
        if (size2 >= 2) {
            this.paint.setColor(this.lineColor);
            this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_3PX));
            while (i < size2 - 1) {
                int i3 = i + 1;
                this.bmpCanvas.drawLine(((Float) arrayList.get(i)).floatValue(), ((Float) arrayList2.get(i)).floatValue(), ((Float) arrayList.get(i3)).floatValue(), ((Float) arrayList2.get(i3)).floatValue(), this.paint);
                i = i3;
            }
        }
    }

    private void drawLineAndMask() {
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(-1);
        this.bmpCanvas.drawRect(new RectF(0.0f, 0.0f, this.leftMargin, this.totalHeight), this.paint);
        this.bmpCanvas.drawRect(new RectF(this.totalWidth - this.rightMargin, 0.0f, this.totalWidth, this.totalHeight), this.paint);
        this.paint.setColor(this.cursorLineDateColor);
        this.paint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 0; i < this.lineCount; i++) {
            float f = this.topMargin + (this.ySpaceLineHeight * i);
            this.bmpCanvas.drawText(String.format("%d", Integer.valueOf(Float.valueOf(this.maxValue - (this.valueSpace * i)).intValue())) + this.unitString, this.leftMargin - 10.0f, f + (this.textSize / 2.0f), this.paint);
        }
    }

    private void drawMethod() {
        if (this.bmpCanvas == null) {
            return;
        }
        clearBitmap();
        drawCursorLine();
        this.bmpCanvas.save();
        this.bmpCanvas.translate(this.tranlateX, 0.0f);
        drawDataPathLineAndDate();
        this.bmpCanvas.restore();
        drawLineAndMask();
        invalidate();
        LogUtil.e("重绘制====");
    }

    private void init(Context context) {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.textSize = getResources().getDimension(R.dimen.DIMEN_24PX);
        this.leftMargin = getResources().getDimension(R.dimen.DIMEN_66PX);
        this.rightMargin = getResources().getDimension(R.dimen.DIMEN_60PX);
        this.topMargin = getResources().getDimension(R.dimen.DIMEN_20PX);
        this.bottomMargin = getResources().getDimension(R.dimen.DIMEN_60PX);
        this.xSpacePointWidth = context.getResources().getDimension(R.dimen.DIMEN_120PX);
        this.startPointSpaceLeft = context.getResources().getDimension(R.dimen.DIMEN_50PX);
        this.pointRadius = getResources().getDimension(R.dimen.DIMEN_6PX);
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_3PX));
        this.paint.setTextSize(this.textSize);
    }

    public void initColor(int i, int i2) {
        this.lineColor = i;
        this.pointColor = i2;
        drawMethod();
    }

    public void initValue(int i, int i2, int i3, String str) {
        this.maxValue = i;
        this.minValue = i2;
        this.valueSpace = i3;
        this.unitString = str;
        drawMethod();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.bitmap == null) {
            return;
        }
        canvas.drawBitmap(this.bitmap, new Matrix(), null);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.totalWidth = getMeasuredWidth();
        this.totalHeight = getMeasuredHeight();
        this.bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        this.bmpCanvas = new Canvas(this.bitmap);
        drawMethod();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x006b, code lost:
    
        return true;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r4) {
        /*
            r3 = this;
            int r0 = r4.getAction()
            r1 = 1
            switch(r0) {
                case 0: goto L65;
                case 1: goto L1d;
                case 2: goto L9;
                default: goto L8;
            }
        L8:
            goto L6b
        L9:
            float r4 = r4.getX()
            r3.moveX = r4
            float r4 = r3.lastX
            float r0 = r3.moveX
            float r2 = r3.downX
            float r0 = r0 - r2
            float r4 = r4 + r0
            r3.tranlateX = r4
            r3.drawMethod()
            goto L6b
        L1d:
            float r4 = r3.tranlateX
            r0 = 0
            int r4 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r4 <= 0) goto L26
            r3.tranlateX = r0
        L26:
            java.util.List<java.lang.Float> r4 = com.czw.smartkit.views.measureView.MeasureLineShowView.lineDataBeanArrayList
            int r4 = r4.size()
            int r4 = r4 - r1
            float r4 = (float) r4
            float r2 = r3.xSpacePointWidth
            float r4 = r4 * r2
            float r2 = r3.startPointSpaceLeft
            float r4 = r4 + r2
            int r2 = r3.getWidth()
            float r2 = (float) r2
            int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r2 <= 0) goto L5b
            float r0 = r3.tranlateX
            float r0 = r0 + r4
            int r2 = r3.getWidth()
            float r2 = (float) r2
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 > 0) goto L5d
            float r4 = -r4
            int r0 = r3.getWidth()
            float r0 = (float) r0
            float r4 = r4 + r0
            float r0 = r3.rightMargin
            r2 = 1077936128(0x40400000, float:3.0)
            float r0 = r0 * r2
            float r4 = r4 - r0
            r3.tranlateX = r4
            goto L5d
        L5b:
            r3.tranlateX = r0
        L5d:
            float r4 = r3.tranlateX
            r3.lastX = r4
            r3.drawMethod()
            goto L6b
        L65:
            float r4 = r4.getX()
            r3.downX = r4
        L6b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.smartkit.views.measureView.MeasureLineShowView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void updateShow(Float f) {
        lineDataBeanArrayList.clear();
        lineDataBeanArrayList.add(f);
        drawMethod();
    }
}
