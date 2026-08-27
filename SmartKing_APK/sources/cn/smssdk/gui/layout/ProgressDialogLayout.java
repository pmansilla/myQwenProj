package cn.smssdk.gui.layout;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/* loaded from: classes.dex */
public class ProgressDialogLayout {
    public static LinearLayout create(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        linearLayout.setOrientation(1);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        int fromPxWidth = SizeHelper.fromPxWidth(context, 20);
        progressBar.setPadding(fromPxWidth, fromPxWidth, fromPxWidth, fromPxWidth);
        linearLayout.addView(progressBar);
        return linearLayout;
    }
}
