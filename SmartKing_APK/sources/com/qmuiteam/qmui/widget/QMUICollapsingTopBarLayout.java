package com.qmuiteam.qmui.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.qmuiteam.qmui.QMUIInterpolatorStaticHolder;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUICollapsingTextHelper;
import com.qmuiteam.qmui.util.QMUILangHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.util.QMUIViewOffsetHelper;

/* loaded from: classes2.dex */
public class QMUICollapsingTopBarLayout extends FrameLayout implements IWindowInsetLayout {
    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;
    final QMUICollapsingTextHelper mCollapsingTextHelper;
    private boolean mCollapsingTitleEnabled;
    private Drawable mContentScrim;
    int mCurrentOffset;
    private int mExpandedMarginBottom;
    private int mExpandedMarginEnd;
    private int mExpandedMarginStart;
    private int mExpandedMarginTop;
    Rect mLastInsetRect;
    WindowInsetsCompat mLastInsets;
    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    private boolean mRefreshToolbar;
    private int mScrimAlpha;
    private long mScrimAnimationDuration;
    private ValueAnimator mScrimAnimator;
    private ValueAnimator.AnimatorUpdateListener mScrimUpdateListener;
    private int mScrimVisibleHeightTrigger;
    private boolean mScrimsAreShown;
    Drawable mStatusBarScrim;
    private final Rect mTmpRect;
    private QMUITopBar mTopBar;
    private View mTopBarDirectChild;
    private int mTopBarId;

    /* loaded from: classes2.dex */
    public static class LayoutParams extends FrameLayout.LayoutParams {
        public static final int COLLAPSE_MODE_OFF = 0;
        public static final int COLLAPSE_MODE_PARALLAX = 2;
        public static final int COLLAPSE_MODE_PIN = 1;
        private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5f;
        int mCollapseMode;
        float mParallaxMult;

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }

        public LayoutParams(int i, int i2, int i3) {
            super(i, i2, i3);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUICollapsingTopBarLayout_Layout);
            this.mCollapseMode = obtainStyledAttributes.getInt(R.styleable.QMUICollapsingTopBarLayout_Layout_qmui_layout_collapseMode, 0);
            setParallaxMultiplier(obtainStyledAttributes.getFloat(R.styleable.QMUICollapsingTopBarLayout_Layout_qmui_layout_collapseParallaxMultiplier, 0.5f));
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }

        @RequiresApi(19)
        @TargetApi(19)
        public LayoutParams(FrameLayout.LayoutParams layoutParams) {
            super(layoutParams);
            this.mCollapseMode = 0;
            this.mParallaxMult = 0.5f;
        }

        public int getCollapseMode() {
            return this.mCollapseMode;
        }

        public float getParallaxMultiplier() {
            return this.mParallaxMult;
        }

        public void setCollapseMode(int i) {
            this.mCollapseMode = i;
        }

        public void setParallaxMultiplier(float f) {
            this.mParallaxMult = f;
        }
    }

    /* loaded from: classes2.dex */
    private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
        OffsetUpdateListener() {
        }

        @Override // android.support.design.widget.AppBarLayout.OnOffsetChangedListener
        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            QMUICollapsingTopBarLayout.this.mCurrentOffset = i;
            int windowInsetTop = QMUICollapsingTopBarLayout.this.getWindowInsetTop();
            int childCount = QMUICollapsingTopBarLayout.this.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = QMUICollapsingTopBarLayout.this.getChildAt(i2);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                QMUIViewOffsetHelper viewOffsetHelper = QMUICollapsingTopBarLayout.getViewOffsetHelper(childAt);
                switch (layoutParams.mCollapseMode) {
                    case 1:
                        viewOffsetHelper.setTopAndBottomOffset(QMUILangHelper.constrain(-i, 0, QMUICollapsingTopBarLayout.this.getMaxOffsetForPinChild(childAt)));
                        break;
                    case 2:
                        viewOffsetHelper.setTopAndBottomOffset(Math.round((-i) * layoutParams.mParallaxMult));
                        break;
                }
            }
            QMUICollapsingTopBarLayout.this.updateScrimVisibility();
            if (QMUICollapsingTopBarLayout.this.mStatusBarScrim != null && windowInsetTop > 0) {
                ViewCompat.postInvalidateOnAnimation(QMUICollapsingTopBarLayout.this);
            }
            QMUICollapsingTopBarLayout.this.mCollapsingTextHelper.setExpansionFraction(Math.abs(i) / ((QMUICollapsingTopBarLayout.this.getHeight() - ViewCompat.getMinimumHeight(QMUICollapsingTopBarLayout.this)) - windowInsetTop));
        }
    }

    public QMUICollapsingTopBarLayout(Context context) {
        this(context, null);
    }

    public QMUICollapsingTopBarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QMUICollapsingTopBarLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRefreshToolbar = true;
        this.mTmpRect = new Rect();
        this.mScrimVisibleHeightTrigger = -1;
        this.mCollapsingTextHelper = new QMUICollapsingTextHelper(this);
        this.mCollapsingTextHelper.setTextSizeInterpolator(QMUIInterpolatorStaticHolder.DECELERATE_INTERPOLATOR);
        QMUIViewHelper.checkAppCompatTheme(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUICollapsingTopBarLayout, i, 0);
        this.mCollapsingTextHelper.setExpandedTextGravity(obtainStyledAttributes.getInt(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleGravity, 81));
        this.mCollapsingTextHelper.setCollapsedTextGravity(obtainStyledAttributes.getInt(R.styleable.QMUICollapsingTopBarLayout_qmui_collapsedTitleGravity, 8388627));
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleMargin, 0);
        this.mExpandedMarginBottom = dimensionPixelSize;
        this.mExpandedMarginEnd = dimensionPixelSize;
        this.mExpandedMarginTop = dimensionPixelSize;
        this.mExpandedMarginStart = dimensionPixelSize;
        if (obtainStyledAttributes.hasValue(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleMarginStart)) {
            this.mExpandedMarginStart = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleMarginStart, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleMarginEnd)) {
            this.mExpandedMarginEnd = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleMarginEnd, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleMarginTop)) {
            this.mExpandedMarginTop = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleMarginTop, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleMarginBottom)) {
            this.mExpandedMarginBottom = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleMarginBottom, 0);
        }
        this.mCollapsingTitleEnabled = obtainStyledAttributes.getBoolean(R.styleable.QMUICollapsingTopBarLayout_qmui_titleEnabled, true);
        setTitle(obtainStyledAttributes.getText(R.styleable.QMUICollapsingTopBarLayout_qmui_title));
        this.mCollapsingTextHelper.setExpandedTextAppearance(R.style.QMUI_CollapsingTopBarLayoutExpanded);
        this.mCollapsingTextHelper.setCollapsedTextAppearance(R.style.QMUI_CollapsingTopBarLayoutCollapsed);
        if (obtainStyledAttributes.hasValue(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleTextAppearance)) {
            this.mCollapsingTextHelper.setExpandedTextAppearance(obtainStyledAttributes.getResourceId(R.styleable.QMUICollapsingTopBarLayout_qmui_expandedTitleTextAppearance, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.QMUICollapsingTopBarLayout_qmui_collapsedTitleTextAppearance)) {
            this.mCollapsingTextHelper.setCollapsedTextAppearance(obtainStyledAttributes.getResourceId(R.styleable.QMUICollapsingTopBarLayout_qmui_collapsedTitleTextAppearance, 0));
        }
        this.mScrimVisibleHeightTrigger = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUICollapsingTopBarLayout_qmui_scrimVisibleHeightTrigger, -1);
        this.mScrimAnimationDuration = obtainStyledAttributes.getInt(R.styleable.QMUICollapsingTopBarLayout_qmui_scrimAnimationDuration, DEFAULT_SCRIM_ANIMATION_DURATION);
        setContentScrim(obtainStyledAttributes.getDrawable(R.styleable.QMUICollapsingTopBarLayout_qmui_contentScrim));
        setStatusBarScrim(obtainStyledAttributes.getDrawable(R.styleable.QMUICollapsingTopBarLayout_qmui_statusBarScrim));
        this.mTopBarId = obtainStyledAttributes.getResourceId(R.styleable.QMUICollapsingTopBarLayout_qmui_topBarId, -1);
        obtainStyledAttributes.recycle();
        setWillNotDraw(false);
        ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() { // from class: com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout.1
            @Override // android.support.v4.view.OnApplyWindowInsetsListener
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return QMUICollapsingTopBarLayout.this.setWindowInsets(windowInsetsCompat);
            }
        });
    }

    private void animateScrim(int i) {
        ensureToolbar();
        if (this.mScrimAnimator == null) {
            this.mScrimAnimator = new ValueAnimator();
            this.mScrimAnimator.setDuration(this.mScrimAnimationDuration);
            this.mScrimAnimator.setInterpolator(i > this.mScrimAlpha ? QMUIInterpolatorStaticHolder.FAST_OUT_LINEAR_IN_INTERPOLATOR : QMUIInterpolatorStaticHolder.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
            this.mScrimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.qmuiteam.qmui.widget.QMUICollapsingTopBarLayout.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    QMUICollapsingTopBarLayout.this.setScrimAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
                }
            });
            if (this.mScrimUpdateListener != null) {
                this.mScrimAnimator.addUpdateListener(this.mScrimUpdateListener);
            }
        } else if (this.mScrimAnimator.isRunning()) {
            this.mScrimAnimator.cancel();
        }
        this.mScrimAnimator.setIntValues(this.mScrimAlpha, i);
        this.mScrimAnimator.start();
    }

    private void ensureToolbar() {
        if (this.mRefreshToolbar) {
            QMUITopBar qMUITopBar = null;
            this.mTopBar = null;
            this.mTopBarDirectChild = null;
            if (this.mTopBarId != -1) {
                this.mTopBar = (QMUITopBar) findViewById(this.mTopBarId);
                if (this.mTopBar != null) {
                    this.mTopBarDirectChild = findDirectChild(this.mTopBar);
                }
            }
            if (this.mTopBar == null) {
                int childCount = getChildCount();
                int i = 0;
                while (true) {
                    if (i >= childCount) {
                        break;
                    }
                    View childAt = getChildAt(i);
                    if (childAt instanceof QMUITopBar) {
                        qMUITopBar = (QMUITopBar) childAt;
                        break;
                    }
                    i++;
                }
                this.mTopBar = qMUITopBar;
            }
            this.mRefreshToolbar = false;
        }
    }

    private View findDirectChild(View view) {
        for (ViewParent parent = view.getParent(); parent != this && parent != null; parent = parent.getParent()) {
            if (parent instanceof View) {
                view = parent;
            }
        }
        return view;
    }

    private static int getHeightWithMargins(@NonNull View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return view.getHeight();
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        return view.getHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    static QMUIViewOffsetHelper getViewOffsetHelper(View view) {
        QMUIViewOffsetHelper qMUIViewOffsetHelper = (QMUIViewOffsetHelper) view.getTag(R.id.qmui_view_offset_helper);
        if (qMUIViewOffsetHelper != null) {
            return qMUIViewOffsetHelper;
        }
        QMUIViewOffsetHelper qMUIViewOffsetHelper2 = new QMUIViewOffsetHelper(view);
        view.setTag(R.id.qmui_view_offset_helper, qMUIViewOffsetHelper2);
        return qMUIViewOffsetHelper2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getWindowInsetTop() {
        if (this.mLastInsets != null) {
            return this.mLastInsets.getSystemWindowInsetTop();
        }
        if (this.mLastInsetRect != null) {
            return this.mLastInsetRect.top;
        }
        return 0;
    }

    private boolean isToolbarChild(View view) {
        if (this.mTopBarDirectChild == null || this.mTopBarDirectChild == this) {
            if (view != this.mTopBar) {
                return false;
            }
        } else if (view != this.mTopBarDirectChild) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WindowInsetsCompat setWindowInsets(WindowInsetsCompat windowInsetsCompat) {
        return (Build.VERSION.SDK_INT < 21 || !applySystemWindowInsets21(windowInsetsCompat)) ? windowInsetsCompat : windowInsetsCompat.consumeSystemWindowInsets();
    }

    @Override // com.qmuiteam.qmui.widget.IWindowInsetLayout
    public boolean applySystemWindowInsets19(Rect rect) {
        if (!ViewCompat.getFitsSystemWindows(this)) {
            rect = null;
        }
        if (QMUILangHelper.objectEquals(this.mLastInsets, rect)) {
            return true;
        }
        this.mLastInsetRect = rect;
        requestLayout();
        return true;
    }

    @Override // com.qmuiteam.qmui.widget.IWindowInsetLayout
    public boolean applySystemWindowInsets21(WindowInsetsCompat windowInsetsCompat) {
        if (!ViewCompat.getFitsSystemWindows(this)) {
            windowInsetsCompat = null;
        }
        if (QMUILangHelper.objectEquals(this.mLastInsets, windowInsetsCompat)) {
            return true;
        }
        this.mLastInsets = windowInsetsCompat;
        requestLayout();
        return true;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int windowInsetTop;
        super.draw(canvas);
        ensureToolbar();
        if (this.mTopBar == null && this.mContentScrim != null && this.mScrimAlpha > 0) {
            this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
        }
        if (this.mCollapsingTitleEnabled) {
            this.mCollapsingTextHelper.draw(canvas);
        }
        if (this.mStatusBarScrim == null || this.mScrimAlpha <= 0 || (windowInsetTop = getWindowInsetTop()) <= 0) {
            return;
        }
        this.mStatusBarScrim.setBounds(0, -this.mCurrentOffset, getWidth(), windowInsetTop - this.mCurrentOffset);
        this.mStatusBarScrim.mutate().setAlpha(this.mScrimAlpha);
        this.mStatusBarScrim.draw(canvas);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        boolean z;
        if (this.mContentScrim == null || this.mScrimAlpha <= 0 || !isToolbarChild(view)) {
            z = false;
        } else {
            this.mContentScrim.mutate().setAlpha(this.mScrimAlpha);
            this.mContentScrim.draw(canvas);
            z = true;
        }
        return super.drawChild(canvas, view, j) || z;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.mStatusBarScrim;
        boolean z = false;
        if (drawable != null && drawable.isStateful()) {
            z = false | drawable.setState(drawableState);
        }
        Drawable drawable2 = this.mContentScrim;
        if (drawable2 != null && drawable2.isStateful()) {
            z |= drawable2.setState(drawableState);
        }
        if (this.mCollapsingTextHelper != null) {
            z |= this.mCollapsingTextHelper.setState(drawableState);
        }
        if (z) {
            invalidate();
        }
    }

    @Override // android.view.View
    protected boolean fitSystemWindows(Rect rect) {
        return applySystemWindowInsets19(rect);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getCollapsedTitleGravity() {
        return this.mCollapsingTextHelper.getCollapsedTextGravity();
    }

    @NonNull
    public Typeface getCollapsedTitleTypeface() {
        return this.mCollapsingTextHelper.getCollapsedTypeface();
    }

    @Nullable
    public Drawable getContentScrim() {
        return this.mContentScrim;
    }

    public int getExpandedTitleGravity() {
        return this.mCollapsingTextHelper.getExpandedTextGravity();
    }

    public int getExpandedTitleMarginBottom() {
        return this.mExpandedMarginBottom;
    }

    public int getExpandedTitleMarginEnd() {
        return this.mExpandedMarginEnd;
    }

    public int getExpandedTitleMarginStart() {
        return this.mExpandedMarginStart;
    }

    public int getExpandedTitleMarginTop() {
        return this.mExpandedMarginTop;
    }

    @NonNull
    public Typeface getExpandedTitleTypeface() {
        return this.mCollapsingTextHelper.getExpandedTypeface();
    }

    final int getMaxOffsetForPinChild(View view) {
        return ((getHeight() - getViewOffsetHelper(view).getLayoutTop()) - view.getHeight()) - ((LayoutParams) view.getLayoutParams()).bottomMargin;
    }

    int getScrimAlpha() {
        return this.mScrimAlpha;
    }

    public long getScrimAnimationDuration() {
        return this.mScrimAnimationDuration;
    }

    public int getScrimVisibleHeightTrigger() {
        if (this.mScrimVisibleHeightTrigger >= 0) {
            return this.mScrimVisibleHeightTrigger;
        }
        int windowInsetTop = getWindowInsetTop();
        int minimumHeight = ViewCompat.getMinimumHeight(this);
        return minimumHeight > 0 ? Math.min((minimumHeight * 2) + windowInsetTop, getHeight()) : getHeight() / 3;
    }

    @Nullable
    public Drawable getStatusBarScrim() {
        return this.mStatusBarScrim;
    }

    @Nullable
    public CharSequence getTitle() {
        if (this.mCollapsingTitleEnabled) {
            return this.mCollapsingTextHelper.getText();
        }
        return null;
    }

    public boolean isTitleEnabled() {
        return this.mCollapsingTitleEnabled;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Object parent = getParent();
        if (parent instanceof AppBarLayout) {
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent));
            if (this.mOnOffsetChangedListener == null) {
                this.mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(this.mOnOffsetChangedListener);
            ViewCompat.requestApplyInsets(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        ViewParent parent = getParent();
        if (this.mOnOffsetChangedListener != null && (parent instanceof AppBarLayout)) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(this.mOnOffsetChangedListener);
        }
        super.onDetachedFromWindow();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mLastInsets != null || this.mLastInsetRect != null) {
            int windowInsetTop = getWindowInsetTop();
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                if (ViewCompat.getFitsSystemWindows(childAt) && childAt.getTop() < windowInsetTop) {
                    ViewCompat.offsetTopAndBottom(childAt, windowInsetTop);
                }
            }
        }
        int childCount2 = getChildCount();
        for (int i6 = 0; i6 < childCount2; i6++) {
            getViewOffsetHelper(getChildAt(i6)).onViewLayout();
        }
        if (this.mCollapsingTitleEnabled) {
            int maxOffsetForPinChild = getMaxOffsetForPinChild(this.mTopBarDirectChild != null ? this.mTopBarDirectChild : this.mTopBar);
            QMUIViewHelper.getDescendantRect(this, this.mTopBar, this.mTmpRect);
            Rect titleContainerRect = this.mTopBar.getTitleContainerRect();
            int i7 = this.mTmpRect.top + maxOffsetForPinChild;
            this.mCollapsingTextHelper.setCollapsedBounds(this.mTmpRect.left + titleContainerRect.left, titleContainerRect.top + i7, this.mTmpRect.left + titleContainerRect.right, i7 + titleContainerRect.bottom);
            this.mCollapsingTextHelper.setExpandedBounds(this.mExpandedMarginStart, this.mTmpRect.top + this.mExpandedMarginTop, (i3 - i) - this.mExpandedMarginEnd, (i4 - i2) - this.mExpandedMarginBottom);
            this.mCollapsingTextHelper.recalculate();
        }
        if (this.mTopBar != null) {
            if (this.mCollapsingTitleEnabled && TextUtils.isEmpty(this.mCollapsingTextHelper.getText())) {
                this.mCollapsingTextHelper.setText(this.mTopBar.getTitle());
            }
            if (this.mTopBarDirectChild == null || this.mTopBarDirectChild == this) {
                setMinimumHeight(getHeightWithMargins(this.mTopBar));
            } else {
                setMinimumHeight(getHeightWithMargins(this.mTopBarDirectChild));
            }
        }
        updateScrimVisibility();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        ensureToolbar();
        super.onMeasure(i, i2);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mContentScrim != null) {
            this.mContentScrim.setBounds(0, 0, i, i2);
        }
    }

    public void setCollapsedTitleGravity(int i) {
        this.mCollapsingTextHelper.setCollapsedTextGravity(i);
    }

    public void setCollapsedTitleTextAppearance(@StyleRes int i) {
        this.mCollapsingTextHelper.setCollapsedTextAppearance(i);
    }

    public void setCollapsedTitleTextColor(@ColorInt int i) {
        setCollapsedTitleTextColor(ColorStateList.valueOf(i));
    }

    public void setCollapsedTitleTextColor(@NonNull ColorStateList colorStateList) {
        this.mCollapsingTextHelper.setCollapsedTextColor(colorStateList);
    }

    public void setCollapsedTitleTypeface(@Nullable Typeface typeface) {
        this.mCollapsingTextHelper.setCollapsedTypeface(typeface);
    }

    public void setContentScrim(@Nullable Drawable drawable) {
        if (this.mContentScrim != drawable) {
            if (this.mContentScrim != null) {
                this.mContentScrim.setCallback(null);
            }
            this.mContentScrim = drawable != null ? drawable.mutate() : null;
            if (this.mContentScrim != null) {
                this.mContentScrim.setBounds(0, 0, getWidth(), getHeight());
                this.mContentScrim.setCallback(this);
                this.mContentScrim.setAlpha(this.mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setContentScrimColor(@ColorInt int i) {
        setContentScrim(new ColorDrawable(i));
    }

    public void setContentScrimResource(@DrawableRes int i) {
        setContentScrim(ContextCompat.getDrawable(getContext(), i));
    }

    public void setExpandedTitleColor(@ColorInt int i) {
        setExpandedTitleTextColor(ColorStateList.valueOf(i));
    }

    public void setExpandedTitleGravity(int i) {
        this.mCollapsingTextHelper.setExpandedTextGravity(i);
    }

    public void setExpandedTitleMargin(int i, int i2, int i3, int i4) {
        this.mExpandedMarginStart = i;
        this.mExpandedMarginTop = i2;
        this.mExpandedMarginEnd = i3;
        this.mExpandedMarginBottom = i4;
        requestLayout();
    }

    public void setExpandedTitleMarginBottom(int i) {
        this.mExpandedMarginBottom = i;
        requestLayout();
    }

    public void setExpandedTitleMarginEnd(int i) {
        this.mExpandedMarginEnd = i;
        requestLayout();
    }

    public void setExpandedTitleMarginStart(int i) {
        this.mExpandedMarginStart = i;
        requestLayout();
    }

    public void setExpandedTitleMarginTop(int i) {
        this.mExpandedMarginTop = i;
        requestLayout();
    }

    public void setExpandedTitleTextAppearance(@StyleRes int i) {
        this.mCollapsingTextHelper.setExpandedTextAppearance(i);
    }

    public void setExpandedTitleTextColor(@NonNull ColorStateList colorStateList) {
        this.mCollapsingTextHelper.setExpandedTextColor(colorStateList);
    }

    public void setExpandedTitleTypeface(@Nullable Typeface typeface) {
        this.mCollapsingTextHelper.setExpandedTypeface(typeface);
    }

    void setScrimAlpha(int i) {
        if (i != this.mScrimAlpha) {
            if (this.mContentScrim != null && this.mTopBar != null) {
                ViewCompat.postInvalidateOnAnimation(this.mTopBar);
            }
            this.mScrimAlpha = i;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setScrimAnimationDuration(@IntRange(from = 0) long j) {
        this.mScrimAnimationDuration = j;
    }

    public void setScrimUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        if (this.mScrimUpdateListener != animatorUpdateListener) {
            if (this.mScrimAnimator == null) {
                this.mScrimUpdateListener = animatorUpdateListener;
                return;
            }
            if (this.mScrimUpdateListener != null) {
                this.mScrimAnimator.removeUpdateListener(this.mScrimUpdateListener);
            }
            this.mScrimUpdateListener = animatorUpdateListener;
            if (this.mScrimUpdateListener != null) {
                this.mScrimAnimator.addUpdateListener(this.mScrimUpdateListener);
            }
        }
    }

    public void setScrimVisibleHeightTrigger(@IntRange(from = 0) int i) {
        if (this.mScrimVisibleHeightTrigger != i) {
            this.mScrimVisibleHeightTrigger = i;
            updateScrimVisibility();
        }
    }

    public void setScrimsShown(boolean z) {
        setScrimsShown(z, ViewCompat.isLaidOut(this) && !isInEditMode());
    }

    public void setScrimsShown(boolean z, boolean z2) {
        if (this.mScrimsAreShown != z) {
            if (z2) {
                animateScrim(z ? 255 : 0);
            } else {
                setScrimAlpha(z ? 255 : 0);
            }
            this.mScrimsAreShown = z;
        }
    }

    public void setStatusBarScrim(@Nullable Drawable drawable) {
        if (this.mStatusBarScrim != drawable) {
            if (this.mStatusBarScrim != null) {
                this.mStatusBarScrim.setCallback(null);
            }
            this.mStatusBarScrim = drawable != null ? drawable.mutate() : null;
            if (this.mStatusBarScrim != null) {
                if (this.mStatusBarScrim.isStateful()) {
                    this.mStatusBarScrim.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.mStatusBarScrim, ViewCompat.getLayoutDirection(this));
                this.mStatusBarScrim.setVisible(getVisibility() == 0, false);
                this.mStatusBarScrim.setCallback(this);
                this.mStatusBarScrim.setAlpha(this.mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setStatusBarScrimColor(@ColorInt int i) {
        setStatusBarScrim(new ColorDrawable(i));
    }

    public void setStatusBarScrimResource(@DrawableRes int i) {
        setStatusBarScrim(ContextCompat.getDrawable(getContext(), i));
    }

    public void setTitle(@Nullable CharSequence charSequence) {
        this.mCollapsingTextHelper.setText(charSequence);
    }

    public void setTitleEnabled(boolean z) {
        if (z != this.mCollapsingTitleEnabled) {
            this.mCollapsingTitleEnabled = z;
            requestLayout();
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        boolean z = i == 0;
        if (this.mStatusBarScrim != null && this.mStatusBarScrim.isVisible() != z) {
            this.mStatusBarScrim.setVisible(z, false);
        }
        if (this.mContentScrim == null || this.mContentScrim.isVisible() == z) {
            return;
        }
        this.mContentScrim.setVisible(z, false);
    }

    final void updateScrimVisibility() {
        if (this.mContentScrim == null && this.mStatusBarScrim == null) {
            return;
        }
        setScrimsShown(getHeight() + this.mCurrentOffset < getScrimVisibleHeightTrigger());
    }

    @Override // android.view.View
    protected boolean verifyDrawable(@NonNull Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mContentScrim || drawable == this.mStatusBarScrim;
    }
}
