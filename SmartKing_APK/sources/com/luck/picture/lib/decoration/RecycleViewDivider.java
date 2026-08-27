package com.luck.picture.lib.decoration;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/* loaded from: classes.dex */
public class RecycleViewDivider extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = {R.attr.listDivider};
    private Drawable mDivider;
    private int mDividerHeight;
    private int mOrientation;
    private Paint mPaint;

    public RecycleViewDivider(Context context, int i) {
        this.mDividerHeight = 2;
        if (i != 1 && i != 0) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        this.mOrientation = i;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(ATTRS);
        this.mDivider = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
    }

    public RecycleViewDivider(Context context, int i, int i2) {
        this(context, i);
        this.mDivider = ContextCompat.getDrawable(context, i2);
        this.mDividerHeight = this.mDivider.getIntrinsicHeight();
    }

    public RecycleViewDivider(Context context, int i, int i2, int i3) {
        this(context, i);
        this.mDividerHeight = i2;
        this.mPaint = new Paint(1);
        this.mPaint.setColor(i3);
        this.mPaint.setStyle(Paint.Style.FILL);
    }

    private void drawHorizontal(Canvas canvas, RecyclerView recyclerView) {
        int paddingLeft = recyclerView.getPaddingLeft();
        int measuredWidth = recyclerView.getMeasuredWidth() - recyclerView.getPaddingRight();
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            int bottom = childAt.getBottom() + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).bottomMargin;
            int i2 = this.mDividerHeight + bottom;
            if (this.mDivider != null) {
                this.mDivider.setBounds(paddingLeft, bottom, measuredWidth, i2);
                this.mDivider.draw(canvas);
            }
            if (this.mPaint != null) {
                canvas.drawRect(paddingLeft, bottom, measuredWidth, i2, this.mPaint);
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView recyclerView) {
        int paddingTop = recyclerView.getPaddingTop();
        int measuredHeight = recyclerView.getMeasuredHeight() - recyclerView.getPaddingBottom();
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            int right = childAt.getRight() + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).rightMargin;
            int i2 = this.mDividerHeight + right;
            if (this.mDivider != null) {
                this.mDivider.setBounds(right, paddingTop, i2, measuredHeight);
                this.mDivider.draw(canvas);
            }
            if (this.mPaint != null) {
                canvas.drawRect(right, paddingTop, i2, measuredHeight, this.mPaint);
            }
        }
    }

    @Override // android.support.v7.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        rect.set(0, 0, 0, this.mDividerHeight);
    }

    @Override // android.support.v7.widget.RecyclerView.ItemDecoration
    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDraw(canvas, recyclerView, state);
        if (this.mOrientation == 1) {
            drawVertical(canvas, recyclerView);
        } else {
            drawHorizontal(canvas, recyclerView);
        }
    }
}
