package com.qmuiteam.qmui.widget.pullRefreshLayout;

import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

/* loaded from: classes2.dex */
public class QMUIDefaultRefreshOffsetCalculator implements QMUIPullRefreshLayout.RefreshOffsetCalculator {
    @Override // com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout.RefreshOffsetCalculator
    public int calculateRefreshOffset(int i, int i2, int i3, int i4, int i5, int i6) {
        if (i4 >= i6) {
            return i2;
        }
        if (i4 <= i5) {
            return i;
        }
        return (int) (i + ((((i4 - i5) * 1.0f) / (i6 - i5)) * (i2 - i)));
    }
}
