package com.qmuiteam.qmui.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: classes2.dex */
public class QMUIStatusBarHelper {
    private static final int STATUSBAR_TYPE_ANDROID6 = 3;
    private static final int STATUSBAR_TYPE_DEFAULT = 0;
    private static final int STATUSBAR_TYPE_FLYME = 2;
    private static final int STATUSBAR_TYPE_MIUI = 1;
    private static final int STATUS_BAR_DEFAULT_HEIGHT_DP = 25;
    private static int mStatuBarType = 0;
    private static int sStatusbarHeight = -1;
    private static Integer sTransparentValue = null;
    public static float sVirtualDensity = -1.0f;
    public static float sVirtualDensityDpi = -1.0f;

    @TargetApi(23)
    private static boolean Android6SetStatusBarLightMode(Window window, boolean z) {
        window.getDecorView().setSystemUiVisibility(changeStatusBarModeRetainFlag(window, z ? 8192 : 256));
        if (!QMUIDeviceHelper.isMIUIV9()) {
            return true;
        }
        MIUISetStatusBarLightMode(window, z);
        return true;
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean z) {
        Android6SetStatusBarLightMode(window, z);
        if (window != null) {
            try {
                WindowManager.LayoutParams attributes = window.getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt(null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (i ^ (-1)) & i2);
                window.setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean z) {
        if (window != null) {
            Class<?> cls = window.getClass();
            try {
                Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
                Method method = cls.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
                if (z) {
                    method.invoke(window, Integer.valueOf(i), Integer.valueOf(i));
                } else {
                    method.invoke(window, 0, Integer.valueOf(i));
                }
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    @TargetApi(23)
    private static int changeStatusBarModeRetainFlag(Window window, int i) {
        return retainSystemUiFlag(window, retainSystemUiFlag(window, retainSystemUiFlag(window, retainSystemUiFlag(window, retainSystemUiFlag(window, retainSystemUiFlag(window, i, 1024), 4), 2), 4096), 1024), 512);
    }

    public static Integer getStatusBarAPITransparentValue(Context context) {
        if (sTransparentValue != null) {
            return sTransparentValue;
        }
        String str = null;
        for (String str2 : context.getPackageManager().getSystemSharedLibraryNames()) {
            if ("touchwiz".equals(str2)) {
                str = "SYSTEM_UI_FLAG_TRANSPARENT_BACKGROUND";
            } else if (str2.startsWith("com.sonyericsson.navigationbar")) {
                str = "SYSTEM_UI_FLAG_TRANSPARENT";
            }
        }
        if (str != null) {
            try {
                Field field = View.class.getField(str);
                if (field != null && field.getType() == Integer.TYPE) {
                    sTransparentValue = Integer.valueOf(field.getInt(null));
                }
            } catch (Exception unused) {
            }
        }
        return sTransparentValue;
    }

    public static int getStatusbarHeight(Context context) {
        if (sStatusbarHeight == -1) {
            initStatusBarHeight(context);
        }
        return sStatusbarHeight;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void initStatusBarHeight(android.content.Context r4) {
        /*
            r0 = 0
            java.lang.String r1 = "com.android.internal.R$dimen"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch: java.lang.Throwable -> L29
            java.lang.Object r2 = r1.newInstance()     // Catch: java.lang.Throwable -> L29
            boolean r3 = com.qmuiteam.qmui.util.QMUIDeviceHelper.isMeizu()     // Catch: java.lang.Throwable -> L27
            if (r3 == 0) goto L1d
            java.lang.String r3 = "status_bar_height_large"
            java.lang.reflect.Field r3 = r1.getField(r3)     // Catch: java.lang.Throwable -> L19
            r0 = r3
            goto L1d
        L19:
            r3 = move-exception
            r3.printStackTrace()     // Catch: java.lang.Throwable -> L27
        L1d:
            if (r0 != 0) goto L2e
            java.lang.String r3 = "status_bar_height"
            java.lang.reflect.Field r1 = r1.getField(r3)     // Catch: java.lang.Throwable -> L27
            r0 = r1
            goto L2e
        L27:
            r1 = move-exception
            goto L2b
        L29:
            r1 = move-exception
            r2 = r0
        L2b:
            r1.printStackTrace()
        L2e:
            if (r0 == 0) goto L4d
            if (r2 == 0) goto L4d
            java.lang.Object r0 = r0.get(r2)     // Catch: java.lang.Throwable -> L49
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L49
            int r0 = java.lang.Integer.parseInt(r0)     // Catch: java.lang.Throwable -> L49
            android.content.res.Resources r1 = r4.getResources()     // Catch: java.lang.Throwable -> L49
            int r0 = r1.getDimensionPixelSize(r0)     // Catch: java.lang.Throwable -> L49
            com.qmuiteam.qmui.util.QMUIStatusBarHelper.sStatusbarHeight = r0     // Catch: java.lang.Throwable -> L49
            goto L4d
        L49:
            r0 = move-exception
            r0.printStackTrace()
        L4d:
            boolean r0 = com.qmuiteam.qmui.util.QMUIDeviceHelper.isTablet(r4)
            r1 = 25
            if (r0 == 0) goto L61
            int r0 = com.qmuiteam.qmui.util.QMUIStatusBarHelper.sStatusbarHeight
            int r2 = com.qmuiteam.qmui.util.QMUIDisplayHelper.dp2px(r4, r1)
            if (r0 <= r2) goto L61
            r4 = 0
            com.qmuiteam.qmui.util.QMUIStatusBarHelper.sStatusbarHeight = r4
            goto L80
        L61:
            int r0 = com.qmuiteam.qmui.util.QMUIStatusBarHelper.sStatusbarHeight
            if (r0 > 0) goto L80
            float r0 = com.qmuiteam.qmui.util.QMUIStatusBarHelper.sVirtualDensity
            r2 = -1082130432(0xffffffffbf800000, float:-1.0)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L74
            int r4 = com.qmuiteam.qmui.util.QMUIDisplayHelper.dp2px(r4, r1)
            com.qmuiteam.qmui.util.QMUIStatusBarHelper.sStatusbarHeight = r4
            goto L80
        L74:
            r4 = 1103626240(0x41c80000, float:25.0)
            float r0 = com.qmuiteam.qmui.util.QMUIStatusBarHelper.sVirtualDensity
            float r0 = r0 * r4
            r4 = 1056964608(0x3f000000, float:0.5)
            float r0 = r0 + r4
            int r4 = (int) r0
            com.qmuiteam.qmui.util.QMUIStatusBarHelper.sStatusbarHeight = r4
        L80:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.qmuiteam.qmui.util.QMUIStatusBarHelper.initStatusBarHeight(android.content.Context):void");
    }

    public static boolean isFullScreen(Activity activity) {
        try {
            return (activity.getWindow().getAttributes().flags & 1024) != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isMIUICustomStatusBarLightModeImpl() {
        return (QMUIDeviceHelper.isMIUIV9() && Build.VERSION.SDK_INT < 23) || QMUIDeviceHelper.isMIUIV5() || QMUIDeviceHelper.isMIUIV6() || QMUIDeviceHelper.isMIUIV7() || QMUIDeviceHelper.isMIUIV8();
    }

    public static int retainSystemUiFlag(Window window, int i, int i2) {
        return (window.getDecorView().getSystemUiVisibility() & i2) == i2 ? i | i2 : i;
    }

    public static boolean setStatusBarDarkMode(Activity activity) {
        if (activity == null) {
            return false;
        }
        if (mStatuBarType == 0) {
            return true;
        }
        if (mStatuBarType == 1) {
            return MIUISetStatusBarLightMode(activity.getWindow(), false);
        }
        if (mStatuBarType == 2) {
            return FlymeSetStatusBarLightMode(activity.getWindow(), false);
        }
        if (mStatuBarType == 3) {
            return Android6SetStatusBarLightMode(activity.getWindow(), false);
        }
        return true;
    }

    public static boolean setStatusBarLightMode(Activity activity) {
        if (activity == null || QMUIDeviceHelper.isZTKC2016()) {
            return false;
        }
        if (mStatuBarType != 0) {
            return setStatusBarLightMode(activity, mStatuBarType);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (isMIUICustomStatusBarLightModeImpl() && MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                mStatuBarType = 1;
                return true;
            }
            if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                mStatuBarType = 2;
                return true;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                Android6SetStatusBarLightMode(activity.getWindow(), true);
                mStatuBarType = 3;
                return true;
            }
        }
        return false;
    }

    private static boolean setStatusBarLightMode(Activity activity, int i) {
        if (i == 1) {
            return MIUISetStatusBarLightMode(activity.getWindow(), true);
        }
        if (i == 2) {
            return FlymeSetStatusBarLightMode(activity.getWindow(), true);
        }
        if (i == 3) {
            return Android6SetStatusBarLightMode(activity.getWindow(), true);
        }
        return false;
    }

    public static void setVirtualDensity(float f) {
        sVirtualDensity = f;
    }

    public static void setVirtualDensityDpi(float f) {
        sVirtualDensityDpi = f;
    }

    public static boolean supportTransclentStatusBar6() {
        return (QMUIDeviceHelper.isZUKZ1() || QMUIDeviceHelper.isZTKC2016()) ? false : true;
    }

    private static boolean supportTranslucent() {
        return Build.VERSION.SDK_INT >= 19 && !Build.BRAND.toLowerCase().contains("essential");
    }

    public static void translucent(Activity activity) {
        translucent(activity, 1073741824);
    }

    @TargetApi(19)
    public static void translucent(Activity activity, @ColorInt int i) {
        if (supportTranslucent()) {
            if (QMUIDeviceHelper.isMeizu() || QMUIDeviceHelper.isMIUI()) {
                activity.getWindow().setFlags(67108864, 67108864);
                return;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = activity.getWindow();
                window.getDecorView().setSystemUiVisibility(1280);
                if (Build.VERSION.SDK_INT < 23 || !supportTransclentStatusBar6()) {
                    window.clearFlags(67108864);
                    window.addFlags(Integer.MIN_VALUE);
                    window.setStatusBarColor(i);
                } else {
                    window.clearFlags(67108864);
                    window.addFlags(Integer.MIN_VALUE);
                    window.setStatusBarColor(0);
                }
            }
        }
    }
}
