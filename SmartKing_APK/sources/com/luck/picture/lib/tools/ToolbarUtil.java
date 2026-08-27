package com.luck.picture.lib.tools;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/* loaded from: classes.dex */
public class ToolbarUtil {
    private static final int DEFAULT_STATUS_BAR_ALPHA = 15;

    private static int calculateStatusColor(int i, int i2) {
        float f = 1.0f - (i2 / 255.0f);
        double d = ((i >> 16) & 255) * f;
        Double.isNaN(d);
        int i3 = (int) (d + 0.5d);
        double d2 = ((i >> 8) & 255) * f;
        Double.isNaN(d2);
        double d3 = (i & 255) * f;
        Double.isNaN(d3);
        return ((int) (d3 + 0.5d)) | (i3 << 16) | (-16777216) | (((int) (d2 + 0.5d)) << 8);
    }

    private static View createStatusBarView(Activity activity, int i, int i2) {
        View view = new View(activity);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, getStatusBarHeight(activity)));
        view.setBackgroundColor(calculateStatusColor(i, i2));
        return view;
    }

    public static int getStatusBarHeight(Context context) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static void setColor(Activity activity, int i) {
        setColor(activity, i, 15);
    }

    public static void setColor(Activity activity, int i, int i2) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(Integer.MIN_VALUE);
            activity.getWindow().clearFlags(67108864);
            activity.getWindow().setStatusBarColor(calculateStatusColor(i, i2));
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            ((ViewGroup) activity.getWindow().getDecorView()).addView(createStatusBarView(activity, i, i2));
            setRootView(activity);
        }
    }

    public static void setColorNoTranslucent(Activity activity, int i) {
        setColor(activity, i, 0);
    }

    private static void setRootView(Activity activity) {
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) activity.findViewById(R.id.content)).getChildAt(0);
        if (viewGroup != null) {
            viewGroup.setFitsSystemWindows(true);
            viewGroup.setClipToPadding(true);
        }
    }
}
