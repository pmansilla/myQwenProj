package com.yanzhenjie.recyclerview.swipe;

import android.view.View;
import android.widget.OverScroller;
import com.yanzhenjie.recyclerview.swipe.SwipeHorizontal;

/* loaded from: classes2.dex */
class SwipeRightHorizontal extends SwipeHorizontal {
    public SwipeRightHorizontal(View view) {
        super(-1, view);
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeHorizontal
    public void autoCloseMenu(OverScroller overScroller, int i, int i2) {
        overScroller.startScroll(-Math.abs(i), 0, Math.abs(i), 0, i2);
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeHorizontal
    public void autoOpenMenu(OverScroller overScroller, int i, int i2) {
        overScroller.startScroll(Math.abs(i), 0, getMenuView().getWidth() - Math.abs(i), 0, i2);
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeHorizontal
    public SwipeHorizontal.Checker checkXY(int i, int i2) {
        this.mChecker.x = i;
        this.mChecker.y = i2;
        this.mChecker.shouldResetSwipe = false;
        if (this.mChecker.x == 0) {
            this.mChecker.shouldResetSwipe = true;
        }
        if (this.mChecker.x < 0) {
            this.mChecker.x = 0;
        }
        if (this.mChecker.x > getMenuView().getWidth()) {
            this.mChecker.x = getMenuView().getWidth();
        }
        return this.mChecker;
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeHorizontal
    public boolean isClickOnContentView(int i, float f) {
        return f < ((float) (i - getMenuView().getWidth()));
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeHorizontal
    public boolean isMenuOpen(int i) {
        int direction = (-getMenuView().getWidth()) * getDirection();
        return i >= direction && direction != 0;
    }

    @Override // com.yanzhenjie.recyclerview.swipe.SwipeHorizontal
    public boolean isMenuOpenNotEqual(int i) {
        return i > (-getMenuView().getWidth()) * getDirection();
    }
}
