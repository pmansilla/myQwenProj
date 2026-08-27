package com.czw.net;

import android.content.Context;
import android.support.annotation.NonNull;
import com.czw.R;
import com.czw.modes.dialog.RootDialog;

/* loaded from: classes.dex */
public class ProDialog extends RootDialog {
    public ProDialog(@NonNull Context context) {
        super(context);
    }

    @Override // com.czw.modes.dialog.RootDialog
    protected void initView() {
    }

    @Override // com.czw.modes.dialog.RootDialog
    protected int loadLayout() {
        return R.layout.dialog_progress;
    }
}
