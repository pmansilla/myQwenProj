package com.wx.wheelview.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.wx.wheelview.common.WheelConstants;
import com.wx.wheelview.widget.WheelView;

/* loaded from: classes2.dex */
public class HoloDrawable extends WheelDrawable {
    private Paint mHoloBgPaint;
    private Paint mHoloPaint;
    private int mItemH;
    private int mWheelSize;

    public HoloDrawable(int i, int i2, WheelView.WheelViewStyle wheelViewStyle, int i3, int i4) {
        super(i, i2, wheelViewStyle);
        this.mWheelSize = i3;
        this.mItemH = i4;
        init();
    }

    private void init() {
        this.mHoloBgPaint = new Paint();
        this.mHoloBgPaint.setColor(this.mStyle.backgroundColor != -1 ? this.mStyle.backgroundColor : -1);
        this.mHoloPaint = new Paint();
        this.mHoloPaint.setStrokeWidth(this.mStyle.holoBorderWidth != -1 ? this.mStyle.holoBorderWidth : 3.0f);
        this.mHoloPaint.setColor(this.mStyle.holoBorderColor != -1 ? this.mStyle.holoBorderColor : WheelConstants.WHEEL_SKIN_HOLO_BORDER_COLOR);
    }

    @Override // com.wx.wheelview.graphics.WheelDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, this.mWidth, this.mHeight, this.mHoloBgPaint);
        if (this.mItemH != 0) {
            canvas.drawLine(0.0f, this.mItemH * (this.mWheelSize / 2), this.mWidth, this.mItemH * (this.mWheelSize / 2), this.mHoloPaint);
            canvas.drawLine(0.0f, this.mItemH * ((this.mWheelSize / 2) + 1), this.mWidth, this.mItemH * ((this.mWheelSize / 2) + 1), this.mHoloPaint);
        }
    }
}
