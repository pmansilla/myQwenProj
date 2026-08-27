package cn.smssdk.gui.layout;

import android.content.Context;
import com.mob.tools.utils.ResHelper;

/* loaded from: classes.dex */
public class SizeHelper {
    public static final float DESIGNED_DENSITY = 1.5f;
    public static final int DESIGNED_SCREEN_WIDTH = 540;

    public static int fromDp(Context context, int i) {
        return ResHelper.designToDevice(context, 1.5f, ResHelper.dipToPx(context, i));
    }

    public static int fromPx(Context context, int i) {
        return ResHelper.designToDevice(context, 1.5f, i);
    }

    public static int fromPxWidth(Context context, int i) {
        return ResHelper.designToDevice(context, DESIGNED_SCREEN_WIDTH, i);
    }
}
