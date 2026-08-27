package com.qmuiteam.qmui.widget;

import android.support.v4.util.Pools;
import android.view.View;
import android.view.ViewGroup;
import com.qmuiteam.qmui.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class QMUIItemViewsAdapter<T, V extends View> {
    private Pools.Pool<V> mCachePool;
    private ViewGroup mParentView;
    private List<T> mItemData = new ArrayList();
    private List<V> mViews = new ArrayList();

    public QMUIItemViewsAdapter(ViewGroup viewGroup) {
        this.mParentView = viewGroup;
    }

    private V getView() {
        V acquire = this.mCachePool != null ? this.mCachePool.acquire() : null;
        return acquire == null ? createView(this.mParentView) : acquire;
    }

    public QMUIItemViewsAdapter<T, V> addItem(T t) {
        this.mItemData.add(t);
        return this;
    }

    protected abstract void bind(T t, V v, int i);

    public void clear() {
        this.mItemData.clear();
        detach(this.mViews.size());
    }

    protected abstract V createView(ViewGroup viewGroup);

    public void detach(int i) {
        int size = this.mViews.size();
        while (size > 0 && i > 0) {
            V remove = this.mViews.remove(size - 1);
            if (this.mCachePool == null) {
                this.mCachePool = new Pools.SimplePool(12);
            }
            Object tag = remove.getTag(R.id.qmui_view_can_not_cache_tag);
            if (tag == null || !((Boolean) tag).booleanValue()) {
                try {
                    this.mCachePool.release(remove);
                } catch (Exception unused) {
                }
            }
            this.mParentView.removeView(remove);
            size--;
            i--;
        }
    }

    public T getItem(int i) {
        if (this.mItemData != null && i >= 0 && i < this.mItemData.size()) {
            return this.mItemData.get(i);
        }
        return null;
    }

    public int getSize() {
        if (this.mItemData == null) {
            return 0;
        }
        return this.mItemData.size();
    }

    public List<V> getViews() {
        return this.mViews;
    }

    public void replaceItem(int i, T t) throws IllegalAccessException {
        if (i >= this.mItemData.size() || i < 0) {
            throw new IllegalAccessException("替换数据不存在");
        }
        this.mItemData.set(i, t);
    }

    public void setup() {
        int size = this.mItemData.size();
        int size2 = this.mViews.size();
        if (size2 > size) {
            detach(size2 - size);
        } else if (size2 < size) {
            for (int i = 0; i < size - size2; i++) {
                V view = getView();
                this.mParentView.addView(view);
                this.mViews.add(view);
            }
        }
        for (int i2 = 0; i2 < size; i2++) {
            bind(this.mItemData.get(i2), this.mViews.get(i2), i2);
        }
        this.mParentView.invalidate();
        this.mParentView.requestLayout();
    }
}
