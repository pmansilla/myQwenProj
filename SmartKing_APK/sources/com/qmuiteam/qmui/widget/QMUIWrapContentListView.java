package com.qmuiteam.qmui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/* loaded from: classes2.dex */
public class QMUIWrapContentListView extends ListView {
    private int mMaxHeight;

    public QMUIWrapContentListView(Context context) {
        super(context);
        this.mMaxHeight = 536870911;
    }

    public QMUIWrapContentListView(Context context, int i) {
        super(context);
        this.mMaxHeight = 536870911;
        this.mMaxHeight = i;
    }

    public QMUIWrapContentListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMaxHeight = 536870911;
    }

    public QMUIWrapContentListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMaxHeight = 536870911;
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.View
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
