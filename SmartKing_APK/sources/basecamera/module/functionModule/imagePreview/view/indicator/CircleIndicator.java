package basecamera.module.functionModule.imagePreview.view.indicator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import basecamera.module.lib.R;

/* loaded from: classes.dex */
public class CircleIndicator extends LinearLayout {
    private static final int DEFAULT_INDICATOR_WIDTH = 5;
    private Animator mAnimatorIn;
    private Animator mAnimatorOut;
    private Animator mImmediateAnimatorIn;
    private Animator mImmediateAnimatorOut;
    private GradientDrawable mIndicatorBackground;
    private int mIndicatorHeight;
    private int mIndicatorMargin;
    private int mIndicatorWidth;
    private DataSetObserver mInternalDataSetObserver;
    private final ViewPager.OnPageChangeListener mInternalPageChangeListener;
    private int mLastPosition;
    private ViewPager mViewpager;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ReverseInterpolator implements Interpolator {
        private ReverseInterpolator() {
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            return Math.abs(1.0f - f);
        }
    }

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIndicatorMargin = -1;
        this.mIndicatorWidth = -1;
        this.mIndicatorHeight = -1;
        this.mLastPosition = -1;
        this.mInternalPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: basecamera.module.functionModule.imagePreview.view.indicator.CircleIndicator.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i3) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                View childAt;
                if (CircleIndicator.this.mViewpager.getAdapter() == null || CircleIndicator.this.mViewpager.getAdapter().getCount() <= 0) {
                    return;
                }
                if (CircleIndicator.this.mAnimatorIn.isRunning()) {
                    CircleIndicator.this.mAnimatorIn.end();
                    CircleIndicator.this.mAnimatorIn.cancel();
                }
                if (CircleIndicator.this.mAnimatorOut.isRunning()) {
                    CircleIndicator.this.mAnimatorOut.end();
                    CircleIndicator.this.mAnimatorOut.cancel();
                }
                if (CircleIndicator.this.mLastPosition >= 0 && (childAt = CircleIndicator.this.getChildAt(CircleIndicator.this.mLastPosition)) != null) {
                    childAt.setBackgroundDrawable(CircleIndicator.this.mIndicatorBackground);
                    CircleIndicator.this.mAnimatorIn.setTarget(childAt);
                    CircleIndicator.this.mAnimatorIn.start();
                }
                View childAt2 = CircleIndicator.this.getChildAt(i2);
                if (childAt2 != null) {
                    childAt2.setBackgroundDrawable(CircleIndicator.this.mIndicatorBackground);
                    CircleIndicator.this.mAnimatorOut.setTarget(childAt2);
                    CircleIndicator.this.mAnimatorOut.start();
                }
                CircleIndicator.this.mLastPosition = i2;
            }
        };
        this.mInternalDataSetObserver = new DataSetObserver() { // from class: basecamera.module.functionModule.imagePreview.view.indicator.CircleIndicator.2
            @Override // android.database.DataSetObserver
            public void onChanged() {
                int count;
                super.onChanged();
                if (CircleIndicator.this.mViewpager == null || (count = CircleIndicator.this.mViewpager.getAdapter().getCount()) == CircleIndicator.this.getChildCount()) {
                    return;
                }
                if (CircleIndicator.this.mLastPosition < count) {
                    CircleIndicator.this.mLastPosition = CircleIndicator.this.mViewpager.getCurrentItem();
                } else {
                    CircleIndicator.this.mLastPosition = -1;
                }
                CircleIndicator.this.createIndicators();
            }
        };
        init(context, attributeSet);
    }

    private void addIndicator(int i, Animator animator) {
        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }
        View view = new View(getContext());
        view.setBackgroundDrawable(this.mIndicatorBackground);
        addView(view, this.mIndicatorWidth, this.mIndicatorHeight);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (i == 0) {
            layoutParams.leftMargin = this.mIndicatorMargin;
            layoutParams.rightMargin = this.mIndicatorMargin;
        } else {
            layoutParams.topMargin = this.mIndicatorMargin;
            layoutParams.bottomMargin = this.mIndicatorMargin;
        }
        view.setLayoutParams(layoutParams);
        animator.setTarget(view);
        animator.start();
    }

    private void checkIndicatorConfig(Context context) {
        this.mIndicatorWidth = this.mIndicatorWidth < 0 ? dip2px(5.0f) : this.mIndicatorWidth;
        this.mIndicatorHeight = this.mIndicatorHeight < 0 ? dip2px(5.0f) : this.mIndicatorHeight;
        this.mIndicatorMargin = this.mIndicatorMargin < 0 ? dip2px(5.0f) : this.mIndicatorMargin;
        this.mAnimatorOut = createAnimatorOut();
        this.mImmediateAnimatorOut = createAnimatorOut();
        this.mImmediateAnimatorOut.setDuration(0L);
        this.mAnimatorIn = createAnimatorIn(context);
        this.mImmediateAnimatorIn = createAnimatorIn(context);
        this.mImmediateAnimatorIn.setDuration(0L);
    }

    private Animator createAnimatorIn(Context context) {
        Animator createAnimatorOut = createAnimatorOut();
        createAnimatorOut.setInterpolator(new ReverseInterpolator());
        return createAnimatorOut;
    }

    private Animator createAnimatorOut() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, "alpha", 0.5f, 1.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object) null, "scaleX", 1.0f, 1.8f);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object) null, "scaleY", 1.0f, 1.8f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofFloat).with(ofFloat2).with(ofFloat3);
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createIndicators() {
        removeAllViews();
        int count = this.mViewpager.getAdapter().getCount();
        if (count <= 0) {
            return;
        }
        int currentItem = this.mViewpager.getCurrentItem();
        int orientation = getOrientation();
        for (int i = 0; i < count; i++) {
            if (currentItem == i) {
                addIndicator(orientation, this.mImmediateAnimatorOut);
            } else {
                addIndicator(orientation, this.mImmediateAnimatorIn);
            }
        }
    }

    private void handleTypedArray(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CircleIndicator);
        this.mIndicatorWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircleIndicator_ci_width, -1);
        this.mIndicatorHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircleIndicator_ci_height, -1);
        this.mIndicatorMargin = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, -1);
        setOrientation(obtainStyledAttributes.getInt(R.styleable.CircleIndicator_ci_orientation, -1) != 1 ? 0 : 1);
        int i = obtainStyledAttributes.getInt(R.styleable.CircleIndicator_ci_gravity, -1);
        if (i < 0) {
            i = 17;
        }
        setGravity(i);
        obtainStyledAttributes.recycle();
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.mIndicatorBackground = new GradientDrawable();
        this.mIndicatorBackground.setShape(1);
        this.mIndicatorBackground.setColor(-1);
        handleTypedArray(context, attributeSet);
        checkIndicatorConfig(context);
    }

    public void configureIndicator(int i, int i2, int i3) {
        this.mIndicatorWidth = i;
        this.mIndicatorHeight = i2;
        this.mIndicatorMargin = i3;
        checkIndicatorConfig(getContext());
    }

    public int dip2px(float f) {
        return (int) ((f * getResources().getDisplayMetrics().density) + 0.5f);
    }

    public DataSetObserver getDataSetObserver() {
        return this.mInternalDataSetObserver;
    }

    @Deprecated
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        if (this.mViewpager == null) {
            throw new NullPointerException("can not find Viewpager , setViewPager first");
        }
        this.mViewpager.removeOnPageChangeListener(onPageChangeListener);
        this.mViewpager.addOnPageChangeListener(onPageChangeListener);
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewpager = viewPager;
        if (this.mViewpager == null || this.mViewpager.getAdapter() == null) {
            return;
        }
        this.mLastPosition = -1;
        createIndicators();
        this.mViewpager.removeOnPageChangeListener(this.mInternalPageChangeListener);
        this.mViewpager.addOnPageChangeListener(this.mInternalPageChangeListener);
        this.mInternalPageChangeListener.onPageSelected(this.mViewpager.getCurrentItem());
    }
}
