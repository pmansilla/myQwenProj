package com.czw.smartkit.views.multiColumnView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.czw.smartkit.R;
import com.czw.utils.LogUtil;
import com.czw.utils.MathUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class MultiColumnView extends View {
    private List<RectF> bigRectList;
    private Bitmap bitmap;
    private Canvas bmpCanvas;
    private float bottomMargin;
    private int cursorLineDateColor;
    private float downX;
    private float lastX;
    private float leftMargin;
    private float maxValue;
    private float moveX;
    private List<MultiColumnBean> multiColumnBeanList;
    private Paint paint;
    private float rectGroupSpaceWidth;
    private float rectWidth;
    private float rightMargin;
    private float textSize;
    private float topMargin;
    private float totalHeight;
    private float totalWidth;
    private float tranlateX;

    public MultiColumnView(Context context) {
        super(context);
        this.cursorLineDateColor = -3355444;
        this.bitmap = null;
        this.bmpCanvas = null;
        this.paint = null;
        this.leftMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.textSize = 10.0f;
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        this.multiColumnBeanList = new ArrayList();
        this.bigRectList = new ArrayList();
        this.maxValue = 0.0f;
        init(context);
    }

    public MultiColumnView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.cursorLineDateColor = -3355444;
        this.bitmap = null;
        this.bmpCanvas = null;
        this.paint = null;
        this.leftMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.textSize = 10.0f;
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        this.multiColumnBeanList = new ArrayList();
        this.bigRectList = new ArrayList();
        this.maxValue = 0.0f;
        init(context);
    }

    public MultiColumnView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.cursorLineDateColor = -3355444;
        this.bitmap = null;
        this.bmpCanvas = null;
        this.paint = null;
        this.leftMargin = 100.0f;
        this.rightMargin = 100.0f;
        this.topMargin = 100.0f;
        this.bottomMargin = 100.0f;
        this.textSize = 10.0f;
        this.tranlateX = 0.0f;
        this.lastX = 0.0f;
        this.multiColumnBeanList = new ArrayList();
        this.bigRectList = new ArrayList();
        this.maxValue = 0.0f;
        init(context);
    }

    private void clearBitmap() {
        if (this.bmpCanvas == null) {
            return;
        }
        this.bmpCanvas.drawColor(-1, PorterDuff.Mode.CLEAR);
    }

    private void drawData() {
        this.bigRectList.clear();
        if (this.multiColumnBeanList == null || this.multiColumnBeanList.size() <= 0) {
            LogUtil.e("没数据====");
            return;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<MultiColumnBean> it = this.multiColumnBeanList.iterator();
        while (true) {
            float f = 0.0f;
            if (!it.hasNext()) {
                break;
            }
            Iterator<Float> it2 = it.next().getValueList().iterator();
            while (it2.hasNext()) {
                f += it2.next().floatValue();
            }
            arrayList.add(Float.valueOf(f));
        }
        this.maxValue = MathUtils.getMax(arrayList);
        LogUtil.e("最大组合值:" + this.maxValue);
        this.paint.setColor(-16777216);
        int size = this.multiColumnBeanList.size();
        float f2 = this.leftMargin;
        for (int i = 0; i < size; i++) {
            RectF rectF = new RectF(f2, 0.0f, 0.0f, this.totalHeight - this.bottomMargin);
            MultiColumnBean multiColumnBean = this.multiColumnBeanList.get(i);
            int size2 = multiColumnBean.getValueList().size();
            float f3 = f2;
            for (int i2 = 0; i2 < size2; i2++) {
                float floatValue = multiColumnBean.getValueList().get(i2).floatValue() / this.maxValue;
                RectF rectF2 = new RectF(f3, 0.0f, 0.0f, this.totalHeight - this.bottomMargin);
                rectF2.right = this.rectWidth + f3;
                rectF2.top = rectF2.bottom - (rectF2.height() * floatValue);
                this.paint.setColor(multiColumnBean.getColorList().get(i2).intValue());
                this.bmpCanvas.drawRect(rectF2, this.paint);
                f3 += this.rectWidth;
            }
            rectF.right = f3;
            this.bigRectList.add(rectF);
            f2 = this.rectGroupSpaceWidth + f3;
        }
        this.paint.setColor(this.cursorLineDateColor);
        this.paint.setTextAlign(Paint.Align.CENTER);
        for (int i3 = 0; i3 < size; i3++) {
            String label = this.multiColumnBeanList.get(i3).getLabel();
            RectF rectF3 = this.bigRectList.get(i3);
            this.bmpCanvas.drawText(label, rectF3.centerX(), rectF3.bottom + this.textSize, this.paint);
        }
    }

    private void drawLineAndMask() {
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(-1);
        this.bmpCanvas.drawRect(new RectF(0.0f, 0.0f, this.leftMargin, this.totalHeight), this.paint);
        this.bmpCanvas.drawRect(new RectF(this.totalWidth - this.rightMargin, 0.0f, this.totalWidth, this.totalHeight), this.paint);
    }

    private void drawMethod() {
        LogUtil.e("===bitmap" + this.bitmap);
        if (this.bmpCanvas == null) {
            return;
        }
        clearBitmap();
        this.bmpCanvas.save();
        this.bmpCanvas.translate(this.tranlateX, 0.0f);
        drawData();
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
        this.rectGroupSpaceWidth = getResources().getDimension(R.dimen.DIMEN_30PX);
        this.rectWidth = getResources().getDimension(R.dimen.DIMEN_40PX);
        this.paint.setStrokeWidth(getResources().getDimension(R.dimen.DIMEN_3PX));
        this.paint.setTextSize(this.textSize);
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
        LogUtil.e("w:" + this.totalWidth);
        LogUtil.e("h:" + this.totalHeight);
        if (this.bitmap == null) {
            long currentTimeMillis = System.currentTimeMillis();
            this.bitmap = Bitmap.createBitmap((int) this.totalWidth, (int) this.totalHeight, Bitmap.Config.ARGB_8888);
            this.bmpCanvas = new Canvas(this.bitmap);
            LogUtil.e("time:" + (System.currentTimeMillis() - currentTimeMillis));
            drawMethod();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.downX = motionEvent.getX();
                return true;
            case 1:
                if (this.tranlateX > 0.0f) {
                    this.tranlateX = 0.0f;
                }
                if (this.bigRectList != null && this.bigRectList.size() > 0) {
                    float size = this.leftMargin + (this.bigRectList.size() * (this.bigRectList.get(0).width() + this.rectGroupSpaceWidth));
                    if (size <= this.totalWidth) {
                        this.tranlateX = 0.0f;
                    } else if (this.tranlateX + size <= getWidth()) {
                        this.tranlateX = ((-size) + getWidth()) - this.leftMargin;
                    }
                }
                this.lastX = this.tranlateX;
                drawMethod();
                return true;
            case 2:
                this.moveX = motionEvent.getX();
                this.tranlateX = this.lastX + (this.moveX - this.downX);
                drawMethod();
                return true;
            default:
                return true;
        }
    }

    public void updateData(List<MultiColumnBean> list) {
        this.multiColumnBeanList.clear();
        if (list != null && list.size() > 0) {
            this.multiColumnBeanList.addAll(list);
        }
        drawMethod();
    }
}
