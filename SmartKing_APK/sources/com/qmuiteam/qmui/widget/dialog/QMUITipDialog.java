package com.qmuiteam.qmui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qmuiteam.qmui.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public class QMUITipDialog extends Dialog {

    /* loaded from: classes2.dex */
    public static class Builder {
        public static final int ICON_TYPE_FAIL = 3;
        public static final int ICON_TYPE_INFO = 4;
        public static final int ICON_TYPE_LOADING = 1;
        public static final int ICON_TYPE_NOTHING = 0;
        public static final int ICON_TYPE_SUCCESS = 2;
        private Context mContext;
        private int mCurrentIconType = 0;
        private CharSequence mTipWord;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface IconType {
        }

        public Builder(Context context) {
            this.mContext = context;
        }

        public QMUITipDialog create() {
            return create(true);
        }

        public QMUITipDialog create(boolean z) {
            QMUITipDialog qMUITipDialog = new QMUITipDialog(this.mContext);
            qMUITipDialog.setCancelable(z);
            qMUITipDialog.setContentView(R.layout.qmui_tip_dialog_layout);
            ViewGroup viewGroup = (ViewGroup) qMUITipDialog.findViewById(R.id.contentWrap);
            if (this.mCurrentIconType == 1) {
                QMUILoadingView qMUILoadingView = new QMUILoadingView(this.mContext);
                qMUILoadingView.setColor(-1);
                qMUILoadingView.setSize(QMUIDisplayHelper.dp2px(this.mContext, 32));
                qMUILoadingView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                viewGroup.addView(qMUILoadingView);
            } else if (this.mCurrentIconType == 2 || this.mCurrentIconType == 3 || this.mCurrentIconType == 4) {
                ImageView imageView = new ImageView(this.mContext);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                if (this.mCurrentIconType == 2) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this.mContext, R.drawable.qmui_icon_notify_done));
                } else if (this.mCurrentIconType == 3) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this.mContext, R.drawable.qmui_icon_notify_error));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(this.mContext, R.drawable.qmui_icon_notify_info));
                }
                viewGroup.addView(imageView);
            }
            if (this.mTipWord != null && this.mTipWord.length() > 0) {
                TextView textView = new TextView(this.mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                if (this.mCurrentIconType != 0) {
                    layoutParams.topMargin = QMUIDisplayHelper.dp2px(this.mContext, 12);
                }
                textView.setLayoutParams(layoutParams);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setGravity(17);
                textView.setMaxLines(2);
                textView.setTextColor(ContextCompat.getColor(this.mContext, R.color.qmui_config_color_white));
                textView.setTextSize(2, 14.0f);
                textView.setText(this.mTipWord);
                viewGroup.addView(textView);
            }
            return qMUITipDialog;
        }

        public Builder setIconType(int i) {
            this.mCurrentIconType = i;
            return this;
        }

        public Builder setTipWord(CharSequence charSequence) {
            this.mTipWord = charSequence;
            return this;
        }
    }

    /* loaded from: classes2.dex */
    public static class CustomBuilder {
        private int mContentLayoutId;
        private Context mContext;

        public CustomBuilder(Context context) {
            this.mContext = context;
        }

        public QMUITipDialog create() {
            QMUITipDialog qMUITipDialog = new QMUITipDialog(this.mContext);
            qMUITipDialog.setContentView(R.layout.qmui_tip_dialog_layout);
            LayoutInflater.from(this.mContext).inflate(this.mContentLayoutId, (ViewGroup) qMUITipDialog.findViewById(R.id.contentWrap), true);
            return qMUITipDialog;
        }

        public CustomBuilder setContent(@LayoutRes int i) {
            this.mContentLayoutId = i;
            return this;
        }
    }

    public QMUITipDialog(Context context) {
        this(context, R.style.QMUI_TipDialog);
    }

    public QMUITipDialog(Context context, int i) {
        super(context, i);
        setCanceledOnTouchOutside(false);
    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = -1;
            window.setAttributes(attributes);
        }
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initDialogWidth();
    }
}
