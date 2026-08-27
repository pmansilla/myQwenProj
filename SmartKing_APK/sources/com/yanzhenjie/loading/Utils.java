package com.yanzhenjie.loading;

import android.content.Context;

/* loaded from: classes2.dex */
public class Utils {
    public static float dip2px(Context context, float f) {
        double d = context.getResources().getDisplayMetrics().density;
        Double.isNaN(d);
        double d2 = f;
        Double.isNaN(d2);
        return (float) ((d + 0.5d) * d2);
    }

    public static float px2dip(Context context, int i) {
        double d = i;
        double d2 = context.getResources().getDisplayMetrics().density;
        Double.isNaN(d2);
        Double.isNaN(d);
        return (float) (d / (d2 + 0.5d));
    }
}
