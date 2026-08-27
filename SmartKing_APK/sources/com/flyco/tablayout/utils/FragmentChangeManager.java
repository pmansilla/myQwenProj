package com.flyco.tablayout.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class FragmentChangeManager {
    private int mContainerViewId;
    private int mCurrentTab;
    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;

    public FragmentChangeManager(FragmentManager fragmentManager, int i, ArrayList<Fragment> arrayList) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = i;
        this.mFragments = arrayList;
        initFragments();
    }

    private void initFragments() {
        Iterator<Fragment> it = this.mFragments.iterator();
        while (it.hasNext()) {
            Fragment next = it.next();
            this.mFragmentManager.beginTransaction().add(this.mContainerViewId, next).hide(next).commit();
        }
        setFragments(0);
    }

    public Fragment getCurrentFragment() {
        return this.mFragments.get(this.mCurrentTab);
    }

    public int getCurrentTab() {
        return this.mCurrentTab;
    }

    public void setFragments(int i) {
        for (int i2 = 0; i2 < this.mFragments.size(); i2++) {
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            Fragment fragment = this.mFragments.get(i2);
            if (i2 == i) {
                beginTransaction.show(fragment);
            } else {
                beginTransaction.hide(fragment);
            }
            beginTransaction.commit();
        }
        this.mCurrentTab = i;
    }
}
