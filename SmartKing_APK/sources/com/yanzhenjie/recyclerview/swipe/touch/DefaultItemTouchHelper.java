package com.yanzhenjie.recyclerview.swipe.touch;

import android.support.v7.widget.helper.CompatItemTouchHelper;

/* loaded from: classes2.dex */
public class DefaultItemTouchHelper extends CompatItemTouchHelper {
    private DefaultItemTouchHelperCallback mDefaultItemTouchHelperCallback;

    public DefaultItemTouchHelper() {
        this(new DefaultItemTouchHelperCallback());
    }

    private DefaultItemTouchHelper(DefaultItemTouchHelperCallback defaultItemTouchHelperCallback) {
        super(defaultItemTouchHelperCallback);
        this.mDefaultItemTouchHelperCallback = (DefaultItemTouchHelperCallback) getCallback();
    }

    public OnItemMoveListener getOnItemMoveListener() {
        return this.mDefaultItemTouchHelperCallback.getOnItemMoveListener();
    }

    public OnItemMovementListener getOnItemMovementListener() {
        return this.mDefaultItemTouchHelperCallback.getOnItemMovementListener();
    }

    public OnItemStateChangedListener getOnItemStateChangedListener() {
        return this.mDefaultItemTouchHelperCallback.getOnItemStateChangedListener();
    }

    public boolean isItemViewSwipeEnabled() {
        return this.mDefaultItemTouchHelperCallback.isItemViewSwipeEnabled();
    }

    public boolean isLongPressDragEnabled() {
        return this.mDefaultItemTouchHelperCallback.isLongPressDragEnabled();
    }

    public void setItemViewSwipeEnabled(boolean z) {
        this.mDefaultItemTouchHelperCallback.setItemViewSwipeEnabled(z);
    }

    public void setLongPressDragEnabled(boolean z) {
        this.mDefaultItemTouchHelperCallback.setLongPressDragEnabled(z);
    }

    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener) {
        this.mDefaultItemTouchHelperCallback.setOnItemMoveListener(onItemMoveListener);
    }

    public void setOnItemMovementListener(OnItemMovementListener onItemMovementListener) {
        this.mDefaultItemTouchHelperCallback.setOnItemMovementListener(onItemMovementListener);
    }

    public void setOnItemStateChangedListener(OnItemStateChangedListener onItemStateChangedListener) {
        this.mDefaultItemTouchHelperCallback.setOnItemStateChangedListener(onItemStateChangedListener);
    }
}
