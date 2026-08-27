package com.yanzhenjie.recyclerview.swipe;

/* loaded from: classes2.dex */
public interface SwipeSwitch {
    boolean isCompleteOpen();

    boolean isLeftCompleteOpen();

    boolean isLeftMenuOpen();

    boolean isLeftMenuOpenNotEqual();

    boolean isMenuOpen();

    boolean isMenuOpenNotEqual();

    boolean isRightCompleteOpen();

    boolean isRightMenuOpen();

    boolean isRightMenuOpenNotEqual();

    void smoothCloseLeftMenu();

    void smoothCloseMenu();

    void smoothCloseMenu(int i);

    void smoothCloseRightMenu();

    void smoothOpenLeftMenu();

    void smoothOpenLeftMenu(int i);

    void smoothOpenMenu();

    void smoothOpenRightMenu();

    void smoothOpenRightMenu(int i);
}
