package cn.smssdk.gui;

import android.app.Dialog;
import android.content.Context;
import android.widget.LinearLayout;
import cn.smssdk.gui.layout.ProgressDialogLayout;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class CommonDialog {
    public static final Dialog ProgressDialog(Context context) {
        int styleRes = ResHelper.getStyleRes(context, "CommonDialog");
        if (styleRes <= 0) {
            return null;
        }
        Dialog dialog = new Dialog(context, styleRes);
        LinearLayout create = ProgressDialogLayout.create(context);
        if (create == null) {
            return null;
        }
        dialog.setContentView(create);
        return dialog;
    }
}
