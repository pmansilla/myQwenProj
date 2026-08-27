package com.yanzhenjie.recyclerview.swipe.touch;

import android.support.v7.widget.RecyclerView;

/* loaded from: classes2.dex */
public interface OnItemMovementListener {
    public static final int DOWN = 2;
    public static final int INVALID = 0;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int UP = 1;

    int onDragFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);

    int onSwipeFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
}
