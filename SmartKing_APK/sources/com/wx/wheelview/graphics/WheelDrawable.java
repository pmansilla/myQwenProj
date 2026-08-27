package com.wx.wheelview.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import com.wx.wheelview.widget.WheelView;

/* loaded from: classes2.dex */
public class WheelDrawable extends Drawable {
    private Paint mBgPaint;
    protected int mHeight;
    protected WheelView.WheelViewStyle mStyle;
    protected int mWidth;

    public WheelDrawable(int i, int i2, WheelView.WheelViewStyle wheelViewStyle) {
        this.mWidth = i;
        this.mHeight = i2;
        this.mStyle = wheelViewStyle;
        init();
    }

    private void init() {
        this.mBgPaint = new Paint();
        this.mBgPaint.setColor(this.mStyle.backgroundColor != -1 ? this.mStyle.backgroundColor : -1);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, this.mWidth, this.mHeight, this.mBgPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }
}
