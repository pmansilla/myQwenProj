package com.wx.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/* loaded from: classes2.dex */
public class NestedScrollView extends ScrollView {
    public NestedScrollView(Context context) {
        super(context);
        init();
    }

    public NestedScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public NestedScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setOnTouchListener(new View.OnTouchListener() { // from class: com.wx.wheelview.widget.NestedScrollView.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                WheelView wheelView = (WheelView) NestedScrollView.this.findViewWithTag("com.wx.wheelview");
                if (wheelView != null) {
                    wheelView.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
    }
}
