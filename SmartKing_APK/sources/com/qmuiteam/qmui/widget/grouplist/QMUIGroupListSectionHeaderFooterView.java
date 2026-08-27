package com.qmuiteam.qmui.widget.grouplist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUILangHelper;

/* loaded from: classes2.dex */
public class QMUIGroupListSectionHeaderFooterView extends LinearLayout {
    private TextView mTextView;

    public QMUIGroupListSectionHeaderFooterView(Context context) {
        this(context, (AttributeSet) null, R.attr.QMUIGroupListSectionViewStyle);
    }

    public QMUIGroupListSectionHeaderFooterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.QMUIGroupListSectionViewStyle);
    }

    public QMUIGroupListSectionHeaderFooterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public QMUIGroupListSectionHeaderFooterView(Context context, CharSequence charSequence) {
        this(context);
        setText(charSequence);
    }

    public QMUIGroupListSectionHeaderFooterView(Context context, CharSequence charSequence, boolean z) {
        this(context);
        if (z) {
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), 0);
        }
        setText(charSequence);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.qmui_group_list_section_layout, (ViewGroup) this, true);
        setGravity(80);
        this.mTextView = (TextView) findViewById(R.id.group_list_section_header_textView);
    }

    public TextView getTextView() {
        return this.mTextView;
    }

    public void setText(CharSequence charSequence) {
        if (QMUILangHelper.isNullOrEmpty(charSequence)) {
            this.mTextView.setVisibility(8);
        } else {
            this.mTextView.setVisibility(0);
        }
        this.mTextView.setText(charSequence);
    }

    public void setTextGravity(int i) {
        this.mTextView.setGravity(i);
    }
}
