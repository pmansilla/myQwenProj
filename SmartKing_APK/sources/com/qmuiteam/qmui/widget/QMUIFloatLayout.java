package com.qmuiteam.qmui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.qmuiteam.qmui.R;

/* loaded from: classes2.dex */
public class QMUIFloatLayout extends ViewGroup {
    private static final int LINES = 0;
    private static final int NUMBER = 1;
    private int mChildHorizontalSpacing;
    private int mChildVerticalSpacing;
    private int mGravity;
    private int[] mItemNumberInEachLine;
    private int mLineCount;
    private int mMaxMode;
    private int mMaximum;
    private OnLineCountChangeListener mOnLineCountChangeListener;
    private int[] mWidthSumInEachLine;
    private int measuredChildCount;

    /* loaded from: classes2.dex */
    public interface OnLineCountChangeListener {
        void onChange(int i, int i2);
    }

    public QMUIFloatLayout(Context context) {
        this(context, null);
    }

    public QMUIFloatLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QMUIFloatLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMaxMode = 0;
        this.mMaximum = Integer.MAX_VALUE;
        this.mLineCount = 0;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUIFloatLayout);
        this.mChildHorizontalSpacing = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIFloatLayout_qmui_childHorizontalSpacing, 0);
        this.mChildVerticalSpacing = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIFloatLayout_qmui_childVerticalSpacing, 0);
        this.mGravity = obtainStyledAttributes.getInteger(R.styleable.QMUIFloatLayout_android_gravity, 3);
        int i = obtainStyledAttributes.getInt(R.styleable.QMUIFloatLayout_android_maxLines, -1);
        if (i >= 0) {
            setMaxLines(i);
        }
        int i2 = obtainStyledAttributes.getInt(R.styleable.QMUIFloatLayout_qmui_maxNumber, -1);
        if (i2 >= 0) {
            setMaxNumber(i2);
        }
        obtainStyledAttributes.recycle();
    }

    private void layoutWithGravityCenterHorizontal(int i) {
        int paddingTop = getPaddingTop();
        int i2 = 0;
        for (int i3 = 0; i3 < this.mItemNumberInEachLine.length && this.mItemNumberInEachLine[i3] != 0 && i2 <= this.measuredChildCount - 1; i3++) {
            int paddingLeft = ((((i - getPaddingLeft()) - getPaddingRight()) - this.mWidthSumInEachLine[i3]) / 2) + getPaddingLeft();
            int i4 = 0;
            for (int i5 = i2; i5 < this.mItemNumberInEachLine[i3] + i2; i5++) {
                View childAt = getChildAt(i5);
                if (childAt.getVisibility() != 8) {
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    childAt.layout(paddingLeft, paddingTop, paddingLeft + measuredWidth, paddingTop + measuredHeight);
                    i4 = Math.max(i4, measuredHeight);
                    paddingLeft += measuredWidth + this.mChildHorizontalSpacing;
                }
            }
            paddingTop += i4 + this.mChildVerticalSpacing;
            i2 += this.mItemNumberInEachLine[i3];
        }
        int childCount = getChildCount();
        if (this.measuredChildCount < childCount) {
            for (int i6 = this.measuredChildCount; i6 < childCount; i6++) {
                View childAt2 = getChildAt(i6);
                if (childAt2.getVisibility() != 8) {
                    childAt2.layout(0, 0, 0, 0);
                }
            }
        }
    }

    private void layoutWithGravityLeft(int i) {
        int paddingRight = i - getPaddingRight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        int min = Math.min(childCount, this.measuredChildCount);
        int i2 = paddingTop;
        int i3 = 0;
        int i4 = paddingLeft;
        for (int i5 = 0; i5 < min; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (i4 + measuredWidth > paddingRight) {
                    i4 = getPaddingLeft();
                    i2 += i3 + this.mChildVerticalSpacing;
                    i3 = 0;
                }
                childAt.layout(i4, i2, i4 + measuredWidth, i2 + measuredHeight);
                i4 += measuredWidth + this.mChildHorizontalSpacing;
                i3 = Math.max(i3, measuredHeight);
            }
        }
        if (this.measuredChildCount < childCount) {
            for (int i6 = this.measuredChildCount; i6 < childCount; i6++) {
                View childAt2 = getChildAt(i6);
                if (childAt2.getVisibility() != 8) {
                    childAt2.layout(0, 0, 0, 0);
                }
            }
        }
    }

    private void layoutWithGravityRight(int i) {
        int paddingTop = getPaddingTop();
        int i2 = 0;
        for (int i3 = 0; i3 < this.mItemNumberInEachLine.length && this.mItemNumberInEachLine[i3] != 0 && i2 <= this.measuredChildCount - 1; i3++) {
            int paddingRight = (i - getPaddingRight()) - this.mWidthSumInEachLine[i3];
            int i4 = 0;
            for (int i5 = i2; i5 < this.mItemNumberInEachLine[i3] + i2; i5++) {
                View childAt = getChildAt(i5);
                if (childAt.getVisibility() != 8) {
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    childAt.layout(paddingRight, paddingTop, paddingRight + measuredWidth, paddingTop + measuredHeight);
                    i4 = Math.max(i4, measuredHeight);
                    paddingRight += measuredWidth + this.mChildHorizontalSpacing;
                }
            }
            paddingTop += i4 + this.mChildVerticalSpacing;
            i2 += this.mItemNumberInEachLine[i3];
        }
        int childCount = getChildCount();
        if (this.measuredChildCount < childCount) {
            for (int i6 = this.measuredChildCount; i6 < childCount; i6++) {
                View childAt2 = getChildAt(i6);
                if (childAt2.getVisibility() != 8) {
                    childAt2.layout(0, 0, 0, 0);
                }
            }
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public int getLineCount() {
        return this.mLineCount;
    }

    public int getMaxLines() {
        if (this.mMaxMode == 0) {
            return this.mMaximum;
        }
        return -1;
    }

    public int getMaxNumber() {
        if (this.mMaxMode == 1) {
            return this.mMaximum;
        }
        return -1;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int i6 = this.mGravity & 7;
        if (i6 == 1) {
            layoutWithGravityCenterHorizontal(i5);
            return;
        }
        if (i6 == 3) {
            layoutWithGravityLeft(i5);
        } else if (i6 != 5) {
            layoutWithGravityLeft(i5);
        } else {
            layoutWithGravityRight(i5);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0144  */
    @Override // android.view.View
    @android.annotation.SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onMeasure(int r20, int r21) {
        /*
            Method dump skipped, instructions count: 449
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qmuiteam.qmui.widget.QMUIFloatLayout.onMeasure(int, int):void");
    }

    public void setChildHorizontalSpacing(int i) {
        this.mChildHorizontalSpacing = i;
        invalidate();
    }

    public void setChildVerticalSpacing(int i) {
        this.mChildVerticalSpacing = i;
        invalidate();
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            this.mGravity = i;
            requestLayout();
        }
    }

    public void setMaxLines(int i) {
        this.mMaximum = i;
        this.mMaxMode = 0;
        requestLayout();
    }

    public void setMaxNumber(int i) {
        this.mMaximum = i;
        this.mMaxMode = 1;
        requestLayout();
    }

    public void setOnLineCountChangeListener(OnLineCountChangeListener onLineCountChangeListener) {
        this.mOnLineCountChangeListener = onLineCountChangeListener;
    }
}
