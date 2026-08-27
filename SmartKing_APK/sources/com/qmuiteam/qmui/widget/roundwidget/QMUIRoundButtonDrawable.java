package com.qmuiteam.qmui.widget.roundwidget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.qmuiteam.qmui.R;

/* loaded from: classes2.dex */
public class QMUIRoundButtonDrawable extends GradientDrawable {
    private ColorStateList mFillColors;
    private ColorStateList mStrokeColors;
    private boolean mRadiusAdjustBounds = true;
    private int mStrokeWidth = 0;

    public static QMUIRoundButtonDrawable fromAttributeSet(Context context, AttributeSet attributeSet, int i) {
        boolean z = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUIRoundButton, i, 0);
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.QMUIRoundButton_qmui_backgroundColor);
        ColorStateList colorStateList2 = obtainStyledAttributes.getColorStateList(R.styleable.QMUIRoundButton_qmui_borderColor);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_borderWidth, 0);
        boolean z2 = obtainStyledAttributes.getBoolean(R.styleable.QMUIRoundButton_qmui_isRadiusAdjustBounds, false);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_radius, 0);
        int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_radiusTopLeft, 0);
        int dimensionPixelSize4 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_radiusTopRight, 0);
        int dimensionPixelSize5 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_radiusBottomLeft, 0);
        int dimensionPixelSize6 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIRoundButton_qmui_radiusBottomRight, 0);
        obtainStyledAttributes.recycle();
        QMUIRoundButtonDrawable qMUIRoundButtonDrawable = new QMUIRoundButtonDrawable();
        qMUIRoundButtonDrawable.setBgData(colorStateList);
        qMUIRoundButtonDrawable.setStrokeData(dimensionPixelSize, colorStateList2);
        if (dimensionPixelSize3 > 0 || dimensionPixelSize4 > 0 || dimensionPixelSize5 > 0 || dimensionPixelSize6 > 0) {
            float f = dimensionPixelSize3;
            float f2 = dimensionPixelSize4;
            float f3 = dimensionPixelSize6;
            float f4 = dimensionPixelSize5;
            qMUIRoundButtonDrawable.setCornerRadii(new float[]{f, f, f2, f2, f3, f3, f4, f4});
        } else {
            qMUIRoundButtonDrawable.setCornerRadius(dimensionPixelSize2);
            if (dimensionPixelSize2 <= 0) {
                z = z2;
            }
        }
        qMUIRoundButtonDrawable.setIsRadiusAdjustBounds(z);
        return qMUIRoundButtonDrawable;
    }

    private boolean hasNativeStateListAPI() {
        return Build.VERSION.SDK_INT >= 21;
    }

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    public boolean isStateful() {
        return (this.mFillColors != null && this.mFillColors.isStateful()) || (this.mStrokeColors != null && this.mStrokeColors.isStateful()) || super.isStateful();
    }

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        if (this.mRadiusAdjustBounds) {
            setCornerRadius(Math.min(rect.width(), rect.height()) / 2);
        }
    }

    @Override // android.graphics.drawable.GradientDrawable, android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        if (this.mFillColors != null) {
            setColor(this.mFillColors.getColorForState(iArr, 0));
            onStateChange = true;
        }
        if (this.mStrokeColors == null) {
            return onStateChange;
        }
        setStroke(this.mStrokeWidth, this.mStrokeColors.getColorForState(iArr, 0));
        return true;
    }

    public void setBgData(@Nullable ColorStateList colorStateList) {
        if (hasNativeStateListAPI()) {
            super.setColor(colorStateList);
        } else {
            this.mFillColors = colorStateList;
            setColor(colorStateList != null ? colorStateList.getColorForState(getState(), 0) : 0);
        }
    }

    public void setIsRadiusAdjustBounds(boolean z) {
        this.mRadiusAdjustBounds = z;
    }

    public void setStrokeData(int i, @Nullable ColorStateList colorStateList) {
        if (hasNativeStateListAPI()) {
            super.setStroke(i, colorStateList);
            return;
        }
        this.mStrokeWidth = i;
        this.mStrokeColors = colorStateList;
        setStroke(i, colorStateList != null ? colorStateList.getColorForState(getState(), 0) : 0);
    }
}
