package com.yanzhenjie.recyclerview.swipe;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/* loaded from: classes2.dex */
public class SwipeMenuView extends LinearLayout implements View.OnClickListener {
    private RecyclerView.ViewHolder mAdapterVIewHolder;
    private int mDirection;
    private SwipeMenuItemClickListener mItemClickListener;
    private SwipeSwitch mSwipeSwitch;

    public SwipeMenuView(Context context) {
        this(context, null);
    }

    public SwipeMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SwipeMenuView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private ImageView createIcon(SwipeMenuItem swipeMenuItem) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(swipeMenuItem.getImage());
        return imageView;
    }

    private TextView createTitle(SwipeMenuItem swipeMenuItem) {
        TextView textView = new TextView(getContext());
        textView.setText(swipeMenuItem.getText());
        textView.setGravity(17);
        int textSize = swipeMenuItem.getTextSize();
        if (textSize > 0) {
            textView.setTextSize(textSize);
        }
        ColorStateList titleColor = swipeMenuItem.getTitleColor();
        if (titleColor != null) {
            textView.setTextColor(titleColor);
        }
        int textAppearance = swipeMenuItem.getTextAppearance();
        if (textAppearance != 0) {
            TextViewCompat.setTextAppearance(textView, textAppearance);
        }
        Typeface textTypeface = swipeMenuItem.getTextTypeface();
        if (textTypeface != null) {
            textView.setTypeface(textTypeface);
        }
        return textView;
    }

    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
        this.mAdapterVIewHolder = viewHolder;
    }

    public void createMenu(SwipeMenu swipeMenu, SwipeSwitch swipeSwitch, SwipeMenuItemClickListener swipeMenuItemClickListener, int i) {
        removeAllViews();
        this.mSwipeSwitch = swipeSwitch;
        this.mItemClickListener = swipeMenuItemClickListener;
        this.mDirection = i;
        List<SwipeMenuItem> menuItems = swipeMenu.getMenuItems();
        for (int i2 = 0; i2 < menuItems.size(); i2++) {
            SwipeMenuItem swipeMenuItem = menuItems.get(i2);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(swipeMenuItem.getWidth(), swipeMenuItem.getHeight());
            layoutParams.weight = swipeMenuItem.getWeight();
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setId(i2);
            linearLayout.setGravity(17);
            linearLayout.setOrientation(1);
            linearLayout.setLayoutParams(layoutParams);
            ViewCompat.setBackground(linearLayout, swipeMenuItem.getBackground());
            linearLayout.setOnClickListener(this);
            addView(linearLayout);
            SwipeMenuBridge swipeMenuBridge = new SwipeMenuBridge(this.mDirection, i2, this.mSwipeSwitch, linearLayout);
            linearLayout.setTag(swipeMenuBridge);
            if (swipeMenuItem.getImage() != null) {
                ImageView createIcon = createIcon(swipeMenuItem);
                swipeMenuBridge.mImageView = createIcon;
                linearLayout.addView(createIcon);
            }
            if (!TextUtils.isEmpty(swipeMenuItem.getText())) {
                TextView createTitle = createTitle(swipeMenuItem);
                swipeMenuBridge.mTextView = createTitle;
                linearLayout.addView(createTitle);
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mItemClickListener == null || !this.mSwipeSwitch.isMenuOpen()) {
            return;
        }
        SwipeMenuBridge swipeMenuBridge = (SwipeMenuBridge) view.getTag();
        swipeMenuBridge.mAdapterPosition = this.mAdapterVIewHolder.getAdapterPosition();
        this.mItemClickListener.onItemClick(swipeMenuBridge);
    }
}
