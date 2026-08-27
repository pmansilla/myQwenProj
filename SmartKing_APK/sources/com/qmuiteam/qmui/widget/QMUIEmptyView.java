package com.qmuiteam.qmui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.qmuiteam.qmui.R;

/* loaded from: classes2.dex */
public class QMUIEmptyView extends FrameLayout {
    protected Button mButton;
    private TextView mDetailTextView;
    private QMUILoadingView mLoadingView;
    private TextView mTitleTextView;

    public QMUIEmptyView(Context context) {
        this(context, null);
    }

    public QMUIEmptyView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QMUIEmptyView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.QMUIEmptyView);
        Boolean valueOf = Boolean.valueOf(obtainStyledAttributes.getBoolean(R.styleable.QMUIEmptyView_qmui_show_loading, false));
        String string = obtainStyledAttributes.getString(R.styleable.QMUIEmptyView_qmui_title_text);
        String string2 = obtainStyledAttributes.getString(R.styleable.QMUIEmptyView_qmui_detail_text);
        String string3 = obtainStyledAttributes.getString(R.styleable.QMUIEmptyView_qmui_btn_text);
        obtainStyledAttributes.recycle();
        show(valueOf.booleanValue(), string, string2, string3, null);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.qmui_empty_view, (ViewGroup) this, true);
        this.mLoadingView = (QMUILoadingView) findViewById(R.id.empty_view_loading);
        this.mTitleTextView = (TextView) findViewById(R.id.empty_view_title);
        this.mDetailTextView = (TextView) findViewById(R.id.empty_view_detail);
        this.mButton = (Button) findViewById(R.id.empty_view_button);
    }

    public void hide() {
        setVisibility(8);
        setLoadingShowing(false);
        setTitleText(null);
        setDetailText(null);
        setButton(null, null);
    }

    public boolean isLoading() {
        return this.mLoadingView.getVisibility() == 0;
    }

    public boolean isShowing() {
        return getVisibility() == 0;
    }

    public void setButton(String str, View.OnClickListener onClickListener) {
        this.mButton.setText(str);
        this.mButton.setVisibility(str != null ? 0 : 8);
        this.mButton.setOnClickListener(onClickListener);
    }

    public void setDetailColor(int i) {
        this.mDetailTextView.setTextColor(i);
    }

    public void setDetailText(String str) {
        this.mDetailTextView.setText(str);
        this.mDetailTextView.setVisibility(str != null ? 0 : 8);
    }

    public void setLoadingShowing(boolean z) {
        this.mLoadingView.setVisibility(z ? 0 : 8);
    }

    public void setTitleColor(int i) {
        this.mTitleTextView.setTextColor(i);
    }

    public void setTitleText(String str) {
        this.mTitleTextView.setText(str);
        this.mTitleTextView.setVisibility(str != null ? 0 : 8);
    }

    public void show() {
        setVisibility(0);
    }

    public void show(String str, String str2) {
        setLoadingShowing(false);
        setTitleText(str);
        setDetailText(str2);
        setButton(null, null);
        show();
    }

    public void show(boolean z) {
        setLoadingShowing(z);
        setTitleText(null);
        setDetailText(null);
        setButton(null, null);
        show();
    }

    public void show(boolean z, String str, String str2, String str3, View.OnClickListener onClickListener) {
        setLoadingShowing(z);
        setTitleText(str);
        setDetailText(str2);
        setButton(str3, onClickListener);
        show();
    }
}
