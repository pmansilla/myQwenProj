package com.qmuiteam.qmui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.location.common.model.AmapLoc;
import com.qmuiteam.qmui.QMUIInterpolatorStaticHolder;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIColorHelper;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.util.QMUILangHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class QMUITabSegment extends HorizontalScrollView {
    public static final int ICON_POSITION_BOTTOM = 3;
    public static final int ICON_POSITION_LEFT = 0;
    public static final int ICON_POSITION_RIGHT = 2;
    public static final int ICON_POSITION_TOP = 1;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    private static final int STATUS_NORMAL = 0;
    private static final int STATUS_PROGRESS = 1;
    private static final int STATUS_SELECTED = 2;
    private Container mContentLayout;
    private int mDefaultNormalColor;
    private int mDefaultSelectedColor;
    private int mDefaultTabIconPosition;
    private boolean mForceIndicatorNotDoLayoutWhenParentLayout;
    private boolean mHasIndicator;
    private Drawable mIndicatorDrawable;
    private int mIndicatorHeight;
    private boolean mIndicatorTop;
    private View mIndicatorView;
    private boolean mIsAnimating;
    private boolean mIsInSelectTab;
    private boolean mIsIndicatorWidthFollowContent;
    private int mItemSpaceInScrollMode;
    private int mMode;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private OnTabClickListener mOnTabClickListener;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mPagerAdapterObserver;
    private int mPendingSelectedIndex;
    private int mSelectedIndex;
    private final ArrayList<OnTabSelectedListener> mSelectedListeners;
    protected View.OnClickListener mTabOnClickListener;
    private int mTabTextSize;
    private TypefaceProvider mTypefaceProvider;
    private ViewPager mViewPager;
    private int mViewPagerScrollState;
    private OnTabSelectedListener mViewPagerSelectedListener;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class Container extends ViewGroup {
        private int mLastSelectedIndex;
        private TabAdapter mTabAdapter;

        public Container(Context context) {
            super(context);
            this.mLastSelectedIndex = -1;
            this.mTabAdapter = new TabAdapter(this);
        }

        public TabAdapter getTabAdapter() {
            return this.mTabAdapter;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            List<TabItemView> views = this.mTabAdapter.getViews();
            int size = views.size();
            int i5 = 0;
            for (int i6 = 0; i6 < size; i6++) {
                if (views.get(i6).getVisibility() == 0) {
                    i5++;
                }
            }
            if (size == 0 || i5 == 0) {
                return;
            }
            int paddingLeft = getPaddingLeft();
            for (int i7 = 0; i7 < size; i7++) {
                TabItemView tabItemView = views.get(i7);
                if (tabItemView.getVisibility() == 0) {
                    int measuredWidth = tabItemView.getMeasuredWidth();
                    int i8 = paddingLeft + measuredWidth;
                    tabItemView.layout(paddingLeft, getPaddingTop(), i8, (i4 - i2) - getPaddingBottom());
                    Tab item = this.mTabAdapter.getItem(i7);
                    int contentLeft = item.getContentLeft();
                    int contentWidth = item.getContentWidth();
                    if (QMUITabSegment.this.mMode == 1 && QMUITabSegment.this.mIsIndicatorWidthFollowContent) {
                        TextView textView = tabItemView.getTextView();
                        paddingLeft += textView.getLeft();
                        measuredWidth = textView.getWidth();
                    }
                    if (contentLeft != paddingLeft || contentWidth != measuredWidth) {
                        item.setContentLeft(paddingLeft);
                        item.setContentWidth(measuredWidth);
                    }
                    paddingLeft = i8 + (QMUITabSegment.this.mMode == 0 ? QMUITabSegment.this.mItemSpaceInScrollMode : 0);
                }
            }
            int i9 = QMUITabSegment.this.mSelectedIndex == Integer.MIN_VALUE ? 0 : QMUITabSegment.this.mSelectedIndex;
            Tab item2 = this.mTabAdapter.getItem(i9);
            int contentLeft2 = item2.getContentLeft();
            int contentWidth2 = item2.getContentWidth();
            if (QMUITabSegment.this.mIndicatorView != null) {
                if (i5 > 1) {
                    QMUITabSegment.this.mIndicatorView.setVisibility(0);
                    if (QMUITabSegment.this.mIndicatorTop) {
                        QMUITabSegment.this.mIndicatorView.layout(contentLeft2, 0, contentWidth2 + contentLeft2, QMUITabSegment.this.mIndicatorHeight);
                    } else {
                        int i10 = i4 - i2;
                        QMUITabSegment.this.mIndicatorView.layout(contentLeft2, i10 - QMUITabSegment.this.mIndicatorHeight, contentWidth2 + contentLeft2, i10);
                    }
                } else {
                    QMUITabSegment.this.mIndicatorView.setVisibility(8);
                }
            }
            this.mLastSelectedIndex = i9;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            List<TabItemView> views = this.mTabAdapter.getViews();
            int size3 = views.size();
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < size3; i5++) {
                if (views.get(i5).getVisibility() == 0) {
                    i4++;
                }
            }
            if (size3 == 0 || i4 == 0) {
                setMeasuredDimension(size, size2);
                return;
            }
            int paddingTop = (size2 - getPaddingTop()) - getPaddingBottom();
            if (QMUITabSegment.this.mMode == 1) {
                int i6 = size / i4;
                while (i3 < size3) {
                    TabItemView tabItemView = views.get(i3);
                    if (tabItemView.getVisibility() == 0) {
                        tabItemView.measure(View.MeasureSpec.makeMeasureSpec(i6, 1073741824), View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824));
                    }
                    i3++;
                }
            } else {
                int i7 = 0;
                while (i3 < size3) {
                    TabItemView tabItemView2 = views.get(i3);
                    if (tabItemView2.getVisibility() == 0) {
                        tabItemView2.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824));
                        i7 += tabItemView2.getMeasuredWidth() + QMUITabSegment.this.mItemSpaceInScrollMode;
                    }
                    i3++;
                }
                size = i7 - QMUITabSegment.this.mItemSpaceInScrollMode;
            }
            if (QMUITabSegment.this.mIndicatorView != null) {
                ViewGroup.LayoutParams layoutParams = QMUITabSegment.this.mIndicatorView.getLayoutParams();
                QMUITabSegment.this.mIndicatorView.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824));
            }
            setMeasuredDimension(size, size2);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface IconPosition {
    }

    /* loaded from: classes2.dex */
    public class InnerTextView extends AppCompatTextView {
        public InnerTextView(Context context) {
            super(context);
        }

        public InnerTextView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override // android.view.View
        public void requestLayout() {
            if (QMUITabSegment.this.mForceIndicatorNotDoLayoutWhenParentLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Mode {
    }

    /* loaded from: classes2.dex */
    public interface OnTabClickListener {
        void onTabClick(int i);
    }

    /* loaded from: classes2.dex */
    public interface OnTabSelectedListener {
        void onDoubleTap(int i);

        void onTabReselected(int i);

        void onTabSelected(int i);

        void onTabUnselected(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class PagerAdapterObserver extends DataSetObserver {
        private final boolean mUseAdapterTitle;

        PagerAdapterObserver(boolean z) {
            this.mUseAdapterTitle = z;
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            QMUITabSegment.this.populateFromPagerAdapter(this.mUseAdapterTitle);
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            QMUITabSegment.this.populateFromPagerAdapter(this.mUseAdapterTitle);
        }
    }

    /* loaded from: classes2.dex */
    public static class Tab {
        public static final int USE_TAB_SEGMENT = Integer.MIN_VALUE;
        private int contentLeft;
        private int contentWidth;
        private boolean dynamicChangeIconColor;
        private int gravity;
        private int iconPosition;
        private List<View> mCustomViews;
        private int mSignCountDigits;
        private int mSignCountMarginLeft;
        private int mSignCountMarginTop;
        private TextView mSignCountTextView;
        private int normalColor;
        private Drawable normalIcon;
        private int selectedColor;
        private Drawable selectedIcon;
        private CharSequence text;
        private int textSize;

        public Tab(Drawable drawable, Drawable drawable2, CharSequence charSequence, boolean z) {
            this(drawable, drawable2, charSequence, z, true);
        }

        public Tab(Drawable drawable, Drawable drawable2, CharSequence charSequence, boolean z, boolean z2) {
            this.textSize = Integer.MIN_VALUE;
            this.normalColor = Integer.MIN_VALUE;
            this.selectedColor = Integer.MIN_VALUE;
            this.normalIcon = null;
            this.selectedIcon = null;
            this.contentWidth = 0;
            this.contentLeft = 0;
            this.iconPosition = Integer.MIN_VALUE;
            this.gravity = 17;
            this.mSignCountDigits = 2;
            this.mSignCountMarginLeft = 0;
            this.mSignCountMarginTop = 0;
            this.dynamicChangeIconColor = true;
            this.normalIcon = drawable;
            if (this.normalIcon != null && z2) {
                this.normalIcon.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            this.selectedIcon = drawable2;
            if (this.selectedIcon != null && z2) {
                this.selectedIcon.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
            }
            this.text = charSequence;
            this.dynamicChangeIconColor = z;
        }

        public Tab(CharSequence charSequence) {
            this.textSize = Integer.MIN_VALUE;
            this.normalColor = Integer.MIN_VALUE;
            this.selectedColor = Integer.MIN_VALUE;
            this.normalIcon = null;
            this.selectedIcon = null;
            this.contentWidth = 0;
            this.contentLeft = 0;
            this.iconPosition = Integer.MIN_VALUE;
            this.gravity = 17;
            this.mSignCountDigits = 2;
            this.mSignCountMarginLeft = 0;
            this.mSignCountMarginTop = 0;
            this.dynamicChangeIconColor = true;
            this.text = charSequence;
        }

        private TextView ensureSignCountView(Context context) {
            if (this.mSignCountTextView == null) {
                this.mSignCountTextView = new TextView(context, null, R.attr.qmui_tab_sign_count_view);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, QMUIResHelper.getAttrDimen(context, R.attr.qmui_tab_sign_count_view_minSize));
                layoutParams.addRule(6, R.id.qmui_tab_segment_item_id);
                layoutParams.addRule(1, R.id.qmui_tab_segment_item_id);
                this.mSignCountTextView.setLayoutParams(layoutParams);
                addCustomView(this.mSignCountTextView);
            }
            setSignCountMargin(this.mSignCountMarginLeft, this.mSignCountMarginTop);
            return this.mSignCountTextView;
        }

        private RelativeLayout.LayoutParams getDefaultCustomLayoutParam() {
            return new RelativeLayout.LayoutParams(-2, -2);
        }

        private String getNumberDigitsFormattingValue(int i) {
            if (QMUILangHelper.getNumberDigits(i) <= this.mSignCountDigits) {
                return String.valueOf(i);
            }
            String str = "";
            for (int i2 = 1; i2 <= this.mSignCountDigits; i2++) {
                str = str + AmapLoc.RESULT_TYPE_CELL_WITHIN_SAME_ADDRESS;
            }
            return str + "+";
        }

        public void addCustomView(@NonNull View view) {
            if (this.mCustomViews == null) {
                this.mCustomViews = new ArrayList();
            }
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(getDefaultCustomLayoutParam());
            }
            this.mCustomViews.add(view);
        }

        public int getContentLeft() {
            return this.contentLeft;
        }

        public int getContentWidth() {
            return this.contentWidth;
        }

        public List<View> getCustomViews() {
            return this.mCustomViews;
        }

        public int getGravity() {
            return this.gravity;
        }

        public int getIconPosition() {
            return this.iconPosition;
        }

        public int getNormalColor() {
            return this.normalColor;
        }

        public Drawable getNormalIcon() {
            return this.normalIcon;
        }

        public int getSelectedColor() {
            return this.selectedColor;
        }

        public Drawable getSelectedIcon() {
            return this.selectedIcon;
        }

        public int getSignCount() {
            if (this.mSignCountTextView == null || QMUILangHelper.isNullOrEmpty(this.mSignCountTextView.getText())) {
                return 0;
            }
            return Integer.parseInt(this.mSignCountTextView.getText().toString());
        }

        public CharSequence getText() {
            return this.text;
        }

        public int getTextSize() {
            return this.textSize;
        }

        public void hideSignCountView() {
            if (this.mSignCountTextView != null) {
                this.mSignCountTextView.setVisibility(8);
            }
        }

        public boolean isDynamicChangeIconColor() {
            return this.dynamicChangeIconColor;
        }

        public void setContentLeft(int i) {
            this.contentLeft = i;
        }

        public void setContentWidth(int i) {
            this.contentWidth = i;
        }

        public void setGravity(int i) {
            this.gravity = i;
        }

        public void setIconPosition(int i) {
            this.iconPosition = i;
        }

        public void setSignCountMargin(int i, int i2) {
            this.mSignCountMarginLeft = i;
            this.mSignCountMarginTop = i2;
            if (this.mSignCountTextView == null || this.mSignCountTextView.getLayoutParams() == null) {
                return;
            }
            ((ViewGroup.MarginLayoutParams) this.mSignCountTextView.getLayoutParams()).leftMargin = i;
            ((ViewGroup.MarginLayoutParams) this.mSignCountTextView.getLayoutParams()).topMargin = i2;
        }

        public void setText(CharSequence charSequence) {
            this.text = charSequence;
        }

        public void setTextColor(@ColorInt int i, @ColorInt int i2) {
            this.normalColor = i;
            this.selectedColor = i2;
        }

        public void setTextSize(int i) {
            this.textSize = i;
        }

        public void setmSignCountDigits(int i) {
            this.mSignCountDigits = i;
        }

        public void showSignCountView(Context context, int i) {
            ensureSignCountView(context);
            this.mSignCountTextView.setVisibility(0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mSignCountTextView.getLayoutParams();
            if (i != 0) {
                layoutParams.height = QMUIResHelper.getAttrDimen(this.mSignCountTextView.getContext(), R.attr.qmui_tab_sign_count_view_minSize_with_text);
                this.mSignCountTextView.setLayoutParams(layoutParams);
                this.mSignCountTextView.setMinHeight(QMUIResHelper.getAttrDimen(this.mSignCountTextView.getContext(), R.attr.qmui_tab_sign_count_view_minSize_with_text));
                this.mSignCountTextView.setMinWidth(QMUIResHelper.getAttrDimen(this.mSignCountTextView.getContext(), R.attr.qmui_tab_sign_count_view_minSize_with_text));
                this.mSignCountTextView.setText(getNumberDigitsFormattingValue(i));
                return;
            }
            layoutParams.height = QMUIResHelper.getAttrDimen(this.mSignCountTextView.getContext(), R.attr.qmui_tab_sign_count_view_minSize);
            this.mSignCountTextView.setLayoutParams(layoutParams);
            this.mSignCountTextView.setMinHeight(QMUIResHelper.getAttrDimen(this.mSignCountTextView.getContext(), R.attr.qmui_tab_sign_count_view_minSize));
            this.mSignCountTextView.setMinWidth(QMUIResHelper.getAttrDimen(this.mSignCountTextView.getContext(), R.attr.qmui_tab_sign_count_view_minSize));
            this.mSignCountTextView.setText((CharSequence) null);
        }
    }

    /* loaded from: classes2.dex */
    public class TabAdapter extends QMUIItemViewsAdapter<Tab, TabItemView> {
        public TabAdapter(ViewGroup viewGroup) {
            super(viewGroup);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.qmuiteam.qmui.widget.QMUIItemViewsAdapter
        public void bind(Tab tab, TabItemView tabItemView, int i) {
            TextView textView = tabItemView.getTextView();
            QMUITabSegment.this.setTextViewTypeface(textView, false);
            List<View> customViews = tab.getCustomViews();
            if (customViews != null && customViews.size() > 0) {
                tabItemView.setTag(R.id.qmui_view_can_not_cache_tag, true);
                for (View view : customViews) {
                    if (view.getParent() == null) {
                        tabItemView.addView(view);
                    }
                }
            }
            if (QMUITabSegment.this.mMode == 1) {
                int gravity = tab.getGravity();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                layoutParams.addRule(9, (gravity & 3) == 3 ? -1 : 0);
                layoutParams.addRule(14, (gravity & 17) == 17 ? -1 : 0);
                layoutParams.addRule(11, (gravity & 5) != 5 ? 0 : -1);
                textView.setLayoutParams(layoutParams);
            }
            textView.setText(tab.getText());
            if (tab.getNormalIcon() == null) {
                textView.setCompoundDrawablePadding(0);
                textView.setCompoundDrawables(null, null, null, null);
            } else {
                Drawable normalIcon = tab.getNormalIcon();
                if (normalIcon != null) {
                    QMUITabSegment.this.setDrawable(textView, normalIcon.mutate(), QMUITabSegment.this.getTabIconPosition(tab));
                    textView.setCompoundDrawablePadding(QMUIDisplayHelper.dp2px(QMUITabSegment.this.getContext(), 4));
                } else {
                    textView.setCompoundDrawables(null, null, null, null);
                }
            }
            int textSize = tab.getTextSize();
            if (textSize == Integer.MIN_VALUE) {
                textSize = QMUITabSegment.this.mTabTextSize;
            }
            textView.setTextSize(0, textSize);
            if (i == QMUITabSegment.this.mSelectedIndex) {
                if (QMUITabSegment.this.mIndicatorView != null && getViews().size() > 1) {
                    if (QMUITabSegment.this.mIndicatorDrawable != null) {
                        QMUIViewHelper.setBackgroundKeepingPadding(QMUITabSegment.this.mIndicatorView, QMUITabSegment.this.mIndicatorDrawable);
                    } else {
                        QMUITabSegment.this.mIndicatorView.setBackgroundColor(QMUITabSegment.this.getTabSelectedColor(tab));
                    }
                }
                QMUITabSegment.this.changeTabColor(tabItemView.getTextView(), QMUITabSegment.this.getTabSelectedColor(tab), tab, 2);
            } else {
                QMUITabSegment.this.changeTabColor(tabItemView.getTextView(), QMUITabSegment.this.getTabNormalColor(tab), tab, 0);
            }
            tabItemView.setTag(Integer.valueOf(i));
            tabItemView.setOnClickListener(QMUITabSegment.this.mTabOnClickListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.qmuiteam.qmui.widget.QMUIItemViewsAdapter
        public TabItemView createView(ViewGroup viewGroup) {
            return new TabItemView(QMUITabSegment.this.getContext());
        }
    }

    /* loaded from: classes2.dex */
    public class TabItemView extends RelativeLayout {
        private GestureDetector mGestureDetector;
        private InnerTextView mTextView;

        public TabItemView(Context context) {
            super(context);
            this.mGestureDetector = null;
            this.mTextView = new InnerTextView(getContext());
            this.mTextView.setSingleLine(true);
            this.mTextView.setGravity(17);
            this.mTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            this.mTextView.setId(R.id.qmui_tab_segment_item_id);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(15, -1);
            addView(this.mTextView, layoutParams);
            this.mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.qmuiteam.qmui.widget.QMUITabSegment.TabItemView.1
                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
                public boolean onDoubleTap(MotionEvent motionEvent) {
                    if (QMUITabSegment.this.mSelectedListeners == null || QMUITabSegment.this.mIsAnimating) {
                        return false;
                    }
                    int intValue = ((Integer) TabItemView.this.getTag()).intValue();
                    if (QMUITabSegment.this.getAdapter().getItem(intValue) == null) {
                        return false;
                    }
                    QMUITabSegment.this.dispatchTabDoubleTap(intValue);
                    return true;
                }
            });
        }

        public TextView getTextView() {
            return this.mTextView;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return this.mGestureDetector.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
        }
    }

    /* loaded from: classes2.dex */
    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<QMUITabSegment> mTabSegmentRef;

        public TabLayoutOnPageChangeListener(QMUITabSegment qMUITabSegment) {
            this.mTabSegmentRef = new WeakReference<>(qMUITabSegment);
        }

        @Override // android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
            QMUITabSegment qMUITabSegment = this.mTabSegmentRef.get();
            if (qMUITabSegment != null) {
                qMUITabSegment.mViewPagerScrollState = i;
            }
        }

        @Override // android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            QMUITabSegment qMUITabSegment = this.mTabSegmentRef.get();
            if (qMUITabSegment != null) {
                qMUITabSegment.updateIndicatorPosition(i, f);
            }
        }

        @Override // android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            QMUITabSegment qMUITabSegment = this.mTabSegmentRef.get();
            if (qMUITabSegment == null || qMUITabSegment.getSelectedIndex() == i || i >= qMUITabSegment.getTabCount()) {
                return;
            }
            qMUITabSegment.selectTab(i);
        }
    }

    /* loaded from: classes2.dex */
    public interface TypefaceProvider {
        boolean isNormalTabBold();

        boolean isSelectedTabBold();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        @Override // com.qmuiteam.qmui.widget.QMUITabSegment.OnTabSelectedListener
        public void onDoubleTap(int i) {
        }

        @Override // com.qmuiteam.qmui.widget.QMUITabSegment.OnTabSelectedListener
        public void onTabReselected(int i) {
        }

        @Override // com.qmuiteam.qmui.widget.QMUITabSegment.OnTabSelectedListener
        public void onTabSelected(int i) {
            this.mViewPager.setCurrentItem(i, false);
        }

        @Override // com.qmuiteam.qmui.widget.QMUITabSegment.OnTabSelectedListener
        public void onTabUnselected(int i) {
        }
    }

    public QMUITabSegment(Context context) {
        this(context, (AttributeSet) null);
    }

    public QMUITabSegment(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.QMUITabSegmentStyle);
    }

    public QMUITabSegment(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSelectedListeners = new ArrayList<>();
        this.mSelectedIndex = Integer.MIN_VALUE;
        this.mPendingSelectedIndex = Integer.MIN_VALUE;
        this.mHasIndicator = true;
        this.mIndicatorTop = false;
        this.mIsIndicatorWidthFollowContent = true;
        this.mMode = 1;
        this.mViewPagerScrollState = 0;
        this.mForceIndicatorNotDoLayoutWhenParentLayout = false;
        this.mTabOnClickListener = new View.OnClickListener() { // from class: com.qmuiteam.qmui.widget.QMUITabSegment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (QMUITabSegment.this.mIsAnimating || QMUITabSegment.this.mViewPagerScrollState != 0) {
                    return;
                }
                int intValue = ((Integer) view.getTag()).intValue();
                if (QMUITabSegment.this.getAdapter().getItem(intValue) != null) {
                    QMUITabSegment.this.selectTab(intValue, !r0.isDynamicChangeIconColor());
                }
                if (QMUITabSegment.this.mOnTabClickListener != null) {
                    QMUITabSegment.this.mOnTabClickListener.onTabClick(intValue);
                }
            }
        };
        this.mIsInSelectTab = false;
        init(context, attributeSet, i);
        setHorizontalScrollBarEnabled(false);
        setClipToPadding(false);
    }

    public QMUITabSegment(Context context, boolean z) {
        this(context, (AttributeSet) null);
        this.mHasIndicator = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeTabColor(TextView textView, int i, Tab tab, int i2) {
        changeTabColor(textView, i, tab, i2, false);
    }

    private void changeTabColor(TextView textView, int i, Tab tab, int i2, boolean z) {
        Drawable drawable;
        if (!z) {
            textView.setTextColor(i);
        }
        if (tab.isDynamicChangeIconColor()) {
            if (z || (drawable = textView.getCompoundDrawables()[getTabIconPosition(tab)]) == null) {
                return;
            }
            QMUIDrawableHelper.setDrawableTintColor(drawable, i);
            setDrawable(textView, tab.getNormalIcon(), getTabIconPosition(tab));
            return;
        }
        if (i2 == 0 || tab.getSelectedIcon() == null) {
            setDrawable(textView, tab.getNormalIcon(), getTabIconPosition(tab));
        } else if (i2 == 2) {
            setDrawable(textView, tab.getSelectedIcon(), getTabIconPosition(tab));
        }
    }

    private void createIndicatorView() {
        if (this.mIndicatorView == null) {
            this.mIndicatorView = new View(getContext());
            this.mIndicatorView.setLayoutParams(new FrameLayout.LayoutParams(-2, this.mIndicatorHeight));
            if (this.mIndicatorDrawable != null) {
                QMUIViewHelper.setBackgroundKeepingPadding(this.mIndicatorView, this.mIndicatorDrawable);
            } else {
                this.mIndicatorView.setBackgroundColor(this.mDefaultSelectedColor);
            }
            this.mContentLayout.addView(this.mIndicatorView);
        }
    }

    private void createTypefaceProvider(Context context, String str) {
        if (QMUILangHelper.isNullOrEmpty(str)) {
            return;
        }
        String trim = str.trim();
        if (trim.length() == 0) {
            return;
        }
        String fullClassName = getFullClassName(context, trim);
        try {
            try {
                Constructor constructor = (isInEditMode() ? getClass().getClassLoader() : context.getClassLoader()).loadClass(fullClassName).asSubclass(TypefaceProvider.class).getConstructor(new Class[0]);
                constructor.setAccessible(true);
                this.mTypefaceProvider = (TypefaceProvider) constructor.newInstance(new Object[0]);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Error creating TypefaceProvider " + fullClassName, e);
            }
        } catch (ClassCastException e2) {
            throw new IllegalStateException("Class is not a TypefaceProvider " + fullClassName, e2);
        } catch (ClassNotFoundException e3) {
            throw new IllegalStateException("Unable to find TypefaceProvider " + fullClassName, e3);
        } catch (IllegalAccessException e4) {
            throw new IllegalStateException("Cannot access non-public constructor " + fullClassName, e4);
        } catch (InstantiationException e5) {
            throw new IllegalStateException("Could not instantiate the TypefaceProvider: " + fullClassName, e5);
        } catch (InvocationTargetException e6) {
            throw new IllegalStateException("Could not instantiate the TypefaceProvider: " + fullClassName, e6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchTabDoubleTap(int i) {
        for (int size = this.mSelectedListeners.size() - 1; size >= 0; size--) {
            this.mSelectedListeners.get(size).onDoubleTap(i);
        }
    }

    private void dispatchTabReselected(int i) {
        for (int size = this.mSelectedListeners.size() - 1; size >= 0; size--) {
            this.mSelectedListeners.get(size).onTabReselected(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchTabSelected(int i) {
        for (int size = this.mSelectedListeners.size() - 1; size >= 0; size--) {
            this.mSelectedListeners.get(size).onTabSelected(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchTabUnselected(int i) {
        for (int size = this.mSelectedListeners.size() - 1; size >= 0; size--) {
            this.mSelectedListeners.get(size).onTabUnselected(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TabAdapter getAdapter() {
        return this.mContentLayout.getTabAdapter();
    }

    private String getFullClassName(Context context, String str) {
        if (str.charAt(0) != '.') {
            return str;
        }
        return context.getPackageName() + str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTabCount() {
        return getAdapter().getSize();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTabIconPosition(Tab tab) {
        int iconPosition = tab.getIconPosition();
        return iconPosition == Integer.MIN_VALUE ? this.mDefaultTabIconPosition : iconPosition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTabNormalColor(Tab tab) {
        int normalColor = tab.getNormalColor();
        return normalColor == Integer.MIN_VALUE ? this.mDefaultNormalColor : normalColor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTabSelectedColor(Tab tab) {
        int selectedColor = tab.getSelectedColor();
        return selectedColor == Integer.MIN_VALUE ? this.mDefaultSelectedColor : selectedColor;
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        this.mDefaultSelectedColor = QMUIResHelper.getAttrColor(context, R.attr.qmui_config_color_blue);
        this.mDefaultNormalColor = ContextCompat.getColor(context, R.color.qmui_config_color_gray_5);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUITabSegment, i, 0);
        this.mHasIndicator = obtainStyledAttributes.getBoolean(R.styleable.QMUITabSegment_qmui_tab_has_indicator, true);
        this.mIndicatorHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUITabSegment_qmui_tab_indicator_height, getResources().getDimensionPixelSize(R.dimen.qmui_tab_segment_indicator_height));
        this.mTabTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUITabSegment_android_textSize, getResources().getDimensionPixelSize(R.dimen.qmui_tab_segment_text_size));
        this.mIndicatorTop = obtainStyledAttributes.getBoolean(R.styleable.QMUITabSegment_qmui_tab_indicator_top, false);
        this.mDefaultTabIconPosition = obtainStyledAttributes.getInt(R.styleable.QMUITabSegment_qmui_tab_icon_position, 0);
        this.mMode = obtainStyledAttributes.getInt(R.styleable.QMUITabSegment_qmui_tab_mode, 1);
        this.mItemSpaceInScrollMode = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QMUITabSegment_qmui_tab_space, QMUIDisplayHelper.dp2px(context, 10));
        String string = obtainStyledAttributes.getString(R.styleable.QMUITabSegment_qmui_tab_typeface_provider);
        obtainStyledAttributes.recycle();
        this.mContentLayout = new Container(context);
        addView(this.mContentLayout, new FrameLayout.LayoutParams(-2, -1));
        if (this.mHasIndicator) {
            createIndicatorView();
        }
        createTypefaceProvider(context, string);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void preventLayoutToChangeTabColor(TextView textView, int i, Tab tab, int i2) {
        this.mForceIndicatorNotDoLayoutWhenParentLayout = true;
        changeTabColor(textView, i, tab, i2);
        this.mForceIndicatorNotDoLayoutWhenParentLayout = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectTab(final int i, boolean z) {
        if (this.mIsInSelectTab) {
            return;
        }
        this.mIsInSelectTab = true;
        if (this.mContentLayout.getTabAdapter().getSize() == 0 || this.mContentLayout.getTabAdapter().getSize() <= i) {
            this.mIsInSelectTab = false;
            return;
        }
        if (this.mSelectedIndex == i) {
            dispatchTabReselected(i);
            this.mIsInSelectTab = false;
            return;
        }
        if (this.mIsAnimating) {
            this.mPendingSelectedIndex = i;
            this.mIsInSelectTab = false;
            return;
        }
        TabAdapter adapter = getAdapter();
        final List<TabItemView> views = adapter.getViews();
        if (this.mSelectedIndex == Integer.MIN_VALUE) {
            adapter.setup();
            Tab item = adapter.getItem(i);
            if (this.mIndicatorView != null && views.size() > 1) {
                if (this.mIndicatorDrawable != null) {
                    QMUIViewHelper.setBackgroundKeepingPadding(this.mIndicatorView, this.mIndicatorDrawable);
                } else {
                    this.mIndicatorView.setBackgroundColor(getTabSelectedColor(item));
                }
            }
            TextView textView = views.get(i).getTextView();
            setTextViewTypeface(textView, true);
            changeTabColor(textView, getTabSelectedColor(item), item, 2);
            dispatchTabSelected(i);
            this.mSelectedIndex = i;
            this.mIsInSelectTab = false;
            return;
        }
        final int i2 = this.mSelectedIndex;
        final Tab item2 = adapter.getItem(i2);
        final TabItemView tabItemView = views.get(i2);
        final Tab item3 = adapter.getItem(i);
        final TabItemView tabItemView2 = views.get(i);
        if (!z) {
            final int contentLeft = item3.getContentLeft() - item2.getContentLeft();
            final int contentWidth = item3.getContentWidth() - item2.getContentWidth();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setInterpolator(QMUIInterpolatorStaticHolder.LINEAR_INTERPOLATOR);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.qmuiteam.qmui.widget.QMUITabSegment.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    if (QMUITabSegment.this.mIndicatorView != null && views.size() > 1) {
                        int contentLeft2 = (int) (item2.getContentLeft() + (contentLeft * floatValue));
                        int contentWidth2 = (int) (item2.getContentWidth() + (contentWidth * floatValue));
                        if (QMUITabSegment.this.mIndicatorDrawable == null) {
                            QMUITabSegment.this.mIndicatorView.setBackgroundColor(QMUIColorHelper.computeColor(QMUITabSegment.this.getTabSelectedColor(item2), QMUITabSegment.this.getTabSelectedColor(item3), floatValue));
                        }
                        QMUITabSegment.this.mIndicatorView.layout(contentLeft2, QMUITabSegment.this.mIndicatorView.getTop(), contentWidth2 + contentLeft2, QMUITabSegment.this.mIndicatorView.getBottom());
                    }
                    int computeColor = QMUIColorHelper.computeColor(QMUITabSegment.this.getTabSelectedColor(item2), QMUITabSegment.this.getTabNormalColor(item2), floatValue);
                    int computeColor2 = QMUIColorHelper.computeColor(QMUITabSegment.this.getTabNormalColor(item3), QMUITabSegment.this.getTabSelectedColor(item3), floatValue);
                    QMUITabSegment.this.preventLayoutToChangeTabColor(tabItemView.getTextView(), computeColor, item2, 1);
                    QMUITabSegment.this.preventLayoutToChangeTabColor(tabItemView2.getTextView(), computeColor2, item3, 1);
                }
            });
            ofFloat.addListener(new Animator.AnimatorListener() { // from class: com.qmuiteam.qmui.widget.QMUITabSegment.3
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    QMUITabSegment.this.changeTabColor(tabItemView2.getTextView(), QMUITabSegment.this.getTabSelectedColor(item3), item3, 2);
                    QMUITabSegment.this.mIsAnimating = false;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    QMUITabSegment.this.mIsAnimating = false;
                    QMUITabSegment.this.changeTabColor(tabItemView2.getTextView(), QMUITabSegment.this.getTabSelectedColor(item3), item3, 2);
                    QMUITabSegment.this.dispatchTabSelected(i);
                    QMUITabSegment.this.dispatchTabUnselected(i2);
                    QMUITabSegment.this.setTextViewTypeface(tabItemView.getTextView(), false);
                    QMUITabSegment.this.setTextViewTypeface(tabItemView2.getTextView(), true);
                    QMUITabSegment.this.mSelectedIndex = i;
                    if (QMUITabSegment.this.mPendingSelectedIndex == Integer.MIN_VALUE || QMUITabSegment.this.mPendingSelectedIndex == QMUITabSegment.this.mSelectedIndex) {
                        return;
                    }
                    QMUITabSegment.this.selectTab(i, false);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    QMUITabSegment.this.mIsAnimating = true;
                }
            });
            ofFloat.setDuration(200L);
            ofFloat.start();
            this.mIsInSelectTab = false;
            return;
        }
        dispatchTabUnselected(i2);
        dispatchTabSelected(i);
        setTextViewTypeface(tabItemView.getTextView(), false);
        setTextViewTypeface(tabItemView2.getTextView(), true);
        changeTabColor(tabItemView.getTextView(), getTabNormalColor(item2), item2, 0, this.mViewPagerScrollState != 0);
        changeTabColor(tabItemView2.getTextView(), getTabSelectedColor(item3), item3, 2, this.mViewPagerScrollState != 0);
        if (getScrollX() > tabItemView2.getLeft()) {
            smoothScrollTo(tabItemView2.getLeft(), 0);
        } else {
            int width = (getWidth() - getPaddingRight()) - getPaddingLeft();
            if (getScrollX() + width < tabItemView2.getRight()) {
                smoothScrollBy((tabItemView2.getRight() - width) - getScrollX(), 0);
            }
        }
        this.mSelectedIndex = i;
        this.mIsInSelectTab = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDrawable(TextView textView, Drawable drawable, int i) {
        Drawable drawable2 = i == 0 ? drawable : null;
        Drawable drawable3 = i == 1 ? drawable : null;
        Drawable drawable4 = i == 2 ? drawable : null;
        if (i != 3) {
            drawable = null;
        }
        textView.setCompoundDrawables(drawable2, drawable3, drawable4, drawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTextViewTypeface(TextView textView, boolean z) {
        if (this.mTypefaceProvider == null || textView == null) {
            return;
        }
        textView.setTypeface(null, z ? this.mTypefaceProvider.isSelectedTabBold() : this.mTypefaceProvider.isNormalTabBold() ? 1 : 0);
    }

    public void addOnTabSelectedListener(@NonNull OnTabSelectedListener onTabSelectedListener) {
        if (this.mSelectedListeners.contains(onTabSelectedListener)) {
            return;
        }
        this.mSelectedListeners.add(onTabSelectedListener);
    }

    public QMUITabSegment addTab(Tab tab) {
        this.mContentLayout.getTabAdapter().addItem(tab);
        return this;
    }

    public void clearOnTabSelectedListeners() {
        this.mSelectedListeners.clear();
    }

    public int getMode() {
        return this.mMode;
    }

    public int getSelectedIndex() {
        return this.mSelectedIndex;
    }

    public int getSignCount(int i) {
        return getAdapter().getItem(i).getSignCount();
    }

    public Tab getTab(int i) {
        return getAdapter().getItem(i);
    }

    public void hideSignCountView(int i) {
        getAdapter().getItem(i).hideSignCountView();
    }

    public void notifyDataChanged() {
        getAdapter().setup();
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mSelectedIndex == Integer.MIN_VALUE || this.mMode != 0) {
            return;
        }
        TabItemView tabItemView = getAdapter().getViews().get(this.mSelectedIndex);
        if (getScrollX() > tabItemView.getLeft()) {
            scrollTo(tabItemView.getLeft(), 0);
            return;
        }
        int width = (getWidth() - getPaddingRight()) - getPaddingLeft();
        if (getScrollX() + width < tabItemView.getRight()) {
            scrollBy((tabItemView.getRight() - width) - getScrollX(), 0);
        }
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int mode = View.MeasureSpec.getMode(i);
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            int paddingLeft = getPaddingLeft() + getPaddingRight();
            childAt.measure(View.MeasureSpec.makeMeasureSpec(size - paddingLeft, 1073741824), i2);
            if (mode == Integer.MIN_VALUE) {
                setMeasuredDimension(Math.min(size, childAt.getMeasuredWidth() + paddingLeft), i2);
                return;
            }
        }
        setMeasuredDimension(i, i2);
    }

    void populateFromPagerAdapter(boolean z) {
        int currentItem;
        if (this.mPagerAdapter == null) {
            if (z) {
                reset();
                return;
            }
            return;
        }
        int count = this.mPagerAdapter.getCount();
        if (z) {
            reset();
            for (int i = 0; i < count; i++) {
                addTab(new Tab(this.mPagerAdapter.getPageTitle(i)));
            }
            notifyDataChanged();
        }
        if (this.mViewPager == null || count <= 0 || (currentItem = this.mViewPager.getCurrentItem()) == this.mSelectedIndex || currentItem >= count) {
            return;
        }
        selectTab(currentItem);
    }

    public void removeOnTabSelectedListener(@NonNull OnTabSelectedListener onTabSelectedListener) {
        this.mSelectedListeners.remove(onTabSelectedListener);
    }

    public void replaceTab(int i, Tab tab) {
        try {
            getAdapter().replaceItem(i, tab);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        this.mContentLayout.getTabAdapter().clear();
    }

    public void selectTab(int i) {
        selectTab(i, true);
    }

    public void setDefaultNormalColor(@ColorInt int i) {
        this.mDefaultNormalColor = i;
    }

    public void setDefaultSelectedColor(@ColorInt int i) {
        this.mDefaultSelectedColor = i;
    }

    public void setDefaultTabIconPosition(int i) {
        this.mDefaultTabIconPosition = i;
    }

    public void setHasIndicator(boolean z) {
        if (this.mHasIndicator != z) {
            this.mHasIndicator = z;
            if (this.mHasIndicator) {
                createIndicatorView();
            } else {
                this.mContentLayout.removeView(this.mIndicatorView);
                this.mIndicatorView = null;
            }
        }
    }

    public void setIndicatorDrawable(Drawable drawable) {
        this.mIndicatorDrawable = drawable;
        if (drawable != null) {
            this.mIndicatorHeight = drawable.getIntrinsicHeight();
        }
        this.mContentLayout.invalidate();
    }

    public void setIndicatorPosition(boolean z) {
        this.mIndicatorTop = z;
    }

    public void setIndicatorWidthAdjustContent(boolean z) {
        this.mIsIndicatorWidthFollowContent = z;
    }

    public void setItemSpaceInScrollMode(int i) {
        this.mItemSpaceInScrollMode = i;
    }

    public void setMode(int i) {
        if (this.mMode != i) {
            this.mMode = i;
            this.mContentLayout.invalidate();
        }
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.mOnTabClickListener = onTabClickListener;
    }

    void setPagerAdapter(@Nullable PagerAdapter pagerAdapter, boolean z, boolean z2) {
        if (this.mPagerAdapter != null && this.mPagerAdapterObserver != null) {
            this.mPagerAdapter.unregisterDataSetObserver(this.mPagerAdapterObserver);
        }
        this.mPagerAdapter = pagerAdapter;
        if (z2 && pagerAdapter != null) {
            if (this.mPagerAdapterObserver == null) {
                this.mPagerAdapterObserver = new PagerAdapterObserver(z);
            }
            pagerAdapter.registerDataSetObserver(this.mPagerAdapterObserver);
        }
        populateFromPagerAdapter(z);
    }

    public void setTabTextSize(int i) {
        this.mTabTextSize = i;
    }

    public void setTypefaceProvider(TypefaceProvider typefaceProvider) {
        this.mTypefaceProvider = typefaceProvider;
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager, boolean z) {
        setupWithViewPager(viewPager, z, true);
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager, boolean z, boolean z2) {
        if (this.mViewPager != null && this.mOnPageChangeListener != null) {
            this.mViewPager.removeOnPageChangeListener(this.mOnPageChangeListener);
        }
        if (this.mViewPagerSelectedListener != null) {
            removeOnTabSelectedListener(this.mViewPagerSelectedListener);
            this.mViewPagerSelectedListener = null;
        }
        if (viewPager == null) {
            this.mViewPager = null;
            setPagerAdapter(null, false, false);
            return;
        }
        this.mViewPager = viewPager;
        if (this.mOnPageChangeListener == null) {
            this.mOnPageChangeListener = new TabLayoutOnPageChangeListener(this);
        }
        viewPager.addOnPageChangeListener(this.mOnPageChangeListener);
        this.mViewPagerSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
        addOnTabSelectedListener(this.mViewPagerSelectedListener);
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null) {
            setPagerAdapter(adapter, z, z2);
        }
    }

    public void showSignCountView(Context context, int i, int i2) {
        getAdapter().getItem(i).showSignCountView(context, i2);
        notifyDataChanged();
    }

    public void updateIndicatorPosition(int i, float f) {
        int i2;
        if (this.mIsAnimating || this.mIsInSelectTab || f == 0.0f) {
            return;
        }
        if (f < 0.0f) {
            i2 = i - 1;
            f = -f;
        } else {
            i2 = i + 1;
        }
        TabAdapter adapter = getAdapter();
        List<TabItemView> views = adapter.getViews();
        if (views.size() <= i || views.size() <= i2) {
            return;
        }
        Tab item = adapter.getItem(i);
        Tab item2 = adapter.getItem(i2);
        TextView textView = views.get(i).getTextView();
        TextView textView2 = views.get(i2).getTextView();
        int computeColor = QMUIColorHelper.computeColor(getTabSelectedColor(item), getTabNormalColor(item), f);
        int computeColor2 = QMUIColorHelper.computeColor(getTabNormalColor(item2), getTabSelectedColor(item2), f);
        preventLayoutToChangeTabColor(textView, computeColor, item, 1);
        preventLayoutToChangeTabColor(textView2, computeColor2, item2, 1);
        this.mForceIndicatorNotDoLayoutWhenParentLayout = false;
        if (this.mIndicatorView == null || views.size() <= 1) {
            return;
        }
        int contentLeft = item2.getContentLeft() - item.getContentLeft();
        int contentLeft2 = (int) (item.getContentLeft() + (contentLeft * f));
        int contentWidth = (int) (item.getContentWidth() + ((item2.getContentWidth() - item.getContentWidth()) * f));
        if (this.mIndicatorDrawable == null) {
            this.mIndicatorView.setBackgroundColor(QMUIColorHelper.computeColor(getTabSelectedColor(item), getTabSelectedColor(item2), f));
        }
        this.mIndicatorView.layout(contentLeft2, this.mIndicatorView.getTop(), contentWidth + contentLeft2, this.mIndicatorView.getBottom());
    }

    public void updateTabText(int i, String str) {
        Tab item = getAdapter().getItem(i);
        if (item == null) {
            return;
        }
        item.setText(str);
        notifyDataChanged();
    }
}
