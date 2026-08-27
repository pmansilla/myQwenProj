package com.yanzhenjie.recyclerview.swipe.touch;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/* loaded from: classes2.dex */
public class DefaultItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private boolean isItemViewSwipeEnabled;
    private boolean isLongPressDragEnabled;
    private OnItemMoveListener onItemMoveListener;
    private OnItemMovementListener onItemMovementListener;
    private OnItemStateChangedListener onItemStateChangedListener;

    @Override // android.support.v7.widget.helper.ItemTouchHelper.Callback
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (this.onItemStateChangedListener != null) {
            this.onItemStateChangedListener.onSelectedChanged(viewHolder, 0);
        }
    }

    @Override // android.support.v7.widget.helper.ItemTouchHelper.Callback
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (this.onItemMovementListener != null) {
            return makeMovementFlags(this.onItemMovementListener.onDragFlags(recyclerView, viewHolder), this.onItemMovementListener.onSwipeFlags(recyclerView, viewHolder));
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        return layoutManager instanceof GridLayoutManager ? ((LinearLayoutManager) layoutManager).getOrientation() == 0 ? makeMovementFlags(15, 3) : makeMovementFlags(15, 12) : layoutManager instanceof LinearLayoutManager ? ((LinearLayoutManager) layoutManager).getOrientation() == 0 ? makeMovementFlags(12, 3) : makeMovementFlags(3, 12) : makeMovementFlags(0, 0);
    }

    public OnItemMoveListener getOnItemMoveListener() {
        return this.onItemMoveListener;
    }

    public OnItemMovementListener getOnItemMovementListener() {
        return this.onItemMovementListener;
    }

    public OnItemStateChangedListener getOnItemStateChangedListener() {
        return this.onItemStateChangedListener;
    }

    @Override // android.support.v7.widget.helper.ItemTouchHelper.Callback
    public boolean isItemViewSwipeEnabled() {
        return this.isItemViewSwipeEnabled;
    }

    @Override // android.support.v7.widget.helper.ItemTouchHelper.Callback
    public boolean isLongPressDragEnabled() {
        return this.isLongPressDragEnabled;
    }

    @Override // android.support.v7.widget.helper.ItemTouchHelper.Callback
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
        if (i == 1) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            float f3 = 1.0f;
            if (layoutManager instanceof LinearLayoutManager) {
                int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
                if (orientation == 0) {
                    f3 = 1.0f - (Math.abs(f2) / viewHolder.itemView.getHeight());
                } else if (orientation == 1) {
                    f3 = 1.0f - (Math.abs(f) / viewHolder.itemView.getWidth());
                }
            }
            viewHolder.itemView.setAlpha(f3);
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
    }

    @Override // android.support.v7.widget.helper.ItemTouchHelper.Callback
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        if (this.onItemMoveListener != null) {
            return this.onItemMoveListener.onItemMove(viewHolder, viewHolder2);
        }
        return false;
    }

    @Override // android.support.v7.widget.helper.ItemTouchHelper.Callback
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
        super.onSelectedChanged(viewHolder, i);
        if (this.onItemStateChangedListener == null || i == 0) {
            return;
        }
        this.onItemStateChangedListener.onSelectedChanged(viewHolder, i);
    }

    @Override // android.support.v7.widget.helper.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        if (this.onItemMoveListener != null) {
            this.onItemMoveListener.onItemDismiss(viewHolder);
        }
    }

    public void setItemViewSwipeEnabled(boolean z) {
        this.isItemViewSwipeEnabled = z;
    }

    public void setLongPressDragEnabled(boolean z) {
        this.isLongPressDragEnabled = z;
    }

    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        this.onItemMoveListener = onItemMoveListener;
    }

    public void setOnItemMovementListener(OnItemMovementListener onItemMovementListener) {
        this.onItemMovementListener = onItemMovementListener;
    }

    public void setOnItemStateChangedListener(OnItemStateChangedListener onItemStateChangedListener) {
        this.onItemStateChangedListener = onItemStateChangedListener;
    }
}
