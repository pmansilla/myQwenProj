package com.flyco.tablayout;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.FragmentChangeManager;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CommonTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    private static final int STYLE_BLOCK = 2;
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private Context mContext;
    private IndicatorPoint mCurrentP;
    private int mCurrentTab;
    private int mDividerColor;
    private float mDividerPadding;
    private Paint mDividerPaint;
    private float mDividerWidth;
    private FragmentChangeManager mFragmentChangeManager;
    private int mHeight;
    private int mIconGravity;
    private float mIconHeight;
    private float mIconMargin;
    private boolean mIconVisible;
    private float mIconWidth;
    private long mIndicatorAnimDuration;
    private boolean mIndicatorAnimEnable;
    private boolean mIndicatorBounceEnable;
    private int mIndicatorColor;
    private float mIndicatorCornerRadius;
    private GradientDrawable mIndicatorDrawable;
    private int mIndicatorGravity;
    private float mIndicatorHeight;
    private float mIndicatorMarginBottom;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginTop;
    private Rect mIndicatorRect;
    private int mIndicatorStyle;
    private float mIndicatorWidth;
    private SparseArray<Boolean> mInitSetMap;
    private OvershootInterpolator mInterpolator;
    private boolean mIsFirstDraw;
    private IndicatorPoint mLastP;
    private int mLastTab;
    private OnTabSelectListener mListener;
    private Paint mRectPaint;
    private int mTabCount;
    private ArrayList<CustomTabEntity> mTabEntitys;
    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;
    private LinearLayout mTabsContainer;
    private boolean mTextAllCaps;
    private int mTextBold;
    private Paint mTextPaint;
    private int mTextSelectColor;
    private int mTextUnselectColor;
    private float mTextsize;
    private Paint mTrianglePaint;
    private Path mTrianglePath;
    private int mUnderlineColor;
    private int mUnderlineGravity;
    private float mUnderlineHeight;
    private ValueAnimator mValueAnimator;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class IndicatorPoint {
        public float left;
        public float right;

        IndicatorPoint() {
        }
    }

    /* loaded from: classes.dex */
    class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
        PointEvaluator() {
        }

        @Override // android.animation.TypeEvaluator
        public IndicatorPoint evaluate(float f, IndicatorPoint indicatorPoint, IndicatorPoint indicatorPoint2) {
            float f2 = indicatorPoint.left + ((indicatorPoint2.left - indicatorPoint.left) * f);
            float f3 = indicatorPoint.right + (f * (indicatorPoint2.right - indicatorPoint.right));
            IndicatorPoint indicatorPoint3 = new IndicatorPoint();
            indicatorPoint3.left = f2;
            indicatorPoint3.right = f3;
            return indicatorPoint3;
        }
    }

    public CommonTabLayout(Context context) {
        this(context, null, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTabEntitys = new ArrayList<>();
        this.mIndicatorRect = new Rect();
        this.mIndicatorDrawable = new GradientDrawable();
        this.mRectPaint = new Paint(1);
        this.mDividerPaint = new Paint(1);
        this.mTrianglePaint = new Paint(1);
        this.mTrianglePath = new Path();
        this.mIndicatorStyle = 0;
        this.mInterpolator = new OvershootInterpolator(1.5f);
        this.mIsFirstDraw = true;
        this.mTextPaint = new Paint(1);
        this.mInitSetMap = new SparseArray<>();
        this.mCurrentP = new IndicatorPoint();
        this.mLastP = new IndicatorPoint();
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);
        this.mContext = context;
        this.mTabsContainer = new LinearLayout(context);
        addView(this.mTabsContainer);
        obtainAttributes(context, attributeSet);
        String attributeValue = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        if (!attributeValue.equals("-1") && !attributeValue.equals("-2")) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{android.R.attr.layout_height});
            this.mHeight = obtainStyledAttributes.getDimensionPixelSize(0, -2);
            obtainStyledAttributes.recycle();
        }
        this.mValueAnimator = ValueAnimator.ofObject(new PointEvaluator(), this.mLastP, this.mCurrentP);
        this.mValueAnimator.addUpdateListener(this);
    }

    private void addTab(int i, View view) {
        ((TextView) view.findViewById(R.id.tv_tab_title)).setText(this.mTabEntitys.get(i).getTabTitle());
        ((ImageView) view.findViewById(R.id.iv_tab_icon)).setImageResource(this.mTabEntitys.get(i).getTabUnselectedIcon());
        view.setOnClickListener(new View.OnClickListener() { // from class: com.flyco.tablayout.CommonTabLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                int intValue = ((Integer) view2.getTag()).intValue();
                if (CommonTabLayout.this.mCurrentTab == intValue) {
                    if (CommonTabLayout.this.mListener != null) {
                        CommonTabLayout.this.mListener.onTabReselect(intValue);
                    }
                } else {
                    CommonTabLayout.this.setCurrentTab(intValue);
                    if (CommonTabLayout.this.mListener != null) {
                        CommonTabLayout.this.mListener.onTabSelect(intValue);
                    }
                }
            }
        });
        LinearLayout.LayoutParams layoutParams = this.mTabSpaceEqual ? new LinearLayout.LayoutParams(0, -1, 1.0f) : new LinearLayout.LayoutParams(-2, -1);
        if (this.mTabWidth > 0.0f) {
            layoutParams = new LinearLayout.LayoutParams((int) this.mTabWidth, -1);
        }
        this.mTabsContainer.addView(view, i, layoutParams);
    }

    private void calcIndicatorRect() {
        View childAt = this.mTabsContainer.getChildAt(this.mCurrentTab);
        float left = childAt.getLeft();
        float right = childAt.getRight();
        this.mIndicatorRect.left = (int) left;
        this.mIndicatorRect.right = (int) right;
        if (this.mIndicatorWidth < 0.0f) {
            return;
        }
        this.mIndicatorRect.left = (int) (childAt.getLeft() + ((childAt.getWidth() - this.mIndicatorWidth) / 2.0f));
        this.mIndicatorRect.right = (int) (this.mIndicatorRect.left + this.mIndicatorWidth);
    }

    private void calcOffset() {
        View childAt = this.mTabsContainer.getChildAt(this.mCurrentTab);
        this.mCurrentP.left = childAt.getLeft();
        this.mCurrentP.right = childAt.getRight();
        View childAt2 = this.mTabsContainer.getChildAt(this.mLastTab);
        this.mLastP.left = childAt2.getLeft();
        this.mLastP.right = childAt2.getRight();
        if (this.mLastP.left == this.mCurrentP.left && this.mLastP.right == this.mCurrentP.right) {
            invalidate();
            return;
        }
        this.mValueAnimator.setObjectValues(this.mLastP, this.mCurrentP);
        if (this.mIndicatorBounceEnable) {
            this.mValueAnimator.setInterpolator(this.mInterpolator);
        }
        if (this.mIndicatorAnimDuration < 0) {
            this.mIndicatorAnimDuration = this.mIndicatorBounceEnable ? 500L : 250L;
        }
        this.mValueAnimator.setDuration(this.mIndicatorAnimDuration);
        this.mValueAnimator.start();
    }

    private void obtainAttributes(Context context, AttributeSet attributeSet) {
        float f;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CommonTabLayout);
        this.mIndicatorStyle = obtainStyledAttributes.getInt(R.styleable.CommonTabLayout_tl_indicator_style, 0);
        this.mIndicatorColor = obtainStyledAttributes.getColor(R.styleable.CommonTabLayout_tl_indicator_color, Color.parseColor(this.mIndicatorStyle == 2 ? "#4B6A87" : "#ffffff"));
        int i = R.styleable.CommonTabLayout_tl_indicator_height;
        if (this.mIndicatorStyle == 1) {
            f = 4.0f;
        } else {
            f = this.mIndicatorStyle == 2 ? -1 : 2;
        }
        this.mIndicatorHeight = obtainStyledAttributes.getDimension(i, dp2px(f));
        this.mIndicatorWidth = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_indicator_width, dp2px(this.mIndicatorStyle == 1 ? 10.0f : -1.0f));
        this.mIndicatorCornerRadius = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_indicator_corner_radius, dp2px(this.mIndicatorStyle == 2 ? -1.0f : 0.0f));
        this.mIndicatorMarginLeft = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_left, dp2px(0.0f));
        this.mIndicatorMarginTop = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_top, dp2px(this.mIndicatorStyle == 2 ? 7.0f : 0.0f));
        this.mIndicatorMarginRight = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_right, dp2px(0.0f));
        this.mIndicatorMarginBottom = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_indicator_margin_bottom, dp2px(this.mIndicatorStyle != 2 ? 0.0f : 7.0f));
        this.mIndicatorAnimEnable = obtainStyledAttributes.getBoolean(R.styleable.CommonTabLayout_tl_indicator_anim_enable, true);
        this.mIndicatorBounceEnable = obtainStyledAttributes.getBoolean(R.styleable.CommonTabLayout_tl_indicator_bounce_enable, true);
        this.mIndicatorAnimDuration = obtainStyledAttributes.getInt(R.styleable.CommonTabLayout_tl_indicator_anim_duration, -1);
        this.mIndicatorGravity = obtainStyledAttributes.getInt(R.styleable.CommonTabLayout_tl_indicator_gravity, 80);
        this.mUnderlineColor = obtainStyledAttributes.getColor(R.styleable.CommonTabLayout_tl_underline_color, Color.parseColor("#ffffff"));
        this.mUnderlineHeight = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_underline_height, dp2px(0.0f));
        this.mUnderlineGravity = obtainStyledAttributes.getInt(R.styleable.CommonTabLayout_tl_underline_gravity, 80);
        this.mDividerColor = obtainStyledAttributes.getColor(R.styleable.CommonTabLayout_tl_divider_color, Color.parseColor("#ffffff"));
        this.mDividerWidth = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_divider_width, dp2px(0.0f));
        this.mDividerPadding = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_divider_padding, dp2px(12.0f));
        this.mTextsize = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_textsize, sp2px(13.0f));
        this.mTextSelectColor = obtainStyledAttributes.getColor(R.styleable.CommonTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"));
        this.mTextUnselectColor = obtainStyledAttributes.getColor(R.styleable.CommonTabLayout_tl_textUnselectColor, Color.parseColor("#AAffffff"));
        this.mTextBold = obtainStyledAttributes.getInt(R.styleable.CommonTabLayout_tl_textBold, 0);
        this.mTextAllCaps = obtainStyledAttributes.getBoolean(R.styleable.CommonTabLayout_tl_textAllCaps, false);
        this.mIconVisible = obtainStyledAttributes.getBoolean(R.styleable.CommonTabLayout_tl_iconVisible, true);
        this.mIconGravity = obtainStyledAttributes.getInt(R.styleable.CommonTabLayout_tl_iconGravity, 48);
        this.mIconWidth = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_iconWidth, dp2px(0.0f));
        this.mIconHeight = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_iconHeight, dp2px(0.0f));
        this.mIconMargin = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_iconMargin, dp2px(2.5f));
        this.mTabSpaceEqual = obtainStyledAttributes.getBoolean(R.styleable.CommonTabLayout_tl_tab_space_equal, true);
        this.mTabWidth = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_tab_width, dp2px(-1.0f));
        this.mTabPadding = obtainStyledAttributes.getDimension(R.styleable.CommonTabLayout_tl_tab_padding, (this.mTabSpaceEqual || this.mTabWidth > 0.0f) ? dp2px(0.0f) : dp2px(10.0f));
        obtainStyledAttributes.recycle();
    }

    private void updateTabSelection(int i) {
        int i2 = 0;
        while (i2 < this.mTabCount) {
            View childAt = this.mTabsContainer.getChildAt(i2);
            boolean z = i2 == i;
            TextView textView = (TextView) childAt.findViewById(R.id.tv_tab_title);
            textView.setTextColor(z ? this.mTextSelectColor : this.mTextUnselectColor);
            ImageView imageView = (ImageView) childAt.findViewById(R.id.iv_tab_icon);
            CustomTabEntity customTabEntity = this.mTabEntitys.get(i2);
            imageView.setImageResource(z ? customTabEntity.getTabSelectedIcon() : customTabEntity.getTabUnselectedIcon());
            if (this.mTextBold == 1) {
                textView.getPaint().setFakeBoldText(z);
            }
            i2++;
        }
    }

    private void updateTabStyles() {
        int i = 0;
        while (i < this.mTabCount) {
            View childAt = this.mTabsContainer.getChildAt(i);
            childAt.setPadding((int) this.mTabPadding, 0, (int) this.mTabPadding, 0);
            TextView textView = (TextView) childAt.findViewById(R.id.tv_tab_title);
            textView.setTextColor(i == this.mCurrentTab ? this.mTextSelectColor : this.mTextUnselectColor);
            textView.setTextSize(0, this.mTextsize);
            if (this.mTextAllCaps) {
                textView.setText(textView.getText().toString().toUpperCase());
            }
            if (this.mTextBold == 2) {
                textView.getPaint().setFakeBoldText(true);
            } else if (this.mTextBold == 0) {
                textView.getPaint().setFakeBoldText(false);
            }
            ImageView imageView = (ImageView) childAt.findViewById(R.id.iv_tab_icon);
            if (this.mIconVisible) {
                imageView.setVisibility(0);
                CustomTabEntity customTabEntity = this.mTabEntitys.get(i);
                imageView.setImageResource(i == this.mCurrentTab ? customTabEntity.getTabSelectedIcon() : customTabEntity.getTabUnselectedIcon());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this.mIconWidth <= 0.0f ? -2 : (int) this.mIconWidth, this.mIconHeight > 0.0f ? (int) this.mIconHeight : -2);
                if (this.mIconGravity == 3) {
                    layoutParams.rightMargin = (int) this.mIconMargin;
                } else if (this.mIconGravity == 5) {
                    layoutParams.leftMargin = (int) this.mIconMargin;
                } else if (this.mIconGravity == 80) {
                    layoutParams.topMargin = (int) this.mIconMargin;
                } else {
                    layoutParams.bottomMargin = (int) this.mIconMargin;
                }
                imageView.setLayoutParams(layoutParams);
            } else {
                imageView.setVisibility(8);
            }
            i++;
        }
    }

    protected int dp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int getCurrentTab() {
        return this.mCurrentTab;
    }

    public int getDividerColor() {
        return this.mDividerColor;
    }

    public float getDividerPadding() {
        return this.mDividerPadding;
    }

    public float getDividerWidth() {
        return this.mDividerWidth;
    }

    public int getIconGravity() {
        return this.mIconGravity;
    }

    public float getIconHeight() {
        return this.mIconHeight;
    }

    public float getIconMargin() {
        return this.mIconMargin;
    }

    public ImageView getIconView(int i) {
        return (ImageView) this.mTabsContainer.getChildAt(i).findViewById(R.id.iv_tab_icon);
    }

    public float getIconWidth() {
        return this.mIconWidth;
    }

    public long getIndicatorAnimDuration() {
        return this.mIndicatorAnimDuration;
    }

    public int getIndicatorColor() {
        return this.mIndicatorColor;
    }

    public float getIndicatorCornerRadius() {
        return this.mIndicatorCornerRadius;
    }

    public float getIndicatorHeight() {
        return this.mIndicatorHeight;
    }

    public float getIndicatorMarginBottom() {
        return this.mIndicatorMarginBottom;
    }

    public float getIndicatorMarginLeft() {
        return this.mIndicatorMarginLeft;
    }

    public float getIndicatorMarginRight() {
        return this.mIndicatorMarginRight;
    }

    public float getIndicatorMarginTop() {
        return this.mIndicatorMarginTop;
    }

    public int getIndicatorStyle() {
        return this.mIndicatorStyle;
    }

    public float getIndicatorWidth() {
        return this.mIndicatorWidth;
    }

    public MsgView getMsgView(int i) {
        if (i >= this.mTabCount) {
            i = this.mTabCount - 1;
        }
        return (MsgView) this.mTabsContainer.getChildAt(i).findViewById(R.id.rtv_msg_tip);
    }

    public int getTabCount() {
        return this.mTabCount;
    }

    public float getTabPadding() {
        return this.mTabPadding;
    }

    public float getTabWidth() {
        return this.mTabWidth;
    }

    public int getTextBold() {
        return this.mTextBold;
    }

    public int getTextSelectColor() {
        return this.mTextSelectColor;
    }

    public int getTextUnselectColor() {
        return this.mTextUnselectColor;
    }

    public float getTextsize() {
        return this.mTextsize;
    }

    public TextView getTitleView(int i) {
        return (TextView) this.mTabsContainer.getChildAt(i).findViewById(R.id.tv_tab_title);
    }

    public int getUnderlineColor() {
        return this.mUnderlineColor;
    }

    public float getUnderlineHeight() {
        return this.mUnderlineHeight;
    }

    public void hideMsg(int i) {
        if (i >= this.mTabCount) {
            i = this.mTabCount - 1;
        }
        MsgView msgView = (MsgView) this.mTabsContainer.getChildAt(i).findViewById(R.id.rtv_msg_tip);
        if (msgView != null) {
            msgView.setVisibility(8);
        }
    }

    public boolean isIconVisible() {
        return this.mIconVisible;
    }

    public boolean isIndicatorAnimEnable() {
        return this.mIndicatorAnimEnable;
    }

    public boolean isIndicatorBounceEnable() {
        return this.mIndicatorBounceEnable;
    }

    public boolean isTabSpaceEqual() {
        return this.mTabSpaceEqual;
    }

    public boolean isTextAllCaps() {
        return this.mTextAllCaps;
    }

    public void notifyDataSetChanged() {
        this.mTabsContainer.removeAllViews();
        this.mTabCount = this.mTabEntitys.size();
        for (int i = 0; i < this.mTabCount; i++) {
            View inflate = this.mIconGravity == 3 ? View.inflate(this.mContext, R.layout.layout_tab_left, null) : this.mIconGravity == 5 ? View.inflate(this.mContext, R.layout.layout_tab_right, null) : this.mIconGravity == 80 ? View.inflate(this.mContext, R.layout.layout_tab_bottom, null) : View.inflate(this.mContext, R.layout.layout_tab_top, null);
            inflate.setTag(Integer.valueOf(i));
            addTab(i, inflate);
        }
        updateTabStyles();
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        View childAt = this.mTabsContainer.getChildAt(this.mCurrentTab);
        IndicatorPoint indicatorPoint = (IndicatorPoint) valueAnimator.getAnimatedValue();
        this.mIndicatorRect.left = (int) indicatorPoint.left;
        this.mIndicatorRect.right = (int) indicatorPoint.right;
        if (this.mIndicatorWidth >= 0.0f) {
            this.mIndicatorRect.left = (int) (indicatorPoint.left + ((childAt.getWidth() - this.mIndicatorWidth) / 2.0f));
            this.mIndicatorRect.right = (int) (this.mIndicatorRect.left + this.mIndicatorWidth);
        }
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || this.mTabCount <= 0) {
            return;
        }
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        if (this.mDividerWidth > 0.0f) {
            this.mDividerPaint.setStrokeWidth(this.mDividerWidth);
            this.mDividerPaint.setColor(this.mDividerColor);
            for (int i = 0; i < this.mTabCount - 1; i++) {
                View childAt = this.mTabsContainer.getChildAt(i);
                canvas.drawLine(childAt.getRight() + paddingLeft, this.mDividerPadding, childAt.getRight() + paddingLeft, height - this.mDividerPadding, this.mDividerPaint);
            }
        }
        if (this.mUnderlineHeight > 0.0f) {
            this.mRectPaint.setColor(this.mUnderlineColor);
            if (this.mUnderlineGravity == 80) {
                float f = height;
                canvas.drawRect(paddingLeft, f - this.mUnderlineHeight, this.mTabsContainer.getWidth() + paddingLeft, f, this.mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0.0f, this.mTabsContainer.getWidth() + paddingLeft, this.mUnderlineHeight, this.mRectPaint);
            }
        }
        if (!this.mIndicatorAnimEnable) {
            calcIndicatorRect();
        } else if (this.mIsFirstDraw) {
            this.mIsFirstDraw = false;
            calcIndicatorRect();
        }
        if (this.mIndicatorStyle == 1) {
            if (this.mIndicatorHeight > 0.0f) {
                this.mTrianglePaint.setColor(this.mIndicatorColor);
                this.mTrianglePath.reset();
                float f2 = height;
                this.mTrianglePath.moveTo(this.mIndicatorRect.left + paddingLeft, f2);
                this.mTrianglePath.lineTo((this.mIndicatorRect.left / 2) + paddingLeft + (this.mIndicatorRect.right / 2), f2 - this.mIndicatorHeight);
                this.mTrianglePath.lineTo(paddingLeft + this.mIndicatorRect.right, f2);
                this.mTrianglePath.close();
                canvas.drawPath(this.mTrianglePath, this.mTrianglePaint);
                return;
            }
            return;
        }
        if (this.mIndicatorStyle != 2) {
            if (this.mIndicatorHeight > 0.0f) {
                this.mIndicatorDrawable.setColor(this.mIndicatorColor);
                if (this.mIndicatorGravity == 80) {
                    this.mIndicatorDrawable.setBounds(((int) this.mIndicatorMarginLeft) + paddingLeft + this.mIndicatorRect.left, (height - ((int) this.mIndicatorHeight)) - ((int) this.mIndicatorMarginBottom), (paddingLeft + this.mIndicatorRect.right) - ((int) this.mIndicatorMarginRight), height - ((int) this.mIndicatorMarginBottom));
                } else {
                    this.mIndicatorDrawable.setBounds(((int) this.mIndicatorMarginLeft) + paddingLeft + this.mIndicatorRect.left, (int) this.mIndicatorMarginTop, (paddingLeft + this.mIndicatorRect.right) - ((int) this.mIndicatorMarginRight), ((int) this.mIndicatorHeight) + ((int) this.mIndicatorMarginTop));
                }
                this.mIndicatorDrawable.setCornerRadius(this.mIndicatorCornerRadius);
                this.mIndicatorDrawable.draw(canvas);
                return;
            }
            return;
        }
        if (this.mIndicatorHeight < 0.0f) {
            this.mIndicatorHeight = (height - this.mIndicatorMarginTop) - this.mIndicatorMarginBottom;
        }
        if (this.mIndicatorHeight > 0.0f) {
            if (this.mIndicatorCornerRadius < 0.0f || this.mIndicatorCornerRadius > this.mIndicatorHeight / 2.0f) {
                this.mIndicatorCornerRadius = this.mIndicatorHeight / 2.0f;
            }
            this.mIndicatorDrawable.setColor(this.mIndicatorColor);
            this.mIndicatorDrawable.setBounds(((int) this.mIndicatorMarginLeft) + paddingLeft + this.mIndicatorRect.left, (int) this.mIndicatorMarginTop, (int) ((paddingLeft + this.mIndicatorRect.right) - this.mIndicatorMarginRight), (int) (this.mIndicatorMarginTop + this.mIndicatorHeight));
            this.mIndicatorDrawable.setCornerRadius(this.mIndicatorCornerRadius);
            this.mIndicatorDrawable.draw(canvas);
        }
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mCurrentTab = bundle.getInt("mCurrentTab");
            parcelable = bundle.getParcelable("instanceState");
            if (this.mCurrentTab != 0 && this.mTabsContainer.getChildCount() > 0) {
                updateTabSelection(this.mCurrentTab);
            }
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", this.mCurrentTab);
        return bundle;
    }

    public void setCurrentTab(int i) {
        this.mLastTab = this.mCurrentTab;
        this.mCurrentTab = i;
        updateTabSelection(i);
        if (this.mFragmentChangeManager != null) {
            this.mFragmentChangeManager.setFragments(i);
        }
        if (this.mIndicatorAnimEnable) {
            calcOffset();
        } else {
            invalidate();
        }
    }

    public void setDividerColor(int i) {
        this.mDividerColor = i;
        invalidate();
    }

    public void setDividerPadding(float f) {
        this.mDividerPadding = dp2px(f);
        invalidate();
    }

    public void setDividerWidth(float f) {
        this.mDividerWidth = dp2px(f);
        invalidate();
    }

    public void setIconGravity(int i) {
        this.mIconGravity = i;
        notifyDataSetChanged();
    }

    public void setIconHeight(float f) {
        this.mIconHeight = dp2px(f);
        updateTabStyles();
    }

    public void setIconMargin(float f) {
        this.mIconMargin = dp2px(f);
        updateTabStyles();
    }

    public void setIconVisible(boolean z) {
        this.mIconVisible = z;
        updateTabStyles();
    }

    public void setIconWidth(float f) {
        this.mIconWidth = dp2px(f);
        updateTabStyles();
    }

    public void setIndicatorAnimDuration(long j) {
        this.mIndicatorAnimDuration = j;
    }

    public void setIndicatorAnimEnable(boolean z) {
        this.mIndicatorAnimEnable = z;
    }

    public void setIndicatorBounceEnable(boolean z) {
        this.mIndicatorBounceEnable = z;
    }

    public void setIndicatorColor(int i) {
        this.mIndicatorColor = i;
        invalidate();
    }

    public void setIndicatorCornerRadius(float f) {
        this.mIndicatorCornerRadius = dp2px(f);
        invalidate();
    }

    public void setIndicatorGravity(int i) {
        this.mIndicatorGravity = i;
        invalidate();
    }

    public void setIndicatorHeight(float f) {
        this.mIndicatorHeight = dp2px(f);
        invalidate();
    }

    public void setIndicatorMargin(float f, float f2, float f3, float f4) {
        this.mIndicatorMarginLeft = dp2px(f);
        this.mIndicatorMarginTop = dp2px(f2);
        this.mIndicatorMarginRight = dp2px(f3);
        this.mIndicatorMarginBottom = dp2px(f4);
        invalidate();
    }

    public void setIndicatorStyle(int i) {
        this.mIndicatorStyle = i;
        invalidate();
    }

    public void setIndicatorWidth(float f) {
        this.mIndicatorWidth = dp2px(f);
        invalidate();
    }

    public void setMsgMargin(int i, float f, float f2) {
        if (i >= this.mTabCount) {
            i = this.mTabCount - 1;
        }
        View childAt = this.mTabsContainer.getChildAt(i);
        MsgView msgView = (MsgView) childAt.findViewById(R.id.rtv_msg_tip);
        if (msgView != null) {
            TextView textView = (TextView) childAt.findViewById(R.id.tv_tab_title);
            this.mTextPaint.setTextSize(this.mTextsize);
            this.mTextPaint.measureText(textView.getText().toString());
            float descent = this.mTextPaint.descent() - this.mTextPaint.ascent();
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) msgView.getLayoutParams();
            float f3 = this.mIconHeight;
            float f4 = 0.0f;
            if (this.mIconVisible) {
                if (f3 <= 0.0f) {
                    f3 = this.mContext.getResources().getDrawable(this.mTabEntitys.get(i).getTabSelectedIcon()).getIntrinsicHeight();
                }
                f4 = this.mIconMargin;
            }
            if (this.mIconGravity == 48 || this.mIconGravity == 80) {
                marginLayoutParams.leftMargin = dp2px(f);
                marginLayoutParams.topMargin = this.mHeight > 0 ? (((int) (((this.mHeight - descent) - f3) - f4)) / 2) - dp2px(f2) : dp2px(f2);
            } else {
                marginLayoutParams.leftMargin = dp2px(f);
                marginLayoutParams.topMargin = this.mHeight > 0 ? (((int) (this.mHeight - Math.max(descent, f3))) / 2) - dp2px(f2) : dp2px(f2);
            }
            msgView.setLayoutParams(marginLayoutParams);
        }
    }

    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        this.mListener = onTabSelectListener;
    }

    public void setTabData(ArrayList<CustomTabEntity> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            throw new IllegalStateException("TabEntitys can not be NULL or EMPTY !");
        }
        this.mTabEntitys.clear();
        this.mTabEntitys.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void setTabData(ArrayList<CustomTabEntity> arrayList, FragmentActivity fragmentActivity, int i, ArrayList<Fragment> arrayList2) {
        this.mFragmentChangeManager = new FragmentChangeManager(fragmentActivity.getSupportFragmentManager(), i, arrayList2);
        setTabData(arrayList);
    }

    public void setTabPadding(float f) {
        this.mTabPadding = dp2px(f);
        updateTabStyles();
    }

    public void setTabSpaceEqual(boolean z) {
        this.mTabSpaceEqual = z;
        updateTabStyles();
    }

    public void setTabWidth(float f) {
        this.mTabWidth = dp2px(f);
        updateTabStyles();
    }

    public void setTextAllCaps(boolean z) {
        this.mTextAllCaps = z;
        updateTabStyles();
    }

    public void setTextBold(int i) {
        this.mTextBold = i;
        updateTabStyles();
    }

    public void setTextSelectColor(int i) {
        this.mTextSelectColor = i;
        updateTabStyles();
    }

    public void setTextUnselectColor(int i) {
        this.mTextUnselectColor = i;
        updateTabStyles();
    }

    public void setTextsize(float f) {
        this.mTextsize = sp2px(f);
        updateTabStyles();
    }

    public void setUnderlineColor(int i) {
        this.mUnderlineColor = i;
        invalidate();
    }

    public void setUnderlineGravity(int i) {
        this.mUnderlineGravity = i;
        invalidate();
    }

    public void setUnderlineHeight(float f) {
        this.mUnderlineHeight = dp2px(f);
        invalidate();
    }

    public void showDot(int i) {
        if (i >= this.mTabCount) {
            i = this.mTabCount - 1;
        }
        showMsg(i, 0);
    }

    public void showMsg(int i, int i2) {
        if (i >= this.mTabCount) {
            i = this.mTabCount - 1;
        }
        MsgView msgView = (MsgView) this.mTabsContainer.getChildAt(i).findViewById(R.id.rtv_msg_tip);
        if (msgView != null) {
            UnreadMsgUtils.show(msgView, i2);
            if (this.mInitSetMap.get(i) == null || !this.mInitSetMap.get(i).booleanValue()) {
                if (this.mIconVisible) {
                    setMsgMargin(i, 0.0f, (this.mIconGravity == 3 || this.mIconGravity == 5) ? 4.0f : 0.0f);
                } else {
                    setMsgMargin(i, 2.0f, 2.0f);
                }
                this.mInitSetMap.put(i, true);
            }
        }
    }

    protected int sp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}
