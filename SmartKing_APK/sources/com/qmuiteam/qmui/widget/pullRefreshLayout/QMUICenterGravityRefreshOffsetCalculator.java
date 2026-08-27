package com.qmuiteam.qmui.widget.pullRefreshLayout;

import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;

/* loaded from: classes2.dex */
public class QMUICenterGravityRefreshOffsetCalculator implements QMUIPullRefreshLayout.RefreshOffsetCalculator {
    @Override // com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout.RefreshOffsetCalculator
    public int calculateRefreshOffset(int i, int i2, int i3, int i4, int i5, int i6) {
        return i4 < i3 ? i4 - i3 : (i4 - i3) / 2;
    }
}
