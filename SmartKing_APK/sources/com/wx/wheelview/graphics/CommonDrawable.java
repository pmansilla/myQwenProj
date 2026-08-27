package com.wx.wheelview.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import com.wx.wheelview.common.WheelConstants;
import com.wx.wheelview.widget.WheelView;

/* loaded from: classes2.dex */
public class CommonDrawable extends WheelDrawable {
    private static final int[] SHADOWS_COLORS = {-15658735, 11184810, 11184810};
    private GradientDrawable mBottomShadow;
    private Paint mCommonBgPaint;
    private Paint mCommonBorderPaint;
    private Paint mCommonDividerPaint;
    private Paint mCommonPaint;
    private int mItemH;
    private GradientDrawable mTopShadow;
    private int mWheelSize;

    public CommonDrawable(int i, int i2, WheelView.WheelViewStyle wheelViewStyle, int i3, int i4) {
        super(i, i2, wheelViewStyle);
        this.mTopShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        this.mBottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, SHADOWS_COLORS);
        this.mWheelSize = i3;
        this.mItemH = i4;
        init();
    }

    private void init() {
        this.mCommonBgPaint = new Paint();
        this.mCommonBgPaint.setColor(this.mStyle.backgroundColor != -1 ? this.mStyle.backgroundColor : WheelConstants.WHEEL_SKIN_COMMON_BG);
        this.mCommonPaint = new Paint();
        this.mCommonPaint.setColor(WheelConstants.WHEEL_SKIN_COMMON_COLOR);
        this.mCommonDividerPaint = new Paint();
        this.mCommonDividerPaint.setColor(WheelConstants.WHEEL_SKIN_COMMON_DIVIDER_COLOR);
        this.mCommonDividerPaint.setStrokeWidth(2.0f);
        this.mCommonBorderPaint = new Paint();
        this.mCommonBorderPaint.setStrokeWidth(6.0f);
        this.mCommonBorderPaint.setColor(WheelConstants.WHEEL_SKIN_COMMON_BORDER_COLOR);
    }

    @Override // com.wx.wheelview.graphics.WheelDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, this.mWidth, this.mHeight, this.mCommonBgPaint);
        if (this.mItemH != 0) {
            canvas.drawRect(0.0f, this.mItemH * (this.mWheelSize / 2), this.mWidth, this.mItemH * ((this.mWheelSize / 2) + 1), this.mCommonPaint);
            canvas.drawLine(0.0f, this.mItemH * (this.mWheelSize / 2), this.mWidth, this.mItemH * (this.mWheelSize / 2), this.mCommonDividerPaint);
            canvas.drawLine(0.0f, this.mItemH * ((this.mWheelSize / 2) + 1), this.mWidth, this.mItemH * ((this.mWheelSize / 2) + 1), this.mCommonDividerPaint);
            this.mTopShadow.setBounds(0, 0, this.mWidth, this.mItemH);
            this.mTopShadow.draw(canvas);
            this.mBottomShadow.setBounds(0, this.mHeight - this.mItemH, this.mWidth, this.mHeight);
            this.mBottomShadow.draw(canvas);
            canvas.drawLine(0.0f, 0.0f, 0.0f, this.mHeight, this.mCommonBorderPaint);
            canvas.drawLine(this.mWidth, 0.0f, this.mWidth, this.mHeight, this.mCommonBorderPaint);
        }
    }
}
