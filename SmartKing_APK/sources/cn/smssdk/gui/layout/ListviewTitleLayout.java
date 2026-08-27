package cn.smssdk.gui.layout;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class ListviewTitleLayout {
    static RelativeLayout create(Context context) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        TextView textView = new TextView(context);
        textView.setId(ResHelper.getIdRes(context, "tv_title"));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, SizeHelper.fromPxWidth(context, 40));
        layoutParams.topMargin = SizeHelper.fromPxWidth(context, -20);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(SizeHelper.fromPxWidth(context, 20), 0, 0, 0);
        textView.setLineSpacing(SizeHelper.fromPxWidth(context, 8), 1.0f);
        textView.setText(ResHelper.getStringRes(context, "smssdk_regist"));
        textView.setTextColor(-6710887);
        textView.setTextSize(0, SizeHelper.fromPxWidth(context, 26));
        textView.setGravity(16);
        textView.setBackgroundColor(-1382162);
        relativeLayout.addView(textView);
        return relativeLayout;
    }
}
