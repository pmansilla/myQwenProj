package com.qmuiteam.qmui.widget.popup;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import com.qmuiteam.qmui.widget.QMUIWrapContentListView;

/* loaded from: classes2.dex */
public class QMUIListPopup extends QMUIPopup {
    private BaseAdapter mAdapter;

    public QMUIListPopup(Context context, int i, BaseAdapter baseAdapter) {
        super(context, i);
        this.mAdapter = baseAdapter;
    }

    public void create(int i, int i2, AdapterView.OnItemClickListener onItemClickListener) {
        QMUIWrapContentListView qMUIWrapContentListView = new QMUIWrapContentListView(this.mContext, i2);
        qMUIWrapContentListView.setLayoutParams(new FrameLayout.LayoutParams(i, i2));
        qMUIWrapContentListView.setAdapter((ListAdapter) this.mAdapter);
        qMUIWrapContentListView.setVerticalScrollBarEnabled(false);
        qMUIWrapContentListView.setOnItemClickListener(onItemClickListener);
        qMUIWrapContentListView.setDivider(null);
        setContentView(qMUIWrapContentListView);
    }
}
