package com.wx.wheelview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.wx.wheelview.util.WheelUtils;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseWheelAdapter<T> extends BaseAdapter {
    protected List<T> mList = null;
    protected boolean mLoop = false;
    protected int mWheelSize = 3;
    protected boolean mClickable = false;
    private int mCurrentPositon = -1;

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean areAllItemsEnabled() {
        return !this.mClickable;
    }

    protected abstract View bindView(int i, View view, ViewGroup viewGroup);

    @Override // android.widget.Adapter
    public final int getCount() {
        if (this.mLoop) {
            return Integer.MAX_VALUE;
        }
        if (WheelUtils.isEmpty(this.mList)) {
            return 0;
        }
        return (this.mList.size() + this.mWheelSize) - 1;
    }

    @Override // android.widget.Adapter
    public final T getItem(int i) {
        if (WheelUtils.isEmpty(this.mList)) {
            return null;
        }
        return this.mList.get(i % this.mList.size());
    }

    @Override // android.widget.Adapter
    public final long getItemId(int i) {
        if (!WheelUtils.isEmpty(this.mList)) {
            i %= this.mList.size();
        }
        return i;
    }

    @Override // android.widget.Adapter
    public final View getView(int i, View view, ViewGroup viewGroup) {
        int size = this.mLoop ? i % this.mList.size() : (i >= this.mWheelSize / 2 && i < (this.mWheelSize / 2) + this.mList.size()) ? i - (this.mWheelSize / 2) : -1;
        View bindView = size == -1 ? bindView(0, view, viewGroup) : bindView(size, view, viewGroup);
        if (!this.mLoop) {
            if (size == -1) {
                bindView.setVisibility(4);
            } else {
                bindView.setVisibility(0);
            }
        }
        return bindView;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int i) {
        if (this.mClickable) {
            return this.mLoop ? i % this.mList.size() == this.mCurrentPositon : i == this.mCurrentPositon + (this.mWheelSize / 2);
        }
        return false;
    }

    @Override // android.widget.BaseAdapter
    @Deprecated
    public final void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override // android.widget.BaseAdapter
    @Deprecated
    public final void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

    public final BaseWheelAdapter setClickable(boolean z) {
        if (z != this.mClickable) {
            this.mClickable = z;
            super.notifyDataSetChanged();
        }
        return this;
    }

    public final void setCurrentPosition(int i) {
        this.mCurrentPositon = i;
    }

    public final BaseWheelAdapter setData(List<T> list) {
        this.mList = list;
        super.notifyDataSetChanged();
        return this;
    }

    public final BaseWheelAdapter setLoop(boolean z) {
        if (z != this.mLoop) {
            this.mLoop = z;
            super.notifyDataSetChanged();
        }
        return this;
    }

    public final BaseWheelAdapter setWheelSize(int i) {
        this.mWheelSize = i;
        super.notifyDataSetChanged();
        return this;
    }
}
