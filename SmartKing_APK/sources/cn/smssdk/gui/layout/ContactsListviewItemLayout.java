package cn.smssdk.gui.layout;

import android.content.Context;
import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class ContactsListviewItemLayout {
    public static LinearLayout create(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setId(ResHelper.getIdRes(context, "rl_lv_item_bg"));
        linearLayout.setLayoutParams(new AbsListView.LayoutParams(-1, SizeHelper.fromPxWidth(context, 95)));
        int fromPxWidth = SizeHelper.fromPxWidth(context, 14);
        linearLayout.setPadding(fromPxWidth, fromPxWidth, fromPxWidth, fromPxWidth);
        linearLayout.setGravity(16);
        linearLayout.setBackgroundColor(-1);
        AsyncImageView asyncImageView = new AsyncImageView(context);
        asyncImageView.setId(ResHelper.getIdRes(context, "iv_contact"));
        int fromPxWidth2 = SizeHelper.fromPxWidth(context, 64);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(fromPxWidth2, fromPxWidth2);
        asyncImageView.setRound(fromPxWidth2 / 2);
        asyncImageView.setLayoutParams(layoutParams);
        asyncImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        linearLayout.addView(asyncImageView);
        LinearLayout linearLayout2 = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, -2);
        layoutParams2.weight = 1.0f;
        layoutParams2.leftMargin = SizeHelper.fromPxWidth(context, 12);
        linearLayout2.setLayoutParams(layoutParams2);
        linearLayout2.setOrientation(1);
        linearLayout.addView(linearLayout2);
        TextView textView = new TextView(context);
        textView.setId(ResHelper.getIdRes(context, "tv_name"));
        textView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        textView.setTextColor(-13421773);
        textView.setTextSize(0, SizeHelper.fromPxWidth(context, 28));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout2.addView(textView);
        TextView textView2 = new TextView(context);
        textView2.setId(ResHelper.getIdRes(context, "tv_contact"));
        textView2.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        textView2.setTextColor(-6710887);
        textView2.setTextSize(0, SizeHelper.fromPxWidth(context, 22));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout2.addView(textView2);
        Button button = new Button(context);
        button.setId(ResHelper.getIdRes(context, "btn_add"));
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(SizeHelper.fromPxWidth(context, 100), SizeHelper.fromPxWidth(context, 46));
        layoutParams3.leftMargin = ResHelper.dipToPx(context, 5);
        button.setLayoutParams(layoutParams3);
        button.setText(ResHelper.getStringRes(context, "smssdk_add_contact"));
        button.setTextSize(0, SizeHelper.fromPxWidth(context, 22));
        button.setTextColor(-1);
        button.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_btn_enable"));
        button.setPadding(0, 0, 0, 0);
        linearLayout.addView(button);
        return linearLayout;
    }
}
