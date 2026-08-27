package com.czw.modes.widget.smartking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.czw.R;

/* loaded from: classes.dex */
public class BatteryView extends View {
    RectF batteryBgRect;
    RectF batteryHeaderRect;
    private int batteryPrecent;
    private int borderColor;
    private float borderWidth;
    private Direction direction;
    private float headerHeight;
    private float height;
    private boolean interval;
    private float margin;
    private Paint paint;
    private float raduis;
    private float width;

    /* loaded from: classes.dex */
    public enum Direction {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public BatteryView(Context context) {
        super(context);
        this.paint = null;
        this.headerHeight = 0.0f;
        this.raduis = 10.0f;
        this.borderWidth = 0.0f;
        this.margin = 10.0f;
        this.borderColor = -1;
        this.batteryBgRect = null;
        this.batteryHeaderRect = null;
        this.height = 0.0f;
        this.width = 0.0f;
        this.direction = Direction.TOP;
        this.interval = false;
        this.batteryPrecent = 0;
        init(context);
    }

    public BatteryView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = null;
        this.headerHeight = 0.0f;
        this.raduis = 10.0f;
        this.borderWidth = 0.0f;
        this.margin = 10.0f;
        this.borderColor = -1;
        this.batteryBgRect = null;
        this.batteryHeaderRect = null;
        this.height = 0.0f;
        this.width = 0.0f;
        this.direction = Direction.TOP;
        this.interval = false;
        this.batteryPrecent = 0;
        init(context);
    }

    public BatteryView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.paint = null;
        this.headerHeight = 0.0f;
        this.raduis = 10.0f;
        this.borderWidth = 0.0f;
        this.margin = 10.0f;
        this.borderColor = -1;
        this.batteryBgRect = null;
        this.batteryHeaderRect = null;
        this.height = 0.0f;
        this.width = 0.0f;
        this.direction = Direction.TOP;
        this.interval = false;
        this.batteryPrecent = 0;
        init(context);
    }

    private void drawBatteryBorderBg(Canvas canvas) {
        this.width = getWidth();
        float f = this.width * 0.5f;
        switch (this.direction) {
            case TOP:
                this.batteryBgRect = new RectF(this.borderWidth, this.headerHeight, getWidth() - this.borderWidth, getHeight() - this.borderWidth);
                float f2 = f / 2.0f;
                this.batteryHeaderRect = new RectF(this.batteryBgRect.centerX() - f2, 0.0f, this.batteryBgRect.centerX() + f2, this.batteryBgRect.top);
                break;
            case BOTTOM:
                this.batteryBgRect = new RectF(0.0f, 0.0f, getWidth(), getHeight() - this.headerHeight);
                float f3 = f / 2.0f;
                this.batteryHeaderRect = new RectF(this.batteryBgRect.centerX() - f3, this.batteryBgRect.bottom, this.batteryBgRect.centerX() + f3, this.batteryBgRect.bottom + this.headerHeight);
                break;
            case LEFT:
                this.batteryBgRect = new RectF(this.headerHeight, 0.0f, getWidth(), getHeight());
                this.batteryHeaderRect = new RectF(this.batteryBgRect.top, this.batteryBgRect.left, this.batteryBgRect.right, this.batteryBgRect.left + this.headerHeight);
                break;
            case RIGHT:
                this.batteryBgRect = new RectF(0.0f, this.headerHeight, getWidth() - this.headerHeight, getHeight());
                break;
        }
        this.paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(this.batteryBgRect, this.raduis, this.raduis, this.paint);
        this.paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(this.batteryHeaderRect, this.raduis, this.raduis, this.paint);
    }

    private void drawBatteryValue(Canvas canvas) {
        if (this.interval) {
            return;
        }
        RectF rectF = null;
        switch (this.direction) {
            case TOP:
                rectF = new RectF(this.batteryBgRect.left, 0.0f, this.batteryBgRect.right, this.batteryBgRect.bottom);
                rectF.top = this.batteryBgRect.top + ((1.0f - (this.batteryPrecent / 100.0f)) * this.batteryBgRect.height());
                break;
            case BOTTOM:
                rectF = new RectF(this.batteryBgRect.left, this.batteryBgRect.top, this.batteryBgRect.right, this.batteryBgRect.bottom);
                rectF.bottom = (1.0f - (this.batteryPrecent / 100.0f)) * this.batteryBgRect.bottom;
                break;
        }
        float f = this.raduis / 2.0f;
        rectF.bottom -= f;
        rectF.left += f;
        rectF.right -= f;
        canvas.drawRect(rectF, this.paint);
    }

    private void init(Context context) {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(this.borderColor);
        this.headerHeight = getResources().getDimension(R.dimen.DIMEN_4PX);
        this.raduis = getResources().getDimension(R.dimen.DIMEN_2PX);
        this.borderWidth = getResources().getDimension(R.dimen.DIMEN_2PX);
        this.paint.setStrokeWidth(this.borderWidth);
    }

    public void initCfg() {
        boolean z = this.interval;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBatteryBorderBg(canvas);
        drawBatteryValue(canvas);
    }

    public void updateShow(int i) {
        this.batteryPrecent = i;
        invalidate();
    }
}
