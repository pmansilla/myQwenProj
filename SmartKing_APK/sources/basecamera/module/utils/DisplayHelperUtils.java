package basecamera.module.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;
import basecamera.module.activity.DeviceHelperUtils;

/* loaded from: classes.dex */
public class DisplayHelperUtils {
    private static final String HUAWAI_DISPLAY_NOTCH_STATUS = "display_notch_status";
    private static final String VIVO_NAVIGATION_GESTURE = "navigation_gesture_on";
    private static final String XIAOMI_DISPLAY_NOTCH_STATUS = "force_black";
    private static final String XIAOMI_FULLSCREEN_GESTURE = "force_fsg_nav_bar";

    public static int dp2px(Context context, int i) {
        double density = getDensity(context) * i;
        Double.isNaN(density);
        return (int) (density + 0.5d);
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getStatusBarHeight(Context context) {
        if (DeviceHelperUtils.isXiaomi()) {
            int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (identifier > 0) {
                return context.getResources().getDimensionPixelSize(identifier);
            }
            return 0;
        }
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            int parseInt = Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString());
            if (parseInt > 0) {
                return context.getResources().getDimensionPixelSize(parseInt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean huaweiIsNotchSetToShowInSetting(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), HUAWAI_DISPLAY_NOTCH_STATUS, 0) == 0;
    }
}
