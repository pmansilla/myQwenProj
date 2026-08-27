package com.wx.wheelview.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.common.WheelConstants;
import com.wx.wheelview.util.WheelUtils;
import com.wx.wheelview.widget.WheelView;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class WheelViewDialog<T> implements View.OnClickListener {
    private TextView mButton;
    private Context mContext;
    private AlertDialog mDialog;
    private View mLine1;
    private View mLine2;
    private OnDialogItemClickListener mOnDialogItemClickListener;
    private int mSelectedPos;
    private T mSelectedText;
    private WheelView.WheelViewStyle mStyle;
    private TextView mTitle;
    private WheelView<T> mWheelView;

    /* loaded from: classes2.dex */
    public interface OnDialogItemClickListener<T> {
        void onItemClick(int i, T t);
    }

    public WheelViewDialog(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(WheelUtils.dip2px(this.mContext, 20.0f), 0, WheelUtils.dip2px(this.mContext, 20.0f), 0);
        this.mTitle = new TextView(this.mContext);
        this.mTitle.setTextColor(WheelConstants.DIALOG_WHEEL_COLOR);
        this.mTitle.setTextSize(2, 16.0f);
        this.mTitle.setGravity(17);
        linearLayout.addView(this.mTitle, new LinearLayout.LayoutParams(-1, WheelUtils.dip2px(this.mContext, 50.0f)));
        this.mLine1 = new View(this.mContext);
        this.mLine1.setBackgroundColor(WheelConstants.DIALOG_WHEEL_COLOR);
        linearLayout.addView(this.mLine1, new LinearLayout.LayoutParams(-1, WheelUtils.dip2px(this.mContext, 2.0f)));
        this.mWheelView = new WheelView<>(this.mContext);
        this.mWheelView.setSkin(WheelView.Skin.Holo);
        this.mWheelView.setWheelAdapter(new ArrayWheelAdapter(this.mContext));
        this.mStyle = new WheelView.WheelViewStyle();
        this.mStyle.textColor = -7829368;
        this.mStyle.selectedTextZoom = 1.2f;
        this.mWheelView.setStyle(this.mStyle);
        this.mWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener<T>() { // from class: com.wx.wheelview.widget.WheelViewDialog.1
            @Override // com.wx.wheelview.widget.WheelView.OnWheelItemSelectedListener
            public void onItemSelected(int i, T t) {
                WheelViewDialog.this.mSelectedPos = i;
                WheelViewDialog.this.mSelectedText = t;
            }
        });
        linearLayout.addView(this.mWheelView, new ViewGroup.MarginLayoutParams(-1, -2));
        this.mLine2 = new View(this.mContext);
        this.mLine2.setBackgroundColor(WheelConstants.DIALOG_WHEEL_COLOR);
        linearLayout.addView(this.mLine2, new LinearLayout.LayoutParams(-1, WheelUtils.dip2px(this.mContext, 1.0f)));
        this.mButton = new TextView(this.mContext);
        this.mButton.setTextColor(WheelConstants.DIALOG_WHEEL_COLOR);
        this.mButton.setTextSize(2, 12.0f);
        this.mButton.setGravity(17);
        this.mButton.setClickable(true);
        this.mButton.setOnClickListener(this);
        this.mButton.setText("OK");
        linearLayout.addView(this.mButton, new LinearLayout.LayoutParams(-1, WheelUtils.dip2px(this.mContext, 45.0f)));
        this.mDialog = new AlertDialog.Builder(this.mContext).create();
        this.mDialog.setView(linearLayout);
        this.mDialog.setCanceledOnTouchOutside(false);
    }

    public WheelViewDialog dismiss() {
        if (this.mDialog.isShowing()) {
            this.mDialog.dismiss();
        }
        return this;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        dismiss();
        if (this.mOnDialogItemClickListener != null) {
            this.mOnDialogItemClickListener.onItemClick(this.mSelectedPos, this.mSelectedText);
        }
    }

    public WheelViewDialog setButtonColor(int i) {
        this.mButton.setTextColor(i);
        return this;
    }

    public WheelViewDialog setButtonSize(int i) {
        this.mButton.setTextSize(i);
        return this;
    }

    public WheelViewDialog setButtonText(String str) {
        this.mButton.setText(str);
        return this;
    }

    public WheelViewDialog setCount(int i) {
        this.mWheelView.setWheelSize(i);
        return this;
    }

    public WheelViewDialog setDialogStyle(int i) {
        this.mTitle.setTextColor(i);
        this.mLine1.setBackgroundColor(i);
        this.mLine2.setBackgroundColor(i);
        this.mButton.setTextColor(i);
        this.mStyle.selectedTextColor = i;
        this.mStyle.holoBorderColor = i;
        return this;
    }

    public WheelViewDialog setItems(List<T> list) {
        this.mWheelView.setWheelData(list);
        return this;
    }

    public WheelViewDialog setItems(T[] tArr) {
        return setItems(Arrays.asList(tArr));
    }

    public WheelViewDialog setLoop(boolean z) {
        this.mWheelView.setLoop(z);
        return this;
    }

    public WheelViewDialog setOnDialogItemClickListener(OnDialogItemClickListener onDialogItemClickListener) {
        this.mOnDialogItemClickListener = onDialogItemClickListener;
        return this;
    }

    public WheelViewDialog setSelection(int i) {
        this.mWheelView.setSelection(i);
        return this;
    }

    public WheelViewDialog setTextColor(int i) {
        this.mTitle.setTextColor(i);
        return this;
    }

    public WheelViewDialog setTextSize(int i) {
        this.mTitle.setTextSize(i);
        return this;
    }

    public WheelViewDialog setTitle(String str) {
        this.mTitle.setText(str);
        return this;
    }

    public WheelViewDialog show() {
        if (!this.mDialog.isShowing()) {
            this.mDialog.show();
        }
        return this;
    }
}
