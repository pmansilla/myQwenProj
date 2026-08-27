package com.qmuiteam.qmui.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.layout.QMUIRelativeLayout;
import com.qmuiteam.qmui.util.QMUIViewHelper;

/* loaded from: classes2.dex */
public class QMUIDialogMenuItemView extends QMUIRelativeLayout {
    private int index;
    private boolean mIsChecked;
    private MenuItemViewListener mListener;

    @SuppressLint({"ViewConstructor"})
    /* loaded from: classes2.dex */
    public static class CheckItemView extends QMUIDialogMenuItemView {
        private ImageView mCheckedView;
        private Context mContext;
        private TextView mTextView;

        public CheckItemView(Context context, boolean z) {
            super(context);
            this.mContext = context;
            this.mCheckedView = new ImageView(this.mContext);
            this.mCheckedView.setId(QMUIViewHelper.generateViewId());
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R.styleable.QMUIDialogMenuCheckDef, R.attr.qmui_dialog_menu_item_style, 0);
            int indexCount = obtainStyledAttributes.getIndexCount();
            int i = 0;
            for (int i2 = 0; i2 < indexCount; i2++) {
                int index = obtainStyledAttributes.getIndex(i2);
                if (index == R.styleable.QMUIDialogMenuCheckDef_qmui_dialog_menu_item_check_mark_margin_hor) {
                    i = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUIDialogMenuCheckDef_qmui_dialog_menu_item_check_drawable) {
                    this.mCheckedView.setImageDrawable(obtainStyledAttributes.getDrawable(index));
                }
            }
            obtainStyledAttributes.recycle();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(15, -1);
            if (z) {
                layoutParams.addRule(11, -1);
                layoutParams.leftMargin = i;
            } else {
                layoutParams.addRule(9, -1);
                layoutParams.rightMargin = i;
            }
            addView(this.mCheckedView, layoutParams);
            this.mTextView = createItemTextView(this.mContext);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
            if (z) {
                layoutParams2.addRule(0, this.mCheckedView.getId());
            } else {
                layoutParams2.addRule(1, this.mCheckedView.getId());
            }
            addView(this.mTextView, layoutParams2);
        }

        public CheckItemView(Context context, boolean z, CharSequence charSequence) {
            this(context, z);
            setText(charSequence);
        }

        public CharSequence getText() {
            return this.mTextView.getText();
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogMenuItemView
        protected void notifyCheckChange(boolean z) {
            QMUIViewHelper.safeSetImageViewSelected(this.mCheckedView, z);
        }

        public void setText(CharSequence charSequence) {
            this.mTextView.setText(charSequence);
        }
    }

    /* loaded from: classes2.dex */
    public static class MarkItemView extends QMUIDialogMenuItemView {
        private ImageView mCheckedView;
        private Context mContext;
        private TextView mTextView;

        public MarkItemView(Context context) {
            super(context);
            this.mContext = context;
            this.mCheckedView = new ImageView(this.mContext);
            this.mCheckedView.setId(QMUIViewHelper.generateViewId());
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R.styleable.QMUIDialogMenuMarkDef, R.attr.qmui_dialog_menu_item_style, 0);
            int indexCount = obtainStyledAttributes.getIndexCount();
            int i = 0;
            for (int i2 = 0; i2 < indexCount; i2++) {
                int index = obtainStyledAttributes.getIndex(i2);
                if (index == R.styleable.QMUIDialogMenuMarkDef_qmui_dialog_menu_item_check_mark_margin_hor) {
                    i = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                } else if (index == R.styleable.QMUIDialogMenuMarkDef_qmui_dialog_menu_item_mark_drawable) {
                    this.mCheckedView.setImageDrawable(obtainStyledAttributes.getDrawable(index));
                }
            }
            obtainStyledAttributes.recycle();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.addRule(15, -1);
            layoutParams.addRule(11, -1);
            layoutParams.leftMargin = i;
            addView(this.mCheckedView, layoutParams);
            this.mTextView = createItemTextView(this.mContext);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams2.addRule(9, -1);
            layoutParams2.addRule(0, this.mCheckedView.getId());
            addView(this.mTextView, layoutParams2);
        }

        public MarkItemView(Context context, CharSequence charSequence) {
            this(context);
            setText(charSequence);
        }

        @Override // com.qmuiteam.qmui.widget.dialog.QMUIDialogMenuItemView
        protected void notifyCheckChange(boolean z) {
            QMUIViewHelper.safeSetImageViewSelected(this.mCheckedView, z);
        }

        public void setText(CharSequence charSequence) {
            this.mTextView.setText(charSequence);
        }
    }

    /* loaded from: classes2.dex */
    public interface MenuItemViewListener {
        void onClick(int i);
    }

    /* loaded from: classes2.dex */
    public static class TextItemView extends QMUIDialogMenuItemView {
        protected TextView mTextView;

        public TextItemView(Context context) {
            super(context);
            init();
        }

        public TextItemView(Context context, CharSequence charSequence) {
            super(context);
            init();
            setText(charSequence);
        }

        private void init() {
            this.mTextView = createItemTextView(getContext());
            addView(this.mTextView, new RelativeLayout.LayoutParams(-1, -1));
        }

        public void setText(CharSequence charSequence) {
            this.mTextView.setText(charSequence);
        }

        public void setTextColor(int i) {
            this.mTextView.setTextColor(i);
        }
    }

    public QMUIDialogMenuItemView(Context context) {
        super(context, null, R.attr.qmui_dialog_menu_item_style);
        this.index = -1;
        this.mIsChecked = false;
    }

    public static TextView createItemTextView(Context context) {
        TextView textView = new TextView(context);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R.styleable.QMUIDialogMenuTextStyleDef, R.attr.qmui_dialog_menu_item_style, 0);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            if (index == R.styleable.QMUIDialogMenuTextStyleDef_android_gravity) {
                textView.setGravity(obtainStyledAttributes.getInt(index, -1));
            } else if (index == R.styleable.QMUIDialogMenuTextStyleDef_android_textColor) {
                textView.setTextColor(obtainStyledAttributes.getColorStateList(index));
            } else if (index == R.styleable.QMUIDialogMenuTextStyleDef_android_textSize) {
                textView.setTextSize(0, obtainStyledAttributes.getDimensionPixelSize(index, 0));
            }
        }
        obtainStyledAttributes.recycle();
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        textView.setDuplicateParentStateEnabled(false);
        return textView;
    }

    public int getMenuIndex() {
        return this.index;
    }

    public boolean isChecked() {
        return this.mIsChecked;
    }

    protected void notifyCheckChange(boolean z) {
    }

    @Override // android.view.View
    public boolean performClick() {
        if (this.mListener != null) {
            this.mListener.onClick(this.index);
        }
        return super.performClick();
    }

    public void setChecked(boolean z) {
        this.mIsChecked = z;
        notifyCheckChange(this.mIsChecked);
    }

    public void setListener(MenuItemViewListener menuItemViewListener) {
        if (!isClickable()) {
            setClickable(true);
        }
        this.mListener = menuItemViewListener;
    }

    public void setMenuIndex(int i) {
        this.index = i;
    }
}
