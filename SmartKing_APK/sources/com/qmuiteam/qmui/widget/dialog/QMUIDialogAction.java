package com.qmuiteam.qmui.widget.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.util.QMUISpanHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public class QMUIDialogAction {
    public static final int ACTION_PROP_NEGATIVE = 2;
    public static final int ACTION_PROP_NEUTRAL = 1;
    public static final int ACTION_PROP_POSITIVE = 0;
    private int mActionProp;
    private QMUIButton mButton;
    private Context mContext;
    private int mIconRes;
    private boolean mIsEnabled;
    private ActionListener mOnClickListener;
    private CharSequence mStr;

    /* loaded from: classes2.dex */
    public interface ActionListener {
        void onClick(QMUIDialog qMUIDialog, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Prop {
    }

    public QMUIDialogAction(Context context, int i, int i2, ActionListener actionListener) {
        this.mIsEnabled = true;
        this.mContext = context;
        this.mStr = this.mContext.getResources().getString(i);
        this.mActionProp = i2;
        this.mOnClickListener = actionListener;
    }

    public QMUIDialogAction(Context context, int i, ActionListener actionListener) {
        this(context, context.getResources().getString(i), 1, actionListener);
    }

    public QMUIDialogAction(Context context, int i, CharSequence charSequence, int i2, ActionListener actionListener) {
        this.mIsEnabled = true;
        this.mContext = context;
        this.mIconRes = i;
        this.mStr = charSequence;
        this.mActionProp = i2;
        this.mOnClickListener = actionListener;
    }

    public QMUIDialogAction(Context context, CharSequence charSequence, int i, ActionListener actionListener) {
        this.mIsEnabled = true;
        this.mContext = context;
        this.mStr = charSequence;
        this.mActionProp = i;
        this.mOnClickListener = actionListener;
    }

    public QMUIDialogAction(Context context, String str, ActionListener actionListener) {
        this(context, str, 1, actionListener);
    }

    private QMUIButton generateActionButton(Context context, CharSequence charSequence, int i) {
        QMUIButton qMUIButton = new QMUIButton(context);
        qMUIButton.setBackground(null);
        qMUIButton.setMinHeight(0);
        qMUIButton.setMinimumHeight(0);
        qMUIButton.setChangeAlphaWhenDisable(true);
        qMUIButton.setChangeAlphaWhenPress(true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R.styleable.QMUIDialogActionStyleDef, R.attr.qmui_dialog_action_style, 0);
        int indexCount = obtainStyledAttributes.getIndexCount();
        ColorStateList colorStateList = null;
        ColorStateList colorStateList2 = null;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < indexCount; i4++) {
            int index = obtainStyledAttributes.getIndex(i4);
            if (index == R.styleable.QMUIDialogActionStyleDef_android_gravity) {
                qMUIButton.setGravity(obtainStyledAttributes.getInt(index, -1));
            } else if (index == R.styleable.QMUIDialogActionStyleDef_android_textColor) {
                qMUIButton.setTextColor(obtainStyledAttributes.getColorStateList(index));
            } else if (index == R.styleable.QMUIDialogActionStyleDef_android_textSize) {
                qMUIButton.setTextSize(0, obtainStyledAttributes.getDimensionPixelSize(index, 0));
            } else if (index == R.styleable.QMUIDialogActionStyleDef_qmui_dialog_action_button_padding_horizontal) {
                i2 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == R.styleable.QMUIDialogActionStyleDef_android_background) {
                qMUIButton.setBackground(obtainStyledAttributes.getDrawable(index));
            } else if (index == R.styleable.QMUIDialogActionStyleDef_android_minWidth) {
                int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                qMUIButton.setMinWidth(dimensionPixelSize);
                qMUIButton.setMinimumWidth(dimensionPixelSize);
            } else if (index == R.styleable.QMUIDialogActionStyleDef_qmui_dialog_positive_action_text_color) {
                colorStateList2 = obtainStyledAttributes.getColorStateList(index);
            } else if (index == R.styleable.QMUIDialogActionStyleDef_qmui_dialog_negative_action_text_color) {
                colorStateList = obtainStyledAttributes.getColorStateList(index);
            } else if (index == R.styleable.QMUIDialogActionStyleDef_qmui_dialog_action_icon_space) {
                i3 = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            }
        }
        obtainStyledAttributes.recycle();
        qMUIButton.setPadding(i2, 0, i2, 0);
        if (i <= 0) {
            qMUIButton.setText(charSequence);
        } else {
            qMUIButton.setText(QMUISpanHelper.generateSideIconText(true, i3, charSequence, ContextCompat.getDrawable(context, i)));
        }
        qMUIButton.setClickable(true);
        qMUIButton.setEnabled(this.mIsEnabled);
        if (this.mActionProp == 2) {
            qMUIButton.setTextColor(colorStateList);
        } else if (this.mActionProp == 0) {
            qMUIButton.setTextColor(colorStateList2);
        }
        return qMUIButton;
    }

    public QMUIButton buildActionView(final QMUIDialog qMUIDialog, final int i) {
        this.mButton = generateActionButton(qMUIDialog.getContext(), this.mStr, this.mIconRes);
        this.mButton.setOnClickListener(new View.OnClickListener() { // from class: com.qmuiteam.qmui.widget.dialog.QMUIDialogAction.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (QMUIDialogAction.this.mOnClickListener == null || !QMUIDialogAction.this.mButton.isEnabled()) {
                    return;
                }
                QMUIDialogAction.this.mOnClickListener.onClick(qMUIDialog, i);
            }
        });
        return this.mButton;
    }

    public int getActionProp() {
        return this.mActionProp;
    }

    public void setEnabled(boolean z) {
        this.mIsEnabled = z;
        if (this.mButton != null) {
            this.mButton.setEnabled(z);
        }
    }

    public void setOnClickListener(ActionListener actionListener) {
        this.mOnClickListener = actionListener;
    }
}
