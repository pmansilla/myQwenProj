package com.qmuiteam.qmui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.qmuiteam.qmui.util.QMUIWindowInsetHelper;

/* loaded from: classes2.dex */
public class QMUIWindowInsetLayout extends FrameLayout implements IWindowInsetLayout {
    private QMUIWindowInsetHelper mQMUIWindowInsetHelper;

    public QMUIWindowInsetLayout(Context context) {
        this(context, null);
    }

    public QMUIWindowInsetLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QMUIWindowInsetLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mQMUIWindowInsetHelper = new QMUIWindowInsetHelper(this, this);
    }

    @Override // com.qmuiteam.qmui.widget.IWindowInsetLayout
    public boolean applySystemWindowInsets19(Rect rect) {
        return this.mQMUIWindowInsetHelper.defaultApplySystemWindowInsets19(this, rect);
    }

    @Override // com.qmuiteam.qmui.widget.IWindowInsetLayout
    public boolean applySystemWindowInsets21(WindowInsetsCompat windowInsetsCompat) {
        return this.mQMUIWindowInsetHelper.defaultApplySystemWindowInsets21(this, windowInsetsCompat);
    }

    @Override // android.view.View
    protected boolean fitSystemWindows(Rect rect) {
        return (Build.VERSION.SDK_INT < 19 || Build.VERSION.SDK_INT >= 21) ? super.fitSystemWindows(rect) : applySystemWindowInsets19(rect);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (ViewCompat.getFitsSystemWindows(this)) {
            ViewCompat.requestApplyInsets(this);
        }
    }
}
