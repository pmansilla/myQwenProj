package com.wx.wheelview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.wx.wheelview.common.WheelData;
import com.wx.wheelview.widget.WheelItem;

/* loaded from: classes2.dex */
public class SimpleWheelAdapter extends BaseWheelAdapter<WheelData> {
    private Context mContext;

    public SimpleWheelAdapter(Context context) {
        this.mContext = context;
    }

    @Override // com.wx.wheelview.adapter.BaseWheelAdapter
    public View bindView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = new WheelItem(this.mContext);
        }
        WheelItem wheelItem = (WheelItem) view;
        wheelItem.setImage(((WheelData) this.mList.get(i)).getId());
        wheelItem.setText(((WheelData) this.mList.get(i)).getName());
        return view;
    }
}
