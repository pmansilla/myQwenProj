package com.qmuiteam.qmui.util;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;

/* loaded from: classes2.dex */
public class QMUIColorHelper {
    public static String colorToString(@ColorInt int i) {
        return String.format("#%08X", Integer.valueOf(i));
    }

    public static int computeColor(@ColorInt int i, @ColorInt int i2, float f) {
        float max = Math.max(Math.min(f, 1.0f), 0.0f);
        return Color.argb(((int) ((Color.alpha(i2) - r0) * max)) + Color.alpha(i), ((int) ((Color.red(i2) - r0) * max)) + Color.red(i), ((int) ((Color.green(i2) - r0) * max)) + Color.green(i), ((int) ((Color.blue(i2) - r4) * max)) + Color.blue(i));
    }

    public static int setColorAlpha(@ColorInt int i, float f) {
        return (i & ViewCompat.MEASURED_SIZE_MASK) | (((int) (f * 255.0f)) << 24);
    }
}
