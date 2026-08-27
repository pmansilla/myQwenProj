package cn.smssdk.gui.layout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class BackVerifyDialogLayout {
    public static LinearLayout create(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        linearLayout.setOrientation(1);
        TextView textView = new TextView(context);
        textView.setId(ResHelper.getIdRes(context, "tv_dialog_hint"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.topMargin = SizeHelper.fromPxWidth(context, 32);
        layoutParams.bottomMargin = SizeHelper.fromPxWidth(context, 32);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(SizeHelper.fromPxWidth(context, 18), 0, SizeHelper.fromPxWidth(context, 18), 0);
        textView.setLineSpacing(SizeHelper.fromPxWidth(context, 8), 1.0f);
        textView.setText(ResHelper.getStringRes(context, "smssdk_make_sure_mobile_detail"));
        textView.setTextColor(-1);
        textView.setTextSize(0, SizeHelper.fromPxWidth(context, 26));
        textView.setGravity(17);
        linearLayout.addView(textView);
        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, SizeHelper.fromPxWidth(context, 1)));
        view.setBackgroundColor(-9211021);
        linearLayout.addView(view);
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        Button button = new Button(context);
        button.setId(ResHelper.getIdRes(context, "btn_dialog_ok"));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, SizeHelper.fromPxWidth(context, 78), 1.0f);
        layoutParams2.leftMargin = SizeHelper.fromPxWidth(context, 3);
        button.setLayoutParams(layoutParams2);
        button.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_dialog_btn_back"));
        int fromPxWidth = SizeHelper.fromPxWidth(context, 8);
        button.setPadding(fromPxWidth, fromPxWidth, fromPxWidth, fromPxWidth);
        button.setText(ResHelper.getStringRes(context, "smssdk_ok"));
        button.setTextSize(0, SizeHelper.fromPxWidth(context, 22));
        button.setTextColor(-1);
        linearLayout2.addView(button);
        View view2 = new View(context);
        view2.setLayoutParams(new LinearLayout.LayoutParams(SizeHelper.fromPxWidth(context, 1), -1));
        view2.setBackgroundColor(-9211021);
        linearLayout2.addView(view2);
        Button button2 = new Button(context);
        button2.setId(ResHelper.getIdRes(context, "btn_dialog_cancel"));
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, SizeHelper.fromPxWidth(context, 78), 1.0f);
        layoutParams3.rightMargin = SizeHelper.fromPxWidth(context, 3);
        button2.setLayoutParams(layoutParams3);
        button2.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_dialog_btn_back"));
        button2.setPadding(fromPxWidth, fromPxWidth, fromPxWidth, fromPxWidth);
        button2.setText(ResHelper.getStringRes(context, "smssdk_cancel"));
        button2.setTextSize(0, SizeHelper.fromPxWidth(context, 22));
        button2.setTextColor(-1);
        linearLayout2.addView(button2);
        linearLayout.addView(linearLayout2);
        return linearLayout;
    }
}
