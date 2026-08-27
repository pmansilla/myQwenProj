package com.qmuiteam.qmui.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIResHelper;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class QMUILayoutHelper implements IQMUILayout {
    private int mBorderColor;
    private RectF mBorderRect;
    private int mBorderWidth;
    private int mBottomDividerColor;
    private int mBottomDividerHeight;
    private int mBottomDividerInsetLeft;
    private int mBottomDividerInsetRight;
    private Paint mClipPaint;
    private Context mContext;
    private Paint mDividerPaint;
    private int mHeightLimit;
    private int mHeightMini;
    private int mHideRadiusSide;
    private boolean mIsOutlineExcludePadding;
    private boolean mIsShowBorderOnlyBeforeL;
    private int mLeftDividerColor;
    private int mLeftDividerInsetBottom;
    private int mLeftDividerInsetTop;
    private int mLeftDividerWidth;
    private PorterDuffXfermode mMode;
    private int mOuterNormalColor;
    private int mOutlineInsetBottom;
    private int mOutlineInsetLeft;
    private int mOutlineInsetRight;
    private int mOutlineInsetTop;
    private WeakReference<View> mOwner;
    private int mRadius;
    private float[] mRadiusArray;
    private int mRightDividerColor;
    private int mRightDividerInsetBottom;
    private int mRightDividerInsetTop;
    private int mRightDividerWidth;
    private float mShadowAlpha;
    private int mTopDividerColor;
    private int mTopDividerHeight;
    private int mTopDividerInsetLeft;
    private int mTopDividerInsetRight;
    private int mWidthLimit;
    private int mWidthMini;
    private int mTopDividerAlpha = 255;
    private int mBottomDividerAlpha = 255;
    private int mLeftDividerAlpha = 255;
    private int mRightDividerAlpha = 255;
    private Path mPath = new Path();
    private int mShadowElevation = 0;

    public QMUILayoutHelper(Context context, AttributeSet attributeSet, int i, View view) {
        boolean z;
        int i2;
        int i3 = 0;
        this.mWidthLimit = 0;
        this.mHeightLimit = 0;
        this.mWidthMini = 0;
        this.mHeightMini = 0;
        this.mTopDividerHeight = 0;
        this.mTopDividerInsetLeft = 0;
        this.mTopDividerInsetRight = 0;
        this.mBottomDividerHeight = 0;
        this.mBottomDividerInsetLeft = 0;
        this.mBottomDividerInsetRight = 0;
        this.mLeftDividerWidth = 0;
        this.mLeftDividerInsetTop = 0;
        this.mLeftDividerInsetBottom = 0;
        this.mRightDividerWidth = 0;
        this.mRightDividerInsetTop = 0;
        this.mRightDividerInsetBottom = 0;
        this.mHideRadiusSide = 0;
        this.mBorderColor = 0;
        this.mBorderWidth = 1;
        this.mOuterNormalColor = 0;
        this.mIsOutlineExcludePadding = false;
        this.mIsShowBorderOnlyBeforeL = true;
        this.mShadowAlpha = 0.0f;
        this.mOutlineInsetLeft = 0;
        this.mOutlineInsetRight = 0;
        this.mOutlineInsetTop = 0;
        this.mOutlineInsetBottom = 0;
        this.mContext = context;
        this.mOwner = new WeakReference<>(view);
        int color = ContextCompat.getColor(context, R.color.qmui_config_color_separator);
        this.mTopDividerColor = color;
        this.mBottomDividerColor = color;
        this.mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        this.mClipPaint = new Paint();
        this.mClipPaint.setAntiAlias(true);
        this.mShadowAlpha = QMUIResHelper.getAttrFloatValue(context, R.attr.qmui_general_shadow_alpha);
        this.mBorderRect = new RectF();
        if (attributeSet == null && i == 0) {
            z = false;
            i2 = 0;
        } else {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUILayout, i, 0);
            int indexCount = obtainStyledAttributes.getIndexCount();
            int i4 = 0;
            z = false;
            i2 = 0;
            for (int i5 = 0; i5 < indexCount; i5++) {
                int index = obtainStyledAttributes.getIndex(i5);
                if (index == R.styleable.QMUILayout_android_maxWidth) {
                    this.mWidthLimit = obtainStyledAttributes.getDimensionPixelSize(index, this.mWidthLimit);
                } else if (index == R.styleable.QMUILayout_android_maxHeight) {
                    this.mHeightLimit = obtainStyledAttributes.getDimensionPixelSize(index, this.mHeightLimit);
                } else if (index == R.styleable.QMUILayout_android_minWidth) {
                    this.mWidthMini = obtainStyledAttributes.getDimensionPixelSize(index, this.mWidthMini);
                } else if (index == R.styleable.QMUILayout_android_minHeight) {
                    this.mHeightMini = obtainStyledAttributes.getDimensionPixelSize(index, this.mHeightMini);
                } else if (index == R.styleable.QMUILayout_qmui_topDividerColor) {
                    this.mTopDividerColor = obtainStyledAttributes.getColor(index, this.mTopDividerColor);
                } else if (index == R.styleable.QMUILayout_qmui_topDividerHeight) {
                    this.mTopDividerHeight = obtainStyledAttributes.getDimensionPixelSize(index, this.mTopDividerHeight);
                } else if (index == R.styleable.QMUILayout_qmui_topDividerInsetLeft) {
                    this.mTopDividerInsetLeft = obtainStyledAttributes.getDimensionPixelSize(index, this.mTopDividerInsetLeft);
                } else if (index == R.styleable.QMUILayout_qmui_topDividerInsetRight) {
                    this.mTopDividerInsetRight = obtainStyledAttributes.getDimensionPixelSize(index, this.mTopDividerInsetRight);
                } else if (index == R.styleable.QMUILayout_qmui_bottomDividerColor) {
                    this.mBottomDividerColor = obtainStyledAttributes.getColor(index, this.mBottomDividerColor);
                } else if (index == R.styleable.QMUILayout_qmui_bottomDividerHeight) {
                    this.mBottomDividerHeight = obtainStyledAttributes.getDimensionPixelSize(index, this.mBottomDividerHeight);
                } else if (index == R.styleable.QMUILayout_qmui_bottomDividerInsetLeft) {
                    this.mBottomDividerInsetLeft = obtainStyledAttributes.getDimensionPixelSize(index, this.mBottomDividerInsetLeft);
                } else if (index == R.styleable.QMUILayout_qmui_bottomDividerInsetRight) {
                    this.mBottomDividerInsetRight = obtainStyledAttributes.getDimensionPixelSize(index, this.mBottomDividerInsetRight);
                } else if (index == R.styleable.QMUILayout_qmui_leftDividerColor) {
                    this.mLeftDividerColor = obtainStyledAttributes.getColor(index, this.mLeftDividerColor);
                } else if (index == R.styleable.QMUILayout_qmui_leftDividerWidth) {
                    this.mLeftDividerWidth = obtainStyledAttributes.getDimensionPixelSize(index, this.mBottomDividerHeight);
                } else if (index == R.styleable.QMUILayout_qmui_leftDividerInsetTop) {
                    this.mLeftDividerInsetTop = obtainStyledAttributes.getDimensionPixelSize(index, this.mLeftDividerInsetTop);
                } else if (index == R.styleable.QMUILayout_qmui_leftDividerInsetBottom) {
                    this.mLeftDividerInsetBottom = obtainStyledAttributes.getDimensionPixelSize(index, this.mLeftDividerInsetBottom);
                } else if (index == R.styleable.QMUILayout_qmui_rightDividerColor) {
                    this.mRightDividerColor = obtainStyledAttributes.getColor(index, this.mRightDividerColor);
                } else if (index == R.styleable.QMUILayout_qmui_rightDividerWidth) {
                    this.mRightDividerWidth = obtainStyledAttributes.getDimensionPixelSize(index, this.mRightDividerWidth);
                } else if (index == R.styleable.QMUILayout_qmui_rightDividerInsetTop) {
                    this.mRightDividerInsetTop = obtainStyledAttributes.getDimensionPixelSize(index, this.mRightDividerInsetTop);
                } else if (index == R.styleable.QMUILayout_qmui_rightDividerInsetBottom) {
                    this.mRightDividerInsetBottom = obtainStyledAttributes.getDimensionPixelSize(index, this.mRightDividerInsetBottom);
                } else if (index == R.styleable.QMUILayout_qmui_borderColor) {
                    this.mBorderColor = obtainStyledAttributes.getColor(index, this.mBorderColor);
                } else if (index == R.styleable.QMUILayout_qmui_borderWidth) {
                    this.mBorderWidth = obtainStyledAttributes.getDimensionPixelSize(index, this.mBorderWidth);
                } else if (index == R.styleable.QMUILayout_qmui_radius) {
                    i2 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUILayout_qmui_outerNormalColor) {
                    this.mOuterNormalColor = obtainStyledAttributes.getColor(index, this.mOuterNormalColor);
                } else if (index == R.styleable.QMUILayout_qmui_hideRadiusSide) {
                    this.mHideRadiusSide = obtainStyledAttributes.getColor(index, this.mHideRadiusSide);
                } else if (index == R.styleable.QMUILayout_qmui_showBorderOnlyBeforeL) {
                    this.mIsShowBorderOnlyBeforeL = obtainStyledAttributes.getBoolean(index, this.mIsShowBorderOnlyBeforeL);
                } else if (index == R.styleable.QMUILayout_qmui_shadowElevation) {
                    i4 = obtainStyledAttributes.getDimensionPixelSize(index, i4);
                } else if (index == R.styleable.QMUILayout_qmui_shadowAlpha) {
                    this.mShadowAlpha = obtainStyledAttributes.getFloat(index, this.mShadowAlpha);
                } else if (index == R.styleable.QMUILayout_qmui_useThemeGeneralShadowElevation) {
                    z = obtainStyledAttributes.getBoolean(index, false);
                } else if (index == R.styleable.QMUILayout_qmui_outlineInsetLeft) {
                    this.mOutlineInsetLeft = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUILayout_qmui_outlineInsetRight) {
                    this.mOutlineInsetRight = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUILayout_qmui_outlineInsetTop) {
                    this.mOutlineInsetTop = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUILayout_qmui_outlineInsetBottom) {
                    this.mOutlineInsetBottom = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUILayout_qmui_outlineExcludePadding) {
                    this.mIsOutlineExcludePadding = obtainStyledAttributes.getBoolean(index, false);
                }
            }
            obtainStyledAttributes.recycle();
            i3 = i4;
        }
        if (i3 == 0 && z) {
            i3 = QMUIResHelper.getAttrDimen(context, R.attr.qmui_general_shadow_elevation);
        }
        setRadiusAndShadow(i2, this.mHideRadiusSide, i3, this.mShadowAlpha);
    }

    private void drawRoundRect(Canvas canvas, RectF rectF, float[] fArr, Paint paint) {
        this.mPath.reset();
        this.mPath.addRoundRect(rectF, fArr, Path.Direction.CW);
        canvas.drawPath(this.mPath, paint);
    }

    private void invalidate() {
        View view;
        if (!useFeature() || (view = this.mOwner.get()) == null) {
            return;
        }
        if (this.mShadowElevation == 0) {
            view.setElevation(0.0f);
        } else {
            view.setElevation(this.mShadowElevation);
        }
        view.invalidateOutline();
    }

    public static boolean useFeature() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public void dispatchRoundBorderDraw(Canvas canvas) {
        if (this.mOwner.get() == null) {
            return;
        }
        if (this.mBorderColor == 0 && (this.mRadius == 0 || this.mOuterNormalColor == 0)) {
            return;
        }
        if (this.mIsShowBorderOnlyBeforeL && useFeature() && this.mShadowElevation != 0) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (this.mIsOutlineExcludePadding) {
            this.mBorderRect.set(r0.getPaddingLeft() + 1, r0.getPaddingTop() + 1, (width - 1) - r0.getPaddingRight(), (height - 1) - r0.getPaddingBottom());
        } else {
            this.mBorderRect.set(1.0f, 1.0f, width - 1, height - 1);
        }
        if (this.mRadius == 0 || (!useFeature() && this.mOuterNormalColor == 0)) {
            this.mClipPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(this.mBorderRect, this.mClipPaint);
            return;
        }
        if (!useFeature()) {
            int saveLayer = canvas.saveLayer(0.0f, 0.0f, width, height, null, 31);
            canvas.drawColor(this.mOuterNormalColor);
            this.mClipPaint.setColor(this.mOuterNormalColor);
            this.mClipPaint.setStyle(Paint.Style.FILL);
            this.mClipPaint.setXfermode(this.mMode);
            if (this.mRadiusArray == null) {
                canvas.drawRoundRect(this.mBorderRect, this.mRadius, this.mRadius, this.mClipPaint);
            } else {
                drawRoundRect(canvas, this.mBorderRect, this.mRadiusArray, this.mClipPaint);
            }
            this.mClipPaint.setXfermode(null);
            canvas.restoreToCount(saveLayer);
        }
        this.mClipPaint.setColor(this.mBorderColor);
        this.mClipPaint.setStrokeWidth(this.mBorderWidth);
        this.mClipPaint.setStyle(Paint.Style.STROKE);
        if (this.mRadiusArray == null) {
            canvas.drawRoundRect(this.mBorderRect, this.mRadius, this.mRadius, this.mClipPaint);
        } else {
            drawRoundRect(canvas, this.mBorderRect, this.mRadiusArray, this.mClipPaint);
        }
    }

    public void drawDividers(Canvas canvas, int i, int i2) {
        if (this.mDividerPaint == null && (this.mTopDividerHeight > 0 || this.mBottomDividerHeight > 0 || this.mLeftDividerWidth > 0 || this.mRightDividerWidth > 0)) {
            this.mDividerPaint = new Paint();
        }
        if (this.mTopDividerHeight > 0) {
            this.mDividerPaint.setStrokeWidth(this.mTopDividerHeight);
            this.mDividerPaint.setColor(this.mTopDividerColor);
            this.mDividerPaint.setAlpha(this.mTopDividerAlpha);
            float f = (this.mTopDividerHeight * 1.0f) / 2.0f;
            canvas.drawLine(this.mTopDividerInsetLeft, f, i - this.mTopDividerInsetRight, f, this.mDividerPaint);
        }
        if (this.mBottomDividerHeight > 0) {
            this.mDividerPaint.setStrokeWidth(this.mBottomDividerHeight);
            this.mDividerPaint.setColor(this.mBottomDividerColor);
            this.mDividerPaint.setAlpha(this.mBottomDividerAlpha);
            float floor = (float) Math.floor(i2 - ((this.mBottomDividerHeight * 1.0f) / 2.0f));
            canvas.drawLine(this.mBottomDividerInsetLeft, floor, i - this.mBottomDividerInsetRight, floor, this.mDividerPaint);
        }
        if (this.mLeftDividerWidth > 0) {
            this.mDividerPaint.setStrokeWidth(this.mLeftDividerWidth);
            this.mDividerPaint.setColor(this.mLeftDividerColor);
            this.mDividerPaint.setAlpha(this.mLeftDividerAlpha);
            canvas.drawLine(0.0f, this.mLeftDividerInsetTop, 0.0f, i2 - this.mLeftDividerInsetBottom, this.mDividerPaint);
        }
        if (this.mRightDividerWidth > 0) {
            this.mDividerPaint.setStrokeWidth(this.mRightDividerWidth);
            this.mDividerPaint.setColor(this.mRightDividerColor);
            this.mDividerPaint.setAlpha(this.mRightDividerAlpha);
            float f2 = i;
            canvas.drawLine(f2, this.mRightDividerInsetTop, f2, i2 - this.mRightDividerInsetBottom, this.mDividerPaint);
        }
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public int getHideRadiusSide() {
        return this.mHideRadiusSide;
    }

    public int getMeasuredHeightSpec(int i) {
        return (this.mHeightLimit <= 0 || View.MeasureSpec.getSize(i) <= this.mHeightLimit) ? i : View.MeasureSpec.getMode(i) == Integer.MIN_VALUE ? View.MeasureSpec.makeMeasureSpec(this.mWidthLimit, Integer.MIN_VALUE) : View.MeasureSpec.makeMeasureSpec(this.mWidthLimit, 1073741824);
    }

    public int getMeasuredWidthSpec(int i) {
        return (this.mWidthLimit <= 0 || View.MeasureSpec.getSize(i) <= this.mWidthLimit) ? i : View.MeasureSpec.getMode(i) == Integer.MIN_VALUE ? View.MeasureSpec.makeMeasureSpec(this.mWidthLimit, Integer.MIN_VALUE) : View.MeasureSpec.makeMeasureSpec(this.mWidthLimit, 1073741824);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public int getRadius() {
        return this.mRadius;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public float getShadowAlpha() {
        return this.mShadowAlpha;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public int getShadowElevation() {
        return this.mShadowElevation;
    }

    public int handleMiniHeight(int i, int i2) {
        return (View.MeasureSpec.getMode(i) == 1073741824 || i2 >= this.mHeightMini) ? i : View.MeasureSpec.makeMeasureSpec(this.mHeightMini, 1073741824);
    }

    public int handleMiniWidth(int i, int i2) {
        return (View.MeasureSpec.getMode(i) == 1073741824 || i2 >= this.mWidthMini) ? i : View.MeasureSpec.makeMeasureSpec(this.mWidthMini, 1073741824);
    }

    public boolean isRadiusWithSideHidden() {
        return this.mRadius > 0 && this.mHideRadiusSide != 0;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void onlyShowBottomDivider(int i, int i2, int i3, int i4) {
        updateBottomDivider(i, i2, i3, i4);
        this.mLeftDividerWidth = 0;
        this.mRightDividerWidth = 0;
        this.mTopDividerHeight = 0;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void onlyShowLeftDivider(int i, int i2, int i3, int i4) {
        updateLeftDivider(i, i2, i3, i4);
        this.mRightDividerWidth = 0;
        this.mTopDividerHeight = 0;
        this.mBottomDividerHeight = 0;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void onlyShowRightDivider(int i, int i2, int i3, int i4) {
        updateRightDivider(i, i2, i3, i4);
        this.mLeftDividerWidth = 0;
        this.mTopDividerHeight = 0;
        this.mBottomDividerHeight = 0;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void onlyShowTopDivider(int i, int i2, int i3, int i4) {
        updateTopDivider(i, i2, i3, i4);
        this.mLeftDividerWidth = 0;
        this.mRightDividerWidth = 0;
        this.mBottomDividerHeight = 0;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setBorderColor(@ColorInt int i) {
        this.mBorderColor = i;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setBorderWidth(int i) {
        this.mBorderWidth = i;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setBottomDividerAlpha(int i) {
        this.mBottomDividerAlpha = i;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public boolean setHeightLimit(int i) {
        if (this.mHeightLimit == i) {
            return false;
        }
        this.mHeightLimit = i;
        return true;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setHideRadiusSide(int i) {
        if (this.mHideRadiusSide == i) {
            return;
        }
        setRadiusAndShadow(this.mRadius, i, this.mShadowElevation, this.mShadowAlpha);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setLeftDividerAlpha(int i) {
        this.mLeftDividerAlpha = i;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setOutlineExcludePadding(boolean z) {
        View view;
        if (!useFeature() || (view = this.mOwner.get()) == null) {
            return;
        }
        this.mIsOutlineExcludePadding = z;
        view.invalidateOutline();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setOutlineInset(int i, int i2, int i3, int i4) {
        View view;
        if (!useFeature() || (view = this.mOwner.get()) == null) {
            return;
        }
        this.mOutlineInsetLeft = i;
        this.mOutlineInsetRight = i3;
        this.mOutlineInsetTop = i2;
        this.mOutlineInsetBottom = i4;
        view.invalidateOutline();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRadius(int i) {
        if (this.mRadius != i) {
            setRadiusAndShadow(i, this.mShadowElevation, this.mShadowAlpha);
        }
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRadius(int i, int i2) {
        if (this.mRadius == i && i2 == this.mHideRadiusSide) {
            return;
        }
        setRadiusAndShadow(i, i2, this.mShadowElevation, this.mShadowAlpha);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRadiusAndShadow(int i, int i2, float f) {
        setRadiusAndShadow(i, this.mHideRadiusSide, i2, f);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRadiusAndShadow(int i, int i2, int i3, float f) {
        View view = this.mOwner.get();
        if (view == null) {
            return;
        }
        this.mRadius = i;
        this.mHideRadiusSide = i2;
        if (this.mRadius > 0) {
            if (i2 == 1) {
                this.mRadiusArray = new float[]{0.0f, 0.0f, 0.0f, 0.0f, this.mRadius, this.mRadius, this.mRadius, this.mRadius};
            } else if (i2 == 2) {
                this.mRadiusArray = new float[]{this.mRadius, this.mRadius, 0.0f, 0.0f, 0.0f, 0.0f, this.mRadius, this.mRadius};
            } else if (i2 == 3) {
                this.mRadiusArray = new float[]{this.mRadius, this.mRadius, this.mRadius, this.mRadius, 0.0f, 0.0f, 0.0f, 0.0f};
            } else if (i2 == 4) {
                this.mRadiusArray = new float[]{0.0f, 0.0f, this.mRadius, this.mRadius, this.mRadius, this.mRadius, 0.0f, 0.0f};
            } else {
                this.mRadiusArray = null;
            }
        }
        this.mShadowElevation = i3;
        this.mShadowAlpha = f;
        if (useFeature()) {
            if (this.mShadowElevation == 0 || isRadiusWithSideHidden()) {
                view.setElevation(0.0f);
            } else {
                view.setElevation(this.mShadowElevation);
            }
            view.setOutlineProvider(new ViewOutlineProvider() { // from class: com.qmuiteam.qmui.layout.QMUILayoutHelper.1
                @Override // android.view.ViewOutlineProvider
                @TargetApi(21)
                public void getOutline(View view2, Outline outline) {
                    int i4;
                    int i5;
                    int i6;
                    int i7;
                    int width = view2.getWidth();
                    int height = view2.getHeight();
                    if (width == 0 || height == 0) {
                        return;
                    }
                    if (!QMUILayoutHelper.this.isRadiusWithSideHidden()) {
                        int i8 = QMUILayoutHelper.this.mOutlineInsetTop;
                        int max = Math.max(i8 + 1, height - QMUILayoutHelper.this.mOutlineInsetBottom);
                        int i9 = QMUILayoutHelper.this.mOutlineInsetLeft;
                        int i10 = width - QMUILayoutHelper.this.mOutlineInsetRight;
                        if (QMUILayoutHelper.this.mIsOutlineExcludePadding) {
                            i9 += view2.getPaddingLeft();
                            i8 += view2.getPaddingTop();
                            i10 = Math.max(i9 + 1, i10 - view2.getPaddingRight());
                            max = Math.max(i8 + 1, max - view2.getPaddingBottom());
                        }
                        int i11 = i10;
                        int i12 = max;
                        int i13 = i8;
                        int i14 = i9;
                        outline.setAlpha(QMUILayoutHelper.this.mShadowAlpha);
                        if (QMUILayoutHelper.this.mRadius <= 0) {
                            outline.setRect(i14, i13, i11, i12);
                            return;
                        } else {
                            outline.setRoundRect(i14, i13, i11, i12, QMUILayoutHelper.this.mRadius);
                            return;
                        }
                    }
                    if (QMUILayoutHelper.this.mHideRadiusSide == 4) {
                        i6 = 0 - QMUILayoutHelper.this.mRadius;
                        i4 = width;
                        i5 = height;
                    } else {
                        if (QMUILayoutHelper.this.mHideRadiusSide == 1) {
                            i7 = 0 - QMUILayoutHelper.this.mRadius;
                            i4 = width;
                            i5 = height;
                            i6 = 0;
                            outline.setRoundRect(i6, i7, i4, i5, QMUILayoutHelper.this.mRadius);
                        }
                        if (QMUILayoutHelper.this.mHideRadiusSide == 2) {
                            width += QMUILayoutHelper.this.mRadius;
                        } else if (QMUILayoutHelper.this.mHideRadiusSide == 3) {
                            height += QMUILayoutHelper.this.mRadius;
                        }
                        i4 = width;
                        i5 = height;
                        i6 = 0;
                    }
                    i7 = 0;
                    outline.setRoundRect(i6, i7, i4, i5, QMUILayoutHelper.this.mRadius);
                }
            });
            view.setClipToOutline(this.mRadius > 0);
        }
        view.invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setRightDividerAlpha(int i) {
        this.mRightDividerAlpha = i;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setShadowAlpha(float f) {
        if (this.mShadowAlpha == f) {
            return;
        }
        this.mShadowAlpha = f;
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setShadowElevation(int i) {
        if (this.mShadowElevation == i) {
            return;
        }
        this.mShadowElevation = i;
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setShowBorderOnlyBeforeL(boolean z) {
        this.mIsShowBorderOnlyBeforeL = z;
        invalidate();
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setTopDividerAlpha(int i) {
        this.mTopDividerAlpha = i;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void setUseThemeGeneralShadowElevation() {
        this.mShadowElevation = QMUIResHelper.getAttrDimen(this.mContext, R.attr.qmui_general_shadow_elevation);
        setRadiusAndShadow(this.mRadius, this.mHideRadiusSide, this.mShadowElevation, this.mShadowAlpha);
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public boolean setWidthLimit(int i) {
        if (this.mWidthLimit == i) {
            return false;
        }
        this.mWidthLimit = i;
        return true;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void updateBottomDivider(int i, int i2, int i3, int i4) {
        this.mBottomDividerInsetLeft = i;
        this.mBottomDividerInsetRight = i2;
        this.mBottomDividerColor = i4;
        this.mBottomDividerHeight = i3;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void updateLeftDivider(int i, int i2, int i3, int i4) {
        this.mLeftDividerInsetTop = i;
        this.mLeftDividerInsetBottom = i2;
        this.mLeftDividerWidth = i3;
        this.mLeftDividerColor = i4;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void updateRightDivider(int i, int i2, int i3, int i4) {
        this.mRightDividerInsetTop = i;
        this.mRightDividerInsetBottom = i2;
        this.mRightDividerWidth = i3;
        this.mRightDividerColor = i4;
    }

    @Override // com.qmuiteam.qmui.layout.IQMUILayout
    public void updateTopDivider(int i, int i2, int i3, int i4) {
        this.mTopDividerInsetLeft = i;
        this.mTopDividerInsetRight = i2;
        this.mTopDividerHeight = i3;
        this.mTopDividerColor = i4;
    }
}
