package cn.smssdk.gui.layout;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/* loaded from: classes.dex */
public class DrawableHelper {
    public static Drawable createCornerBg(Context context) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, createCornerBgPressed(context));
        stateListDrawable.addState(new int[0], createCornerBgNormal(context));
        return stateListDrawable;
    }

    public static Drawable createCornerBgNormal(Context context) {
        int fromPxWidth = SizeHelper.fromPxWidth(context, 1);
        int fromPxWidth2 = SizeHelper.fromPxWidth(context, 6);
        int parseColor = Color.parseColor("#ffc9c9cb");
        int parseColor2 = Color.parseColor("#ffffffff");
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(parseColor2);
        gradientDrawable.setCornerRadius(fromPxWidth2);
        gradientDrawable.setStroke(fromPxWidth, parseColor);
        return gradientDrawable;
    }

    public static Drawable createCornerBgPressed(Context context) {
        int fromPxWidth = SizeHelper.fromPxWidth(context, 1);
        int fromPxWidth2 = SizeHelper.fromPxWidth(context, 6);
        int parseColor = Color.parseColor("#ffc9c9cb");
        int parseColor2 = Color.parseColor("#afc9c9cb");
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(parseColor2);
        gradientDrawable.setCornerRadius(fromPxWidth2);
        gradientDrawable.setStroke(fromPxWidth, parseColor);
        return gradientDrawable;
    }
}
