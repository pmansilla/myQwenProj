package cn.smssdk.gui.layout;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class ContactDetailPageLayout extends BasePageLayout {
    public ContactDetailPageLayout(Context context) {
        super(context, (String) null);
    }

    @Override // cn.smssdk.gui.layout.BasePageLayout
    protected void onCreateContent(LinearLayout linearLayout) {
        TextView textView = new TextView(this.context);
        textView.setId(ResHelper.getIdRes(this.context, "tv_contact_name"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(SizeHelper.fromPxWidth(this.context, 26), SizeHelper.fromPxWidth(this.context, 60), SizeHelper.fromPxWidth(this.context, 26), 0);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(17);
        textView.setTextColor(this.context.getResources().getColor(ResHelper.getColorRes(this.context, "smssdk_main_color")));
        textView.setTextSize(0, SizeHelper.fromPxWidth(this.context, 52));
        linearLayout.addView(textView);
        LinearLayout linearLayout2 = new LinearLayout(this.context);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams2.setMargins(SizeHelper.fromPxWidth(this.context, 26), SizeHelper.fromPxWidth(this.context, 60), SizeHelper.fromPxWidth(this.context, 26), 0);
        linearLayout2.setLayoutParams(layoutParams2);
        linearLayout2.setOrientation(0);
        linearLayout.addView(linearLayout2);
        int dipToPx = ResHelper.dipToPx(this.context, 80);
        int dipToPx2 = ResHelper.dipToPx(this.context, 14);
        int color = this.context.getResources().getColor(ResHelper.getColorRes(this.context, "smssdk_black"));
        TextView textView2 = new TextView(this.context);
        textView2.setLayoutParams(new LinearLayout.LayoutParams(dipToPx, -2));
        textView2.setText(ResHelper.getStringRes(this.context, "smssdk_label_phone"));
        textView2.setTextColor(color);
        float f = dipToPx2;
        textView2.setTextSize(0, f);
        linearLayout2.addView(textView2);
        TextView textView3 = new TextView(this.context);
        textView3.setId(ResHelper.getIdRes(this.context, "tv_phone"));
        textView3.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        textView3.setGravity(5);
        textView3.setTextColor(this.context.getResources().getColor(ResHelper.getColorRes(this.context, "smssdk_tv_light_gray")));
        textView3.setTextSize(0, f);
        linearLayout2.addView(textView3);
        View view = new View(this.context);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, ResHelper.dipToPx(this.context, 1));
        layoutParams3.setMargins(SizeHelper.fromPxWidth(this.context, 26), SizeHelper.fromPxWidth(this.context, 10), SizeHelper.fromPxWidth(this.context, 26), 0);
        view.setLayoutParams(layoutParams3);
        view.setBackgroundResource(ResHelper.getColorRes(this.context, "smssdk_line_light_gray"));
        linearLayout.addView(view);
        LinearLayout linearLayout3 = new LinearLayout(this.context);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-1, -2);
        layoutParams4.setMargins(SizeHelper.fromPxWidth(this.context, 26), SizeHelper.fromPxWidth(this.context, 22), SizeHelper.fromPxWidth(this.context, 26), 0);
        linearLayout3.setId(ResHelper.getIdRes(this.context, "ll_phone2"));
        linearLayout3.setLayoutParams(layoutParams4);
        linearLayout3.setOrientation(0);
        linearLayout3.setVisibility(8);
        linearLayout.addView(linearLayout3);
        TextView textView4 = new TextView(this.context);
        textView4.setLayoutParams(new LinearLayout.LayoutParams(dipToPx, -2));
        textView4.setText(ResHelper.getStringRes(this.context, "smssdk_label_phone2"));
        textView4.setTextColor(color);
        textView4.setTextSize(0, f);
        linearLayout3.addView(textView4);
        TextView textView5 = new TextView(this.context);
        textView5.setId(ResHelper.getIdRes(this.context, "tv_phone2"));
        textView5.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        textView5.setGravity(5);
        textView5.setTextColor(this.context.getResources().getColor(ResHelper.getColorRes(this.context, "smssdk_tv_light_gray")));
        textView5.setTextSize(0, f);
        linearLayout3.addView(textView5);
        View view2 = new View(this.context);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-1, ResHelper.dipToPx(this.context, 1));
        layoutParams5.setMargins(SizeHelper.fromPxWidth(this.context, 26), SizeHelper.fromPxWidth(this.context, 10), SizeHelper.fromPxWidth(this.context, 26), 0);
        view2.setLayoutParams(layoutParams5);
        view2.setId(ResHelper.getIdRes(this.context, "vw_divider2"));
        view2.setBackgroundResource(ResHelper.getColorRes(this.context, "smssdk_line_light_gray"));
        view2.setVisibility(8);
        linearLayout.addView(view2);
        Button button = new Button(this.context);
        button.setId(ResHelper.getIdRes(this.context, "btn_invite"));
        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(-1, SizeHelper.fromPxWidth(this.context, 72));
        layoutParams6.setMargins(SizeHelper.fromPxWidth(this.context, 26), SizeHelper.fromPxWidth(this.context, 40), SizeHelper.fromPxWidth(this.context, 26), 0);
        button.setLayoutParams(layoutParams6);
        button.setBackgroundResource(ResHelper.getBitmapRes(this.context, "smssdk_btn_enable"));
        button.setText(ResHelper.getStringRes(this.context, "smssdk_send_invitation"));
        button.setTextColor(-1);
        button.setTextSize(0, SizeHelper.fromPxWidth(this.context, 28));
        button.setPadding(0, 0, 0, 0);
        linearLayout.addView(button);
    }
}
