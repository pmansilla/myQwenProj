package com.qmuiteam.qmui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes2.dex */
public class QMUIWrapContentScrollView extends QMUIObservableScrollView {
    private int mMaxHeight;

    public QMUIWrapContentScrollView(Context context) {
        super(context);
        this.mMaxHeight = 536870911;
    }

    public QMUIWrapContentScrollView(Context context, int i) {
        super(context);
        this.mMaxHeight = 536870911;
        this.mMaxHeight = i;
    }

    public QMUIWrapContentScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMaxHeight = 536870911;
    }

    public QMUIWrapContentScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMaxHeight = 536870911;
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(this.mMaxHeight, Integer.MIN_VALUE));
    }

    public void setMaxHeight(int i) {
        if (this.mMaxHeight != i) {
            this.mMaxHeight = i;
            requestLayout();
        }
    }
}
