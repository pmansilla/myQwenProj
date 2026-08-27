package basecamera.module.utils;

import android.content.Context;

/* loaded from: classes.dex */
public class DistanceUtil {
    public static int dp2px(Context context, int i) {
        return DisplayHelperUtils.dp2px(context, i);
    }

    public static int getActivityHeight(Context context) {
        return (DisplayHelperUtils.getScreenWidth(context) - dp2px(context, 24)) / 3;
    }

    public static int getCameraAlbumWidth(Context context) {
        return ((DisplayHelperUtils.getScreenWidth(context) - dp2px(context, 10)) / 4) - dp2px(context, 4);
    }

    public static int getCameraPhotoAreaHeight(Context context) {
        return getCameraPhotoWidth(context) + dp2px(context, 4);
    }

    public static int getCameraPhotoWidth(Context context) {
        return (DisplayHelperUtils.getScreenWidth(context) / 4) - dp2px(context, 2);
    }
}
