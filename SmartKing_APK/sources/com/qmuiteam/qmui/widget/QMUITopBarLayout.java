package com.qmuiteam.qmui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;

/* loaded from: classes2.dex */
public class QMUITopBarLayout extends FrameLayout {
    private QMUITopBar mTopBar;
    private int mTopBarBgColor;
    private Drawable mTopBarBgWithSeparatorDrawableCache;
    private int mTopBarSeparatorColor;
    private int mTopBarSeparatorHeight;

    public QMUITopBarLayout(Context context) {
        this(context, null);
    }

    public QMUITopBarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.QMUITopBarStyle);
    }

    public QMUITopBarLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.QMUITopBar, R.attr.QMUITopBarStyle, 0);
        this.mTopBarSeparatorColor = obtainStyledAttributes.getColor(R.styleable.QMUITopBar_qmui_topbar_separator_color, ContextCompat.getColor(context, R.color.qmui_config_color_separator));
        this.mTopBarSeparatorHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUITopBar_qmui_topbar_separator_height, 1);
        this.mTopBarBgColor = obtainStyledAttributes.getColor(R.styleable.QMUITopBar_qmui_topbar_bg_color, -1);
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.QMUITopBar_qmui_topbar_need_separator, true);
        this.mTopBar = new QMUITopBar(context, true);
        this.mTopBar.getCommonFieldFormTypedArray(context, obtainStyledAttributes);
        addView(this.mTopBar, new FrameLayout.LayoutParams(-1, QMUIResHelper.getAttrDimen(context, R.attr.qmui_topbar_height)));
        obtainStyledAttributes.recycle();
        setBackgroundDividerEnabled(z);
    }

    public QMUIAlphaImageButton addLeftBackImageButton() {
        return this.mTopBar.addLeftBackImageButton();
    }

    public QMUIAlphaImageButton addLeftImageButton(int i, int i2) {
        return this.mTopBar.addLeftImageButton(i, i2);
    }

    public Button addLeftTextButton(int i, int i2) {
        return this.mTopBar.addLeftTextButton(i, i2);
    }

    public Button addLeftTextButton(String str, int i) {
        return this.mTopBar.addLeftTextButton(str, i);
    }

    public void addLeftView(View view, int i) {
        this.mTopBar.addLeftView(view, i);
    }

    public void addLeftView(View view, int i, RelativeLayout.LayoutParams layoutParams) {
        this.mTopBar.addLeftView(view, i, layoutParams);
    }

    public QMUIAlphaImageButton addRightImageButton(int i, int i2) {
        return this.mTopBar.addRightImageButton(i, i2);
    }

    public Button addRightTextButton(int i, int i2) {
        return this.mTopBar.addRightTextButton(i, i2);
    }

    public Button addRightTextButton(String str, int i) {
        return this.mTopBar.addRightTextButton(str, i);
    }

    public void addRightView(View view, int i) {
        this.mTopBar.addRightView(view, i);
    }

    public void addRightView(View view, int i, RelativeLayout.LayoutParams layoutParams) {
        this.mTopBar.addRightView(view, i, layoutParams);
    }

    public int computeAndSetBackgroundAlpha(int i, int i2, int i3) {
        int max = (int) (Math.max(0.0d, Math.min((i - i2) / (i3 - i2), 1.0d)) * 255.0d);
        setBackgroundAlpha(max);
        return max;
    }

    public void removeAllLeftViews() {
        this.mTopBar.removeAllLeftViews();
    }

    public void removeAllRightViews() {
        this.mTopBar.removeAllRightViews();
    }

    public void removeCenterViewAndTitleView() {
        this.mTopBar.removeCenterViewAndTitleView();
    }

    public void setBackgroundAlpha(int i) {
        getBackground().setAlpha(i);
    }

    public void setBackgroundDividerEnabled(boolean z) {
        if (!z) {
            QMUIViewHelper.setBackgroundColorKeepPadding(this, this.mTopBarBgColor);
            return;
        }
        if (this.mTopBarBgWithSeparatorDrawableCache == null) {
            this.mTopBarBgWithSeparatorDrawableCache = QMUIDrawableHelper.createItemSeparatorBg(this.mTopBarSeparatorColor, this.mTopBarBgColor, this.mTopBarSeparatorHeight, false);
        }
        QMUIViewHelper.setBackgroundKeepingPadding(this, this.mTopBarBgWithSeparatorDrawableCache);
    }

    public void setCenterView(View view) {
        this.mTopBar.setCenterView(view);
    }

    public TextView setEmojiTitle(String str) {
        return this.mTopBar.setEmojiTitle(str);
    }

    public void setSubTitle(int i) {
        this.mTopBar.setSubTitle(i);
    }

    public void setSubTitle(String str) {
        this.mTopBar.setSubTitle(str);
    }

    public TextView setTitle(int i) {
        return this.mTopBar.setTitle(i);
    }

    public TextView setTitle(String str) {
        return this.mTopBar.setTitle(str);
    }

    public void setTitleGravity(int i) {
        this.mTopBar.setTitleGravity(i);
    }

    public void showTitlteView(boolean z) {
        this.mTopBar.showTitleView(z);
    }
}
