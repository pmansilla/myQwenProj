package com.yanzhenjie.recyclerview.swipe;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/* loaded from: classes2.dex */
public final class SwipeMenuBridge {
    int mAdapterPosition;
    private final int mDirection;
    ImageView mImageView;
    private final int mPosition;
    private final SwipeSwitch mSwipeSwitch;
    TextView mTextView;
    private final View mViewRoot;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SwipeMenuBridge(int i, int i2, SwipeSwitch swipeSwitch, View view) {
        this.mDirection = i;
        this.mPosition = i2;
        this.mSwipeSwitch = swipeSwitch;
        this.mViewRoot = view;
    }

    public void closeMenu() {
        this.mSwipeSwitch.smoothCloseMenu();
    }

    public int getAdapterPosition() {
        return this.mAdapterPosition;
    }

    public int getDirection() {
        return this.mDirection;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public SwipeMenuBridge setBackgroundColor(@ColorInt int i) {
        this.mViewRoot.setBackgroundColor(i);
        return this;
    }

    public SwipeMenuBridge setBackgroundColorResource(@ColorRes int i) {
        return setBackgroundColor(ContextCompat.getColor(this.mViewRoot.getContext(), i));
    }

    public SwipeMenuBridge setBackgroundDrawable(@DrawableRes int i) {
        return setBackgroundDrawable(ContextCompat.getDrawable(this.mViewRoot.getContext(), i));
    }

    public SwipeMenuBridge setBackgroundDrawable(Drawable drawable) {
        ViewCompat.setBackground(this.mViewRoot, drawable);
        return this;
    }

    public SwipeMenuBridge setImage(int i) {
        return setImage(ContextCompat.getDrawable(this.mViewRoot.getContext(), i));
    }

    public SwipeMenuBridge setImage(Drawable drawable) {
        if (this.mImageView != null) {
            this.mImageView.setImageDrawable(drawable);
        }
        return this;
    }

    public SwipeMenuBridge setText(int i) {
        return setText(this.mViewRoot.getContext().getString(i));
    }

    public SwipeMenuBridge setText(String str) {
        if (this.mTextView != null) {
            this.mTextView.setText(str);
        }
        return this;
    }
}
