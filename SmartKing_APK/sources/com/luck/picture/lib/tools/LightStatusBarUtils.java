package com.luck.picture.lib.tools;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class LightStatusBarUtils {
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean z) {
        View decorView = activity.getWindow().getDecorView();
        if (!z) {
            decorView.setSystemUiVisibility(0);
            return;
        }
        String str = Build.MODEL;
        if (TextUtils.isEmpty(str) || !str.startsWith("Le")) {
            decorView.setSystemUiVisibility(8192);
        }
    }

    private static boolean setFlymeLightStatusBar(Activity activity, boolean z) {
        if (activity != null) {
            try {
                WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt(null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (i ^ (-1)) & i2);
                activity.getWindow().setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static void setLightStatusBar(Activity activity, boolean z) {
        switch (RomUtils.getLightStatausBarAvailableRomType()) {
            case 1:
                if (Build.VERSION.SDK_INT <= 19) {
                    z = false;
                }
                setMIUILightStatusBar(activity, z);
                return;
            case 2:
                if (Build.VERSION.SDK_INT <= 19) {
                    z = false;
                }
                setFlymeLightStatusBar(activity, z);
                return;
            case 3:
                setAndroidNativeLightStatusBar(activity, z);
                return;
            case 4:
                setAndroidNativeLightStatusBar(activity, z);
                return;
            default:
                return;
        }
    }

    private static boolean setMIUILightStatusBar(Activity activity, boolean z) {
        Class<?> cls = activity.getWindow().getClass();
        try {
            Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
            Method method = cls.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
            Window window = activity.getWindow();
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(z ? i : 0);
            objArr[1] = Integer.valueOf(i);
            method.invoke(window, objArr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
