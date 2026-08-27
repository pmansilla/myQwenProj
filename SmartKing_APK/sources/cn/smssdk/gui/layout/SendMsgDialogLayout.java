package cn.smssdk.gui.layout;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mob.tools.utils.ResHelper;

@Deprecated
/* loaded from: classes.dex */
public class SendMsgDialogLayout {
    public static LinearLayout create(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        linearLayout.setOrientation(1);
        TextView textView = new TextView(context);
        textView.setId(ResHelper.getIdRes(context, "tv_dialog_title"));
        textView.setLayoutParams(new LinearLayout.LayoutParams(-2, SizeHelper.fromPxWidth(context, 92)));
        textView.setPadding(SizeHelper.fromPxWidth(context, 20), SizeHelper.fromPxWidth(context, 20), SizeHelper.fromPxWidth(context, 20), SizeHelper.fromPxWidth(context, 20));
        textView.setText(ResHelper.getStringRes(context, "smssdk_make_sure_mobile_num"));
        textView.setTextColor(-12801001);
        textView.setTextSize(0, SizeHelper.fromPxWidth(context, 32));
        textView.setGravity(16);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        linearLayout.addView(textView);
        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, SizeHelper.fromPxWidth(context, 1)));
        view.setBackgroundColor(-12801001);
        linearLayout.addView(view);
        TextView textView2 = new TextView(context);
        textView2.setId(ResHelper.getIdRes(context, "tv_dialog_hint"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.topMargin = SizeHelper.fromPxWidth(context, 28);
        textView2.setLayoutParams(layoutParams);
        textView2.setPadding(SizeHelper.fromPxWidth(context, 18), 0, SizeHelper.fromPxWidth(context, 18), 0);
        textView2.setText(ResHelper.getStringRes(context, "smssdk_make_sure_mobile_detail"));
        textView2.setTextColor(-1);
        textView2.setTextSize(0, SizeHelper.fromPxWidth(context, 24));
        linearLayout.addView(textView2);
        TextView textView3 = new TextView(context);
        textView3.setId(ResHelper.getIdRes(context, "tv_phone"));
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.bottomMargin = SizeHelper.fromPxWidth(context, 26);
        textView3.setLayoutParams(layoutParams2);
        textView3.setPadding(SizeHelper.fromPxWidth(context, 18), 0, SizeHelper.fromPxWidth(context, 18), 0);
        textView3.setTextColor(-1);
        textView3.setTextSize(0, SizeHelper.fromPxWidth(context, 24));
        linearLayout.addView(textView3);
        View view2 = new View(context);
        view2.setLayoutParams(new LinearLayout.LayoutParams(-1, SizeHelper.fromPxWidth(context, 1)));
        view2.setBackgroundColor(-9211021);
        linearLayout.addView(view2);
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        Button button = new Button(context);
        button.setId(ResHelper.getIdRes(context, "btn_dialog_ok"));
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, SizeHelper.fromPxWidth(context, 80), 1.0f);
        layoutParams3.rightMargin = SizeHelper.fromPxWidth(context, 1);
        button.setLayoutParams(layoutParams3);
        button.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_dialog_btn_back"));
        int fromPxWidth = SizeHelper.fromPxWidth(context, 18);
        button.setPadding(fromPxWidth, fromPxWidth, fromPxWidth, fromPxWidth);
        button.setText(ResHelper.getStringRes(context, "smssdk_ok"));
        button.setTextSize(0, SizeHelper.fromPxWidth(context, 22));
        button.setTextColor(-1);
        linearLayout2.addView(button);
        View view3 = new View(context);
        view3.setLayoutParams(new LinearLayout.LayoutParams(SizeHelper.fromPxWidth(context, 1), -1));
        view3.setBackgroundColor(-9211021);
        linearLayout2.addView(view3);
        Button button2 = new Button(context);
        button2.setId(ResHelper.getIdRes(context, "btn_dialog_cancel"));
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(0, SizeHelper.fromPxWidth(context, 80), 1.0f);
        layoutParams4.rightMargin = SizeHelper.fromPxWidth(context, 1);
        button2.setLayoutParams(layoutParams4);
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
