package com.czw.smartkit.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.czw.smartkit.R;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class LineView extends View {
    private float bottomMargin;
    private Bitmap bufferBm;
    private Canvas bufferCanvas;
    private int cut;
    private float downX;
    private boolean isUpdateView;
    private float lastX;
    private float leftMargin;
    private int lineColor;
    private int max;
    private int min;
    private float moveX;
    private Paint paint;
    private float raduis;
    private float rightMargin;
    private float textSize;
    private float topMargin;
    private float tranlateX;
    private String unit;
    private float updateTrans;
    private ArrayList<Float> valArr;
    private float viewHeight;
    private float viewWidth;
    private float xAddDis;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Point {
        private float x;
        private float y;

        public Point(float f, float f2) {
            this.x = f;
            this.y = f2;
        }
    }

    public LineView(Context context) {
        super(context);
        this.bufferBm = null;
        this.bufferCanvas = null;
        this.viewWidth = 0.0f;
        this.viewHeight = 0.0f;
        this.lineColor = SupportMenu.CATEGORY_MASK;
        this.paint = null;
        this.leftMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.xAddDis = 20.0f;
        this.textSize = 10.0f;
        this.raduis = 0.0f;
        this.max = 100;
        this.min = 40;
        this.cut = 10;
        this.updateTrans = 0.0f;
        this.valArr = new ArrayList<>();
        this.unit = "";
        this.downX = 0.0f;
        this.moveX = 0.0f;
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        this.isUpdateView = false;
        init(context);
    }

    public LineView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.bufferBm = null;
        this.bufferCanvas = null;
        this.viewWidth = 0.0f;
        this.viewHeight = 0.0f;
        this.lineColor = SupportMenu.CATEGORY_MASK;
        this.paint = null;
        this.leftMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.xAddDis = 20.0f;
        this.textSize = 10.0f;
        this.raduis = 0.0f;
        this.max = 100;
        this.min = 40;
        this.cut = 10;
        this.updateTrans = 0.0f;
        this.valArr = new ArrayList<>();
        this.unit = "";
        this.downX = 0.0f;
        this.moveX = 0.0f;
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        this.isUpdateView = false;
        init(context);
    }

    public LineView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.bufferBm = null;
        this.bufferCanvas = null;
        this.viewWidth = 0.0f;
        this.viewHeight = 0.0f;
        this.lineColor = SupportMenu.CATEGORY_MASK;
        this.paint = null;
        this.leftMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.xAddDis = 20.0f;
        this.textSize = 10.0f;
        this.raduis = 0.0f;
        this.max = 100;
        this.min = 40;
        this.cut = 10;
        this.updateTrans = 0.0f;
        this.valArr = new ArrayList<>();
        this.unit = "";
        this.downX = 0.0f;
        this.moveX = 0.0f;
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        this.isUpdateView = false;
        init(context);
    }

    private void drawBitmap() {
        this.bufferCanvas.drawColor(-1);
        drawLine(this.bufferCanvas);
        this.bufferCanvas.save();
        this.bufferCanvas.translate(this.tranlateX, 0.0f);
        drawPathAndPoint(this.bufferCanvas);
        this.bufferCanvas.restore();
        drawUnit(this.bufferCanvas);
        invalidate();
    }

    private void drawLine(Canvas canvas) {
        int i = ((this.max - this.min) / this.cut) + 1;
        float f = ((this.viewHeight - this.topMargin) - this.bottomMargin) / (i - 1.0f);
        this.paint.setColor(Color.parseColor("#f0f0f0"));
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_2PX));
        for (int i2 = 0; i2 < i; i2++) {
            float f2 = this.topMargin + (i2 * f);
            canvas.drawLine(this.leftMargin, f2, this.viewWidth - this.rightMargin, f2, this.paint);
        }
    }

    private void drawPathAndPoint(Canvas canvas) {
        if (this.valArr.size() < 1) {
            return;
        }
        this.paint.setStyle(Paint.Style.STROKE);
        ArrayList arrayList = new ArrayList();
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_4PX));
        this.paint.setColor(this.lineColor);
        float f = (this.viewHeight - this.topMargin) - this.bottomMargin;
        Path path = new Path();
        float floatValue = (1.0f - (Float.valueOf(this.valArr.get(0).floatValue() - this.min).floatValue() / (this.max - (this.min * 1.0f)))) * f;
        arrayList.add(new Point(this.leftMargin, this.topMargin + floatValue));
        path.moveTo(this.leftMargin, floatValue + this.topMargin);
        for (int i = 1; i < this.valArr.size(); i++) {
            float floatValue2 = 1.0f - (Float.valueOf(this.valArr.get(i).floatValue() - this.min).floatValue() / (this.max - (this.min * 1.0f)));
            float f2 = (i * this.xAddDis) + this.leftMargin;
            float f3 = (floatValue2 * f) + this.topMargin;
            path.lineTo(f2, f3);
            arrayList.add(new Point(f2, f3));
        }
        canvas.drawPath(path, this.paint);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(this.lineColor);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Point point = (Point) it.next();
            canvas.drawCircle(point.x, point.y, this.raduis, this.paint);
        }
        if (this.isUpdateView) {
            float size = this.leftMargin + ((arrayList.size() - 1) * this.xAddDis);
            float f4 = this.tranlateX;
            if (this.tranlateX + size >= this.viewWidth - (this.rightMargin * 2.0f)) {
                float width = (f4 + getWidth()) - ((size + this.tranlateX) + (this.rightMargin * 2.0f));
                this.updateTrans = width;
                this.tranlateX = width;
            }
        }
    }

    private void drawUnit(Canvas canvas) {
        int i = ((this.max - this.min) / this.cut) + 1;
        this.paint.setColor(-1);
        this.paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new RectF(0.0f, 0.0f, this.leftMargin, getHeight()), this.paint);
        canvas.drawRect(new RectF(this.viewWidth - this.rightMargin, 0.0f, this.viewWidth, getHeight()), this.paint);
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setColor(Color.parseColor("#888888"));
        float f = ((this.viewHeight - this.topMargin) - this.bottomMargin) / (i - 1.0f);
        for (int i2 = 0; i2 < i; i2++) {
            canvas.drawText((this.max - (this.cut * i2)) + this.unit, this.leftMargin / 2.0f, this.topMargin + (i2 * f) + (this.textSize / 3.0f), this.paint);
        }
    }

    private void init(Context context) {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.textSize = getResources().getDimension(R.dimen.DIMEN_24PX);
        this.leftMargin = getResources().getDimension(R.dimen.DIMEN_60PX);
        this.rightMargin = getResources().getDimension(R.dimen.DIMEN_30PX);
        this.topMargin = getResources().getDimension(R.dimen.DIMEN_20PX);
        this.bottomMargin = getResources().getDimension(R.dimen.DIMEN_60PX);
        this.xAddDis = getResources().getDimension(R.dimen.DIMEN_60PX);
        this.raduis = getResources().getDimension(R.dimen.DIMEN_4PX);
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_2PX));
        this.paint.setTextSize(this.textSize);
    }

    public void clear() {
        this.valArr.clear();
        this.tranlateX = 0.0f;
        drawBitmap();
    }

    public void initUnit(String str) {
        this.unit = str;
        invalidate();
    }

    public void initValue(int i, int i2, int i3, int i4) {
        this.max = i;
        this.min = i2;
        this.cut = i3;
        this.lineColor = i4;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.bufferBm, 0.0f, 0.0f, (Paint) null);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.viewWidth = getMeasuredWidth();
        this.viewHeight = getMeasuredHeight();
        this.bufferBm = Bitmap.createBitmap((int) this.viewWidth, (int) this.viewHeight, Bitmap.Config.ARGB_8888);
        this.bufferCanvas = new Canvas(this.bufferBm);
        drawBitmap();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.downX = motionEvent.getX();
                return true;
            case 1:
                if (this.tranlateX > this.leftMargin) {
                    this.tranlateX = 0.0f;
                }
                this.lastX = this.tranlateX;
                drawBitmap();
                return true;
            case 2:
                this.moveX = motionEvent.getX();
                this.tranlateX = this.lastX + (this.moveX - this.downX);
                drawBitmap();
                return true;
            default:
                return true;
        }
    }

    public void stopUpdate() {
        this.isUpdateView = false;
        invalidate();
    }

    public void update(int i) {
        this.valArr.add(Float.valueOf(i + 0.0f));
        this.isUpdateView = true;
        drawBitmap();
    }
}
