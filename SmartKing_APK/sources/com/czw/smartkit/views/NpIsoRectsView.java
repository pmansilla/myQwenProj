package com.czw.smartkit.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.czw.smartkit.R;
import com.czw.utils.MathUtils;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class NpIsoRectsView extends BaseView {
    private static final int hightColor = -2069737;
    private static final int lowColor = -1534891;
    private static final int rectColor = -9878807;
    private float bottomMargin;
    float distance;
    private float downX;
    private ArrayList<RectF> flagRects;
    private int height;
    private float lastX;
    private float leftMargin;
    int maxValue;
    private float moveX;
    private float rectMargin;
    private float rectWidth;
    public ArrayList<String> texts;
    private float topMargin;
    private float tranlateX;
    public ArrayList<int[]> values;
    private int width;

    public NpIsoRectsView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.bottomMargin = 100.0f;
        this.topMargin = 100.0f;
        this.leftMargin = 100.0f;
        this.rectMargin = 100.0f;
        this.rectWidth = 100.0f;
        this.flagRects = new ArrayList<>();
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        this.distance = 0.0f;
        this.width = 0;
        this.height = 0;
        this.maxValue = 2;
        this.texts = new ArrayList<>();
        this.values = new ArrayList<>();
        clearUI();
    }

    private void drawHLine(Canvas canvas) {
        this.paint.setColor(-7829368);
    }

    private void drawRects(Canvas canvas) {
        float f = (this.height - this.bottomMargin) - this.topMargin;
        float f2 = this.height - this.bottomMargin;
        this.flagRects.clear();
        this.maxValue = this.maxValue <= 0 ? 1 : this.maxValue;
        int size = this.values.size();
        this.paint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < size; i++) {
            setColor(i);
            for (int i2 = 0; i2 < this.values.get(i).length; i2++) {
                float f3 = (this.values.get(i)[i2] * 1.0f) / this.maxValue;
                float f4 = this.leftMargin + (((this.rectWidth * size) + this.rectMargin) * i2) + (i * this.rectWidth);
                RectF rectF = new RectF(f4, (int) (f2 - (f3 * f)), this.rectWidth + f4, f2);
                canvas.drawRect(rectF, this.paint);
                this.flagRects.add(rectF);
            }
        }
    }

    private void drawTexts(Canvas canvas) {
        if (this.texts.size() < 1) {
            return;
        }
        float f = this.height - this.bottomMargin;
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setColor(-7829368);
        this.paint.setTextSize(getResources().getDimension(R.dimen.DIMEN_24PX));
        RectF rectF = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        for (int i = 0; i < this.texts.size(); i++) {
            rectF.left = this.leftMargin + (((this.values.size() * this.rectWidth) + this.rectMargin) * i);
            rectF.right = rectF.left + (this.values.size() * this.rectWidth);
            canvas.drawText(this.texts.get(i), rectF.centerX(), 60.0f + f, this.paint);
        }
    }

    private void initWH() {
        this.width = getWidth();
        this.height = getHeight();
    }

    private void setColor(int i) {
        if (i != 1) {
            this.paint.setColor(hightColor);
        } else {
            this.paint.setColor(lowColor);
        }
    }

    @Override // com.czw.smartkit.views.BaseView
    public /* bridge */ /* synthetic */ void afterMeasure() {
        super.afterMeasure();
    }

    public void clearUI() {
        this.values.clear();
        this.texts.clear();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.czw.smartkit.views.BaseView
    public void init(Context context) {
        super.init(context);
        this.paint.setStyle(Paint.Style.FILL);
        this.topMargin = getResources().getDimension(R.dimen.DIMEN_80PX);
        this.bottomMargin = getResources().getDimension(R.dimen.DIMEN_40PX);
        this.rectWidth = getResources().getDimension(R.dimen.DIMEN_40PX);
        this.leftMargin = getResources().getDimension(R.dimen.DIMEN_40PX);
        this.rectMargin = getResources().getDimension(R.dimen.DIMEN_40PX);
        this.paint.setTextSize(this.textUnit * 13.3f);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initWH();
        canvas.translate(this.tranlateX, 0.0f);
        drawHLine(canvas);
        drawRects(canvas);
        drawTexts(canvas);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0079, code lost:
    
        return true;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            int r0 = r5.getAction()
            r1 = 1
            switch(r0) {
                case 0: goto L73;
                case 1: goto L21;
                case 2: goto L9;
                default: goto L8;
            }
        L8:
            goto L79
        L9:
            float r5 = r5.getX()
            r4.moveX = r5
            float r5 = r4.moveX
            float r0 = r4.downX
            float r5 = r5 - r0
            r4.distance = r5
            float r5 = r4.lastX
            float r0 = r4.distance
            float r5 = r5 + r0
            r4.tranlateX = r5
            r4.invalidate()
            goto L79
        L21:
            float r5 = r4.tranlateX
            float r0 = r4.rectWidth
            r2 = 0
            int r5 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
            if (r5 <= 0) goto L2c
            r4.tranlateX = r2
        L2c:
            java.util.ArrayList<android.graphics.RectF> r5 = r4.flagRects
            int r5 = r5.size()
            int r5 = r5 - r1
            float r5 = (float) r5
            float r0 = r4.rectMargin
            float r3 = r4.rectWidth
            float r0 = r0 + r3
            float r5 = r5 * r0
            int r0 = r4.getWidth()
            float r0 = (float) r0
            int r0 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
            if (r0 <= 0) goto L69
            float r0 = r4.tranlateX
            float r0 = r0 + r5
            int r2 = r4.getWidth()
            float r2 = (float) r2
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 > 0) goto L6b
            float r5 = -r5
            int r0 = r4.getWidth()
            float r0 = (float) r0
            float r5 = r5 + r0
            float r0 = r4.leftMargin
            r2 = 1077936128(0x40400000, float:3.0)
            float r0 = r0 * r2
            float r5 = r5 - r0
            float r0 = r4.rectMargin
            float r3 = r4.rectWidth
            float r0 = r0 + r3
            float r0 = r0 * r2
            float r5 = r5 + r0
            r4.tranlateX = r5
            goto L6b
        L69:
            r4.tranlateX = r2
        L6b:
            float r5 = r4.tranlateX
            r4.lastX = r5
            r4.invalidate()
            goto L79
        L73:
            float r5 = r5.getX()
            r4.downX = r5
        L79:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.czw.smartkit.views.NpIsoRectsView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void updateWithUI(ArrayList<String> arrayList, ArrayList<int[]> arrayList2) {
        if (arrayList == null || arrayList2 == null || arrayList.size() < 1 || arrayList2.size() < 1) {
            clearUI();
            return;
        }
        this.texts.clear();
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            this.texts.add(arrayList.get(size));
        }
        this.values.clear();
        for (int size2 = arrayList2.size() - 1; size2 >= 0; size2--) {
            this.values.add(arrayList2.get(size2));
        }
        Iterator<int[]> it = arrayList2.iterator();
        int i = 0;
        while (it.hasNext()) {
            i += it.next().length;
        }
        int[] iArr = new int[i];
        Iterator<int[]> it2 = arrayList2.iterator();
        int i2 = 0;
        while (it2.hasNext()) {
            int[] next = it2.next();
            System.arraycopy(next, 0, iArr, i2, next.length);
            i2 += next.length;
        }
        this.maxValue = MathUtils.getMax(iArr);
        this.tranlateX = 0.0f;
        invalidate();
    }
}
