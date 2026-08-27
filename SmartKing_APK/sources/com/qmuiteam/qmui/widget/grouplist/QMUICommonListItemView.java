package com.qmuiteam.qmui.widget.grouplist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.Space;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUILangHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public class QMUICommonListItemView extends RelativeLayout {
    public static final int ACCESSORY_TYPE_CHEVRON = 1;
    public static final int ACCESSORY_TYPE_CUSTOM = 3;
    public static final int ACCESSORY_TYPE_NONE = 0;
    public static final int ACCESSORY_TYPE_SWITCH = 2;
    public static final int HORIZONTAL = 1;
    public static final int REDDOT_POSITION_LEFT = 0;
    public static final int REDDOT_POSITION_RIGHT = 1;
    public static final int VERTICAL = 0;
    private int mAccessoryType;
    private ViewGroup mAccessoryView;
    protected TextView mDetailTextView;
    protected ImageView mImageView;
    private View mNewTip;
    private ViewStub mNewTipViewStub;
    private int mOrientation;
    private ImageView mRedDot;
    private int mRedDotPosition;
    protected CheckBox mSwitch;
    protected LinearLayout mTextContainer;
    protected Space mTextDetailSpace;
    protected TextView mTextView;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface QMUICommonListItemAccessoryType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface QMUICommonListItemOrientation {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface QMUICommonListItemRedDotPosition {
    }

    public QMUICommonListItemView(Context context) {
        this(context, null);
    }

    public QMUICommonListItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.QMUICommonListItemViewStyle);
    }

    public QMUICommonListItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOrientation = 1;
        this.mRedDotPosition = 0;
        init(context, attributeSet, i);
    }

    private ImageView getAccessoryImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(getAccessoryLayoutParams());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        return imageView;
    }

    private ViewGroup.LayoutParams getAccessoryLayoutParams() {
        return new ViewGroup.LayoutParams(-2, -2);
    }

    public void addAccessoryCustomView(View view) {
        if (this.mAccessoryType == 3) {
            this.mAccessoryView.addView(view);
        }
    }

    public ViewGroup getAccessoryContainerView() {
        return this.mAccessoryView;
    }

    public int getAccessoryType() {
        return this.mAccessoryType;
    }

    public CharSequence getDetailText() {
        return this.mDetailTextView.getText();
    }

    public TextView getDetailTextView() {
        return this.mDetailTextView;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public CheckBox getSwitch() {
        return this.mSwitch;
    }

    public CharSequence getText() {
        return this.mTextView.getText();
    }

    public TextView getTextView() {
        return this.mTextView;
    }

    protected void init(Context context, AttributeSet attributeSet, int i) {
        LayoutInflater.from(context).inflate(R.layout.qmui_common_list_item, (ViewGroup) this, true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUICommonListItemView, i, 0);
        int i2 = obtainStyledAttributes.getInt(R.styleable.QMUICommonListItemView_qmui_orientation, 1);
        int i3 = obtainStyledAttributes.getInt(R.styleable.QMUICommonListItemView_qmui_accessory_type, 0);
        int color = obtainStyledAttributes.getColor(R.styleable.QMUICommonListItemView_qmui_commonList_titleColor, QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_1));
        int color2 = obtainStyledAttributes.getColor(R.styleable.QMUICommonListItemView_qmui_commonList_detailColor, QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_5));
        obtainStyledAttributes.recycle();
        this.mImageView = (ImageView) findViewById(R.id.group_list_item_imageView);
        this.mTextContainer = (LinearLayout) findViewById(R.id.group_list_item_textContainer);
        this.mTextView = (TextView) findViewById(R.id.group_list_item_textView);
        this.mTextView.setTextColor(color);
        this.mRedDot = (ImageView) findViewById(R.id.group_list_item_tips_dot);
        this.mNewTipViewStub = (ViewStub) findViewById(R.id.group_list_item_tips_new);
        this.mDetailTextView = (TextView) findViewById(R.id.group_list_item_detailTextView);
        this.mTextDetailSpace = (Space) findViewById(R.id.group_list_item_space);
        this.mDetailTextView.setTextColor(color2);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDetailTextView.getLayoutParams();
        if (QMUIViewHelper.getIsLastLineSpacingExtraError()) {
            layoutParams.bottomMargin = -QMUIResHelper.getAttrDimen(context, R.attr.qmui_common_list_item_detail_line_space);
        }
        if (i2 == 0) {
            layoutParams.topMargin = QMUIDisplayHelper.dp2px(getContext(), 6);
        } else {
            layoutParams.topMargin = 0;
        }
        this.mAccessoryView = (ViewGroup) findViewById(R.id.group_list_item_accessoryView);
        setOrientation(i2);
        setAccessoryType(i3);
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int width;
        super.onLayout(z, i, i2, i3, i4);
        if (this.mRedDot != null && this.mRedDot.getVisibility() == 0) {
            int height = (getHeight() / 2) - (this.mRedDot.getMeasuredHeight() / 2);
            int left = this.mTextContainer.getLeft();
            if (this.mRedDotPosition == 0) {
                width = (int) (left + this.mTextView.getPaint().measureText(this.mTextView.getText().toString()) + QMUIDisplayHelper.dp2px(getContext(), 4));
            } else if (this.mRedDotPosition != 1) {
                return;
            } else {
                width = (left + this.mTextContainer.getWidth()) - this.mRedDot.getMeasuredWidth();
            }
            this.mRedDot.layout(width, height, this.mRedDot.getMeasuredWidth() + width, this.mRedDot.getMeasuredHeight() + height);
        }
        if (this.mNewTip == null || this.mNewTip.getVisibility() != 0) {
            return;
        }
        int left2 = (int) (this.mTextContainer.getLeft() + this.mTextView.getPaint().measureText(this.mTextView.getText().toString()) + QMUIDisplayHelper.dp2px(getContext(), 4));
        int height2 = (getHeight() / 2) - (this.mNewTip.getMeasuredHeight() / 2);
        this.mNewTip.layout(left2, height2, this.mNewTip.getMeasuredWidth() + left2, this.mNewTip.getMeasuredHeight() + height2);
    }

    public void setAccessoryType(int i) {
        this.mAccessoryView.removeAllViews();
        this.mAccessoryType = i;
        switch (i) {
            case 0:
                this.mAccessoryView.setVisibility(8);
                return;
            case 1:
                ImageView accessoryImageView = getAccessoryImageView();
                accessoryImageView.setImageDrawable(QMUIResHelper.getAttrDrawable(getContext(), R.attr.qmui_common_list_item_chevron));
                this.mAccessoryView.addView(accessoryImageView);
                this.mAccessoryView.setVisibility(0);
                return;
            case 2:
                if (this.mSwitch == null) {
                    this.mSwitch = new CheckBox(getContext());
                    this.mSwitch.setButtonDrawable(QMUIResHelper.getAttrDrawable(getContext(), R.attr.qmui_common_list_item_switch));
                    this.mSwitch.setLayoutParams(getAccessoryLayoutParams());
                    this.mSwitch.setClickable(false);
                    this.mSwitch.setEnabled(false);
                }
                this.mAccessoryView.addView(this.mSwitch);
                this.mAccessoryView.setVisibility(0);
                return;
            case 3:
                this.mAccessoryView.setVisibility(0);
                return;
            default:
                return;
        }
    }

    public void setDetailText(CharSequence charSequence) {
        this.mDetailTextView.setText(charSequence);
        if (QMUILangHelper.isNullOrEmpty(charSequence)) {
            this.mDetailTextView.setVisibility(8);
        } else {
            this.mDetailTextView.setVisibility(0);
        }
    }

    public void setImageDrawable(Drawable drawable) {
        if (drawable == null) {
            this.mImageView.setVisibility(8);
        } else {
            this.mImageView.setImageDrawable(drawable);
            this.mImageView.setVisibility(0);
        }
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mTextDetailSpace.getLayoutParams();
        if (this.mOrientation == 0) {
            this.mTextContainer.setOrientation(1);
            this.mTextContainer.setGravity(3);
            layoutParams.width = -2;
            layoutParams.height = QMUIDisplayHelper.dp2px(getContext(), 4);
            layoutParams.weight = 0.0f;
            this.mTextView.setTextSize(0, QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_common_list_item_title_v_text_size));
            this.mDetailTextView.setTextSize(0, QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_common_list_item_detail_v_text_size));
            return;
        }
        this.mTextContainer.setOrientation(0);
        this.mTextContainer.setGravity(16);
        layoutParams.width = 0;
        layoutParams.height = 0;
        layoutParams.weight = 1.0f;
        this.mTextView.setTextSize(0, QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_common_list_item_title_h_text_size));
        this.mDetailTextView.setTextSize(0, QMUIResHelper.getAttrDimen(getContext(), R.attr.qmui_common_list_item_detail_h_text_size));
    }

    public void setRedDotPosition(int i) {
        this.mRedDotPosition = i;
        requestLayout();
    }

    public void setText(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
        if (QMUILangHelper.isNullOrEmpty(charSequence)) {
            this.mTextView.setVisibility(8);
        } else {
            this.mTextView.setVisibility(0);
        }
    }

    public void showNewTip(boolean z) {
        if (z) {
            if (this.mNewTip == null) {
                this.mNewTip = this.mNewTipViewStub.inflate();
            }
            this.mNewTip.setVisibility(0);
            this.mRedDot.setVisibility(8);
            return;
        }
        if (this.mNewTip == null || this.mNewTip.getVisibility() != 0) {
            return;
        }
        this.mNewTip.setVisibility(8);
    }

    public void showRedDot(boolean z) {
        showRedDot(z, true);
    }

    public void showRedDot(boolean z, boolean z2) {
        this.mRedDot.setVisibility((z && z2) ? 0 : 8);
    }
}
