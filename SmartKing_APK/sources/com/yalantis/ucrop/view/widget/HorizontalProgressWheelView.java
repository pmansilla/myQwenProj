package com.yalantis.ucrop.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.yalantis.ucrop.R;

/* loaded from: classes2.dex */
public class HorizontalProgressWheelView extends View {
    private final Rect mCanvasClipBounds;
    private float mLastTouchedPosition;
    private int mMiddleLineColor;
    private int mProgressLineHeight;
    private int mProgressLineMargin;
    private Paint mProgressLinePaint;
    private int mProgressLineWidth;
    private boolean mScrollStarted;
    private ScrollingListener mScrollingListener;
    private float mTotalScrollDistance;

    /* loaded from: classes2.dex */
    public interface ScrollingListener {
        void onScroll(float f, float f2);

        void onScrollEnd();

        void onScrollStart();
    }

    public HorizontalProgressWheelView(Context context) {
        this(context, null);
    }

    public HorizontalProgressWheelView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public HorizontalProgressWheelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCanvasClipBounds = new Rect();
        init();
    }

    @TargetApi(21)
    public HorizontalProgressWheelView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mCanvasClipBounds = new Rect();
    }

    private void init() {
        this.mMiddleLineColor = ContextCompat.getColor(getContext(), R.color.ucrop_color_progress_wheel_line);
        this.mProgressLineWidth = getContext().getResources().getDimensionPixelSize(R.dimen.ucrop_width_horizontal_wheel_progress_line);
        this.mProgressLineHeight = getContext().getResources().getDimensionPixelSize(R.dimen.ucrop_height_horizontal_wheel_progress_line);
        this.mProgressLineMargin = getContext().getResources().getDimensionPixelSize(R.dimen.ucrop_margin_horizontal_wheel_progress_line);
        this.mProgressLinePaint = new Paint(1);
        this.mProgressLinePaint.setStyle(Paint.Style.STROKE);
        this.mProgressLinePaint.setStrokeWidth(this.mProgressLineWidth);
    }

    private void onScrollEvent(MotionEvent motionEvent, float f) {
        this.mTotalScrollDistance -= f;
        postInvalidate();
        this.mLastTouchedPosition = motionEvent.getX();
        if (this.mScrollingListener != null) {
            this.mScrollingListener.onScroll(-f, this.mTotalScrollDistance);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(this.mCanvasClipBounds);
        int width = this.mCanvasClipBounds.width() / (this.mProgressLineWidth + this.mProgressLineMargin);
        float f = this.mTotalScrollDistance % (this.mProgressLineMargin + this.mProgressLineWidth);
        this.mProgressLinePaint.setColor(getResources().getColor(R.color.ucrop_color_progress_wheel_line));
        for (int i = 0; i < width; i++) {
            int i2 = width / 4;
            if (i < i2) {
                this.mProgressLinePaint.setAlpha((int) ((i / i2) * 255.0f));
            } else if (i > (width * 3) / 4) {
                this.mProgressLinePaint.setAlpha((int) (((width - i) / i2) * 255.0f));
            } else {
                this.mProgressLinePaint.setAlpha(255);
            }
            float f2 = -f;
            canvas.drawLine(this.mCanvasClipBounds.left + f2 + ((this.mProgressLineWidth + this.mProgressLineMargin) * i), this.mCanvasClipBounds.centerY() - (this.mProgressLineHeight / 4.0f), f2 + this.mCanvasClipBounds.left + ((this.mProgressLineWidth + this.mProgressLineMargin) * i), this.mCanvasClipBounds.centerY() + (this.mProgressLineHeight / 4.0f), this.mProgressLinePaint);
        }
        this.mProgressLinePaint.setColor(this.mMiddleLineColor);
        canvas.drawLine(this.mCanvasClipBounds.centerX(), this.mCanvasClipBounds.centerY() - (this.mProgressLineHeight / 2.0f), this.mCanvasClipBounds.centerX(), (this.mProgressLineHeight / 2.0f) + this.mCanvasClipBounds.centerY(), this.mProgressLinePaint);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x003b, code lost:
    
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
                case 0: goto L35;
                case 1: goto L28;
                case 2: goto L9;
                default: goto L8;
            }
        L8:
            goto L3b
        L9:
            float r0 = r4.getX()
            float r2 = r3.mLastTouchedPosition
            float r0 = r0 - r2
            r2 = 0
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 == 0) goto L3b
            boolean r2 = r3.mScrollStarted
            if (r2 != 0) goto L24
            r3.mScrollStarted = r1
            com.yalantis.ucrop.view.widget.HorizontalProgressWheelView$ScrollingListener r2 = r3.mScrollingListener
            if (r2 == 0) goto L24
            com.yalantis.ucrop.view.widget.HorizontalProgressWheelView$ScrollingListener r2 = r3.mScrollingListener
            r2.onScrollStart()
        L24:
            r3.onScrollEvent(r4, r0)
            goto L3b
        L28:
            com.yalantis.ucrop.view.widget.HorizontalProgressWheelView$ScrollingListener r4 = r3.mScrollingListener
            if (r4 == 0) goto L3b
            r4 = 0
            r3.mScrollStarted = r4
            com.yalantis.ucrop.view.widget.HorizontalProgressWheelView$ScrollingListener r4 = r3.mScrollingListener
            r4.onScrollEnd()
            goto L3b
        L35:
            float r4 = r4.getX()
            r3.mLastTouchedPosition = r4
        L3b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yalantis.ucrop.view.widget.HorizontalProgressWheelView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void setMiddleLineColor(@ColorInt int i) {
        this.mMiddleLineColor = i;
        invalidate();
    }

    public void setScrollingListener(ScrollingListener scrollingListener) {
        this.mScrollingListener = scrollingListener;
    }
}
