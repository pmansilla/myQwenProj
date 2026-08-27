package com.yanzhenjie.recyclerview.swipe;

import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;

/* loaded from: classes2.dex */
abstract class SwipeHorizontal {
    private int direction;
    protected Checker mChecker = new Checker();
    private View menuView;

    /* loaded from: classes2.dex */
    public static final class Checker {
        public boolean shouldResetSwipe;
        public int x;
        public int y;
    }

    public SwipeHorizontal(int i, View view) {
        this.direction = i;
        this.menuView = view;
    }

    public abstract void autoCloseMenu(OverScroller overScroller, int i, int i2);

    public abstract void autoOpenMenu(OverScroller overScroller, int i, int i2);

    public boolean canSwipe() {
        return (this.menuView instanceof ViewGroup) && ((ViewGroup) this.menuView).getChildCount() > 0;
    }

    public abstract Checker checkXY(int i, int i2);

    public int getDirection() {
        return this.direction;
    }

    public View getMenuView() {
        return this.menuView;
    }

    public int getMenuWidth() {
        return this.menuView.getWidth();
    }

    public abstract boolean isClickOnContentView(int i, float f);

    public boolean isCompleteClose(int i) {
        return i == 0 && (-getMenuView().getWidth()) * getDirection() != 0;
    }

    public abstract boolean isMenuOpen(int i);

    public abstract boolean isMenuOpenNotEqual(int i);
}
