package com.czw.smartkit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.czw.modes.dialog.RootDialog;

/* loaded from: classes.dex */
public class TipsDialog extends RootDialog {
    private Button btn;
    private TextView msg;

    public TipsDialog(@NonNull Context context) {
        super(context);
    }

    @Override // com.czw.modes.dialog.RootDialog
    protected void initView() {
        this.msg = (TextView) $View(R.id.msg);
        this.btn = (Button) $View(R.id.btn);
        this.btn.setOnClickListener(new View.OnClickListener() { // from class: com.czw.smartkit.TipsDialog.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TipsDialog.this.dismiss();
                TipsDialog.this.onDialogClose();
            }
        });
    }

    @Override // com.czw.modes.dialog.RootDialog
    protected int loadLayout() {
        return R.layout.dialog_tips;
    }

    public void onDialogClose() {
    }

    public TipsDialog showDialog(int i, int i2) {
        show();
        this.msg.setText(i);
        this.btn.setText(i2);
        return this;
    }
}
