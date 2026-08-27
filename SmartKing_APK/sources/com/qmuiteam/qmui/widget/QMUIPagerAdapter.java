package com.qmuiteam.qmui.widget;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/* loaded from: classes2.dex */
public abstract class QMUIPagerAdapter extends PagerAdapter {
    private SparseArray<Object> mScrapItems = new SparseArray<>();

    protected abstract void destroy(ViewGroup viewGroup, int i, Object obj);

    @Override // android.support.v4.view.PagerAdapter
    public final void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        destroy(viewGroup, i, obj);
        this.mScrapItems.put(i, obj);
    }

    protected abstract Object hydrate(ViewGroup viewGroup, int i);

    @Override // android.support.v4.view.PagerAdapter
    public final Object instantiateItem(ViewGroup viewGroup, int i) {
        Object obj = this.mScrapItems.get(i);
        if (obj == null) {
            obj = hydrate(viewGroup, i);
        } else {
            this.mScrapItems.remove(i);
        }
        populate(viewGroup, obj, i);
        return obj;
    }

    protected abstract void populate(ViewGroup viewGroup, Object obj, int i);
}
