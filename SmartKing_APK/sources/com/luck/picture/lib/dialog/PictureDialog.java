package com.luck.picture.lib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.luck.picture.lib.R;

/* loaded from: classes.dex */
public class PictureDialog extends Dialog {
    public Context context;

    public PictureDialog(Context context) {
        super(context, R.style.picture_alert_dialog);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        getWindow().setWindowAnimations(R.style.DialogWindowStyle);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.picture_alert_dialog);
    }
}
