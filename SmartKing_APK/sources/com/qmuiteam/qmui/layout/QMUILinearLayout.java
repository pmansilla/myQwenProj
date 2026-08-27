package com.qmuiteam.qmui.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import com.qmuiteam.qmui.alpha.QMUIAlphaLinearLayout;

/* loaded from: classes2.dex */
public class QMUILinearLayout extends QMUIAlphaLinearLayout implements IQMUILayout {
    private QMUILayoutHelper mLayoutHelper;

    public QMUILinearLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public QMUILinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public QMUILinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        this.mLayoutHelper = new QMUILayoutHelper(context, attributeSet, i, this);
        setChangeAlphaWhenPress(false);
        setChangeAlphaWhenDisable(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.mLayoutHelper.drawDividers(canvas, getWidth(), getHeight());
        this.mLayoutHelper.dispatchRoundBorderDraw(canvas);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public int getHideRadiusSide() {
        return this.mLayoutHelper.getHideRadiusSide();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public int getRadius() {
        return this.mLayoutHelper.getRadius();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public float getShadowAlpha() {
        return this.mLayoutHelper.getShadowAlpha();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public int getShadowElevation() {
        return this.mLayoutHelper.getShadowElevation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int measuredWidthSpec = this.mLayoutHelper.getMeasuredWidthSpec(i);
        int measuredHeightSpec = this.mLayoutHelper.getMeasuredHeightSpec(i2);
        super.onMeasure(measuredWidthSpec, measuredHeightSpec);
        int handleMiniWidth = this.mLayoutHelper.handleMiniWidth(measuredWidthSpec, getMeasuredWidth());
        int handleMiniHeight = this.mLayoutHelper.handleMiniHeight(measuredHeightSpec, getMeasuredHeight());
        if (measuredWidthSpec == handleMiniWidth && measuredHeightSpec == handleMiniHeight) {
            return;
        }
        super.onMeasure(handleMiniWidth, handleMiniHeight);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void onlyShowBottomDivider(int i, int i2, int i3, int i4) {
        this.mLayoutHelper.onlyShowBottomDivider(i, i2, i3, i4);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void onlyShowLeftDivider(int i, int i2, int i3, int i4) {
        this.mLayoutHelper.onlyShowLeftDivider(i, i2, i3, i4);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void onlyShowRightDivider(int i, int i2, int i3, int i4) {
        this.mLayoutHelper.onlyShowRightDivider(i, i2, i3, i4);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void onlyShowTopDivider(int i, int i2, int i3, int i4) {
        this.mLayoutHelper.onlyShowTopDivider(i, i2, i3, i4);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setBorderColor(@ColorInt int i) {
        this.mLayoutHelper.setBorderColor(i);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setBorderWidth(int i) {
        this.mLayoutHelper.setBorderWidth(i);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setBottomDividerAlpha(int i) {
        this.mLayoutHelper.setBottomDividerAlpha(i);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public boolean setHeightLimit(int i) {
        if (!this.mLayoutHelper.setHeightLimit(i)) {
            return true;
        }
        requestLayout();
        invalidate();
        return true;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setHideRadiusSide(int i) {
        this.mLayoutHelper.setHideRadiusSide(i);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setLeftDividerAlpha(int i) {
        this.mLayoutHelper.setLeftDividerAlpha(i);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setOutlineExcludePadding(boolean z) {
        this.mLayoutHelper.setOutlineExcludePadding(z);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setOutlineInset(int i, int i2, int i3, int i4) {
        this.mLayoutHelper.setOutlineInset(i, i2, i3, i4);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRadius(int i) {
        this.mLayoutHelper.setRadius(i);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRadius(int i, int i2) {
        this.mLayoutHelper.setRadius(i, i2);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRadiusAndShadow(int i, int i2, float f) {
        this.mLayoutHelper.setRadiusAndShadow(i, i2, f);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRadiusAndShadow(int i, int i2, int i3, float f) {
        this.mLayoutHelper.setRadiusAndShadow(i, i2, i3, f);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRightDividerAlpha(int i) {
        this.mLayoutHelper.setRightDividerAlpha(i);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setShadowAlpha(float f) {
        this.mLayoutHelper.setShadowAlpha(f);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setShadowElevation(int i) {
        this.mLayoutHelper.setShadowElevation(i);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setShowBorderOnlyBeforeL(boolean z) {
        this.mLayoutHelper.setShowBorderOnlyBeforeL(z);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setTopDividerAlpha(int i) {
        this.mLayoutHelper.setTopDividerAlpha(i);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setUseThemeGeneralShadowElevation() {
        this.mLayoutHelper.setUseThemeGeneralShadowElevation();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public boolean setWidthLimit(int i) {
        if (!this.mLayoutHelper.setWidthLimit(i)) {
            return true;
        }
        requestLayout();
        invalidate();
        return true;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void updateBottomDivider(int i, int i2, int i3, int i4) {
        this.mLayoutHelper.updateBottomDivider(i, i2, i3, i4);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void updateLeftDivider(int i, int i2, int i3, int i4) {
        this.mLayoutHelper.updateLeftDivider(i, i2, i3, i4);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void updateRightDivider(int i, int i2, int i3, int i4) {
        this.mLayoutHelper.updateRightDivider(i, i2, i3, i4);
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void updateTopDivider(int i, int i2, int i3, int i4) {
        this.mLayoutHelper.updateTopDivider(i, i2, i3, i4);
        invalidate();
    }
}
