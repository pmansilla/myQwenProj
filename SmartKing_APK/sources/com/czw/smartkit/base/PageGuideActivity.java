package com.czw.smartkit.base;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.czw.smartkit.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class PageGuideActivity extends BaseActivity {
    protected ViewPagerAdapter adapter;
    protected LinearLayout pointsLayout;
    protected ViewPager viewPager;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> pages;

        public ViewPagerAdapter(ArrayList<View> arrayList) {
            this.pages = null;
            this.pages = arrayList;
        }

        @Override // android.support.v4.view.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView(this.pages.get(i));
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return this.pages.size();
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            viewGroup.addView(this.pages.get(i));
            return this.pages.get(i);
        }

        @Override // android.support.v4.view.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
    }

    private void initPagers() {
        this.adapter = new ViewPagerAdapter(loadPages());
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.czw.smartkit.base.PageGuideActivity.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                if (i == PageGuideActivity.this.getPointCount() - 1) {
                    PageGuideActivity.this.onLastSelect();
                }
            }
        });
    }

    protected abstract int getPointCount();

    @Override // com.czw.smartkit.base.BaseActivity
    public void initView() {
        this.viewPager = (ViewPager) $View(R.id.viewPager);
        this.pointsLayout = (LinearLayout) $View(R.id.pointsLayout);
        initPagers();
    }

    @Override // com.czw.smartkit.base.BaseActivity
    public int loadLayout() {
        return R.layout.controller_guide;
    }

    protected abstract ArrayList<View> loadPages();

    public abstract void onLastSelect();
}
