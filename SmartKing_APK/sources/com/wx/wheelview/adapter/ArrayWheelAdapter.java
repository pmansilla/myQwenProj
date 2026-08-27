package com.wx.wheelview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.wx.wheelview.widget.WheelItem;

/* loaded from: classes2.dex */
public class ArrayWheelAdapter<T> extends BaseWheelAdapter<T> {
    private Context mContext;

    public ArrayWheelAdapter(Context context) {
        this.mContext = context;
    }

    @Override // com.wx.wheelview.adapter.BaseWheelAdapter
    public View bindView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = new WheelItem(this.mContext);
        }
        WheelItem wheelItem = (WheelItem) view;
        T item = getItem(i);
        if (wheelItem instanceof CharSequence) {
            wheelItem.setText((CharSequence) item);
        } else {
            wheelItem.setText(item.toString());
        }
        return view;
    }
}
