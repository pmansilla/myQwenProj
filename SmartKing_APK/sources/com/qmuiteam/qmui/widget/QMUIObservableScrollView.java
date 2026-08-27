package com.qmuiteam.qmui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class QMUIObservableScrollView extends ScrollView {
    private List<OnScrollChangedListener> mOnScrollChangedListeners;

    /* loaded from: classes2.dex */
    public interface OnScrollChangedListener {
        void onScrollChanged(QMUIObservableScrollView qMUIObservableScrollView, int i, int i2, int i3, int i4);
    }

    public QMUIObservableScrollView(Context context) {
        super(context);
    }

    public QMUIObservableScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public QMUIObservableScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void addOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        if (this.mOnScrollChangedListeners == null) {
            this.mOnScrollChangedListeners = new ArrayList();
        }
        if (this.mOnScrollChangedListeners.contains(onScrollChangedListener)) {
            return;
        }
        this.mOnScrollChangedListeners.add(onScrollChangedListener);
    }

    @Override // android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        if (this.mOnScrollChangedListeners == null || this.mOnScrollChangedListeners.isEmpty()) {
            return;
        }
        Iterator<OnScrollChangedListener> it = this.mOnScrollChangedListeners.iterator();
        while (it.hasNext()) {
            it.next().onScrollChanged(this, i, i2, i3, i4);
        }
    }

    public void removeOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        if (this.mOnScrollChangedListeners == null) {
            return;
        }
        this.mOnScrollChangedListeners.remove(onScrollChangedListener);
    }
}
