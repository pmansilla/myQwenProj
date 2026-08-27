package com.wx.wheelview.widget;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wx.wheelview.util.WheelUtils;

/* loaded from: classes2.dex */
public class WheelItem extends FrameLayout {
    private ImageView mImage;
    private TextView mText;

    public WheelItem(Context context) {
        super(context);
        init();
    }

    public WheelItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public WheelItem(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, WheelUtils.dip2px(getContext(), 45.0f));
        linearLayout.setOrientation(0);
        linearLayout.setPadding(20, 20, 20, 20);
        linearLayout.setGravity(17);
        addView(linearLayout, layoutParams);
        this.mImage = new ImageView(getContext());
        this.mImage.setTag(100);
        this.mImage.setVisibility(8);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, -2);
        layoutParams2.rightMargin = 20;
        linearLayout.addView(this.mImage, layoutParams2);
        this.mText = new TextView(getContext());
        this.mText.setTag(101);
        this.mText.setEllipsize(TextUtils.TruncateAt.END);
        this.mText.setSingleLine();
        this.mText.setIncludeFontPadding(false);
        this.mText.setGravity(17);
        this.mText.setTextColor(-16777216);
        linearLayout.addView(this.mText, new FrameLayout.LayoutParams(-1, -1));
    }

    public void setImage(int i) {
        this.mImage.setVisibility(0);
        this.mImage.setImageResource(i);
    }

    public void setText(CharSequence charSequence) {
        this.mText.setText(Html.fromHtml(charSequence.toString()));
    }
}
