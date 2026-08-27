package com.yanzhenjie.recyclerview.swipe;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.TextView;
import com.yanzhenjie.recyclerview.swipe.SwipeHorizontal;

/* loaded from: classes2.dex */
public class SwipeMenuLayout extends FrameLayout implements SwipeSwitch {
    public static final int DEFAULT_SCROLLER_DURATION = 200;
    private View mContentView;
    private int mContentViewId;
    private int mDownX;
    private int mDownY;
    private boolean mDragging;
    private int mLastX;
    private int mLastY;
    private int mLeftViewId;
    private float mOpenPercent;
    private int mRightViewId;
    private int mScaledMaximumFlingVelocity;
    private int mScaledMinimumFlingVelocity;
    private int mScaledTouchSlop;
    private OverScroller mScroller;
    private int mScrollerDuration;
    private SwipeHorizontal mSwipeCurrentHorizontal;
    private SwipeLeftHorizontal mSwipeLeftHorizontal;
    private SwipeRightHorizontal mSwipeRightHorizontal;
    private VelocityTracker mVelocityTracker;
    private boolean shouldResetSwipe;
    private boolean swipeEnable;

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mLeftViewId = 0;
        this.mContentViewId = 0;
        this.mRightViewId = 0;
        this.mOpenPercent = 0.5f;
        this.mScrollerDuration = 200;
        this.swipeEnable = true;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.recycler_swipe_SwipeMenuLayout);
        this.mLeftViewId = obtainStyledAttributes.getResourceId(R.styleable.recycler_swipe_SwipeMenuLayout_leftViewId, this.mLeftViewId);
        this.mContentViewId = obtainStyledAttributes.getResourceId(R.styleable.recycler_swipe_SwipeMenuLayout_contentViewId, this.mContentViewId);
        this.mRightViewId = obtainStyledAttributes.getResourceId(R.styleable.recycler_swipe_SwipeMenuLayout_rightViewId, this.mRightViewId);
        obtainStyledAttributes.recycle();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mScaledMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mScaledMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mScroller = new OverScroller(getContext());
    }

    private int getSwipeDuration(MotionEvent motionEvent, int i) {
        int x = (int) (motionEvent.getX() - getScrollX());
        int menuWidth = this.mSwipeCurrentHorizontal.getMenuWidth();
        int i2 = menuWidth / 2;
        float f = menuWidth;
        float f2 = i2;
        return Math.min(i > 0 ? Math.round(Math.abs((f2 + (distanceInfluenceForSnapDuration(Math.min(1.0f, (Math.abs(x) * 1.0f) / f)) * f2)) / i) * 1000.0f) * 4 : (int) (((Math.abs(x) / f) + 1.0f) * 100.0f), this.mScrollerDuration);
    }

    private void judgeOpenClose(int i, int i2) {
        if (this.mSwipeCurrentHorizontal != null) {
            if (Math.abs(getScrollX()) < this.mSwipeCurrentHorizontal.getMenuView().getWidth() * this.mOpenPercent) {
                smoothCloseMenu();
                return;
            }
            if (Math.abs(i) > this.mScaledTouchSlop || Math.abs(i2) > this.mScaledTouchSlop) {
                if (isMenuOpenNotEqual()) {
                    smoothCloseMenu();
                    return;
                } else {
                    smoothOpenMenu();
                    return;
                }
            }
            if (isMenuOpen()) {
                smoothCloseMenu();
            } else {
                smoothOpenMenu();
            }
        }
    }

    private void smoothOpenMenu(int i) {
        if (this.mSwipeCurrentHorizontal != null) {
            this.mSwipeCurrentHorizontal.autoOpenMenu(this.mScroller, getScrollX(), i);
            invalidate();
        }
    }

    @Override // android.view.View
    public void computeScroll() {
        if (!this.mScroller.computeScrollOffset() || this.mSwipeCurrentHorizontal == null) {
            return;
        }
        if (this.mSwipeCurrentHorizontal instanceof SwipeRightHorizontal) {
            scrollTo(Math.abs(this.mScroller.getCurrX()), 0);
            invalidate();
        } else {
            scrollTo(-Math.abs(this.mScroller.getCurrX()), 0);
            invalidate();
        }
    }

    float distanceInfluenceForSnapDuration(float f) {
        Double.isNaN(f - 0.5f);
        return (float) Math.sin((float) (r0 * 0.4712389167638204d));
    }

    public float getOpenPercent() {
        return this.mOpenPercent;
    }

    public boolean hasLeftMenu() {
        return this.mSwipeLeftHorizontal != null && this.mSwipeLeftHorizontal.canSwipe();
    }

    public boolean hasRightMenu() {
        return this.mSwipeRightHorizontal != null && this.mSwipeRightHorizontal.canSwipe();
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public boolean isCompleteOpen() {
        return isLeftCompleteOpen() || isRightMenuOpen();
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public boolean isLeftCompleteOpen() {
        return (this.mSwipeLeftHorizontal == null || this.mSwipeLeftHorizontal.isCompleteClose(getScrollX())) ? false : true;
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public boolean isLeftMenuOpen() {
        return this.mSwipeLeftHorizontal != null && this.mSwipeLeftHorizontal.isMenuOpen(getScrollX());
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public boolean isLeftMenuOpenNotEqual() {
        return this.mSwipeLeftHorizontal != null && this.mSwipeLeftHorizontal.isMenuOpenNotEqual(getScrollX());
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public boolean isMenuOpen() {
        return isLeftMenuOpen() || isRightMenuOpen();
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public boolean isMenuOpenNotEqual() {
        return isLeftMenuOpenNotEqual() || isRightMenuOpenNotEqual();
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public boolean isRightCompleteOpen() {
        return (this.mSwipeRightHorizontal == null || this.mSwipeRightHorizontal.isCompleteClose(getScrollX())) ? false : true;
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public boolean isRightMenuOpen() {
        return this.mSwipeRightHorizontal != null && this.mSwipeRightHorizontal.isMenuOpen(getScrollX());
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public boolean isRightMenuOpenNotEqual() {
        return this.mSwipeRightHorizontal != null && this.mSwipeRightHorizontal.isMenuOpenNotEqual(getScrollX());
    }

    public boolean isSwipeEnable() {
        return this.swipeEnable;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (this.mLeftViewId != 0 && this.mSwipeLeftHorizontal == null) {
            this.mSwipeLeftHorizontal = new SwipeLeftHorizontal(findViewById(this.mLeftViewId));
        }
        if (this.mRightViewId != 0 && this.mSwipeRightHorizontal == null) {
            this.mSwipeRightHorizontal = new SwipeRightHorizontal(findViewById(this.mRightViewId));
        }
        if (this.mContentViewId != 0 && this.mContentView == null) {
            this.mContentView = findViewById(this.mContentViewId);
            return;
        }
        TextView textView = new TextView(getContext());
        textView.setClickable(true);
        textView.setGravity(17);
        textView.setTextSize(16.0f);
        textView.setText("You may not have set the ContentView.");
        this.mContentView = textView;
        addView(this.mContentView);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        switch (motionEvent.getAction()) {
            case 0:
                int x = (int) motionEvent.getX();
                this.mLastX = x;
                this.mDownX = x;
                this.mDownY = (int) motionEvent.getY();
                return false;
            case 1:
                boolean z = this.mSwipeCurrentHorizontal != null && this.mSwipeCurrentHorizontal.isClickOnContentView(getWidth(), motionEvent.getX());
                if (!isMenuOpen() || !z) {
                    return false;
                }
                smoothCloseMenu();
                return true;
            case 2:
                int x2 = (int) (motionEvent.getX() - this.mDownX);
                return Math.abs(x2) > this.mScaledTouchSlop && Math.abs(x2) > Math.abs((int) (motionEvent.getY() - ((float) this.mDownY)));
            case 3:
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                return false;
            default:
                return onInterceptTouchEvent;
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mContentView != null) {
            int measuredWidthAndState = this.mContentView.getMeasuredWidthAndState();
            int measuredHeightAndState = this.mContentView.getMeasuredHeightAndState();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mContentView.getLayoutParams();
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop() + layoutParams.topMargin;
            this.mContentView.layout(paddingLeft, paddingTop, measuredWidthAndState + paddingLeft, measuredHeightAndState + paddingTop);
        }
        if (this.mSwipeLeftHorizontal != null) {
            View menuView = this.mSwipeLeftHorizontal.getMenuView();
            int measuredWidthAndState2 = menuView.getMeasuredWidthAndState();
            int measuredHeightAndState2 = menuView.getMeasuredHeightAndState();
            int paddingTop2 = getPaddingTop() + ((FrameLayout.LayoutParams) menuView.getLayoutParams()).topMargin;
            menuView.layout(-measuredWidthAndState2, paddingTop2, 0, measuredHeightAndState2 + paddingTop2);
        }
        if (this.mSwipeRightHorizontal != null) {
            View menuView2 = this.mSwipeRightHorizontal.getMenuView();
            int measuredWidthAndState3 = menuView2.getMeasuredWidthAndState();
            int measuredHeightAndState3 = menuView2.getMeasuredHeightAndState();
            int paddingTop3 = getPaddingTop() + ((FrameLayout.LayoutParams) menuView2.getLayoutParams()).topMargin;
            int measuredWidthAndState4 = getMeasuredWidthAndState();
            menuView2.layout(measuredWidthAndState4, paddingTop3, measuredWidthAndState3 + measuredWidthAndState4, measuredHeightAndState3 + paddingTop3);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        super.onMeasure(i, i2);
        if (this.mContentView != null) {
            measureChildWithMargins(this.mContentView, i, 0, i2, 0);
            i3 = this.mContentView.getMeasuredHeight();
        } else {
            i3 = 0;
        }
        if (this.mSwipeLeftHorizontal != null) {
            View menuView = this.mSwipeLeftHorizontal.getMenuView();
            menuView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i3 == 0 ? menuView.getMeasuredHeightAndState() : i3, 1073741824));
        }
        if (this.mSwipeRightHorizontal != null) {
            View menuView2 = this.mSwipeRightHorizontal.getMenuView();
            menuView2.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i3 == 0 ? menuView2.getMeasuredHeightAndState() : i3, 1073741824));
        }
        if (i3 > 0) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), i3);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (motionEvent.getAction()) {
            case 0:
                this.mLastX = (int) motionEvent.getX();
                this.mLastY = (int) motionEvent.getY();
                break;
            case 1:
                int x = (int) (this.mDownX - motionEvent.getX());
                int y = (int) (this.mDownY - motionEvent.getY());
                this.mDragging = false;
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mScaledMaximumFlingVelocity);
                int xVelocity = (int) this.mVelocityTracker.getXVelocity();
                int abs = Math.abs(xVelocity);
                if (abs <= this.mScaledMinimumFlingVelocity) {
                    judgeOpenClose(x, y);
                } else if (this.mSwipeCurrentHorizontal != null) {
                    int swipeDuration = getSwipeDuration(motionEvent, abs);
                    if (this.mSwipeCurrentHorizontal instanceof SwipeRightHorizontal) {
                        if (xVelocity < 0) {
                            smoothOpenMenu(swipeDuration);
                        } else {
                            smoothCloseMenu(swipeDuration);
                        }
                    } else if (xVelocity > 0) {
                        smoothOpenMenu(swipeDuration);
                    } else {
                        smoothCloseMenu(swipeDuration);
                    }
                    ViewCompat.postInvalidateOnAnimation(this);
                }
                this.mVelocityTracker.clear();
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                if (Math.abs(this.mDownX - motionEvent.getX()) > this.mScaledTouchSlop || Math.abs(this.mDownY - motionEvent.getY()) > this.mScaledTouchSlop || isLeftMenuOpen() || isRightMenuOpen()) {
                    motionEvent.setAction(3);
                    super.onTouchEvent(motionEvent);
                    return true;
                }
                break;
            case 2:
                if (isSwipeEnable()) {
                    int x2 = (int) (this.mLastX - motionEvent.getX());
                    int y2 = (int) (this.mLastY - motionEvent.getY());
                    if (!this.mDragging && Math.abs(x2) > this.mScaledTouchSlop && Math.abs(x2) > Math.abs(y2)) {
                        this.mDragging = true;
                    }
                    if (this.mDragging) {
                        if (this.mSwipeCurrentHorizontal == null || this.shouldResetSwipe) {
                            if (x2 < 0) {
                                if (this.mSwipeLeftHorizontal != null) {
                                    this.mSwipeCurrentHorizontal = this.mSwipeLeftHorizontal;
                                } else {
                                    this.mSwipeCurrentHorizontal = this.mSwipeRightHorizontal;
                                }
                            } else if (this.mSwipeRightHorizontal != null) {
                                this.mSwipeCurrentHorizontal = this.mSwipeRightHorizontal;
                            } else {
                                this.mSwipeCurrentHorizontal = this.mSwipeLeftHorizontal;
                            }
                        }
                        scrollBy(x2, 0);
                        this.mLastX = (int) motionEvent.getX();
                        this.mLastY = (int) motionEvent.getY();
                        this.shouldResetSwipe = false;
                        break;
                    }
                }
                break;
            case 3:
                this.mDragging = false;
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                    break;
                } else {
                    judgeOpenClose((int) (this.mDownX - motionEvent.getX()), (int) (this.mDownY - motionEvent.getY()));
                    break;
                }
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public void scrollTo(int i, int i2) {
        if (this.mSwipeCurrentHorizontal == null) {
            super.scrollTo(i, i2);
            return;
        }
        SwipeHorizontal.Checker checkXY = this.mSwipeCurrentHorizontal.checkXY(i, i2);
        this.shouldResetSwipe = checkXY.shouldResetSwipe;
        if (checkXY.x != getScrollX()) {
            super.scrollTo(checkXY.x, checkXY.y);
        }
    }

    public void setOpenPercent(float f) {
        this.mOpenPercent = f;
    }

    public void setScrollerDuration(int i) {
        this.mScrollerDuration = i;
    }

    public void setSwipeEnable(boolean z) {
        this.swipeEnable = z;
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public void smoothCloseLeftMenu() {
        if (this.mSwipeLeftHorizontal != null) {
            this.mSwipeCurrentHorizontal = this.mSwipeLeftHorizontal;
            smoothCloseMenu();
        }
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public void smoothCloseMenu() {
        smoothCloseMenu(this.mScrollerDuration);
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public void smoothCloseMenu(int i) {
        if (this.mSwipeCurrentHorizontal != null) {
            this.mSwipeCurrentHorizontal.autoCloseMenu(this.mScroller, getScrollX(), i);
            invalidate();
        }
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public void smoothCloseRightMenu() {
        if (this.mSwipeRightHorizontal != null) {
            this.mSwipeCurrentHorizontal = this.mSwipeRightHorizontal;
            smoothCloseMenu();
        }
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public void smoothOpenLeftMenu() {
        smoothOpenLeftMenu(this.mScrollerDuration);
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public void smoothOpenLeftMenu(int i) {
        if (this.mSwipeLeftHorizontal != null) {
            this.mSwipeCurrentHorizontal = this.mSwipeLeftHorizontal;
            smoothOpenMenu(i);
        }
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public void smoothOpenMenu() {
        smoothOpenMenu(this.mScrollerDuration);
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public void smoothOpenRightMenu() {
        smoothOpenRightMenu(this.mScrollerDuration);
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeSwitch
    public void smoothOpenRightMenu(int i) {
        if (this.mSwipeRightHorizontal != null) {
            this.mSwipeCurrentHorizontal = this.mSwipeRightHorizontal;
            smoothOpenMenu(i);
        }
    }
}
