package com.yanzhenjie.recyclerview.swipe.touch;

import android.support.v7.widget.RecyclerView;

/* loaded from: classes2.dex */
public interface OnItemStateChangedListener {
    public static final int ACTION_STATE_DRAG = 2;
    public static final int ACTION_STATE_IDLE = 0;
    public static final int ACTION_STATE_SWIPE = 1;

    void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i);
}
