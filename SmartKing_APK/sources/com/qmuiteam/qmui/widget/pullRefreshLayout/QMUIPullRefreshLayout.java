package com.qmuiteam.qmui.widget.pullRefreshLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;

/* loaded from: classes2.dex */
public class QMUIPullRefreshLayout extends ViewGroup implements NestedScrollingParent {
    private static final int FLAG_NEED_DELIVER_VELOCITY = 8;
    private static final int FLAG_NEED_DO_REFRESH = 4;
    private static final int FLAG_NEED_SCROLL_TO_INIT_POSITION = 1;
    private static final int FLAG_NEED_SCROLL_TO_REFRESH_POSITION = 2;
    private static final int INVALID_POINTER = -1;
    private static final String TAG = "QMUIPullRefreshLayout";
    private int mActivePointerId;
    private boolean mAutoCalculateRefreshEndOffset;
    private boolean mAutoCalculateRefreshInitOffset;
    private int mAutoScrollToRefreshMinOffset;
    private OnChildScrollUpCallback mChildScrollUpCallback;
    private float mDragRate;
    private boolean mEnableOverPull;
    private boolean mEqualTargetRefreshOffsetToRefreshViewHeight;
    private IRefreshView mIRefreshView;
    private float mInitialDownX;
    private float mInitialDownY;
    private float mInitialMotionY;
    private boolean mIsDragging;
    boolean mIsRefreshing;
    private float mLastMotionY;
    private OnPullListener mListener;
    private float mMaxVelocity;
    private float mMiniVelocity;
    private boolean mNestScrollDurationRefreshing;
    private boolean mNestedScrollInProgress;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private int mRefreshCurrentOffset;
    private int mRefreshEndOffset;
    private int mRefreshInitOffset;
    private RefreshOffsetCalculator mRefreshOffsetCalculator;
    private View mRefreshView;
    private int mRefreshZIndex;
    private int mScrollFlag;
    private Scroller mScroller;
    private int mSystemTouchSlop;
    private int mTargetCurrentOffset;
    private int mTargetInitOffset;
    private int mTargetRefreshOffset;
    private View mTargetView;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    /* loaded from: classes2.dex */
    public interface IRefreshView {
        void doRefresh();

        void onPull(int i, int i2, int i3);

        void stop();
    }

    /* loaded from: classes2.dex */
    public interface OnChildScrollUpCallback {
        boolean canChildScrollUp(QMUIPullRefreshLayout qMUIPullRefreshLayout, @Nullable View view);
    }

    /* loaded from: classes2.dex */
    public interface OnPullListener {
        void onMoveRefreshView(int i);

        void onMoveTarget(int i);

        void onRefresh();
    }

    /* loaded from: classes2.dex */
    public interface RefreshOffsetCalculator {
        int calculateRefreshOffset(int i, int i2, int i3, int i4, int i5, int i6);
    }

    /* loaded from: classes2.dex */
    public static class RefreshView extends AppCompatImageView implements IRefreshView {
        static final int CIRCLE_DIAMETER = 40;
        static final int CIRCLE_DIAMETER_LARGE = 56;
        private static final int MAX_ALPHA = 255;
        private static final float TRIM_OFFSET = 0.4f;
        private static final float TRIM_RATE = 0.85f;
        private int mCircleDiameter;
        private CircularProgressDrawable mProgress;

        public RefreshView(Context context) {
            super(context);
            this.mProgress = new CircularProgressDrawable(context);
            setColorSchemeColors(QMUIResHelper.getAttrColor(context, R.attr.qmui_config_color_blue));
            this.mProgress.setStyle(0);
            this.mProgress.setAlpha(255);
            this.mProgress.setArrowScale(0.8f);
            setImageDrawable(this.mProgress);
            this.mCircleDiameter = (int) (getResources().getDisplayMetrics().density * 40.0f);
        }

        @Override // com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout.IRefreshView
        public void doRefresh() {
            this.mProgress.start();
        }

        @Override // android.widget.ImageView, android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(this.mCircleDiameter, this.mCircleDiameter);
        }

        @Override // com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout.IRefreshView
        public void onPull(int i, int i2, int i3) {
            if (this.mProgress.isRunning()) {
                return;
            }
            Log.i("cgine", "=========");
            float f = i;
            float f2 = i2;
            float f3 = (TRIM_RATE * f) / f2;
            float f4 = (f * TRIM_OFFSET) / f2;
            if (i3 > 0) {
                f4 += (i3 * TRIM_OFFSET) / f2;
            }
            this.mProgress.setArrowEnabled(true);
            this.mProgress.setStartEndTrim(0.0f, f3);
            this.mProgress.setProgressRotation(f4);
        }

        public void setColorSchemeColors(@ColorInt int... iArr) {
            this.mProgress.setColorSchemeColors(iArr);
        }

        public void setColorSchemeResources(@ColorRes int... iArr) {
            Context context = getContext();
            int[] iArr2 = new int[iArr.length];
            for (int i = 0; i < iArr.length; i++) {
                iArr2[i] = ContextCompat.getColor(context, iArr[i]);
            }
            setColorSchemeColors(iArr2);
        }

        public void setSize(int i) {
            if (i == 0 || i == 1) {
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                if (i == 0) {
                    this.mCircleDiameter = (int) (displayMetrics.density * 56.0f);
                } else {
                    this.mCircleDiameter = (int) (displayMetrics.density * 40.0f);
                }
                setImageDrawable(null);
                this.mProgress.setStyle(i);
                setImageDrawable(this.mProgress);
            }
        }

        @Override // com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout.IRefreshView
        public void stop() {
            this.mProgress.stop();
        }
    }

    public QMUIPullRefreshLayout(Context context) {
        this(context, null);
    }

    public QMUIPullRefreshLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.QMUIPullRefreshLayoutStyle);
    }

    public QMUIPullRefreshLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        boolean z;
        this.mIsRefreshing = false;
        this.mRefreshZIndex = -1;
        boolean z2 = true;
        this.mAutoCalculateRefreshInitOffset = true;
        this.mAutoCalculateRefreshEndOffset = true;
        this.mEqualTargetRefreshOffsetToRefreshViewHeight = false;
        this.mAutoScrollToRefreshMinOffset = -1;
        this.mEnableOverPull = true;
        this.mActivePointerId = -1;
        this.mDragRate = 0.65f;
        this.mScrollFlag = 0;
        this.mNestScrollDurationRefreshing = false;
        setWillNotDraw(false);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMiniVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mSystemTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mTouchSlop = QMUIDisplayHelper.px2dp(context, this.mSystemTouchSlop);
        this.mScroller = new Scroller(getContext());
        this.mScroller.setFriction(getScrollerFriction());
        addRefreshView();
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUIPullRefreshLayout, i, 0);
        try {
            this.mRefreshInitOffset = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIPullRefreshLayout_qmui_refresh_init_offset, Integer.MIN_VALUE);
            this.mRefreshEndOffset = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIPullRefreshLayout_qmui_refresh_end_offset, Integer.MIN_VALUE);
            this.mTargetInitOffset = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIPullRefreshLayout_qmui_target_init_offset, 0);
            this.mTargetRefreshOffset = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUIPullRefreshLayout_qmui_target_refresh_offset, QMUIDisplayHelper.dp2px(getContext(), 72));
            if (this.mRefreshInitOffset != Integer.MIN_VALUE && !obtainStyledAttributes.getBoolean(R.styleable.QMUIPullRefreshLayout_qmui_auto_calculate_refresh_init_offset, false)) {
                z = false;
                this.mAutoCalculateRefreshInitOffset = z;
                if (this.mRefreshEndOffset != Integer.MIN_VALUE && !obtainStyledAttributes.getBoolean(R.styleable.QMUIPullRefreshLayout_qmui_auto_calculate_refresh_end_offset, false)) {
                    z2 = false;
                }
                this.mAutoCalculateRefreshEndOffset = z2;
                this.mEqualTargetRefreshOffsetToRefreshViewHeight = obtainStyledAttributes.getBoolean(R.styleable.QMUIPullRefreshLayout_qmui_equal_target_refresh_offset_to_refresh_view_height, false);
                obtainStyledAttributes.recycle();
                this.mRefreshCurrentOffset = this.mRefreshInitOffset;
                this.mTargetCurrentOffset = this.mTargetInitOffset;
            }
            z = true;
            this.mAutoCalculateRefreshInitOffset = z;
            if (this.mRefreshEndOffset != Integer.MIN_VALUE) {
                z2 = false;
            }
            this.mAutoCalculateRefreshEndOffset = z2;
            this.mEqualTargetRefreshOffsetToRefreshViewHeight = obtainStyledAttributes.getBoolean(R.styleable.QMUIPullRefreshLayout_qmui_equal_target_refresh_offset_to_refresh_view_height, false);
            obtainStyledAttributes.recycle();
            this.mRefreshCurrentOffset = this.mRefreshInitOffset;
            this.mTargetCurrentOffset = this.mTargetInitOffset;
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    private void acquireVelocityTracker(MotionEvent motionEvent) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
    }

    private void addRefreshView() {
        if (this.mRefreshView == null) {
            this.mRefreshView = createRefreshView();
        }
        if (!(this.mRefreshView instanceof IRefreshView)) {
            throw new RuntimeException("refreshView must be a instance of IRefreshView");
        }
        this.mIRefreshView = (IRefreshView) this.mRefreshView;
        if (this.mRefreshView.getLayoutParams() == null) {
            this.mRefreshView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        }
        addView(this.mRefreshView);
    }

    public static boolean defaultCanScrollUp(View view) {
        if (view == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(view, -1);
        }
        if (!(view instanceof AbsListView)) {
            return ViewCompat.canScrollVertically(view, -1) || view.getScrollY() > 0;
        }
        AbsListView absListView = (AbsListView) view;
        if (absListView.getChildCount() > 0) {
            return absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop();
        }
        return false;
    }

    private void deliverVelocity() {
        if (hasFlag(8)) {
            removeFlag(8);
            if (this.mScroller.getCurrVelocity() > this.mMiniVelocity) {
                info("deliver velocity: " + this.mScroller.getCurrVelocity());
                if (this.mTargetView instanceof RecyclerView) {
                    ((RecyclerView) this.mTargetView).fling(0, (int) this.mScroller.getCurrVelocity());
                } else {
                    if (!(this.mTargetView instanceof AbsListView) || Build.VERSION.SDK_INT < 21) {
                        return;
                    }
                    ((AbsListView) this.mTargetView).fling((int) this.mScroller.getCurrVelocity());
                }
            }
        }
    }

    private void ensureTargetView() {
        if (this.mTargetView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (!childAt.equals(this.mRefreshView)) {
                    onSureTargetView(childAt);
                    this.mTargetView = childAt;
                    return;
                }
            }
        }
    }

    private void finishPull(int i) {
        info("finishPull: vy = " + i + " ; mTargetCurrentOffset = " + this.mTargetCurrentOffset + " ; mTargetRefreshOffset = " + this.mTargetRefreshOffset + " ; mTargetInitOffset = " + this.mTargetInitOffset + " ; mScroller.isFinished() = " + this.mScroller.isFinished());
        int i2 = i / 1000;
        onFinishPull(i2, this.mRefreshInitOffset, this.mRefreshEndOffset, this.mRefreshView.getHeight(), this.mTargetCurrentOffset, this.mTargetInitOffset, this.mTargetRefreshOffset);
        if (this.mTargetCurrentOffset >= this.mTargetRefreshOffset) {
            if (i2 > 0) {
                this.mScrollFlag = 6;
                this.mScroller.fling(0, this.mTargetCurrentOffset, 0, i2, 0, 0, this.mTargetInitOffset, Integer.MAX_VALUE);
                invalidate();
                return;
            }
            if (i2 >= 0) {
                if (this.mTargetCurrentOffset > this.mTargetRefreshOffset) {
                    this.mScroller.startScroll(0, this.mTargetCurrentOffset, 0, this.mTargetRefreshOffset - this.mTargetCurrentOffset);
                }
                this.mScrollFlag = 4;
                invalidate();
                return;
            }
            this.mScroller.fling(0, this.mTargetCurrentOffset, 0, i, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (this.mScroller.getFinalY() < this.mTargetInitOffset) {
                this.mScrollFlag = 8;
            } else if (this.mScroller.getFinalY() < this.mTargetRefreshOffset) {
                this.mScroller.startScroll(0, this.mTargetCurrentOffset, 0, this.mTargetInitOffset - this.mTargetCurrentOffset);
            } else if (this.mScroller.getFinalY() == this.mTargetRefreshOffset) {
                this.mScrollFlag = 4;
            } else {
                this.mScroller.startScroll(0, this.mTargetCurrentOffset, 0, this.mTargetRefreshOffset - this.mTargetCurrentOffset);
                this.mScrollFlag = 4;
            }
            invalidate();
            return;
        }
        if (i2 > 0) {
            this.mScroller.fling(0, this.mTargetCurrentOffset, 0, i2, 0, 0, this.mTargetInitOffset, Integer.MAX_VALUE);
            if (this.mScroller.getFinalY() > this.mTargetRefreshOffset) {
                this.mScrollFlag = 6;
            } else if (this.mAutoScrollToRefreshMinOffset < 0 || this.mScroller.getFinalY() <= this.mAutoScrollToRefreshMinOffset) {
                this.mScrollFlag = 1;
            } else {
                this.mScroller.startScroll(0, this.mTargetCurrentOffset, 0, this.mTargetRefreshOffset - this.mTargetCurrentOffset);
                this.mScrollFlag = 4;
            }
            invalidate();
            return;
        }
        if (i2 < 0) {
            this.mScrollFlag = 0;
            this.mScroller.fling(0, this.mTargetCurrentOffset, 0, i, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (this.mScroller.getFinalY() < this.mTargetInitOffset) {
                this.mScrollFlag = 8;
            } else {
                this.mScroller.startScroll(0, this.mTargetCurrentOffset, 0, this.mTargetInitOffset - this.mTargetCurrentOffset);
                this.mScrollFlag = 0;
            }
            invalidate();
            return;
        }
        if (this.mTargetCurrentOffset == this.mTargetInitOffset) {
            return;
        }
        if (this.mAutoScrollToRefreshMinOffset < 0 || this.mTargetCurrentOffset < this.mAutoScrollToRefreshMinOffset) {
            this.mScroller.startScroll(0, this.mTargetCurrentOffset, 0, this.mTargetInitOffset - this.mTargetCurrentOffset);
            this.mScrollFlag = 0;
        } else {
            this.mScroller.startScroll(0, this.mTargetCurrentOffset, 0, this.mTargetRefreshOffset - this.mTargetCurrentOffset);
            this.mScrollFlag = 4;
        }
        invalidate();
    }

    private boolean hasFlag(int i) {
        return (this.mScrollFlag & i) == i;
    }

    private void info(String str) {
    }

    private int moveTargetView(float f, boolean z) {
        return moveTargetViewTo((int) (this.mTargetCurrentOffset + f), z);
    }

    private int moveTargetViewTo(int i, boolean z) {
        return moveTargetViewTo(i, z, false);
    }

    private int moveTargetViewTo(int i, boolean z, boolean z2) {
        int max = Math.max(i, this.mTargetInitOffset);
        if (!this.mEnableOverPull) {
            max = Math.min(max, this.mTargetRefreshOffset);
        }
        int i2 = 0;
        if (max != this.mTargetCurrentOffset || z2) {
            i2 = max - this.mTargetCurrentOffset;
            ViewCompat.offsetTopAndBottom(this.mTargetView, i2);
            this.mTargetCurrentOffset = max;
            int i3 = this.mTargetRefreshOffset - this.mTargetInitOffset;
            if (z) {
                this.mIRefreshView.onPull(Math.min(this.mTargetCurrentOffset - this.mTargetInitOffset, i3), i3, this.mTargetCurrentOffset - this.mTargetRefreshOffset);
            }
            onMoveTargetView(this.mTargetCurrentOffset);
            if (this.mListener != null) {
                this.mListener.onMoveTarget(this.mTargetCurrentOffset);
            }
            if (this.mRefreshOffsetCalculator == null) {
                this.mRefreshOffsetCalculator = new QMUIDefaultRefreshOffsetCalculator();
            }
            int calculateRefreshOffset = this.mRefreshOffsetCalculator.calculateRefreshOffset(this.mRefreshInitOffset, this.mRefreshEndOffset, this.mRefreshView.getHeight(), this.mTargetCurrentOffset, this.mTargetInitOffset, this.mTargetRefreshOffset);
            if (calculateRefreshOffset != this.mRefreshCurrentOffset) {
                ViewCompat.offsetTopAndBottom(this.mRefreshView, calculateRefreshOffset - this.mRefreshCurrentOffset);
                this.mRefreshCurrentOffset = calculateRefreshOffset;
                onMoveRefreshView(this.mRefreshCurrentOffset);
                if (this.mListener != null) {
                    this.mListener.onMoveRefreshView(this.mRefreshCurrentOffset);
                }
            }
        }
        return i2;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            this.mActivePointerId = motionEvent.getPointerId(actionIndex == 0 ? 1 : 0);
        }
    }

    private void releaseVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void removeFlag(int i) {
        this.mScrollFlag = (i ^ (-1)) & this.mScrollFlag;
    }

    public boolean canChildScrollUp() {
        return this.mChildScrollUpCallback != null ? this.mChildScrollUpCallback.canChildScrollUp(this, this.mTargetView) : defaultCanScrollUp(this.mTargetView);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int currY = this.mScroller.getCurrY();
            moveTargetViewTo(currY, false);
            if (currY <= 0 && hasFlag(8)) {
                deliverVelocity();
                this.mScroller.forceFinished(true);
            }
            invalidate();
            return;
        }
        if (hasFlag(1)) {
            removeFlag(1);
            if (this.mTargetCurrentOffset != this.mTargetInitOffset) {
                this.mScroller.startScroll(0, this.mTargetCurrentOffset, 0, this.mTargetInitOffset - this.mTargetCurrentOffset);
            }
            invalidate();
            return;
        }
        if (hasFlag(2)) {
            removeFlag(2);
            if (this.mTargetCurrentOffset != this.mTargetRefreshOffset) {
                this.mScroller.startScroll(0, this.mTargetCurrentOffset, 0, this.mTargetRefreshOffset - this.mTargetCurrentOffset);
            } else {
                moveTargetViewTo(this.mTargetRefreshOffset, false, true);
            }
            invalidate();
            return;
        }
        if (!hasFlag(4)) {
            deliverVelocity();
            return;
        }
        removeFlag(4);
        onRefresh();
        moveTargetViewTo(this.mTargetRefreshOffset, false, true);
    }

    protected View createRefreshView() {
        return new RefreshView(getContext());
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mNestScrollDurationRefreshing = this.mIsRefreshing;
        } else if (this.mNestScrollDurationRefreshing) {
            if (action != 2) {
                this.mNestScrollDurationRefreshing = false;
            } else if (!this.mIsRefreshing) {
                this.mNestScrollDurationRefreshing = false;
                motionEvent.setAction(0);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void finishRefresh() {
        this.mIsRefreshing = false;
        this.mIRefreshView.stop();
        this.mScrollFlag = 1;
        this.mScroller.forceFinished(true);
        invalidate();
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int i, int i2) {
        return this.mRefreshZIndex < 0 ? i2 : i2 == this.mRefreshZIndex ? i - 1 : i2 > this.mRefreshZIndex ? i2 - 1 : i2;
    }

    @Override // android.view.ViewGroup, android.support.v4.view.NestedScrollingParent
    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public int getRefreshEndOffset() {
        return this.mRefreshEndOffset;
    }

    public int getRefreshInitOffset() {
        return this.mRefreshInitOffset;
    }

    protected float getScrollerFriction() {
        return ViewConfiguration.getScrollFriction();
    }

    public int getTargetInitOffset() {
        return this.mTargetInitOffset;
    }

    public int getTargetRefreshOffset() {
        return this.mTargetRefreshOffset;
    }

    public View getTargetView() {
        return this.mTargetView;
    }

    protected boolean isYDrag(float f, float f2) {
        return Math.abs(f2) > Math.abs(f);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    protected void onFinishPull(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        ensureTargetView();
        int action = motionEvent.getAction();
        if (!isEnabled() || canChildScrollUp() || this.mNestedScrollInProgress) {
            return false;
        }
        if (action != 6) {
            switch (action) {
                case 0:
                    this.mIsDragging = false;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex >= 0) {
                        this.mInitialDownX = motionEvent.getX(findPointerIndex);
                        this.mInitialDownY = motionEvent.getY(findPointerIndex);
                        break;
                    } else {
                        return false;
                    }
                case 1:
                case 3:
                    this.mIsDragging = false;
                    this.mActivePointerId = -1;
                    break;
                case 2:
                    int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex2 >= 0) {
                        startDragging(motionEvent.getX(findPointerIndex2), motionEvent.getY(findPointerIndex2));
                        break;
                    } else {
                        Log.e(TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    }
            }
        } else {
            onSecondaryPointerUp(motionEvent);
        }
        return this.mIsDragging;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        ensureTargetView();
        if (this.mTargetView == null) {
            Log.d(TAG, "onLayout: mTargetView == null");
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        this.mTargetView.layout(paddingLeft, this.mTargetCurrentOffset + paddingTop, ((measuredWidth - getPaddingLeft()) - getPaddingRight()) + paddingLeft, paddingTop + ((measuredHeight - getPaddingTop()) - getPaddingBottom()) + this.mTargetCurrentOffset);
        int measuredWidth2 = this.mRefreshView.getMeasuredWidth();
        int i5 = measuredWidth / 2;
        int i6 = measuredWidth2 / 2;
        this.mRefreshView.layout(i5 - i6, this.mRefreshCurrentOffset, i5 + i6, this.mRefreshCurrentOffset + this.mRefreshView.getMeasuredHeight());
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        super.onMeasure(i, i2);
        ensureTargetView();
        if (this.mTargetView == null) {
            Log.d(TAG, "onMeasure: mTargetView == null");
            return;
        }
        this.mTargetView.measure(View.MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824), View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), 1073741824));
        measureChild(this.mRefreshView, i, i2);
        this.mRefreshZIndex = -1;
        int i4 = 0;
        while (true) {
            if (i4 >= getChildCount()) {
                break;
            }
            if (getChildAt(i4) == this.mRefreshView) {
                this.mRefreshZIndex = i4;
                break;
            }
            i4++;
        }
        int measuredHeight = this.mRefreshView.getMeasuredHeight();
        if (this.mAutoCalculateRefreshInitOffset && this.mRefreshInitOffset != (i3 = -measuredHeight)) {
            this.mRefreshInitOffset = i3;
            this.mRefreshCurrentOffset = this.mRefreshInitOffset;
        }
        if (this.mEqualTargetRefreshOffsetToRefreshViewHeight) {
            this.mTargetRefreshOffset = measuredHeight;
        }
        if (this.mAutoCalculateRefreshEndOffset) {
            this.mRefreshEndOffset = (this.mTargetRefreshOffset - measuredHeight) / 2;
        }
    }

    protected void onMoveRefreshView(int i) {
    }

    protected void onMoveTargetView(int i) {
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        try {
            return super.onNestedFling(view, f, f2, z);
        } catch (Throwable unused) {
            return false;
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public boolean onNestedPreFling(View view, float f, float f2) {
        info("onNestedPreFling: mTargetCurrentOffset = " + this.mTargetCurrentOffset + " ; velocityX = " + f + " ; velocityY = " + f2);
        if (this.mTargetCurrentOffset <= this.mTargetInitOffset) {
            return false;
        }
        this.mNestedScrollInProgress = false;
        finishPull((int) (-f2));
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        info("onNestedPreScroll: dx = " + i + " ; dy = " + i2);
        int i3 = this.mTargetCurrentOffset - this.mTargetInitOffset;
        if (i2 <= 0 || i3 <= 0) {
            return;
        }
        if (i2 >= i3) {
            iArr[1] = i3;
            moveTargetViewTo(this.mTargetInitOffset, true);
        } else {
            iArr[1] = i2;
            moveTargetView(-i2, true);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        info("onNestedScroll: dxConsumed = " + i + " ; dyConsumed = " + i2 + " ; dxUnconsumed = " + i3 + " ; dyUnconsumed = " + i4);
        if (i4 >= 0 || canChildScrollUp()) {
            return;
        }
        moveTargetView(-i4, true);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public void onNestedScrollAccepted(View view, View view2, int i) {
        info("onNestedScrollAccepted: axes = " + i);
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        this.mNestedScrollInProgress = true;
    }

    protected void onRefresh() {
        if (this.mIsRefreshing) {
            return;
        }
        this.mIsRefreshing = true;
        this.mIRefreshView.doRefresh();
        if (this.mListener != null) {
            this.mListener.onRefresh();
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public boolean onStartNestedScroll(View view, View view2, int i) {
        info("onStartNestedScroll: nestedScrollAxes = " + i);
        return isEnabled() && (i & 2) != 0;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, android.support.v4.view.NestedScrollingParent
    public void onStopNestedScroll(View view) {
        info("onStopNestedScroll: mNestedScrollInProgress = " + this.mNestedScrollInProgress);
        this.mNestedScrollingParentHelper.onStopNestedScroll(view);
        if (this.mNestedScrollInProgress) {
            this.mNestedScrollInProgress = false;
            finishPull(0);
        }
    }

    protected void onSureTargetView(View view) {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (!isEnabled() || canChildScrollUp() || this.mNestedScrollInProgress) {
            Log.d(TAG, "fast end onTouchEvent: isEnabled = " + isEnabled() + "; canChildScrollUp = " + canChildScrollUp() + " ; mNestedScrollInProgress = " + this.mNestedScrollInProgress);
            return false;
        }
        acquireVelocityTracker(motionEvent);
        switch (action) {
            case 0:
                this.mIsDragging = false;
                this.mScrollFlag = 0;
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mActivePointerId = motionEvent.getPointerId(0);
                return true;
            case 1:
                if (motionEvent.findPointerIndex(this.mActivePointerId) < 0) {
                    Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    return false;
                }
                if (this.mIsDragging) {
                    this.mIsDragging = false;
                    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
                    float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                    if (Math.abs(yVelocity) < this.mMiniVelocity) {
                        yVelocity = 0.0f;
                    }
                    finishPull((int) yVelocity);
                }
                this.mActivePointerId = -1;
                releaseVelocityTracker();
                return false;
            case 2:
                int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                if (findPointerIndex < 0) {
                    Log.e(TAG, "onTouchEvent Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }
                float x = motionEvent.getX(findPointerIndex);
                float y = motionEvent.getY(findPointerIndex);
                startDragging(x, y);
                if (this.mIsDragging) {
                    float f = (y - this.mLastMotionY) * this.mDragRate;
                    if (f >= 0.0f) {
                        moveTargetView(f, true);
                    } else {
                        float abs = Math.abs(f) - Math.abs(moveTargetView(f, true));
                        if (abs > 0.0f) {
                            motionEvent.setAction(0);
                            float f2 = this.mSystemTouchSlop + 1;
                            if (abs > f2) {
                                f2 = abs;
                            }
                            motionEvent.offsetLocation(0.0f, f2);
                            dispatchTouchEvent(motionEvent);
                            motionEvent.setAction(action);
                            motionEvent.offsetLocation(0.0f, -f2);
                            dispatchTouchEvent(motionEvent);
                        }
                    }
                    this.mLastMotionY = y;
                }
                return true;
            case 3:
                releaseVelocityTracker();
                return false;
            case 4:
            default:
                return true;
            case 5:
                int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                if (actionIndex < 0) {
                    Log.e(TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                return true;
            case 6:
                onSecondaryPointerUp(motionEvent);
                return true;
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (Build.VERSION.SDK_INT >= 21 || !(this.mTargetView instanceof AbsListView)) {
            if (this.mTargetView == null || ViewCompat.isNestedScrollingEnabled(this.mTargetView)) {
                super.requestDisallowInterceptTouchEvent(z);
            }
        }
    }

    public void reset() {
        moveTargetViewTo(this.mTargetInitOffset, false);
        this.mIRefreshView.stop();
        this.mIsRefreshing = false;
        this.mScroller.forceFinished(true);
        this.mScrollFlag = 0;
    }

    public void setAutoScrollToRefreshMinOffset(int i) {
        this.mAutoScrollToRefreshMinOffset = i;
    }

    public void setChildScrollUpCallback(OnChildScrollUpCallback onChildScrollUpCallback) {
        this.mChildScrollUpCallback = onChildScrollUpCallback;
    }

    public void setEnableOverPull(boolean z) {
        this.mEnableOverPull = z;
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        if (z) {
            return;
        }
        reset();
        invalidate();
    }

    public void setOnPullListener(OnPullListener onPullListener) {
        this.mListener = onPullListener;
    }

    public void setRefreshOffsetCalculator(RefreshOffsetCalculator refreshOffsetCalculator) {
        this.mRefreshOffsetCalculator = refreshOffsetCalculator;
    }

    public void setTargetRefreshOffset(int i) {
        this.mEqualTargetRefreshOffsetToRefreshViewHeight = false;
        this.mTargetRefreshOffset = i;
    }

    protected void startDragging(float f, float f2) {
        float f3 = f - this.mInitialDownX;
        float f4 = f2 - this.mInitialDownY;
        if (isYDrag(f3, f4)) {
            if ((f4 > this.mTouchSlop || (f4 < (-this.mTouchSlop) && this.mTargetCurrentOffset > this.mTargetInitOffset)) && !this.mIsDragging) {
                this.mInitialMotionY = this.mInitialDownY + this.mTouchSlop;
                this.mLastMotionY = this.mInitialMotionY;
                this.mIsDragging = true;
            }
        }
    }
}
