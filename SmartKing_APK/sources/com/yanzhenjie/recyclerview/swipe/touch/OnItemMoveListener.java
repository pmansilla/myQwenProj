package com.yanzhenjie.recyclerview.swipe.touch;

import android.support.v7.widget.RecyclerView;

/* loaded from: classes2.dex */
public interface OnItemMoveListener {
    void onItemDismiss(RecyclerView.ViewHolder viewHolder);

    boolean onItemMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2);
}
