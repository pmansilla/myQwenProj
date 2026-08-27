package com.czw.modes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;
import com.czw.R;

/* loaded from: classes.dex */
public abstract class RootDialog extends Dialog {
    private Context context;

    public RootDialog(@NonNull Context context) {
        super(context, R.style.dialog_style);
        _init_(context);
    }

    private void _init_(Context context) {
        this.context = context;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T extends View> T $View(int i) {
        return (T) findViewById(i);
    }

    protected String $str(int i) {
        return this.context.getString(i);
    }

    protected void closeDialog() {
        cancel();
        onCancel();
    }

    protected abstract void initView();

    protected abstract int loadLayout();

    public void onCancel() {
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(loadLayout());
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        initView();
    }

    protected RootDialog showDialog() {
        show();
        return this;
    }

    public final void toast(int i) {
        toast(getContext().getString(i));
    }

    public final void toast(String str) {
        Toast.makeText(getContext(), str, 1).show();
    }
}
