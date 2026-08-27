package com.yanzhenjie.loading.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import com.yanzhenjie.loading.LoadingView;
import com.yanzhenjie.loading.R;

/* loaded from: classes2.dex */
public class LoadingDialog extends Dialog {
    private LoadingView mLoadingView;
    private TextView mTvMessage;

    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialog_Loading);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.loading_wait_dialog);
        this.mLoadingView = (LoadingView) findViewById(R.id.loading_view);
        this.mTvMessage = (TextView) findViewById(R.id.loading_tv_message);
    }

    public void setCircleColors(int i, int i2, int i3) {
        this.mLoadingView.setCircleColors(i, i2, i3);
    }

    public void setMessage(int i) {
        this.mTvMessage.setText(i);
    }

    public void setMessage(String str) {
        this.mTvMessage.setText(str);
    }
}
