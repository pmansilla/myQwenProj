package com.wx.wheelview.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Collection;

/* loaded from: classes2.dex */
public class WheelUtils {
    private static final String TAG = "WheelView";

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static TextView findTextView(View view) {
        if (view instanceof TextView) {
            return (TextView) view;
        }
        if (view instanceof ViewGroup) {
            return findTextView(((ViewGroup) view).getChildAt(0));
        }
        return null;
    }

    public static <V> boolean isEmpty(Collection<V> collection) {
        return collection == null || collection.size() == 0;
    }

    public static void log(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        Log.d(TAG, str);
    }

    public static int sp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}
