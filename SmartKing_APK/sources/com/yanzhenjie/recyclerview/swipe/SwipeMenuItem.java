package com.yanzhenjie.recyclerview.swipe;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Px;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;

/* loaded from: classes2.dex */
public class SwipeMenuItem {
    private Drawable background;
    private Drawable icon;
    private Context mContext;
    private int textAppearance;
    private Typeface textTypeface;
    private String title;
    private ColorStateList titleColor;
    private int titleSize;
    private int width = -2;
    private int height = -2;
    private int weight = 0;

    public SwipeMenuItem(Context context) {
        this.mContext = context;
    }

    public Drawable getBackground() {
        return this.background;
    }

    public int getHeight() {
        return this.height;
    }

    public Drawable getImage() {
        return this.icon;
    }

    public String getText() {
        return this.title;
    }

    public int getTextAppearance() {
        return this.textAppearance;
    }

    public int getTextSize() {
        return this.titleSize;
    }

    public Typeface getTextTypeface() {
        return this.textTypeface;
    }

    public ColorStateList getTitleColor() {
        return this.titleColor;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getWidth() {
        return this.width;
    }

    public SwipeMenuItem setBackground(@DrawableRes int i) {
        return setBackground(ContextCompat.getDrawable(this.mContext, i));
    }

    public SwipeMenuItem setBackground(Drawable drawable) {
        this.background = drawable;
        return this;
    }

    public SwipeMenuItem setBackgroundColor(@ColorInt int i) {
        this.background = new ColorDrawable(i);
        return this;
    }

    public SwipeMenuItem setBackgroundColorResource(@ColorRes int i) {
        return setBackgroundColor(ContextCompat.getColor(this.mContext, i));
    }

    public SwipeMenuItem setHeight(int i) {
        this.height = i;
        return this;
    }

    public SwipeMenuItem setImage(int i) {
        return setImage(ContextCompat.getDrawable(this.mContext, i));
    }

    public SwipeMenuItem setImage(Drawable drawable) {
        this.icon = drawable;
        return this;
    }

    public SwipeMenuItem setText(int i) {
        return setText(this.mContext.getString(i));
    }

    public SwipeMenuItem setText(String str) {
        this.title = str;
        return this;
    }

    public SwipeMenuItem setTextAppearance(@StyleRes int i) {
        this.textAppearance = i;
        return this;
    }

    public SwipeMenuItem setTextColor(@ColorInt int i) {
        this.titleColor = ColorStateList.valueOf(i);
        return this;
    }

    public SwipeMenuItem setTextColorResource(@ColorRes int i) {
        return setTextColor(ContextCompat.getColor(this.mContext, i));
    }

    public SwipeMenuItem setTextSize(@Px int i) {
        this.titleSize = i;
        return this;
    }

    public SwipeMenuItem setTextTypeface(Typeface typeface) {
        this.textTypeface = typeface;
        return this;
    }

    public SwipeMenuItem setWeight(int i) {
        this.weight = i;
        return this;
    }

    public SwipeMenuItem setWidth(int i) {
        this.width = i;
        return this;
    }
}
