package com.qmuiteam.qmui.widget.dialog;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewStub;
import android.widget.TextView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.alpha.QMUIAlphaLinearLayout;

/* loaded from: classes2.dex */
public class QMUIBottomSheetItemView extends QMUIAlphaLinearLayout {
    private AppCompatImageView mAppCompatImageView;
    private ViewStub mSubScript;
    private TextView mTextView;

    public QMUIBottomSheetItemView(Context context) {
        super(context);
    }

    public QMUIBottomSheetItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public QMUIBottomSheetItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public AppCompatImageView getAppCompatImageView() {
        return this.mAppCompatImageView;
    }

    public ViewStub getSubScript() {
        return this.mSubScript;
    }

    public TextView getTextView() {
        return this.mTextView;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mAppCompatImageView = (AppCompatImageView) findViewById(R.id.grid_item_image);
        this.mSubScript = (ViewStub) findViewById(R.id.grid_item_subscript);
        this.mTextView = (TextView) findViewById(R.id.grid_item_title);
    }
}
