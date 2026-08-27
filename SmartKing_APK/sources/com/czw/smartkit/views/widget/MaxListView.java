package com.czw.smartkit.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/* loaded from: classes.dex */
public class MaxListView extends ListView {
    private int listViewHeight;

    public MaxListView(Context context) {
        super(context);
    }

    public MaxListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MaxListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public int getListViewHeight() {
        return this.listViewHeight;
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.listViewHeight > -1) {
            i2 = View.MeasureSpec.makeMeasureSpec(this.listViewHeight, Integer.MIN_VALUE);
        }
        super.onMeasure(i, i2);
    }

    public void setListViewHeight(int i) {
        this.listViewHeight = i;
    }
}
