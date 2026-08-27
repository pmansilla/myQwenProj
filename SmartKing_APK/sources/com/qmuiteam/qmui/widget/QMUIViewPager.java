package com.qmuiteam.qmui.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.qmuiteam.qmui.util.QMUIWindowInsetHelper;

/* loaded from: classes2.dex */
public class QMUIViewPager extends ViewPager implements IWindowInsetLayout {
    private static final int DEFAULT_INFINITE_RATIO = 100;
    private boolean mEnableLoop;
    private int mInfiniteRatio;
    private boolean mIsInMeasure;
    private boolean mIsSwipeable;
    private QMUIWindowInsetHelper mQMUIWindowInsetHelper;

    /* loaded from: classes2.dex */
    class WrapperPagerAdapter extends PagerAdapter {
        private QMUIPagerAdapter mAdapter;

        public WrapperPagerAdapter(QMUIPagerAdapter qMUIPagerAdapter) {
            this.mAdapter = qMUIPagerAdapter;
        }

        @Override // android.support.v4.view.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            if (QMUIViewPager.this.mEnableLoop && this.mAdapter.getCount() != 0) {
                i %= this.mAdapter.getCount();
            }
            this.mAdapter.destroyItem(viewGroup, i, obj);
        }

        @Override // android.support.v4.view.PagerAdapter
        public void finishUpdate(ViewGroup viewGroup) {
            this.mAdapter.finishUpdate(viewGroup);
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            if (!QMUIViewPager.this.mEnableLoop) {
                return this.mAdapter.getCount();
            }
            if (this.mAdapter.getCount() == 0) {
                return 0;
            }
            return this.mAdapter.getCount() * QMUIViewPager.this.mInfiniteRatio;
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getItemPosition(Object obj) {
            return this.mAdapter.getItemPosition(obj);
        }

        @Override // android.support.v4.view.PagerAdapter
        public CharSequence getPageTitle(int i) {
            return this.mAdapter.getPageTitle(i % this.mAdapter.getCount());
        }

        @Override // android.support.v4.view.PagerAdapter
        public float getPageWidth(int i) {
            return this.mAdapter.getPageWidth(i);
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            if (QMUIViewPager.this.mEnableLoop && this.mAdapter.getCount() != 0) {
                i %= this.mAdapter.getCount();
            }
            return this.mAdapter.instantiateItem(viewGroup, i);
        }

        @Override // android.support.v4.view.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return this.mAdapter.isViewFromObject(view, obj);
        }

        @Override // android.support.v4.view.PagerAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            this.mAdapter.notifyDataSetChanged();
        }

        @Override // android.support.v4.view.PagerAdapter
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            this.mAdapter.registerDataSetObserver(dataSetObserver);
        }

        @Override // android.support.v4.view.PagerAdapter
        public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
            this.mAdapter.restoreState(parcelable, classLoader);
        }

        @Override // android.support.v4.view.PagerAdapter
        public Parcelable saveState() {
            return this.mAdapter.saveState();
        }

        @Override // android.support.v4.view.PagerAdapter
        public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
            this.mAdapter.setPrimaryItem(viewGroup, i, obj);
        }

        @Override // android.support.v4.view.PagerAdapter
        public void startUpdate(ViewGroup viewGroup) {
            this.mAdapter.startUpdate(viewGroup);
        }

        @Override // android.support.v4.view.PagerAdapter
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            this.mAdapter.unregisterDataSetObserver(dataSetObserver);
        }
    }

    public QMUIViewPager(Context context) {
        this(context, null);
    }

    public QMUIViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsSwipeable = true;
        this.mIsInMeasure = false;
        this.mEnableLoop = false;
        this.mInfiniteRatio = 100;
        this.mQMUIWindowInsetHelper = new QMUIWindowInsetHelper(this, this);
    }

    @Override // com.qmuiteam.qmui.widget.IWindowInsetLayout
    public boolean applySystemWindowInsets19(Rect rect) {
        return this.mQMUIWindowInsetHelper.defaultApplySystemWindowInsets19(this, rect);
    }

    @Override // com.qmuiteam.qmui.widget.IWindowInsetLayout
    public boolean applySystemWindowInsets21(WindowInsetsCompat windowInsetsCompat) {
        return this.mQMUIWindowInsetHelper.defaultApplySystemWindowInsets21(this, windowInsetsCompat);
    }

    @Override // android.view.View
    protected boolean fitSystemWindows(Rect rect) {
        return (Build.VERSION.SDK_INT < 19 || Build.VERSION.SDK_INT >= 21) ? super.fitSystemWindows(rect) : applySystemWindowInsets19(rect);
    }

    public int getInfiniteRatio() {
        return this.mInfiniteRatio;
    }

    public boolean isEnableLoop() {
        return this.mEnableLoop;
    }

    public boolean isInMeasure() {
        return this.mIsInMeasure;
    }

    @Override // android.support.v4.view.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mIsSwipeable && super.onInterceptTouchEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.view.ViewPager, android.view.View
    public void onMeasure(int i, int i2) {
        this.mIsInMeasure = true;
        super.onMeasure(i, i2);
        this.mIsInMeasure = false;
    }

    @Override // android.support.v4.view.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mIsSwipeable && super.onTouchEvent(motionEvent);
    }

    @Override // android.support.v4.view.ViewPager
    public void setAdapter(PagerAdapter pagerAdapter) {
        if (pagerAdapter instanceof QMUIPagerAdapter) {
            super.setAdapter(new WrapperPagerAdapter((QMUIPagerAdapter) pagerAdapter));
        } else {
            super.setAdapter(pagerAdapter);
        }
    }

    public void setEnableLoop(boolean z) {
        if (this.mEnableLoop != z) {
            this.mEnableLoop = z;
            if (getAdapter() != null) {
                getAdapter().notifyDataSetChanged();
            }
        }
    }

    public void setInfiniteRatio(int i) {
        this.mInfiniteRatio = i;
    }

    public void setSwipeable(boolean z) {
        this.mIsSwipeable = z;
    }
}
