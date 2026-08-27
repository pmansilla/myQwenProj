package basecamera.module.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import basecamera.module.utils.DisplayHelperUtils;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
class NotchHelperUtils {
    private static final String MIUI_NOTCH = "ro.miui.notch";
    private static final int NOTCH_IN_SCREEN_VOIO = 32;
    private static final String TAG = "NotchHelperUtils";
    private static Boolean sHasNotch;
    private static Boolean sHuaweiIsNotchSetToShow;
    private static int[] sNotchSizeInHawei;
    private static Rect sRotation0SafeInset;
    private static Rect sRotation180SafeInset;
    private static Rect sRotation270SafeInset;
    private static Rect sRotation90SafeInset;

    NotchHelperUtils() {
    }

    @TargetApi(28)
    private static boolean attachHasOfficialNotch(View view) {
        WindowInsets rootWindowInsets = view.getRootWindowInsets();
        if (rootWindowInsets == null) {
            return false;
        }
        sHasNotch = Boolean.valueOf(rootWindowInsets.getDisplayCutout() != null);
        return true;
    }

    private static void clearAllRectInfo() {
        sRotation0SafeInset = null;
        sRotation90SafeInset = null;
        sRotation180SafeInset = null;
        sRotation270SafeInset = null;
    }

    private static void clearLandscapeRectInfo() {
        sRotation90SafeInset = null;
        sRotation270SafeInset = null;
    }

    private static void clearPortraitRectInfo() {
        sRotation0SafeInset = null;
        sRotation180SafeInset = null;
    }

    private static Rect get3rdSafeInsetRect(Context context) {
        if (DeviceHelperUtils.isHuawei()) {
            boolean huaweiIsNotchSetToShowInSetting = DisplayHelperUtils.huaweiIsNotchSetToShowInSetting(context);
            if (sHuaweiIsNotchSetToShow != null && sHuaweiIsNotchSetToShow.booleanValue() != huaweiIsNotchSetToShowInSetting) {
                clearLandscapeRectInfo();
            }
            sHuaweiIsNotchSetToShow = Boolean.valueOf(huaweiIsNotchSetToShowInSetting);
        }
        int screenRotation = getScreenRotation(context);
        if (screenRotation == 1) {
            if (sRotation90SafeInset == null) {
                sRotation90SafeInset = getRectInfoRotation90(context);
            }
            return sRotation90SafeInset;
        }
        if (screenRotation == 2) {
            if (sRotation180SafeInset == null) {
                sRotation180SafeInset = getRectInfoRotation180(context);
            }
            return sRotation180SafeInset;
        }
        if (screenRotation == 3) {
            if (sRotation270SafeInset == null) {
                sRotation270SafeInset = getRectInfoRotation270(context);
            }
            return sRotation270SafeInset;
        }
        if (sRotation0SafeInset == null) {
            sRotation0SafeInset = getRectInfoRotation0(context);
        }
        return sRotation0SafeInset;
    }

    public static int getNotchHeightInVivo(Context context) {
        return DisplayHelperUtils.dp2px(context, 27);
    }

    public static int getNotchHeightInXiaomi(Context context) {
        int identifier = context.getResources().getIdentifier("notch_height", "dimen", "android");
        return identifier > 0 ? context.getResources().getDimensionPixelSize(identifier) : DisplayHelperUtils.getStatusBarHeight(context);
    }

    public static int[] getNotchSizeInHuawei(Context context) {
        if (sNotchSizeInHawei == null) {
            sNotchSizeInHawei = new int[]{0, 0};
            try {
                Class<?> loadClass = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
                sNotchSizeInHawei = (int[]) loadClass.getMethod("getNotchSize", new Class[0]).invoke(loadClass, new Object[0]);
            } catch (ClassNotFoundException unused) {
                Log.e(TAG, "getNotchSizeInHuawei ClassNotFoundException");
            } catch (NoSuchMethodException unused2) {
                Log.e(TAG, "getNotchSizeInHuawei NoSuchMethodException");
            } catch (Exception unused3) {
                Log.e(TAG, "getNotchSizeInHuawei Exception");
            }
        }
        return sNotchSizeInHawei;
    }

    public static int getNotchWidthInVivo(Context context) {
        return DisplayHelperUtils.dp2px(context, 100);
    }

    public static int getNotchWidthInXiaomi(Context context) {
        int identifier = context.getResources().getIdentifier("notch_width", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return -1;
    }

    @TargetApi(28)
    private static void getOfficialSafeInsetRect(View view, Rect rect) {
        WindowInsets rootWindowInsets;
        DisplayCutout displayCutout;
        if (view == null || (rootWindowInsets = view.getRootWindowInsets()) == null || (displayCutout = rootWindowInsets.getDisplayCutout()) == null) {
            return;
        }
        rect.set(displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom());
    }

    private static Rect getRectInfoRotation0(Context context) {
        Rect rect = new Rect();
        if (DeviceHelperUtils.isVivo()) {
            rect.top = getNotchHeightInVivo(context);
            rect.bottom = 0;
        } else if (DeviceHelperUtils.isOppo()) {
            rect.top = StatusBarHelperUtils.getStatusbarHeight(context);
            rect.bottom = 0;
        } else if (DeviceHelperUtils.isHuawei()) {
            rect.top = getNotchSizeInHuawei(context)[1];
            rect.bottom = 0;
        } else if (DeviceHelperUtils.isXiaomi()) {
            rect.top = getNotchHeightInXiaomi(context);
            rect.bottom = 0;
        }
        return rect;
    }

    private static Rect getRectInfoRotation180(Context context) {
        Rect rect = new Rect();
        if (DeviceHelperUtils.isVivo()) {
            rect.top = 0;
            rect.bottom = getNotchHeightInVivo(context);
        } else if (DeviceHelperUtils.isOppo()) {
            rect.top = 0;
            rect.bottom = StatusBarHelperUtils.getStatusbarHeight(context);
        } else if (DeviceHelperUtils.isHuawei()) {
            int[] notchSizeInHuawei = getNotchSizeInHuawei(context);
            rect.top = 0;
            rect.bottom = notchSizeInHuawei[1];
        } else if (DeviceHelperUtils.isXiaomi()) {
            rect.top = 0;
            rect.bottom = getNotchHeightInXiaomi(context);
        }
        return rect;
    }

    private static Rect getRectInfoRotation270(Context context) {
        Rect rect = new Rect();
        if (DeviceHelperUtils.isVivo()) {
            rect.right = getNotchHeightInVivo(context);
            rect.left = 0;
        } else if (DeviceHelperUtils.isOppo()) {
            rect.right = StatusBarHelperUtils.getStatusbarHeight(context);
            rect.left = 0;
        } else if (DeviceHelperUtils.isHuawei()) {
            if (sHuaweiIsNotchSetToShow.booleanValue()) {
                rect.right = getNotchSizeInHuawei(context)[1];
            } else {
                rect.right = 0;
            }
            rect.left = 0;
        } else if (DeviceHelperUtils.isXiaomi()) {
            rect.right = getNotchHeightInXiaomi(context);
            rect.left = 0;
        }
        return rect;
    }

    private static Rect getRectInfoRotation90(Context context) {
        Rect rect = new Rect();
        if (DeviceHelperUtils.isVivo()) {
            rect.left = getNotchHeightInVivo(context);
            rect.right = 0;
        } else if (DeviceHelperUtils.isOppo()) {
            rect.left = StatusBarHelperUtils.getStatusbarHeight(context);
            rect.right = 0;
        } else if (DeviceHelperUtils.isHuawei()) {
            if (sHuaweiIsNotchSetToShow.booleanValue()) {
                rect.left = getNotchSizeInHuawei(context)[1];
            } else {
                rect.left = 0;
            }
            rect.right = 0;
        } else if (DeviceHelperUtils.isXiaomi()) {
            rect.left = getNotchHeightInXiaomi(context);
            rect.right = 0;
        }
        return rect;
    }

    public static int getSafeInsetBottom(Activity activity) {
        if (hasNotch(activity)) {
            return getSafeInsetRect(activity).bottom;
        }
        return 0;
    }

    public static int getSafeInsetBottom(View view) {
        if (hasNotch(view)) {
            return getSafeInsetRect(view).bottom;
        }
        return 0;
    }

    public static int getSafeInsetLeft(Activity activity) {
        if (hasNotch(activity)) {
            return getSafeInsetRect(activity).left;
        }
        return 0;
    }

    public static int getSafeInsetLeft(View view) {
        if (hasNotch(view)) {
            return getSafeInsetRect(view).left;
        }
        return 0;
    }

    private static Rect getSafeInsetRect(Activity activity) {
        if (!isNotchOfficialSupport()) {
            return get3rdSafeInsetRect(activity);
        }
        Rect rect = new Rect();
        getOfficialSafeInsetRect(activity.getWindow().getDecorView(), rect);
        return rect;
    }

    private static Rect getSafeInsetRect(View view) {
        if (!isNotchOfficialSupport()) {
            return get3rdSafeInsetRect(view.getContext());
        }
        Rect rect = new Rect();
        getOfficialSafeInsetRect(view, rect);
        return rect;
    }

    public static int getSafeInsetRight(Activity activity) {
        if (hasNotch(activity)) {
            return getSafeInsetRect(activity).right;
        }
        return 0;
    }

    public static int getSafeInsetRight(View view) {
        if (hasNotch(view)) {
            return getSafeInsetRect(view).right;
        }
        return 0;
    }

    public static int getSafeInsetTop(Activity activity) {
        if (hasNotch(activity)) {
            return getSafeInsetRect(activity).top;
        }
        return 0;
    }

    public static int getSafeInsetTop(View view) {
        if (hasNotch(view)) {
            return getSafeInsetRect(view).top;
        }
        return 0;
    }

    private static int getScreenRotation(Context context) {
        Display defaultDisplay;
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null || (defaultDisplay = windowManager.getDefaultDisplay()) == null) {
            return 0;
        }
        return defaultDisplay.getRotation();
    }

    public static boolean has3rdNotch(Context context) {
        if (DeviceHelperUtils.isHuawei()) {
            return hasNotchInHuawei(context);
        }
        if (DeviceHelperUtils.isVivo()) {
            return hasNotchInVivo(context);
        }
        if (DeviceHelperUtils.isOppo()) {
            return hasNotchInOppo(context);
        }
        if (DeviceHelperUtils.isXiaomi()) {
            return hasNotchInXiaomi(context);
        }
        return false;
    }

    public static boolean hasNotch(Activity activity) {
        View decorView;
        if (sHasNotch == null) {
            if (isNotchOfficialSupport()) {
                Window window = activity.getWindow();
                if (window == null || (decorView = window.getDecorView()) == null || !attachHasOfficialNotch(decorView)) {
                    return false;
                }
            } else {
                sHasNotch = Boolean.valueOf(has3rdNotch(activity));
            }
        }
        return sHasNotch.booleanValue();
    }

    public static boolean hasNotch(View view) {
        if (sHasNotch == null) {
            if (!isNotchOfficialSupport()) {
                sHasNotch = Boolean.valueOf(has3rdNotch(view.getContext()));
            } else if (!attachHasOfficialNotch(view)) {
                return false;
            }
        }
        return sHasNotch.booleanValue();
    }

    public static boolean hasNotchInHuawei(Context context) {
        try {
            Class<?> loadClass = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            return ((Boolean) loadClass.getMethod("hasNotchInScreen", new Class[0]).invoke(loadClass, new Object[0])).booleanValue();
        } catch (ClassNotFoundException unused) {
            Log.i(TAG, "hasNotchInHuawei ClassNotFoundException");
            return false;
        } catch (NoSuchMethodException unused2) {
            Log.e(TAG, "hasNotchInHuawei NoSuchMethodException");
            return false;
        } catch (Exception unused3) {
            Log.e(TAG, "hasNotchInHuawei Exception");
            return false;
        }
    }

    public static boolean hasNotchInOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static boolean hasNotchInVivo(Context context) {
        try {
            Class<?> loadClass = context.getClassLoader().loadClass("android.util.FtFeature");
            Method[] declaredMethods = loadClass.getDeclaredMethods();
            if (declaredMethods == null) {
                return false;
            }
            for (Method method : declaredMethods) {
                if (method.getName().equalsIgnoreCase("isFeatureSupport")) {
                    return ((Boolean) method.invoke(loadClass, 32)).booleanValue();
                }
            }
            return false;
        } catch (ClassNotFoundException unused) {
            Log.i(TAG, "hasNotchInVivo ClassNotFoundException");
            return false;
        } catch (Exception unused2) {
            Log.e(TAG, "hasNotchInVivo Exception");
            return false;
        }
    }

    @SuppressLint({"PrivateApi"})
    public static boolean hasNotchInXiaomi(Context context) {
        try {
            Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("getInt", String.class, Integer.TYPE);
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(null, MIUI_NOTCH, 0)).intValue() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isNotchOfficialSupport() {
        return Build.VERSION.SDK_INT >= 28;
    }

    public static boolean needFixLandscapeNotchAreaFitSystemWindow(View view) {
        return DeviceHelperUtils.isXiaomi() && hasNotch(view);
    }
}
