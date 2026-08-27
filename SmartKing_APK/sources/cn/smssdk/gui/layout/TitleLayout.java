package cn.smssdk.gui.layout;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class TitleLayout {
    static final int IN_HEIGHT = 74;
    static final int LINE_HEIGHT = 2;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LinearLayout create(Context context, String str) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(1);
        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, SizeHelper.fromPxWidth(context, 1)));
        view.setBackgroundColor(-12236213);
        linearLayout.addView(view);
        if (TextUtils.isEmpty(str)) {
            createBackLeft(linearLayout, context);
        } else {
            createStringLeft(linearLayout, context, str);
        }
        return linearLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LinearLayout create(Context context, boolean z) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(1);
        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, SizeHelper.fromPxWidth(context, 1)));
        view.setBackgroundColor(-12236213);
        linearLayout.addView(view);
        if (z) {
            createSearch(linearLayout, context);
        } else {
            createNormal(linearLayout, context);
        }
        View view2 = new View(context);
        view2.setLayoutParams(new LinearLayout.LayoutParams(-1, SizeHelper.fromPxWidth(context, 2)));
        view2.setBackgroundColor(-15066083);
        linearLayout.addView(view2);
        return linearLayout;
    }

    private static void createBackLeft(LinearLayout linearLayout, Context context) {
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, SizeHelper.fromPx(context, 74));
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setLayoutParams(layoutParams);
        linearLayout2.setBackgroundColor(-1);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -1);
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setLayoutParams(layoutParams2);
        linearLayout3.setId(ResHelper.getIdRes(context, "ll_back"));
        linearLayout3.setPadding(SizeHelper.fromPx(context, 14), 0, SizeHelper.fromPx(context, 26), 0);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(SizeHelper.fromPx(context, 40), SizeHelper.fromPx(context, 40));
        layoutParams3.gravity = 16;
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams3);
        imageView.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_back_arrow"));
        linearLayout3.addView(imageView);
        linearLayout2.addView(linearLayout3);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(0, -2);
        layoutParams4.weight = 1.0f;
        layoutParams4.gravity = 17;
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams4);
        textView.setGravity(17);
        textView.setId(ResHelper.getIdRes(context, "tv_title"));
        textView.setTextColor(-16777216);
        textView.setTextSize(0, SizeHelper.fromPx(context, 32));
        linearLayout2.addView(textView);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-2, -1);
        layoutParams5.gravity = 17;
        layoutParams5.setMargins(0, 0, ResHelper.dipToPx(context, 10), 0);
        TextView textView2 = new TextView(context);
        textView2.setLayoutParams(layoutParams5);
        textView2.setId(ResHelper.getIdRes(context, "tv_right"));
        textView2.setGravity(17);
        textView2.setMinWidth(ResHelper.dipToPx(context, 40));
        textView2.setTextColor(-16722276);
        textView2.setTextSize(0, SizeHelper.fromPx(context, 24));
        linearLayout2.addView(textView2);
        linearLayout.addView(linearLayout2);
    }

    private static void createNormal(LinearLayout linearLayout, Context context) {
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, SizeHelper.fromPx(context, 74));
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setLayoutParams(layoutParams);
        linearLayout2.setBackgroundColor(-13617865);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -1);
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setLayoutParams(layoutParams2);
        linearLayout3.setId(ResHelper.getIdRes(context, "ll_back"));
        linearLayout3.setPadding(SizeHelper.fromPx(context, 14), 0, SizeHelper.fromPx(context, 26), 0);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(SizeHelper.fromPx(context, 15), SizeHelper.fromPx(context, 25));
        layoutParams3.gravity = 16;
        layoutParams3.rightMargin = SizeHelper.fromPx(context, 14);
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams3);
        imageView.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_nav_back"));
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(SizeHelper.fromPx(context, 30), SizeHelper.fromPx(context, 44));
        layoutParams4.gravity = 16;
        layoutParams4.rightMargin = SizeHelper.fromPx(context, 14);
        ImageView imageView2 = new ImageView(context);
        imageView2.setLayoutParams(layoutParams4);
        imageView2.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_sharesdk_icon"));
        linearLayout3.addView(imageView);
        linearLayout3.addView(imageView2);
        linearLayout2.addView(linearLayout3);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams5.gravity = 16;
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams5);
        textView.setId(ResHelper.getIdRes(context, "tv_title"));
        textView.setTextColor(-3158065);
        textView.setTextSize(0, SizeHelper.fromPx(context, 32));
        linearLayout2.addView(textView);
        linearLayout.addView(linearLayout2);
    }

    private static void createSearch(LinearLayout linearLayout, Context context) {
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, SizeHelper.fromPx(context, 74));
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setLayoutParams(layoutParams);
        linearLayout2.setBackgroundColor(-13617865);
        linearLayout2.setBaselineAligned(false);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -1);
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setLayoutParams(layoutParams2);
        linearLayout3.setId(ResHelper.getIdRes(context, "ll_back"));
        linearLayout3.setPadding(SizeHelper.fromPx(context, 14), 0, SizeHelper.fromPx(context, 26), 0);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(SizeHelper.fromPx(context, 15), SizeHelper.fromPx(context, 25));
        layoutParams3.gravity = 16;
        layoutParams3.rightMargin = SizeHelper.fromPxWidth(context, 14);
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams3);
        imageView.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_back_arrow"));
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(SizeHelper.fromPx(context, 30), SizeHelper.fromPx(context, 44));
        layoutParams4.gravity = 16;
        layoutParams4.rightMargin = SizeHelper.fromPx(context, 14);
        ImageView imageView2 = new ImageView(context);
        imageView2.setLayoutParams(layoutParams4);
        imageView2.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_sharesdk_icon"));
        linearLayout3.addView(imageView);
        linearLayout3.addView(imageView2);
        linearLayout2.addView(linearLayout3);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-2, -1, 1.0f);
        LinearLayout linearLayout4 = new LinearLayout(context);
        linearLayout4.setLayoutParams(layoutParams5);
        linearLayout4.setId(ResHelper.getIdRes(context, "llTitle"));
        linearLayout2.addView(linearLayout4);
        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams6.gravity = 16;
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams6);
        textView.setId(ResHelper.getIdRes(context, "tv_title"));
        textView.setText(ResHelper.getStringRes(context, "smssdk_choose_country"));
        textView.setTextColor(-3158065);
        textView.setTextSize(0, SizeHelper.fromPx(context, 32));
        linearLayout4.addView(textView);
        LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(SizeHelper.fromPx(context, 70), -2);
        layoutParams7.gravity = 16;
        ImageView imageView3 = new ImageView(context);
        imageView3.setLayoutParams(layoutParams7);
        imageView3.setId(ResHelper.getIdRes(context, "ivSearch"));
        imageView3.setImageResource(ResHelper.getBitmapRes(context, "smssdk_search_icon"));
        imageView3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView3.setPadding(SizeHelper.fromPx(context, 15), 0, SizeHelper.fromPx(context, 15), 0);
        linearLayout4.addView(imageView3);
        LinearLayout.LayoutParams layoutParams8 = new LinearLayout.LayoutParams(-2, -2, 1.0f);
        layoutParams8.gravity = 16;
        layoutParams8.rightMargin = SizeHelper.fromPx(context, 18);
        LinearLayout linearLayout5 = new LinearLayout(context);
        linearLayout5.setLayoutParams(layoutParams8);
        linearLayout5.setId(ResHelper.getIdRes(context, "llSearch"));
        linearLayout5.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_input_bg_focus"));
        linearLayout5.setPadding(SizeHelper.fromPx(context, 14), 0, SizeHelper.fromPx(context, 14), 0);
        linearLayout5.setVisibility(8);
        linearLayout2.addView(linearLayout5);
        LinearLayout.LayoutParams layoutParams9 = new LinearLayout.LayoutParams(SizeHelper.fromPx(context, 36), SizeHelper.fromPx(context, 36));
        layoutParams9.gravity = 16;
        layoutParams9.rightMargin = SizeHelper.fromPx(context, 8);
        ImageView imageView4 = new ImageView(context);
        imageView4.setLayoutParams(layoutParams9);
        imageView4.setImageResource(ResHelper.getBitmapRes(context, "smssdk_search_icon"));
        imageView4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        linearLayout5.addView(imageView4);
        LinearLayout.LayoutParams layoutParams10 = new LinearLayout.LayoutParams(0, -2, 1.0f);
        layoutParams10.gravity = 16;
        EditText editText = new EditText(context);
        editText.setLayoutParams(layoutParams10);
        editText.setId(ResHelper.getIdRes(context, "et_put_identify"));
        editText.setHint(ResHelper.getStringRes(context, "smssdk_search"));
        editText.setTextColor(-1);
        editText.setBackgroundDrawable(null);
        editText.setSingleLine(true);
        linearLayout5.addView(editText);
        LinearLayout.LayoutParams layoutParams11 = new LinearLayout.LayoutParams(SizeHelper.fromPx(context, 30), SizeHelper.fromPx(context, 30));
        layoutParams11.gravity = 16;
        layoutParams11.rightMargin = SizeHelper.fromPxWidth(context, 5);
        ImageView imageView5 = new ImageView(context);
        imageView5.setLayoutParams(layoutParams11);
        imageView5.setId(ResHelper.getIdRes(context, "iv_clear"));
        imageView5.setImageResource(ResHelper.getBitmapRes(context, "smssdk_clear_search"));
        imageView5.setScaleType(ImageView.ScaleType.FIT_CENTER);
        linearLayout5.addView(imageView5);
        linearLayout.addView(linearLayout2);
    }

    private static void createStringLeft(LinearLayout linearLayout, Context context, String str) {
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, SizeHelper.fromPx(context, 74));
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setLayoutParams(layoutParams);
        linearLayout2.setBackgroundColor(-1);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -1);
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setLayoutParams(layoutParams2);
        linearLayout3.setId(ResHelper.getIdRes(context, "ll_back"));
        linearLayout3.setPadding(SizeHelper.fromPx(context, 14), 0, SizeHelper.fromPx(context, 26), 0);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, -1);
        layoutParams3.gravity = 17;
        layoutParams3.setMargins(0, 0, ResHelper.dipToPx(context, 10), 0);
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams3);
        textView.setText(ResHelper.getStringRes(context, str));
        textView.setTextColor(-16777216);
        textView.setTextSize(0, SizeHelper.fromPx(context, 24));
        linearLayout3.addView(textView);
        linearLayout2.addView(linearLayout3);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(0, -2);
        layoutParams4.weight = 1.0f;
        layoutParams4.gravity = 17;
        TextView textView2 = new TextView(context);
        textView2.setLayoutParams(layoutParams4);
        textView2.setGravity(17);
        textView2.setId(ResHelper.getIdRes(context, "tv_title"));
        textView2.setTextColor(-16777216);
        textView2.setTextSize(0, SizeHelper.fromPx(context, 32));
        linearLayout2.addView(textView2);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(-2, -1);
        layoutParams5.gravity = 17;
        layoutParams5.setMargins(0, 0, ResHelper.dipToPx(context, 10), 0);
        TextView textView3 = new TextView(context);
        textView3.setLayoutParams(layoutParams5);
        textView3.setId(ResHelper.getIdRes(context, "tv_right"));
        textView3.setGravity(17);
        textView3.setMinWidth(ResHelper.dipToPx(context, 40));
        textView3.setTextColor(-16722276);
        textView3.setTextSize(0, SizeHelper.fromPx(context, 24));
        linearLayout2.addView(textView3);
        linearLayout.addView(linearLayout2);
    }
}
