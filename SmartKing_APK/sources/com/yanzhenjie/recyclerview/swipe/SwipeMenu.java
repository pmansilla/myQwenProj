package com.yanzhenjie.recyclerview.swipe;

import android.content.Context;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SwipeMenu {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private SwipeMenuLayout mSwipeMenuLayout;
    private int mViewType;
    private int orientation = 0;
    private List<SwipeMenuItem> mSwipeMenuItems = new ArrayList(2);

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface OrientationMode {
    }

    public SwipeMenu(SwipeMenuLayout swipeMenuLayout, int i) {
        this.mSwipeMenuLayout = swipeMenuLayout;
        this.mViewType = i;
    }

    public void addMenuItem(SwipeMenuItem swipeMenuItem) {
        this.mSwipeMenuItems.add(swipeMenuItem);
    }

    public Context getContext() {
        return this.mSwipeMenuLayout.getContext();
    }

    public SwipeMenuItem getMenuItem(int i) {
        return this.mSwipeMenuItems.get(i);
    }

    public List<SwipeMenuItem> getMenuItems() {
        return this.mSwipeMenuItems;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public int getViewType() {
        return this.mViewType;
    }

    public void removeMenuItem(SwipeMenuItem swipeMenuItem) {
        this.mSwipeMenuItems.remove(swipeMenuItem);
    }

    public void setOpenPercent(float f) {
        if (f != this.mSwipeMenuLayout.getOpenPercent()) {
            if (f > 1.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = 0.0f;
            }
            this.mSwipeMenuLayout.setOpenPercent(f);
        }
    }

    public void setOrientation(int i) {
        if (i != 0 && i != 1) {
            throw new IllegalArgumentException("Use SwipeMenu#HORIZONTAL or SwipeMenu#VERTICAL.");
        }
        this.orientation = i;
    }

    public void setScrollerDuration(int i) {
        this.mSwipeMenuLayout.setScrollerDuration(i);
    }
}
